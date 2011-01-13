package org.scielo.search;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class SearchIssuesResult extends SearchResult {
	
	protected JSONObject facetFields;	
	protected String generic_PDF_URL;		
	protected ArrayList<Issue> searchResultList;
	
	protected SciELONetwork jc;
	protected PairsList subjects;
	protected PairsList languages;
	
	private Journal journal;
	
	public Journal getJournal() {
		return journal;
	}

	public void setJournal(Journal journal) {
		this.journal = journal;
	}

	
	SearchIssuesResult(String url, SciELONetwork jc, PairsList subjects, PairsList languages, ArrayList<Issue> searchResultList, ArrayList<Page> pagesList){
		super(url, pagesList);
    	this.jc = jc;
    	this.subjects = subjects;
    	this.languages = languages;
		this.searchResultList = searchResultList;
		this.journal = new Journal();
    }
	public String getURL(String searchExpression, String itemsPerPage, String filter, int selectedPageIndex, String colid) {
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
		if (selectedPageIndex>0){
			query = query + "&start=" + pagination.getPageSearchKey(selectedPageIndex);
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
	public void loadPaginationAndDocsData(){		
		try {
			docs = jsonObject.getJSONArray("issues");
			pagination.loadData("1", new Integer(docs.length()).toString(), 3000, 0);			
		} catch(JSONException e){
			Log.d(TAG, "JSONException", e);				
		}
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
