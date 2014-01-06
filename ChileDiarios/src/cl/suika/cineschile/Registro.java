package cl.suika.cineschile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cl.suika.cineschile.R;
import cl.suika.cineschile.connection.AsyncResponse;
import cl.suika.cineschile.connection.ConnectionRegistro;
import cl.suika.cineschile.utils.LoadingDialog;
import cl.suika.cineschile.utils.MenusCreator;
import cl.suika.cineschile.utils.Validators;
import cl.suika.cineschile.utils.Varios;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.facebook.AppEventsLogger;
import com.google.analytics.tracking.android.EasyTracker;

public class Registro extends SherlockActivity implements AsyncResponse{
	LoadingDialog preloader;
	

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registro);
		
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE|ActionBar.DISPLAY_SHOW_HOME|ActionBar.DISPLAY_HOME_AS_UP);
		MenusCreator.setupActionBar(this);
		
		//((EditText) findViewById(R.id.correo)).setText("felipe.hrdina@suika.cl");
		//((EditText) findViewById(R.id.pass)).setText("felipe.hrdina@suika.cl");
		//((EditText) findViewById(R.id.pass2)).setText("felipe.hrdina@suika.cl");
		
		
		preloader = new LoadingDialog(this);
		
		
		TextView terminos = (TextView) findViewById(R.id.terminos);
		terminos.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				startActivity(new Intent(Registro.this,TerminosActivity.class));
			}
		});
		
		
		TextView resend = (TextView) findViewById(R.id.resend);
		resend.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Intent intento = new Intent(Registro.this,ValidarRegistro.class);
				intento.putExtra("COMMING", "1");
				intento.putExtra("user", "" );
				startActivity(intento);				
			}
		});
		
		Button submit = (Button) findViewById(R.id.submit);
		submit.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				String correo = ((EditText) findViewById(R.id.correo)).getText().toString();
				String pass1  = ((EditText) findViewById(R.id.pass)).getText().toString();
				String pass2  = ((EditText) findViewById(R.id.pass2)).getText().toString();
				
				if( !((CheckBox) findViewById(R.id.terminosCheck)).isChecked() ){
					Toast.makeText(Registro.this, getString(R.string.registro_errorTerms), Toast.LENGTH_SHORT).show();	
				}else if(!Validators.isValidEmail(correo)){
					Toast.makeText(Registro.this, getString(R.string.registro_errorMail), Toast.LENGTH_SHORT).show();
				}else if(!pass1.equals(pass2)){
					Toast.makeText(Registro.this, getString(R.string.registro_errorPass), Toast.LENGTH_SHORT).show();
				}else if(pass1.length()<5){
					Toast.makeText(Registro.this, getString(R.string.registro_errorPassLen), Toast.LENGTH_SHORT).show();
				}else{
					preloader.show();
					ConnectionRegistro regConnect = new ConnectionRegistro(Registro.this, Registro.this, correo, pass1);
					regConnect.execute();
				}
			}
		});
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Varios.clearAllNotifications = false;
		AppEventsLogger.activateApp(this, getString(R.string.facebook_app_id));
		EasyTracker.getInstance(this).activityStart(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		 EasyTracker.getInstance(this).activityStop(this);
	}
	
	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		menu.add(Menu.NONE, 0, Menu.NONE, "").setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS).setCheckable(false).setEnabled(false);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId()==android.R.id.home){
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void processFinish(Object[] output) {
		preloader.dismiss();
		if(output[0].equals("OK")){
			Intent intento = new Intent(this,ValidarRegistro.class);
			intento.putExtra("COMMING", "0");
			intento.putExtra("user", ((EditText) findViewById(R.id.correo)).getText().toString() );
			startActivity(intento);
		}else{
			Toast.makeText(this, ""+output[0], Toast.LENGTH_LONG).show();
		}
	}
	
	
	
}