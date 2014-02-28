package br.edu.utfpr.cm.keymanager.activity;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.crypto.NoSuchPaddingException;

import br.edu.utfpr.cm.keymanager.main.R;
import br.edu.utfpr.cm.keymanager.main.R.layout;
import br.edu.utfpr.cm.keymanager.main.R.menu;
import br.edu.utfpr.cm.keymanager.security.KeyManager;
import br.edu.utfpr.cm.keymanager.server.SocketServerKey;
import br.edu.utfpr.cm.keymanager.util.Utils;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class KeyGeneratorActivity extends Activity {

	private Button btnGenerateKey;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_key_generator);
		setTitle("Key Manager");
		Button btnGenerateKey = (Button) findViewById(R.id.btnGenerateKey);
		btnGenerateKey.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					KeyManager.generateKey();
					AlertDialog.Builder alert = new AlertDialog.Builder(
							KeyGeneratorActivity.this);

					alert.setTitle("Success");
					alert.setMessage("The key was successfully generated!");

					alert.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
								}
							});
					alert.show();
				} catch (Exception e) {
					AlertDialog.Builder alert = new AlertDialog.Builder(
							KeyGeneratorActivity.this);

					alert.setTitle("Error");
					alert.setMessage("The key cannot be generated!"+ e.getMessage());

					alert.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
								}
							});
					alert.show();
					
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.key_generator, menu);
		return true;
	}

}
