/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.antinsa.util;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 *
 * @author junior
 */
public class Util {

    public static String getMimeType(String absolutePath) throws IOException {
        Path path = Paths.get(absolutePath);
        return java.nio.file.Files.probeContentType(path);
    }

    public static String getFileName(String relativePath) throws IOException {
        Path path = Paths.get(relativePath);
        return path.getFileName().toFile().getName();
    }

    public static String getRelativePath(String relativePathFile) throws IOException {
        Path path = Paths.get(relativePathFile);
        return path.getFileName().toFile().getAbsolutePath();
    }

    public static Boolean verifyServiceConnection(String address) {

        try {
            URL url = new URL(address);
            URLConnection conn = url.openConnection();

            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.connect();
            int x = httpConn.getResponseCode();
            if (x == 200) {
                return true;
            }
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
            return false;
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            return false;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
        return false;
    }

    public static String convertInputStreamtoString(InputStream is) {
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

    public void saveToFile(FileInputStream fis, String path) {
        try {
            FileOutputStream out = new FileOutputStream(path);
            int b;
            while ((b = fis.read()) > -1) {
                out.write(b);
            }
            fis.close();
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void createPane(Component comp, String msg) {
        JOptionPane pane = new JOptionPane(msg);
        final JDialog dialog = pane.createDialog(comp, "INFORMATION");


        Timer timer = new Timer(4000,
                new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                dialog.dispose();
            }
        });

        timer.setRepeats(false);
        timer.start();
        dialog.show();
        timer.stop();
        dialog.show(false);
    }

    public static void saveToFile(String path, String content) throws IOException {
        FileOutputStream fos = new FileOutputStream(path);
        fos.write(content.getBytes());
        fos.close();
    }
}
