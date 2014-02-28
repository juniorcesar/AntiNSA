package br.edu.utfpr.cm.keymanager.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

import br.edu.utfpr.cm.keymanager.main.R;
import br.edu.utfpr.cm.keymanager.util.Config;
import br.edu.utfpr.cm.keymanager.activity.ConfigActivity;
import br.edu.utfpr.cm.keymanager.activity.KeysActivity;
import br.edu.utfpr.cm.keymanager.activity.ServerActivity;
import android.os.Bundle;
import android.os.Environment;
import android.app.ActivityGroup;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.Menu;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActivityGroup {
	static TabHost tabHost;
	static int tab = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Resources res = getResources();
		tabHost = (TabHost) findViewById(R.id.tabhost);
		tabHost.setup(this.getLocalActivityManager());

		TabHost.TabSpec spec;
		Intent intent;

		intent = new Intent().setClass(this, ServerActivity.class);
		spec = tabHost.newTabSpec("0")
				.setIndicator("", res.getDrawable(R.drawable.ic_server))
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, ConfigActivity.class);
		spec = tabHost.newTabSpec("1")
				.setIndicator("", res.getDrawable(R.drawable.ic_ckm))
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, KeysActivity.class);
		spec = tabHost.newTabSpec("1")
				.setIndicator("", res.getDrawable(R.drawable.ic_keys))
				.setContent(intent);
		tabHost.addTab(spec);

		tabHost.setCurrentTab(0);
		setup();
	}

	private void setup() {
		try {
			openFileInput(Config.CONFIG_FILE);

		} catch (FileNotFoundException e) {
			Log.v("ERRO", "Arquivo não existe");
			Config config = new Config();
			try {
				config.generateXml(openFileOutput(config.CONFIG_FILE,
						Context.MODE_PRIVATE));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putInt("tab", tabHost.getCurrentTab());

	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		tabHost.setCurrentTab(savedInstanceState.getInt("tab"));
	}

}
