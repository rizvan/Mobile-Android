package org.scielo.search;


import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class SearchJournalsResult extends SearchResult {
	// Parse to get translated text
	//protected JSONObject facetFields;
	
	
	protected ArrayList<Journal> searchResultList;
	
	protected SciELONetwork jc;
	protected PairsList subjects;
	protected PairsList languages;
	
	
	
	SearchJournalsResult(String url, SciELONetwork jc, PairsList subjects, PairsList languages, ArrayList<Journal> searchResultList, ArrayList<Page> pagesList){
		super(url, pagesList);
		
    	this.jc = jc;
    	this.subjects = subjects;
    	this.languages = languages;
		    	
		this.searchResultList = searchResultList;
				
    }
	public String getURL(String searchExpression, String itemsPerPage, String filter, String pagePosition) {
		return url;
	}
	public void loadControlData(String _data){		
		try {
			docs = jsonObject.getJSONArray("rows");
			resultCount = new Integer(docs.length()).toString();
			itemsPerPage = 3000;
			from = "1";
			
		} catch(JSONException e){
			Log.d(TAG, "JSONException", e);				
		}
	}
	
	

	public boolean loadClusterCollection() {
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
		JSONArray s;
		String subj = "";
		
		searchResultList.clear();
		/*
		 * {"id":"9553ce99-9536-47de-b987-3a5626e7680d","key":["arg","20030404"],"value":{"collection":"arg","issn":"0002-7014","title":"Ameghiniana","subject":["PALEONTOLOGIA"],"publisher":{"_":"Asociaci\u00f3n Paleontol\u00f3gica Argentina"},"insert_date":"20030404"}},

		 * 
		 */
		//message = "docs.length: " +  new Integer(this.docs.length()).toString() + "\n" + "rows: " + this.itemsPerPage;
		for (int i=0; i<this.docs.length(); i++){
			r = new Journal();
			last = "";
			try {				
				last = last + "\n" +"item " ;
				resultItem = this.docs.getJSONObject(i).getJSONObject("value");
				
				try {
					r.setTitle(  resultItem.getString("title"));	
				} catch (JSONException e) {
					last = last + "\n" +"ti";
				}
				try {
					collectionCode = resultItem.getString("collection");
					r.setCollectionId(collectionCode);
				} catch (JSONException e) {
					last = last + "\n" +"in" ;	
					collectionCode = "";
				}
				try {
					_pid = resultItem.getString("issn");	
					r.setId(_pid);
				} catch (JSONException e) {
					last = last + "\n" +"id" ;
					_pid = "";
				}
				try {
					s = resultItem.getJSONArray("subject");
					subj = "";
					for (int k=0; k<s.length();k++){
						subj = subj + s.getString(k) +  ";";
					}
					r.setSubjects(subj);
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
