package cl.suika.cineschile.utils;

import android.content.Context;
import cl.suika.cineschile.R;

public class Validators {
	
	
	public static  boolean isValidEmail(CharSequence target) {
	    if (target == null) {
	        return false;
	    } else {
	        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
	    }
	}

	public static String validateForm(Context contexto, String n, String a, String f, String p) {
		if (n.length() == 0 || a.length() == 0 || f.length() == 0 || p.length() == 0) {
			return contexto.getString(R.string.perfil_formErr);
		}
		return "Ok";
	}

	public static String validatePass(Context contexto, String pa, String p1, String p2) {
		if (p1.length() == 0 && p2.length() == 0) {
			return "Ok";
		}

		if (pa.length() == 0) {
			return contexto.getString(R.string.perfil_noPassAct);
		}

		if (!p1.equals(p2)) {
			return contexto.getString(R.string.perfil_passDiff);
		}

		if (p1.length() < 5) {
			return contexto.getString(R.string.perfil_shotPass);
		}
			
		return "Ok";
	}

}
