package br.edu.utfpr.cm.keymanager.activity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import br.edu.utfpr.cm.keymanager.main.R;
import br.edu.utfpr.cm.keymanager.util.Config;
import android.os.Bundle;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ConfigActivity extends ListActivity {

	private static final String ABOUT = "About";

	private ListView lvOptions;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ckm);
		lvOptions = getListView();

		lvOptions.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, new String[] {"About" }));

		lvOptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
					AlertDialog.Builder alert = new AlertDialog.Builder(
							ConfigActivity.this);

					alert.setTitle("About");
					alert.setMessage("KeyManager is a application for manager the keys of the application AntiNSA. \n \n Developed by:    \n Junior Cesar de Oliveira \n Luiz Arthur Feitosa dos Santos");

					alert.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
								}
							});
					alert.show();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ckm, menu);
		return true;
	}

}
