package org.scielo.search;


import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class SearchJournalsResult extends SearchResult {
	// Parse to get translated text
	//protected JSONObject facetFields;
	
	
	protected ArrayList<Journal> searchResultList;
	
	protected SciELONetwork jc;
	protected IdAndValueObjects subjects;
	protected IdAndValueObjects languages;
	protected IdAndValueObjects URL_list;
	
	protected IdAndValueObjects currentAndNextLetters;
	
	private AlphabeticPagination pagination;
	
	private boolean rebuildPagination;
	private boolean isInit;
	
	String journalsTotal;
	
	
	public String getJournalsTotal() {
		return journalsTotal;
	}
	
	SearchJournalsResult(IdAndValueObjects urls, ClusterCollection clusterCollection, SciELONetwork jc, IdAndValueObjects subjects, IdAndValueObjects languages, ArrayList<Journal> searchResultList, ArrayList<Page> pagesList){
		super(pagesList);
		this.URL_list = urls;
    	this.jc = jc;
    	this.subjects = subjects;
    	this.languages = languages;
		this.searchResultList = searchResultList;
		this.clusterCollection = clusterCollection;	
		this.currentAndNextLetters = new IdAndValueObjects();
		pagination = new AlphabeticPagination();
		
    }
	
	public String getURL(String searchExpression, String itemsPerPage, String filter, int selectedPageIndex) {
		String r = "";
		String filterSubject = "";
		String filterCollection = "";
		String[] temp = filter.split(" AND ");
		String teste = "";
		
		for (int i=0; i < temp.length;i++){
			if (temp[i].contains("ac:")){
				filterSubject = temp[i].substring(3);
			} else {
				if (temp[i].contains("in:")){
					filterCollection = temp[i].substring(3);
				}
			}
		}
		
		
		if ((filterSubject.length()==0) && (filterCollection.length()==0)){
			
			if (selectedPageIndex<0){
				r = URL_list.getItem("initial_url").getValue() ;
				generateAlphabeticList();
				rebuildPagination = false;
				isInit = true;
			} else {
				r = URL_list.getItem("alphabetic").getValue() ;
				rebuildPagination = false;
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
		
		if (selectedPageIndex > -1){			
			r = r.replace("LETTERz" ,'"'+ currentAndNextLetters.getItem( teste ).getValue() + '"');
			r = r.replace("LETTER" ,'"'+ teste  + '"');
			rebuildPagination = false;
		} else {
			r = r.replace(",LETTERz", ",{}");						
			r = r.replace(",LETTER", "");
			//rebuildPagination = true;
		}
		
		return r;
	}
	
	public void loadPaginationAndDocsData(){		
		try {
			
			docs = jsonObject.getJSONArray("rows");
			
			if (isInit){
				journalsTotal = jsonObject.getString("total_rows") ;
			}
						
		} catch(JSONException e){
			Log.d(TAG, "JSONException", e);				
		}
	}

	public void loadSearchResultList(){
		JSONObject resultItem ;
		Journal r;
		
		String collectionCode;
		String last = "";
		String _pid;
		SciELOCollection col = new SciELOCollection();
		JSONArray s;
		String subj = "";
		
		searchResultList.clear();
		/*
		 * {"id":"9553ce99-9536-47de-b987-3a5626e7680d","key":["arg","20030404"],"value":{"collection":"arg","issn":"0002-7014","title":"Ameghiniana","subject":["PALEONTOLOGIA"],"publisher":{"_":"Asociaci\u00f3n Paleontol\u00f3gica Argentina"},"insert_date":"20030404"}},

		 * 
		 */
		//message = "docs.length: " +  new Integer(this.docs.length()).toString() + "\n" + "rows: " + this.itemsPerPage;
		String t = new Integer(this.docs.length()).toString();
		String str_i = "";
		
		total = t;
		
		if (rebuildPagination){
			pagination = new AlphabeticPagination();
		}
		
		for (int i=0; i<this.docs.length(); i++){
			r = new Journal();
			last = "";
			
			str_i = new Integer(i).toString();
			try {				
				last = last + "\n" +"item " ;
				resultItem = this.docs.getJSONObject(i).getJSONObject("value");
				r.setPosition( str_i  + "/" + t );
				
				Log.d(TAG, "[" + str_i + "] 1");
				try {
					r.setTitle(  resultItem.getString("title"));
					if (rebuildPagination){
						pagination.addLetter(r.getTitle().substring(0,1));
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
				col = jc.getItem(collectionCode);
				
				r.setCollection(col.getName());
				
				searchResultList.add(r);
					
			} catch (JSONException e) {
				Log.d(TAG, "JSONException loadResultList " + str_i + " " + last, e);	
	        } 
			
			
		}
		
	}

	private void generateAlphabeticList(){
		String u = "";
		SearchService ss = new SearchService();
		JSONObject jsonData;
		JSONArray jsonArray;
		String _data;
		String letter="";
		String nextLetter="";
		IdAndValue item ;
		
		pagination = new AlphabeticPagination();
		for (int i=65;i<91;i++){
			letter = String.valueOf((char)i);
			nextLetter = String.valueOf((char)(i+1));
			u = URL_list.getItem("letter").getValue().replace("LETTERz", '"' + nextLetter + '"' ) ;
			u = u.replace("LETTER", '"' + letter + '"' ) ;
			_data = ss.call(u);
			try {
				jsonData = new JSONObject(_data);
				try {
					jsonArray = jsonData.getJSONArray("rows");
					if (jsonArray.length()>0){
						item = new IdAndValue(letter, nextLetter);
						currentAndNextLetters.add(letter, item, false);
						pagination.addLetter(letter);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
}
