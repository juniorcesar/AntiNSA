package br.edu.utfpr.cm.keymanager.activity;

import br.edu.utfpr.cm.keymanager.main.R;
import br.edu.utfpr.cm.keymanager.util.Config;
import android.os.Bundle;
import android.app.ListActivity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class KeysActivity extends ListActivity {
	private ListView lvKeys;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_keys);

		lvKeys = getListView();

		//Criar método para obter os nomes das chaves para exibir na tela as chaves existentes no sistema.

		lvKeys.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, new String[] { "" }));

		lvKeys.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Toast.makeText(getApplicationContext(),
						((TextView) view).getText(), Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.keys, menu);
		return true;
	}

}
