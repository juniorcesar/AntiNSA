package br.edu.utfpr.cm.keymanager.server;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

import br.edu.utfpr.cm.keymanager.util.Config;
import android.content.Context;
import android.util.Log;

public class SocketServerKey extends Thread {
	private SSLServerSocketFactory factory;
	private SSLServerSocket server;
	private SSLSocket client;
	private static final int PORT = 7000;
	private final String[] enabledCipherSuites = { "SSL_DH_anon_WITH_RC4_128_MD5" };
	private static int typeMessage = 0;
	private static Config config;


	public SocketServerKey(Context context) {
		super();
		config = new Config(context);
	}

	@Override
	public void run() {
		try {
			factory = (SSLServerSocketFactory) SSLServerSocketFactory
					.getDefault();
			server = (SSLServerSocket) factory.createServerSocket(PORT);
			server.setEnabledCipherSuites(enabledCipherSuites);
			while (!isInterrupted()) {
				Log.v("junior", config.getSharedPreferencesKey("JUNIOR"));
				client = (SSLSocket) server.accept();
				Connect c = new Connect(client,config);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stopServer() {
		try {
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		interrupt();
	}

}
