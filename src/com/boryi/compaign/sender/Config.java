/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boryi.compaign.sender;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.SchemaFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author yang.song
 */
public class Config {
    private Document doc;
   
    private String mailServerName;
    private String mailUserName;
    private String mailPassword;
    private String csvFile;
    private String emailFormat;
    private String dbConnStr;
    private String spGetEmailList;
    private String spMarkEmailAsSent;
    private String emailSubject;
    
    private int interval;
    private int maxKey;
    private int startId;
    private int endId;
    
    /**
     * @return the connection
     */
    public String getConnection() {
        return dbConnStr;
    }

    /**
     * @param connection the connection to set
     */
    public void setConnection(String connection) {
        this.dbConnStr = connection;
    }
    
    protected void read(String xml)
            throws SAXException, IOException, ParserConfigurationException, 
            URISyntaxException, Exception
    {
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true);         // Don't forget this
        DocumentBuilder builder = domFactory.newDocumentBuilder();
        
        File xmlFile = new File(xml);
        doc = builder.parse(xmlFile);
       
        setConnection(doc.getElementsByTagName("dbConnStr").item(0).getTextContent());
        setMailServerName(doc.getElementsByTagName("mailServerName").item(0).getTextContent());
        setMailUserName(doc.getElementsByTagName("mailUserName").item(0).getTextContent());
        setMailPassword(doc.getElementsByTagName("mailPassword").item(0).getTextContent());
        setCsvFile(doc.getElementsByTagName("csvFile").item(0).getTextContent());
        
        setEmailSubject(doc.getElementsByTagName("emailSubject").item(0).getTextContent());
        setEmailFormat(readFile("tplEmail.html"));
        
        setSpGetEmailList(doc.getElementsByTagName("spGetEmailList").item(0).getTextContent());
        setSpMarkEmailAsSent(doc.getElementsByTagName("spMarkEmailAsSent").item(0).getTextContent());
        setInterval(Integer.valueOf(doc.getElementsByTagName("IntervalSeconds").item(0).getTextContent()));
        
        setMaxKey(Integer.valueOf(doc.getElementsByTagName("MaxKey").item(0).getTextContent()));
        setStartId(Integer.valueOf(doc.getElementsByTagName("StartId").item(0).getTextContent()));
        setEndId(Integer.valueOf(doc.getElementsByTagName("EndId").item(0).getTextContent()));
    }

    /**
     * @return the mailServerName
     */
    public String getMailServerName() {
        return mailServerName;
    }

    /**
     * @param mailServerName the mailServerName to set
     */
    public void setMailServerName(String mailServerName) {
        this.mailServerName = mailServerName;
    }

    /**
     * @return the mailUserName
     */
    public String getMailUserName() {
        return mailUserName;
    }

    /**
     * @param mailUserName the mailUserName to set
     */
    public void setMailUserName(String mailUserName) {
        this.mailUserName = mailUserName;
    }

    /**
     * @return the mailPassword
     */
    public String getMailPassword() {
        return mailPassword;
    }

    /**
     * @param mailPassword the mailPassword to set
     */
    public void setMailPassword(String mailPassword) {
        this.mailPassword = mailPassword;
    }

    /**
     * @return the csvFile
     */
    public String getCsvFile() {
        return csvFile;
    }

    /**
     * @param csvFile the csvFile to set
     */
    public void setCsvFile(String csvFile) {
        this.csvFile = csvFile;
    }

    /**
     * @return the emailFormat
     */
    public String getEmailFormat() {
        return emailFormat;
    }

    /**
     * @param emailFormat the emailFormat to set
     */
    public void setEmailFormat(String emailFormat) {
        this.emailFormat = emailFormat;
    }

    /**
     * @return the spGetEmailList
     */
    public String getSpGetEmailList() {
        return spGetEmailList;
    }

    /**
     * @param spGetEmailList the spGetEmailList to set
     */
    public void setSpGetEmailList(String spGetEmailList) {
        this.spGetEmailList = spGetEmailList;
    }

    /**
     * @return the spMarkEmailAsSent
     */
    public String getSpMarkEmailAsSent() {
        return spMarkEmailAsSent;
    }

    /**
     * @param spMarkEmailAsSent the spMarkEmailAsSent to set
     */
    public void setSpMarkEmailAsSent(String spMarkEmailAsSent) {
        this.spMarkEmailAsSent = spMarkEmailAsSent;
    }

    /**
     * @return the emailSubject
     */
    public String getEmailSubject() {
        return emailSubject;
    }

    /**
     * @param emailSubject the emailSubject to set
     */
    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }
    
    private static String readFile(String path) throws IOException {
        FileInputStream stream = new FileInputStream(new File(path));
        try {
            FileChannel fc = stream.getChannel();
            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            /* Instead of using default, pass in a decoder. */
            return Charset.defaultCharset().decode(bb).toString();
        }
        finally {
            stream.close();
        }
    }

    /**
     * @return the interval
     */
    public int getInterval() {
        return interval;
    }

    /**
     * @param interval the interval to set
     */
    public void setInterval(int interval) {
        this.interval = interval;
    }

    /**
     * @return the maxKey
     */
    public int getMaxKey() {
        return maxKey;
    }

    /**
     * @param maxKey the maxKey to set
     */
    public void setMaxKey(int maxKey) {
        this.maxKey = maxKey;
    }

    /**
     * @return the startId
     */
    public int getStartId() {
        return startId;
    }

    /**
     * @param startId the startId to set
     */
    public void setStartId(int startId) {
        this.startId = startId;
    }

    /**
     * @return the endId
     */
    public int getEndId() {
        return endId;
    }

    /**
     * @param endId the endId to set
     */
    public void setEndId(int endId) {
        this.endId = endId;
    }
}