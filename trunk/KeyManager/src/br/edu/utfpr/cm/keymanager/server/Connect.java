package br.edu.utfpr.cm.keymanager.server;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;

import javax.net.ssl.SSLSocket;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import br.edu.utfpr.cm.keymanager.activity.ServerActivity;
import br.edu.utfpr.cm.keymanager.main.MainActivity;
import br.edu.utfpr.cm.keymanager.util.Config;
import br.edu.utfpr.cm.keymanager.util.Utils;

public class Connect {

	private SSLSocket clienteSocket = null;
	private ObjectInputStream ois = null;
	private ObjectOutputStream oos = null;
	private int count = 0;
	private final String[] enabledCipherSuites = { "SSL_DH_anon_WITH_RC4_128_MD5" };
	private static final int SEND_KEY = 1;
	private static final int RECEIVE_KEY = 2;
	private static int typeMessage = 0;
	private static Config config;
	private static String keyName = "";
	private ServerActivity parent;

	public Connect(SSLSocket clientSocket, Config config, ServerActivity parent)
			throws Exception {
		this.config = config;
		this.parent = parent;
		clienteSocket = clientSocket;
		clienteSocket.setEnabledCipherSuites(enabledCipherSuites);
		try {
			ois = new ObjectInputStream(clienteSocket.getInputStream());
			oos = new ObjectOutputStream(clienteSocket.getOutputStream());
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		while (count < 3) {
			count++;
			executeRequest();
		}
		closeConnection();
	}

	// Executa o pedido do cliente
	public void executeRequest() throws Exception {
		String mensagemOut = "";
		String mensagemIn = receiveMessage();

		if (mensagemIn.length() == 1 && count == 1) {
			if (Integer.valueOf(mensagemIn) == SEND_KEY) {
				typeMessage = SEND_KEY;
			} else if (Integer.valueOf(mensagemIn) == RECEIVE_KEY) {
				typeMessage = RECEIVE_KEY;
			}
		} else if (count == 2) {
			keyName = mensagemIn;

		} else {
			if (typeMessage == SEND_KEY) {

				if (!config.verifyKey(keyName)) {
					config.setSharedPreferences(keyName, mensagemIn);
					Log.v("junior", config.getSharedPreferencesKey("JUNIOR"));
					mensagemOut = "Key sent with success!";
					parent.runOnUiThread(new Runnable() {
						public void run() {
							AlertDialog.Builder alert = new AlertDialog.Builder(
									parent);

							alert.setTitle("Information");
							alert.setMessage("The key "
									+ keyName
									+ " was received from address "
									+ clienteSocket.getInetAddress()
											.getHostAddress() + ".");

							alert.setPositiveButton("Ok",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int whichButton) {
											try {
											} catch (Exception e) {
												e.printStackTrace();
											}
										}
									});
							alert.show();
						}
					});
				} else {
					mensagemOut = "0";
				}
			} else if (typeMessage == RECEIVE_KEY) {
				if (config.verifyKey(keyName)) {
					mensagemOut = config.getSharedPreferencesKey(keyName);
					parent.runOnUiThread(new Runnable() {
						public void run() {
							AlertDialog.Builder alert = new AlertDialog.Builder(
									parent);

							alert.setTitle("Information");
							alert.setMessage("The key "
									+ keyName
									+ " was sent to the address "
									+ clienteSocket.getInetAddress()
											.getHostAddress() + ".");

							alert.setPositiveButton("Ok",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int whichButton) {
											try {
											} catch (Exception e) {
												e.printStackTrace();
											}
										}
									});
							alert.show();
						}
					});
				} else {
					mensagemOut = "0";
				}
			}
			sendMessage(mensagemOut);
		}

	}

	// Escreve no stream
	public void sendMessage(String mensagem) throws IOException {
		oos.writeObject(mensagem);
		oos.flush();

	}

	// Lê o stream
	public String receiveMessage() throws OptionalDataException,
			ClassNotFoundException, IOException {
		String msg = null;
		msg = ois.readObject().toString();

		return msg;
	}

	// Libera os recursos
	public void closeConnection() {
		try {
			ois.close();
			oos.close();
			clienteSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Handler mHandler = new Handler() {
	// public void handleMessage(Message msg) {

	// }
	// }; // Thread Thread thread= new Thread() { public void run() { //Logic
	// // MHandler.sendEmptyMessage(0); } }
}
