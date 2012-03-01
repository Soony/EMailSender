/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boryi.compaign.sender;

import java.io.*;

/**
 *
 * @author yang.song
 */
public class SaveLoader {
    
    static private String filename = "save.dat";

    static void Save(Invitee invitee)
            throws IOException
    {
        ObjectOutputStream out = 
                new ObjectOutputStream(new FileOutputStream(filename));

        out.writeObject(invitee);
        
        out.close();
    }
    
    static Invitee Load()
            throws IOException, ClassNotFoundException
    {
        ObjectInputStream in =
                new ObjectInputStream(new FileInputStream(filename));

        Invitee invitee =(Invitee)in.readObject();
        
        in.close();
        
        File file = new File(filename);
        file.delete();
        
        return invitee;
    }
}
