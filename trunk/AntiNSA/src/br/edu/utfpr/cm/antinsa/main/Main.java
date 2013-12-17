/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.antinsa.main;

import br.edu.utfpr.cm.antinsa.controller.GoogleDriveController;
import br.edu.utfpr.cm.antinsa.oauth.googledrive.GoogleDriveOAuth;
import br.edu.utfpr.cm.antinsa.service.googledrive.GoogleDrive;
import br.edu.utfpr.cm.antinsa.configuration.Config;
import br.edu.utfpr.cm.antinsa.gui.JFramePreferences;
import br.edu.utfpr.cm.antinsa.controller.GoogleDriveController;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.jdom2.JDOMException;

/**
 *
 * @author junior
 */
public class Main {

    public static void main(String[] args) {
        GoogleDriveController googleDriveController = new GoogleDriveController();
        try {
            try {
                javax.swing.UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            } catch (InstantiationException ex) {
                ex.printStackTrace();
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            } catch (UnsupportedLookAndFeelException ex) {
                ex.printStackTrace();
            }
            Config.setup();
            JFramePreferences main = new JFramePreferences();
            main.setLocationRelativeTo(null);
            main.setVisible(false);
            googleDriveController.initServiceGoogleDrive();
        } catch (JDOMException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
