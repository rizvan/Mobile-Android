package org.scielo.search;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class SearchServiceData {
	// Parse to get translated text
	private static final String TAG = "SearchServiceData";

	
	
	private JSONArray docs;
	private JSONObject facetFields;
	
	String generic_PDF_URL;
	
    String resultCount;
    String from;
    int currentItem;
    int itemsPerPage;
    
	SearchServiceData(String _data, String _generic_pdf_url){		
		JSONObject jsonObject;
		JSONObject diaServerResponse;
		JSONObject response;
		JSONObject responseParameters;
		
		this.generic_PDF_URL = _generic_pdf_url;
		
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
			
		} catch(JSONException e){
			Log.d(TAG, "JSONException", e);				
		}
	}
	
	public String getResultCount(){
		return this.resultCount;
	}
	public void loadResultList(ArrayList<Document> searchResultList, JournalCollections jc, ArrayList<Page> pagesList){
		JSONObject resultItem ;
		Document r;
		String result;
		String collectionCode;
		
		String link;
		Article_PDF_URL article_PDF_URL;
		String _pid;
		String _filename;
		String _lang;
		Page p;
	    int i;
	    int k;
	    String pText;
	    
	    boolean stop = false;
		
		
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
	    
		searchResultList.clear();
		int test = 0;
		for (i=0; i<this.docs.length(); i++){
			r = new Document();
			try {
				resultItem = this.docs.getJSONObject(i);
				test++;
				//r.setPosition( new Integer(i+currentItem).toString() );
				r.setPosition( new Integer(i + this.currentItem -1).toString() + "(" + new Integer(test).toString() + "," + new Integer(i).toString() + "," + new Integer(this.currentItem).toString() +  ")" );
				
				r.setDocumentTitle(  resultItem.getJSONArray("ti").getString(0));
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
				r.setDocumentId(_pid);
				r.setCollectionId(collectionCode);
				
				_filename = resultItem.getString("filename");
				
				_lang = resultItem.getJSONArray("la").getString(0);
				
				link = article_PDF_URL.getURL(jc.getCollectionUrl(collectionCode), jc.getCollectionAppName(collectionCode), _pid, _filename, _lang);
				
				r.setDocumentPDFLink(link);
				r.setDocumentCollection(jc.getCollectionName(collectionCode));
				r.setDocumentAbstracts(resultItem.getJSONArray("ab_" + _lang).getString(0));
				//r.setDocumentPDFLink("<a href=" + '"' + link + '"' +"> </a>");
				//r.setDocumentPDFLink("http://teste.scielo.br");
				//r.setJournalISSN(text);
				//r.setJournalTitle(resultItem.getJSONArray("ta").getString(0));
				r.setIssueLabel(resultItem.getJSONArray("fo").getString(0));
				
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
