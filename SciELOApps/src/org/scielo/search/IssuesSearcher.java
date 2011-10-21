package org.scielo.search;


import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;


import android.util.Log;

public class IssuesSearcher extends Searcher {
	
	
	protected ArrayList<Issue> searchResultList;
	
	private Journal journal;
	
	//protected JSONObject facetFields;	
	//protected String generic_PDF_URL;		
	
	
	IssuesSearcher(String url, ArrayList<Issue> searchResultList, ArrayList<Page> pagesList, Journal journal){
		super(pagesList, url);
		this.searchResultList = searchResultList;
		this.journal = journal;
    }
	public Journal getJournal() {
		return journal;
	}

	public String buildURL(String searchExpression, String itemsPerPage, String filter, int selectedPageIndex, String colid) {
		String u = "";
		
		
		u =	URL.replace("&amp;", "&" );
		
		if (searchExpression.length()>0){
			u = u.replace("PIDz", '"' + searchExpression + "z" + '"');
			u = u.replace("PID", '"' + searchExpression + '"');
			
		}
		
		return u ;						
	}
	
	
	public void loadResultList(){
		JSONObject resultItem ;
		Issue r;
		String suppl = "";
		String last = "";
		String id = "";
		
		searchResultList.clear();
		
		//message = "docs.length: " +  new Integer(this.docs.length()).toString() + "\n" + "rows: " + this.itemsPerPage;
		for (int i=0; i<this.documentRoot.length(); i++){
			r = new Issue();
			r.setJournal(journal);
			
			last = "";
			suppl = "";
			try {				
				last = last + "\n" +"item " ;
				resultItem = this.documentRoot.getJSONObject(i);
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
