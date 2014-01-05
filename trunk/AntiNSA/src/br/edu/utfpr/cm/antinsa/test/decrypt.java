/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.antinsa.test;

import br.edu.utfpr.cm.antinsa.configuration.Config;
import br.edu.utfpr.cm.antinsa.configuration.GDUtils;
import br.edu.utfpr.cm.antinsa.security.SecretKeyAESCrypto;
import br.edu.utfpr.cm.antinsa.service.googledrive.GoogleDrive;
import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom2.JDOMException;

/**
 *
 * @author junior
 */
public class decrypt {

    public static void main(String[] args) throws GeneralSecurityException, IOException {
        GDUtils.CACHE_DIR.mkdirs(); 
    //        try {
    //            SecretKeyAESCrypto s = new SecretKeyAESCrypto();
    ////           s.encrypt(new File("/home/junior/junior.txt"));
    //            
    ////            s.decrypt(new File("/home/junior/junior.txt"));
    //        } catch (Exception ex) {
    //        }
    //        }
    }
}
