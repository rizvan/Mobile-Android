package org.scielo.search;


import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Searcher {
	
	protected static final String TAG = "Searcher";
	
	protected IdAndValueObjects URL_list;
	protected String URL="";
	
	
	protected JSONArray documentRoot;
	
	
	protected ClusterCollection clusterCollection;
	protected ArrayList<Page> pagesList;

	
	protected String totalResults="";
	protected String totalQtd = "";
	protected int iTotalResults = 0;
	protected boolean connProblem=false;
		
	Searcher(ArrayList<Page> pagesList, IdAndValueObjects urls){
		this.URL_list = urls;
		this.pagesList = pagesList;
    }
	
	Searcher(ArrayList<Page> pagesList, String url){
		this.URL = url;
		this.pagesList = pagesList;
    }
	
	protected int getIntTotalResults(){
		return iTotalResults;
	}
	protected boolean hasConnectionProblem(){	
		return connProblem;
	}
	
	protected String getURL(){
		return this.URL;
	}
	
	protected String getQtdTotal() {
		return totalQtd;
	}
	public String getResultTotal(){
		return totalResults;
	}
	
	protected void genLoadData(String _data){		
		try {
			JSONObject jso = new JSONObject(_data);
			readRawResult(jso);
			loadClusterCollection();
			loadResultList();
			
		} catch(JSONException e){
			Log.d(TAG, "JSONException", e);				
		}
	}
	
	protected ClusterCollection getSearchClusterCollection(){
		return this.clusterCollection;
	}
	
	protected void readRawResult(JSONObject rawResult){		
		try {
			documentRoot = rawResult.getJSONArray("rows");
			iTotalResults = documentRoot.length();
			//pagination.loadData("1", new Integer(docs.length()).toString(), 3000, 0);			
		} catch(JSONException e){
			Log.d(TAG, "JSONException", e);				
		}
		
	}
	
	
	protected boolean loadClusterCollection() {
    	boolean r = true;
    	return r;
	}
	
	protected void loadResultList(){
	}

	
	
	
}
