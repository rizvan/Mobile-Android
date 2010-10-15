package org.scielo.search;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class SearchServiceData {
	// Parse to get translated text
	private static final String TAG = "SearchServiceData";
	JSONObject jsonObject;
	JSONObject result;
	JSONObject response;
	JSONObject facetFields;
	String generic_PDF_URL;
	
    String resultCount;
    int listItemCount;
    
	SearchServiceData(String _data, String _generic_pdf_url){		
		this.generic_PDF_URL = _generic_pdf_url;
		try {
			this.jsonObject = new JSONObject(_data);
			this.result = jsonObject.getJSONArray("diaServerResponse").getJSONObject(0);
			
			this.facetFields = this.result.getJSONObject("facet_counts").getJSONObject("facet_fields");
			this.response = this.result.getJSONObject("response");
			
			this.resultCount = this.response.get("numFound").toString();				
			this.listItemCount = this.response.getJSONArray("docs").length();
			
		} catch(JSONException e){
			Log.d(TAG, "JSONException", e);				
		}
	}
	
	public String getResultCount(){
		return this.resultCount;
	}
	public void loadResultList(ArrayList<SearchResult> searchResultList, JournalCollections jc){
		JSONObject resultItem ;
		SearchResult r;
		String result;
		String collectionCode;
		
		String link;
		Article_PDF_URL article_PDF_URL;
		String _pid;
		String _filename;
		String _lang;
		
		
		
		searchResultList.clear();
		for (int i=0; i<this.listItemCount; i++){
			r = new SearchResult();
			try {
				resultItem = this.response.getJSONArray("docs").getJSONObject(i );
				r.setDocumentTitle( new Integer(i+1).toString()+ "\n" + resultItem.getJSONArray("ti").getString(0));
				result = "";
				for (int j=0; j<resultItem.getJSONArray("au").length(); j++) {
					result = result + resultItem.getJSONArray("au").getString(j) + "; ";
				}
				r.setDocumentAuthors(result);
				collectionCode = resultItem.getString("in");
				
				article_PDF_URL = new Article_PDF_URL(this.generic_PDF_URL);
				_pid = resultItem.getString("id");
				_pid = _pid.replace("art-", "");
				_pid = _pid.replace("^c" + collectionCode, "");
				
				_filename = resultItem.getString("filename");
				
				_lang = resultItem.getJSONArray("la").getString(0);
				link = article_PDF_URL.getURL(jc.getCollectionUrl(collectionCode), jc.getCollectionAppName(collectionCode), _pid, _filename, _lang);
				r.setDocumentPDFLink(link);
				
				//r.setDocumentPDFLink("http://teste.scielo.br");
				
				
				searchResultList.add(r);
			} catch (JSONException e) {
				Log.d(TAG, "JSONException", e);	
	        } 
		}
		
	}
	
	public boolean loadClusterCollection(ClusterCollection clusterCollection) {
    	boolean r = true;
    	int filterCollectionId = 0;
    	int i = 0;
    	int subMenuId = 0;
    	int t = 0;
    	
    	JSONArray keys = this.facetFields.names();
    	JSONArray clusterData;
    	JSONArray filter = null;
    	
    	SearchFilter searchFilter;
    	
    	String key ;
    	String filterCode = "";
    	String filterName = "";
    	String filterResultCount = "0";
    	
    	Cluster cluster;
    	ArrayList<SearchFilter> searchFilterArray;
    	Log.d(TAG, "loadClusterCollection inicio");
    	for (filterCollectionId=0;filterCollectionId<keys.length();filterCollectionId++){
    		key="";
    		Log.d(TAG,  new Integer(filterCollectionId).toString() + "/" + new Integer(keys.length()).toString());
			try {
    			key = keys.getString(filterCollectionId);
    			searchFilterArray = new ArrayList<SearchFilter>();
    			cluster = new Cluster(searchFilterArray, key);
    			
    			clusterData = (JSONArray) this.facetFields.get(key);				
	    		t = clusterData.length();
	            
	        	for (i=0;i<t;i++){        		
	        		try {
	        			Log.d(TAG,  new Integer(i).toString() + "/" + new Integer(t).toString());
	        			Log.d(TAG,  "inicio " + key );
	    				filter = clusterData.getJSONArray(i);
	        			Log.d(TAG,  "1");
	    				
	    				filterResultCount = filter.getString(1);
	        			Log.d(TAG,  "2");
	    				filterCode = filter.getString(0);
	        			Log.d(TAG,  "3");
	    				filterName = filterCode;
	        			Log.d(TAG,  "4 " + filterCode);
	    				
	    				subMenuId = i + (filterCollectionId * 100) ;
	        			Log.d(TAG,  "5");
	    				searchFilter = new SearchFilter(filterName, filterResultCount, filterCode, cluster.getCode() );
	    				searchFilter.setSubmenuId(subMenuId);
	        			Log.d(TAG,  "6");
	    				cluster.addFilter(searchFilter);        			
	    				Log.d(TAG, "fim " + key + " " + filterCode);	 				
	    			} catch (JSONException e) {
	    				// TODO Auto-generated catch block
	    				Log.d(TAG, "JSONException 1");
	    				e.printStackTrace();
	    				
	    			}
	        	}     
	        	Log.d(TAG, "addCluster inicio");
	    		r = clusterCollection.addCluster(cluster);
	    		Log.d(TAG, "addCluster fim");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Log.d(TAG, "JSONException 2");
				e.printStackTrace();
			}
			Log.d(TAG, "fim loop " + key);
    	}
    	Log.d(TAG, "loadClusterCollection fim mesmo");
    	return r;
	}
	/*
    private void getSearchFilterList(SearchFilterGroup searchFilterGroup, int filterListId, JSONArray filterList){
    	int i;
    	int x;
    	int t;
    	//SearchFilterGroup searchFilterGroup = new SearchFilterGroup( new ArrayList<SearchFilter>(), menuId, filterType);
    	SearchFilter searchFilter;
    	
    	JSONArray filter = null;
    	String filterCode = "";
    	String filterName = "";
    	String filterResultCount = "0";
    	
    	
        t = filterList.length();
        
    	for (i=0;i<t;i++){        		
    		try {
				filter = filterList.getJSONArray(i);
				
				filterCode = filter.getString(0);
				filterResultCount = filter.getString(1);
				filterName = filterCode;
    		
				x = i + (filterListId * 100) ;
				searchFilter = new SearchFilter(i, x, filterName, filterResultCount, filterCode, searchFilterGroup.getFilterType() );
				searchFilterGroup.add(searchFilter);        			
				
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}        	            
    }*/
}
