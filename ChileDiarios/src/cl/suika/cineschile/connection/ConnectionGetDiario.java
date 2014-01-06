package cl.suika.cineschile.connection;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
//import android.util.Log;
import cl.suika.cineschile.R;
import cl.suika.cineschile.diario.DiarioCategoriaNoticiaObject;
import cl.suika.cineschile.diario.DiarioCategoriaObject;
import cl.suika.cineschile.utils.Varios;

public class ConnectionGetDiario extends AsyncTask<Void, Long, Object[]> {

	private AsyncResponse delegate;
	private Activity contexto;
	private String idDiario;

	public ConnectionGetDiario(AsyncResponse delegate, Activity contexto, String idDiario) {
		this.delegate = delegate;
		this.contexto = contexto;
		this.idDiario = idDiario;
	}

	@Override
	protected Object[] doInBackground(Void... arg0) {

		RestHttpPost updateRequest = new RestHttpPost(contexto.getString(R.string.url_get_diario));
		updateRequest.addPair("id_diario", idDiario);
		updateRequest.addPair("id_unico", Varios.getUUID(contexto));
		updateRequest.addPair("token", Varios.getMasterTokn(contexto));
		updateRequest.addPair("app", contexto.getString(R.string.rest_name));
		updateRequest.addPair("user", Varios.getMasterUsr(contexto));
		
		String response = updateRequest.getResponse();
		//Log.i("TEST TES RESPONSE", response);

		if (response.equals("error")) {
			return new String[] { contexto.getString(R.string.connect_error) };
		}

		try {
			if (new JSONObject(response).getString("STATUS_NUMBER").equals("2")) {
				Varios.doLogOut(contexto);
				return  new String[] { "Error",contexto.getString(R.string.connect_error)};
			}
			
			JSONArray categorias = new JSONObject(response).getJSONArray("CATEGORIAS");
			List<DiarioCategoriaObject> retornoNews = new ArrayList<DiarioCategoriaObject>();
			for (int i = 0; i < categorias.length(); i++) {
				JSONObject objeto = categorias.getJSONObject(i);

				List<DiarioCategoriaNoticiaObject> listaNoticias = new ArrayList<DiarioCategoriaNoticiaObject>();
				JSONArray noticias = objeto.getJSONArray("NOTICIA");
				for (int j = 0; j < noticias.length(); j++) {
					JSONObject noticia = noticias.getJSONObject(j);
					listaNoticias.add(new DiarioCategoriaNoticiaObject(noticia.getString("ID"), noticia.getString("HORA"), noticia.getString("TITULO")));
				}
				retornoNews.add(new DiarioCategoriaObject(objeto.getString("CATEGORIA"), listaNoticias));
			}
			return new Object[] { retornoNews };

		} catch (JSONException e) {
			e.printStackTrace();
			return new String[] { contexto.getString(R.string.connect_error) };
		}

	}

	@Override
	protected void onPostExecute(Object[] result) {
		delegate.processFinish(result);
	}

}
