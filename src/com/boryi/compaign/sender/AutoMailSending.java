package com.boryi.compaign.sender;

import java.util.Date;
import java.util.List;
import org.apache.log4j.*;
import com.sun.mail.smtp.*;
import com.sun.mail.imap.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.log4j.xml.DOMConfigurator;

/**
 *
 * @author yang.song
 */
public class AutoMailSending {
    private String mailServerName;//mail server
    private String mailUserName;//sender's email username
    private String mailPassword;//sender's email password
    private String emailFormat;//email's body
    private String emailSubject;//email's subject
    private String dbConnStr;//database connection string
    private String spGetEmailList;
    private String spMarkEmailAsSent;
    private Date runDate;
    private Dao dao;
    private Hashtable<String, List<Invitee>> list;
    private Hashtable<String, List<Invitee>> failList;
    private Hashtable<String, List<Invitee>> templist;
    private Boolean needRetry = false;
    private int maxRetry = 3;
    private int retry = 1;
    private int interval;
    private int maxKey;
    private int startId;
    private int endId;
    
    private static Logger eventLogger = Logger.getLogger("EventLogger");

    static {
        DOMConfigurator.configure("log4j.xml");
    }
    
    public static void main(String[] args)
    {
        List hm = new ArrayList();
        
        
        AutoMailSending c = new AutoMailSending();
        c.OnRun();
    }
    
    private void Init() 
    {
        try
        {
            Config config = new Config();
            config.read("config.xml");

            mailServerName = config.getMailServerName();
            mailUserName = config.getMailUserName();
            mailPassword = config.getMailPassword();
            emailFormat = config.getEmailFormat();
            dbConnStr = config.getConnection();
            spGetEmailList = config.getSpGetEmailList();
            spMarkEmailAsSent = config.getSpMarkEmailAsSent();
            emailSubject = config.getEmailSubject();
            interval = config.getInterval();
            
            dao = new Dao(dbConnStr);
        }
        catch(Exception ex)
        {
            eventLogger.error(ex);
        }
    }

    /// <summary>
    /// Send email to the receivers in the list collection
    /// </summary>
    /// <param name="list">Invitee info list</param>
    private void MailMessage(List<Invitee> list)
    {
        needRetry = false;
        if (list == null)
        {
            return;
        }

        for (Invitee email : list)
        {
            templist.clear();
            
            try
            {
                SendEmail(email);
                
                dao.MarkEmailAsSent(email.getInviter_email(), email.getEmail(), runDate);
            }
            catch (Exception ex)
            {
                String exs = ex.toString();
                needRetry = true;
//                templist.add(email);
                
                eventLogger.warn(ex);
            }
        }
    }

    private void SendEmail(Invitee email)
            throws MessagingException, NoSuchProviderException {

        String subject = this.emailSubject;
        String format = this.emailFormat;
        
        Properties props = System.getProperties();
        // Setup mail server
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.smtp.host", this.mailServerName);
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.starttles.enable", "true");
        
        //props.setProperty("mail.user", this.mailUserName);
        //props.setProperty("mail.password", this.mailPassword);
        
        Session mailSession = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(mailUserName, mailPassword);
            };
        });
        //mailSession.setDebug(true);
        Transport transport = mailSession.getTransport();

        MimeMessage message = new MimeMessage(mailSession);
        
//        subject = emailSubject.replace("{Invitee_firstname}", email.getInvitee_firstname())
//            .replace("{Invitee_lastname}", email.getInvitee_lastname())
//            .replace("{Invitee_email}", email.getInvitee_email())
//            .replace("{Inviter_firstname}", email.getInviter_firstname())
//            .replace("{Inviter_lastname}", email.getInviter_lastname())
//            .replace("{Inviter_email}", email.getInviter_email());
//        format = emailFormat.replace("{Invitee_firstname}", email.getInvitee_firstname())
//            .replace("{Invitee_lastname}", email.getInvitee_lastname())
//            .replace("{Invitee_email}", email.getInvitee_email())
//            .replace("{Inviter_firstname}", email.getInviter_firstname())
//            .replace("{Inviter_lastname}", email.getInviter_lastname())
//            .replace("{Inviter_email}", email.getInviter_email());
        
        message.setSubject(this.emailSubject);
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
        runDate = new Date();
        
        try
        {
            Init();
        }
        catch(Exception ex)
        {
            System.err.println(ex);
        }
        
        String[] args = {dbConnStr, spGetEmailList, spMarkEmailAsSent};


//        inviteeList = dao.GetEmailList();
//        failList = new ArrayList<Invitee>();
//        templist = new ArrayList<Invitee>();
//        MailMessage(list);
//
//        while (needRetry && retry <= maxRetry)
//        {
//            retry++;
//            failList.clear();
//            failList.addAll(templist);
//            MailMessage(failList);
//            //After max retry times output the failed send email address.
//            if (retry == maxRetry)
//            {
//                for (Invitee email : failList)
//                {
//                    eventLogger.error("Tried " + maxRetry 
//                        + " times,Failed to send email to:" + email.getEmail());
//                }
//            }
//        }
    }
}