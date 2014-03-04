package br.edu.utfpr.cm.keymanager.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import br.edu.utfpr.cm.keymanager.activity.ConfigActivity;
import android.R.string;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.OpenableColumns;
import android.util.Log;
import android.util.Xml;
import android.widget.TextView;
import android.widget.Toast;

public class Config {
	public static final String CONFIG = "KEY_MANAGER";
	private static Context context;
	private static SharedPreferences shared;

	public Config(Context context) {
		this.context = context;
		shared = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
	}

	public void setSharedPreferences(String name, String value) {
		Editor editor = shared.edit();
		editor.putString(name, value);
		editor.commit();
	}

	public String getSharedPreferencesKey(String key) {
		return shared.getString(key, "");
	}

	public boolean verifyKey(String name) {
		return shared.contains(name);
	}

	public String[] getKeysName() {
		int count = 0;
		Map<String, ?> map = shared.getAll();
		Set<String> set = map.keySet();
		String[] keys = new String[set.size()];

		for (String key : set) {
			keys[count] = key;
			count++;
		}

		return keys;
	}

}
