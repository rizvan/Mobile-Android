package org.scielo.search;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;


import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;


import android.widget.AdapterView;


import android.widget.AdapterView.OnItemClickListener;


public class SearchDocsActivity extends SearchActivity {
	protected Document searched;
    protected ArrayList<Document> resultList =  new ArrayList<Document>();	
    
    protected ArticleURL articleURL;

	@Override	
	public void onCreate(Bundle savedInstanceState) {
		clusterCodeOrder = getResources().getStringArray(R.array.cluster_list_doc);
	    
		super.onCreate(savedInstanceState);
	    
	    articleURL = new ArticleURL(getResources().getString(R.string.pdf_and_log_url), getResources().getString(R.string.pdf_url), getResources().getString(R.string.article_url));
		searcher = new DocSearcher(this.getResources().getString(R.string.search_feed), clusterCollection, resultList, pagesList);
		
	    int resID = R.layout.list_item_doc;
	    arrayAdapter = new DocumentAdapter(this, resID, resultList,false);
	    
	    
	    searchResultListView.setAdapter(arrayAdapter);	    
	    searchResultListView.setOnItemClickListener(new OnItemClickListener() {
	       @Override
		   public void onItemClick(AdapterView<?> _av, View _v, int _index, long id) {
	           searched = resultList.get(_index);
	           //showDialog(SEARCH_RESULT_DIALOG);
	           
	           Intent docIntent = new Intent(_v.getContext(), DocumentActivity.class);
	           //docIntent.putExtra("DATA", selectedSearchResult);
	           docIntent.putExtra("query", "");
	           docIntent.putExtra("id", searched.getDocumentId());
	           docIntent.putExtra("title", searched.getDocumentTitle());
	           docIntent.putExtra("authors", searched.getDocumentAuthors());
	           
	           if (searched.getPdf_url().length()==0){
	        	   searched.setPdf_url(articleURL.returnPDFURL(searched));
	           }
	           if (searched.getHtml_url().length()==0){
	        	   searched.setHtml_url(articleURL.returnFullTextURL(searched));
	           }

	           
	           docIntent.putExtra("fulltext_url", searched.getHtml_url());
	           docIntent.putExtra("pdf_url", searched.getPdf_url());
	           docIntent.putExtra("collection", searched.getCol().getName());
	           docIntent.putExtra("abstract", searched.getDocumentAbstracts());
	           docIntent.putExtra("issue", searched.getIssueLabel());
	           startActivity(docIntent);
	           
               
	       }
		});
	    //aaPage = new ButtonPageAdapter(this, R.layout.pagination,  pagesList);
	    //aaPage = new PaginationItemAdapter(this, R.layout.pagination,  pagesList);
	    //aaPage.setList(pagesList);
	    paginationGridView.setAdapter(aaPage);
	    paginationGridView.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	        	//page = pagesList.get(position);
	        	//header = header + searcher.getResultCount();
		        selectedPageIndex = position;
		        searchAndDisplay();	
	        }
	    });

	    //btn.setOnClickListener(new MyOnClickListener(position)); 

	    //oldOnCreate();
	    //onSearchRequested();
	    setQueryAndSearchAndDisplay(getIntent());
		
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
		if (this.filter.length()==0){
			displayFilterName = "";
		}
		return ((DocSearcher) searcher).getURL(query, this.getResources().getString(R.string.search_doc_count), this.filter, this.selectedPageIndex);
	}
	/*
	protected void loadAndDisplayResult(String result){
		
		searcher.genLoadData(result);
		//pagesList = searcher.getPageList();
		clusterCollection = searcher.getSearchClusterCollection();
		
		displayResultTotal = searcher.getResultTotal();
		
		arrayAdapter.notifyDataSetChanged();		
		aaPage.notifyDataSetChanged();
	}*/
	
	

}
