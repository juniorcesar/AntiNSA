package br.edu.utfpr.cm.keymanager.server;

import java.io.InputStream;
import java.net.Socket;

import android.content.Intent;
import android.os.IBinder;

public interface ServerKey {
public boolean start();
public boolean stop();
public boolean restart();
public boolean send(InputStream in);
public String getInfo();
}
