package cl.suika.cineschile.notificaciones;

import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.view.Window;
import android.widget.TextView;
import cl.suika.cineschile.R;


public class TextNotificationDialog extends Dialog {


	public TextNotificationDialog(Context context, String titulo, String text) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_notification_text);
		
		
		
		((TextView)findViewById(R.id.titulo)).setText(titulo);
		((TextView)findViewById(R.id.texto)).setText(Html.fromHtml(text));		
	}

}

