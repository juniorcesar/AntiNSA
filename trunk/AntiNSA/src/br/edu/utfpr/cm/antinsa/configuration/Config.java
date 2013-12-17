/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.antinsa.configuration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

/**
 *
 * @author junior
 */
public class Config {

    public static final File STORE_DEFAULT = new File(System.getProperty("user.home"), "/AntiNSA");
    public static final File STORE_CONFIG = new File(System.getProperty("user.home"), ".antiNSA");
    public static final File XML_CONFIG = new File(STORE_CONFIG + "/config.xml");
    private static Element config;
    private static Document document;

    public static void setup() throws JDOMException, IOException {
        if (!STORE_DEFAULT.exists()) {
            STORE_DEFAULT.mkdirs();
        }
        if (!STORE_CONFIG.exists()) {
            STORE_CONFIG.mkdirs();
        }
        if (!XML_CONFIG.exists()) {
            createXMLConfig();
        }
        startXMLConfig();

    }

    private static void startXMLConfig() {
        if (XML_CONFIG.exists()) {
            try {
                SAXBuilder sb = new SAXBuilder();
                document = sb.build(XML_CONFIG);
                config = document.getRootElement();
            } catch (JDOMException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            createXMLConfig();
        }
    }

    public static void saveXMLConfig() {
        try {
            XMLOutputter xout = new XMLOutputter();
            xout.output(document, new FileWriter(XML_CONFIG));
        } catch (IOException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Element readXMLConfig(String tag) {
        List elements = config.getChildren();
        Iterator i = elements.iterator();
        while (i.hasNext()) {
            Element element = (Element) i.next();
            if (element.getName().equals(tag)) {
                return element;
            }
            if (element.getChild(tag) != null) {
                return element.getChild(tag);
            }
        }
        return null;
    }

    private static void createXMLConfig() {
        config = new Element("config");
        Element googleDrive = new Element("google-drive");
        Element enable = new Element("enable-google-drive");
        Element enableAPIPersonal = new Element("enable-api-personal");
        Element googleName = new Element("google-name");
        Element googleEmail = new Element("google-email");
        Element folderId = new Element("folder-id");
        enable.setText("false");
        enableAPIPersonal.setText("false");
        googleDrive.addContent(enable);
        googleDrive.addContent(enableAPIPersonal);
        googleDrive.addContent(googleName);
        googleDrive.addContent(googleEmail);
        googleDrive.addContent(folderId);
        config.addContent(googleDrive);
        document = new Document(config);
        saveXMLConfig();
    }

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                new File(dir + "/" + children[i]).delete();
            }
        }
        // Agora o diretório está vazio, restando apenas deletá-lo.  
        return dir.delete();
    }

    public static String setLibraryPath() {
        String arch = System.getProperty("os.version");
//        System.setProperty("java.library.path", System.getProperty("java.library.path") + ":/home/junior/.antiNSA/64");
//        System.out.println(System.getProperty("java.library.path"));
//        System.out.println(arch);
        return arch;
    }
}
