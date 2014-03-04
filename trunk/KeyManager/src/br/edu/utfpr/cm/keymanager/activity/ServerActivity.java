package br.edu.utfpr.cm.keymanager.activity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import br.edu.utfpr.cm.keymanager.main.R;
import br.edu.utfpr.cm.keymanager.server.SocketServerKey;
import br.edu.utfpr.cm.keymanager.util.Config;
import br.edu.utfpr.cm.keymanager.util.Utils;

public class ServerActivity extends Activity {

	private Boolean status = false;
	private ImageButton btnPower;
	private TextView tvStatus;
	private TextView tvAddress;
	private int imgButton;
	private SocketServerKey server;
	private Thread serverThread;
	private Button btnshow;
	private Config config;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		config = new Config(getApplicationContext());
		setContentView(R.layout.activity_server);
		btnPower = (ImageButton) findViewById(R.id.imbPower);
		tvStatus = (TextView) findViewById(R.id.tvStatus);
		tvStatus.setText("Start");
		tvAddress = (TextView) findViewById(R.id.tvAddress);
		tvAddress.setText("Server stoped...");
		btnshow = (Button) findViewById(R.id.btnshow);

		btnPower.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (status == false
						&& Utils.isConnected(getApplicationContext())) {
					if (server == null) {
						server = new SocketServerKey(config,
								ServerActivity.this);
					}
					serverThread = new Thread(server);
					serverThread.start();
					imgButton = R.drawable.ic_power_off;
					btnPower.setImageResource(imgButton);
					status = true;
					tvStatus.setText("Stop");
					tvAddress.setText("Listening... IP "
							+ Utils.getIPAddress(true));

				} else if (Utils.isConnected(getApplicationContext())) {
					server.stopServer();
					imgButton = R.drawable.ic_power_on;
					btnPower.setImageResource(imgButton);
					status = false;
					tvStatus.setText("Start");
					tvAddress.setText("Server stoped...");
				} else {
					tvAddress
							.setText("The cell phone isn't connected to NetWork...");
				}

			}
		});
		btnshow.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder alert = new AlertDialog.Builder(
						ServerActivity.this);

				alert.setTitle("Stored keys");

				String[] keys = config.getKeysName();
				String msg = "";
				if (keys.length != 0) {
					for (String key : keys) {
						msg += key + "\n";
					}
					alert.setMessage(msg);
				} else {
					alert.setMessage("There isn't keys\n");
				}

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
		getMenuInflater().inflate(R.menu.server, menu);
		return true;
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putBoolean("status", status);
		savedInstanceState.putString("tvStatus", tvStatus.getText().toString());
		savedInstanceState.putString("tvAddress", tvAddress.getText()
				.toString());
		savedInstanceState.putInt("ic_power", imgButton);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		status = savedInstanceState.getBoolean("status");
		tvStatus.setText(savedInstanceState.getString("tvStatus"));
		tvAddress.setText(savedInstanceState.getString("tvAddress"));
		btnPower.setImageResource(savedInstanceState.getInt("ic_power"));
		imgButton = savedInstanceState.getInt("ic_power");
	}

}
