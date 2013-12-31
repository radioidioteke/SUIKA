package cl.suika.cineschile.utils;


import cl.suika.cineschile.gui.R;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;

public class MenusCreator {
	
	public static void setupActionBar(SherlockActivity aa) {
		ActionBar actionBar = aa.getSupportActionBar();
		actionBar.setDisplayOptions( ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP);
		actionBar.setCustomView(R.layout.layout_header);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayShowHomeEnabled(true);
		
		actionBar.setIcon(R.drawable.abs__ic_onepixel);
	}
	
	public static void setupActionBar(SherlockFragmentActivity aa) {
		ActionBar actionBar = aa.getSupportActionBar();
		actionBar.setDisplayOptions( ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP);
		actionBar.setCustomView(R.layout.layout_header);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayShowHomeEnabled(true);
		
		actionBar.setIcon(R.drawable.abs__ic_onepixel);
	}
	
	public static void setupActionBarDrawer(SherlockFragmentActivity aa) {
		ActionBar actionBar = aa.getSupportActionBar();
		actionBar.setDisplayOptions( ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP);
		actionBar.setCustomView(R.layout.layout_header);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		
		actionBar.setIcon(R.drawable.abs__ic_onepixel);
	}
	
	
	
	public static void menuUtils(com.actionbarsherlock.view.Menu menu){
		SubMenu subMenu = menu.addSubMenu(0, 0, 2, null);
		subMenu.add(0, 3, 3, "Notificaciones").setIcon(R.drawable.boton_notificaciones).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
		subMenu.add(0, 6, 6, "Perfil").setIcon(R.drawable.boton_perfil).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
		subMenu.add(0, 7, 7, "Información").setIcon(R.drawable.info_icon).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
		subMenu.add(0, 8, 8, "Cerrar Sesión").setIcon(R.drawable.boton_salir);

		MenuItem subMenuItem = subMenu.getItem();
		subMenuItem.setIcon(R.drawable.abs__ic_menu_moreoverflow_holo_light);
		subMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
	}

}
