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
	
	
	//private String searchResultCount;
	//private String currSearchExpr = "";
    
	private SciELONetwork jc;
	private IdAndValueObjects languages;
	private IdAndValueObjects subjects;
	private IdAndValueObjects searchURLs;

    private Journal document;
    private ArrayAdapter<Journal> aa;
    private ArrayList<Journal> searchResultList =  new ArrayList<Journal>();
	private SearchJournalsResult ssData;

	protected GridView paginationGridView;    
	protected PaginationItemAdapter aaPage;    
	protected ArrayList<Page> pagesList  = new ArrayList<Page>();	
	protected Page page;
	


	@Override	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.search);
	    header = "";
	    selectedPageIndex = -1;
	    clusterCodeOrder = getResources().getStringArray(R.array.cluster_list_journal);
	    
		jc = new SciELONetwork();
		jc.multiAdd(
  		    getResources().getStringArray(R.array.collections_code),
			getResources().getStringArray(R.array.collections_name), 
			getResources().getStringArray(R.array.log_collections_code), 
			getResources().getStringArray(R.array.collections_url) );		
		subjects = new IdAndValueObjects();
		subjects.multiAdd(getResources().getStringArray(R.array.subjects_id),
				getResources().getStringArray(R.array.subjects_name),false);
		languages = new IdAndValueObjects();
		languages.multiAdd(getResources().getStringArray(R.array.languages_id),
				getResources().getStringArray(R.array.languages_name), false);
		searchURLs = new IdAndValueObjects();
		searchURLs.multiAdd(getResources().getStringArray(R.array.journal_urls_id),
				getResources().getStringArray(R.array.journal_urls), false);
		
		setClusterCollection(jc, subjects, languages);
		ssData = new SearchJournalsResult(searchURLs, clusterCollection, jc, subjects, languages, searchResultList,pagesList);
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
	    aaPage = new PaginationItemAdapter(this, R.layout.pagination, pagesList);
	    paginationGridView.setAdapter(aaPage);	    
	    paginationGridView.setOnItemClickListener(new OnItemClickListener() {
		       @Override
			   public void onItemClick(AdapterView<?> _av, View _v, int _index, long arg3) {		           
		           selectedPageIndex = _index;
		           header = header + " /" + pagesList.get(_index).getLabel()+ ":";
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
		return ssData.getURL(query, "",  this.filter, this.selectedPageIndex);
	}
	protected void loadAndDisplayData(String result){
		
		
		ssData.loadData(result);
		//searchResultCount = ssData.getResultCount();
		clusterCollection = ssData.getSearchClusterCollection();
		if (header.length()==0){
			header = ssData.getJournalsTotal();
		} else {
			header = header + ssData.getResultCount();
		}
		// ssData.getJournalsTotal();
		aa.notifyDataSetChanged();		
		aaPage.notifyDataSetChanged();
	}
}
