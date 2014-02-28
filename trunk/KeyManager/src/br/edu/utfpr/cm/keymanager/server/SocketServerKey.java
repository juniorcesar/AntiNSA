package br.edu.utfpr.cm.keymanager.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class SocketServerKey extends Thread {
	private static ServerSocket serverSocket = null;
	private Socket socket;
	private static final int PORT = 7000;

	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(PORT);
			System.out.print("Endereco "
					+ serverSocket.getInetAddress().getHostAddress());

			while (!isInterrupted()) {
				socket = serverSocket.accept();
				 BufferedReader in = new BufferedReader(
				 new InputStreamReader(socket.getInputStream()));
				
				 String str = in.readLine();
				
				 Log.i("received response from server", str);
				
				 in.close();
				socket.close();
			}
		} catch (IOException e) {
			Log.e("S", e.getMessage());
		} catch (Exception e) {
			Log.e("S", e.getMessage());
		}
	}
	

	public void stopServer(){
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		interrupt();
	}

}
