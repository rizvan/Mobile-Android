package org.scielo.search;


import android.app.Activity;

import android.view.MenuInflater;


import android.view.View;


import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
//import android.widget.EditText;

import java.util.ArrayList;

//import org.json.JSONArray;

//import org.json.JSONException;
//import org.json.JSONObject;

import android.app.SearchManager;
import android.content.Intent;
//import android.view.KeyEvent;
import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.SubMenu;
//import android.view.SubMenu;
//import android.view.View.OnKeyListener;

import android.widget.ListView;


public class SearchActivity extends Activity {
	View searchButton;	
	TextView messageTextView;
	ListView searchResultListView;
	GridView paginationGridView;
    
	ArrayAdapter<Page> aaPage;    
	ArrayList<Page> pagesList  = new ArrayList<Page>();
	Page page;
	
	protected String filter = "";
	protected String pagePosition = "";
	protected String query = "";
	protected String query_id = "";
	
    
	@Override
	protected void onNewIntent(Intent intent) {
	    setIntent(intent);
	    handleIntent(intent);
	}

	protected void handleIntent(Intent intent) {
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	      query = intent.getStringExtra(SearchManager.QUERY);
	       
	    } else {
	    	query = query_id;
	    }
	    doSearch();
	}
	protected void doSearch() {
	}
    
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
      
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.menu, menu);
        
    	return true;
    }
	public void addFilter(String _filter){
		if (this.filter.length()>0){
			this.filter = this.filter + " AND " + _filter;
		} else {
			this.filter = _filter;
		}			
	}

}
