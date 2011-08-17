package org.scielo.search;

import java.util.ArrayList;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.Menu;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.TextView;

public class SearchActivity extends Activity {
	TextView textNav;
	TextView textSearch;
	
	View searchButton;	
	TextView headerTextView;
	ListView searchResultListView;
	
	protected Gallery gal;
	protected GridView paginationGridView;    
	
	
	protected ArrayList<Page> pagesList  = new ArrayList<Page>();	

	protected PaginationItemAdapter aaPage;  
	protected PageAdapter aaPageH;
	protected ArrayAdapter<?> arrayAdapter;
	
	
	protected Page page;	
	protected Searcher searcher;
	protected SearchService ss  = new SearchService();
	
	protected ClusterCollection clusterCollection;
	protected String[] clusterCodeOrder;
	
	protected String displayHeader = "";
	protected String displayQuery = "";	
	protected String displayFilterName = "";
	protected String displayResultTotal ="";
	protected String displayTotal = "";
	protected String displayLetter = "";
	
	protected int selectedPageIndex = 0;
	protected String query = "";
	protected String filter = "";

	private String filterSelectionTracker = "";	
	protected String query_id = "";
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    clusterCollection = new ClusterCollection();
	    
	    displayHeader = "";
	    query = "";
	    selectedPageIndex = -1;
	    
	    
	    setContentView(R.layout.search);
	    
		searchResultListView = (ListView) findViewById(R.id.list);
	    paginationGridView = (GridView) findViewById(R.id.paginationListView);
	    aaPage = new PaginationItemAdapter(this, R.layout.pagination, pagesList);
	    
	    
	    textNav = (TextView) findViewById(R.id.call_naveg);
    	textSearch = (TextView) findViewById(R.id.call_search);
		
    	textNav.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent docIntent = new Intent(v.getContext(), SearchJournalsActivity.class);
	            startActivity(docIntent);
			}
		});
    	
    	textSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent docIntent = new Intent(v.getContext(), SearchDocsActivity.class);		           
	            startActivity(docIntent);					
			}
		});
	    
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
	    setIntent(intent);
	    setQueryAndSearchAndDisplay(intent);
	}

	protected void setQueryAndSearchAndDisplay(Intent intent) {
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	      query = intent.getStringExtra(SearchManager.QUERY);
	    } else {
	    	query = query_id;
	    }	    
	    searchAndDisplay();
	}
	
	protected void addFilter(String _filter, String caption){
		String prefix = _filter.substring(0, 4);
		
		if (this.filterSelectionTracker.length()>0){			
			if (this.filterSelectionTracker.contains(prefix)){
				this.filterSelectionTracker = _filter;
				this.filter = _filter;
		    	displayFilterName = "/" + caption + ":";

			} else {
				this.filterSelectionTracker = this.filterSelectionTracker + "|" + _filter;
				this.filter = this.filter + " AND " + _filter;				
		    	displayFilterName = displayFilterName + "/" + caption + ":";
			}
		} else {
			this.filter = _filter;
			this.filterSelectionTracker = _filter;
	    	displayFilterName = "/" +  caption + ":";
		}			
		
    	this.selectedPageIndex = -1;
    	displayLetter = "";

	}
	@Override
    public boolean onPrepareOptionsMenu(Menu menu) {
    	
    	MenuItem menuItem;
    	//MenuItem subMenuItem;
        SubMenu subMenu;
        Cluster cluster;
        SearchFilter sf;
        int i;
        int clusterIndex = 0;
        //menu.clear();
        //this.mymenu = menu;
       
        
        for (int index=0; index < menu.size(); index++){
    		menuItem = menu.getItem(index);
    		subMenu = menuItem.getSubMenu();
    		
    		if (subMenu != null) {
	    		subMenu.clear();
	    		
	    		if (clusterCollection.getCount()>0){
		    		cluster = clusterCollection.getItemById(clusterCodeOrder[clusterIndex]);
		    		if (cluster!=null){
		        		for (i=0;i<cluster.getFilterCount();i++){
		        			sf = cluster.getFilterByIndex(i);
		        			subMenu.add(menuItem.getItemId(), sf.getSubmenuId(), i, sf.displayCaptionAndCount());
		        			//teste = subMenuItem.getItemId();
		        	    } 
		    		}
		    		clusterIndex ++;
	    		}
    		}
    	}
        
    	return true;
    }


	@Override
    public boolean onOptionsItemSelected(MenuItem item) {    
        super.onOptionsItemSelected(item);
        int itemSelected;
        
        String teste2 = "";
        itemSelected = item.getItemId();
        switch (itemSelected) {
	        case R.id.search:
	            onSearchRequested();
	            return true;
	        case R.id.reset:
	        	this.filterSelectionTracker = "";
	        	this.filter = "";
				
		    	displayFilterName = "";
		    	searchAndDisplay();
	            return true;
	        default:
	        	if (!item.hasSubMenu()){
	        		
		    	    SearchFilter sf = clusterCollection.getFilterBySubmenuId(itemSelected, teste2);
		            if (sf != null) {
		            	teste2 = sf.getFilterExpression();
		            	addFilter(teste2 , sf.getCaption());
		            	searchAndDisplay();	            	
		            }	        	
		            
	        	}
	            return true;	          	          
        }
        
    }

	protected String getURL(){
		return "";
	}
	
	public void searchAndDisplay() {
		
		String result;
		
		String url = getURL();
		result = ss.call(url);
		
		if (result.length()>0){
			loadAndDisplayResult(result);
		}
			if (query.length()>0){
				displayQuery = query + ":";
			}
			if (displayTotal.length()==0){
				displayTotal = displayResultTotal;
			}
			
			displayHeader = displayQuery + displayTotal;
			
			if (displayFilterName.length()>0) {
				displayHeader = displayHeader + displayFilterName + displayResultTotal;
				displayFilterName = displayFilterName + displayResultTotal;	
			}
			if (displayLetter.length()>0) {
				displayHeader = displayHeader + displayLetter + displayResultTotal;
			}
			
			headerTextView = (TextView) findViewById(R.id.TextViewHeader);
			headerTextView.setText(displayHeader);
	}	

	
	protected void loadAndDisplayResult(String result){
		searcher.genLoadData(result);
		ClusterCollection cCollection = searcher.getSearchClusterCollection();		
		
		if (cCollection!=null){
			clusterCollection = cCollection;
		}
		displayTotal = searcher.getQtdTotal();
		displayResultTotal = searcher.getResultTotal();
		arrayAdapter.notifyDataSetChanged();		
		aaPage.notifyDataSetChanged();
	}
	
}
