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

import br.edu.utfpr.cm.keymanager.activity.ServerActivity;
import br.edu.utfpr.cm.keymanager.util.Config;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

public class SocketServerKey extends Thread {
	private SSLServerSocketFactory factory;
	private SSLServerSocket server;
	private SSLSocket client;
	private static final int PORT = 7000;
	private final String[] enabledCipherSuites = { "SSL_DH_anon_WITH_RC4_128_MD5" };
	private static int typeMessage = 0;
	private static Config config;
	private ServerActivity parent;

	public SocketServerKey(Config config, ServerActivity parent) {
		super();
		this.config = config;
		this.parent = parent;
	}

	@Override
	public void run() {
		try {
			factory = (SSLServerSocketFactory) SSLServerSocketFactory
					.getDefault();
			server = (SSLServerSocket) factory.createServerSocket(PORT);
			server.setEnabledCipherSuites(enabledCipherSuites);
			while (!isInterrupted()) {
				client = (SSLSocket) server.accept();
				parent.runOnUiThread(new Runnable() {
					public void run() {
						AlertDialog.Builder alert = new AlertDialog.Builder(
								parent);

						alert.setTitle("Information");
						alert.setMessage("The address "
								+ client.getInetAddress().getHostAddress()
								+ " wants to connect to this server"
								+ "\n Would you like authorize this connection?");

						alert.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										try {
											dialog.dismiss();
											Connect c = new Connect(client,
													config, parent);
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								});
						alert.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										try {
											client.close();
										} catch (IOException e) {
											e.printStackTrace();
										}
									}
								});
						alert.show();
					}
				});

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
