package com.boryi.compaign.sender;

import java.io.Serializable;

/**
 * Class to hold Invitee's info
 * @author yang.song
 */
public class Invitee implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    private int id;              //Invitee's id
    private String email;           //Invitee's email
    private String domain;          //Invitee's domain
    private String name;            //Invitee's name
    private Boolean gender;          //Invitee's gender
    private String inviter_name;    //Inviter's name
    private String inviter_email;   //Inviter's email
    private Boolean inviter_gender;  //Inviter's gender
    
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
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
        
        if (email != null)
        {
            this.domain = email.substring(email.indexOf("@"));
        }
    }

    /**
     * @return the domain
     */
    public String getDomain() {
        return domain;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the gender
     */
    public Boolean getGender() {
        return gender;
    }

    /**
     * @param gender the gender to set
     */
    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    /**
     * @return the inviter_name
     */
    public String getInviter_name() {
        return inviter_name;
    }

    /**
     * @param inviter_name the inviter_name to set
     */
    public void setInviter_name(String inviter_name) {
        this.inviter_name = inviter_name;
    }

    /**
     * @return the inviter_email
     */
    public String getInviter_email() {
        return inviter_email;
    }

    /**
     * @param inviter_email the inviter_email to set
     */
    public void setInviter_email(String inviter_email) {
        this.inviter_email = inviter_email;
    }

    /**
     * @return the inviter_gender
     */
    public Boolean getInviter_gender() {
        return inviter_gender;
    }

    /**
     * @param inviter_gender the inviter_gender to set
     */
    public void setInviter_gender(Boolean inviter_gender) {
        this.inviter_gender = inviter_gender;
    }
    
}
