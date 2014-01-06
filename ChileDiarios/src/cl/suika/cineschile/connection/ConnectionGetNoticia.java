package cl.suika.cineschile.connection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
//import android.util.Log;
import cl.suika.cineschile.R;
import cl.suika.cineschile.utils.Varios;

public class ConnectionGetNoticia extends AsyncTask<Void, Long, Object[]> {

	private AsyncResponse delegate;
	private Activity contexto;
	private String idNoticia;

	public ConnectionGetNoticia(AsyncResponse delegate, Activity contexto, String idNoticia) {
		this.delegate = delegate;
		this.contexto = contexto;
		this.idNoticia = idNoticia;
	}

	@Override
	protected Object[] doInBackground(Void... arg0) {

		RestHttpPost updateRequest = new RestHttpPost(contexto.getString(R.string.url_get_noticia));
		updateRequest.addPair("id_noticia", idNoticia);
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
			
			JSONArray noticias = new JSONObject(response).getJSONArray("NOTICIA");
			String[] retornoNews = null;
			for (int i = 0; i < noticias.length(); i++) {
				JSONObject objeto = noticias.getJSONObject(i);
				retornoNews = new String[] { objeto.getString("TITULO"), objeto.getString("BAJADA"), objeto.getString("TEXTO"), objeto.getString("URL"), objeto.getString("IMAGEN") };
			}
			return retornoNews;

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
