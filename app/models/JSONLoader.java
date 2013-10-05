package models;

import java.io.*;

import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;

public class JSONLoader {

	private static String result;
	
	public static String loadTracksJSON(String jsonURL) throws ClientProtocolException, IOException{
		DefaultHttpClient   httpclient = new DefaultHttpClient(new BasicHttpParams());
		HttpGet httpget = new HttpGet(jsonURL);
		
		HttpResponse response = httpclient.execute(httpget);
		HttpEntity entity = response.getEntity();
		
		InputStream inputStream=entity.getContent();
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
		StringBuilder sb = new StringBuilder();

		String line = null;
		while ((line = reader.readLine()) != null){
			sb.append(line + "\n");
		}
		result = sb.toString();
		return result;
	}
}
