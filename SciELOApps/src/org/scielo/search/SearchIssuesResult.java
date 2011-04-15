package org.scielo.search;


import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class SearchIssuesResult extends SearchResult {
	
	protected JSONObject facetFields;	
	protected String generic_PDF_URL;		
	protected ArrayList<Issue> searchResultList;
	
	protected JournalsCollectionsNetwork jc;
	protected IdAndValueObjects subjects;
	protected IdAndValueObjects languages;
	
	private Journal journal;
	
	public Journal getJournal() {
		return journal;
	}

	public void setJournal(Journal journal) {
		this.journal = journal;
	}

	
	SearchIssuesResult(String url, JournalsCollectionsNetwork jc, IdAndValueObjects subjects, IdAndValueObjects languages, ArrayList<Issue> searchResultList, ArrayList<Page> pagesList){
		super(pagesList);
		this.url = url;
    	this.jc = jc;
    	this.subjects = subjects;
    	this.languages = languages;
		this.searchResultList = searchResultList;
		this.journal = new Journal();
    }
	public String getURL(String searchExpression, String itemsPerPage, String filter, int selectedPageIndex, String colid) {
		String u = "";
		//URLEncoder.encode(queryURL, "UTF-8")
		u = this.url;
		u =	u.replace("&amp;", "&" );
		
		if (searchExpression.length()>0){
			u = u.replace("PIDz", '"' + searchExpression + "z" + '"');
			u = u.replace("PID", '"' + searchExpression + '"');
			
		}
		
		return u ;						
	}
	public void specLoadPaginationAndDocsData(){		
		try {
			docs = jsonObject.getJSONArray("rows");
			//pagination.loadData("1", new Integer(docs.length()).toString(), 3000, 0);			
		} catch(JSONException e){
			Log.d(TAG, "JSONException", e);				
		}
	}
	
	public void specLoadSearchResultList(){
		JSONObject resultItem ;
		Issue r;
		String suppl = "";
		String last = "";
		String id = "";
		
		searchResultList.clear();
		
		//message = "docs.length: " +  new Integer(this.docs.length()).toString() + "\n" + "rows: " + this.itemsPerPage;
		for (int i=0; i<this.docs.length(); i++){
			r = new Issue();
			r.setJournal(journal);
			
			last = "";
			suppl = "";
			try {				
				last = last + "\n" +"item " ;
				resultItem = this.docs.getJSONObject(i);
				id = resultItem.getString("key");
				
				try {					
					resultItem = resultItem.getJSONObject("doc");	
				} catch (JSONException e) {
					last = last + "\n" +"doc";
				}
				// "v31":[{"_":"23"}]

				try {					
					// jsonObject.getJSONArray("diaServerResponse").getJSONObject(0).getJSONObject("responseHeader").getJSONObject("params");
					r.setDate(resultItem.getJSONArray("v65").getJSONObject(0).getString("_").substring(0,4));	
				} catch (JSONException e) {
					last = last + "\n" +"year";
				}
				try {
					r.setVolume(resultItem.getJSONArray("v31").getJSONObject(0).getString("_"));					
				} catch (JSONException e) {
					last = last + "\n" +"vol" ;						
				}
				try {
					r.setNumber(resultItem.getJSONArray("v32").getJSONObject(0).getString("_"));						
				} catch (JSONException e) {
					last = last + "\n" +"number" ;
					
				}
				try {
					suppl = resultItem.getJSONArray("v131").getJSONObject(0).getString("_");
				} catch (JSONException e) {
					last = last + "\n" +"suppl" ;			
				}
				if (suppl==""){
					try {
						suppl = resultItem.getJSONArray("v132").getJSONObject(0).getString("_");
					} catch (JSONException e) {
						last = last + "\n" +"suppl" ;			
					}					
				}
				
				r.setSuppl(suppl);
				
					r.setId(id);						
				
				
				
				searchResultList.add(r);
			} catch (JSONException e) {
				Log.d(TAG, "JSONException loadResultList " + new Integer(i).toString() + " " + last, e);	
	        } 
		}
	}
	
}
