/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.antinsa.security;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author junior
 */
public class HashGenerator {

    private static String readFile(String path) {
        String text = "";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path));
            String line;
            try {
                while ((line = br.readLine()) != null) {
                    text = text + line + "\n";
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return text;
    }

    public static String hashFile(String path) {
        String fileContent = readFile(path);
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            md5.update(fileContent.getBytes());
            return new BigInteger(1, md5.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}