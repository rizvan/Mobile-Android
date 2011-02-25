package org.scielo.search;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;


import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class TOCResult extends SearchResult {

	protected JSONObject facetFields;
	
	protected String generic_PDF_URL;	
	
	protected ArrayList<Document> searchResultList;
	
	protected SciELONetwork jc;
	protected IdAndValueObjects subjects;
	protected IdAndValueObjects languages;
	
	

	TOCResult(String url, String _generic_pdf_url, SciELONetwork jc, IdAndValueObjects subjects, IdAndValueObjects languages, ArrayList<Document> searchResultList, ArrayList<Page> pagesList){
		super(pagesList);
		this.url = url;
		this.generic_PDF_URL = _generic_pdf_url;		
		
    	this.jc = jc;
    	this.subjects = subjects;
    	this.languages = languages;
		    	
		this.searchResultList = searchResultList;
				
    }
	public String getURL(String searchExpression, String itemsPerPage, String filter, int selectedPageIndex, String collectionId) {
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
			docs = jsonObject.getJSONArray("issuetoc");
			pagination.loadData("1", new Integer(docs.length()).toString(), docs.length(), 0);
			
		} catch(JSONException e){
			Log.d(TAG, "JSONException", e);				
		}
	}
	
	

	public boolean loadClusterCollection() {
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
				//r.setPosition( new Integer(i + this.currentItem).toString() + "/" + new Integer(this.resultCount).toString() );
				
				try {
					r.setDocumentTitle( clean( resultItem.getJSONArray("ti").getString(0)));	
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
	private String clean(String title){
		return title.replace("<b>", "").replace("</b>", "").replace("<i>", "").replace("</i>","");
	}
	
}
