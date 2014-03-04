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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.nio.channels.FileChannel;
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

    public static ObjectInputStream convertStringToStream(String str) throws IOException {
        ObjectInputStream c = new ObjectInputStream(new ByteArrayInputStream(str.getBytes()));
        return c;
    }
    
    public static byte[] ObjecttoBytes(Object object) {
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        try {
            java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(baos);
            oos.writeObject(object);
        } catch (java.io.IOException ioe) {
            ioe.printStackTrace();
        }
        System.out.println(baos.toString());
        return baos.toByteArray();
    }

    public static Object bytesToObject(byte[] bytes) {
        Object object = null;
        try {
            object = new java.io.ObjectInputStream(new java.io.ByteArrayInputStream(bytes)).readObject();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (java.lang.ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return object;
    }
    
    public static void copyFile(File source, File destination) throws IOException {  
     if (destination.exists())  
         destination.delete();  
  
     FileChannel sourceChannel = null;  
     FileChannel destinationChannel = null;  
  
     try {  
         sourceChannel = new FileInputStream(source).getChannel();  
         destinationChannel = new FileOutputStream(destination).getChannel();  
         sourceChannel.transferTo(0, sourceChannel.size(),  
                 destinationChannel);  
     } finally {  
         if (sourceChannel != null && sourceChannel.isOpen())  
             sourceChannel.close();  
         if (destinationChannel != null && destinationChannel.isOpen())  
             destinationChannel.close();  
    }  
}  
}
