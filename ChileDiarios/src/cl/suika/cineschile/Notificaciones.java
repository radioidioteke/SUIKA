package cl.suika.cineschile;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import cl.suika.cineschile.R;
import cl.suika.cineschile.database.NotificationsConsults;
import cl.suika.cineschile.utils.MenusCreator;
import cl.suika.cineschile.utils.Varios;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.adsdk.sdk.banner.AdView;
import com.facebook.AppEventsLogger;
import com.google.analytics.tracking.android.EasyTracker;
import com.parse.ParseAnalytics;

public class Notificaciones extends SherlockActivity {
	LinearLayout contenedorNotificacion;
	BroadcastReceiver receiver;
	LayoutInflater layoutInflater;
	Context contexto;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notifications);

		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP);
		MenusCreator.setupActionBar(this);

		ParseAnalytics.trackAppOpened(getIntent());
		initView();
		
		AdView mAdView = new AdView(this, getString(R.string.url_madServer), getString(R.string.ad_notifi));
		((LinearLayout) findViewById(R.id.dummieAlign)).removeAllViews();
		((LinearLayout) findViewById(R.id.dummieAlign)).addView(mAdView);
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		final MenuItem aa = menu.add(0, 4, 4, "Eliminar").setIcon(R.drawable.boton_basura).setActionView(R.layout.item_menu_delete).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
		LinearLayout a = (LinearLayout) aa.getActionView().findViewById(R.id.icon_delete);
		a.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onOptionsItemSelected(aa);
			}
		});
		return super.onCreateOptionsMenu(menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// if (item.getItemId() == 3) {
		// showHideConfig();
		// }
		//Log.i("TEST TEST TEST", "EA" + item.getItemId());
		if (item.getItemId() == 4) {
			new AlertDialog.Builder(contexto).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Borrar").setMessage("�Esta seguro que desea borrar todas las notificaciones?").setPositiveButton("Si", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					(new NotificationsConsults(Notificaciones.this)).deleteAllNotification();
					contenedorNotificacion.removeAllViews();
				}
			}).setNegativeButton("No", null).show();

		}

		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (Varios.clearAllNotifications) {
			finish();
			return;
		}
		LocalBroadcastManager.getInstance(this).registerReceiver((receiver), new IntentFilter(Varios.PARSERECEIVER_UPDATE));
		createNotificationViews();

		AppEventsLogger.activateApp(this, getString(R.string.facebook_app_id));
		EasyTracker.getInstance(this).activityStart(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
		EasyTracker.getInstance(this).activityStop(this);
	}

	private void initView() {
		this.contexto = this;

		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				createNotificationViews();
			}
		};

		contenedorNotificacion = (LinearLayout) findViewById(R.id.notification_container);
		layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// new ApplicationsConsults(this).getMenuIcons(int page, int country);
	}

	private void createNotificationViews() {
		contenedorNotificacion.removeAllViews();
		(new NotificationsConsults(this)).getNotifications(contenedorNotificacion, layoutInflater);
	}

	// private void showHideConfig() {
	// LinearLayout contenedorConfig = (LinearLayout)
	// findViewById(R.id.contenedorConfig);
	//
	// Log.i("ITEM PRESSED", " " + contenedorConfig.getLayoutParams().height);
	//
	// if (contenedorConfig.getLayoutParams().height == 0) {
	// HeightResizeAnimation anim = new HeightResizeAnimation(contenedorConfig,
	// Varios.cDpToPx(500, Notificaciones.this), true);
	// anim.setAnimationListener(new AnimationListener() {
	// public void onAnimationEnd(Animation arg0) {
	// }
	//
	// public void onAnimationRepeat(Animation arg0) {
	// }
	//
	// public void onAnimationStart(Animation arg0) {
	// }
	// });
	// anim.setFillAfter(true);
	// anim.setDuration(500);
	// contenedorConfig.startAnimation(anim);
	// } else {
	// HeightResizeAnimation anim = new HeightResizeAnimation(contenedorConfig,
	// Varios.cDpToPx(0, Notificaciones.this), false);
	// anim.setAnimationListener(new AnimationListener() {
	// public void onAnimationEnd(Animation arg0) {
	// }
	//
	// public void onAnimationRepeat(Animation arg0) {
	// }
	//
	// public void onAnimationStart(Animation arg0) {
	// }
	// });
	// anim.setFillAfter(true);
	// anim.setDuration(500);
	// contenedorConfig.startAnimation(anim);
	// }
	// }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK || event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			Varios.clearAllNotifications = true;
			finish();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

}
