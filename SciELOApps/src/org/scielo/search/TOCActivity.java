package org.scielo.search;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;


public class TOCActivity extends SearchActivity {
	
	private String serviceURL = "";

	
	private String filter = "";
	
	//private String searchResultCount;
	//private String currSearchExpr = "";
    
	private SciELONetwork jc;

	private IdAndValueObjects languages;
	private IdAndValueObjects subjects;

	private Issue issue;
    private Document document;
    private ArrayAdapter<Document> aa;
    private ArrayList<Document> searchResultList =  new ArrayList<Document>();
	private TOCResult ssData;
	
	private String collectionId;
		
	GridView paginationGridView;    
	ArrayAdapter<Page> aaPage;    
	ArrayList<Page> pagesList  = new ArrayList<Page>();
	Page page;

	
	@Override	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.search);
	    
	    clusterCodeOrder = getResources().getStringArray(R.array.cluster_list_doc);
	    serviceURL = this.getResources().getString(R.string.search_feed_issuetoc);
	    

	    jc = new SciELONetwork();
		jc.multiAdd(
  		    getResources().getStringArray(R.array.collections_code),
			getResources().getStringArray(R.array.collections_name), 
			getResources().getStringArray(R.array.log_collections_code), 
			getResources().getStringArray(R.array.collections_url) );		
		subjects = new IdAndValueObjects();
		subjects.multiAdd(getResources().getStringArray(R.array.subjects_id),
				getResources().getStringArray(R.array.subjects_name),true);
		languages = new IdAndValueObjects();
		languages.multiAdd(getResources().getStringArray(R.array.languages_id),
				getResources().getStringArray(R.array.languages_name), false);
		
	    //serviceURL = serviceURL.replace("REPLACE_PID", document.getId());
		
		issue = new Issue();
		Journal j = new Journal();
		j.setCollection(getIntent().getStringExtra("collection"));
		j.setTitle(getIntent().getStringExtra("title"));
		
	    issue.setId(getIntent().getStringExtra("id"));
	    issue.setJournal(j);
	    issue.setNumber(getIntent().getStringExtra("n"));
	    issue.setSuppl(getIntent().getStringExtra("v"));
	    issue.setVolume(getIntent().getStringExtra("s"));
	    /*
	     * docIntent.putExtra("id", document.getId());
		           docIntent.putExtra("date", document.getDate());
		           docIntent.putExtra("v", document.getVolume());
		           docIntent.putExtra("n", document.getNumber());
		           docIntent.putExtra("s", document.getSuppl());
		           docIntent.putExtra("collection_id", document.getJournal().getCollectionId());
		           docIntent.putExtra("collection", document.getJournal().getCollection());
		           docIntent.putExtra("title", document.getJournal().getTitle());
		           s
	     * 
	     */
	    query_id = issue.getId();
	    collectionId = getIntent().getStringExtra("collection_id");
		
		ssData = new TOCResult(serviceURL, this.getResources().getString(R.string.pdf_url), jc, subjects, languages, searchResultList, pagesList);
		
		TextView header = (TextView) findViewById(R.id.TextViewHeader);
		header.setText(issue.getText());
		
		searchResultListView = (ListView) findViewById(R.id.list);
	    int resID = R.layout.list_item_doc;
	    aa = new DocumentAdapter(this, resID, searchResultList, true);
	    
	    searchResultListView.setAdapter(aa);	    
	    searchResultListView.setOnItemClickListener(new OnItemClickListener() {
		       @Override
			   public void onItemClick(AdapterView<?> _av, View _v, int _index, long id) {
		           document = searchResultList.get(_index);
		           //showDialog(SEARCH_RESULT_DIALOG);
		           
		           Intent docIntent = new Intent(_v.getContext(), DocumentActivity.class);
		           //docIntent.putExtra("DATA", selectedSearchResult);
		           String q = "id:" + '"' + "art-" + document.getDocumentId() + "^c" + document.getCollectionId() + '"';
		           docIntent.putExtra("query", q);
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
		           searchAndPresentResults();	
		       }
		    });

	    //oldOnCreate();
	    handleIntent(getIntent());
	}	
	
		
	protected String specGetURL(){
		//this.pagePosition = aaPage.getPageSelected();
		return ssData.getURL(query, "20", this.filter, this.selectedPageIndex, collectionId);
	}
	protected void specLoadAndDisplayData(String result){
		
		
		
		ssData.genLoadData(result);
		//pagesList = ssData.getPageList();
		clusterCollection = ssData.getSearchClusterCollection();
		specHeader = issue.getIssueLabel(true);
		aa.notifyDataSetChanged();		
		aaPage.notifyDataSetChanged();
	}
	
	
}
