/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.antinsa.test;


import com.sun.crypto.provider.SunJCE;
import java.security.*;
import java.security.cert.*;
import javax.crypto.*;
import java.io.*;
import java.util.*;

public class KS {
    public static void main(String args[]) {

	// Verificar se os parametros estão correctos
	if (((args.length == 3) && (args[0].equals("l"))) || ((args.length == 4) && (args[0].equals("c")))) {
	    
	    try {
		// Requisitar uma keystore do tipo JKS
		KeyStore ks = KeyStore.getInstance("JKS");
		
		// Carregar a keystore a partir do ficheiro especificado
		try {
		    FileInputStream fks = new FileInputStream(args[2]);
		    ks.load(fks,args[1].toCharArray());
		    fks.close();
		} catch (FileNotFoundException e) {
		    System.out.println("Essa keystore não existe. Vai ser criada uma nova.");
		    ks.load(null,args[1].toCharArray());
		}
		  
		if (args[0].equals("l")) {

		    // Listar todos os aliases da keystore
		    Enumeration alias = ks.aliases();
		    while (alias.hasMoreElements()) {
			System.out.println(alias.nextElement());
		    }
		} else {

		    // Criar uma chave secreta para um determinado alias
		  
		    // Adicionar dinamicamente o provider ABA
		    Security.addProvider(new SunJCE());
		    
		    try {
			// Requisitar gerador de chaves para o IDEA
			KeyGenerator kgidea = KeyGenerator.getInstance("AES");

			// Criar uma fonte de números aleatórios segura
			SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");

			// Inicializar o mecanismo independentemente do algoritmo
			// O tamanho de chave pretendido é de 128 bits
			kgidea.init(128, sr);

			// Gerar a chave
			SecretKey sk = kgidea.generateKey();
			
			// Armazenar a chave na keystore
			ks.setKeyEntry(args[3],sk,args[1].toCharArray(),null);

		    } catch (NoSuchAlgorithmException e) {
			System.out.println("Não há nenhum gerador de chaves para o IDEA!");
		    } 

		}

		// Guardar a keystore
		FileOutputStream fks = new FileOutputStream(args[2]);
		ks.store(fks,args[1].toCharArray());
		fks.flush();
		fks.close();
		System.exit(0);
		    
	    } catch (NoSuchAlgorithmException e) {
		System.out.println("Não há nenhum KeyStore do tipo JKS!");
	    } catch (IOException e) {
		System.out.println("Erro na manipulação da keystore!");
	    } catch (CertificateException e) {
		System.out.println("Erro na manipulação dos certificados!");
	    } catch (KeyStoreException e) {
		System.out.println("A keystore foi inicializada incorretamente!");
	    } 
	} else {
	    System.out.println("Modo de utilização: java KS l <password> <keystore>");
	    System.out.println("                    java KS c <password> <keystore> <alias>");
	}
    }
}