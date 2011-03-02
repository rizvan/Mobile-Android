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
		
		if (this.filter.length()>0){			
			if (this.filter.contains(prefix)){
				this.filter = _filter;
		    	specHeaderFilterName = "/" + caption + ":";

			} else {
				this.filter = this.filter + " AND " + _filter;
		    	specHeaderFilterName = specHeaderFilterName + "/" + caption + ":";

			}
		} else {
			this.filter = _filter;
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
			
			specHeader = specQuery + specTotal + specHeaderFilterName + specHeaderLetter ;
			if ((specHeaderFilterName.length()>0) || (specHeaderLetter.length()>0)){
				specHeader = specHeader + specResultCount;
			}
				
			
			
			if (specHeaderFilterName.length()>0){
				if (specHeaderLetter.length()==0){
					// it means specResultCount is related to the filter
					specHeaderFilterName = specHeaderFilterName + specResultCount;	
				}
			}
			
			
			
			headerTextView = (TextView) findViewById(R.id.TextViewHeader);
			headerTextView.setText(specHeader);
			
		}
	}	

	protected void setClusterCollection(SciELONetwork jc, IdAndValueObjects subjects, IdAndValueObjects languages){
		
		clusterCollection = new ClusterCollection();
		Cluster cluster;
		IdAndValueObjects idAndValueObjects;
		IdAndValue idAndValue;
		int subMenuId;
		SearchFilter searchFilter;
		int k;
		SciELOCollection c;
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
