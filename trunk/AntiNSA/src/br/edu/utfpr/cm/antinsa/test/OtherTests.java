/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.antinsa.test;

import br.edu.utfpr.cm.antinsa.configuration.Config;
import br.edu.utfpr.cm.antinsa.configuration.GDUtils;
import br.edu.utfpr.cm.antinsa.security.SecretKeyAESCrypto;
import br.edu.utfpr.cm.antinsa.service.googledrive.GoogleDrive;
import br.edu.utfpr.cm.antinsa.security.HashGenerator;
import com.google.api.services.drive.model.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

    public static void main(String[] args) throws JDOMException, IOException, ClassNotFoundException, SQLException, GeneralSecurityException {
        try {
            Config.setup();
            GoogleDrive g = new GoogleDrive();
            java.io.File f = new java.io.File("/home/junior/AntiNSA/junior.txt");
            //        g.fileCreated(f);
            SecretKeyAESCrypto s = new SecretKeyAESCrypto();
            System.out.println("#############CLOUD###############");
            List<File> filesDefaultFolder = g.getFilesDefaultFolder();
            //        SecretKeyAESCrypto s = new 
            //        System.out.println(HashGenerator.hashFile(f.getAbsolutePath()));
            for (File file : filesDefaultFolder) {
                System.out.println(file.getFileSize());
                System.out.println(file.getMd5Checksum());
                System.out.println(file.getModifiedByMeDate().getValue());
                InputStream downloadFile = g.downloadFile(file.getDownloadUrl());
//                s.decrypt(test(downloadFile));
//                g.saveFile(downloadFile, file.getTitle(), file.getLastViewedByMeDate().getValue());
            }
            //        System.out.println("#############CLOUD###############");
            //        System.out.println("#############LOCAL###############");
            //        System.out.println(f.length());
            //        f.setLastModified(1388494521000L);
            //        System.out.println(HashGenerator.hashFile(f.getAbsolutePath()));
            //        System.out.println(f.lastModified());
            //        System.out.println("#############LOCAL###############");
        } catch (Exception ex) {
            Logger.getLogger(OtherTests.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static java.io.File test(InputStream inputStream) {

        OutputStream outputStream = null;

        try {
            // write the inputStream to a FileOutputStream
            java.io.File temp = java.io.File.createTempFile(".", "", GDUtils.CACHE_DIR);
            outputStream =
                    new FileOutputStream(temp);

            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }

            return temp;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    // outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        return null;
    }
}