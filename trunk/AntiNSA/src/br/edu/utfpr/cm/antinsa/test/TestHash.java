/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.antinsa.test;

import br.edu.utfpr.cm.antinsa.security.HashGenerator;

/**
 *
 * @author junior
 */
public class TestHash {
    public static void main(String[] args) {
        System.out.println(HashGenerator.hashFile("/home/junior/junior.txt"));
    }
}
