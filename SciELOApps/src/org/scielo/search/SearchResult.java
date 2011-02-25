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

	
	protected JSONObject jsonObject;
	
	protected static final String TAG = "SearchResult";
	protected String url;
		
	protected Pagination pagination;
	
	public int getCurrentItem() {
		return pagination.getCurrentItem();
	}

	public String getResultCount(){
		return this.pagination.getResultCount();
	}
	
	SearchResult(ArrayList<Page> pagesList ){
		//clusterCollection = new ClusterCollection();
		this.pagesList = pagesList;
		
    }
	protected String getURL() {
		return url;						
	}
	
	
	protected void loadData(String _data){		
		try {
			jsonObject = new JSONObject(_data);
			pagination = new Pagination();
			loadPaginationAndDocsData();
			//pagination.loadData(from, resultCount, currentItem, itemsPerPage, paginationType);
			
			loadClusterCollection();
			loadSearchResultList();
			pagination.generatePages(this.pagesList); //ordem obrigatoria
		} catch(JSONException e){
			Log.d(TAG, "JSONException", e);				
		}
	}
	
	public ClusterCollection getSearchClusterCollection(){
		return this.clusterCollection;
	}
	
	protected void loadPaginationAndDocsData(){
		
	}
	protected boolean loadClusterCollection() {
    	boolean r = true;
    	return r;
	}
	
	protected void loadSearchResultList(){
	}

	
	
}
