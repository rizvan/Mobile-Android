package org.scielo.search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;



import android.util.Log;

public class SearchService {
	private static final String TAG = "SearchService";
	
	
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
		String line = "";
		BufferedReader reader;
		
		try { 
			
			u = queryURL;
			//u = "http://www.google.com";
			URL url = new URL(u);		
						
			
			
			System.setProperty("http.keepAlive", "false");
			con = (HttpURLConnection) url.openConnection();
			con.setReadTimeout(getMilliseconds(5)); /* milliseconds */ 
			con.setConnectTimeout(getMilliseconds(5)); /* milliseconds */ 
			con.setRequestMethod("GET");
			//con.setDoInput(true);			
			con.connect();
			
			
			
			
			
			// Check if task has been interrupted
			if (Thread.interrupted())
				throw new InterruptedException();
			
			// Read results from the query
			reader = new BufferedReader(
					new InputStreamReader(con.getInputStream(), "UTF-8") );
			
			while ((line = reader.readLine()) != null) {
				payload += line;
			}
			reader.close();
		} catch  (java.net.MalformedURLException e){  
			  
			Log.e(TAG, "MalformedURLException", e);
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
