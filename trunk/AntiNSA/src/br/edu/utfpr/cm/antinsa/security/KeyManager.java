/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.antinsa.security;

import br.edu.utfpr.cm.antinsa.configuration.Config;
import br.edu.utfpr.cm.antinsa.configuration.GDUtils;
import br.edu.utfpr.cm.antinsa.util.Util;
import java.beans.Encoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 *
 * @author junior
 */
public class KeyManager {

    public static SecretKey loadKey() throws Exception {
        KeyStore ks = KeyStore.getInstance("JCEKS");
        ks.load(new FileInputStream(GDUtils.SECRET_KEY), "keymanager".toCharArray());
        SecretKey key = (SecretKey) ks.getKey("antinsa", "keymanager".toCharArray());
        return key;
    }

    public static boolean isValidKey(String path) {
        try {
            KeyStore ks = KeyStore.getInstance("JCEKS");
            ks.load(new FileInputStream(path), "keymanager".toCharArray());
            SecretKey key = (SecretKey) ks.getKey("antinsa", "keymanager".toCharArray());
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

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
        FileOutputStream fos = new FileOutputStream(Config.STORE_CONFIG + "/antinsa.keystore");
        ks.store(fos, "keymanager".toCharArray());
        fos.close();
    }

    public static String generateSecretKeyFile() throws FileNotFoundException, IOException, Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(loadKey());
        oos.flush();
        oos.close();
        baos.flush();
        baos.close();
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encodeBuffer(baos.toByteArray());
    }

    public static void storeSecretKeyFile(String content) throws FileNotFoundException, IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException, ClassNotFoundException {
        BASE64Decoder decoder = new BASE64Decoder();
        ByteArrayInputStream bais = new ByteArrayInputStream(decoder.decodeBuffer(content));
        ObjectInputStream ois = new ObjectInputStream(bais);
        storeKey((SecretKey) ois.readObject());
        ois.close();
        bais.close();
    }
}
