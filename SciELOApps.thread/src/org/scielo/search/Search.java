package org.scielo.search;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsSpinner;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;


public class Search extends Activity {
    protected static final int DATA_JOURNAL = 1;


	/** Called when the activity is first created. */
	private Spinner fromSpinner;
	

	private Handler guiThread;
	private ExecutorService transThread;
	private Runnable updateTask;
	private Future<?> transPending;
	
	private String url="";
	private String query_id="";
	
	private final int DATA_DOC = 0;
	private int dataType=0;
	
	private ClusterCollection clusterCollection = new ClusterCollection();
	private ArrayList<Document> docResulList = new ArrayList<Document>();
	private ArrayList<Page> pagesList = new ArrayList<Page>();
	private String[] clusterCodeOrder = null;
	private ArticleURL articleURL = null ;
	
	
	private TextView textNav;
	private TextView textSearch;
	private TextView textNavLetter;
	private TextView textSearchLetter;
	private TextView banner;
	private TextView headerText;
	private GridView paginationGridView;
	private ListView resultListView;
	
	private DocumentAdapter listItemAdapter;
	private JournalAdapter journalAdapter;
	private PaginationItemAdapter pageAdapter;
	private OnItemClickListener listItemListener;
	private OnItemClickListener pageListener;
	
	private int layout_menu_id;
	
	private boolean header_update = false;
	private String header_text = "";
	private String header_sep = "";
	private String header_filter = "";
	private String header_letter = "";
	private String header_slash = "";

	private String filterSelectionTracker="";


	private String param_start = "0";
	private String param_words="";
	private String param_filter = "";


	private String ws_docs_total = "";
	private String ws_query= "";
	private String ws_result_total= "";

	IdAndValueObjects journal_ws_urls= new IdAndValueObjects();


	private List<Journal> journalList;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int layout_id = R.layout.search_articles;
        layout_menu_id=0;
        
        clusterCollection = new ClusterCollection();
	    
	    header_text = "";
	    param_words = "";
	    
	    dataType = getIntent().getIntExtra("data_type", DATA_DOC);
        switch(dataType){
        case DATA_DOC:
        	clusterCodeOrder = getResources().getStringArray(R.array.cluster_list_doc);
            header_sep = ":";
            header_slash = "/";
    	    articleURL = new ArticleURL(getResources().getString(R.string.pdf_and_log_url), getResources().getString(R.string.pdf_url), getResources().getString(R.string.article_url));
    	    layout_id = R.layout.search_articles;
    	    layout_menu_id = R.menu.menu;
    	    listItemAdapter = new DocumentAdapter(this, R.layout.list_item_doc, docResulList,false);
        	pageAdapter = new PaginationItemAdapter(this, R.layout.pagination, pagesList);
    	    break;
        case DATA_JOURNAL:
        	clusterCodeOrder = getResources().getStringArray(R.array.cluster_list_journal);
            header_sep = ":";
            header_slash = "/";
            journal_ws_urls.multiAdd(getResources().getStringArray(R.array.journal_urls_id),
    				getResources().getStringArray(R.array.journal_urls), false);
    		            layout_id = R.layout.search_journals;
    	    layout_menu_id = R.menu.menu_journals;
    	    
    	    journalAdapter = new JournalAdapter(this, R.layout.list_item_journal, journalList);
    	    break;
        }
        
        
        setContentView(layout_id);
        initThreading();
        
        findViews();
        setAdapters();
        setListeners();
        setQuery(getIntent());

	    queueUpdate(1000);
	    
