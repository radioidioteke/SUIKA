package cl.suika.cineschile.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.provider.Settings.Secure;
import cl.suika.cineschile.LogIn;

import com.facebook.Session;

public class Varios {
	
	public static String PARSERECEIVER_UPDATE = "cl.suika.chilediarios.ParseReceiver.UPDATENOTIFICATIONS";
	public static boolean clearAllNotifications = false;
	
	
	public static SQLiteDatabase database;
	private static String sharedUniUser 		= "ff11";
	private static String sharedMasterTkn 		= "ee11";
	private static String sharedConnectionTkn 	= "cc11";
	private static String sharedUpdate 			= "dd11";
	private static String sharedUser 			= "rr11";
	
	private static String sharedName 			= "wa11";
	private static String sharedApellido 		= "we23";
	private static String sharedSex 			= "ed19";
	private static String sharedNac 			= "wq1z";
	private static String sharedPais 			= "11d5";
	
	
	
	public static void setMasterName(Context contexto, String user){
		SharedPreferences prefs = contexto.getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(sharedName, user);
		editor.commit();
	}
	
	public static String getMasterName(Context contexto){
		SharedPreferences prefs = contexto.getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
		return prefs.getString(sharedName, "");
	}
	
	public static void setMasterApellido(Context contexto, String user){
		SharedPreferences prefs = contexto.getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(sharedApellido, user);
		editor.commit();
	}
	
	public static String getMasterApellido(Context contexto){
		SharedPreferences prefs = contexto.getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
		return prefs.getString(sharedApellido, "");
	}
	
	public static void setMasterSex(Context contexto, int sex){
		SharedPreferences prefs = contexto.getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(sharedSex, sex);
		editor.commit();
	}
	
	public static int getMasterSex(Context contexto){
		SharedPreferences prefs = contexto.getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
		return prefs.getInt(sharedSex, 0);
	}
	
	public static void setMasterNac(Context contexto, String user){
		SharedPreferences prefs = contexto.getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(sharedNac, user);
		editor.commit();
	}
	
	public static String getMasterNac(Context contexto){
		SharedPreferences prefs = contexto.getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
		return prefs.getString(sharedNac, "");
	}
	
	public static void setMasterPais(Context contexto, int pais){
		SharedPreferences prefs = contexto.getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(sharedPais, pais);
		editor.commit();
	}
	
	public static int getMasterPais(Context contexto){
		SharedPreferences prefs = contexto.getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
		return prefs.getInt(sharedPais, 0);
	}
	
	
	
	
	
	public static void setMasterUsr(Context contexto, String user){
		SharedPreferences prefs = contexto.getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(sharedUser, user);
		editor.commit();
	}
	
	public static String getMasterUsr(Context contexto){
		SharedPreferences prefs = contexto.getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
		return prefs.getString(sharedUser, "null");
	}
	
	
	
	public static String getFechaActualizacion(Context contexto){
		SharedPreferences prefs = contexto.getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
		String unique = prefs.getString(sharedUpdate, "null");
		
		if(unique.equals("null")){
			String date = "2010-01-01 00:00:01";
			SharedPreferences.Editor editor = prefs.edit();
			editor.putString(sharedUpdate, date);
			editor.commit();
			return date;
		}else{
			return unique;
		}
	}
	
	public static void setFechaActualizacion(Context contexto, String tnk){
		SharedPreferences prefs = contexto.getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(sharedUpdate, tnk);
		editor.commit();
	}
	
	
	public static String getUUID(Context contexto){
		SharedPreferences prefs = contexto.getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
		String unique = prefs.getString(sharedUniUser, "null");
		
		if(unique.equals("null")){
			Long tsLong = System.currentTimeMillis() / 1000;
			String android_id = Secure.getString(contexto.getContentResolver(), Secure.ANDROID_ID) + tsLong.toString();
			
			SharedPreferences.Editor editor = prefs.edit();
			editor.putString(sharedUniUser, android_id);
			editor.commit();
			return android_id;
		}else{
			return unique;
		}
	}
	
	public static void setMasterTokn(Context contexto, String tnk){
		SharedPreferences prefs = contexto.getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(sharedMasterTkn, tnk);
		editor.commit();
	}
	
	public static String getMasterTokn(Context contexto){
		SharedPreferences prefs = contexto.getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
		return prefs.getString(sharedMasterTkn, "null");
	}
	
	public static void setConnectionTokn(Context contexto, String tnk){
		SharedPreferences prefs = contexto.getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(sharedConnectionTkn, tnk);
		editor.commit();
	}
	
	
	public static String getConnectionTokn(Context contexto){
		SharedPreferences prefs = contexto.getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
		return prefs.getString(sharedConnectionTkn, "null");
	}
	
	
	@SuppressLint("InlinedApi")
	public static void doLogOut(Activity activity){
		setMasterTokn(activity, "null");
		setConnectionTokn(activity, "null");
		setMasterUsr(activity, "");
		setMasterName(activity, "");
		setMasterApellido(activity, "");
		setMasterSex(activity, -1);
		setMasterPais(activity, -1);
		setFechaActualizacion(activity, "2013-01-01 10:00:00");
		setMasterNac(activity, "");
		
		Session session = Session.getActiveSession();
	    if (session != null && (session.isOpened()) ) {
	    	session.closeAndClearTokenInformation();
	    }
		
		Intent intento = new Intent(activity, LogIn.class);
		intento.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP); 
		intento.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		activity.startActivity(intento);
		activity.finish();
	}
	
	
	public static int cDpToPx(float dips, Context context) {
		return (int) (dips * context.getResources().getDisplayMetrics().density + 0.5f);
	}

	public static String sha1Hash(String toHash) {
		String hash = null;
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			byte[] bytes = toHash.getBytes("UTF-8");
			digest.update(bytes, 0, bytes.length);
			bytes = digest.digest();
			StringBuilder sb = new StringBuilder();
			for (byte b : bytes) {
				sb.append(String.format("%02X", b));
			}
			hash = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return hash;
	}

}
