package org.scielo.search;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class DocManager {

	private static final String TAG = "DocMANAGER";
	private ArrayList<Document> searchResultList;
	private String totalResults;
	private String totalQtd;
	private int iTotalResults;

	public String getURL(String URL, String itemsPerPage, String searchExpression, String filter, int selectedPageIndex) {
		String u = "";
		String query = "";
		
		//URLEncoder.encode(queryURL, "UTF-8")
		u =	URL.replace("&amp;", "&" );
		if (itemsPerPage.length()>0){
			query = query + "&count=" + itemsPerPage;
		}
		query = query + "&start=" + this.getStart(selectedPageIndex, Integer.parseInt(itemsPerPage));
		/*
		if ((selectedPageIndex<0) || (this.pagination.getPagesList()==null)){
			query = query + "&start=0" ;
		} else {
			query = query + "&start=" + this.pagination.returnSearchStartParameter(selectedPageIndex);	
		}*/
		if (searchExpression.length()>0){
			try {
				query = query + "&q=" + URLEncoder.encode(searchExpression, "UTF-8");
				
				
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (filter.length()>0){
			try {
				query = query + "&fq=" + URLEncoder.encode(filter, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return u + query;						
	}
	private String getStart(int selectedPageIndex, int itemPerPage) {
		// TODO Auto-generated method stub
		int i;
		// 0 - 1-10
		// 1 - 11-20
		// 2 - 21-30
		if (selectedPageIndex < 1) {
			i = 0;
		} else {
			i = (itemPerPage * (selectedPageIndex)) ;
		}
		return new Integer(i).toString();
	}
	public void loadData(String text, ClusterCollection c, ArrayList<Document> resultList,ArrayList<Page> pagesList) {
		// TODO Auto-generated method stub
		JSONObject diaServerResponse;
		JSONObject response;
		JSONObject responseParameters;
		
		
		try {
			JSONObject rawResult = new JSONObject(text);
			diaServerResponse = rawResult.getJSONArray("diaServerResponse").getJSONObject(0);
			Log.d("SearchResult_DOC","1");	
			loadClusterCollection(diaServerResponse.getJSONObject("facet_counts").getJSONObject("facet_fields"), c);
			Log.d("SearchResult_DOC","2");	
			response = diaServerResponse.getJSONObject("response");
			Log.d("SearchResult_DOC","3");	
			responseParameters = diaServerResponse.getJSONObject("responseHeader").getJSONObject("params");
			Log.d("SearchResult_DOC","4");	
			totalResults = response.get("numFound").toString();
			
			
			loadResultList(response.getJSONArray("docs"), c, resultList,Integer.parseInt(responseParameters.get("start").toString()));
		} catch(JSONException e){
			Log.d(TAG, "JSONException", e);				
		}
	}
	
	private void loadClusterCollection(JSONObject facetFields, ClusterCollection clusterCollection) {
    	boolean r = true;
    	int i_clusters = 0;
    	int i = 0;
    	int subMenuId = 0;
    	int filterCount = 0;
    	
    	JSONArray a_clusters = facetFields.names();
    	JSONArray clusterData;
    	JSONArray filter = null;
    	
    	SearchFilter searchFilter;
    	
    	String cluster_id ;
    	String filterCode = "";
    	String filterName = "";
    	String filterResultCount = "0";
    	
    	
    	Cluster cluster;
    	Log.d(TAG, "loadClusterCollection inicio");
    	
    	clusterCollection.clear();
    	for (i_clusters=0;i_clusters<a_clusters.length();i_clusters++){
    		cluster_id="";
    		Log.d(TAG,  new Integer(i_clusters).toString() + "/" + new Integer(a_clusters.length()).toString());
			try {
    			cluster_id = a_clusters.getString(i_clusters);
    			cluster = new Cluster(cluster_id);
    			
    			clusterData = (JSONArray) facetFields.get(cluster_id);				
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
	        				filterName = SciELOAppsActivity.myConfig.getJcn().getItem(filterCode).getName();
	        			} else {
		        			if (cluster_id.equals("la")){
		        				filterName = SciELOAppsActivity.myConfig.getLanguages().getItem(filterCode).getValue();
		        			} else {
			        			if (cluster_id.equals("ac")){
			        				filterName = SciELOAppsActivity.myConfig.getSubjects().getItem(filterCode).getValue() ;
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
	    				cluster.addFilter(searchFilter, subMenuId, filterCode);        			
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
    	
    	String t="";
    	for (i=0;i<clusterCollection.getCount();i++){
    		t = t + clusterCollection.getItemByIndex(i).display();
    	}
    	Log.d(TAG, t);
    	//return clusterCollection;
	}
	
	private void loadResultList(JSONArray documentRoot, ClusterCollection clusterCollection, ArrayList<Document> searchResultList,int selected_item_index){
		JSONObject resultItem ;
		int k;
	    Document r;
		String result;
		String collectionCode;
		
		//ArticleURL articleURL = new ArticleURL(this.generic_PDF_URL, this.generic_article_URL);
		
		String _pid;
		String _filename;
		String _lang;
		String abstracts;
	    String last = "";
	    String lang_code = "";

	    Cluster languageCluster;
	    	    
		languageCluster = clusterCollection.getItemById("la");
		searchResultList.clear();
		
		//message = "docs.length: " +  new Integer(this.docs.length()).toString() + "\n" + "rows: " + this.itemsPerPage;
		for (int i=0; i<documentRoot.length(); i++){
			r = new Document();
			last = "";
			try {				
				last = last + "\n" +"item " ;
				resultItem = documentRoot.getJSONObject(i);
				//r.setPosition( new Integer(i+currentItem).toString() );
				r.setPosition( new Integer(i + selected_item_index + 1).toString() + "/" + totalResults);
				
				try {
					r.setDocumentTitle(  resultItem.getJSONArray("ti").getString(0));	
				} catch (JSONException e) {
					last = last + "\n" +"ti";
				}
				try {
					result = "";
					for (int j=0; j<resultItem.getJSONArray("au").length(); j++) {
						result = result + resultItem.getJSONArray("au").getString(j) + "; ";
					}
					r.setDocumentAuthors(result);	
				} catch (JSONException e) {
					last = last + "\n" +"au" ;
				}
				try {
					collectionCode = resultItem.getString("in");
				} catch (JSONException e) {
					last = last + "\n" +"in" ;	
					collectionCode = "";
				}
				try {
					_pid = resultItem.getString("id");	
					_pid = _pid.replace("art-", "");
					_pid = _pid.replace("^c" + collectionCode, "");
					r.setDocumentId(_pid);
				} catch (JSONException e) {
					last = last + "\n" +"id" ;
					_pid = "";
				}
				try {
					_filename = resultItem.getString("filename");
				} catch (JSONException e) {
					_filename =  "";
					last = last + "\n" +"filename" ;
				}
				try {
					_lang = resultItem.getJSONArray("la").getString(0);
				} catch (JSONException e) {
					last = last + "\n" +"la" ;
					_lang = "";
				}
							
				
				
				r.setFilename(_filename);
				r.setLang(_lang);
				r.setCol(SciELOAppsActivity.myConfig.getJcn().getItem(collectionCode));
				
				try {
					r.setIssueLabel(resultItem.getJSONArray("fo").getString(0));					
				} catch (JSONException e) {
					last = last + "\n" +"fo";
				}
				
				abstracts = "";				
				for (k=0;k< languageCluster.getFilterCount();k++){
					lang_code = languageCluster.getFilterByIndex(k).getCode();
					
					if (lang_code.length()>0){
						try {
							abstracts = abstracts + "\n" + resultItem.getJSONArray("ab_" + lang_code).getString(0);	
						} catch (JSONException e) {
							last = last + "\n" +"ab";	
						}						
					}					
				}
				r.setDocumentAbstracts(abstracts);
				
				searchResultList.add(r);
			} catch (JSONException e) {
				Log.d(TAG, "JSONException loadResultList " + new Integer(i).toString() + " " + last, e);	
	        } 
		}
	}

	public ArrayList<Document> getSearchResultList() {
		// TODO Auto-generated method stub
		
		return searchResultList;
	}
	

	
}