	    /*
	     * paginas
	     * menu artigos
	     * header
	     * journals
	     * menu journals 
	     * issues
	     * sumario
	     * 
	     * 
	     */
        
    }
    
    private void findViews(){
    	banner = (TextView) findViewById(R.id.banner);
    	textNav = (TextView) findViewById(R.id.call_naveg);
    	textSearch = (TextView) findViewById(R.id.call_search);
	    textNavLetter = (TextView) findViewById(R.id.call_naveg_letter);
    	textSearchLetter = (TextView) findViewById(R.id.call_search_letter);
		headerText = (TextView) findViewById(R.id.TextViewHeader);
		paginationGridView = (GridView) findViewById(R.id.paginationListView);
	    resultListView = (ListView) findViewById(R.id.list);
	}
    
    private void setAdapters(){
    	if (listItemAdapter != null){
    		resultListView.setAdapter(listItemAdapter);        		
    	}
    	if (pageAdapter!=null){
	    	paginationGridView.setAdapter(pageAdapter);
    	}
    }
    
    private void setListeners() {
    	banner.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent docIntent = new Intent(v.getContext(), HomeActivity.class);		           
		        startActivity(docIntent);
			}
		});
    
    	OnClickListener a = new OnClickListener() {
			@Override
			public void onClick(View v) {				
				if (SciELOAppsActivity.currentSearchMainActivity == "articles"){
					queueUpdate(1000);
				} else {
					dataType = DATA_DOC;
					SciELOAppsActivity.currentSearchMainActivity = "articles";
					Intent docIntent = new Intent(v.getContext(), Search.class);
					docIntent.putExtra("data_type", dataType);
		            startActivity(docIntent);
				}				
			}
		};
    	OnClickListener j = new OnClickListener() {
			@Override
			public void onClick(View v) {				
				if (SciELOAppsActivity.currentSearchMainActivity == "journals"){
					queueUpdate(1000);
				} else {
					dataType = DATA_JOURNAL;
					SciELOAppsActivity.currentSearchMainActivity = "journals";
					Intent docIntent = new Intent(v.getContext(), Search.class);
					docIntent.putExtra("data_type", dataType);
		            startActivity(docIntent);
				}				
			}
		};
    	textSearchLetter.setOnClickListener(a);
    	textSearch.setOnClickListener(a);
    	textNavLetter.setOnClickListener(j);
    	textNav.setOnClickListener(j);
	
    	listItemListener = new OnItemClickListener() {
 	       @Override
 		   public void onItemClick(AdapterView<?> _av, View _v, int _index, long id) {
 	           Document searched = docResulList.get(_index);
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
 		};
 		pageListener = new OnItemClickListener() {
	        
 			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	        	Page page = pagesList.get(position);
	        	param_start = page.getIndex2search();
	        	header_update = false;
	        	queueUpdate(1000);
	        }
	    };
    	resultListView.setOnItemClickListener(listItemListener);
	    paginationGridView.setOnItemClickListener(pageListener);

    }
    @Override
	protected void onNewIntent(Intent intent) {
	    setIntent(intent);
	    setQuery(intent);
	}

	protected void setQuery(Intent intent) {
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	      param_words = intent.getStringExtra(SearchManager.QUERY);
	      ws_query = param_words;
	      header_filter = "";
	      param_filter = "";
	    } else {
	    	param_words = query_id;
	    }	    
	    
	}
	

    private void initThreading() {
    	guiThread = new Handler();
    	transThread = Executors.newSingleThreadExecutor();
    	// This task does a translation and updates the screen
    	updateTask = new Runnable() {
    		

			public void run() {
    		    // Get text to translate
    			//String q = origText.getText().toString().trim();
    			   
		        // Cancel prvious translation if there was one
		        if (transPending != null)
			        transPending.cancel(true);
			    // Take care of the easy case
			    
				    	// Let user know we re doing something
			    	headerText.setText("...");
			    	
			    	// Begin translation now but do not wait for it
			    	try {
			    		setUrl();
			    		headerText.setText(url);
			    		SearchTask translateTask = new SearchTask(
			    				Search.this, // reference to activity
			    			url);
			    		transPending = transThread.submit(translateTask);
			    	} catch (RejectedExecutionException e) {
			    		// Unable to start new task
			    		headerText.setText("error");
			    	}
			    
    		}    		
    	};
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	if (layout_menu_id>0){
    		super.onCreateOptionsMenu(menu);      
    		MenuInflater inflater = getMenuInflater();
    		inflater.inflate(layout_menu_id, menu);
    	}
    	return true;
    }
	@Override
    public boolean onPrepareOptionsMenu(Menu menu) {
    	MenuItem menuItem;
        SubMenu subMenu;
        Cluster cluster;
        SearchFilter sf;
        JournalsCollectionsNetwork jcn = null;
        IdAndValueObjects list = null;
        if (layout_menu_id>0){
        	int i;
            int clusterIndex = 0;
            
            
            for (int index=0; index < menu.size(); index++){
        		menuItem = menu.getItem(index);
        		subMenu = menuItem.getSubMenu();        		
        		if (subMenu != null) {
    	    		subMenu.clear();    	    		
    	    		if (clusterCollection.getCount()>0){
    		    		cluster = clusterCollection.getItemById(clusterCodeOrder[clusterIndex]);
    		    		if (cluster!=null){
    		    			if (clusterCodeOrder[clusterIndex].equals("in")){
	    		    			jcn = (JournalsCollectionsNetwork)SciELOAppsActivity.myConfig.getJcn();
	    		    			for (i=0;i<cluster.getFilterCount();i++){
	    		        			sf = cluster.getFilterByIndex(i);
	    		        			sf.setCaption(jcn.getItem(sf.getCode()).getName());
	    		        			subMenu.add(menuItem.getItemId(), sf.getSubmenuId(), i, sf.displayCaptionAndCount());
	    		        		}
	    		    			jcn = null;
	    		        	} else {
	    		        		if (clusterCodeOrder[clusterIndex].equals("la")){
	    		        			list = (IdAndValueObjects) SciELOAppsActivity.myConfig.getLanguages();
	    		        		} else {
	    		        			if (clusterCodeOrder[clusterIndex].equals("ac")){
	    		        				list = (IdAndValueObjects) SciELOAppsActivity.myConfig.getSubjects() ;
	    		        			} else {
	    		        				list = null;
	    		        			}		        				
	    		        		}	        				
	    		        		if (list ==null){
	        		    			for (i=0;i<cluster.getFilterCount();i++){
	        		        			sf = cluster.getFilterByIndex(i);
	        		        			sf.setCaption(sf.getCode());
	        		        			subMenu.add(menuItem.getItemId(), sf.getSubmenuId(), i, sf.displayCaptionAndCount());	        	
	        		        	    }	
	    		        		} else {
	        		    			for (i=0;i<cluster.getFilterCount();i++){
	        		        			sf = cluster.getFilterByIndex(i);
	        		        			sf.setCaption(list.getItem(sf.getCode()).getValue());
	        		        			subMenu.add(menuItem.getItemId(), sf.getSubmenuId(), i, sf.displayCaptionAndCount());	        	
	        		        	    }	

	        		    			list = null;
	        		    		}
	        		    	}    		    		
    		    		}
    		    		clusterIndex ++;
    	    		}
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
        String teste3 = "";
        itemSelected = item.getItemId();
        switch (itemSelected) {
	        case R.id.search:
	            onSearchRequested();
	            return true;
	        case R.id.reset:
	        	//this.filterSelectionTracker = "";
	        	param_filter = "";
				header_filter = "";
				
		    	queueUpdate(1000);
	            return true;
	        default:
	        	if (!item.hasSubMenu()){
	        	    SearchFilter sf = clusterCollection.getFilterBySubmenuId(itemSelected, teste2);
		            if (sf != null) {
		            	teste2 = sf.getFilterExpression();
		            	teste3 = sf.getCaption();
		            	addFilter(teste2 , teste3, header_sep);
		            	this.queueUpdate(1000);    	
		            }	        	
		        }
	            return true;	          	          
        }
        
    }
	protected void addFilter(String _filter, String caption, String sep){
		String prefix = _filter.substring(0, 4);
		
		if (this.filterSelectionTracker.length()>0){			
			if (this.filterSelectionTracker.contains(prefix)){
				this.filterSelectionTracker = _filter;
				this.param_filter = _filter;
		    	header_filter = header_slash + caption + sep;
			} else {
				this.filterSelectionTracker = this.filterSelectionTracker + "|" + _filter;
				this.param_filter = this.param_filter + " AND " + _filter;				
		    	header_filter = header_filter + header_slash + caption + sep;
			}
		} else {
			this.param_filter = _filter;
			this.filterSelectionTracker = _filter;
	    	header_filter = header_slash  +  caption + sep;
		}			
		
    	param_start = "0";
    	header_letter = "";

	}

    private void  setUrl() {
		// TODO Auto-generated method stub
    	
    	switch (dataType){
    	case DATA_DOC:
    		DocWS docWS;
    		docWS = new DocWS();
    		header_update = (param_start=="0");
    		url = docWS.getURL(this.getResources().getString(R.string.search_feed), this.getResources().getString(R.string.search_doc_count), param_words, param_filter, param_start);
    		break;
    	}
    	
		
	}
    private void treatResult(String text){
    	
		switch (dataType){
    	case DATA_DOC:
    		DocWS docWS;
    		docWS = new DocWS();
    		docWS.loadData(text, clusterCollection, docResulList, pagesList,this.getResources().getString(R.string.search_doc_count));
    		ws_result_total = docWS.getResultTotal();
    		if (ws_docs_total==""){
    			ws_docs_total = ws_result_total;
    		}
    		break;
    	}
    }
	/** Request an update to start after a short delay */
    private void queueUpdate(long delayMillis) {
    	// Cancel previous update if it has no started yet
    	guiThread.removeCallbacks(updateTask);
    	// Start an update if nothing happens after a few milliseconds
    	guiThread.postDelayed(updateTask, delayMillis);
    	
    }
    
    /** All changes to the GUI must be done in the GUI thread */
    private void guiSetText(final TextView view, final String text) {
    	guiThread.post(new Runnable() {
    		public void run() {
    			view.setText(text);
    		}
    	});
    }
    /** All changes to the GUI must be done in the GUI thread */
    private void guiSetData(final ArrayAdapter<?> adapter, final ArrayList<?> text) {
    	guiThread.post(new Runnable() {
    		public void run() {
    			//view.setText(text);
    			adapter.notifyDataSetChanged();		
    	    	
    		}
    	});
    }
    /** Modify text on the screen (called from another thread) */
    public void setResult(String text) {
       	if (text.length()>0){
       		treatResult(text);
       		if (header_update==true){
    			header_text = ws_docs_total;
    		    if (ws_query.length()>0){
    		    	header_text = header_text + header_slash + ws_query + header_sep + ws_result_total;
    			}
    			if (header_filter.length()>0) {
    				header_filter = header_filter + ws_result_total;
    				header_text = header_text + header_filter ;				
    			}
    			if (header_letter.length()>0) {
    				header_text = header_text + header_slash + header_letter  + header_sep + ws_result_total;
    			}			
    		}
    		
    	} else {
    		text ="No results found";
    	}
    	guiSetText(headerText, header_text);
    	if (listItemAdapter!=null){
    		guiSetData(listItemAdapter, docResulList);
    	}
    	if (pageAdapter!=null){
    		guiSetData(pageAdapter, pagesList);	
    	}
    	
    	
    }
    
}