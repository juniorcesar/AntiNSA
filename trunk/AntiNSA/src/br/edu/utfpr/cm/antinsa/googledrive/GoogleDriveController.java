/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.antinsa.googledrive;

import br.edu.utfpr.cm.antinsa.configuration.GDUtils;
import br.edu.utfpr.cm.antinsa.googledrive.GoogleDrive;
import br.edu.utfpr.cm.antinsa.configuration.Config;
import br.edu.utfpr.cm.antinsa.dao.DaoDataFile;
import br.edu.utfpr.cm.antinsa.security.KeyManager;
import br.edu.utfpr.cm.antinsa.security.SecretKeyAESCrypto;
import br.edu.utfpr.cm.antinsa.googledrive.DataFile;
import br.edu.utfpr.cm.antinsa.security.HashGenerator;
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
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author junior
 */
public class GoogleDriveController extends Thread {

    private DaoDataFile daoDataFile;
    private java.io.File[] files;
    private List<DataFile> dbFiles;
    private java.io.File dir;
    private GoogleDrive googleDrive;
    private List<File> cloudFiles;
    private java.io.File encryptedFile;
    private java.io.File decryptedFile;
    private SecretKeyAESCrypto cipher;

    public GoogleDriveController() {
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
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (!isInterrupted()) {
                if (cipher == null && GDUtils.SECRET_KEY.exists()) {
                    cipher = new SecretKeyAESCrypto();
                }

                if (Config.STORE_CONFIG.exists() && Util.verifyServiceConnection(GDUtils.URL_SERVICE)) {
                    try {
                        Config.STORE_DEFAULT.mkdirs();
                        GDUtils.CACHE_DIR.mkdirs();
                        //Sincroniza os arquivos locais com a nuvem
                        cloudSync();
                        //Atualiza base de dados com arquivos locais
                        localSync();
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    //A aplicacao nao esta devidamente configurada, e necessario reinicializar o aplicativo ou não há conexão com o serviço
                    interrupt();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void initServiceGoogleDrive() {
        try {
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
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
                    ex.printStackTrace();
                } catch (GeneralSecurityException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
                    ex.printStackTrace();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
                    ex.printStackTrace();
                }
            } else if (Config.readXMLConfig("enable-google-drive").getText().equals("true") && GoogleDriveOAuth.isValidCredential()) {
                try {
                    GoogleDriveOAuth.getCredential();
                    start();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
                    ex.printStackTrace();
                } catch (GeneralSecurityException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
                    ex.printStackTrace();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
                    ex.printStackTrace();
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
            ex.printStackTrace();
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

    private void localSync() {
        try {
            files = dir.listFiles();
            dbFiles = daoDataFile.listAll();
            for (java.io.File file : files) {
                if (!isTempFile(file.getAbsolutePath()) && !Util.getMimeType(file.getAbsolutePath()).equals("inode/directory") && !isOpen(file.getAbsolutePath().replace(" ", "\\ "))) {
                    DataFile dataFile = daoDataFile.getDataFile(file.getName());
                    if (dataFile != null) {
                        if (file.lastModified() > dataFile.getDate()) {

                            // String hash = HashGenerator.hashFile(encryptedFile.getAbsolutePath());
                            String hash = HashGenerator.hashFile(file.getAbsolutePath());
                            if (!dataFile.getHash().equals(hash)) {
                                encryptedFile = cipher.encrypt(file);
                                File fileUpdated = googleDrive.updateFile(encryptedFile, file.lastModified());
                                if (fileUpdated != null) {
                                    daoDataFile.update(file.getName(), file.lastModified(), HashGenerator.hashFile(file.getAbsolutePath()), fileUpdated.getMd5Checksum());
                                }
                            }
                        }
                    } else {
                        encryptedFile = cipher.encrypt(file);
                        File fileCreated = googleDrive.createFile(encryptedFile, file.lastModified());
                        if (fileCreated != null) {
                            daoDataFile.insert(file.getName(), file.length(), HashGenerator.hashFile(file.getAbsolutePath()), fileCreated.getMd5Checksum());
                        }
                    }
                }
                if (encryptedFile != null) {
                    encryptedFile.delete();
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
                    if (googleDrive.deleteFile(dataFile.getName())) {
                        daoDataFile.delete(dataFile.getName());
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void cloudSync() {
        try {
            dbFiles = daoDataFile.listAll();
            cloudFiles = googleDrive.getFilesDefaultFolder();
            for (File file : cloudFiles) {
                DataFile dataFile = daoDataFile.getDataFile(file.getTitle());
                if (dataFile != null) {
                    if (dataFile.getName().equals(file.getTitle()) && dataFile.getDate() < file.getModifiedByMeDate().getValue()) {
                        if (!dataFile.getCloudHash().equals(file.getMd5Checksum())) {
                            encryptedFile = googleDrive.saveFile(googleDrive.downloadFile(file.getDownloadUrl()), file.getTitle());
                            if (encryptedFile != null) {
                                decryptedFile = cipher.decrypt(encryptedFile);
                                decryptedFile.setLastModified(file.getLastViewedByMeDate().getValue());
                                daoDataFile.update(decryptedFile.getName(), decryptedFile.lastModified(), HashGenerator.hashFile(decryptedFile.getAbsolutePath()), file.getMd5Checksum());
                                encryptedFile.delete();
                            }
                        }
                    }
                } else {
                    if (file.getDownloadUrl() != null) {
                        encryptedFile = googleDrive.saveFile(googleDrive.downloadFile(file.getDownloadUrl()), file.getTitle());
                        if (encryptedFile != null) {
                            decryptedFile = cipher.decrypt(encryptedFile);
                            decryptedFile.setLastModified(file.getLastViewedByMeDate().getValue());
                            daoDataFile.insert(decryptedFile.getName(), decryptedFile.lastModified(), HashGenerator.hashFile(decryptedFile.getAbsolutePath()), file.getMd5Checksum());
                            encryptedFile.delete();
                        }
                    }
                }
            }
            for (DataFile dataFile : dbFiles) {
                int count = 0;
                for (File file : cloudFiles) {
                    if (dataFile.getName().equals(file.getTitle())) {
                        count = 1;
                    }
                }
                if (count == 0) {
                    daoDataFile.delete(dataFile.getName());
                    new java.io.File(Config.STORE_DEFAULT.getAbsolutePath() + "/" + dataFile.getName()).delete();
                }
            }

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

    public boolean isOpen(String path) {
        try {
            Runtime r = Runtime.getRuntime();
            Process p = r.exec("lsof " + path);

            Scanner scanner = new Scanner(p.getInputStream());
            scanner.useDelimiter("$$").next();
            return true;
        } catch (NoSuchElementException ex) {
            return false;
        } catch (IOException ex) {
            return false;
        }
    }
}
