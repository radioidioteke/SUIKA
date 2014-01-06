package cl.suika.cineschile;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;
import cl.suika.cineschile.R;
import cl.suika.cineschile.connection.AsyncResponse;
import cl.suika.cineschile.connection.ConnectionGetDiario;
import cl.suika.cineschile.diario.CategoriaFragment;
import cl.suika.cineschile.diario.DiarioCategoriaObject;
import cl.suika.cineschile.utils.MenusCreator;
import cl.suika.cineschile.utils.Varios;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.adsdk.sdk.banner.AdView;
import com.facebook.AppEventsLogger;
import com.google.analytics.tracking.android.EasyTracker;
import com.viewpagerindicator.TitlePageIndicator;

public class Diario extends SherlockFragmentActivity implements AsyncResponse {

	private MenuItem refreshItem;
	private boolean isRefreshing = true;
	private boolean isShowContent;
	private String idDiario;
	private String nombreDiario;
	private String idAdserver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_diario);

		Bundle extras = getIntent().getExtras();
		idDiario     = extras.getString("IDDIARIO");
		nombreDiario = extras.getString("NOMBREDIARIO");
		idAdserver   = extras.getString("IDAD");
		
		AdView mAdView = new AdView(this, "http://adserver.suika.cl/md.request.php", idAdserver);
		((LinearLayout) findViewById(R.id.dummieAlign)).removeAllViews();
		((LinearLayout) findViewById(R.id.dummieAlign)).addView(mAdView);
		
		
		MenusCreator.setupActionBar(this);
		((TextView) findViewById(R.id.actionBarText)).setText(nombreDiario);
		((TextView) findViewById(R.id.actionBarText)).setVisibility(ImageView.VISIBLE);
		((ImageView) findViewById(R.id.actionBarLogo)).setVisibility(ImageView.GONE);
		
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


	@SuppressWarnings("unchecked")
	@Override
	public void processFinish(Object[] output) {
		generateMenu();
		if (output instanceof String[]) {
			Toast.makeText(this, "" + output[0], Toast.LENGTH_SHORT).show();
			((TextView) findViewById(R.id.TextoEmptyContent)).setText(getString(R.string.connect_error_content));
		} else {			
			
			MyFragmentPagerAdapter mMyFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), this, (List<DiarioCategoriaObject>) output[0], idAdserver);
			ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
			pager.setAdapter(mMyFragmentPagerAdapter);

			TitlePageIndicator titleIndicator = (TitlePageIndicator) findViewById(R.id.titlesIndicator);
			titleIndicator.setViewPager(pager);

			if (!isShowContent) {
				isShowContent = true;
				hideLoading();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		MenusCreator.menuUtils(menu);
		refreshItem = menu.add(Menu.NONE, 99, Menu.NONE, "").setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
		generateMenu();

		ConnectionGetDiario conn = new ConnectionGetDiario(this, this, idDiario);
		conn.execute();
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
			finish();
			return true;
		}

		return super.onOptionsItemSelected(item);
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
					ConnectionGetDiario conn = new ConnectionGetDiario(Diario.this, Diario.this, idDiario);
					conn.execute();
				}
			});
		}
	}

	private void hideLoading() {
		ViewAnimator viewAnimator = (ViewAnimator) findViewById(R.id.viewAnimator);
		viewAnimator.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left));
		viewAnimator.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right));
		viewAnimator.showNext();
	}

	/** PAGE ADAPTER **/
	private static class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {
		List<DiarioCategoriaObject> categorias;
		String idAdserver;

		public MyFragmentPagerAdapter(FragmentManager fm, Activity contexto, List<DiarioCategoriaObject> categorias, String idAdserver) {
			super(fm);
			this.categorias = categorias;
			this.idAdserver = idAdserver;
		}

		@Override
		public Fragment getItem(int index) {
			return CategoriaFragment.newInstance(index, categorias, idAdserver);
		}

		@Override
		public int getCount() {
			return categorias.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return categorias.get(position).categoria;
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

	}

}
