package cl.suika.cineschile;

import java.util.List;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;
import cl.suika.cineschile.R;
import cl.suika.cineschile.connection.AsyncResponse;
import cl.suika.cineschile.connection.ConnectionBasicInfo;
import cl.suika.cineschile.main.DiarioObject;
import cl.suika.cineschile.main.DiariosListAdapter;
import cl.suika.cineschile.main.MainNewObject;
import cl.suika.cineschile.main.MainNoticiasLayout;
import cl.suika.cineschile.utils.MenusCreator;
import cl.suika.cineschile.utils.Varios;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.adsdk.sdk.banner.AdView;
import com.facebook.AppEventsLogger;
import com.google.analytics.tracking.android.EasyTracker;

public class Main extends SherlockFragmentActivity implements AsyncResponse {

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle actbardrawertoggle = null;
	private MenuItem refreshItem;
	private boolean isRefreshing = true;
	private boolean isFirstExec = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MenusCreator.setupActionBarDrawer(this);
		getSupportActionBar().hide();
		setContentView(R.layout.activity_main);
		
		AdView mAdView = new AdView(this, "http://adserver.suika.cl/md.request.php", getString(R.string.ad_home));
		((LinearLayout) findViewById(R.id.dummieAlign)).removeAllViews();
		((LinearLayout) findViewById(R.id.dummieAlign)).addView(mAdView);

	}

	@SuppressWarnings("unchecked")
	@Override
	public void processFinish(Object[] output) {

		if (output[0] instanceof String) {
			Toast.makeText(this, "" + output[1], Toast.LENGTH_SHORT).show();
			((ImageView) findViewById(R.id.splashImage)).setVisibility(ImageView.GONE);
			((TextView) findViewById(R.id.TextoEmptyContent)).setVisibility(ImageView.VISIBLE);

		} else if (output[1] instanceof List<?>) {
			LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			((ScrollView) findViewById(R.id.newsContainer)).removeAllViews();
			((ScrollView) findViewById(R.id.newsContainer)).addView(new MainNoticiasLayout(this, (List<MainNewObject>) output[1]), lp);
			if (isFirstExec) {
				isFirstExec = false;
				hideSplash();
				getSupportActionBar().show();
			}

			if (output[0] instanceof List<?>) {
				setLeftMenu((List<DiarioObject>) output[0]);
			}
		}

		if (isFirstExec) {
			getSupportActionBar().show();
		}
		generateMenu();

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
		MenusCreator.menuUtils(menu);
		refreshItem = menu.add(Menu.NONE, 99, Menu.NONE, "").setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
		generateMenu();
		
		(new ConnectionBasicInfo(this, this)).execute();
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == 3) {
			startActivity(new Intent(this, Notificaciones.class));
		}
		if (item.getItemId() == 6) {
			startActivity(new Intent(this, Perfil.class));
		}
		if (item.getItemId() == 7) {
			startActivity(new Intent(this, Info.class));
		}
		if (item.getItemId() == 8) {
			Varios.doLogOut(this);
		}

		if (item.getItemId() == android.R.id.home) {
			if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
				mDrawerLayout.closeDrawer(mDrawerList);
			} else {
				mDrawerLayout.openDrawer(mDrawerList);
			}
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		if (actbardrawertoggle != null)
			actbardrawertoggle.syncState();

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (actbardrawertoggle != null)
			actbardrawertoggle.onConfigurationChanged(newConfig);
	}

	private void hideSplash() {
		ViewAnimator viewAnimator = (ViewAnimator) findViewById(R.id.viewAnimator);
		viewAnimator.setInAnimation(AnimationUtils.loadAnimation(Main.this, android.R.anim.slide_in_left));
		viewAnimator.setOutAnimation(AnimationUtils.loadAnimation(Main.this, android.R.anim.slide_out_right));
		viewAnimator.showNext();
	}

	private void generateMenu() {
		if (isRefreshing) {
			isRefreshing = false;
			refreshItem.setActionView(R.layout.item_menu_refresh_loading);
		} else {
			isRefreshing = true;
			refreshItem.setActionView(R.layout.item_menu_refresh);
			LinearLayout a = (LinearLayout) refreshItem.getActionView().findViewById(R.id.abs__icon);
			a.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					generateMenu();
					ConnectionBasicInfo connect = new ConnectionBasicInfo(Main.this, Main.this);
					connect.execute();
				}
			});
		}
	}

	// //////////// Menu Lateral //////////// //
	private void setLeftMenu(final List<DiarioObject> diariosNames) {
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		mDrawerList.setAdapter(new DiariosListAdapter(this, R.layout.drawer_list_item, diariosNames));
		mDrawerList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent intento = new Intent(Main.this, Diario.class);
				intento.putExtra("IDDIARIO", diariosNames.get(arg2).id);
				intento.putExtra("NOMBREDIARIO", diariosNames.get(arg2).diario);
				intento.putExtra("IDAD", diariosNames.get(arg2).adDiario);
				startActivity(intento);
			}
		});
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(true);
		actbardrawertoggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.boton_menu_on, R.string.abs__action_bar_home_description, R.string.abs__action_bar_home_description) {
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
			}

			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
			}
		};
		mDrawerLayout.setDrawerListener(actbardrawertoggle);
		actbardrawertoggle.syncState();

	}

}
