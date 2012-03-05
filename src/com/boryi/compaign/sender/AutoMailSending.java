package com.boryi.compaign.sender;

import org.apache.log4j.*;
import com.sun.mail.smtp.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.lang.Thread;
import java.sql.SQLException;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.log4j.xml.DOMConfigurator;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author yang.song
 */
public class AutoMailSending {
    private Date runDate;
    private Dao dao;
    private Config config;
    
    private Hashtable<Integer, EmailType> types;

    private Hashtable<String, List<Invitee>> list = new Hashtable<String, List<Invitee>>();
    
    private Vector<Integer> checkList = new Vector<Integer>();
    
    private Hashtable<Integer, String> domains;
    private Hashtable<String, Date> domainTimers = new Hashtable<String, Date>();
    
    private static Logger eventLogger = Logger.getLogger("EventLogger");

    static 
    {
        DOMConfigurator.configure("log4j.xml");
    }
    
    public static void main(String[] args) 
    {
        AutoMailSending c = new AutoMailSending();
        c.OnRun();
    }

    /*
     * Collect Invitees by domain types - if it displays images
     */
    private void CollectInvitees(Boolean displayImage)
            throws ClassNotFoundException, SQLException 
    {
        this.domains = dao.GetDomains(displayImage);

        for(Iterator<Integer> iter = domains.keySet().iterator(); iter.hasNext();)
        {
            // domain Id
            Integer domainId = iter.next();
            String domain = domains.get(domainId);
            
            int i = 0;
            for (Iterator<Integer> itr = types.keySet().iterator(); itr.hasNext();i++)
            {
                Integer typeId = itr.next();
                
                // getEmailList
                List<Invitee> invitees = dao.GetEmailList(
                    typeId, domainId, config.getStartId(), config.getEndId(), 
                    this.checkList, displayImage);

                if (invitees.size()>0)
                {
                    // collect the invitee
                    if (list.containsKey(domain))
                    {
                        list.get(domain).addAll(invitees);
                    }
                    else
                    {
                        list.put(domain, invitees);
                    }
                }
            }   
        }
    }
    
    /*
     * Initilization
     */
    private void Init() 
    {
        runDate = new Date();

        try
        {
            config = new Config();
            config.read("config.xml");

            dao = new Dao(config.getConnection());
        }
        catch (Exception ex)
        {
            eventLogger.error(ex);
        }
    }

    private void SendEmail(Invitee email)
            throws MessagingException, NoSuchProviderException {

        EmailType emailType = this.types.get(email.getTypeId()); // type id vs index

        String subject = emailType.getSubject();
        
        String format = email.getDisplayImage() ? 
                emailType.getEmailBodyWithImg() : emailType.getEmailBodyWithoutImg();
        
        Properties props = new Properties();
        
        props.put("mail.smtp.starttls.enable", "true"); // added this line
        props.put("mail.smtp.host", config.getMailHost());
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.auth", "true");
        
        
        Session mailSession = Session.getDefaultInstance(props,
            new javax.mail.Authenticator() {
            @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(
                                    config.getMailUser(),
                                    config.getMailPassword());
                    }
            });
        
        Transport transport = mailSession.getTransport("smtp");

        MimeMessage message = new MimeMessage(mailSession);
        
        message.setSubject(subject);
        message.setFrom(new InternetAddress(email.getInviter_email()));
        message.setContent(format, "text/html");
        message.addRecipient(Message.RecipientType.TO,
             new InternetAddress(email.getEmail()));

        transport.connect();
        transport.sendMessage(message,
            message.getRecipients(Message.RecipientType.TO));
        transport.close();
    }

    /// <summary>
    /// program entry point
    /// </summary>
    public void OnRun()
    {
        try
        {
            Init();
        }
        catch(Exception ex)
        {
            eventLogger.error(ex);
        }
        
        try
        {
            this.types = this.config.getTypes();
            
//            this.checkList = dao.CheckLastRun();
            
//            if (checkList.size()>0)
//            {
//                Vector<Integer> checkListLastRun = checkList;
//                
//                checkList = new Vector<Integer>();
//                
//                CollectInvitees(true);
//                CollectInvitees(false);
//                
//                // lasttime missing some mails
//                RemoveSearched(checkListLastRun);
//            }
//            else
//            {
                dao.CleanChecklist();
                
                CollectInvitees(true);
                CollectInvitees(false);
                
                for(Integer id : this.checkList)
                {
                    dao.BuildChecklist(id);
                }
//            }
            
            SendEmails();
        }
        catch(Exception ex)
        {
            eventLogger.warn(ex);
        }
    }
    
    
    private void RemoveSearched(Vector<Integer> checkListLastRun)
    {
        for (Iterator<String> itr = list.keySet().iterator(); itr.hasNext();)
        {
            String key = itr.next();
            List<Invitee> invitees = list.get(key);
            
            List<Invitee> newInvitees = new ArrayList<Invitee>();
            
            for (Invitee invitee: invitees)
            {
                if (checkListLastRun.contains(invitee.getId()))
                {
                    newInvitees.add(invitee);
                }
            }
            
            list.remove(key);
            
            if(newInvitees.size() > 0)
            {
                list.put(key, newInvitees);
            }
        }
        
        checkList = checkListLastRun;
    }
    
    private void SendEmails() throws InterruptedException
    {
        int i = 0;
        
        while (!list.isEmpty())
        {
            for (Iterator<String> itr = list.keySet().iterator(); itr.hasNext(); i++)
            {
                String domain = itr.next();
                
                System.out.println("Checking: " + domain);

                List<Invitee> emailList = list.get(domain);
                
                if (emailList == null || emailList.isEmpty())
                {
                    list.remove(domain);
                    domainTimers.remove(domain);
                    System.out.println("Remove: " + domain);
                    
                    // list modified
                    break;
                }
                else
                {
                    // ready to send email
                    Date startTime = null;
                    if (domainTimers.get(domain) != null)
                    {
                        startTime = domainTimers.get(domain);
                        Date now = new Date();
                        long diff = config.getInterval() - (now.getTime() - startTime.getTime());

                        if (diff > 0)
                        {
                            System.out.println("Wait for: " + diff);
                            continue; 
                        }
                    }

                    Invitee invitee = emailList.remove(0);    

                    System.out.println("Get invitee: " + invitee);
                    
                    try 
                    {
                        SendEmail(invitee);
                        
                        this.domainTimers.put(domain, new Date());

                        try 
                        {
                            dao.MarkEmailAsSent(invitee.getId());
                        }
                        catch (ClassNotFoundException ex) 
                        {
                            eventLogger.error(ex);
                        }
                        catch (SQLException ex) 
                        {
                            eventLogger.error(ex);
                        }
                    }//Messaging
                    catch (Exception ex) 
                    {
                        eventLogger.warn(ex);
                    }
                }
            }
        }
    }
}