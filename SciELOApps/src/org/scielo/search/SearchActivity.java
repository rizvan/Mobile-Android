package org.scielo.search;

import android.app.Activity;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
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

	protected int selectedMenuId;
	protected String header = "";
	protected String filter = "";
	protected int selectedPageIndex = 0;
	protected String query = "";
	protected String query_id = "";
	protected ClusterCollection clusterCollection;
	protected SearchService ss;
	protected String[] clusterCodeOrder;
	
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
	    header = query;
	    doSearch();
	}
	
    
	
	public void addFilter(String _filter){
		if (this.filter.length()>0){
			this.filter = this.filter + " AND " + _filter;
		} else {
			this.filter = _filter;
		}			
	}
	@Override
    public boolean onPrepareOptionsMenu(Menu menu) {
    	
    	MenuItem menuItem;
    	MenuItem subMenuItem;
        SubMenu subMenu;
        Cluster cluster;
        SearchFilter sf;
        int i;
        int teste;
    	//menu.clear();
        //this.mymenu = menu;
       
        for (int index=1; index < menu.size(); index++){
    		menuItem = menu.getItem(index);
    		subMenu = menuItem.getSubMenu();
    		subMenu.clear();
    		
    		if (clusterCollection.getCount()>0){
	    		cluster = clusterCollection.getItemById(clusterCodeOrder[index-1]);
	    		if (cluster!=null){
	        		for (i=0;i<cluster.getFilterCount();i++){
	        			sf = cluster.getFilterByIndex(i);
	        			subMenuItem = subMenu.add(menuItem.getItemId(), sf.getSubmenuId(), i, sf.getCaption() + " (" + sf.getResultCount() + ")" );
	        			teste = subMenuItem.getItemId();
	        	    } 
	    		}
    		}
    	}
        
    	return true;
    }


	@Override
    public boolean onOptionsItemSelected(MenuItem item) {    
        super.onOptionsItemSelected(item);
        int teste;
        String teste2 = "";
        switch (item.getItemId()) {
	        case R.id.search:
	            onSearchRequested();
	            return true;
	        default:
	        	if (!item.hasSubMenu()){
	        		teste = item.getItemId();
		    	    SearchFilter sf = clusterCollection.getFilterBySubmenuId(teste, teste2);
		            if (sf != null) {
		            	addFilter(sf.getFilterExpression());
		            	this.selectedPageIndex = 0;
		            	header = header + "/" + sf.getCaption();
		            	doSearch();	            	
		            }	        	
		            
	        	}
	            return true;	          	          
        }
    }
	protected void loadAndDisplayData(String result){
		
	}
	protected String getURL(){
		return "";
	}
	public void doSearch() {
		String queryurl;
		String result;
		queryurl = getURL();
		result = ss.call(queryurl);
		
		presentResult(result);
	}
	

	protected void presentResult(String result) {
		if (result.length()>0){
			loadAndDisplayData(result);
			//aaPage.notifyDataSetChanged();
			headerTextView = (TextView) findViewById(R.id.TextViewHeader);
			headerTextView.setText(header);
			
		}
	}	
	
}
