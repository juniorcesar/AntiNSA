/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.antinsa.service.googledrive;

import br.edu.utfpr.cm.antinsa.configuration.GDUtils;
import br.edu.utfpr.cm.antinsa.oauth.googledrive.GoogleDriveOAuth;
import br.edu.utfpr.cm.antinsa.configuration.Config;
import br.edu.utfpr.cm.antinsa.util.Util;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.ParentList;
import com.google.api.services.drive.model.ParentReference;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author junior
 */
public class GoogleDrive {

    private Credential credential;
    private HttpTransport httpTransport;
    private JsonFactory jsonFactory;
    private Drive service;

    public GoogleDrive() throws IOException, GeneralSecurityException {
        buildGoogleDriveService();
        verifyDefaultFolder();
    }

    private void buildGoogleDriveService() throws IOException, GeneralSecurityException {
        if (service == null) {
            httpTransport = new NetHttpTransport();
            jsonFactory = new JacksonFactory();
            credential = GoogleDriveOAuth.getCredential();
            service = new Drive.Builder(httpTransport, jsonFactory, credential).setApplicationName(GDUtils.APPLICATION_NAME).build();
        }
    }

    public void downloadAllFiles(List<File> files) throws IOException, GeneralSecurityException {
        buildGoogleDriveService();
        for (File file : files) {
            if (file.getDownloadUrl() != null) {
                InputStream downloadFile = downloadFile(service, file.getDownloadUrl());
                saveFile(downloadFile, file.getTitle());
            }
        }

    }

    private InputStream downloadFile(Drive service, String downloadUrl) {
        if (downloadUrl != null && downloadUrl.length() > 0) {
            try {
                HttpResponse resp =
                        service.getRequestFactory().buildGetRequest(new GenericUrl(downloadUrl))
                        .execute();
                return resp.getContent();
            } catch (IOException e) {
                // An error occurred.
                e.printStackTrace();
                return null;
            }
        } else {
            // The file doesn't have any content stored on Drive.
            return null;
        }
    }

    private void saveFile(InputStream input, String name) throws FileNotFoundException, IOException {

        FileOutputStream out = new FileOutputStream(new java.io.File("/home/junior/AntiNSA/" + name));

        int b;

        while ((b = input.read()) > -1) {
            out.write(b);
        }
        input.close();
        out.close();
    }

    public List<File> getFilesDefaultFolder() throws IOException, GeneralSecurityException {
        buildGoogleDriveService();
        List<File> result = new ArrayList<File>();
        String parentId = Config.readXMLConfig("folder-id").getText();
        Files.List request = service.files().list().setQ(" '" + parentId + "' in parents  and trashed=false");

        do {
            try {
                FileList files = request.execute();

                result.addAll(files.getItems());
                request.setPageToken(files.getNextPageToken());
            } catch (IOException e) {
                System.out.println("An error occurred: " + e);
                request.setPageToken(null);
            }

        } while (request.getPageToken() != null
                && request.getPageToken().length() > 0);
        return result;
    }

