package org.scielo.search;

import java.util.ArrayList;

import android.app.Activity;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.TextView;
//import android.widget.EditText;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
import android.app.SearchManager;
import android.content.Intent;
//import android.view.KeyEvent;
import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.SubMenu;
//import android.view.SubMenu;
//import android.view.View.OnKeyListener;
import android.widget.ListView;

public class SearchActivity extends Activity {
	View searchButton;	
	TextView headerTextView;
	ListView searchResultListView;
	protected Gallery gal;

	protected GridView paginationGridView;    
	protected PaginationItemAdapter aaPage;  
	
	
	protected ArrayList<Page> pagesList  = new ArrayList<Page>();	
	
	
	protected int selectedMenuId;
	
	protected String filterSelectionTracker = "";
	protected String letterOrPageSelected = "";
	protected String filter = "";
	protected int selectedPageIndex = 0;
	protected String query = "";
	protected String query_id = "";
	protected ClusterCollection clusterCollection;
	protected SearchService ss  = new SearchService();
	protected String[] clusterCodeOrder;
	
	protected String specHeader = "";
	protected String specQuery = "";
	
	protected String specHeaderFilterName = "";
	protected String specResultCount ="";
	protected String specTotal = "";
	protected String specHeaderLetter = "";
	
	@Override
	protected void onNewIntent(Intent intent) {
	    setIntent(intent);
	    handleIntent(intent);
	}

	protected void handleIntent(Intent intent) {
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	      query = intent.getStringExtra(SearchManager.QUERY);
	    } else {
	    	query = query_id;
	    }
	    
	    searchAndPresentResults();
	}
	
    
	
	protected void addFilter(String _filter, String caption){
		String prefix = _filter.substring(0, 4);
		
		if (this.filterSelectionTracker.length()>0){			
			if (this.filterSelectionTracker.contains(prefix)){
				// reinicializa
				this.filterSelectionTracker = _filter;
				this.filter = _filter;
		    	specHeaderFilterName = "/" + caption + ":";

			} else {
				this.filterSelectionTracker = this.filterSelectionTracker + "|" + _filter;
				this.filter = this.filter + " AND " + _filter;				
		    	specHeaderFilterName = specHeaderFilterName + "/" + caption + ":";
			}
		} else {
			this.filter = _filter;
			this.filterSelectionTracker = _filter;
	    	specHeaderFilterName = "/" +  caption + ":";
		}			
		
    	this.selectedPageIndex = -1;
    	specHeaderLetter = "";

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
				this.filterSelectionTracker = "";
		    	specHeaderFilterName = "";
		    	searchAndPresentResults();
	            return true;
	        default:
	        	if (!item.hasSubMenu()){
	        		
		    	    SearchFilter sf = clusterCollection.getFilterBySubmenuId(itemSelected, teste2);
		            if (sf != null) {
		            	teste2 = sf.getFilterExpression();
		            	addFilter(teste2 , sf.getCaption());
		            	searchAndPresentResults();	            	
		            }	        	
		            
	        	}
	            return true;	          	          
        }
        
    }
	protected void specLoadAndDisplayData(String result){
		
	}
	protected String specGetURL(){
		return "";
	}
	protected String specGetAcumHeader(){
		return "";
	}
	public void searchAndPresentResults() {
		String queryurl;
		String result;
		
		queryurl = specGetURL();
		result = ss.call(queryurl);
		
		if (result.length()>0){
			specLoadAndDisplayData(result);
			//aaPage.notifyDataSetChanged();
			
			
			if (query.length()>0){
				specQuery = query + ":";
			}
			if (specTotal.length()==0){
				specTotal = specResultCount;
			}
			
			specHeader = specQuery + specTotal;
			
			if (specHeaderFilterName.length()>0) {
				specHeader = specHeader + specHeaderFilterName + specResultCount;
				specHeaderFilterName = specHeaderFilterName + specResultCount;	
			}
			if (specHeaderLetter.length()>0) {
				specHeader = specHeader + specHeaderLetter + specResultCount;
			}
			
			headerTextView = (TextView) findViewById(R.id.TextViewHeader);
			headerTextView.setText(specHeader);
			
		}
	}	

	protected void setClusterCollection(JournalsCollectionsNetwork jc, IdAndValueObjects subjects, IdAndValueObjects languages, IdAndValueObjects letters){
		
		clusterCollection = new ClusterCollection();
		Cluster cluster;
		IdAndValueObjects idAndValueObjects;
		IdAndValue idAndValue;
		int subMenuId;
		SearchFilter searchFilter;
		int k;
		JournalsCollection c;
		for (int i=0;i<clusterCodeOrder.length;i++){
			idAndValueObjects = null;
			
			cluster = new Cluster(clusterCodeOrder[i]);
			if (clusterCodeOrder[i].equals("in")){
				for (k=0;k<jc.getCount() ;k++){        		
		    		c = jc.getItemByIndex(k);
		    		
		    		searchFilter = new SearchFilter(c.getName(), "0", c.getId(), cluster.getId() );
					subMenuId = k + (i * 100) ;
		    		searchFilter.setSubmenuId(subMenuId);
		    		cluster.addFilter(searchFilter, subMenuId, c.getId());        			
				}
				clusterCollection.add(cluster);
			} else {
				if (clusterCodeOrder[i].equals("ac")){
					idAndValueObjects = subjects;
				} else {
	    			if (clusterCodeOrder[i].equals("la")){
	    				idAndValueObjects = languages;
	    			} else {
	    				if (clusterCodeOrder[i].equals("le")){
		    				idAndValueObjects = letters;
		    			}
	    			}
	
				}
		    	for (k=0;k<idAndValueObjects.getCount();k++){        		
		    		idAndValue = idAndValueObjects.getItemByIndex(k);
		    		searchFilter = new SearchFilter(idAndValue.getValue(), "0",  idAndValue.getId(), cluster.getId() );
					subMenuId = k + (i * 100) ;
		    		searchFilter.setSubmenuId(subMenuId);
		    		cluster.addFilter(searchFilter, subMenuId, idAndValue.getId());        			
				}
				clusterCollection.add(cluster);
			}
		}
	}
}
