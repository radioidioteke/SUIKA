
package cl.suika.cineschile.connection;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import cl.suika.cineschile.R;
import cl.suika.cineschile.main.DiarioObject;
import cl.suika.cineschile.main.MainNewObject;
import cl.suika.cineschile.utils.Varios;

@SuppressLint("SimpleDateFormat")
public class ConnectionBasicInfo extends AsyncTask<Void, Long, Object[]> {

	private AsyncResponse delegate;
	private Activity contexto;

	public ConnectionBasicInfo(AsyncResponse delegate, Activity contexto) {
		this.delegate = delegate;
		this.contexto = contexto;
	}

	@Override
	protected Object[] doInBackground(Void... arg0) {

		RestHttpPost updateRequest = new RestHttpPost(contexto.getString(R.string.url_basic_info));
		updateRequest.addPair("id_unico", Varios.getUUID(contexto));
		updateRequest.addPair("token", Varios.getMasterTokn(contexto));
		updateRequest.addPair("app", contexto.getString(R.string.rest_name));
		updateRequest.addPair("user", Varios.getMasterUsr(contexto));
		
		String response = updateRequest.getResponse();
		
		Log.i("TEST", response);

		if (response.equals("error")) {
			return new String[] { "Error", contexto.getString(R.string.connect_error) };
		}

		try {
			if (new JSONObject(response).getString("STATUS_NUMBER").equals("2")) {
				Varios.doLogOut(contexto);
				return  new String[] { "Error",contexto.getString(R.string.connect_error)};
			}
			
			parseAndStoreUser(new JSONObject(response).getJSONArray("USER"));
			
			
			JSONArray diarios = new JSONObject(response).getJSONArray("DIARIOS");
			List<DiarioObject> retornoDiario = new ArrayList<DiarioObject>();
			for (int i = 0; i < diarios.length(); i++) {
				JSONObject objeto = diarios.getJSONObject(i);
				retornoDiario.add(new DiarioObject(objeto.getString("ID"), objeto.getString("NOMBRE"),objeto.getString("AD_DIARIO"), objeto.getString("IMAGE")));
			}

			JSONArray noticias = new JSONObject(response).getJSONArray("NOTICIAS_HOME");
			List<MainNewObject> retornoNews = new ArrayList<MainNewObject>();
			for (int i = 0; i < noticias.length(); i++) {
				JSONObject objeto = noticias.getJSONObject(i);
				retornoNews.add(new MainNewObject(objeto.getString("ID"), objeto.getString("DIARIO"), objeto.getString("TITULO"), objeto.getString("BAJADA"), objeto.getString("AD_DIARIO")));
			}
			return new Object[] { retornoDiario, retornoNews };

		} catch (JSONException e) {
			e.printStackTrace();
			return new String[] { "Error", contexto.getString(R.string.connect_error) };
		}

	}

	@Override
	protected void onPostExecute(Object[] result) {
		delegate.processFinish(result);
	}
	
	private void parseAndStoreUser(JSONArray arreglo){
		for (int i = 0; i < arreglo.length(); i++) {
			
			try {
				JSONObject campoUser = arreglo.getJSONObject(i);
				String nombre = campoUser.getString("NOMBRE_CAMPO");
				String valor  = campoUser.getString("VALOR_CAMPO");
				
				if(valor.length()>0 && nombre.equals("Nombre")){ 
					Varios.setMasterName(contexto, valor);
				}else if(valor.length()>0 && nombre.equals("Apellido")){
					Varios.setMasterApellido(contexto, valor);
				}else if(valor.length()>0 && nombre.equals("Sexo")){
					if(valor.equals("Hombre")){
						Varios.setMasterSex(contexto, 0);
					}else{
						Varios.setMasterSex(contexto, 1);
					}
				}else if(valor.length()>0 && nombre.equals("Fecha Nacimiento")){
					//String[] splited = valor.split("-");
					//Varios.setMasterNac(contexto, splited[2]+"-"+splited[1]+"-"+splited[0]);
					Varios.setMasterNac(contexto, valor);
				}else if(valor.length()>0 && nombre.equals("Pais")){
					Varios.setMasterPais(contexto, getPosOfCountry(valor));
				}
				
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	
	private int getPosOfCountry(String valor){
		String[] es = contexto.getResources().getStringArray(R.array.perfil_country_arrays);
		for(int i=0; i<es.length;i++){
			if(valor.equals(es[i])) return i;
		}
		return 0;
		
	}

}
