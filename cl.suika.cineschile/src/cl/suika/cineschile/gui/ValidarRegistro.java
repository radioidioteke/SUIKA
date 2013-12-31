package cl.suika.cineschile.gui;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ValidarRegistro extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_validar_registro);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.validar_registro, menu);
		return true;
	}

}
