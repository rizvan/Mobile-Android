package org.scielo.search;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;



public class TOCActivity extends SearchActivity {
	protected Document searched;    
    protected ArrayList<Document> resultList =  new ArrayList<Document>();

	    
	private Issue issue;	
	String collectionId;
		
	@Override	
	public void onCreate(Bundle savedInstanceState) {
		
		
		super.onCreate(savedInstanceState);
	    
	    
		issue = new Issue();
		Journal j = new Journal();
		
		j.setCollection(getIntent().getStringExtra("collection"));
		j.setTitle(getIntent().getStringExtra("title"));
		
	    issue.setId(getIntent().getStringExtra("id"));
	    issue.setJournal(j);
	    issue.setNumber(getIntent().getStringExtra("n"));
	    issue.setSuppl(getIntent().getStringExtra("v"));
	    issue.setVolume(getIntent().getStringExtra("s"));
	    /*
	     * docIntent.putExtra("id", document.getId());
		           docIntent.putExtra("date", document.getDate());
		           docIntent.putExtra("v", document.getVolume());
		           docIntent.putExtra("n", document.getNumber());
		           docIntent.putExtra("s", document.getSuppl());
		           docIntent.putExtra("collection_id", document.getJournal().getCollectionId());
		           docIntent.putExtra("collection", document.getJournal().getCollection());
		           docIntent.putExtra("title", document.getJournal().getTitle());
		           s
	     * 
	     */
	    query_id = issue.getId();
	    collectionId = getIntent().getStringExtra("collection_id");
		
	    displayQuery = issue.getIssueLabel(true);
	    
		searcher = new TOCSearcher(this.getResources().getString(R.string.search_feed_issuetoc), resultList, pagesList, SciELOAppsActivity.myConfig.getJcn().getItem(collectionId));
		
		
		
		int resID = R.layout.list_item_doc;
	    arrayAdapter = new DocumentAdapter(this, resID, resultList, true);
	    
	    searchResultListView.setAdapter(arrayAdapter);	    
	    searchResultListView.setOnItemClickListener(new OnItemClickListener() {
		       @Override
			   public void onItemClick(AdapterView<?> _av, View _v, int _index, long id) {
		           searched = resultList.get(_index);
		           //showDialog(SEARCH_RESULT_DIALOG);
		           
		           Intent docIntent = new Intent(_v.getContext(), DocumentActivity.class);
		           //docIntent.putExtra("DATA", selectedSearchResult);
		           String q = "id:" + '"' + "art-" + searched.getDocumentId() + "^c" + searched.getCol().getId() + '"';
		           docIntent.putExtra("query", q);
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
	    setQueryAndSearchAndDisplay(getIntent());
	}
		
	
		
	protected String getURL(){
		//this.pagePosition = aaPage.getPageSelected();
		return ((TOCSearcher) searcher).getURL( query, "20", this.filter, this.selectedPageIndex, collectionId);
	}
	protected void loadAndDisplayResult(String result){
		displayHeader = issue.getIssueLabel(true);
		
		super.loadAndDisplayResult(result);
	}
	
	
}
