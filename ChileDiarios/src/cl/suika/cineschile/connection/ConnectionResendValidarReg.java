package cl.suika.cineschile.connection;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
//import android.util.Log;
import cl.suika.cineschile.R;
import cl.suika.cineschile.utils.Varios;

public class ConnectionResendValidarReg extends AsyncTask<Void, Void, String> {

	private AsyncResponse delegate;
	private Context contexto;
	private String user;
	//private String tokenUser;

	public ConnectionResendValidarReg(AsyncResponse delegate, Context contexto, String user) {
		this.delegate = delegate;
		this.contexto = contexto;
		this.user = user;
		//this.tokenUser = tokenUser;
	}

	@Override
	protected String doInBackground(Void... arg0) {
		// GET TOKEN
//		RestHttpPost cTokenRequest = new RestHttpPost(contexto.getString(R.string.url_token));
//		cTokenRequest.addPair("id_unico", Varios.getUUID(contexto));
//		String tResponse = cTokenRequest.getResponse();
//		Log.i("ConnectionResendValidarReg", "url_token: "+tResponse);
//		if (tResponse.equals("error")) {
//			return contexto.getString(R.string.connect_error);
//		}
//
//		String[] tToken = parseTokenResponse(tResponse);
//		if (tToken[0].equals("ERROR")) {
//			if (!tToken[1].equals("2")) {
//				return tToken[1];
//			}
//		} else {
//			Varios.setConnectionTokn(contexto, tToken[1]);
//		}
		//id_unico, token, app, user
		

		RestHttpPost tokenRequest = new RestHttpPost(contexto.getString(R.string.url_resend_token));
		tokenRequest.addPair("id_unico", Varios.getUUID(contexto));
		tokenRequest.addPair("token", Varios.getConnectionTokn(contexto));
		tokenRequest.addPair("app", contexto.getString(R.string.rest_name));
		tokenRequest.addPair("user", user);

		String response = tokenRequest.getResponse();
		//Log.i("ConnectionResendValidarReg", "url_resend_token: "+response);
		if (response.equals("error")) {
			return contexto.getString(R.string.connect_error);
		}

		String[] detail = parseValidatorResponse(response);

		if (detail[0].equals("ERROR")) {
			return detail[1];
		}

		Varios.setMasterUsr(contexto, user);
		//Varios.setMasterTokn(contexto, token[1]);
		//Varios.setConnectionTokn(contexto, "null");
		return detail[1];
	}

	@Override
	protected void onPostExecute(String result) {
		delegate.processFinish(new Object[]{result});
	}
//	
//	private String[] parseTokenResponse(String response) {
//		try {
//			JSONObject jResponse = new JSONObject((new JSONObject(response)).getString("RESPONSE"));
//			String status = jResponse.getString("STATUS");
//			String detail = jResponse.getString("DETAIL");
//			String sNum   = jResponse.getString("STATUS_NUMBER");
//			if (status.equals("ERROR")) {
//				if(sNum.equals("2")){
//					return new String[] { status, sNum };	
//				}
//				return new String[] { status, detail };
//			} else {
//				return new String[] { status, detail };
//			}
//
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//
//		return null;
//	}

	private String[] parseValidatorResponse(String response) {
		try {
			JSONObject jResponse = new JSONObject((new JSONObject(response)).getString("RESPONSE"));
			String status = jResponse.getString("STATUS");
			String detail = jResponse.getString("DETAIL");
			//String token = jResponse.getString("TOKEN");
			if (status.equals("ERROR")) {

				return new String[] { status, detail };
			} else {
				return new String[] { status, detail };
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

}
