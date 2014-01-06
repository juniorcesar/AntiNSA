/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.antinsa.test;

import br.edu.utfpr.cm.antinsa.configuration.Config;
import br.edu.utfpr.cm.antinsa.configuration.GDUtils;
import br.edu.utfpr.cm.antinsa.security.SecretKeyAESCrypto;
import br.edu.utfpr.cm.antinsa.service.googledrive.GoogleDrive;
import com.google.api.services.drive.Drive;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.Channel;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.file.Files;
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
public class decrypt {

    public static void main(String[] args) throws GeneralSecurityException, IOException, Exception {
        SecretKeyAESCrypto s = new SecretKeyAESCrypto();
        Config.setup();
        GoogleDrive googleDrive = new GoogleDrive();

        List<com.google.api.services.drive.model.File> filesDefaultFolder = googleDrive.getFilesDefaultFolder();
        for (com.google.api.services.drive.model.File file : filesDefaultFolder) {
//            InputStream downloadFile = googleDrive.downloadFile(file.getDownloadUrl());
////            googleDrive.saveFile(downloadFile, file.getTitle(), file.getModifiedByMeDate().getValue());
            System.out.println(file.getLastViewedByMeDate().toStringRfc3339());
        }



    }
}
