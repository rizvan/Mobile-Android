package org.scielo.search;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class SearchIssuesResult {
	// Parse to get translated text
	protected JSONArray docs;
	protected JSONObject facetFields;
	
	protected String generic_PDF_URL;	
	
	protected ArrayList<Issue> searchResultList;
	protected ClusterCollection clusterCollection;
	
	protected SciELONetwork jc;
	protected PairsList subjects;
	protected PairsList languages;
	
	private Pagination pagination;
	protected static final String TAG = "SearchServiceData";
	private String resultCount;
	private int currentItem;
	private String url;
	
	private Journal journal;
	
	public Journal getJournal() {
		return journal;
	}

	public void setJournal(Journal journal) {
		this.journal = journal;
	}

	public int getCurrentItem() {
		return currentItem;
	}

	public void setCurrentItem(int currentItem) {
		this.currentItem = currentItem;
	}

	public void setResultCount(String resultCount) {
		this.resultCount = resultCount;
	}

	SearchIssuesResult(String url, SciELONetwork jc, PairsList subjects, PairsList languages, ArrayList<Issue> searchResultList){
		clusterCollection = new ClusterCollection();
    	this.url = url;
    	
    	this.jc = jc;
    	this.subjects = subjects;
    	this.languages = languages;
		    	
		this.searchResultList = searchResultList;
		this.pagination = new Pagination();	
		this.journal = new Journal();
    }
	public String getURL(String searchExpression, String itemsPerPage, String filter, String pagePosition, String colid) {
		String u = "";
		String query = "";
		//URLEncoder.encode(queryURL, "UTF-8")
		u = this.url;
		u =	u.replace("&amp;", "&" );
		if (itemsPerPage.length()>0){
			query = query + "&count=" + itemsPerPage;
		}	
		if (colid.length()>0){
			query = query + "&col=" + colid;
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
		
		String from = "";
		int itemsPerPage = 1000;
		
		
		try {
			jsonObject = new JSONObject(_data);
			docs = jsonObject.getJSONArray("issues");
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
	public ArrayList<Page> getPageList(){
		return this.pagination.getPageList();
	}
	public ClusterCollection getSearchClusterCollection(){
		return this.clusterCollection;
	}
	

	private boolean loadClusterCollection() {
		return true;
	}
	
	public void loadSearchResultList(){
		JSONObject resultItem ;
		Issue r;
		
		String last = "";
		
		
		searchResultList.clear();
		
		//message = "docs.length: " +  new Integer(this.docs.length()).toString() + "\n" + "rows: " + this.itemsPerPage;
		for (int i=0; i<this.docs.length(); i++){
			r = new Issue();
			last = "";
				
			try {				
				last = last + "\n" +"item " ;
				resultItem = this.docs.getJSONObject(i);
				r.setJournal(journal);
				try {					
					r.setDate(resultItem.getString("year"));	
				} catch (JSONException e) {
					last = last + "\n" +"year";
				}
				try {
					r.setVolume(resultItem.getString("vol"));					
				} catch (JSONException e) {
					last = last + "\n" +"vol" ;						
				}
				try {
					r.setNumber(resultItem.getString("issue"));						
				} catch (JSONException e) {
					last = last + "\n" +"number" ;
					
				}
				try {
					r.setSuppl(resultItem.getString("suppl"));						
				} catch (JSONException e) {
					last = last + "\n" +"suppl" ;			
				}
				try {
					r.setId(resultItem.getString("pid"));						
				} catch (JSONException e) {
					last = last + "\n" +"pid" ;			
				}
				
				
				
				searchResultList.add(r);
			} catch (JSONException e) {
				Log.d(TAG, "JSONException loadResultList " + new Integer(i).toString() + " " + last, e);	
	        } 
		}
	}
	
}
