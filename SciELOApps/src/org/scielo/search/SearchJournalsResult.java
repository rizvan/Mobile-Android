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
    }
	
	public String getURL(String searchExpression, String itemsPerPage, String filter, int selectedPageIndex) {
		String r = "";
		String filterSubject = "";
		String filterCollection = "";
		String[] temp = filter.split(" AND ");
		
		
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
			r = URL_list.getItem("initial_url").getValue() + "?limit=";
			if (itemsPerPage.equals("")){
				r += "20"; 
			} else {
				r += itemsPerPage;
			}
		} else {
			if ((filterSubject.length()>0) && (filterCollection.length()>0)){
				r = URL_list.getItem("collection_subject").getValue();
				r = r.replace("SUBJECT", filterSubject );
				r = r.replace("COLLECTION", filterCollection);
			} else{
				if (filterSubject.length()>0){					
					r = URL_list.getItem("subject").getValue();
					r = r.replace("SUBJECT", filterSubject );
				} else{
					r = URL_list.getItem("collection").getValue();
					r = r.replace("COLLECTION", filterCollection );
				}
			}
		}
		
		if (selectedPageIndex>0){
			r = r.replace("LETTER\u9999" ,'"'+ pagination.getPageSearchKey(selectedPageIndex) + "z" + '"');
			r = r.replace("LETTER" ,'"'+ pagination.getPageSearchKey(selectedPageIndex)  + '"');
		} else {
			r = r.replace(",LETTER\u9999", ",{}");						
			r = r.replace(",LETTER", "");
		}
		
		return r;
	}
	
	public void loadPaginationAndDocsData(){		
		try {
			docs = jsonObject.getJSONArray("rows");
			String total;
			
			total = new Integer(docs.length()).toString() ;
			
			journalsTotal = jsonObject.getString("total_rows") ;
			
			
			pagination.loadData("1", total , docs.length(), 2);			
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
		for (int i=0; i<this.docs.length(); i++){
			r = new Journal();
			last = "";
			try {				
				last = last + "\n" +"item " ;
				resultItem = this.docs.getJSONObject(i).getJSONObject("value");
				r.setPosition( new Integer(i + pagination.getCurrentItem()).toString() + "/" + new Integer(pagination.getResultCount()).toString() );
				
				Log.d(TAG, "[" + new Integer(i).toString()+ "] 1");
				try {
					r.setTitle(  resultItem.getString("title"));
					pagination.addLetter(r.getTitle().substring(0,1));
					
				} catch (JSONException e) {
					last = last + "\n" +"ti";
					Log.d(TAG, "[" + new Integer(i).toString()+ "]" + last);
				}
				Log.d(TAG, "[" + new Integer(i).toString()+ "] 2");
				try {
					collectionCode = resultItem.getString("collection");
					r.setCollectionId(collectionCode);
				} catch (JSONException e) {
					last = last + "\n" +"in" ;	
					collectionCode = "";
					Log.d(TAG, "[" + new Integer(i).toString()+ "]" + last);
				}
				Log.d(TAG, "[" + new Integer(i).toString()+ "] 3");
				try {
					_pid = resultItem.getString("issn");	
					r.setId(_pid);
				} catch (JSONException e) {
					last = last + "\n" +"id" ;
					_pid = "";
					Log.d(TAG, "[" + new Integer(i).toString()+ "]" + last);
				}
				Log.d(TAG, "[" + new Integer(i).toString()+ "] 4");
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
					Log.d(TAG, "[" + new Integer(i).toString()+ "] " + last);
				}
				
				Log.d(TAG, "[" + new Integer(i).toString()+ "] 5");
				col = jc.getItem(collectionCode);
				
				r.setCollection(col.getName());
				
				searchResultList.add(r);
					
			} catch (JSONException e) {
				Log.d(TAG, "JSONException loadResultList " + new Integer(i).toString() + " " + last, e);	
	        } 
		}
	}
	}
