package cl.suika.cineschile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import cl.suika.cineschile.R;
import cl.suika.cineschile.utils.MenusCreator;
import cl.suika.cineschile.utils.Varios;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.adsdk.sdk.Const;
import com.adsdk.sdk.banner.InAppWebView;
import com.facebook.AppEventsLogger;
import com.google.analytics.tracking.android.EasyTracker;

public class Info extends SherlockActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
		MenusCreator.setupActionBar(this);
		
		
		((ImageView) findViewById(R.id.mail)).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("text/html");
				intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "cotantacto@chilediarioapp.cl" });
				intent.putExtra(Intent.EXTRA_SUBJECT, "Contacto");
				intent.putExtra(Intent.EXTRA_TEXT, "");
				startActivity(Intent.createChooser(intent, "Enviar comentario"));
			}
		});
		((ImageView) findViewById(R.id.facebook)).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(Info.this, InAppWebView.class);
				intent.putExtra(Const.REDIRECT_URI, "https://www.facebook.com/pages/Chile-Diario/320913561431?fref=ts");
				startActivity(intent);
				
				//Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
				//startActivity(browserIntent);
			}
		});
		((ImageView) findViewById(R.id.twitter)).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(Info.this, InAppWebView.class);
				intent.putExtra(Const.REDIRECT_URI, "http://www.google.cl");
				startActivity(intent);
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
		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}




}
