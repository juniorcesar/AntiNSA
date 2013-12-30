/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.antinsa.test;

import br.edu.utfpr.cm.antinsa.configuration.Config;
import br.edu.utfpr.cm.antinsa.database.DaoDataFile;
import br.edu.utfpr.cm.antinsa.security.SecretKeyAESCrypto;
import br.edu.utfpr.cm.antinsa.service.googledrive.DataFile;
import br.edu.utfpr.cm.antinsa.service.googledrive.GoogleDrive;
import br.edu.utfpr.cm.antinsa.util.HashGenerator;
import com.google.api.client.util.DateTime;
import com.google.api.services.drive.model.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom2.JDOMException;

/**
 *
 * @author junior
 */
public class OtherTests {

    public static void main(String[] args) {
//        String hashFile = HashGenerator.hashFile("/home/junior/AntiNSA/teste.txt");
//        System.out.println(hashFile);
          
        
    
        try {
            SecretKeyAESCrypto c = new SecretKeyAESCrypto();
            java.io.File f = new java.io.File("/home/junior/teste.txt");
            java.io.File encrypt = c.encrypt(new java.io.File("/home/junior/teste.txt"));
            System.out.println("Normal "+f.lastModified());
            System.out.println("Criptografado "+encrypt.lastModified());

        } catch (Exception ex) {
            Logger.getLogger(OtherTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    public static void saveFile(InputStream input, String name) throws FileNotFoundException, IOException {
//        FileOutputStream out = new FileOutputStream(new java.io.File("/home/junior/AntiNSA/" + name));
//
//        int b;
//
//        while ((b = input.read()) > -1) {
//            out.write(b);
//        }
//        input.close();
//        out.close();
//    }
//    
//        try {
//            SecretKeyAESCrypto c = new SecretKeyAESCrypto();
//            java.io.File encrypt = c.encrypt(new java.io.File("/home/junior/teste.txt"));
//            saveFile(new FileInputStream(encrypt), "teste.txt");
//
//        } catch (Exception ex) {
//            Logger.getLogger(OtherTests.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//    public static void saveFile(InputStream input, String name) throws FileNotFoundException, IOException {
//        FileOutputStream out = new FileOutputStream(new java.io.File("/home/junior/AntiNSA/" + name));
//
//        int b;
//
//        while ((b = input.read()) > -1) {
//            out.write(b);
//        }
//        input.close();
//        out.close();
//    }
}
