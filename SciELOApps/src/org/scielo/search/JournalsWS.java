package org.scielo.search;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;


public class JournalsWS {

	private static final String TAG = "JournalWS";
	
	NextLetter next_letter = new NextLetter(SciELOAppsActivity.myConfig.getLetters());
	private String result_total = "";
	private String docs_total = "";
	
	public JournalsWS(){
		
	}
	
	public String getURL( IdAndValueObjects journal_ws_urls, String param_filter) {
		String r = "";
		String filterSubject = "";
		String filterCollection = "";
		String filterInitialLetter = "";
		
		String[] array_filters = param_filter.split(" AND ");
		
		
		for (int i=0; i < array_filters.length;i++){
			if (array_filters[i].contains("ac:")){
				filterSubject = array_filters[i].substring(3);
			} else {
				if (array_filters[i].contains("in:")){
					filterCollection = array_filters[i].substring(3);
				} else {
					if (array_filters[i].contains("le:")){
						filterInitialLetter = array_filters[i].replace("le:","");
					} 
				}
 
			}
		}
		
		
		if ((filterSubject.length()==0) && (filterCollection.length()==0)){
			
			if (filterInitialLetter.length()>0){
				r = journal_ws_urls.getItem("alphabetic").getValue() ;
			} else {
				r = journal_ws_urls.getItem("initial_url").getValue() ;
				//r = journal_ws_urls.getItem("alphabetic").getValue() ;
				//filterInitialLetter =  "A";
				
			}
			
		} else {
			if ((filterSubject.length()>0) && (filterCollection.length()>0)){
				r = journal_ws_urls.getItem("collection_subject").getValue();
				r = r.replace("SUBJECT", filterSubject );
				r = r.replace("COLLECTION", filterCollection);
			} else{
				if (filterSubject.length()>0){					
					r = journal_ws_urls.getItem("subject").getValue();
					r = r.replace("SUBJECT", filterSubject );
				} else{
					r = journal_ws_urls.getItem("collection").getValue();
					r = r.replace("COLLECTION", filterCollection );
				}
			}
		}
		
		if (filterInitialLetter.length() > 0){	
			String l = filterInitialLetter.replace("\"", "");
			r = r.replace("LETTERz" ,'"'+ next_letter.getLetter(l)   + '"');
			r = r.replace("LETTER" , '"'+ l + '"');
		} else {
			r = r.replace(",LETTERz", ",{}");						
			r = r.replace(",LETTER", "");
			//rebuildPagination = true;
		}
		
		r = r.replace("\"", "%22");
		r = r.replace(" ", "%20");
		return r;
	}

	private void loadClusterCollection(String[] clusterCodeOrder, ClusterCollection clusterCollection){
		
		int subMenuId;
		int k;
		Cluster cluster;
		IdAndValueObjects idAndValueObjects;
		IdAndValue idAndValue;
		SearchFilter searchFilter;
		JournalsCollection c;

		

		for (int i=0;i<clusterCodeOrder.length;i++){
			idAndValueObjects = null;
			
			cluster = new Cluster(clusterCodeOrder[i]);
			if (clusterCodeOrder[i].equals("in")){
				for (k=0;k<SciELOAppsActivity.myConfig.getJcn().getCount() ;k++){        		
		    		c = SciELOAppsActivity.myConfig.getJcn().getItemByIndex(k);
		    		
		    		searchFilter = new SearchFilter(c.getName(), "0", c.getId(), cluster.getId() );
					subMenuId = k + (i * 100) ;
		    		searchFilter.setSubmenuId(subMenuId);
		    		cluster.addFilter(searchFilter, subMenuId, c.getId());        			
				}
				clusterCollection.add(cluster);
			} else {
				if (clusterCodeOrder[i].equals("ac")){
					idAndValueObjects = SciELOAppsActivity.myConfig.getSubjects();
				} else {
	    			if (clusterCodeOrder[i].equals("la")){
	    				idAndValueObjects = SciELOAppsActivity.myConfig.getLanguages();
	    			} else {
	    				if (clusterCodeOrder[i].equals("le")){
		    				idAndValueObjects = SciELOAppsActivity.myConfig.getLetters();
		    			}
	    			}
	
				}
				Log.d(TAG, clusterCodeOrder[i]);
				
				if (idAndValueObjects!=null){
			    	for (k=0;k<idAndValueObjects.getCount();k++){        		
			    		idAndValue = idAndValueObjects.getItemByIndex(k);
			    		searchFilter = new SearchFilter(idAndValue.getValue(), "0",  idAndValue.getId(), cluster.getId() );
						subMenuId = k + (i * 100) ;
			    		searchFilter.setSubmenuId(subMenuId);
			    		cluster.addFilter(searchFilter, subMenuId, idAndValue.getId());        			
					}
					clusterCollection.add(cluster);
				} else {
					Log.d(TAG, "nenhum idAndValueObjects");
				}
			}
		}
	}

	public void loadData(String text, String[] clusterCodeOrder, ClusterCollection clusterCollection,
		ArrayList<Journal> journalResultList, boolean isInit) {
	// TODO Auto-generated method stub
	// TODO Auto-generated method stub
			

			try {
				JSONObject rawResult = new JSONObject(text);
				JSONArray documentRoot = rawResult.getJSONArray("rows");
				int iTotalResults = documentRoot.length();
				result_total = new Integer(iTotalResults).toString();
				
					docs_total = rawResult.getString("total_rows") ;
				
				loadClusterCollection(clusterCodeOrder, clusterCollection);
				loadResultList(documentRoot, journalResultList);
				
			} catch(JSONException e){
				Log.d(TAG, "JSONException", e);				
			}
		}

		
		
		public void loadResultList(JSONArray documentRoot, ArrayList<Journal> journalResultList){
			JSONObject resultItem ;
			Journal r;
			
			String collectionCode;
			String last = "";
			String _pid;
			JSONArray s;
			String subj = "";
			JournalsCollection col = new JournalsCollection();
			
			if (documentRoot.length()>0){
				journalResultList.clear();
				/*
				 * {"id":"9553ce99-9536-47de-b987-3a5626e7680d","key":["arg","20030404"],"value":{"collection":"arg","issn":"0002-7014","title":"Ameghiniana","subject":["PALEONTOLOGIA"],"publisher":{"_":"Asociaci\u00f3n Paleontol\u00f3gica Argentina"},"insert_date":"20030404"}},
		
				 * 
				 */
				//message = "docs.length: " +  new Integer(this.docs.length()).toString() + "\n" + "rows: " + this.itemsPerPage;
				String t = new Integer(documentRoot.length()).toString();
				String str_i = "";
				

				for (int i=0; i<documentRoot.length(); i++){
					r = new Journal();
					last = "";
					
					str_i = new Integer(i+1).toString();
					try {				
						last = last + "\n" +"item " ;
						resultItem = documentRoot.getJSONObject(i).getJSONObject("value");
						r.setPosition( str_i  + "/" + t );
						
						Log.d(TAG, "[" + str_i + "] 1");
						try {
							r.setTitle(  resultItem.getString("title"));
							
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
						
						journalResultList.add(r);
							
					} catch (JSONException e) {
						Log.d(TAG, "JSONException loadResultList " + str_i + " " + last, e);	
			        } 
					
				}
				
			}
		}

		public String getResult_total() {
			return result_total;
		}

		public String getDocs_total() {
			return docs_total;
		}
}
