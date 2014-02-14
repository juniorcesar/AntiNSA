/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.antinsa.security;

import br.edu.utfpr.cm.antinsa.configuration.Config;
import br.edu.utfpr.cm.antinsa.configuration.GDUtils;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 * @author junior
 */
 public class SecretKeyAESCrypto {

    private Cipher cipher;
    private SecretKey key;

    public SecretKeyAESCrypto() throws Exception {
        key = KeyManager.loadKey();
        cipher = Cipher.getInstance("AES");
    }

    public File encrypt(File file) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);

            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            CipherInputStream cipherInputStream = new CipherInputStream(bufferedInputStream, cipher);
            File tempFile = new File(GDUtils.CACHE_DIR + "/" + file.getName());

            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(tempFile));

            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = cipherInputStream.read(bytes)) != -1) {
                bufferedOutputStream.write(bytes, 0, read);
            }

            bufferedOutputStream.close();
            bufferedInputStream.close();

            return tempFile;

        } catch (FileNotFoundException ex) {
            Logger.getLogger(SecretKeyAESCrypto.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SecretKeyAESCrypto.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(SecretKeyAESCrypto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public File decrypt(File file) {
        try {

            cipher.init(Cipher.DECRYPT_MODE, key);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            CipherInputStream cipherInputStream = new CipherInputStream(bufferedInputStream, cipher);

            File decryptedFile = new File(Config.STORE_DEFAULT.getAbsolutePath() + "/" + file.getName());
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(decryptedFile));

            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = cipherInputStream.read(bytes)) != -1) {
                bufferedOutputStream.write(bytes, 0, read);
            }

            bufferedOutputStream.close();
            bufferedInputStream.close();

            return decryptedFile;

        } catch (FileNotFoundException ex) {
            Logger.getLogger(SecretKeyAESCrypto.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SecretKeyAESCrypto.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(SecretKeyAESCrypto.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(SecretKeyAESCrypto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    //Cria uma instância da Classe Cipher que passará a utilizar o algoritmo lido do arquivo .properties

    public void initCipher() {
        try {
            cipher = Cipher.getInstance("AES");

            //Implementar o KeyManager para gerenciar as chaves que forem utilizadas
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            keygen.init(128);
            key = keygen.generateKey();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(SecretKeyAESCrypto.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(SecretKeyAESCrypto.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //Criar método para salvar as configurações em um arquivo .properties e depois realizar leitura por aqui
    private void loadProperties() {
    }
}
