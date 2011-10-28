package org.scielo.search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;



import android.util.Log;

public class SearchTask implements Runnable{
	private static final String TAG = "SearchService";
	private Search search;
	private String URL;
	
	
	SearchTask(Search search, String URL){
		this.search = search;
		this.URL = URL;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		// Translate the original text to the target language
		String trans = call();
		//String trans = "k";
		search.setResult(trans);

	}
	
	private int sec2millisec(int sec){
		return sec *  1000;	
	}
	public String call() {
		HttpURLConnection con = null;
		String payload = "";
		String u = "";
		String line = "";
		BufferedReader reader;
		
		try { 
			
			u = URL;
			//u = "http://www.google.com";
			URL url = new URL(u);		
						
			
			
			System.setProperty("http.keepAlive", "false");
			con = (HttpURLConnection) url.openConnection();
			con.setReadTimeout(sec2millisec(60)); /* milliseconds */ 
			con.setConnectTimeout(sec2millisec(60)); /* milliseconds */ 
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
