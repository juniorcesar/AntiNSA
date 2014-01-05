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
import com.google.api.services.drive.model.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author junior
 */
public class GoogleDriveLocalController extends Thread {

    private DaoDataFile daoDataFile;
    private java.io.File[] files;
    private List<DataFile> dbFiles;
    private java.io.File dir;
    private GoogleDrive googleDrive;
    private List<File> cloudFiles;
    private java.io.File encrypt;
    private java.io.File decrypt;
    private SecretKeyAESCrypto cipher;

    public GoogleDriveLocalController() {
        try {
            daoDataFile = new DaoDataFile();
            dir = new java.io.File(Config.STORE_DEFAULT.getAbsolutePath());
            googleDrive = new GoogleDrive();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (GeneralSecurityException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            if (daoDataFile == null) {
                daoDataFile = new DaoDataFile();
            }
            if (!GDUtils.SECRET_KEY.exists()) {
                KeyManager.generateKey();
            }
            cipher = new SecretKeyAESCrypto();

            while (!isInterrupted()) {
                if (Config.STORE_CONFIG.exists()) {
                    if (googleDrive.hasStorage()) {
                        try {
                            Config.STORE_DEFAULT.mkdirs();
                            GDUtils.CACHE_DIR.mkdirs();
                            //Sincroniza os arquivos locais com a nuvem
//                    cloudSync();
                            //Atualiza base de dados com arquivos locais
                            localSync();
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        //Usuario nao possui mais espaco de armazenamento
                    }
                } else {
                    //A aplicacao nao esta devidamente configurada, e necessario reinicializar o aplicativo
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(GoogleDriveLocalController.class.getName()).log(Level.SEVERE, null, ex);
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

    private List<DataFile> getListLocalFiles() throws SQLException, ClassNotFoundException {
        if (daoDataFile == null) {
            daoDataFile = new DaoDataFile();
        }
        return daoDataFile.listAll();
    }

    private List<DataFile> getListCloudDataFile() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void localSync() {
        try {
            files = dir.listFiles();
            dbFiles = daoDataFile.listAll();
            for (java.io.File file : files) {
                if (!isTempFile(file.getAbsolutePath()) && !Util.getMimeType(file.getAbsolutePath()).equals("inode/directory")) {
                    if (daoDataFile.dataFileExists(file.getName())) {
                        for (DataFile dataFile : dbFiles) {
                            if (dataFile.getName().equals(file.getName()) && dataFile.getDate() != file.lastModified()) {
                                encrypt = cipher.encrypt(file);
                                String hash = HashGenerator.hashFile(encrypt.getAbsolutePath());
                                if (!dataFile.getHash().equals(hash)) {
                                    File fileModified = googleDrive.fileModified(encrypt, file.lastModified());
                                    daoDataFile.update(file.getName(), file.length(), file.lastModified(), HashGenerator.hashFile(file.getAbsolutePath()), fileModified.getMd5Checksum());
                                }
                            }
                        }
                    } else {
                        encrypt = cipher.encrypt(file);
                        File fileCreated = googleDrive.fileCreated(encrypt, file.lastModified());
                        daoDataFile.insert(file.getName(), file.length(), file.lastModified(), HashGenerator.hashFile(file.getAbsolutePath()), fileCreated.getMd5Checksum());

                    }
                }
            }
            for (DataFile dataFile : dbFiles) {
                int count = 0;
                for (java.io.File file : files) {
                    if (dataFile.getName().equals(file.getName())) {
                        count = 1;
                    }
                }
                if (count == 0) {
                    daoDataFile.delete(dataFile.getName());
                    googleDrive.fileDeleted(dataFile.getName());
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(GoogleDriveLocalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void cloudSync() {
        try {
            dbFiles = getListLocalFiles();
            cloudFiles = googleDrive.getFilesDefaultFolder();
            if (!GDUtils.SECRET_KEY.exists()) {
                KeyManager.generateKey();
            }
            SecretKeyAESCrypto cipher = new SecretKeyAESCrypto();
            for (File file : cloudFiles) {
                if (daoDataFile.dataFileExists(file.getTitle())) {
                    for (DataFile dataFile : dbFiles) {
                        if (dataFile.getName().equals(file.getTitle()) && dataFile.getDate() != file.getModifiedByMeDate().getValue()) {
                            if (!dataFile.getCloudHash().equals(file.getMd5Checksum())) {
//                                daoDataFile.update(file.getTitle(), file.size(), file.getModifiedByMeDate().getValue(), file.getMd5Checksum());
                            }
                        }
                    }
                } else {
//                    InputStream downloadFile = googleDrive.downloadFile(file.getDownloadUrl());
//                    java.io.File decrypt = cipher.decrypt(downloadFile);
//                    decrypt.setLastModified(file.getLastViewedByMeDate().getValue());
//                    daoDataFile.insert(decrypt.getName(), decrypt.length(), decrypt.lastModified(), HashGenerator.hashFile(decrypt.getAbsolutePath()), file.getMd5Checksum());
//                    saveFile(decrypt);
                }

            }
//            for (DataFile dataFile : dbFiles) {
//                int count = 0;
//                for (java.io.File file : files) {
//                    if (dataFile.getName().equals(file.getName())) {
//                        count = 1;
//                    }
//                }
//                if (count == 0) {
//                    daoDataFile.delete(dataFile.getName());
//                    googleDrive.fileDeleted(dataFile.getName());
//                }
//            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void saveFile(java.io.File file) throws FileNotFoundException, IOException {

        FileOutputStream out = new FileOutputStream(new java.io.File(Config.STORE_DEFAULT + file.getName()));
        InputStream input = new FileInputStream(file);
        int b;

        while ((b = input.read()) > -1) {
            out.write(b);
        }
        input.close();
        out.close();
    }
}
