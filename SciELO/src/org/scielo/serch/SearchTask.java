package org.scielo.serch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject; 

import android.util.Log;

public class SearchTask implements Runnable {
	private static final String TAG = "TranslateTask";
	private final Search translate;
	private final String original, from, to;
	
	SearchTask(Search translate, String original, String from, String to) {
		this.translate = translate;
		this.original = original;
		this.from = from;
		this.to = to;
	}
	public void run() {
		// Translate the original text to the target language
		String trans = doQuery("");
		translate.setTranslated(trans);

		// Translate the original text to the target language
		String retrans = doTranslate(trans, to, from);
		translate.setRetranslated(retrans);
	}
	
	
	private int s2ms(int sec){
		return sec * 1000;	
	}
	public String doQuery(String queryURL) {
		HttpURLConnection con = null;
		String payload = "";
		String u = "";
		String line = "";
		BufferedReader reader;
		
		try { 
			if (queryURL==""){
				queryURL = "http://www.google.com";
			}
			u = queryURL;
			//u = "http://www.google.com";
			URL url = new URL(u);		
						
			
			
			System.setProperty("http.keepAlive", "false");
			con = (HttpURLConnection) url.openConnection();
			con.setReadTimeout(s2ms(30)); /* milliseconds */ 
			con.setConnectTimeout(s2ms(30)); /* milliseconds */ 
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
	
	/** Call the Google Translation API to translate a string from on
	 * language to another Form more info on the API see:
	 * http://code.google.com/apis/ajaxlanguage
	 */
	private String doTranslate(String original, String from, String to) {
		String result = "error";
		HttpURLConnection con = null;
		Log.d(TAG, "doTranslate(" + original + ", " + from + ", " + to + ")");
		
		try {
			// Check if task has been interrupted
			if (Thread.interrupted())
				throw new InterruptedException();
			
			// Build RESTful query for Google API
			String q = URLEncoder.encode(original, "UTF-8");
			URL url = new URL(
					"http://ajax.googleapis.com/ajax/services/language/translate"
					    + "?v=1.0" + "&q=" + q + "&langpair=" + from + "%7C" + to);
			con = (HttpURLConnection) url.openConnection();
			con.setReadTimeout(10000 /* milliseconds */);
			con.setConnectTimeout(15000 /* milliseconds */);
			con.setRequestMethod("GET");
			con.addRequestProperty("Referer", "http://www.pragprog.com/titles/eband3/hello-android");
			con.setDoInput(true);
			
			// Start the query
			con.connect();
			
			// Check if task has been interrupted
			if (Thread.interrupted())
				throw new InterruptedException();
			
			// Read results from the query
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(con.getInputStream(), "UTF-8"));
			String payload = reader.readLine();
			reader.close();
			
			// Parse to get translated text
			JSONObject jsonObject = new JSONObject(payload);
			result = jsonObject.getJSONObject("responseData")
				.getString("translatedText")
				.replace("&#39;", "'")
			    .replace("&amp;", "&");
			
			// Check if task has been interrupted
			if (Thread.interrupted())
				throw new InterruptedException();
		
		} catch (IOException e) {
			Log.e(TAG, "IOException", e);
		} catch (JSONException e) {
			Log.e(TAG, "JSONException", e);
		} catch (InterruptedException e) {
			Log.d(TAG, "InterruptedException", e);
			result = "interr";
		} finally {
			if (con != null) {
				con.disconnect();
			}
		}
		// All done
		Log.d(TAG, " -> returned " + result);
		return result;
	}
}
