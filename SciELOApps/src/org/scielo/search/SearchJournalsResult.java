package org.scielo.search;


import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class SearchJournalsResult {
	// Parse to get translated text
	protected JSONArray docs;
	protected JSONObject facetFields;
	
	protected String generic_PDF_URL;	
	
	protected ArrayList<Journal> searchResultList;
	protected ClusterCollection clusterCollection;
	
	protected SciELONetwork jc;
	protected PairsList subjects;
	protected PairsList languages;
	
	private Pagination pagination;
	protected static final String TAG = "SearchServiceData";
	private String resultCount;
	private int currentItem;
	private String url;
	
	public int getCurrentItem() {
		return currentItem;
	}

	public void setCurrentItem(int currentItem) {
		this.currentItem = currentItem;
	}

	public void setResultCount(String resultCount) {
		this.resultCount = resultCount;
	}

	SearchJournalsResult(String url, SciELONetwork jc, PairsList subjects, PairsList languages, ArrayList<Journal> searchResultList){
		clusterCollection = new ClusterCollection();
    	this.url = url;
    	
    	this.jc = jc;
    	this.subjects = subjects;
    	this.languages = languages;
		    	
		this.searchResultList = searchResultList;
		this.pagination = new Pagination();		
    }
	public String getURL(String searchExpression, String itemsPerPage, String filter, String pagePosition) {
		String u = "";
		String query = "";
		
		u = this.url;
		u =	u.replace("amp;", "" );
		if (itemsPerPage.length()>0){
			query = query + "&count=" + itemsPerPage;
		}	
		if (pagePosition.length()>0){
			query = query + "&start=" + pagePosition;
		} 
		if (searchExpression.length()>0){
			query = query + "&q=" + searchExpression;
		}
		if (filter.length()>0){
			query = query + "&fq=" + filter;
		}
		return u + query;						
	}
	public void loadData(String _data){		
		JSONObject jsonObject;
		
		//JSONObject diaServerResponse;
		//JSONObject response;
		//JSONObject responseParameters;
		
		String from = "";
		int itemsPerPage;
		
		
		try {
			jsonObject = new JSONObject(_data);
			/*
			diaServerResponse = jsonObject.getJSONArray("diaServerResponse").getJSONObject(0);
			Log.d("SearchServiceData","1");	
			this.facetFields = diaServerResponse.getJSONObject("facet_counts").getJSONObject("facet_fields");
			Log.d("SearchServiceData","2");	
			response = diaServerResponse.getJSONObject("response");
			Log.d("SearchServiceData","3");	
			responseParameters = diaServerResponse.getJSONObject("responseHeader").getJSONObject("params");
			Log.d("SearchServiceData","4");	
			
			resultCount = response.get("numFound").toString();				
			Log.d("SearchServiceData","5");	
			
			from = responseParameters.get("start").toString();
			Log.d("SearchServiceData","6");	
			itemsPerPage = Integer.parseInt(responseParameters.get("rows").toString());
			Log.d("SearchServiceData","7");	
			
			this.docs = response.getJSONArray("docs");
			Log.d("SearchServiceData","8");	
			*/
			docs = jsonObject.getJSONArray("titles");
			resultCount = new Integer(docs.length()).toString();
			currentItem = 0;
			Log.d("SearchServiceData","9");	
			if ((from.length() == 0) ) {
				currentItem = 1;
			} else {
				currentItem = Integer.parseInt(from);	
			}
			Log.d("SearchServiceData","10");	
			itemsPerPage = 3000;
			loadClusterCollection();
			pagination.loadData(from, resultCount, currentItem, itemsPerPage);
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
	

	private boolean loadClusterCollection() {
    	boolean r = true;
    	/* 
    	int i_clusters = 0;
    	int i = 0;
    	int subMenuId = 0;
    	int filterCount = 0;
    	
    	JSONArray a_clusters = this.facetFields.names();
    	JSONArray clusterData;
    	JSONArray filter = null;
    	
    	SearchFilter searchFilter;
    	
    	String cluster_id ;
    	String filterCode = "";
    	String filterName = "";
    	String filterResultCount = "0";
    	
    	
    	Cluster cluster;
    	Log.d(TAG, "loadClusterCollection inicio");
    	for (i_clusters=0;i_clusters<a_clusters.length();i_clusters++){
    		cluster_id="";
    		Log.d(TAG,  new Integer(i_clusters).toString() + "/" + new Integer(a_clusters.length()).toString());
			try {
    			cluster_id = a_clusters.getString(i_clusters);
    			cluster = new Cluster(cluster_id);
    			
    			clusterData = (JSONArray) this.facetFields.get(cluster_id);				
	    		filterCount = clusterData.length();
	            
	        	for (i=0;i<filterCount;i++){        		
	        		try {
	        			
	        			Log.d(TAG, "cluster:" + cluster_id);
	        			Log.d(TAG, "filter:" + new Integer(i).toString() + "/" + new Integer(filterCount).toString());
	        			filter = clusterData.getJSONArray(i);
	        			filterResultCount = filter.getString(1);
	        			filterCode = filter.getString(0);
	        			Log.d(TAG, " filterCode:" + filterCode);
	        			
	        			
	        			if (cluster_id.equals("in")){
	        				filterName = jc.getItem(filterCode).getName();
	        			} else {
		        			if (cluster_id.equals("la")){
		        				filterName = languages.getItem(filterCode).getValue();
		        			} else {
			        			if (cluster_id.equals("ac")){
			        				filterName = subjects.getItem(filterCode).getValue() ;
			        			} else {
			        				filterName = filterCode;
			        			}		        				
		        			}	        				
	        			}
	    				
	        			Log.d(TAG,  " filterName:" + filterName);
	    				
	    				subMenuId = i + (i_clusters * 100) ;
	        			Log.d(TAG,  " submenuId" + new Integer(subMenuId).toString());
	    				searchFilter = new SearchFilter(filterName, filterResultCount,  filterCode, cluster.getId() );
	    				searchFilter.setSubmenuId(subMenuId);
	        			Log.d(TAG,  " addFilter ");
	    				cluster.addFilter(searchFilter);        			
	    				Log.d(TAG, "fim " + cluster_id + ":" + filterCode);	 				
	    			} catch (JSONException e) {
	    				// TODO Auto-generated catch block
	    				Log.d(TAG, "JSONException 1");
	    				e.printStackTrace();
	    				
	    			}
	        	}     
	        	Log.d(TAG, "addCluster inicio");
	    		r = clusterCollection.add(cluster);
	    		Log.d(TAG, "addCluster fim");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Log.d(TAG, "JSONException 2");
				e.printStackTrace();
			}
			Log.d(TAG, "fim loop " + cluster_id);
    	}
    	Log.d(TAG, "loadClusterCollection fim mesmo");
    	 */
		return r;
	}
	
	public void loadSearchResultList(){
		JSONObject resultItem ;
		Journal r;
		
		String collectionCode;
		String last = "";
		String _pid;
		SciELOCollection col = new SciELOCollection();
	    
		searchResultList.clear();
		
		//message = "docs.length: " +  new Integer(this.docs.length()).toString() + "\n" + "rows: " + this.itemsPerPage;
		for (int i=0; i<this.docs.length(); i++){
			r = new Journal();
			last = "";
			try {				
				last = last + "\n" +"item " ;
				resultItem = this.docs.getJSONObject(i);
				
				try {
					r.setTitle(  resultItem.getString("title"));	
				} catch (JSONException e) {
					last = last + "\n" +"ti";
				}
				try {
					collectionCode = resultItem.getString("col");
					r.setCollectionId(collectionCode);
				} catch (JSONException e) {
					last = last + "\n" +"in" ;	
					collectionCode = "";
				}
				try {
					_pid = resultItem.getString("pid");	
					r.setId(_pid);
				} catch (JSONException e) {
					last = last + "\n" +"id" ;
					_pid = "";
				}
				
				col = jc.getItem(collectionCode);							
				r.setCollection(col.getName());
				searchResultList.add(r);
			} catch (JSONException e) {
				Log.d(TAG, "JSONException loadResultList " + new Integer(i).toString() + " " + last, e);	
	        } 
		}
	}
	
}