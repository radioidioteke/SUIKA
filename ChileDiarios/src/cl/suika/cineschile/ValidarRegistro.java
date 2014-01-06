package cl.suika.cineschile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cl.suika.cineschile.R;
import cl.suika.cineschile.connection.AsyncResponse;
import cl.suika.cineschile.connection.ConnectionResendValidarReg;
import cl.suika.cineschile.connection.ConnectionValidarRegistro;
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

public class ValidarRegistro extends SherlockActivity implements AsyncResponse{
	private LoadingDialog preloader;
	private String masterUser;
	private String typeComming;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_validar_registro);
		
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE|ActionBar.DISPLAY_SHOW_HOME|ActionBar.DISPLAY_HOME_AS_UP);
		MenusCreator.setupActionBar(this);
		
		preloader = new LoadingDialog(this);
		
		Bundle extras = getIntent().getExtras(); 
		masterUser  = extras.getString("user");	
		typeComming = extras.getString("COMMING");
		
		
		if(typeComming.equals("1")){
			((EditText) findViewById(R.id.inputC)).setVisibility(EditText.VISIBLE);
		}
		
		
		((TextView) findViewById(R.id.resend)).setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				if(typeComming.equals("1") && !Validators.isValidEmail( ((EditText) findViewById(R.id.inputC)).getText().toString() )){
					 Toast.makeText(ValidarRegistro.this, getString(R.string.registro_errorMail), Toast.LENGTH_SHORT).show(); 
				 }else{
					 preloader.show();
					 String user = typeComming.equals("1")?((EditText) findViewById(R.id.inputC)).getText().toString():masterUser;
					 ConnectionResendValidarReg conn = new ConnectionResendValidarReg(ValidarRegistro.this, ValidarRegistro.this, user);
					 conn.execute();
				 }
				
				
			}
		});
		
		
		((Button) findViewById(R.id.submit)).setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				String codigo = ((TextView) findViewById(R.id.input)).getText().toString();
				String user = typeComming.equals("1")?((EditText) findViewById(R.id.inputC)).getText().toString():masterUser;
				
				
			 if(typeComming.equals("1") && !Validators.isValidEmail( ((EditText) findViewById(R.id.inputC)).getText().toString() )){
				 Toast.makeText(ValidarRegistro.this, getString(R.string.registro_errorMail), Toast.LENGTH_SHORT).show(); 
			 }else if(!codigo.equals("")){
					preloader.show();
					ConnectionValidarRegistro regConnect = new ConnectionValidarRegistro(ValidarRegistro.this, ValidarRegistro.this, user, codigo);
					regConnect.execute();
				}else{
					Toast.makeText(getApplicationContext(), getString(R.string.validar_registro_errorCodigo), Toast.LENGTH_LONG).show();
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

	@SuppressLint("InlinedApi")
	@Override
	public void processFinish(Object[] output) {
		preloader.dismiss();
		if(output[0].equals("OK")){
			Toast.makeText(this, getString(R.string.validar_registro_okmessage), Toast.LENGTH_LONG).show();
			
			Intent intento = new Intent(this, Main.class);
			intento.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP); 
			intento.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(intento);
			finish();
		}else{
			Toast.makeText(this, ""+output[0], Toast.LENGTH_LONG).show();
		}
	}

}