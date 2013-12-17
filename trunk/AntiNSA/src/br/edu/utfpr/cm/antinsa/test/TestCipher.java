/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.antinsa.test;

import java.io.File;
import java.io.FileInputStream;
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
public class TestCipher {

    public static void loadKey() throws Exception {
        KeyGenerator keygen = KeyGenerator.getInstance("AES");

        SecretKey key = (SecretKey) keygen.generateKey();

        KeyStore ks = KeyStore.getInstance("JCEKS");
        ks.load(new FileInputStream(new File("chave.keystore")), "123456".toCharArray());
        System.out.println(ks.getType());

        SecretKey s = (SecretKey) ks.getKey("chave", "junior".toCharArray());

    }

    public static void generateKey() throws NoSuchAlgorithmException, KeyStoreException, IOException, CertificateException, NoSuchPaddingException, InvalidKeyException {
        SecretKey key = KeyGenerator.getInstance("AES").generateKey();

        KeyStore ks = KeyStore.getInstance("JCEKS");
        ks.load(null, null);
        KeyStore.SecretKeyEntry skEntry = new KeyStore.SecretKeyEntry(key);

        ks.setEntry("chave", skEntry,
                new KeyStore.PasswordProtection("junior".toCharArray()));
        FileOutputStream fos = new FileOutputStream("chave.keystore");
        ks.store(fos, "123456".toCharArray());
        fos.close();


        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);

    }
}
