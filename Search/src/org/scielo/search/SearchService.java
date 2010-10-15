package org.scielo.search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import android.util.Log;

public class SearchService {
	private static final String TAG = "Search";
	private String url;
	
	SearchService(String _url){
		this.url = _url;
	}
	public String call(String searchExpression, String filter) {
		HttpURLConnection con = null;
		String query = "";
		String u = "";
		String payload = "";
		try { 
			u = this.url;
			u =	u.replace("amp;", "" );
			if (searchExpression.length()>0){
				query = query + "&q=" + URLEncoder.encode(searchExpression, "UTF-8");
			}
			if (filter.length()>0){
				query = query + "&fq=" + URLEncoder.encode(filter, "UTF-8");
			}	
			
			URL url = new URL(u + query);						
			con = (HttpURLConnection) url.openConnection();
			con.setReadTimeout(10000); /* milliseconds */ 
			con.setConnectTimeout(15000); /* milliseconds */ 
			con.setRequestMethod("GET");
			//con.addRequestProperty("Referer", "http://www.pragprog.com/titles/eband3/hello-android");
			con.setDoInput(true);			
			// Start the query
			con.connect();
			
			// Check if task has been interrupted
			if (Thread.interrupted())
				throw new InterruptedException();
						
			// Read results from the query
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(con.getInputStream(), "UTF-8"));
			payload = reader.readLine();
			reader.close();			
		} catch (IOException e) {
			Log.e(TAG, "IOException", e);
		} catch (InterruptedException e) {
			Log.d(TAG, "InterruptedException", e);
			
		} finally {
			if (con != null) {
				con.disconnect();
			}	
		}	
		return payload;
	}
}
