/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.antinsa.security;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 *
 * @author junior
 */
public class SecretKeyAESCrypto implements Crypto {

    private Cipher cipher;
    /*É possível utilizar 3 parâmetros, no qual o primeiro parâmetro é o algortimo de critografia, o segundo é o modo com que será realizado 
     * (No caso o modo ECB fornece confidencia e não assegura integridade das informações),
     * o último é o esquema de preenchimento dos blocos. */
    private final String ALGORITHM = "AES";
    private SecretKey key;
    private File file;
    private BufferedInputStream bufferedInputStream;
    private BufferedOutputStream bufferedOutputStream;
    private CipherInputStream cipherInputStream;
    private File tempFile;
    private int size = 128;

    public SecretKeyAESCrypto() throws Exception {
        key = KeyManager.loadKey();
    }

//Tentar utilizar o cipher.dofinal(bytes[]);
    @Override
    public File encrypt(File file) {
        try {
            this.file = file;
            initCipher();
            key = KeyManager.loadKey();
            cipher.init(Cipher.ENCRYPT_MODE, key);
            bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            cipherInputStream = new CipherInputStream(bufferedInputStream, cipher);
            return getFile();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(SecretKeyAESCrypto.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SecretKeyAESCrypto.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(SecretKeyAESCrypto.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(SecretKeyAESCrypto.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(SecretKeyAESCrypto.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(SecretKeyAESCrypto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public File decrypt(File file) {
        try {
            initCipher();
            cipher.init(Cipher.DECRYPT_MODE, key);
            bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            cipherInputStream = new CipherInputStream(bufferedInputStream, cipher);
            return getFile();

        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(SecretKeyAESCrypto.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(SecretKeyAESCrypto.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SecretKeyAESCrypto.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SecretKeyAESCrypto.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(SecretKeyAESCrypto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    //Cria uma instância da Classe Cipher que passará a utilizar o algoritmo lido do arquivo .properties

    private void initCipher() throws NoSuchAlgorithmException, NoSuchPaddingException {
        cipher = Cipher.getInstance(ALGORITHM);

        //Implementar o KeyManager para gerenciar as chaves que forem utilizadas
        KeyGenerator keygen = KeyGenerator.getInstance(ALGORITHM);
        keygen.init(size);
        key = keygen.generateKey();

    }

    //Criar método para salvar as configurações em um arquivo .properties e depois realizar leitura por aqui
    private void loadProperties() {
    }

    private File getFile() throws FileNotFoundException, IOException {
        if (cipherInputStream != null && file.exists()) {

            //Encontrar uma maneira de salvar o arquivo em um File na memória
            tempFile = new File(file.getName());

            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(tempFile));
            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = cipherInputStream.read(bytes)) != -1) {
                bufferedOutputStream.write(bytes, 0, read);
            }
            bufferedOutputStream.close();
            bufferedInputStream.close();

            return tempFile;
        }

        return null;
    }
}
