package cl.suika.cineschile.notificaciones;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import cl.suika.cineschile.database.NotificationsConsults;
import cl.suika.cineschile.utils.Varios;

public class ParseReceiver extends BroadcastReceiver {
	Context context;

	@SuppressLint("SimpleDateFormat")
	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;

		Bundle extras = intent.getExtras();
		String message = extras != null ? extras.getString("com.parse.Data") : "";
		if (message.length() != 0) {
			String jsonData = extras.getString("com.parse.Data");
			(new NotificationsConsults(context)).insertNotification(jsonData);
		}
		// Mandar actualizacion a las activity
		LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(context);
		Intent update = new Intent(Varios.PARSERECEIVER_UPDATE);
		broadcaster.sendBroadcast(update);
	}

}
