/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.antinsa.main;

import br.edu.utfpr.cm.antinsa.configuration.Config;
import br.edu.utfpr.cm.antinsa.controller.GoogleDriveController;
import br.edu.utfpr.cm.antinsa.gui.JFramePreferences;
import java.io.IOException;
import java.sql.SQLException;
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
        try {
            javax.swing.UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            Config.setup();
            JFramePreferences main = new JFramePreferences();
            main.setLocationRelativeTo(null);
            main.setVisible(false);
            if (Boolean.valueOf(Config.readXMLConfig("enable-google-drive").getValue())) {
                GoogleDriveController driveController = new GoogleDriveController();
                driveController.initServiceGoogleDrive();
            }
        } catch (JDOMException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "WARNING", JOptionPane.WARNING_MESSAGE);
            ex.printStackTrace();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "WARNING", JOptionPane.WARNING_MESSAGE);
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "WARNING", JOptionPane.WARNING_MESSAGE);
            ex.printStackTrace();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "SQL Error:" + ex.getMessage(), "WARNING", JOptionPane.WARNING_MESSAGE);
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "WARNING", JOptionPane.WARNING_MESSAGE);
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "WARNING", JOptionPane.WARNING_MESSAGE);
            ex.printStackTrace();
        } catch (UnsupportedLookAndFeelException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "WARNING", JOptionPane.WARNING_MESSAGE);
            ex.printStackTrace();
        }
    }
}
