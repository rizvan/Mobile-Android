package org.scielo.search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


import android.util.Log;

public class SearchService {
	private static final String TAG = "Search";
	
	
	SearchService(){
		
	}
	private int getMilliseconds(int min){
		int sec;
		int millisec;
		sec = min * 60;
		millisec = sec * 1000;
		return millisec;
	}
	public String call(String queryURL) {
		HttpURLConnection con = null;
		String payload = "";
		String u = "";
		
		
		
		try { 
			
			//u = URLEncoder.encode(queryURL, "UTF-8");
			u = queryURL;
			URL url = new URL(u);						
			con = (HttpURLConnection) url.openConnection();
			con.setReadTimeout(getMilliseconds(10)); /* milliseconds */ 
			con.setConnectTimeout(getMilliseconds(10)); /* milliseconds */ 
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
