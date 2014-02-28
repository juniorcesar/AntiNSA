package br.edu.utfpr.cm.keymanager.security;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import android.os.Environment;

public class KeyManager {

	public static void generateKey() throws NoSuchAlgorithmException,
			KeyStoreException, IOException, CertificateException,
			NoSuchPaddingException, InvalidKeyException {
		KeyGenerator keygen = KeyGenerator.getInstance("AES");
		keygen.init(128);
		SecretKey key = keygen.generateKey();
		KeyStore ks = KeyStore.getInstance("JCEKS");
		ks.load(null, null);
		KeyStore.SecretKeyEntry skEntry = new KeyStore.SecretKeyEntry(key);
		ks.setEntry("antinsa", skEntry,
				new KeyStore.PasswordProtection("keymanager".toCharArray()));
		FileOutputStream fos = new FileOutputStream(new File(
				Environment.getExternalStorageDirectory().getAbsolutePath()+"/antinsa.keystore"));
		ks.store(fos, "keymanager".toCharArray());
		fos.close();
	}
}
