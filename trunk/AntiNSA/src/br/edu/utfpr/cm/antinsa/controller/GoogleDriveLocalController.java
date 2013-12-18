/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.antinsa.controller;

import br.edu.utfpr.cm.antinsa.oauth.googledrive.GoogleDriveOAuth;
import br.edu.utfpr.cm.antinsa.util.GDUtils;
import br.edu.utfpr.cm.antinsa.service.googledrive.GoogleDrive;
import br.edu.utfpr.cm.antinsa.configuration.Config;
import br.edu.utfpr.cm.antinsa.database.DaoDataFile;
import br.edu.utfpr.cm.antinsa.util.HashGenerator;
import br.edu.utfpr.cm.antinsa.util.Util;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import net.contentobjects.jnotify.JNotify;
import net.contentobjects.jnotify.JNotifyException;

/**
 *
 * @author junior
 */
public class GoogleDriveLocalController extends Thread {

    @Override
    public void run() {
        java.io.File dir = new File(Config.STORE_DEFAULT.getAbsolutePath());
        while (!isInterrupted()) {
            //Verificar os arquivos do diretório padrão
            java.io.File[] files = dir.listFiles();
            updateDatabase(files);
            for (File file : files) {
                if (verifyFile(file.getAbsolutePath())) {
                    System.out.println(file.getName());
                }
            }
        }
    }

    public void initServiceGoogleDrive() {
        if (Config.readXMLConfig("enable-google-drive").getText().equals("true") && !GoogleDriveOAuth.isValidCredential()) {
            try {
                int value = JOptionPane.showConfirmDialog(null, "Your Google Drive account is enable, but isn't authorized! \n Would you like to open your browser to perform authorization?", "Information", JOptionPane.YES_NO_OPTION);
                if (value == JOptionPane.YES_OPTION) {
                    GoogleDriveOAuth.getCredential();
                    JOptionPane.showMessageDialog(null, "Authentication performed successfully!", "Sucessful", JOptionPane.INFORMATION_MESSAGE);
                    start();
                } else {
                    Config.readXMLConfig("enable-google-drive").setText("false");
                    Config.saveXMLConfig();
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "WARNING", JOptionPane.WARNING_MESSAGE);
                ex.printStackTrace();
            } catch (GeneralSecurityException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "WARNING", JOptionPane.WARNING_MESSAGE);
                ex.printStackTrace();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "WARNING", JOptionPane.WARNING_MESSAGE);
                ex.printStackTrace();
            }
        } else if (Config.readXMLConfig("enable-google-drive").getText().equals("true") && GoogleDriveOAuth.isValidCredential()) {
            try {
                GoogleDriveOAuth.getCredential();
                start();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "WARNING", JOptionPane.WARNING_MESSAGE);
                ex.printStackTrace();
            } catch (GeneralSecurityException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "WARNING", JOptionPane.WARNING_MESSAGE);
                ex.printStackTrace();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "WARNING", JOptionPane.WARNING_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private boolean verifyFile(String fileName) {
        char c1 = fileName.charAt(fileName.length() - 1);
        char c2 = fileName.charAt(0);
        if (c1 == '~' || c2 == '.') {
            return false;
        }
        return true;
    }

    private void updateDatabase(java.io.File[] files) {
        try {
            DaoDataFile daoDataFile = new DaoDataFile();
            for (File file : files) {
                if (verifyFile(file.getAbsolutePath()) && file.isFile() && !daoDataFile.dataFileExists(file.getName())) {
                    daoDataFile.insert(file.getName(), file.length(), file.lastModified(), HashGenerator.hashFile(file.getAbsolutePath()));
                }
            }
            daoDataFile.getTransactionManager().close();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
