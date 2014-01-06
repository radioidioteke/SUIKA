package cl.suika.cineschile.connection;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.graphics.Bitmap;
import android.util.Base64;

public class RestHttpPost {

	private String url;
	private ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	private HttpParams httpParameters = new BasicHttpParams();
	
	public RestHttpPost(String url) {
		this.url = url;
	}

	public void addPair(Bitmap bitmap) {

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
		byte[] byte_arr = stream.toByteArray();
		String image_str = Base64.encodeToString(byte_arr, Base64.DEFAULT);
		nameValuePairs.add(new BasicNameValuePair("image", image_str));
	}

	public void addPair(String type, String info) {
		nameValuePairs.add(new BasicNameValuePair(type, info));
	}

	public String getResponse() {
		try {
			HttpConnectionParams.setConnectionTimeout(httpParameters, 30000);
			HttpConnectionParams.setSoTimeout(httpParameters, 30000);
			
			
			HttpClient httpclient = new DefaultHttpClient(httpParameters);
			HttpPost httppost = new HttpPost(url);
			
			
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);

			return EntityUtils.toString(response.getEntity());
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}

}
