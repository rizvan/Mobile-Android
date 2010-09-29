package org.scielo.search;

//import android.R;
import android.app.Activity;
import android.os.Bundle;

//import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
//import android.widget.EditText;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

//import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URLEncoder;

import java.net.URL;
import java.util.ArrayList;

//import org.json.JSONArray;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import org.json.JSONException;
//import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;


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



public class Search extends Activity {
	private static final String TAG = "Search";
	TextView resultCountTextView; 
	EditText searchExpressionEditText;
	View searchButton;	
	ListView searchResultListView;
    String searchResultCount;
    String currSearchExpr = "";
	ArrayAdapter<SearchResult> aa;
	ArrayList<SearchResult> searchResultList = new ArrayList<SearchResult>();
	SearchFilterGroupList searchFilterGroupList = new SearchFilterGroupList( new ArrayList<SearchFilterGroup>(), new ArrayList<SearchFilter>());
	      
	static final private int SEARCH_RESULT_DIALOG = 1;
	SearchResult selectedSearchResult;

	@Override
	public void onCreate(Bundle icicle) {
	    super.onCreate(icicle);
	    setContentView(R.layout.main);

	    resultCountTextView = (TextView) findViewById(R.id.TextViewDocumentCount);
	    searchExpressionEditText = (EditText) findViewById(R.id.searchExpressionEditText);
	    searchButton = (View) findViewById(R.id.searchButton);
	    searchResultListView = (ListView) findViewById(R.id.searchResultListView);
	    
	    int resID = R.layout.result_list_item;
	    aa = new ResultItemAdapter(this, resID, searchResultList);
	    searchResultListView.setAdapter(aa);
	    
	    searchResultListView.setOnItemClickListener(new OnItemClickListener() {
	       @Override
		   public void onItemClick(AdapterView<?> _av, View _v, int _index, long arg3) {
	           selectedSearchResult = searchResultList.get(_index);
	           showDialog(SEARCH_RESULT_DIALOG);
	       }
	    });
	    searchButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				refreshSearchs("");
				
			}
	    });
	    searchExpressionEditText.setOnKeyListener(new OnKeyListener(){
	    	
	    	public boolean onKey(View v, int keyCode, KeyEvent event){
	    			if (keyCode == KeyEvent.KEYCODE_ENTER){
	    				refreshSearchs("");
	    				return true;
	    			}
	    		return false;
	    	}
	    });
	    
	    /*
	     int layoutID = android.R.layout.simple_list_item_1;
	     
	    aa = new ArrayAdapter<SearchResult>(this, layoutID , searchResultList);
	    searchResultListView.setAdapter(aa);
	     */
	    
	    	    
	    
	    //resultCountTextView.setText("1113");
	    refreshSearchs("");	
	}
	  
	  
	private void refreshSearchs(String filter) {
		//fixme String result = translate.getResources().getString(R.string.translation_error);
		HttpURLConnection con = null;
		String query = "";
		String result = "";
		String u = "";
		SearchResult r ;
		String searchExpression = this.searchExpressionEditText.getText().toString().trim();
		
		//Log.d(TAG, "refreshSearchs(" + searchExpression +  ")");
		
		try {
			// Check if task has been interrupted
			if (Thread.interrupted())
				throw new InterruptedException();
			
			u = this.getResources().getString(
					R.string.search_feed);
			u =	u.replace("amp;", "" );
			
			if (searchExpression.length()>0){
				query = query + "&q=" + URLEncoder.encode(searchExpression, "UTF-8");
			}
			if (filter.length()>0){
				query = query + "&fq=" + URLEncoder.encode(filter,  "UTF-8");
			}	
					
			
			URL url = new URL(u + query);
						
			con = (HttpURLConnection) url.openConnection();
			con.setReadTimeout(10000); /* milliseconds */ 
			con.setConnectTimeout(15000); /* milliseconds */ 
			con.setRequestMethod("GET");
			//con.addRequestProperty("Referer", "http://www.pragprog.com/titles/eband3/hello-android");
			con.setDoInput(true);
			
			// Start the query
			con.connect();
			
			// Check if task has been interrupted
			if (Thread.interrupted())
				throw new InterruptedException();
			
			
			// Read results from the query
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(con.getInputStream(), "UTF-8"));
			String payload = reader.readLine();
			reader.close();
			
			// Parse to get translated text
			JSONObject jsonObject = new JSONObject(payload);
			
			
			searchResultCount = jsonObject.getJSONArray("diaServerResponse").getJSONObject(0).getJSONObject("response").get("numFound").toString();
			resultCountTextView.setText(searchResultCount);
			
			JSONObject resultItem ;
			
			int resultCount = jsonObject.getJSONArray("diaServerResponse" ).getJSONObject(0).getJSONObject("response").getJSONArray("docs").length();
			searchResultList.clear();
			for (int i=0; i<resultCount; i++){
				r = new SearchResult();
				
				resultItem = jsonObject.getJSONArray("diaServerResponse").getJSONObject(0).getJSONObject("response").getJSONArray("docs").getJSONObject(i );
				r.setDocumentTitle( new Integer(i+1).toString()+ "\n" + resultItem.getJSONArray("ti").getString(0));
				result = "";
				for (int j=0; j<resultItem.getJSONArray("au").length(); j++) {
					result = result + resultItem.getJSONArray("au").getString(j) + "; ";
				}
				r.setDocumentAuthors(result);
				r.setDocumentPDFLink("http://teste.scielo.br");
				addNewSearchResult(r);				
			}
			try {
				resultItem = jsonObject.getJSONArray( "diaServerResponse").getJSONObject(0).getJSONObject("facet_counts");
				resultItem = resultItem.getJSONObject("facet_fields");
				
				populateSearchFilterLists(resultItem);
			} catch(JSONException e){
				Log.d(TAG, "JSONException", e);				
			}
						
			
				
			
			// Check if task has been interrupted
			//if (Thread.interrupted())
			//	throw new InterruptedException();
		
		} catch (IOException e) {
			Log.e(TAG, "IOException", e);
		} catch (JSONException e) {
			Log.e(TAG, "JSONException", e);
		} catch (InterruptedException e) {
			Log.d(TAG, "InterruptedException", e);
			result = this.getResources().getString(
					R.string.search_interrupted);
		} finally {
			if (con != null) {
				con.disconnect();
			}	}
		
		// All done
		Log.d(TAG, " -> returned " + result);
		
		
	}
    private void addNewSearchResult(SearchResult _searchResult) {
	  // Add the new quake to our list of searchResultList.
	  searchResultList.add(_searchResult);
	  // Notify the array adapter of a change.
	  aa.notifyDataSetChanged();
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
      
      for (int index = 0; index < searchFilterGroupList.length(); index++){
    	  populateMenu(menu, searchFilterGroupList.getItem(index).getMenuId(), index);  
      }
      
      return true;
    }
    
    private void populateMenu(Menu menu, int id, int index){
    	MenuItem menuItem;
        SubMenu subMenu;
        SearchFilterGroup searchFilterGroup;
        
        menuItem = menu.findItem(id);        
        subMenu = menuItem.getSubMenu();
        searchFilterGroup = searchFilterGroupList.getItem(index);
        subMenu.clear();
        for (int i=0;i<searchFilterGroup.length();i++){
          subMenu.add(id, (index * 1000) + searchFilterGroup.getItem(i).getId(), i, searchFilterGroup.getItem(i).getName()+ "(" + searchFilterGroup.getItem(i).getCount() + ")" );
        }        
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	String qualifier = "";
    	String filter = "";
        
      super.onOptionsItemSelected(item);
      /*switch(item.getItemId()) {
      case (R.id.menuItemRefineBySubjectHumanSciences): {
          qualifier = "ac:";
          filter = '"' + "Human Sciences" + '"' ; 
          break; 
        }
      case (R.id.menuItemRefineBySubjectHealthSciences): {
          qualifier = "ac:";
          filter = '"' + "Health Sciences" + '"' ; 
          break; 
        }
      case (R.id.menuItemRefineBySubjectBiologicalSciences): {
          qualifier = "ac:";
          filter = '"' + "Biological Sciences" + '"' ; 
          break; 
        }
      case (R.id.menuItemRefineBySubjectAgriculturalSciences): {
          qualifier = "ac:";
          filter = '"' + "Agricultural Sciences" + '"' ; 
          break; 
        }
      case (R.id.menuItemRefineBySubjectExactAndEarthSciences): {
          qualifier = "ac:";
          filter = '"' + "Exact And Earth Sciences" + '"' ; 
          break; 
        }
      case (R.id.menuItemRefineBySubjectEngineering): {
          qualifier = "ac:";
          filter = '"' + "Engineering" + '"' ; 
          break; 
        }
      }*/
      qualifier = "ac:";
      filter = '"' + "Agricultural Sciences" + '"' ;
      refreshSearchs(qualifier + filter);
      return false;
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

    @Override
    public void onPrepareDialog(int id, Dialog dialog) {
      switch(id) {
        case (SEARCH_RESULT_DIALOG) :
          
          String searchResultText = selectedSearchResult.getText();

          AlertDialog searchResultDialog = (AlertDialog)dialog;
          searchResultDialog.setTitle(R.string.searchResultDialogTitle);
          TextView tv = (TextView)searchResultDialog.findViewById(R.id.searchResultDetailsTextView);
          tv.setText(searchResultText);

          break;
          
      }
    }
    
    private boolean populateSearchFilterLists(JSONObject filters){
    	boolean r = true;
    	
    	
    	searchFilterGroupList.clear();
    	
    	   
    	r = populateSearchFilterList(getSearchFilterList(filters, "ac", R.id.menuItemRefineBySubject));
    	r = populateSearchFilterList(getSearchFilterList(filters, "ta_cluster", R.id.menuItemRefineByJournal));
    	r = populateSearchFilterList(getSearchFilterList(filters, "year_cluster", R.id.menuItemRefineByYear));
    	r = populateSearchFilterList(getSearchFilterList(filters, "la", R.id.menuItemRefineByLanguage));
    	r = populateSearchFilterList(getSearchFilterList(filters, "in", R.id.menuItemRefineByCollection));
    	
    	return r;
    }
    
    private boolean populateSearchFilterList(SearchFilterGroup list){
    	boolean r = false;
    	if (list != null){
    		searchFilterGroupList.add(list);
    		r = true;
    	}
    	return r;
    }
    private SearchFilterGroup getSearchFilterList(JSONObject filterLists, String filterListName, int menuId){
    	SearchFilterGroup searchFilterList = new SearchFilterGroup( new ArrayList<SearchFilter>(), menuId, filterListName);
    	SearchFilter searchFilter;
    	int i;
    	JSONArray filterList;
    	JSONArray filter;
    	String filterName;
    	String filterResultCount;
    	
    	try {
            //f = filter.getJSONArray(filterName).to;
    		filterList = filterLists.getJSONArray(filterListName);
            
        	for (i=0;i<filterList.length();i++){
        		filter = filterList.getJSONArray(i);
        		filterName = filter.getString(0);
        		filterResultCount = filter.getString(1);
    			searchFilter = new SearchFilter( i,  filterName, filterResultCount);
        		searchFilterList.add(searchFilter);
        	}
        	            
        } catch (JSONException e) {
            
        }
        return searchFilterList;
    }

    
    
}
