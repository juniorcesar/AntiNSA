/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.antinsa.security;

import br.edu.utfpr.cm.antinsa.configuration.Config;
import br.edu.utfpr.cm.antinsa.configuration.GDUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
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
        KeyGenerator keygen = KeyGenerator.getInstance("AES");
        SecretKey key = (SecretKey) keygen.generateKey();
        KeyStore ks = KeyStore.getInstance("JCEKS");
        ks.load(new FileInputStream(GDUtils.SECRET_KEY), "keymanager".toCharArray());
        SecretKey s = (SecretKey) ks.getKey("antinsa", "keymanager".toCharArray());
        return s;
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
 
}
