package cl.suika.cineschile.gui;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class TerminosActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_terminos);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.terminos, menu);
		return true;
	}

}
