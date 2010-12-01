package org.scielo.search;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class SearchResult {
	// Parse to get translated text
	protected static final String TAG = "SearchServiceData";
	protected JSONArray docs;
	protected JSONObject facetFields;
	
	protected String generic_PDF_URL;
	
	protected String resultCount;
	protected String from;
	protected int currentItem;
	protected int itemsPerPage;
	
	protected ArrayList<Document> searchResultList;
	protected ArrayList<Page> pagesList;
	protected ClusterCollection clusterCollection;
	
	protected SciELONetwork jc;
	protected PairsList subjects;
	protected PairsList languages;
	
    SearchResult(String _generic_pdf_url, SciELONetwork jc, PairsList subjects, PairsList languages, ArrayList<Document> searchResultList ,ArrayList<Page> pagesList){		
    	clusterCollection = new ClusterCollection();
    	
    	this.generic_PDF_URL = _generic_pdf_url;		
		
    	this.jc = jc;
    	this.subjects = subjects;
    	this.languages = languages;
		    	
		this.searchResultList = searchResultList;
		this.pagesList = pagesList;		
		
    }
    public void loadData(String _data){		
		JSONObject jsonObject;
		JSONObject diaServerResponse;
		JSONObject response;
		JSONObject responseParameters;
		
		
		try {
			jsonObject = new JSONObject(_data);
			diaServerResponse = jsonObject.getJSONArray("diaServerResponse").getJSONObject(0);
			Log.d("SearchServiceData","1");	
			this.facetFields = diaServerResponse.getJSONObject("facet_counts").getJSONObject("facet_fields");
			Log.d("SearchServiceData","2");	
			response = diaServerResponse.getJSONObject("response");
			Log.d("SearchServiceData","3");	
			responseParameters = diaServerResponse.getJSONObject("responseHeader").getJSONObject("params");
			Log.d("SearchServiceData","4");	
			
			this.resultCount = response.get("numFound").toString();				
			Log.d("SearchServiceData","5");	
			
			this.from = responseParameters.get("start").toString();
			Log.d("SearchServiceData","6");	
			this.itemsPerPage = Integer.parseInt(responseParameters.get("rows").toString());
			Log.d("SearchServiceData","7");	
			
			this.docs = response.getJSONArray("docs");
			Log.d("SearchServiceData","8");	
			
			
			this.currentItem = 0;
			Log.d("SearchServiceData","9");	
			if ((this.from.length() == 0) ) {
				this.currentItem = 1;
			} else {
				this.currentItem = Integer.parseInt(this.from);	
			}
			Log.d("SearchServiceData","10");	
			loadClusterCollection();
			loadResultList();
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
	
	public void loadResultList(){
		int i;
	    int k;
	    boolean stop = false;
	    String pText;
	    Page p;
		
	    pagesList.clear();
		i = 1;
		while (!stop && (i<=6)){
			k = i * itemsPerPage;
			pText = new Integer( k - itemsPerPage + 1).toString();
			if ( Integer.parseInt(this.resultCount) > k) {
			} else {
				stop = true;
			}
			p = new Page( pText, pText);
			pagesList.add(p);

			i++;
		}
		
		loadSearchResultList();
		
		
	}
	protected void loadSearchResultList(){
		
	}
	
	private boolean loadClusterCollection() {
    	boolean r = true;
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
			        				filterName = subjects.getItem(filterCode).getValue() + "(" + filterCode + ")";
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
    	return r;
	}
	}
