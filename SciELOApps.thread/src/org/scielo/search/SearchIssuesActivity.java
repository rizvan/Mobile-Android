package org.scielo.search;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;


import android.view.View;
import android.widget.AdapterView;

import android.widget.AdapterView.OnItemClickListener;

public class SearchIssuesActivity extends SearchActivity {

	protected Issue searched;
	protected ArrayList<Issue> resultList =  new ArrayList<Issue>();
	
        
	private String  collectionId = "";
			
	
	@Override	
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
	    
	    
	    Journal journal = new Journal();
		
	    collectionId = getIntent().getStringExtra("collection_id");
	    
		journal.setId(getIntent().getStringExtra("id"));
		journal.setTitle(getIntent().getStringExtra("title"));
		journal.setCollectionName(getIntent().getStringExtra("collection"));
		journal.setCollectionId(collectionId);
	    query_id = journal.getId();
	    
		
		searcher = new IssuesSearcher( this.getResources().getString(R.string.search_feed_issues), resultList, pagesList, journal);
		displayQuery = getIntent().getStringExtra("title");
		
		
	    int resID = R.layout.list_item_issue;
	    arrayAdapter = new IssuesListAdapter(this, resID, resultList);
	    searchResultListView.setAdapter(arrayAdapter);	    
	    searchResultListView.setOnItemClickListener(new OnItemClickListener() {
		       @Override
			   public void onItemClick(AdapterView<?> _av, View _v, int _index, long id) {
		           searched = resultList.get(_index);
		           //showDialog(SEARCH_RESULT_DIALOG);
		           
		           Intent docIntent = new Intent(_v.getContext(), TOCActivity.class);
		           //docIntent.putExtra("DATA", selectedSearchResult);
		           docIntent.putExtra("id", searched.getId());
		           docIntent.putExtra("date", searched.getDate());
		           docIntent.putExtra("v", searched.getVolume());
		           docIntent.putExtra("n", searched.getNumber());
		           docIntent.putExtra("s", searched.getSuppl());
		           docIntent.putExtra("collection_id", searched.getJournal().getCollectionId());
		           docIntent.putExtra("collection", searched.getJournal().getCollectionName());
		           docIntent.putExtra("title", searched.getJournal().getTitle());
		           startActivity(docIntent);
		           
	               
		       }
		});
	    paginationGridView.setAdapter(aaPage);
	    paginationGridView.setOnItemClickListener(new OnItemClickListener() {
		       @Override
			   public void onItemClick(AdapterView<?> _av, View _v, int _index, long arg3) {
		           
		           selectedPageIndex = _index;
		           searchAndDisplay();	
		       }
		    });

	    //oldOnCreate();
	    //onSearchRequested();
	    setQueryAndSearchAndDisplay(getIntent());
	}	
	
	
	protected String getURL(){
		//this.pagePosition = aaPage.getPageSelected();
		return ((IssuesSearcher) searcher).buildURL(query, "20", this.filter, this.selectedPageIndex, collectionId);
	}
	protected void loadAndDisplayResult(String result){		
		displayHeader = ((IssuesSearcher) searcher).getJournal().getTitle() + " " + ((IssuesSearcher) searcher).getJournal().getId();
		super.loadAndDisplayResult(result);
		
	}

	
	
}
