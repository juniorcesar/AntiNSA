/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import br.edu.utfpr.cm.antinsa.configuration.Config;
import br.edu.utfpr.cm.antinsa.security.KeyManager;
import br.edu.utfpr.cm.antinsa.security.SSLSocketClient;
import br.edu.utfpr.cm.antinsa.util.Util;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKey;

/**
 *
 * @author junior
 */
public class NewClass {

    private static FileInputStream fis;

    public static void main(String[] args) {
        try {
//            
             SSLSocketClient client = new SSLSocketClient();
                            //                            JOptionPane.showMessageDialog(this, "Wait ... Establishing communication with the server!", "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
                            client.connect("192.168.0.101");
                            client.sendMessage("2");
                            client.sendMessage("JUNIOR");
                            client.sendMessage("");
                            String receiveMessage = client.receiveMessage();
                            System.out.println(receiveMessage);
//                              Util.saveToFile(Config.STORE_CONFIG + "/tempkey.txt", receiveMessage);
                            KeyManager.storeSecretKeyFile(receiveMessage);
//            SecretKey loadKey = KeyManager.loadKey();
//            getSecretKey(loadKey);
            fis = new FileInputStream(Config.STORE_CONFIG + "/tempkey.txt");
//            
//            String key = Util.convertInputStreamtoString(new FileInputStream(KeyManager.generateSecretKeyFile()));
//            System.out.println(receiveMessage);
//            System.out.println(key);
            System.out.println(getStringFromInputStream(fis));
            String loadFileAsString = loadFileAsString(Config.STORE_CONFIG + "/tempkey.txt");
//            Util.saveToFile(Config.STORE_CONFIG + "/tempkey.txt", loadFileAsString);
            System.out.println(loadFileAsString(Config.STORE_CONFIG + "/tempkey.txt"));
//            KeyStore ks = KeyStore.getInstance("JCEKS");
//            ks.load(new FileInputStream(new File("/home/junior/.antiNSA/antinsa.keystore")), "keymanager".toCharArray());
//            SecretKey s = (SecretKey) ks.getKey("antinsa", "keymanager".toCharArray());
//            //            serializaListaTenis(s);
//            SecretKey key = deserializaListaTenis();
//            Ke0yManager.storeKey(key);
//            

        } catch (Exception ex) {
            Logger.getLogger(NewClass.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
public static String loadFileAsString(String filename)
			throws java.io.IOException {
		final int BUFLEN = 1024;
		BufferedInputStream is = new BufferedInputStream(new FileInputStream(
				filename), BUFLEN);
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream(BUFLEN);
			byte[] bytes = new byte[BUFLEN];
			boolean isUTF8 = false;
			int read, count = 0;
			while ((read = is.read(bytes)) != -1) {
				if (count == 0 && bytes[0] == (byte) 0xEF
						&& bytes[1] == (byte) 0xBB && bytes[2] == (byte) 0xBF) {
					isUTF8 = true;
					baos.write(bytes, 3, read - 3); // drop UTF8 bom marker
				} else {
					baos.write(bytes, 0, read);
				}
				count += read;
			}
			return isUTF8 ? new String(baos.toByteArray(), "UTF-8")
					: new String(baos.toByteArray());
		} finally {
			try {
				is.close();
			} catch (Exception ex) {
			}
		}
	}
    public static SecretKey deserializaListaTenis() {
        FileInputStream fis = null;
        ObjectInputStream ois = null;

        SecretKey key = null;
        try {
            //arquivo onde estao os dados serializados
            fis = new FileInputStream(Config.STORE_CONFIG + "/tempkey.txt");

            //objeto que vai ler os dados do arquivo
            ois = new ObjectInputStream(fis);

            //recupera os dados
            key = (SecretKey) ois.readObject();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                fis.close();
                ois.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return key;
    }

    public static void getSecretKey(SecretKey key) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            //arquivo no qual os dados vao ser gravados
            fos = new FileOutputStream(Config.STORE_CONFIG + "/tempkey.txt");

            //objeto que vai escrever os dados
            oos = new ObjectOutputStream(fos);

            //escreve todos os dados
            oos.writeObject(key);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                fos.close();
                oos.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }
}
