package org.scielo.search;


import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class SearchResult {
	// Parse to get translated text
	protected JSONArray docs;
	protected ClusterCollection clusterCollection;
	protected ArrayList<Page> pagesList;

	protected String resultCount;
	protected int currentItem;
	protected String from = "";
	protected int itemsPerPage;
	protected JSONObject jsonObject;
	
	protected static final String TAG = "SearchServiceData";
	protected String url;
		
	
	public int getCurrentItem() {
		return currentItem;
	}

	public void setCurrentItem(int currentItem) {
		this.currentItem = currentItem;
	}

	public void setResultCount(String resultCount) {
		this.resultCount = resultCount;
	}

	SearchResult( String url, ArrayList<Page> pagesList ){
		this.pagesList = pagesList;
		this.url = url;
    }
	protected String getURL() {
		return url;						
	}
	protected void loadData(String _data){		
		try {
			jsonObject = new JSONObject(_data);
			
			loadControlData(_data);
			
			currentItem = 0;
			Log.d("SearchServiceData","9");	
			if (from.length() == 0 || from.equals("0")) {
				currentItem = 1;
			} else {
				currentItem = Integer.parseInt(from);	
			}
			Log.d("SearchServiceData","10");	
			
			loadClusterCollection();
			
			Pagination pagination = new Pagination();
			pagination.loadData(from, resultCount, currentItem, itemsPerPage);
			pagination.generatePageList(this.pagesList);
			
			loadSearchResultList();
		} catch(JSONException e){
			Log.d(TAG, "JSONException", e);				
		}
	}
	
	public String getResultCount(){
		return this.resultCount;
	}
	
	public ClusterCollection getSearchClusterCollection(){
		return this.clusterCollection;
	}
	
	protected void loadControlData(String _data){
		
	}
	protected boolean loadClusterCollection() {
    	boolean r = true;
    	return r;
	}
	
	protected void loadSearchResultList(){
	}

	
	
}
