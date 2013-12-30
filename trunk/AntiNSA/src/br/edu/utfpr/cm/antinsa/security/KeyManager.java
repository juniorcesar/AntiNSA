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

    private File file = null;

    public void generateKey() throws NoSuchAlgorithmException, FileNotFoundException, IOException {
        KeyGenerator keygen = KeyGenerator.getInstance("AES");
        keygen.init(128);
        SecretKey key = keygen.generateKey();
    }

    public static SecretKey loadKey() throws Exception {
        KeyGenerator keygen = KeyGenerator.getInstance("AES");
        SecretKey key = (SecretKey) keygen.generateKey();
        KeyStore ks = KeyStore.getInstance("JCEKS");
        ks.load(new FileInputStream(new File(GDUtils.SECRET_KEY_NAME)), "123456".toCharArray());
        System.out.println(ks.getType());
        SecretKey s = (SecretKey) ks.getKey("chave", "junior".toCharArray());
        return s;
    }

    public static void saveKey() throws NoSuchAlgorithmException, KeyStoreException, IOException, CertificateException, NoSuchPaddingException, InvalidKeyException {
        SecretKey key = KeyGenerator.getInstance("AES").generateKey();
        KeyStore ks = KeyStore.getInstance("JCEKS");
        ks.load(null, null);
        KeyStore.SecretKeyEntry skEntry = new KeyStore.SecretKeyEntry(key);
        ks.setEntry("chave", skEntry, new KeyStore.PasswordProtection("junior".toCharArray()));
        FileOutputStream fos = new FileOutputStream("chave.keystore");
        ks.store(fos, "123456".toCharArray());
        fos.close();
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
    }
}
