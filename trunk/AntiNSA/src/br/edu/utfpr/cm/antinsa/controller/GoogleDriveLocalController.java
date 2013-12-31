/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.antinsa.controller;

import br.edu.utfpr.cm.antinsa.oauth.googledrive.GoogleDriveOAuth;
import br.edu.utfpr.cm.antinsa.configuration.GDUtils;
import br.edu.utfpr.cm.antinsa.service.googledrive.GoogleDrive;
import br.edu.utfpr.cm.antinsa.configuration.Config;
import br.edu.utfpr.cm.antinsa.database.DaoDataFile;
import br.edu.utfpr.cm.antinsa.security.KeyManager;
import br.edu.utfpr.cm.antinsa.security.SecretKeyAESCrypto;
import br.edu.utfpr.cm.antinsa.service.googledrive.DataFile;
import br.edu.utfpr.cm.antinsa.util.HashGenerator;
import br.edu.utfpr.cm.antinsa.util.Util;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.swing.JOptionPane;

/**
 *
 * @author junior
 */
public class GoogleDriveLocalController extends Thread {

    private DaoDataFile daoDataFile;

    public GoogleDriveLocalController() {
        try {
            daoDataFile = new DaoDataFile();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {

            java.io.File dir = new File(Config.STORE_DEFAULT.getAbsolutePath());
            java.io.File[] files = dir.listFiles();
            List<DataFile> dbFiles;
//          List<DataFile> cloudFiles = getListCloudDataFile();
            GoogleDrive googleDrive = new GoogleDrive();
            if (!GDUtils.SECRET_KEY.exists()) {
                KeyManager.generateKey();
            }
            SecretKeyAESCrypto cipher = new SecretKeyAESCrypto();
            boolean tempFile;
            File encrypt;
            if (daoDataFile == null) {
                daoDataFile = new DaoDataFile();
            }
//            updateDatabase(files);
            while (!isInterrupted()) {
                try {
                    dbFiles = getListLocalDataFile();
                    files = dir.listFiles();
                    for (File file : files) {
                        if (!isTempFile(file.getAbsolutePath()) && !Util.getMimeType(file.getAbsolutePath()).equals("inode/directory")) {
                            if (daoDataFile.dataFileExists(file.getName())) {
                                for (DataFile dataFile : dbFiles) {
                                    if (dataFile.getName().equals(file.getName()) && dataFile.getDate() != file.lastModified()) {
                                        encrypt = cipher.encrypt(file);
                                        String hash = HashGenerator.hashFile(encrypt.getAbsolutePath());
                                        if (!dataFile.getHash().equals(hash)) {
                                            daoDataFile.update(file.getName(), file.length(), file.lastModified(), hash);
                                            googleDrive.fileModified(encrypt, file.lastModified());
                                        }
                                    }
                                }
                            } else {
                                encrypt = cipher.encrypt(file);
                                daoDataFile.insert(file.getName(), file.length(), file.lastModified(), HashGenerator.hashFile(encrypt.getAbsolutePath()));
                                googleDrive.fileCreated(encrypt, file.lastModified());
                            }
                        }
                    }
                    for (DataFile dataFile : dbFiles) {
                        int count = 0;
                        for (File file : files) {
                            if (dataFile.getName().equals(file.getName())) {
                                count = 1;
                            }
                        }
                        if (count == 0) {
                            daoDataFile.delete(dataFile.getName());
                            googleDrive.fileDeleted(dataFile.getName());
                        }
                    }

                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (GeneralSecurityException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
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

    private boolean isTempFile(String fileName) {
        char c1 = fileName.charAt(fileName.length() - 1);
        char c2 = fileName.charAt(0);
        if (c1 != '~' && c2 != '.') {
            return false;
        }
        return true;
    }

    private void updateDatabase(java.io.File[] files) {
        try {
            if (daoDataFile == null) {
                daoDataFile = new DaoDataFile();
            }
            for (File file : files) {
//                if (verifyFile(file.getAbsolutePath()) && file.isFile() && !daoDataFile.dataFileExists(file.getName())) {
//                    daoDataFile.insert(file.getName(), file.length(), file.lastModified(), HashGenerator.hashFile(file.getAbsolutePath()));
//                }
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private List<DataFile> getListLocalDataFile() throws SQLException, ClassNotFoundException {
        if (daoDataFile == null) {
            daoDataFile = new DaoDataFile();
        }
        return daoDataFile.listAll();
    }

    private List<DataFile> getListCloudDataFile() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
