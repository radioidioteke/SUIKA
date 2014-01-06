package cl.suika.cineschile.connection;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
//import android.util.Log;
import cl.suika.cineschile.R;
import cl.suika.cineschile.utils.Varios;

public class ConnectionLogIn extends AsyncTask<Void, Void, String> {
	
	private AsyncResponse delegate;
	private Context contexto;
	private String user;
	private String pass;
	
	public ConnectionLogIn(AsyncResponse delegate, Context contexto, String user, String pass){
		this.delegate = delegate;
		this.contexto = contexto;
		this.user = user;
		this.pass = pass;
	}

	@Override
	protected String doInBackground(Void... arg0) {
		
		// GET TOKEN
		RestHttpPost tokenRequest = new RestHttpPost(contexto.getString(R.string.url_token));
		tokenRequest.addPair("id_unico", Varios.getUUID(contexto) );
		String response = tokenRequest.getResponse();
		if(response.equals("error")){
			return contexto.getString(R.string.connect_error);
		}
		
		String[] token = parseTokenResponse(response);
		if( token[0].equals("ERROR") ){
			if(!token[1].equals("2")){
				return token[1];
			}
		}else{
			Varios.setConnectionTokn(contexto, token[1]);
		}
		
		// LOGIN
		RestHttpPost registerReq = new RestHttpPost(contexto.getString(R.string.url_login));
		registerReq.addPair("id_unico", Varios.getUUID(contexto) );
		registerReq.addPair("token", Varios.getConnectionTokn(contexto));
		registerReq.addPair("app", contexto.getString(R.string.rest_name));
		registerReq.addPair("user", user);
		registerReq.addPair("password", Varios.sha1Hash(pass) );
		registerReq.addPair("dispositivo", "android_phone" );
		
		String response2 = registerReq.getResponse();
		
		//Log.i("TEST TEST", "RESPON2 "+response2);
		
		if(response2.equals("error")){
			return contexto.getString(R.string.connect_error);
		}
		
		String[] tokenReg = parseRegisterResponse( response2 );
		if( tokenReg[0].equals("ERROR") ){
			return tokenReg[1];
		}
		
		Varios.setMasterTokn(contexto, tokenReg[1]);
		Varios.setMasterUsr(contexto, user);
		
		return "OK";
	}
	
	@Override
    protected void onPostExecute(String result) {             
		delegate.processFinish(new Object[]{result});
	}
	
	
	private String[] parseTokenResponse(String response){
		try {
			JSONObject jResponse = new JSONObject( (new JSONObject(response)).getString("RESPONSE") );
			String status 	= jResponse.getString("STATUS");
			String detail 	= jResponse.getString("DETAIL");
			String token 	= jResponse.getString("TOKEN");
			String sNumber 	= jResponse.getString("STATUS_NUMBER");
			
			if(sNumber.equals("2")){
				return new String[]{status,sNumber};
			}
			if(status.equals("ERROR")){
				return new String[]{status,detail};
			}else{
				return new String[]{status,token};
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private String[] parseRegisterResponse(String response){
		try {
			JSONObject jResponse = new JSONObject( (new JSONObject(response)).getString("RESPONSE") );
			String status 	= jResponse.getString("STATUS");
			String detail 	= jResponse.getString("DETAIL");
			String token 	= jResponse.getString("TOKEN");
			if(status.equals("ERROR")){
				return new String[]{status,detail};
			}else{
				return new String[]{status,token};
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
