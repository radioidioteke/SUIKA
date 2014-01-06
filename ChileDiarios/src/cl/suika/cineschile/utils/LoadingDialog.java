package cl.suika.cineschile.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import cl.suika.cineschile.R;


public class LoadingDialog extends Dialog {


	public LoadingDialog(Context context) {
		super(context,android.R.style.Theme_Translucent_NoTitleBar);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_loading);
		setCancelable(false);
	}

}

