package cl.suika.cineschile.database;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cl.suika.cineschile.gui.R;
import cl.suika.cineschile.notificaciones.TextNotificationDialog;
import cl.suika.cineschile.utils.Varios;

import com.adsdk.sdk.Const;
import com.adsdk.sdk.banner.InAppWebView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

@SuppressLint("SimpleDateFormat")
public class NotificationsConsults {
	Context contexto;

	public NotificationsConsults(Context contexto) {
		this.contexto = contexto;
		startDatabase(contexto);
	}

	public void updateNotification(int id) {
		Varios.database.execSQL("UPDATE notifications SET readed=1 WHERE _id=" + id);
	}

	public void deleteNotification(int id) {
		Varios.database.execSQL("DELETE FROM notifications WHERE _id=" + id);
	}
	
	public void deleteAllNotification() {
		Varios.database.execSQL("DELETE FROM notifications");
	}


	public void insertNotification(String jsonData) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd MM yyyy");
		String currentDateandTime = sdf.format(new Date());
		Varios.database.execSQL("INSERT INTO Notifications(data,readed,date) VALUES('" + jsonData + "',0,'" + currentDateandTime + "') ");
	}

	public void getNotifications(final LinearLayout contenedorNotificacion, final LayoutInflater layoutInflater) {

		Cursor c = Varios.database.rawQuery("SELECT _id,data,readed FROM notifications ORDER BY _id DESC;", null);
		while (c.moveToNext()) {
			final int id = c.getInt(0);
			String message = c.getString(1);
			int readed = c.getInt(2);
			try {
				View view = layoutInflater.inflate(R.layout.item_notification, null);
				JSONObject jsonNotification = new JSONObject(message);
				final String jsonAccion     = jsonNotification.getString("ac");
				final String notifTitle     = jsonNotification.getString("alert");

				((TextView) view.findViewById(R.id.notification_message)).setText(notifTitle);

				if(!ImageLoader.getInstance().isInited()){
					ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(contexto).build(); //.discCacheExtraOptions(width, width, CompressFormat.JPEG, 75, null)
					ImageLoader.getInstance().init(config);
				}
				DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.drawable.default_image).showImageForEmptyUri(R.drawable.default_image).showImageOnFail(R.drawable.default_image).cacheInMemory(true).cacheOnDisc(true).build();
				ImageView imagen = (ImageView) view.findViewById(R.id.catImage);
				ImageLoader.getInstance().displayImage(jsonNotification.getString("im"), imagen, options, null);

				if (readed == 1) {
					(view.findViewById(R.id.notification_notreaded)).setVisibility(View.GONE);
				}

				(view.findViewById(R.id.notification_item_container)).setOnClickListener(new OnClickListener() {
					public void onClick(View arg0) {
						updateNotification(id);
						goToAction(jsonAccion, notifTitle, contenedorNotificacion, layoutInflater);
					}
				});
				(view.findViewById(R.id.deleteNotification)).setOnClickListener(new OnClickListener() {
					public void onClick(View arg0) {
						displayDeleteDialog(id, contenedorNotificacion, layoutInflater);
					}
				});

				contenedorNotificacion.addView(view);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		c.close();
	}

	private void goToAction(String json, String title, LinearLayout contenedorNotificacion, LayoutInflater layoutInflater) {
		try {
			JSONObject jsonNotification = new JSONObject(json);
			String accion = jsonNotification.getString("t");
			String info = jsonNotification.getString("i");
			if (accion.equals("l")) {
				Intent intent = new Intent(contexto, InAppWebView.class);
				intent.putExtra(Const.REDIRECT_URI, info);
				contexto.startActivity(intent);
			}else if(accion.equals("t")){
				contenedorNotificacion.removeAllViews();
				getNotifications(contenedorNotificacion, layoutInflater);
				(new TextNotificationDialog(contexto,title,info)).show();
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void displayDeleteDialog(final int id, final LinearLayout contenedorNotificacion, final LayoutInflater layoutInflater) {
		new AlertDialog.Builder(contexto).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Borrar").setMessage("�Esta seguro que desea borrar la notificaci�n?").setPositiveButton("Si", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				deleteNotification(id);
				contenedorNotificacion.removeAllViews();
				getNotifications(contenedorNotificacion, layoutInflater);
			}
		}).setNegativeButton("No", null).show();
	}

	private void startDatabase(Context contexto) {
		if (Varios.database == null || !Varios.database.isOpen()) {
			DbHelper dbHelper = new DbHelper(contexto);
			Varios.database = dbHelper.getWritableDatabase();
		}
		Varios.database.execSQL("CREATE TABLE IF NOT EXISTS Notifications (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, data TEXT, readed INTEGER, date TEXT)");
	}
}
