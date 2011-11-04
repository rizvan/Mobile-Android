package org.scielo.search;


import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JournalSearcher extends Searcher {
	// Parse to get translated text
	//protected JSONObject facetFields;
	
	
	protected ArrayList<Journal> resultList;
	private FollowingLetters followingLetters;
	
	private boolean rebuildPagination;
	private boolean isInit;
	
	JournalSearcher(IdAndValueObjects urls,  ArrayList<Journal> searchResultList, ArrayList<Page> pagesList){
		super(pagesList, urls);
		
    	this.resultList = searchResultList;
		
		followingLetters = new FollowingLetters(SciELOAppsActivity.myConfig.getLetters());
    }
	
	public String buildURL(String searchExpression, String itemsPerPage, String filter, int selectedPageIndex) {
		String r = "";
		String filterSubject = "";
		String filterCollection = "";
		String filterInitialLetter = "";
		
		String[] temp = filter.split(" AND ");
		
		
		for (int i=0; i < temp.length;i++){
			if (temp[i].contains("ac:")){
				filterSubject = temp[i].substring(3);
			} else {
				if (temp[i].contains("in:")){
					filterCollection = temp[i].substring(3);
				} else {
					if (temp[i].contains("le:")){
						filterInitialLetter = temp[i].replace("le:","");
					} 
				}
 
			}
		}
		
		
		if ((filterSubject.length()==0) && (filterCollection.length()==0)){
			
			if (filterInitialLetter.length()>0){
				r = URL_list.getItem("alphabetic").getValue() ;
				rebuildPagination = false;
			} else {
				r = URL_list.getItem("initial_url").getValue() ;
				//generateAlphabeticList();
				rebuildPagination = false;
				isInit = true;
			}
			
		} else {
			if ((filterSubject.length()>0) && (filterCollection.length()>0)){
				r = URL_list.getItem("collection_subject").getValue();
				r = r.replace("SUBJECT", filterSubject );
				r = r.replace("COLLECTION", filterCollection);
				rebuildPagination = true;
			} else{
				if (filterSubject.length()>0){					
					r = URL_list.getItem("subject").getValue();
					r = r.replace("SUBJECT", filterSubject );
					rebuildPagination = true;
				} else{
					r = URL_list.getItem("collection").getValue();
					r = r.replace("COLLECTION", filterCollection );
					rebuildPagination = true;
				}
			}
		}
		
		if (filterInitialLetter.length() > 0){	
			
			IdAndValue item;
			String l = filterInitialLetter.replace("\"", "");
			
			r = r.replace("LETTERz" ,'"'+ followingLetters.getFollowing(l)   + '"');
			
			item = SciELOAppsActivity.myConfig.getLetters().getItem(l);
			r = r.replace("LETTER" , '"'+ item.getId()+ '"');
			
			rebuildPagination = false;
		} else {
			r = r.replace(",LETTERz", ",{}");						
			r = r.replace(",LETTER", "");
			//rebuildPagination = true;
		}
		
		r = r.replace("\"", "%22");
		r = r.replace(" ", "%20");
		return r;
	}
	
	public void readRawResult(JSONObject rawResult){		
		try {
			
			documentRoot = rawResult.getJSONArray("rows");
			iTotalResults = documentRoot.length();
			if (isInit){
				totalQtd = rawResult.getString("total_rows") ;
			} 			
		} catch(JSONException e){
			Log.d(TAG, "JSONException", e);				
		}
	}

	
	
	public void loadResultList(){
		JSONObject resultItem ;
		Journal r;
		
		String collectionCode;
		String last = "";
		String _pid;
		JSONArray s;
		String subj = "";
		JournalsCollection col = new JournalsCollection();
		
		if (this.documentRoot.length()>0){
			resultList.clear();
			/*
			 * {"id":"9553ce99-9536-47de-b987-3a5626e7680d","key":["arg","20030404"],"value":{"collection":"arg","issn":"0002-7014","title":"Ameghiniana","subject":["PALEONTOLOGIA"],"publisher":{"_":"Asociaci\u00f3n Paleontol\u00f3gica Argentina"},"insert_date":"20030404"}},
	
			 * 
			 */
			//message = "docs.length: " +  new Integer(this.docs.length()).toString() + "\n" + "rows: " + this.itemsPerPage;
			String t = new Integer(this.documentRoot.length()).toString();
			String str_i = "";
			
			totalResults = t;
			
			if (rebuildPagination){
				//pagination.resetLetters();
			}
			
			for (int i=0; i<this.documentRoot.length(); i++){
				r = new Journal();
				last = "";
				
				str_i = new Integer(i+1).toString();
				try {				
					last = last + "\n" +"item " ;
					resultItem = this.documentRoot.getJSONObject(i).getJSONObject("value");
					r.setPosition( str_i  + "/" + t );
					
					Log.d(TAG, "[" + str_i + "] 1");
					try {
						r.setTitle(  resultItem.getString("title"));
						if (rebuildPagination){
							//pagination.addLetter(r.getTitle().substring(0,1));
						}
						
					} catch (JSONException e) {
						last = last + "\n" +"ti";
						Log.d(TAG, "[" + str_i + "]" + last);
					}
					Log.d(TAG, "[" + str_i + "] 2");
					try {
						collectionCode = resultItem.getString("collection");
						r.setCollectionId(collectionCode);
					} catch (JSONException e) {
						last = last + "\n" +"in" ;	
						collectionCode = "";
						Log.d(TAG, "[" + str_i + "]" + last);
					}
					Log.d(TAG, "[" + str_i + "] 3");
					try {
						_pid = resultItem.getString("issn");	
						r.setId(_pid);
					} catch (JSONException e) {
						last = last + "\n" +"id" ;
						_pid = "";
						Log.d(TAG, "[" + str_i + "]" + last);
					}
					Log.d(TAG, "[" + str_i + "] 4");
					try {
						s = resultItem.getJSONArray("subject");
						subj = "";
						for (int k=0; k<s.length();k++){
							subj = subj + s.getString(k) +  ";";
						}
						r.setSubjects(subj);
						
					} catch (JSONException e) {
						last = last + "\n" +"id" ;
						_pid = "";
						Log.d(TAG, "[" + str_i + "] " + last);
					}
					
					Log.d(TAG, "[" + str_i + "] 5");
					col = SciELOAppsActivity.myConfig.getJcn().getItem(collectionCode);
					r.setCollectionName(col.getName());
					r.setCollectionId(col.getId());
					
					resultList.add(r);
						
				} catch (JSONException e) {
					Log.d(TAG, "JSONException loadResultList " + str_i + " " + last, e);	
		        } 
				
			}
			if (rebuildPagination){
				//pagination.loadPagesList(pagesList);
			}
		}
	}
}
