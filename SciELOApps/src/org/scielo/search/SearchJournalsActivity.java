package org.scielo.search;

import java.util.ArrayList;



import android.content.Intent;
import android.os.Bundle;


import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;




public class SearchJournalsActivity extends SearchActivity {

	protected Journal searched;
	//protected ArrayAdapter<Journal> arrayAdapter;
    protected ArrayList<Journal> resultList =  new ArrayList<Journal>();
	//protected JournalSearcher searcher;
	
	@Override	
	public void onCreate(Bundle savedInstanceState) {
		IdAndValueObjects searchURLs;
		
		SEPARATOR = ":";
		
		clusterCodeOrder = getResources().getStringArray(R.array.cluster_list_journal);
		super.onCreate(savedInstanceState);
		loadClusterCollection();
		searchURLs = new IdAndValueObjects();
		searchURLs.multiAdd(getResources().getStringArray(R.array.journal_urls_id),
				getResources().getStringArray(R.array.journal_urls), false);
		
		searcher = new JournalSearcher(searchURLs, resultList, pagesList);
		
		int resID = R.layout.list_item_journal;
	    arrayAdapter = new JournalAdapter(this, resID, resultList);
	    searchResultListView.setAdapter(arrayAdapter);	    
	    searchResultListView.setOnItemClickListener(new OnItemClickListener() {
		       @Override
			   public void onItemClick(AdapterView<?> _av, View _v, int _index, long id) {
		           searched = resultList.get(_index);
		           Intent docIntent = new Intent(_v.getContext(), SearchIssuesActivity.class);
		           docIntent.putExtra("id", searched.getId());
		           docIntent.putExtra("title", searched.getTitle());
		           docIntent.putExtra("collection", searched.getCollection());
		           docIntent.putExtra("collection_id", searched.getCollectionId());		           
		           startActivity(docIntent);
		       }
		});
	    
	    paginationGridView.setAdapter(aaPage);
	    paginationGridView.setOnItemClickListener(new OnItemClickListener() {
		       @Override
			   public void onItemClick(AdapterView<?> _av, View _v, int _index, long arg3) {		           
		           selectedPageIndex = _index;
		           
		           displayLetter = "/" + pagesList.get(_index).getLabel()+ ":";
		           searchAndDisplay();
		           
		           selectedPageIndex = -1;
		       }
		    });
	    

	    setQueryAndSearchAndDisplay(getIntent());
	}	
	
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
      
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.menu_journals, menu);
        
    	return true;
    }
	

	protected String getURL(){
		updateHeader = true;
		return ((JournalSearcher) searcher).buildURL(query, "",  this.filter, this.selectedPageIndex);
	}
	private void loadClusterCollection(){
		
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
				Log.d("SearchActivity", clusterCodeOrder[i]);
				
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
					Log.d("SearchActivity", "nenhum idAndValueObjects");
				}
			}
		}
	}
	
}
