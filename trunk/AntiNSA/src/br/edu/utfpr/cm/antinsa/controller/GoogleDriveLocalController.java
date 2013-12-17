/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.antinsa.controller;

import br.edu.utfpr.cm.antinsa.oauth.googledrive.GoogleDriveOAuth;
import br.edu.utfpr.cm.antinsa.util.GDUtils;
import br.edu.utfpr.cm.antinsa.service.googledrive.GoogleDrive;
import br.edu.utfpr.cm.antinsa.configuration.Config;
import br.edu.utfpr.cm.antinsa.util.Util;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import javax.swing.JOptionPane;
import net.contentobjects.jnotify.JNotify;
import net.contentobjects.jnotify.JNotifyException;

/**
 *
 * @author junior
 */
public class GoogleDriveLocalController extends Thread {

    private int watchID = 0;
    private boolean watchSubtree;
    private String path;
    private boolean resultStop;
    int id;

    @Override
    public void run() {
////        initServiceGoogleDrive();
        java.io.File dir = new File(Config.STORE_DEFAULT.getAbsolutePath());
        while (!isInterrupted()) {
            //Verificar os arquivos do diretório padrão
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (verifyFile(files[i].getAbsolutePath())) {
                    System.out.println(files[i].getName());
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
//                    start();
                } else {
                    Config.readXMLConfig("enable-google-drive").setText("false");
                    Config.saveXMLConfig();
                }
            } catch (IOException ex) {
//                stop();
                JOptionPane.showMessageDialog(null, ex.getMessage(), "WARNING", JOptionPane.WARNING_MESSAGE);
                ex.printStackTrace();
            } catch (GeneralSecurityException ex) {
//                stop();
                JOptionPane.showMessageDialog(null, ex.getMessage(), "WARNING", JOptionPane.WARNING_MESSAGE);
                ex.printStackTrace();
            } catch (Exception ex) {
//                stop();
                JOptionPane.showMessageDialog(null, ex.getMessage(), "WARNING", JOptionPane.WARNING_MESSAGE);
                ex.printStackTrace();
            }
        } else if (Config.readXMLConfig("enable-google-drive").getText().equals("true") && GoogleDriveOAuth.isValidCredential()) {
            try {
                GoogleDriveOAuth.getCredential();
//                start();
            } catch (IOException ex) {
//                stop();
                JOptionPane.showMessageDialog(null, ex.getMessage(), "WARNING", JOptionPane.WARNING_MESSAGE);
                ex.printStackTrace();
            } catch (GeneralSecurityException ex) {
//                stop();
                JOptionPane.showMessageDialog(null, ex.getMessage(), "WARNING", JOptionPane.WARNING_MESSAGE);
                ex.printStackTrace();
            } catch (Exception ex) {
//                stop();
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
}
