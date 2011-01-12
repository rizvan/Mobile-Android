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

public class SearchDocsActivity extends SearchActivity {
	
	private String serviceURL = "";
	
	private SciELONetwork jc;
	private PairsList languages;
	private PairsList subjects;

    private Document document;
    private ArrayAdapter<Document> aa;
    private ArrayList<Document> searchResultList =  new ArrayList<Document>();	
	private SearchDocsResult ssData;

	protected GridView paginationGridView;    
	protected PaginationItemAdapter aaPage;    
	protected ArrayList<Page> pagesList  = new ArrayList<Page>();	
	protected Page page;
	
		
	@Override	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.search);
	    
	    clusterCodeOrder = getResources().getStringArray(R.array.cluster_list_doc);
	    serviceURL = this.getResources().getString(R.string.search_feed);
	    
		jc = new SciELONetwork(
  		    getResources().getStringArray(R.array.collections_code),
			getResources().getStringArray(R.array.collections_name), 
			getResources().getStringArray(R.array.log_collections_code), 
			getResources().getStringArray(R.array.collections_url) );		
		subjects = new PairsList(getResources().getStringArray(R.array.subjects_id),
				getResources().getStringArray(R.array.subjects_name));
		languages = new PairsList(getResources().getStringArray(R.array.languages_id),
				getResources().getStringArray(R.array.languages_name));
		
		ssData = new SearchDocsResult(serviceURL, this.getResources().getString(R.string.pdf_url) ,jc, subjects, languages, searchResultList, pagesList);
		ss = new SearchService();
		
		
		
		searchResultListView = (ListView) findViewById(R.id.list);

	    int resID = R.layout.list_item_doc;
	    aa = new DocumentAdapter(this, resID, searchResultList,false);
	    searchResultListView.setAdapter(aa);	    
	    searchResultListView.setOnItemClickListener(new OnItemClickListener() {
		       @Override
			   public void onItemClick(AdapterView<?> _av, View _v, int _index, long id) {
		           document = searchResultList.get(_index);
		           //showDialog(SEARCH_RESULT_DIALOG);
		           
		           Intent docIntent = new Intent(_v.getContext(), DocumentActivity.class);
		           //docIntent.putExtra("DATA", selectedSearchResult);
		           docIntent.putExtra("query", "");
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
	    //aaPage = new ButtonPageAdapter(this, R.layout.pagination,  pagesList);
	    aaPage = new PaginationItemAdapter(this, R.layout.pagination,  pagesList);
	    //aaPage.setList(pagesList);
	    paginationGridView.setAdapter(aaPage);
	    paginationGridView.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	        	page = pagesList.get(position);
		        pagePosition = page.getPosition( );
		        doSearch();	
	        }
	    });

	    //btn.setOnClickListener(new MyOnClickListener(position)); 

	    //oldOnCreate();
	    //onSearchRequested();
	    handleIntent(getIntent());
		
	}	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
      
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.menu, menu);
        
    	return true;
    }
	protected String getURL(){
		//this.pagePosition = aaPage.getPageSelected();
		return ssData.getURL(query, "20", this.filter, this.pagePosition);
	}
	protected void loadAndDisplayData(String result){
		
		ssData.loadData(result);
		//pagesList = ssData.getPageList();
		clusterCollection = ssData.getSearchClusterCollection();
		
		aa.notifyDataSetChanged();		
		aaPage.notifyDataSetChanged();
	}
	
	

}
