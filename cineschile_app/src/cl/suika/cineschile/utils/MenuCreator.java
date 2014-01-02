package cl.suika.cineschile.utils;


import cl.suika.cineschile.R;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;

public class MenuCreator {
	
	public static void setupActionBar(SherlockActivity aa) {
		ActionBar actionBar = aa.getSupportActionBar();
		actionBar.setDisplayOptions( ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP);
		actionBar.setCustomView(R.layout.header_layout);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayShowHomeEnabled(true);
		
		actionBar.setIcon(R.drawable.abs__ic_onepixel);
	}
	
	public static void setupActionBar(SherlockFragmentActivity aa) {
		ActionBar actionBar = aa.getSupportActionBar();
		actionBar.setDisplayOptions( ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP);
		actionBar.setCustomView(R.layout.header_layout);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayShowHomeEnabled(true);
		
		actionBar.setIcon(R.drawable.abs__ic_onepixel);
	}
	
	public static void setupActionBarDrawer(SherlockFragmentActivity aa) {
		ActionBar actionBar = aa.getSupportActionBar();
		actionBar.setDisplayOptions( ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP);
		actionBar.setCustomView(R.layout.header_layout);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		
		actionBar.setIcon(R.drawable.abs__ic_onepixel);
	}
	

}
