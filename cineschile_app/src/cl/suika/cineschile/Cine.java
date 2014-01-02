package cl.suika.cineschile;


import java.util.List;

import cl.suika.cineschile.connection.AsyncResponse;
import cl.suika.cineschile.utils.MenuCreator;
import cl.suika.cineschile.R;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.adsdk.sdk.banner.AdView;
import com.viewpagerindicator.TitlePageIndicator;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Cine extends SherlockFragmentActivity implements AsyncResponse  {
	
	private MenuItem refreshItem;
	private boolean isRefreshing = true;
	private boolean isShowContent;
	private String idCine;
	private String nombreCine;
	private String idAdserver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cine);
		
		Bundle extras = getIntent().getExtras();
		idCine     = extras.getString("IDCINE");
		nombreCine = extras.getString("NOMBRECINE");
		idAdserver   = extras.getString("IDAD");
		
		AdView mAdView = new AdView(this, "http://adserver.suika.cl/md.request.php", idAdserver);
		((LinearLayout) findViewById(R.id.dummieAlign)).removeAllViews();
		((LinearLayout) findViewById(R.id.dummieAlign)).addView(mAdView);
		
		
		MenuCreator.setupActionBar(this);
		((TextView) findViewById(R.id.actionBarText)).setText(nombreCine);
		((TextView) findViewById(R.id.actionBarText)).setVisibility(ImageView.VISIBLE);
		((ImageView) findViewById(R.id.actionBarLogo)).setVisibility(ImageView.GONE);
	}


	@Override
	public void processFinish(Object[] output) {
		
		generateMenu();
//		if (output instanceof String[]) {
//			Toast.makeText(this, "" + output[0], Toast.LENGTH_SHORT).show();
//			((TextView) findViewById(R.id.TextoEmptyContent)).setText(getString(R.string.connect_error_content));
//		} else {			
//			
//			MyFragmentPagerAdapter mMyFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), this, (List<DiarioCategoriaObject>) output[0], idAdserver);
//			ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
//			pager.setAdapter(mMyFragmentPagerAdapter);
//
//			TitlePageIndicator titleIndicator = (TitlePageIndicator) findViewById(R.id.titlesIndicator);
//			titleIndicator.setViewPager(pager);
//
//			if (!isShowContent) {
//				isShowContent = true;
//				hideLoading();
//			}
//		}
		
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
//					ConnectionGetDiario conn = new ConnectionGetDiario(Diario.this, Diario.this, idDiario);
//					conn.execute();
				}
			});
		}
	}
	
	
	/** PAGE ADAPTER **/
//	private static class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {
//		List<DiarioCategoriaObject> categorias;
//		String idAdserver;
//
//		public MyFragmentPagerAdapter(FragmentManager fm, Activity contexto, List<DiarioCategoriaObject> categorias, String idAdserver) {
//			super(fm);
//			this.categorias = categorias;
//			this.idAdserver = idAdserver;
//		}
//
//		@Override
//		public Fragment getItem(int index) {
//			return CategoriaFragment.newInstance(index, categorias, idAdserver);
//		}
//
//		@Override
//		public int getCount() {
//			return categorias.size();
//		}
//
//		@Override
//		public CharSequence getPageTitle(int position) {
//			return categorias.get(position).categoria;
//		}
//
//		@Override
//		public int getItemPosition(Object object) {
//			return POSITION_NONE;
//		}
//
//	}

}
