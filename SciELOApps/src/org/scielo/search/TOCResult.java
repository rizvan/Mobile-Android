package org.scielo.search;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class TOCResult {
	// Parse to get translated text
	protected JSONArray docs;
	protected JSONObject facetFields;
	
	protected String generic_PDF_URL;	
	
	protected ArrayList<Document> searchResultList;
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

	TOCResult(String url, String _generic_pdf_url, SciELONetwork jc, PairsList subjects, PairsList languages, ArrayList<Document> searchResultList){
		clusterCollection = new ClusterCollection();
    	this.url = url;
    	this.generic_PDF_URL = _generic_pdf_url;		
		
    	this.jc = jc;
    	this.subjects = subjects;
    	this.languages = languages;
		    	
		this.searchResultList = searchResultList;
		this.pagination = new Pagination();		
    }
	public String getURL(String searchExpression, String itemsPerPage, String filter, String pagePosition, String collectionId) {
		String u = "";
		String query = "";
		//URLEncoder.encode(queryURL, "UTF-8")
		u = this.url;
		u =	u.replace("&amp;", "&" );
		if (collectionId.length()>0){
			query = query + "&col=" + collectionId;
		}	
		if (itemsPerPage.length()>0){
			query = query + "&count=" + itemsPerPage;
		}	
		if (pagePosition.length()>0){
			query = query + "&start=" + pagePosition;
		} else {
			query = query + "&start=1" ;
		}
		if (searchExpression.length()>0){
			try {
				query = query + "&pid=" + URLEncoder.encode(searchExpression, "UTF-8");
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
	public void loadData(String _data){		
		JSONObject jsonObject;
		
		String from;
		int itemsPerPage;
		
		
		try {
			jsonObject = new JSONObject(_data);
		
			this.docs = jsonObject.getJSONArray("issuetoc");
			Log.d("SearchServiceData","8");	
			resultCount = new Integer( this.docs.length()).toString();				
			currentItem = 1;
			from = "1";
			itemsPerPage = 1000;
			Log.d("SearchServiceData","10");	
			
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
	public ArrayList<Page> getPageList(){
		return this.pagination.getPageList();
	}
	public ClusterCollection getSearchClusterCollection(){
		return this.clusterCollection;
	}
	

	private boolean loadClusterCollection() {
    	boolean r = true;
    	
    	return r;
	}
	
	public void loadSearchResultList(){
		JSONObject resultItem ;
		Document r;
		String result;
		String collectionCode;
		
		String _pid;
		String last = "";
	    
	    SciELOCollection col = new SciELOCollection();
	    
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
				
				
				r.setDocumentCollection(col.getName());
				
				try {
					r.setIssueLabel(resultItem.getJSONArray("fo").getString(0));					
				} catch (JSONException e) {
					last = last + "\n" +"fo";
				}
				
				
				
				searchResultList.add(r);
			} catch (JSONException e) {
				Log.d(TAG, "JSONException loadResultList " + new Integer(i).toString() + " " + last, e);	
	        } 
		}
	}
	
}
