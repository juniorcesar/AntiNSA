/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.antinsa.security;

import java.io.File;

/**
 *
 * @author junior
 */
public interface Crypto {
   public File encrypt(File file);
   public File decrypt(File file);
}
