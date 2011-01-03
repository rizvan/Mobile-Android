package org.scielo.search;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;




public class SearchJournalsActivity extends SearchActivity {
	
	private String serviceURL = "";

	private ClusterCollection searchClusterCollection ;
	private String[] clusterCodeOrder;
	
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
	
	private SearchService ss;
	
			
	
	@Override	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.search);
	    
	    clusterCodeOrder = getResources().getStringArray(R.array.cluster_list);
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
	    aaPage = new PaginationItemAdapter(this, R.layout.pagination, pagesList);
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
	    onSearchRequested();
	    handleIntent(getIntent());
	}	
	
	@Override
    public boolean onPrepareOptionsMenu(Menu menu) {
    	
    	MenuItem menuItem;
    	MenuItem subMenuItem;
        SubMenu subMenu;
        Cluster cluster;
        SearchFilter sf;
        
    	//menu.clear();
        //this.mymenu = menu;
    	for (int index=1; index < menu.size(); index++){
    		menuItem = menu.getItem(index);
    		subMenu = menuItem.getSubMenu();
    		subMenu.clear();
    		
    		cluster = this.searchClusterCollection.getItemById(this.clusterCodeOrder[index-1]);
    		if (cluster!=null){
        		for (int i=0;i<cluster.getFilterCount();i++){
        			sf = cluster.getFilterByIndex(i);
        			subMenuItem = subMenu.add(menuItem.getItemId(),  sf.getSubmenuId(), i, sf.getCaption() + " (" + sf.getResultCount() + ")" ); 
        			sf.setSubmenuId(subMenuItem.getItemId());
        			
        	    } 
    			
    		}
    	}
        
    	return true;
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {    
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
	        case R.id.search:
	            onSearchRequested();
	            return true;
	        default:
	    	    SearchFilter sf = searchClusterCollection.getFilterBySubmenuId(item.getItemId());
	            if (sf != null) {
	            	addFilter(sf.getFilterExpression());
	            	this.pagePosition = "";
	            	doSearch();
	            	
	            }
	            return true;	          	          
        }
    }
	
	@Override
	public void doSearch() {
		String queryurl;
		String result;
		queryurl = ssData.getURL(query, "20", this.filter, this.pagePosition);
		result = ss.call(queryurl);
		treatSearchResult(result);
	}
	

	private void treatSearchResult(String result) {
		if (result.length()>0){
			pagesList = ssData.getPageList();
			
			ssData.loadData(result);
			//searchResultCount = ssData.getResultCount();
			searchClusterCollection = ssData.getSearchClusterCollection();
			
			aa.notifyDataSetChanged();
			aaPage.notifyDataSetChanged();
		}
	}
	
}
