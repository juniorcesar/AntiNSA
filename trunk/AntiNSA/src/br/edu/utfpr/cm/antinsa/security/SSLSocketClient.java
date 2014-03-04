/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.antinsa.security;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.security.GeneralSecurityException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class SSLSocketClient {

    private ObjectOutputStream oos = null;
    private ObjectInputStream ois = null;
    private SSLSocket socket = null;
    private int port = 7000;
    private String host = "";
    final String[] enabledCipherSuites = {"SSL_DH_anon_WITH_RC4_128_MD5"};

    public void connect(String host) throws ConnectException, IOException, GeneralSecurityException {
        socket = createSSLSocket(host, port);
        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());
    }

    private SSLSocket createSSLSocket(String host, int porta) throws IOException, GeneralSecurityException {
        SSLContext context = SSLContext.getDefault();
        SSLSocketFactory ssf = context.getSocketFactory();
        socket = (SSLSocket) ssf.createSocket(host, porta);
        socket.setEnabledCipherSuites(enabledCipherSuites);
        return socket;
    }

    public void sendMessage(String msg) throws IOException {
        oos.writeObject(msg);
        oos.flush();
    }

    //read stream
    public String receiveMessage() throws IOException, ClassNotFoundException {
        String msg = ois.readObject().toString();
        return msg;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
    public void close() throws IOException{
        socket.close();
    }
    public boolean isClosed(){
        return socket.isClosed();
    }
}