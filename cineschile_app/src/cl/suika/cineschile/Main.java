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
import cl.suika.cineschile.connection.AsyncResponse;
import cl.suika.cineschile.object.CineObj;
import cl.suika.cineschile.utils.CinesListAdapter;
//import cl.suika.chilediarios.connection.ConnectionBasicInfo;
//import cl.suika.chilediarios.main.DiarioObject;
//import cl.suika.chilediarios.main.DiariosListAdapter;
//import cl.suika.chilediarios.main.MainNewObject;
//import cl.suika.chilediarios.main.MainNoticiasLayout;
import cl.suika.cineschile.utils.MenuCreator;
//import cl.suika.chilediarios.utils.Varios;

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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MenuCreator.setupActionBarDrawer(this);
		setContentView(R.layout.activity_main);
		getSupportActionBar().hide();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void processFinish(Object[] output) {
		// TODO Auto-generated method stub
		
	}
	
	
	//MENU LATERAL
	private void setLeftMenu(final List<CineObj> cinesNombre) {
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		mDrawerList.setAdapter(new CinesListAdapter(this, R.layout.drawer_list_item, cinesNombre));
		mDrawerList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent intento = new Intent(Main.this, Cine.class);
				intento.putExtra("IDCINE", cinesNombre.get(arg2).getId());
				intento.putExtra("NOMBRECINE", cinesNombre.get(arg2).getCine());
				intento.putExtra("IDAD", cinesNombre.get(arg2).getAdCine());
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
