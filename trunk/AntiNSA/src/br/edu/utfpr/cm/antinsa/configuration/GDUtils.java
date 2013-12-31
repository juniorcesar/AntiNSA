/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.antinsa.configuration;

import com.google.api.services.drive.DriveScopes;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.json.JSONObject;

/**
 *
 * @author junior
 */
public class GDUtils {

    public static final String APPLICATION_NAME = "APPJUNIOR";
    public static final String DEFAULT_FOLDER_NAME = "AntiNSA";
    public static final String CLIENT_ID = "440187793751.apps.googleusercontent.com";
    public static final String CLIENT_SECRET = "vmDcytBykuO0UZdxtKlv983g";
    public static final File SECRET_KEY = new File(Config.STORE_CONFIG + "/chave.keystore");
    public static final List<String> SCOPES = Arrays.asList(
            "https://www.googleapis.com/auth/userinfo.profile",
            "https://www.googleapis.com/auth/userinfo.email",
            DriveScopes.DRIVE_FILE,
            DriveScopes.DRIVE,
            DriveScopes.DRIVE_APPDATA);
    public static final File STORE_CONFIG_GOOGLE_DRIVE = new File(Config.STORE_CONFIG + "/googledrive");
    public static final File CLIENT_SECRETS = new File(STORE_CONFIG_GOOGLE_DRIVE + "/client_secrets.json");
    public static final File STORE_GOOGLE_DRIVE = new File(Config.STORE_DEFAULT + "/Google Drive");
    public static final String URL_SERVICE = "https://drive.google.com";

    public static void createClientSecrets() throws IOException {
        if (!STORE_CONFIG_GOOGLE_DRIVE.exists()) {
            STORE_CONFIG_GOOGLE_DRIVE.mkdirs();
        }
        if (!CLIENT_SECRETS.exists()) {
            CLIENT_SECRETS.createNewFile();
        }
        try (FileWriter writer = new FileWriter(CLIENT_SECRETS)) {
            JSONObject clientSecrets = new JSONObject();
            clientSecrets.put("client_secret", CLIENT_SECRET);
            clientSecrets.put("client_id", CLIENT_ID);
            JSONObject installed = new JSONObject();
            installed.put("installed", clientSecrets);
            writer.write(installed.toString());
            writer.flush();
            writer.close();
        }
    }
}