    private void createDefaultFolder() {
        try {
            buildGoogleDriveService();
            File body = new File();
            body.setTitle(GDUtils.DEFAULT_FOLDER_NAME);
            body.setMimeType("application/vnd.google-apps.folder");
            File folder = service.files().insert(body).execute();
            Config.readXMLConfig("folder-id").setText(folder.getId());
            Config.saveXMLConfig();
        } catch (GeneralSecurityException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void verifyDefaultFolder() {
        try {
            buildGoogleDriveService();
            Files.List request = service.files().list().setQ(
                    "mimeType='application/vnd.google-apps.folder' and trashed=false");
            FileList folders = request.execute();
            for (File folder : folders.getItems()) {
                if (folder.getTitle().equals(GDUtils.DEFAULT_FOLDER_NAME)) {
                    Config.readXMLConfig("folder-id").setText(folder.getId());
                    Config.saveXMLConfig();
                    return;
                }
            }
            createDefaultFolder();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (GeneralSecurityException ex) {
            ex.printStackTrace();
        }
    }

    private String getFolderId() {
        try {
            buildGoogleDriveService();
            Files.List request = service.files().list().setQ(
                    "mimeType='application/vnd.google-apps.folder' and trashed=false");
            FileList files = request.execute();
            for (File file : files.getItems()) {
                if (file.getTitle().equals(GDUtils.DEFAULT_FOLDER_NAME)) {
                    return file.getId();
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (GeneralSecurityException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public File getFileId(String filename) {
        try {
            buildGoogleDriveService();
            FileList files = service.files().list().execute();
            String parentId = Config.readXMLConfig("folder-id").getText();
            for (File file : files.getItems()) {
                if (file.getTitle().equals(filename)) {
                    for (ParentReference parent : file.getParents()) {
                        if (parent.getId().equals(parentId)) {
                            return file;
                        }
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (GeneralSecurityException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void fileCreated(java.io.File file, long lastModified) {
        String parentId = Config.readXMLConfig("folder-id").getText();
        try {
//            if (!Util.getMimeType(path).equals("inode/directory")) {
            File body = new File();
            body.setTitle(file.getName());
            body.setModifiedDate(new DateTime(lastModified));
            if (parentId == null) {
                verifyDefaultFolder();
            } else {
                body.setParents(
                        Arrays.asList(new ParentReference().setId(parentId)));
            }

            java.io.File fileContent = new java.io.File(file.getAbsolutePath());

            FileContent mediaContent = new FileContent(Util.getMimeType(file.getAbsolutePath()), fileContent);
            File fileCloud = service.files().insert(body, mediaContent).execute();
            System.out.println("Id do arquivo: " + fileCloud.getId());


        } catch (IOException ex) {
            Logger.getLogger(GoogleDrive.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void fileDeleted(String fileName) {
        try {
            File file = getFileId(fileName);
            if (file != null) {
                buildGoogleDriveService();
                service.files().delete(file.getId()).execute();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (GeneralSecurityException ex) {
            ex.printStackTrace();
        }
    }

    public void fileModified(java.io.File fileModified, long lastModified) {
        try {
            buildGoogleDriveService();
            File file = getFileId(fileModified.getName());
            if (file != null) {
                String parentId = Config.readXMLConfig("folder-id").getText();
                File body = new File();
                body.setTitle(fileModified.getName());
                body.setModifiedDate(new DateTime(lastModified));
                if (parentId == null) {
                    verifyDefaultFolder();
                } else {
                    body.setParents(
                            Arrays.asList(new ParentReference().setId(parentId)));
                }
                FileContent mediaContent = new FileContent(Util.getMimeType(fileModified.getAbsolutePath()), fileModified);
                service.files().update(file.getId(), body, mediaContent).execute();
            }
        } catch (IOException ex) {
            Logger.getLogger(GoogleDrive.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralSecurityException ex) {
            Logger.getLogger(GoogleDrive.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void list() {
        try {
            buildGoogleDriveService();
            ParentList parents = service.parents().list("0B4zDy88Nx4zRTTVETG5IZXJ0cDQ").execute();

            for (ParentReference parent : parents.getItems()) {
                System.out.println("File Id: " + parent.getId());
                System.out.println("File Id: " + parent.getKind());


            }
        } catch (IOException ex) {
            Logger.getLogger(GoogleDrive.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralSecurityException ex) {
            Logger.getLogger(GoogleDrive.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void files() {
        try {
            buildGoogleDriveService();
            FileList list = service.files().list().setQ(" '0B4zDy88Nx4zRTTVETG5IZXJ0cDQ' in parents  and trashed=false").execute();

            for (File file : list.getItems()) {
                System.out.println(file.getTitle());


            }
        } catch (IOException ex) {
            Logger.getLogger(GoogleDrive.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralSecurityException ex) {
            Logger.getLogger(GoogleDrive.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }
}
