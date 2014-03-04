/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.antinsa.security;

import br.edu.utfpr.cm.antinsa.configuration.Config;
import br.edu.utfpr.cm.antinsa.configuration.GDUtils;
import br.edu.utfpr.cm.antinsa.util.Util;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 *
 * @author junior
 */
public class KeyManager {

    public static SecretKey loadKey() throws Exception {
//        KeyGenerator keygen = KeyGenerator.getInstance("AES");
//        SecretKey key = (SecretKey) keygen.generateKey();
        KeyStore ks = KeyStore.getInstance("JCEKS");
        ks.load(new FileInputStream(GDUtils.SECRET_KEY), "keymanager".toCharArray());
        SecretKey key = (SecretKey) ks.getKey("antinsa", "keymanager".toCharArray());
        return key;
    }

    public static void generateKey() throws NoSuchAlgorithmException, KeyStoreException, IOException, CertificateException, NoSuchPaddingException, InvalidKeyException {
        KeyGenerator keygen = KeyGenerator.getInstance("AES");
        keygen.init(128);
        SecretKey key = keygen.generateKey();
        KeyStore ks = KeyStore.getInstance("JCEKS");
        ks.load(null, null);
        KeyStore.SecretKeyEntry skEntry = new KeyStore.SecretKeyEntry(key);
        ks.setEntry("antinsa", skEntry,
                new KeyStore.PasswordProtection("keymanager".toCharArray()));
        FileOutputStream fos = new FileOutputStream(new File(
                Config.STORE_CONFIG + "/antinsa.keystore"));
        ks.store(fos, "keymanager".toCharArray());
        fos.close();
    }

    public static void storeKey(File newfile) throws KeyStoreException, FileNotFoundException, IOException, NoSuchAlgorithmException, CertificateException, Exception {
        KeyStore ks = KeyStore.getInstance("JCEKS");
        ks.load(null, null);
        KeyStore.SecretKeyEntry skEntry = new KeyStore.SecretKeyEntry(loadKey());
        ks.setEntry("antinsa", skEntry,
                new KeyStore.PasswordProtection("keymanager".toCharArray()));
        FileOutputStream fos = new FileOutputStream(newfile);
        ks.store(fos, "keymanager".toCharArray());
        fos.close();
    }

    private static void storeKey(SecretKey key) throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException {
        KeyStore ks = KeyStore.getInstance("JCEKS");
        ks.load(null, null);
        KeyStore.SecretKeyEntry skEntry = new KeyStore.SecretKeyEntry(key);
        ks.setEntry("antinsa", skEntry,
                new KeyStore.PasswordProtection("keymanager".toCharArray()));
        FileOutputStream fos = new FileOutputStream(new File("/home/junior/antinsa.keystore"));
        ks.store(fos, "keymanager".toCharArray());
        fos.close();
    }

    public static File generateSecretKeyFile() throws FileNotFoundException, IOException, Exception {
        File secretKey = new File(Config.STORE_CONFIG + "/tempkey.txt");
        FileOutputStream fos = new FileOutputStream(secretKey);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(loadKey());
        fos.close();
        oos.close();
        return secretKey;
    }

    public static void storeSecretKeyFile(String content) throws FileNotFoundException, IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException, ClassNotFoundException {
        Util.saveToFile(Config.STORE_CONFIG + "/tempkey.txt", content);
        FileInputStream fis = new FileInputStream(Config.STORE_CONFIG + "/tempkey.txt");
        ObjectInputStream ois = new ObjectInputStream(fis);
        storeKey((SecretKey) ois.readObject());
        fis.close();
        ois.close();
    }
}
