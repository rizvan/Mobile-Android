package org.scielo.search;

//import android.R;
import android.app.Activity;
import android.os.Bundle;

//import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
//import android.widget.EditText;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

//import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import java.net.URL;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

//import org.json.JSONException;
//import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;



public class Search extends Activity {
	private static final String TAG = "Search";
	//private EditText searchExpressionText;
    ListView searchResultListView;
	ArrayAdapter<SearchResult> aa;
	ArrayList<SearchResult> searchResultList = new ArrayList<SearchResult>();
	      
	static final private int SEARCH_RESULT_DIALOG = 1;
	SearchResult selectedSearchResult;

	@Override
	public void onCreate(Bundle icicle) {
	    super.onCreate(icicle);
	    setContentView(R.layout.main);
	    
	    searchResultListView = (ListView) findViewById(R.id.searchResultListView);
	    //searchExpressionText = (EditText) findViewById(R.id.search_expression_text);
	    searchResultListView.setOnItemClickListener(new OnItemClickListener() {
	     
	       @Override
		   public void onItemClick(AdapterView<?> _av, View _v, int _index, long arg3) {
	           selectedSearchResult = searchResultList.get(_index);
	           showDialog(SEARCH_RESULT_DIALOG);
	       }
	    });
	    
	    
	    int layoutID = android.R.layout.simple_list_item_1;
	    aa = new ArrayAdapter<SearchResult>(this, layoutID , searchResultList);
	    searchResultListView.setAdapter(aa);
	    //searchExpressionText.setAdapter(aa);
	    
	    refreshSearchs();
	    
	}
	  
	  
	private void refreshSearchs() {
		//fixme String result = translate.getResources().getString(R.string.translation_error);
		HttpURLConnection con = null;
		String result = "";
		SearchResult r ;
		//String searchExpression = this.searchExpressionText.getText().toString().trim();		
		//Log.d(TAG, "refreshSearchs(" + searchExpression +  ")");
		
		try {
			// Check if task has been interrupted
			if (Thread.interrupted())
				throw new InterruptedException();
			
			// Build RESTful query for Google API
			   
			//String q = URLEncoder.encode(searchExpression, "UTF-8");
			String q = "dna";
			String u = this.getResources().getString(
					R.string.search_feed);
			URL url = new URL(u.replace("amp;", "") + "&q=" + q );
            
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
			String payload = reader.readLine();
			reader.close();
			
			// Parse to get translated text
			JSONObject jsonObject = new JSONObject(payload);
			
			
			int resultCount = jsonObject.getJSONArray("diaServerResponse").getJSONObject(0).getJSONObject("response").getJSONArray("docs").length();
			JSONObject resultItem = new JSONObject();
			for (int i=0; i<resultCount; i++){
				resultItem = jsonObject.getJSONArray("diaServerResponse").getJSONObject(0).getJSONObject("response").getJSONArray("docs").getJSONObject(i );
				result = resultItem.getJSONArray("ti").getString(0);
				for (int j=0; j<resultItem.getJSONArray("au").length(); j++) {
					result = result + "\n" + resultItem.getJSONArray("au").getString(j);
				}
				r = new SearchResult();
				r.setText(result);			
				addNewSearchResult(r);				
			}
			
			// Check if task has been interrupted
			//if (Thread.interrupted())
			//	throw new InterruptedException();
		
		} catch (IOException e) {
			Log.e(TAG, "IOException", e);
		} catch (JSONException e) {
			Log.e(TAG, "JSONException", e);
		} catch (InterruptedException e) {
			Log.d(TAG, "InterruptedException", e);
			result = this.getResources().getString(
					R.string.search_interrupted);
		} finally {
			if (con != null) {
				con.disconnect();
			}	}
		
		// All done
		Log.d(TAG, " -> returned " + result);
		
		
	}
    private void addNewSearchResult(SearchResult _searchResult) {
	  // Add the new quake to our list of searchResultList.
	  searchResultList.add(_searchResult);

	  // Notify the array adapter of a change.
	  aa.notifyDataSetChanged();
	}
    
    static final private int MENU_UPDATE = Menu.FIRST;
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      super.onCreateOptionsMenu(menu);

      menu.add(0, MENU_UPDATE, Menu.NONE, R.string.menu_update);
                  
      return true;
    }
            
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
      super.onOptionsItemSelected(item);
           
      switch (item.getItemId()) {
        case (MENU_UPDATE): {
          refreshSearchs();
          return true; 
        }
      } 
      return false;
    }
    
    @Override
    public Dialog onCreateDialog(int id) {
      switch(id) {
        case (SEARCH_RESULT_DIALOG) :
          LayoutInflater li = LayoutInflater.from(this);
          View searchResultDetailsView = li.inflate(R.layout.search_result_details, null);

          AlertDialog.Builder searchResultDialog = new AlertDialog.Builder(this);
          searchResultDialog.setTitle(R.string.search_result_details_title);
          searchResultDialog.setView(searchResultDetailsView);
          return searchResultDialog.create();
      }
      return null;
    }

    @Override
    public void onPrepareDialog(int id, Dialog dialog) {
      switch(id) {
        case (SEARCH_RESULT_DIALOG) :
          
          String searchResultText = selectedSearchResult.getText();

          AlertDialog searchResultDialog = (AlertDialog)dialog;
          searchResultDialog.setTitle(R.string.searchResultDialogTitle);
          TextView tv = (TextView)searchResultDialog.findViewById(R.id.searchResultDetailsTextView);
          tv.setText(searchResultText);

          break;
      }
    }
}
