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
	
	private JournalsCollectionsNetwork jcn;
	private IdAndValueObjects languages;
	private IdAndValueObjects subjects;

    private Document document;
    
    private ArticleURL articleURL;
    
    private ArrayAdapter<Document> aa;
    private ArrayList<Document> searchResultList =  new ArrayList<Document>();	
	private SearchDocsResult ssData;

	@Override	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.search);
	    
	    clusterCodeOrder = getResources().getStringArray(R.array.cluster_list_doc);
	    serviceURL = this.getResources().getString(R.string.search_feed);
	    
	    articleURL = new ArticleURL(getResources().getString(R.string.pdf_and_log_url), getResources().getString(R.string.pdf_url), getResources().getString(R.string.article_url));
	    
		jcn = new JournalsCollectionsNetwork();
		jcn.multiAdd(
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
		
		//setClusterCollection(jc, subjects, languages);
		clusterCollection = new ClusterCollection();
		ssData = new SearchDocsResult(serviceURL, clusterCollection, jcn, subjects, languages, searchResultList, pagesList);
		
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
	           
	           if (document.getPdf_url().length()==0){
	        	   document.setPdf_url(articleURL.returnPDFURL(document));
	           }
	           if (document.getHtml_url().length()==0){
	        	   document.setHtml_url(articleURL.returnFullTextURL(document));
	           }

	           
	           docIntent.putExtra("fulltext_url", document.getHtml_url());
	           docIntent.putExtra("pdf_url", document.getPdf_url());
	           docIntent.putExtra("collection", document.getCol().getName());
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
	        	//page = pagesList.get(position);
	        	//header = header + ssData.getResultCount();
		        selectedPageIndex = position;
		        searchAndPresentResults();	
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
	protected String specGetURL(){
		//this.pagePosition = aaPage.getPageSelected();
		if (this.filter.length()==0){
			specHeaderFilterName = "";
		}
		return ssData.getURL(query, this.getResources().getString(R.string.search_doc_count), this.filter, this.selectedPageIndex);
	}
	protected void specLoadAndDisplayData(String result){
		
		ssData.genLoadData(result);
		//pagesList = ssData.getPageList();
		clusterCollection = ssData.getSearchClusterCollection();
		
		specResultCount = ssData.getResultCount();
		
		aa.notifyDataSetChanged();		
		aaPage.notifyDataSetChanged();
	}
	protected String specGetAcumHeader(){
		return ssData.getAcumHeader();
	}
	

}
