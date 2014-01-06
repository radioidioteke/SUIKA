package cl.suika.cineschile.connection;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
//import android.util.Log;
import cl.suika.cineschile.R;
import cl.suika.cineschile.utils.Varios;

public class ConnectionValidarRegistro extends AsyncTask<Void, Void, String> {

	private AsyncResponse delegate;
	private Context contexto;
	private String user;
	private String tokenUser;

	public ConnectionValidarRegistro(AsyncResponse delegate, Context contexto, String user, String tokenUser) {
		this.delegate = delegate;
		this.contexto = contexto;
		this.user = user;
		this.tokenUser = tokenUser;
	}

	@Override
	protected String doInBackground(Void... arg0) {

		RestHttpPost tokenRequest = new RestHttpPost(contexto.getString(R.string.url_validar));
		tokenRequest.addPair("id_unico", Varios.getUUID(contexto));
		tokenRequest.addPair("token", Varios.getConnectionTokn(contexto));
		tokenRequest.addPair("app", contexto.getString(R.string.rest_name));
		tokenRequest.addPair("user", user);
		tokenRequest.addPair("token_user", tokenUser);
		tokenRequest.addPair("dispositivo", "android_phone" );

		String response = tokenRequest.getResponse();
		//Log.i("ConnectionValidarRegistro", "response: " + response);
		if (response.equals("error")) {
			return contexto.getString(R.string.connect_error);
		}

		String[] token = parseValidatorResponse(response);

		if (token[0].equals("ERROR")) {
			if (token[1].equals("2")) {
				getTokenConnection();
				return doRegisterConnection();
			} else {
				return token[1];
			}
		}

		Varios.setMasterTokn(contexto, token[1]);
		return "OK";
	}

	@Override
	protected void onPostExecute(String result) {
		delegate.processFinish(new Object[]{result});
	}

	public String doRegisterConnection() {
		RestHttpPost tokenRequest = new RestHttpPost(contexto.getString(R.string.url_validar));
		tokenRequest.addPair("id_unico", Varios.getUUID(contexto));
		tokenRequest.addPair("token", Varios.getConnectionTokn(contexto));
		tokenRequest.addPair("app", contexto.getString(R.string.rest_name));
		tokenRequest.addPair("user", user);
		tokenRequest.addPair("token_user", tokenUser);

		String response = tokenRequest.getResponse();
		//Log.i("ConnectionValidarRegistro", "response: " + response+ " token:"+Varios.getConnectionTokn(contexto)+"   uuid:"+Varios.getUUID(contexto));
		if (response.equals("error")) {
			return contexto.getString(R.string.connect_error);
		}

		String[] token = parseValidatorResponse(response);

		if (token[0].equals("ERROR")) {
			if (token[1].equals("2")) {
				return contexto.getString(R.string.connect_error);

			} else {
				return token[1];
			}
		}

		Varios.setMasterTokn(contexto, token[1]);
		return "OK";
	}

	public String getTokenConnection() {
		RestHttpPost cTokenRequest = new RestHttpPost(contexto.getString(R.string.url_token));
		cTokenRequest.addPair("id_unico", Varios.getUUID(contexto));
		String tResponse = cTokenRequest.getResponse();
		//Log.i("TEST GET TOKEN 2", "response:"+tResponse);
		if (tResponse.equals("error")) {
			return contexto.getString(R.string.connect_error);
		}

		String[] tToken = parseTokenResponse(tResponse);
		if (tToken[0].equals("ERROR")) {
			// if (!tToken[1].equals("2")) {
			return tToken[1];
			// }else{
			// return tToken[1];
			// }
		} else {
			Varios.setConnectionTokn(contexto, tToken[1]);
			return "OK";
		}
	}

	private String[] parseTokenResponse(String response) {
		try {
			JSONObject jResponse = new JSONObject((new JSONObject(response)).getString("RESPONSE"));
			String status = jResponse.getString("STATUS");
			String detail = jResponse.getString("DETAIL");
			String sNum = jResponse.getString("STATUS_NUMBER");
			String token = jResponse.getString("TOKEN");
			if (status.equals("ERROR")) {
				if (sNum.equals("2")) {
					return new String[] { status, sNum };
				}
				return new String[] { status, detail };
			} else {
				return new String[] { status, token };
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	private String[] parseValidatorResponse(String response) {
		try {
			JSONObject jResponse = new JSONObject((new JSONObject(response)).getString("RESPONSE"));
			String status = jResponse.getString("STATUS");
			String detail = jResponse.getString("DETAIL");
			String snumber = jResponse.getString("STATUS_NUMBER");
			String token = jResponse.getString("TOKEN");
			if (status.equals("ERROR")) {
				if (snumber.equals("2")) {
					return new String[] { status, snumber };
				} else {
					return new String[] { status, detail };
				}

			} else {
				return new String[] { status, token };
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

}
