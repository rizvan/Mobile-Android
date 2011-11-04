package org.scielo.search;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.util.Log;

public class IssuesWS  {
	private String TAG  = "IssuesWS";

	public String getURL(String URL, String journalId) {
		String u = "";
		
		
		u =	URL.replace("&amp;", "&" );
		
		if (journalId.length()>0){
			u = u.replace("PIDz", '"' + journalId + "z" + '"');
			u = u.replace("PID", '"' + journalId + '"');
			
		}
		
		return u ;						
	}
	
	public void loadData(String text, Journal j, ArrayList<Issue> resultList){

		try {
			JSONObject rawResult = new JSONObject(text);
			JSONArray documentRoot = rawResult.getJSONArray("rows");
			

			loadResultList(documentRoot, j, resultList);
			
		} catch(JSONException e){
			Log.d(TAG  , "JSONException", e);				
		}
	}

	private void loadResultList(JSONArray documentRoot, Journal journal, ArrayList<Issue> searchResultList){
		JSONObject resultItem ;
		Issue r;
		String suppl = "";
		String last = "";
		String id = "";
		
		searchResultList.clear();
		
		//message = "docs.length: " +  new Integer(this.docs.length()).toString() + "\n" + "rows: " + this.itemsPerPage;
		for (int i=0; i<documentRoot.length(); i++){
			r = new Issue();
			r.setJournal(journal);
			
			last = "";
			suppl = "";
			try {				
				last = last + "\n" +"item " ;
				resultItem = documentRoot.getJSONObject(i);
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
