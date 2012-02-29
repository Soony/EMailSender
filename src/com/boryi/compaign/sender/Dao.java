/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boryi.compaign.sender;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

/**
 *
 * @author yang.song
 */
public class Dao {
    private String dbConnStr;
    private String spGetEmailList;
    private String spMarkEmailAsSent;

    public Dao(String dbConnStr)
    {
        this.dbConnStr = dbConnStr;
    }

    public Dao(String dbConnStr, String spGetEmailList)
    {
        this.dbConnStr = dbConnStr;
        this.spGetEmailList = spGetEmailList;
    }

    public Dao(String dbConnStr, String spGetEmailList, String spMarkEmailAsSent)
    {
        this.dbConnStr = dbConnStr;
        this.spGetEmailList = spGetEmailList;
        this.spMarkEmailAsSent = spMarkEmailAsSent;
    }


    /**
     * Get the waiting to be sent email addresses
     * 
     * @param typeId    Id of the email type
     * @param domainId  Id of the email domain
     * @param start     Start id
     * @param end       End id
     * @return
     */
    public Hashtable<String, List<Invitee>> GetEmailList(
            int typeId, int domainId, int start, int end)
    {
        Hashtable<String, List<Invitee>> list = new Hashtable<String, List<Invitee>>();
        
        try 
        {
            Class<?> forName = Class.forName("com.mysql.jdbc.Driver");

            Connection conn = DriverManager.getConnection(dbConnStr);

            if(conn.isClosed())
            {
                throw new SQLException("Closed connection");
            }

            CallableStatement sm = conn.prepareCall(
                    "{ call " + this.spGetEmailList + "(?,?,?,?)}");
            
            sm.setInt(1, typeId);
            sm.setInt(2, domainId);
            sm.setInt(3, start);
            sm.setInt(4, end);
            
            ResultSet rs = sm.executeQuery();

            if (rs != null)
            {
                while (rs.next())
                {
                    Invitee invitee = new Invitee();
                    
                    invitee.setId(rs.getInt("nvt_id"));
                    invitee.setEmail(rs.getString("nvt_email"));
                    invitee.setName(rs.getString("nvt_name"));
                    invitee.setGender(rs.getBoolean("nvt_gender"));
                    invitee.setInviter_email(rs.getString("nvtr_email"));
                    invitee.setInviter_name(rs.getString("nvtr_name"));
                    invitee.setInviter_gender(rs.getBoolean("nvtr_gender"));
                    
                    // collect the invitee
                    if (list.containsKey(invitee.getDomain()))
                    {
                        list.get(invitee.getDomain()).add(invitee);
                    }
                    else
                    {
                        List<Invitee> newList = new ArrayList<Invitee>();
                        newList.add(invitee);
                        list.put(invitee.getDomain(), newList);
                    }
                }
            }
        }
        catch(Exception ex)
        {
            System.err.println(ex);
        }
        return list;
    }


    /// <summary>
    /// Mark the email successful be sent
    /// </summary>
    /// <param name="sender">Sender's email address</param>
    /// <param name="receiver">Receiver's email adderss</param>
    /// <param name="runDate">Send email date</param>
    public void MarkEmailAsSent(String sender, String receiver, Date sentDate) 
            throws ClassNotFoundException, SQLException
    {
        Class.forName("com.mysql.jdbc.Driver");

        Connection conn = DriverManager.getConnection(dbConnStr);

        if(conn.isClosed())
        {
            throw new SQLException("Connection is closed");
        }

        CallableStatement sm = conn.prepareCall(
            "{ call " + this.spMarkEmailAsSent + "(?,?,?)}");
        sm.setString(1, sender);
        sm.setString(2, receiver);
        sm.setObject(3, sentDate);
        sm.execute();
    }   

    /**
     * Get the domains for all the emails
     * 
     * @param photo    Whether this domain can display cross site link of image
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException  
     */
    public Hashtable<Integer, String> GetDomains(Boolean photo)
            throws ClassNotFoundException, SQLException
    {
        Hashtable<Integer, String> list = new Hashtable<Integer, String>();
        
        Class<?> forName = Class.forName("com.mysql.jdbc.Driver");

        Connection conn = DriverManager.getConnection(dbConnStr);

        if(conn.isClosed())
        {
            throw new SQLException("Closed connection");
        }

        CallableStatement sm = conn.prepareCall("{ call get_domains(?)}");
        sm.setBoolean(1, photo);

        ResultSet rs = sm.executeQuery();

        if (rs != null)
        {
            while (rs.next())
            {
                list.put(rs.getInt("nvt_id"), rs.getString("nvt_id"));
            }
        }
        return list;
    }
}
