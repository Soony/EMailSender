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
import java.util.Hashtable;
import java.util.Vector;
import javax.lang.model.element.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.SchemaFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

/**
 *
 * @author yang.song
 */
public class Config {
    private Document doc;

    private String mailHost;
    private String mailUser;
    private String mailPassword;
    private String dbConnStr;

    private long interval;
    private int maxKey;
    private int startId;
    private int endId;
    
    private Hashtable<Integer, EmailType> types;
    
    /**
     * @return the connection
     */
    public String getConnection() {
        return getDbConnStr();
    }

    /**
     * @param connection the connection to set
     */
    public void setConnection(String connection) {
        this.setDbConnStr(connection);
    }
    
    protected void read(String xml)
            throws SAXException, IOException, ParserConfigurationException, 
            URISyntaxException, Exception
    {
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true);         // Don't forget this
        DocumentBuilder builder = domFactory.newDocumentBuilder();
        
        File xmlFile = new File(xml);
        this.doc = builder.parse(xmlFile);
        
        setConnection(doc.getElementsByTagName("DbConnStr").item(0).getTextContent());
        setMailHost(doc.getElementsByTagName("MailHost").item(0).getTextContent());
        setMailUser(doc.getElementsByTagName("MailUser").item(0).getTextContent());
        setMailPassword(doc.getElementsByTagName("MailPassword").item(0).getTextContent());
        setInterval(Integer.valueOf(doc.getElementsByTagName("IntervalSeconds").item(0).getTextContent()));
        setMaxKey(Integer.valueOf(doc.getElementsByTagName("MaxKey").item(0).getTextContent()));
        setStartId(Integer.valueOf(doc.getElementsByTagName("StartId").item(0).getTextContent()));
        setEndId(Integer.valueOf(doc.getElementsByTagName("EndId").item(0).getTextContent()));
        
        types = new Hashtable<Integer, EmailType>();
        
        NodeList typesNodes = doc.getElementsByTagName("Type");
        
        for (int i = 0; i < typesNodes.getLength(); i++)
        {
            Node node = typesNodes.item(i);
            
            String text = node.getAttributes().getNamedItem("active").getTextContent();
            
            // skip the inactive ones
            if (!Boolean.valueOf(text)){ continue; }
            
            int id = Integer.valueOf(node.getAttributes().getNamedItem("id").getTextContent());
            types.put(id,
                    new EmailType(
                        id,
                        node.getAttributes().getNamedItem("name").getTextContent(),
                        node.getAttributes().getNamedItem("emailSubject").getTextContent(),
                        node.getAttributes().getNamedItem("emailBodyWithImg").getTextContent(),
                        node.getAttributes().getNamedItem("emailBodyWithoutImg").getTextContent()));
        }
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
     * @return the mailHost
     */
    public String getMailHost() {
        return mailHost;
    }

    /**
     * @param mailHost the mailHost to set
     */
    public void setMailHost(String mailHost) {
        this.mailHost = mailHost;
    }

    /**
     * @return the mailUser
     */
    public String getMailUser() {
        return mailUser;
    }

    /**
     * @param mailUser the mailUser to set
     */
    public void setMailUser(String mailUser) {
        this.mailUser = mailUser;
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
     * @return the dbConnStr
     */
    public String getDbConnStr() {
        return dbConnStr;
    }

    /**
     * @param dbConnStr the dbConnStr to set
     */
    public void setDbConnStr(String dbConnStr) {
        this.dbConnStr = dbConnStr;
    }

    /**
     * @return the interval
     */
    public long getInterval() {
        return interval;
    }

    /**
     * @param interval the interval to set
     */
    public void setInterval(long intervalSecond) {
        this.interval = intervalSecond * 1000 ;
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

    /**
     * @return the types
     */
    public Hashtable<Integer, EmailType> getTypes() {
        return types;
    }

    /**
     * @param types the types to set
     */
    public void setTypes(Hashtable<Integer, EmailType> types) {
        this.types = types;
    }
}