package cl.suika.cineschile.connection;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
//import android.util.Log;
import cl.suika.cineschile.R;
import cl.suika.cineschile.utils.Varios;

@SuppressLint("SimpleDateFormat")
public class ConnectionUpdateUser extends AsyncTask<Void, Long, String> {

	private AsyncResponse delegate;
	private Activity contexto;
	
	private String name;
	private String apellido;
	private String sexo;
	private String fecha;
	private String pais;
	private String contrasenaActual;
	private String contrasena;


	public ConnectionUpdateUser(AsyncResponse delegate, Activity contexto) {
		this.delegate = delegate;
		this.contexto = contexto;
	}
	
	
	public void setUserStuff(String n, String a, String s, String f, String p, String ca, String c){
		name = n;
		apellido = a;
		sexo = s;
		fecha = f;
		pais = p;
		contrasenaActual = ca;
		contrasena = c;
	}
	

	@Override
	protected String doInBackground(Void... arg0) {
		
		RestHttpPost updateRequest = new RestHttpPost(contexto.getString(R.string.url_update_user));
		updateRequest.addPair("id_unico", Varios.getUUID(contexto));
		updateRequest.addPair("token", Varios.getMasterTokn(contexto));
		updateRequest.addPair("app", contexto.getString(R.string.rest_name));
		updateRequest.addPair("user", Varios.getMasterUsr(contexto));
		
		updateRequest.addPair("nombre", name);
		updateRequest.addPair("apellido", apellido);
		updateRequest.addPair("sexo", sexo);
		updateRequest.addPair("fecha_nacimiento", fecha);
		updateRequest.addPair("pais", pais);
		
		updateRequest.addPair("contrasena_actual",  contrasenaActual.length()!=0?Varios.sha1Hash(contrasenaActual):"");
		updateRequest.addPair("contrasena", contrasena.length()!=0?Varios.sha1Hash(contrasena):"");


		String response = updateRequest.getResponse();
		//Log.i("TEST TES RESPONSE", response);
		
		String result[] = parseResponse(response);
		
		if(result[2].equals("2")){
			Varios.doLogOut(contexto);
			return contexto.getString(R.string.connect_error);
		}
		
		if(result[0].equals("OK")){
			return "OK";
		}
		
		return result[1];

	}

	@Override
	protected void onPostExecute(String result) {
		delegate.processFinish( new Object[]{result});
	}
	
	
	private String[] parseResponse(String response){
		try {
			JSONObject jResponse = new JSONObject(response);
			String status  = jResponse.getString("STATUS");
			String detail  = jResponse.getString("DETAIL");
			String sNumber = jResponse.getString("STATUS_NUMBER");
			Varios.setMasterTokn(contexto, jResponse.getString("TOKEN") );
			
			return new String[]{status,detail,sNumber};
			
		} catch (JSONException e) {
			return new String[]{"ERROR",contexto.getString(R.string.connect_error),"-1"};
		}
	}

}
