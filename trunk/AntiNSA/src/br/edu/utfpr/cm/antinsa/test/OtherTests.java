/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.antinsa.test;

import br.edu.utfpr.cm.antinsa.configuration.Config;
import br.edu.utfpr.cm.antinsa.database.DaoDataFile;
import br.edu.utfpr.cm.antinsa.service.googledrive.DataFile;
import br.edu.utfpr.cm.antinsa.service.googledrive.GoogleDrive;
import br.edu.utfpr.cm.antinsa.util.HashGenerator;
import com.google.api.client.util.DateTime;
import com.google.api.services.drive.model.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.List;
import org.jdom2.JDOMException;

/**
 *
 * @author junior
 */
public class OtherTests {

    public static void main(String[] args) throws JDOMException, IOException, ClassNotFoundException, SQLException, GeneralSecurityException {

        Config.setup();
        GoogleDrive g = new GoogleDrive();
        java.io.File f = new java.io.File("/home/junior/AntiNSA/junior.txt");
//        g.fileCreated(f);
        System.out.println("#############CLOUD###############");
        List<File> filesDefaultFolder = g.getFilesDefaultFolder();
        
//        System.out.println(HashGenerator.hashFile(f.getAbsolutePath()));
        for (File file : filesDefaultFolder) {
            System.out.println(file.getFileSize());
            System.out.println(file.getMd5Checksum());
            System.out.println(file.getModifiedByMeDate().getValue());

        }
        System.out.println("#############CLOUD###############");
        System.out.println("#############LOCAL###############");
        System.out.println(f.length());
        f.setLastModified(1388494521000L);
        System.out.println(HashGenerator.hashFile(f.getAbsolutePath()));
        System.out.println(f.lastModified());
        System.out.println("#############LOCAL###############");

    }
}