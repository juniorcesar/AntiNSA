/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.antinsa.googledrive;

import br.edu.utfpr.cm.antinsa.configuration.GDUtils;
import br.edu.utfpr.cm.antinsa.configuration.Config;
import br.edu.utfpr.cm.antinsa.util.Util;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Tokeninfo;
import com.google.api.services.oauth2.model.Userinfo;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author junior
 */
public class GoogleDriveOAuth {

    private static FileDataStoreFactory dataStoreFactory;
    private static HttpTransport httpTransport;
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static Oauth2 oauth2;
    private static Credential credential;
    private static GoogleClientSecrets clientSecrets;
    private static GoogleAuthorizationCodeFlow flow;

    public static Credential authorize() throws IOException, GeneralSecurityException {
        if (!GDUtils.CLIENT_SECRETS.exists()) {
            GDUtils.createClientSecrets();
        }
        clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
                new InputStreamReader(new FileInputStream(GDUtils.CLIENT_SECRETS)));
//        if (clientSecrets.getDetails().getClientId().startsWith("Enter")
//                || clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
//            System.out.println("https://code.google.com/apis/console/ "
//                    + "into client_secrets.json");
//            System.exit(1);
//        }
        httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        dataStoreFactory = new FileDataStoreFactory(GDUtils.STORE_CONFIG_GOOGLE_DRIVE);
        flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientSecrets, GDUtils.SCOPES).setDataStoreFactory(
                dataStoreFactory).build();
        credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
        if (Config.readXMLConfig("google-name").getText().equals("")) {
            saveAccountInfo();
        }
        flow.createAndStoreCredential(new TokenResponse().setAccessToken(credential.getAccessToken()), "1");
        return credential;

    }

    private static void tokenInfo(String accessToken) {
        Tokeninfo tokeninfo;
        try {
            tokeninfo = oauth2.tokeninfo().setAccessToken(accessToken).execute();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static Oauth2 getOauth2() throws IOException, GeneralSecurityException {
        if (oauth2 == null) {
            getCredential();
            oauth2 = new Oauth2.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(
                    GDUtils.APPLICATION_NAME).build();
            tokenInfo(credential.getAccessToken());
        }
        return oauth2;
    }

    public static Credential getCredential() throws IOException, GeneralSecurityException {
        if (credential == null) {
            authorize();
        }
        return credential;
    }

    public static Userinfo userInfo() throws IOException, GeneralSecurityException {
        oauth2 = getOauth2();
        return oauth2.userinfo().get().execute();
    }

    private static void saveAccountInfo() throws IOException {
        if (Util.verifyServiceConnection(GDUtils.URL_SERVICE)) {
            try {
                Config.readXMLConfig("google-name").setText(userInfo().getName());
                Config.readXMLConfig("google-email").setText(userInfo().getEmail());
                Config.saveXMLConfig();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (GeneralSecurityException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static boolean isValidCredential() {
        try {
            if (!GDUtils.CLIENT_SECRETS.exists()) {
                GDUtils.createClientSecrets();
            }
            clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
                    new InputStreamReader(new FileInputStream(GDUtils.CLIENT_SECRETS)));
            try {
                httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            } catch (GeneralSecurityException ex) {
                Logger.getLogger(GoogleDriveOAuth.class.getName()).log(Level.SEVERE, null, ex);
            }
            dataStoreFactory = new FileDataStoreFactory(GDUtils.STORE_CONFIG_GOOGLE_DRIVE);
            flow = new GoogleAuthorizationCodeFlow.Builder(
                    httpTransport, JSON_FACTORY, clientSecrets, GDUtils.SCOPES).setDataStoreFactory(
                    dataStoreFactory).build();
            Credential loadCredential = flow.loadCredential("1");
            if (loadCredential != null) {
                return true;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
