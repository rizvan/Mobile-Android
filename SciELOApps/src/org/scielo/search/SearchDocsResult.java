package org.scielo.search;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class SearchDocsResult extends SearchResult{
	// Parse to get translated text
	private JSONObject facetFields;	
	private String generic_PDF_URL;	
	
	private ArrayList<Document> searchResultList;
		
	private SciELONetwork jc;
	private PairsList subjects;
	private PairsList languages;
	
	//private Pagination pagination;
	
	
	SearchDocsResult(String url, String _generic_pdf_url, SciELONetwork jc, PairsList subjects, PairsList languages, ArrayList<Document> searchResultList, ArrayList<Page> pagesList ){
		super(url, pagesList);
		
    	
    	this.generic_PDF_URL = _generic_pdf_url;		
		
    	this.jc = jc;
    	this.subjects = subjects;
    	this.languages = languages;
		    	
		this.searchResultList = searchResultList;
		
		
		
    }
	public String getURL(String searchExpression, String itemsPerPage, String filter, String pagePosition) {
		String u = "";
		String query = "";
		//URLEncoder.encode(queryURL, "UTF-8")
		u = this.url;
		u =	u.replace("&amp;", "&" );
		if (itemsPerPage.length()>0){
			query = query + "&count=" + itemsPerPage;
		}	
		if (pagePosition.length()==0 || pagePosition.equals("0")){
			
			query = query + "&start=0" ;
			
		} else {
			query = query + "&start=" + pagePosition;
			
			
		}
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
	@Override
	public void loadControlData(String _data){		
		JSONObject diaServerResponse;
		JSONObject response;
		JSONObject responseParameters;
		
		
		try {
			diaServerResponse = jsonObject.getJSONArray("diaServerResponse").getJSONObject(0);
			Log.d("SearchResult_DOC","1");	
			this.facetFields = diaServerResponse.getJSONObject("facet_counts").getJSONObject("facet_fields");
			Log.d("SearchResult_DOC","2");	
			response = diaServerResponse.getJSONObject("response");
			Log.d("SearchResult_DOC","3");	
			responseParameters = diaServerResponse.getJSONObject("responseHeader").getJSONObject("params");
			Log.d("SearchResult_DOC","4");	
			
			resultCount = response.get("numFound").toString();				
			Log.d("SearchResult_DOC","5");	
			
			from = responseParameters.get("start").toString();
			Log.d("SearchResult_DOC","6");	
			itemsPerPage = Integer.parseInt(responseParameters.get("rows").toString());
			Log.d("SearchResult_DOC","7");	
			
			this.docs = response.getJSONArray("docs");
			Log.d("SearchResult_DOC","8");	
			
		} catch(JSONException e){
			Log.d(TAG, "JSONException", e);				
		}
	}
	@Override
	public boolean loadClusterCollection() {
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
    	return r;
	}
	@Override
	public void loadSearchResultList(){
		JSONObject resultItem ;
		int k;
	    Document r;
		String result;
		String collectionCode;
		
		String link;
		Article_PDF_URL article_PDF_URL = new Article_PDF_URL(this.generic_PDF_URL);
		String _pid;
		String _filename;
		String _lang;
		String abstracts;
	    String last = "";
	    String lang_code = "";

	    Cluster languageCluster;
	    SciELOCollection col = new SciELOCollection();
	    
		languageCluster = clusterCollection.getItemById("la");
		searchResultList.clear();
		
		//message = "docs.length: " +  new Integer(this.docs.length()).toString() + "\n" + "rows: " + this.itemsPerPage;
		for (int i=0; i<this.docs.length(); i++){
			r = new Document();
			last = "";
			try {				
				last = last + "\n" +"item " ;
				resultItem = this.docs.getJSONObject(i);
				//r.setPosition( new Integer(i+currentItem).toString() );
				r.setPosition( new Integer(i + this.currentItem).toString() + "/" + new Integer(this.resultCount).toString() );
				
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
					r.setCollectionId(collectionCode);
				} catch (JSONException e) {
					last = last + "\n" +"in" ;	
					collectionCode = "";
				}
				try {
					_pid = resultItem.getString("id");	
					_pid = _pid.replace("art-", "");
					_pid = _pid.replace("^c" + r.getCollectionId(), "");
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
				col = jc.getItem(collectionCode);			
				link = article_PDF_URL.getURL(col.getUrl(), col.getNickname(), _pid, _filename, _lang);
				r.setDocumentPDFLink(link);
				r.setDocumentCollection(col.getName());
				
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
