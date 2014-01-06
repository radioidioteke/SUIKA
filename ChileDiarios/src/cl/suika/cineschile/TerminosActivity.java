package cl.suika.cineschile;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;
import cl.suika.cineschile.R;
import cl.suika.cineschile.utils.MenusCreator;
import cl.suika.cineschile.utils.Varios;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.facebook.AppEventsLogger;
import com.google.analytics.tracking.android.EasyTracker;

public class TerminosActivity extends SherlockActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_terminos_condiciones);
		
		((TextView) findViewById(R.id.terms)).setText( Html.fromHtml(getString(R.string.terminosycondicioneshmtl)) );
		
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE|ActionBar.DISPLAY_SHOW_HOME|ActionBar.DISPLAY_HOME_AS_UP);
		MenusCreator.setupActionBar(this);
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
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId()==android.R.id.home){
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		menu.add(Menu.NONE, 0, Menu.NONE, "").setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS).setCheckable(false).setEnabled(false);
		return super.onCreateOptionsMenu(menu);
	}

}