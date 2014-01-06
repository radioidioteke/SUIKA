package cl.suika.cineschile;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
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
import cl.suika.cineschile.connection.ConnectionGetNoticia;
import cl.suika.cineschile.utils.MenusCreator;
import cl.suika.cineschile.utils.Varios;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.adsdk.sdk.banner.AdView;
import com.facebook.AppEventsLogger;
import com.google.analytics.tracking.android.EasyTracker;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class DetalleNoticia extends SherlockActivity implements AsyncResponse {

	private MenuItem refreshItem;
	private boolean isRefreshing = true;
	private boolean isShowContent;
	private String idNoticias;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detalle_noticia);
		MenusCreator.setupActionBar(this);

		Bundle extras = getIntent().getExtras();
		idNoticias = extras.getString("IDNOTICIA");
		
		
		AdView mAdView = new AdView(this, "http://adserver.suika.cl/md.request.php", extras.getString("IDAD"));
		((LinearLayout) findViewById(R.id.dummieAlign)).removeAllViews();
		((LinearLayout) findViewById(R.id.dummieAlign)).addView(mAdView);
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
	public void processFinish(Object[] output) {
		generateMenu();
		if (output.length < 5) {
			Toast.makeText(this, "" + output[0], Toast.LENGTH_SHORT).show();
			((TextView) findViewById(R.id.TextoEmptyContent)).setText(getString(R.string.connect_error_content));
		} else {
			String bajada = (output[1]+"").length()>0?"<b>"+output[1]+"</b><br><br>":"";
			
			((TextView) findViewById(R.id.Titulo)).setText(Html.fromHtml(output[0] + ""));
			((TextView) findViewById(R.id.NoticiaText)).setText( Html.fromHtml(bajada+output[2] + "") );
			((TextView) findViewById(R.id.dateOwner)).setText(output[3] + "");
			

			if(!ImageLoader.getInstance().isInited()){
				ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build(); //.discCacheExtraOptions(width, width, CompressFormat.JPEG, 75, null)
				ImageLoader.getInstance().init(config);
			}
			DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.drawable.default_image).showImageForEmptyUri(R.drawable.default_image).showImageOnFail(R.drawable.default_image).cacheInMemory(true).cacheOnDisc(false).build();
			ImageLoader.getInstance().displayImage("" + output[4], ((ImageView) findViewById(R.id.Imagen)), options);

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

		ConnectionGetNoticia conn = new ConnectionGetNoticia(this, this, idNoticias);
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

	private void hideLoading() {
		ViewAnimator viewAnimator = (ViewAnimator) findViewById(R.id.viewAnimator);
		viewAnimator.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left));
		viewAnimator.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right));
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
					ConnectionGetNoticia conn = new ConnectionGetNoticia(DetalleNoticia.this, DetalleNoticia.this, idNoticias);
					conn.execute();
				}
			});
		}
	}

}
