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
import java.io.IOException;
import java.security.GeneralSecurityException;
import javax.swing.JOptionPane;
import net.contentobjects.jnotify.JNotify;
import net.contentobjects.jnotify.JNotifyException;

/**
 *
 * @author junior
 */
public class GoogleDriveController implements Runnable {

    private int watchID = 0;
    private boolean watchSubtree;
    private String path;
    private boolean resultStop;

    public void start() throws Exception {
        if (Util.verifyServiceConnection(GDUtils.URL_SERVICE)) {
            path = Config.STORE_DEFAULT.getAbsolutePath();
            Config.setLibraryPath();

            int mask = JNotify.FILE_CREATED
                    | JNotify.FILE_DELETED
                    | JNotify.FILE_MODIFIED
                    | JNotify.FILE_RENAMED;

            // watch subtree?
            watchSubtree = true;

            // add actual watch
            watchID = JNotify.addWatch(path, mask, watchSubtree, new GoogleDrive());

            // sleep a little, the application will exit if you
            // don't (watching is asynchronous), depending on your
            // application, this may not be required
            while (true) {
            }
        }
    }

    @Override
    public void run() {
        while (true) {
        }
    }

    public void stop() {
        if (watchID != 0) {
            try {
                resultStop = JNotify.removeWatch(watchID);
            } catch (JNotifyException ex) {
                ex.printStackTrace();
            }
        }

    }

    public  void initServiceGoogleDrive() {
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
                stop();
                JOptionPane.showMessageDialog(null, ex.getMessage(), "WARNING", JOptionPane.WARNING_MESSAGE);
                ex.printStackTrace();
            } catch (GeneralSecurityException ex) {
                stop();
                JOptionPane.showMessageDialog(null, ex.getMessage(), "WARNING", JOptionPane.WARNING_MESSAGE);
                ex.printStackTrace();
            } catch (Exception ex) {
                stop();
                JOptionPane.showMessageDialog(null, ex.getMessage(), "WARNING", JOptionPane.WARNING_MESSAGE);
                ex.printStackTrace();
            }
        } else if (Config.readXMLConfig("enable-google-drive").getText().equals("true") && GoogleDriveOAuth.isValidCredential()) {
            try {
                GoogleDriveOAuth.getCredential();
                start();
            } catch (IOException ex) {
                stop();
                JOptionPane.showMessageDialog(null, ex.getMessage(), "WARNING", JOptionPane.WARNING_MESSAGE);
                ex.printStackTrace();
            } catch (GeneralSecurityException ex) {
                stop();
                JOptionPane.showMessageDialog(null, ex.getMessage(), "WARNING", JOptionPane.WARNING_MESSAGE);
                ex.printStackTrace();
            } catch (Exception ex) {
                stop();
                JOptionPane.showMessageDialog(null, ex.getMessage(), "WARNING", JOptionPane.WARNING_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
}
