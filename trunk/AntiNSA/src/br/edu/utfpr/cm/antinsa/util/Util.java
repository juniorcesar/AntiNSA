/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.antinsa.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;
import java.nio.file.Paths;

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
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            return false;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
        return false;
    }
}
