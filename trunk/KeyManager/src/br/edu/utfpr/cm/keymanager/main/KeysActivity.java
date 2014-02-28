package br.edu.utfpr.cm.keymanager.main;

import br.edu.utfpr.cm.keymanager.main.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class KeysActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_keys);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.keys, menu);
		return true;
	}

}
