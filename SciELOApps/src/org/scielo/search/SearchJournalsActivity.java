package org.scielo.search;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;




public class SearchJournalsActivity extends SearchActivity {
	
	private String serviceURL = "";
	private String filter = "";
	
	//private String searchResultCount;
	//private String currSearchExpr = "";
    
	private SciELONetwork jc;
	private PairsList languages;
	private PairsList subjects;

    private Journal document;
    private ArrayAdapter<Journal> aa;
    private ArrayList<Journal> searchResultList =  new ArrayList<Journal>();
	private SearchJournalsResult ssData;

	GridView paginationGridView;    
	ArrayAdapter<Page> aaPage;    
	ArrayList<Page> pagesList  = new ArrayList<Page>();
	Page page;

	@Override	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.search);
	    
	    clusterCodeOrder = getResources().getStringArray(R.array.cluster_list_journal);
	    serviceURL = this.getResources().getString(R.string.search_feed_journal);
	    
		jc = new SciELONetwork(
  		    getResources().getStringArray(R.array.collections_code),
			getResources().getStringArray(R.array.collections_name), 
			getResources().getStringArray(R.array.log_collections_code), 
			getResources().getStringArray(R.array.collections_url) );		
		subjects = new PairsList(getResources().getStringArray(R.array.subjects_id),
				getResources().getStringArray(R.array.subjects_name));
		languages = new PairsList(getResources().getStringArray(R.array.languages_id),
				getResources().getStringArray(R.array.languages_name));
		
		ssData = new SearchJournalsResult(serviceURL, jc, subjects, languages, searchResultList);
		ss = new SearchService();
		
		searchResultListView = (ListView) findViewById(R.id.list);

	    int resID = R.layout.list_item_journal;
	    aa = new JournalAdapter(this, resID, searchResultList);
	    searchResultListView.setAdapter(aa);	    
	    searchResultListView.setOnItemClickListener(new OnItemClickListener() {
		       @Override
			   public void onItemClick(AdapterView<?> _av, View _v, int _index, long id) {
		           document = searchResultList.get(_index);
		           //showDialog(SEARCH_RESULT_DIALOG);
		           
		           Intent docIntent = new Intent(_v.getContext(), SearchIssuesActivity.class);
		           //docIntent.putExtra("DATA", selectedSearchResult);
		           docIntent.putExtra("id", document.getId());
		           docIntent.putExtra("title", document.getTitle());
		           docIntent.putExtra("collection", document.getCollection());
		           docIntent.putExtra("collection_id", document.getCollectionId());
		           
		           startActivity(docIntent);
		           
	               
		       }
		});
	    paginationGridView = (GridView) findViewById(R.id.paginationListView);
	    aaPage = (ArrayAdapter<Page>) new PaginationItemAdapter(this, R.layout.pagination, pagesList);
	    paginationGridView.setAdapter(aaPage);	    
	    paginationGridView.setOnItemClickListener(new OnItemClickListener() {
		       @Override
			   public void onItemClick(AdapterView<?> _av, View _v, int _index, long arg3) {
		           page = pagesList.get(_index);
		           pagePosition = page.getPosition( );
		           doSearch();	
		       }
		    });

	    //oldOnCreate();
	    //onSearchRequested();
	    handleIntent(getIntent());
	}	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
      
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.menu_journals, menu);
        
    	return true;
    }
	

	protected String getURL(){
		return ssData.getURL(query, "20", this.filter, this.pagePosition);
	}
	protected void loadAndDisplayData(String result){
		
		
		ssData.loadData(result);
		//searchResultCount = ssData.getResultCount();
		clusterCollection = ssData.getSearchClusterCollection();
		
		aa.notifyDataSetChanged();		
		aaPage.notifyDataSetChanged();
	}
}
