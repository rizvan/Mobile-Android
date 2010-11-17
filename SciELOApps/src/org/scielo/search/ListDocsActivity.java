package org.scielo.search;


import android.app.Activity;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
//import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

//import org.json.JSONArray;

//import org.json.JSONException;
//import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
//import android.view.SubMenu;
import android.view.View.OnKeyListener;

import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ListDocsActivity extends Activity {
	TextView resultCountTextView; 
	EditText searchExpressionEditText;
	View searchButton;	
	ListView searchResultListView;
	GridView paginationGridView;
    String searchResultCount;
    String currSearchExpr = "";
    ArrayAdapter<Document> aa;
    ArrayAdapter<Page> aaPage;
	
	ArrayList<Document> searchResultList = new ArrayList<Document>();
	ArrayList<Page> pagesList = new ArrayList<Page>();
	ClusterCollection searchClusterCollection = new ClusterCollection( new ArrayList<Cluster>());
	ArrayList<SearchFilter> searchFilterList;
	static final private int SEARCH_RESULT_DIALOG = 1;
	Document document;
	Page page;
	String filter;
	String[] col_c;
	String[] col_n; 
	String[] col_ln;
	String[] col_url;
	Menu mymenu;
	
	JournalCollections jc;
	
	@Override
	public void onCreate(Bundle icicle) {
	    super.onCreate(icicle);
	    setContentView(R.layout.list_docs);

	    col_c = getResources().getStringArray(R.array.collections_code);
		col_n = getResources().getStringArray(R.array.collections_name);
		col_ln = getResources().getStringArray(R.array.log_collections_code);
		col_url = getResources().getStringArray(R.array.collections_url);
		
		jc = new JournalCollections(col_c, col_n, col_ln, col_url );		
		

	    //resultCountTextView = (TextView) findViewById(R.id.TextViewDocumentCount);
	    searchExpressionEditText = (EditText) findViewById(R.id.searchExpressionEditText);
	    searchButton = (View) findViewById(R.id.searchButton);
	    searchResultListView = (ListView) findViewById(R.id.searchResultListView);
	    
	    int resID = R.layout.result_list_item;
	    
	    aa = new DocumentAdapter(this, resID, searchResultList);
	    searchResultListView.setAdapter(aa);	    
	    searchResultListView.setOnItemClickListener(new OnItemClickListener() {
	       @Override
		   public void onItemClick(AdapterView<?> _av, View _v, int _index, long id) {
	           document = searchResultList.get(_index);
	           //showDialog(SEARCH_RESULT_DIALOG);
	           
	           Intent docIntent = new Intent(_v.getContext(), DocumentActivity.class);
	           //docIntent.putExtra("DATA", selectedSearchResult);
	           docIntent.putExtra("id", document.getDocumentId());
	           docIntent.putExtra("title", document.getDocumentTitle());
	           docIntent.putExtra("authors", document.getDocumentAuthors());
	           docIntent.putExtra("pdf", document.getDocumentPDFLink());
	           docIntent.putExtra("collection", document.getDocumentCollection());
	           docIntent.putExtra("abstract", document.getDocumentAbstracts());
	           docIntent.putExtra("issue", document.getIssueLabel());
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
	           refreshSearchs(filter, page.getPosition());	
	       }
	    });

	    searchButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				refreshSearchs("", "");
				
			}
	    });
	    searchExpressionEditText.setOnKeyListener(new OnKeyListener(){
	    	
	    	public boolean onKey(View v, int keyCode, KeyEvent event){
	    			if (keyCode == KeyEvent.KEYCODE_ENTER){
	    				refreshSearchs("", "");
	    				return true;
	    			}
	    		return false;
	    	}
	    });
	    
	    refreshSearchs("", "");	
	}
	  
	
	private void refreshSearchs(String _filter, String pagePosition) {
		SearchServiceData ssData;
		String searchExpression;
		SearchService ss;
		String payload;
		
		ss = new SearchService(this.getResources().getString(R.string.search_feed));
		
		searchExpression = this.searchExpressionEditText.getText().toString().trim();
		this.filter = _filter;
		payload = ss.call(searchExpression, "20", _filter, pagePosition);
		if (payload.length()>0){
			ssData = new SearchServiceData(payload, this.getResources().getString(R.string.pdf_url));
			searchResultCount = ssData.getResultCount();
			//resultCountTextView.setText(searchResultCount + " ..." + new Integer(col_c.length).toString());
			//resultCountTextView.setText(searchResultCount + " ..." + jc.getCollectionAppName("scl"));
			ssData.loadResultList(searchResultList, jc, pagesList);
			
			//ssData.loadResultList(searchResultList);
			ssData.loadClusterCollection(searchClusterCollection);
	    	//this.onPrepareOptionsMenu(this.mymenu);
			
			aa.notifyDataSetChanged();
			aaPage.notifyDataSetChanged();
			
		}
	}
    
    
    //static final private int MENU_REFINE_BY_SUBJECT = Menu.FIRST;
    /*
    @Override
    public void onCreateContextMenu(ContextMenu menu, 
    		                           View v, 
    		                           ContextMenu.ContextMenuInfo menuInfo) {
      super.onCreateContextMenu(menu, v, menuInfo);
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu, menu);
      menu.setHeaderTitle("Context menu");
      
    }
    */
    
	  
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
      
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.menu, menu);
      
    	return true;
    }
	
	@Override
    public boolean onPrepareOptionsMenu(Menu menu) {
    	String[] clusterCodeOrder = { "ac", "ta_cluster", "year_cluster", "la", "in"};
    	MenuItem menuItem;
    	MenuItem subMenuItem;
        SubMenu subMenu;
        Cluster cluster;
        SearchFilter sf;
        
    	//menu.clear();
        //this.mymenu = menu;
    	for (int index=0; index < menu.size(); index++){
    		menuItem = menu.getItem(index);
    		subMenu = menuItem.getSubMenu();
    		subMenu.clear();
    		
    		cluster = searchClusterCollection.getClusterByCode(clusterCodeOrder[index]);
    		if (cluster!=null){
        		for (int i=0;i<cluster.getFilterCount();i++){
        			sf = cluster.getFilter(i);
        			subMenuItem = subMenu.add(menuItem.getItemId(),  sf.getSubmenuId(), i, sf.getCaption()); // + " (" + sf.getResultCount() + ")" );
        			sf.setSubmenuId(subMenuItem.getItemId());
        	    } 
    			
    		}
    	}
      
    	return true;
    }
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {    
      super.onOptionsItemSelected(item);
      
      SearchFilter sf = searchClusterCollection.getFilterById(item.getItemId());
      if (sf != null) refreshSearchs(sf.getFilterExpression(), "");
      
      return true;
    }
    
    @Override
    public Dialog onCreateDialog(int id) {
      switch(id) {
        case (SEARCH_RESULT_DIALOG) :
          LayoutInflater li = LayoutInflater.from(this);
          View searchResultDetailsView = li.inflate(R.layout.search_result_details, null);

          AlertDialog.Builder searchResultDialog = new AlertDialog.Builder(this);
          searchResultDialog.setTitle(R.string.search_result_details_title);
          searchResultDialog.setView(searchResultDetailsView);
          return searchResultDialog.create();
      }
      return null;
    }
    
    /*
     @Override(non-Javadoc)
     @see android.app.Activity#onPrepareDialog(int, android.app.Dialog)
     
    
    public void onPrepareDialog(int id, Dialog dialog) {
      switch(id) {
        case (SEARCH_RESULT_DIALOG) :
          
          String searchResultText = document.getText();

          AlertDialog searchResultDialog = (AlertDialog)dialog;
          searchResultDialog.setTitle(R.string.searchResultDialogTitle);
          TextView tv = (TextView)searchResultDialog.findViewById(R.id.searchResultDetailsTextView);
          tv.setText(searchResultText);

          break;
          
      }
    }
      */
    
}
