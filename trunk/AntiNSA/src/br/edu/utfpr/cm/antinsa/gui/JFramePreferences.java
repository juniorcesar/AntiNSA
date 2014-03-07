/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.antinsa.gui;

import br.edu.utfpr.cm.antinsa.configuration.GDUtils;
import br.edu.utfpr.cm.antinsa.googledrive.GoogleDriveOAuth;
import br.edu.utfpr.cm.antinsa.configuration.Config;
import br.edu.utfpr.cm.antinsa.googledrive.GoogleDriveController;
import br.edu.utfpr.cm.antinsa.security.KeyManager;
import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.NoSuchPaddingException;
import javax.swing.JOptionPane;

/**
 *
 * @author junior
 */
public class JFramePreferences extends javax.swing.JFrame {

    /**
     * Creates new form JFramePreferences
     */
    private TaskBar task;
    private static GoogleDriveController driveController;
    private Thread thread;

    public JFramePreferences() {
        try {
            this.task = new TaskBar("icon.png", this, "AntiNSA");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        initComponents();
        task.instanceTask();
        getConfigGoogleDriveAccount();
        jTextFieldDefaultLocation.setText(Config.STORE_DEFAULT.getAbsolutePath());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane3 = new javax.swing.JTabbedPane();
        jPanelGeneral2 = new javax.swing.JPanel();
        jLabelLocation = new javax.swing.JLabel();
        jTextFieldDefaultLocation = new javax.swing.JTextField();
        jPanelAccount = new javax.swing.JPanel();
        jTabbedPaneAccount = new javax.swing.JTabbedPane();
        jPanelGoogleDrive = new javax.swing.JPanel();
        jCheckBoxEnableGoogle = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButtonAuth = new javax.swing.JButton();
        jLabelName = new javax.swing.JLabel();
        jLabelEmail = new javax.swing.JLabel();
        jButtonDeleteAccount = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        jButtonCopyKey = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jPanelAbout = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jButtonClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("AntiNSA");
        setResizable(false);
        setType(java.awt.Window.Type.UTILITY);

        jLabelLocation.setText("Default location:");

        jTextFieldDefaultLocation.setEnabled(false);

        javax.swing.GroupLayout jPanelGeneral2Layout = new javax.swing.GroupLayout(jPanelGeneral2);
        jPanelGeneral2.setLayout(jPanelGeneral2Layout);
        jPanelGeneral2Layout.setHorizontalGroup(
            jPanelGeneral2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelGeneral2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelLocation)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldDefaultLocation, javax.swing.GroupLayout.PREFERRED_SIZE, 362, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(102, Short.MAX_VALUE))
        );
        jPanelGeneral2Layout.setVerticalGroup(
            jPanelGeneral2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelGeneral2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelGeneral2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelLocation)
                    .addComponent(jTextFieldDefaultLocation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(268, Short.MAX_VALUE))
        );

        jTabbedPane3.addTab("General", jPanelGeneral2);

        jPanelGoogleDrive.setMaximumSize(new java.awt.Dimension(0, 0));
        jPanelGoogleDrive.setPreferredSize(new java.awt.Dimension(612, 364));

        jCheckBoxEnableGoogle.setText("Enable Google Drive Account");
        jCheckBoxEnableGoogle.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxEnableGoogleItemStateChanged(evt);
            }
        });

        jLabel1.setText("Name:");

        jLabel2.setText("E-mail:");

        jButtonAuth.setText("Authorization");
        jButtonAuth.setMaximumSize(new java.awt.Dimension(97, 34));
        jButtonAuth.setMinimumSize(new java.awt.Dimension(97, 34));
        jButtonAuth.setPreferredSize(new java.awt.Dimension(97, 33));
        jButtonAuth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAuthActionPerformed(evt);
            }
        });

        jButtonDeleteAccount.setText("Delete Account");
        jButtonDeleteAccount.setMaximumSize(new java.awt.Dimension(116, 34));
        jButtonDeleteAccount.setMinimumSize(new java.awt.Dimension(116, 34));
        jButtonDeleteAccount.setPreferredSize(new java.awt.Dimension(116, 34));
        jButtonDeleteAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteAccountActionPerformed(evt);
            }
        });

        jButtonCopyKey.setText("Save key");
        jButtonCopyKey.setMaximumSize(new java.awt.Dimension(72, 34));
        jButtonCopyKey.setMinimumSize(new java.awt.Dimension(72, 34));
        jButtonCopyKey.setPreferredSize(new java.awt.Dimension(72, 33));
        jButtonCopyKey.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCopyKeyActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Cantarell", 0, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 0, 0));
        jLabel3.setText("Click this button to save your cryptographic key.");

        javax.swing.GroupLayout jPanelGoogleDriveLayout = new javax.swing.GroupLayout(jPanelGoogleDrive);
        jPanelGoogleDrive.setLayout(jPanelGoogleDriveLayout);
        jPanelGoogleDriveLayout.setHorizontalGroup(
            jPanelGoogleDriveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jSeparator5, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanelGoogleDriveLayout.createSequentialGroup()
                .addGroup(jPanelGoogleDriveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBoxEnableGoogle)
                    .addGroup(jPanelGoogleDriveLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanelGoogleDriveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelGoogleDriveLayout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addComponent(jLabelName, javax.swing.GroupLayout.PREFERRED_SIZE, 368, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanelGoogleDriveLayout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(jLabelEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 366, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanelGoogleDriveLayout.createSequentialGroup()
                                .addComponent(jButtonAuth, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonDeleteAccount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(123, Short.MAX_VALUE))
            .addGroup(jPanelGoogleDriveLayout.createSequentialGroup()
                .addGroup(jPanelGoogleDriveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonCopyKey, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanelGoogleDriveLayout.setVerticalGroup(
            jPanelGoogleDriveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelGoogleDriveLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCheckBoxEnableGoogle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 8, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelGoogleDriveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonAuth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonDeleteAccount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelGoogleDriveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabelName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelGoogleDriveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabelEmail))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonCopyKey, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 44, Short.MAX_VALUE))
        );

        jTabbedPaneAccount.addTab("Google Drive", jPanelGoogleDrive);

        javax.swing.GroupLayout jPanelAccountLayout = new javax.swing.GroupLayout(jPanelAccount);
        jPanelAccount.setLayout(jPanelAccountLayout);
        jPanelAccountLayout.setHorizontalGroup(
            jPanelAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelAccountLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPaneAccount, javax.swing.GroupLayout.PREFERRED_SIZE, 575, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelAccountLayout.setVerticalGroup(
            jPanelAccountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelAccountLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPaneAccount, javax.swing.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE))
        );

        jTabbedPane3.addTab("Account", jPanelAccount);

        jLabel4.setText("AntiNSA - Version 1.0");

        jLabel5.setText("Junior Cesar de Oliveira");

        jLabel6.setText("Developed by: ");

        jLabel7.setText("Luiz Arthur Feitosa dos Santos");

        javax.swing.GroupLayout jPanelAboutLayout = new javax.swing.GroupLayout(jPanelAbout);
        jPanelAbout.setLayout(jPanelAboutLayout);
        jPanelAboutLayout.setHorizontalGroup(
            jPanelAboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelAboutLayout.createSequentialGroup()
                .addGroup(jPanelAboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelAboutLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel4))
                    .addGroup(jPanelAboutLayout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(jPanelAboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))))
                .addContainerGap(372, Short.MAX_VALUE))
        );
        jPanelAboutLayout.setVerticalGroup(
            jPanelAboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelAboutLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel4)
                .addGap(46, 46, 46)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addContainerGap(164, Short.MAX_VALUE))
        );

        jTabbedPane3.addTab("About", jPanelAbout);

        jButtonClose.setText("Close");
        jButtonClose.setMaximumSize(new java.awt.Dimension(97, 34));
        jButtonClose.setMinimumSize(new java.awt.Dimension(97, 34));
        jButtonClose.setPreferredSize(new java.awt.Dimension(97, 33));
        jButtonClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane3, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonClose, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButtonClose, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCloseActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_jButtonCloseActionPerformed

    private void jButtonCopyKeyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCopyKeyActionPerformed
        if (GDUtils.SECRET_KEY.exists()) {
            saveKey();
        } else {
            JOptionPane.showMessageDialog(null, "Key not found", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButtonCopyKeyActionPerformed

    private void jButtonDeleteAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteAccountActionPerformed
        // TODO add your handling code here:
        int value = JOptionPane.showConfirmDialog(this, "Would you like to delete your Google Drive account this computer?", "Information", JOptionPane.YES_NO_OPTION);
        if (value == JOptionPane.YES_OPTION) {
            if (value == JOptionPane.YES_OPTION) {
                deleteGoogleAccount();
                if (driveController != null) {
                    driveController.interrupt();
                }
//                int value1 = JOptionPane.showConfirmDialog(this, "Would you like to remove directory AntiNSA?", "Information", JOptionPane.YES_NO_OPTION);
//                if (value == JOptionPane.YES_OPTION) {
////                    if (Config.STORE_DEFAULT != null) {
//                        Config.STORE_DEFAULT.delete();
////                    }
//                }
                JOptionPane.showMessageDialog(null, "Your Google Drive account was deleted this computer with success!", "Sucessful", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }//GEN-LAST:event_jButtonDeleteAccountActionPerformed

    private void jButtonAuthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAuthActionPerformed
        if (!GoogleDriveOAuth.isValidCredential()) {
            try {
                JOptionPane.showMessageDialog(this, "Your browser will be to open to perform authorization!", "Information", JOptionPane.INFORMATION_MESSAGE);
                GoogleDriveOAuth.authorize();
                if (Config.STORE_DEFAULT != null) {
                    Config.STORE_DEFAULT.mkdirs();
                }
                jButtonDeleteAccount.setEnabled(true);
                JOptionPane.showMessageDialog(this, "Authorization performed successfully!", "Sucessful", JOptionPane.INFORMATION_MESSAGE);
                getKey();
                if (Boolean.valueOf(Config.readXMLConfig("enable-google-drive").getText()) == true) {
                    if (driveController == null) {
                        driveController = new GoogleDriveController();
                    }
                    driveController.initServiceGoogleDrive();
                    setAccountInfo();
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
                ex.printStackTrace();
            } catch (GeneralSecurityException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
                ex.printStackTrace();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
                ex.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "The application is already authorized!", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_jButtonAuthActionPerformed

    private void jCheckBoxEnableGoogleItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxEnableGoogleItemStateChanged
        setConfigGoogleDriveAccount();
        getConfigGoogleDriveAccount();
    }//GEN-LAST:event_jCheckBoxEnableGoogleItemStateChanged

    private void getConfigGoogleDriveAccount() {
        Boolean enable = Boolean.valueOf(Config.readXMLConfig("enable-google-drive").getValue());
        Boolean enableAPIPersonal = Boolean.valueOf(Config.readXMLConfig("enable-api-personal").getValue());
        jCheckBoxEnableGoogle.setSelected(enable);
        jButtonAuth.setEnabled(enable);
        if (GoogleDriveOAuth.isValidCredential()) {
            jButtonCopyKey.setEnabled(enable);
            jButtonDeleteAccount.setEnabled(enable);
        } else {
            jButtonDeleteAccount.setEnabled(false);
        }
        setAccountInfo();
    }

    private void setConfigGoogleDriveAccount() {
        Config.readXMLConfig("enable-google-drive").setText(String.valueOf(jCheckBoxEnableGoogle.isSelected()));
        Config.saveXMLConfig();
    }

    private void setAccountInfo() {
        jLabelName.setText(Config.readXMLConfig("google-name").getText());
        jLabelEmail.setText(Config.readXMLConfig("google-email").getText());
    }

    private void deleteGoogleAccount() {
        Config.readXMLConfig("enable-google-drive").setText("false");
        Config.readXMLConfig("enable-api-personal").setText("false");
        Config.readXMLConfig("google-name").setText("");
        Config.readXMLConfig("google-email").setText("");
        Config.saveXMLConfig();
        Config.deleteDir(GDUtils.STORE_CONFIG_GOOGLE_DRIVE);
        GDUtils.SECRET_KEY.delete();
        getConfigGoogleDriveAccount();
    }
    //    /**
//     * @param args the command line arguments
//     */
//    public static void main(String args[]) {
//        try {
//            /* Set the System look and feel */
//            //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//            /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//             * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//             */
//            javax.swing.UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        } catch (ClassNotFoundException ex) {
//            ex.printStackTrace();
//            Logger.getLogger(JFramePreferences.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            ex.printStackTrace();
//            Logger.getLogger(JFramePreferences.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            ex.printStackTrace();
//            Logger.getLogger(JFramePreferences.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (UnsupportedLookAndFeelException ex) {
//            ex.printStackTrace();
//            Logger.getLogger(JFramePreferences.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//
//
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                JFramePreferences main = new JFramePreferences();
//                main.setLocationRelativeTo(null);
//                main.setVisible(false);
//            }
//        });
//    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAuth;
    private javax.swing.JButton jButtonClose;
    private javax.swing.JButton jButtonCopyKey;
    private javax.swing.JButton jButtonDeleteAccount;
    private javax.swing.JCheckBox jCheckBoxEnableGoogle;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabelEmail;
    private javax.swing.JLabel jLabelLocation;
    private javax.swing.JLabel jLabelName;
    private javax.swing.JPanel jPanelAbout;
    private javax.swing.JPanel jPanelAccount;
    private javax.swing.JPanel jPanelGeneral2;
    private javax.swing.JPanel jPanelGoogleDrive;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTabbedPane jTabbedPaneAccount;
    private javax.swing.JTextField jTextFieldDefaultLocation;
    // End of variables declaration//GEN-END:variables

    private void getKey() {
        int value = JOptionPane.showConfirmDialog(this, "Would you like to create a new key?", "Question", JOptionPane.YES_NO_OPTION);
        if (JOptionPane.YES_OPTION == value) {
            try {
                KeyManager.generateKey();
                int value1 = JOptionPane.showConfirmDialog(this, "Your key was generated and saved in the  configuration directory of application!\nIs recommended that you save your key in a smartphone using the application KeyManager \n or to save in another local!"
                        + "\nWould you like to save your key now?", "Information", JOptionPane.YES_NO_OPTION);
                if (JOptionPane.YES_OPTION == value1) {
                    saveKey();
                } else if (JOptionPane.NO_OPTION == value1) {
                }
            } catch (NoSuchAlgorithmException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            } catch (KeyStoreException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            } catch (CertificateException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            } catch (NoSuchPaddingException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            } catch (InvalidKeyException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        } else if (JOptionPane.NO_OPTION == value) {
            receiveKey();
        }
    }

    private void saveKey() {
        JDialogSaveKey saveKey = new JDialogSaveKey(this, true);
        saveKey.setLocationRelativeTo(null);
        saveKey.setVisible(true);
    }

    private void receiveKey() {
        JDialogReceiveKey receiveKey = new JDialogReceiveKey(this, true);
        receiveKey.setLocationRelativeTo(null);
        receiveKey.setVisible(true);
        if (!GDUtils.SECRET_KEY.exists()) {
            deleteGoogleAccount();
            JOptionPane.showMessageDialog(this, "Unable to get key!\n Your account has not been configured!", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    public GoogleDriveController getDriveController() {
        return driveController;
    }

    public void setDriveController(GoogleDriveController driveController) {
        this.driveController = driveController;
    }
    
    
}
