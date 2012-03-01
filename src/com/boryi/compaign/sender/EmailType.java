/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boryi.compaign.sender;

/**
 *
 * @author yang.song
 */
public class EmailType {
    private int id;
    private String type;
    private String subject;
    private String emailBodyWithImg;
    private String emailBodyWithoutImg;
    
    public EmailType(){}
    
    public EmailType(int id, String type){
        this.id = id;
        this.type = type;
    }
    
    public EmailType(int id, String type, String subject, 
            String emailBodyWithImg, String emailBodyWithoutImg)
    {
        this(id, type);
        
        this.subject = subject;
        this.emailBodyWithImg = emailBodyWithImg;
        this.emailBodyWithoutImg = emailBodyWithoutImg;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * @return the emailBodyWithImg
     */
    public String getEmailBodyWithImg() {
        return emailBodyWithImg;
    }

    /**
     * @param emailBodyWithImg the emailBodyWithImg to set
     */
    public void setEmailBodyWithImg(String emailBodyWithImg) {
        this.emailBodyWithImg = emailBodyWithImg;
    }

    /**
     * @return the emailBodyWithoutImg
     */
    public String getEmailBodyWithoutImg() {
        return emailBodyWithoutImg;
    }

    /**
     * @param emailBodyWithoutImg the emailBodyWithoutImg to set
     */
    public void setEmailBodyWithoutImg(String emailBodyWithoutImg) {
        this.emailBodyWithoutImg = emailBodyWithoutImg;
    }
    
    
}
