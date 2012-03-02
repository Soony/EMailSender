/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boryi.compaign.sender;

import java.sql.*;
import java.util.Date;
import java.util.*;
import java.lang.Integer;
import java.util.Vector;

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

    /**
     * Get the waiting to be sent email addresses
     * 
     * @param typeId            Id of the email type
     * @param domainId          Id of the email domain
     * @param start             Start id
     * @param end               End id
     * @param checklist         The checklist holding invitees to send
     * @param displayImage      If this domain displays image
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException  
     */
    public List<Invitee> GetEmailList(
            int typeId, int domainId, int start, int end, 
            Vector<Integer> checklist, Boolean displayImage) 
            throws ClassNotFoundException, SQLException
    {
        List<Invitee> list = new ArrayList<Invitee>();

        Class<?> forName = Class.forName("com.mysql.jdbc.Driver");

        Connection conn = DriverManager.getConnection(dbConnStr);

        if(conn.isClosed())
        {
            throw new SQLException("Closed connection");
        }

        CallableStatement sm = conn.prepareCall(
                "{ call get_invitees(?,?,?,?)}");

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
                invitee.setTypeId(rs.getInt("nvt_typ_id"));
                invitee.setGender(rs.getBoolean("nvt_gender"));
                invitee.setInviter_email(rs.getString("nvtr_email"));
                invitee.setInviter_name(rs.getString("nvtr_name"));
                invitee.setInviter_gender(rs.getBoolean("nvtr_gender"));
                invitee.setDisplayImage(displayImage);

                list.add(invitee);

                checklist.add(invitee.getId());
            }
        }
        return list;
    }


    /// <summary>
    /// Mark the email successful be sent
    /// </summary>
    /// <param name="sender">Sender's email address</param>
    /// <param name="receiver">Receiver's email adderss</param>
    /// <param name="runDate">Send email date</param>
    public void MarkEmailAsSent(int id)
            throws ClassNotFoundException, SQLException
    {
        Connection conn = DriverManager.getConnection(dbConnStr);

        if(conn.isClosed())
        {
            throw new SQLException("Connection is closed");
        }

        CallableStatement sm = conn.prepareCall(
            "{ call check_email_sent(?)}");
        sm.setInt(1, id);
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
                list.put(rs.getInt("dmn_id"), rs.getString("dmn_domain"));
            }
        }
        return list;
    }
    
    /**
     * Get the types
     * 
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException  
     */
    public Hashtable<Integer, String> GetTypes()
            throws ClassNotFoundException, SQLException
    {
        Hashtable<Integer, String> list = new Hashtable<Integer, String>();
        
        Class<?> forName = Class.forName("com.mysql.jdbc.Driver");

        Connection conn = DriverManager.getConnection(dbConnStr);

        if(conn.isClosed())
        {
            throw new SQLException("Closed connection");
        }

        CallableStatement sm = conn.prepareCall("{ call get_types()}");

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
    
    
        /**
     * Get the types
     * 
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException  
     */
    public void BuildChecklist(int id)
            throws ClassNotFoundException, SQLException
    {
        Class<?> forName = Class.forName("com.mysql.jdbc.Driver");

        Connection conn = DriverManager.getConnection(dbConnStr);

        if(conn.isClosed())
        {
            throw new SQLException("Closed connection");
        }

        CallableStatement sm = conn.prepareCall("{ call build_checklist(?)}");
        
        sm.setInt(1, id);
        
        sm.execute();
    }
    
    
    /**
     * Get the checklist last run not finished
     * 
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException  
     */
    public Vector<Integer> CheckLastRun()
            throws ClassNotFoundException, SQLException
    {
        Vector<Integer> list = new Vector<Integer>();
        
        Class<?> forName = Class.forName("com.mysql.jdbc.Driver");

        Connection conn = DriverManager.getConnection(dbConnStr);

        if(conn.isClosed())
        {
            throw new SQLException("Closed connection");
        }

        CallableStatement sm = conn.prepareCall("{ call check_lastrun()}");
        
        ResultSet rs = sm.executeQuery();

        if (rs != null)
        {
            while (rs.next())
            {
                list.add(rs.getInt("sndl_id"));
            }
        }
        return list;
    }
    
    /**
     * Get the types
     * 
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException  
     */
    public void CleanChecklist()
            throws ClassNotFoundException, SQLException
    {
        Class<?> forName = Class.forName("com.mysql.jdbc.Driver");

        Connection conn = DriverManager.getConnection(dbConnStr);

        if(conn.isClosed())
        {
            throw new SQLException("Closed connection");
        }

        CallableStatement sm = conn.prepareCall("{ call clear_email_list()}");
        sm.execute();
    }
}