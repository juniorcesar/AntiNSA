/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.antinsa.test;

import br.edu.utfpr.cm.antinsa.util.GDUtils;
import br.edu.utfpr.cm.antinsa.oauth.googledrive.GoogleDriveOAuth;
import br.edu.utfpr.cm.antinsa.service.googledrive.GoogleDrive;
import br.edu.utfpr.cm.antinsa.configuration.Config;
import br.edu.utfpr.cm.antinsa.controller.GoogleDriveController;
import br.edu.utfpr.cm.antinsa.controller.GoogleDriveLocalController;
import com.google.api.services.oauth2.model.Userinfo;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import javax.swing.JOptionPane;

/**
 *
 * @author junior
 */
public class OtherTests {

    public static void main(String[] args) throws IOException, GeneralSecurityException {
//        String s1 =  "tete.txt~";
//        String s2 =  ".tete.txt";
//        char c1 = s2.charAt(s2.length() - 1);
//        char c2 = s2.charAt(0);
//        if (c1 == '~' || c2 == '.') {
//            System.out.println(false);
//        }
//       
//File f = new File("/home/junior/novo.txt");
//        System.out.println(f.toURI());
//        System.out.println(f.hashCode());
//        GoogleDrive.list();
//        GoogleDriveLocalController c1 = new GoogleDriveLocalController();
//  
//        Thread t1 = new Thread(c1);
//        t1.start();

        File f = new File("/home/junior/NOVO.txt");
        System.out.println(f.hashCode());
     
    }
}
