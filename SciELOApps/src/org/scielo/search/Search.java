package org.scielo.search;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


public class Search extends MyActivity {


	/** Called when the activity is first created. */
	
	private Handler guiThread;
	private ExecutorService transThread;
	private Runnable updateTask;
	private Future<?> transPending;
	
	private String url="";

	
	private int dataType=0;
	
	private String[] clusterCodeOrder = null;
	private ClusterCollection clusterCollection = new ClusterCollection();
	
	private ArrayList<Document> docResultList = new ArrayList<Document>();
	private ArrayList<Page> pagesList = new ArrayList<Page>();
	private ArrayList<Journal> journalResultList = new ArrayList<Journal>();
	private ArrayList<Issue> issuesResultList = new ArrayList<Issue>();
	
	
	
	private TextView textNav;
	private TextView textSearch;
	private TextView textNavLetter;
	private TextView textSearchLetter;
	private TextView banner;
	private TextView headerText;
	private GridView paginationGridView;
	private ListView resultListView;
	
	private DocumentsListAdapter docListAdapter;
	private JournalsListAdapter journalListAdapter;
	private IssuesListAdapter issuesListAdapter;
	private PaginationAdapter pageListAdapter;
	private OnItemClickListener listItemListener;
	private OnItemClickListener pageListener;
	
	
	private boolean header_update = false;
	private String header_text = "";
	private String header_sep = "";
	private String header_filter = "";
	//private String header_letter = "";
	private String header_slash = "";
	private String header_search_expression ="";

	private String filterSelectionTracker="";


	private String ws_param_start = "0";
	private String ws_param_search_expression="";
	private String ws_param_filter = "";


	private String ws_docs_total = "";
	private String ws_result_total= "";

	private Journal journal ;
	private Issue issue;

	private int layout_menu_id;
	private String header_tracking ="";
	private String currentFilter = "";
	private String currentCaption= "";
	private String bkp_ws_param_filter = "";
	private String bkp_filterSelectionTracker= "";
	private String bkp_le= "";
	private String bkp_ac= "";
	private String bkp_in= "";
	private String bkp_le_caption= "";
	private String bkp_ac_caption= "";
	private String bkp_in_caption= "";
	private String bkp_la;
	private String bkp_la_caption;
	private String bkp_ta;
	private String bkp_ta_caption;
	private String bkp_y;
	private String bkp_y_caption;
	

	
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        int layout_id = 0;
        layout_menu_id=0;
        clusterCollection = new ClusterCollection();
	    
	    header_text = "";
	    header_slash = "";
	    header_sep = "";
	    
	    ws_param_search_expression = "";
	    
	    dataType = getIntent().getIntExtra("data_type", SciELOAppsActivity.DATA_DOC);
	    dialog = new ProgressDialog(Search.this);
	    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	    dialog.setMessage(getResources().getString(R.string.loading_data_message));
	    
	    switch(dataType){
        case SciELOAppsActivity.DATA_DOC:
        	SciELOAppsActivity.currentSearchMainActivity = "articles";
        	
        	layout_menu_id = R.menu.menu;
        	layout_id = R.layout.search_articles;
    	    
        	docListAdapter = new DocumentsListAdapter(this, R.layout.list_item_doc, docResultList,false);
        	pageListAdapter = new PaginationAdapter(this, R.layout.pagination, pagesList);
    	    
        	clusterCodeOrder = getResources().getStringArray(R.array.cluster_list_doc);
            
        	header_sep = ":";
            header_slash = "/";
            
    	    break;
        case SciELOAppsActivity.DATA_JOURNAL:
        	SciELOAppsActivity.currentSearchMainActivity = "journals";
        	
        	layout_id = R.layout.search_journals;
    	    layout_menu_id = R.menu.menu_journals;
    	    
    	    journalListAdapter = new JournalsListAdapter(this, R.layout.list_item_journal, journalResultList);
    	    
        	clusterCodeOrder = getResources().getStringArray(R.array.cluster_list_journal);
            
        	//this.queueUpdate(1000);  
        	
        	header_sep = ":";
            header_slash = "/";
            addFilter("le:A" , "A", header_sep);
        	
            break;
        case SciELOAppsActivity.DATA_ISSUES:
        	SciELOAppsActivity.currentSearchMainActivity = "journals";
        	journal = new Journal();
        	journal.setId(getIntent().getStringExtra("id"));
    		journal.setTitle(getIntent().getStringExtra("title"));
    		journal.setCollectionName(getIntent().getStringExtra("collection"));
    		journal.setCollectionId(getIntent().getStringExtra("collection_id"));
    	    

    		layout_id = R.layout.search_journals;
    	    layout_menu_id = 0;
    	    
    	    issuesListAdapter = new IssuesListAdapter(this, R.layout.list_item_issue, issuesResultList);
    	    
    	    ws_param_search_expression = journal.getId();
    	    
    		header_text =  journal.getCollectionName() + " - " + journal.getTitle();
    		header_update = false;
    		break;
        case SciELOAppsActivity.DATA_TOC:
        	SciELOAppsActivity.currentSearchMainActivity = "journals";
        	journal = new Journal();
        	journal.setCollectionName(getIntent().getStringExtra("collection"));
        	journal.setTitle(getIntent().getStringExtra("title"));
        	journal.setCollectionId(getIntent().getStringExtra("collection_id"));
    	    
        	issue = new Issue();
    		issue.setId(getIntent().getStringExtra("id"));
    	    issue.setJournal(journal);
    	    issue.setNumber(getIntent().getStringExtra("n"));
    	    issue.setSuppl(getIntent().getStringExtra("s"));
    	    issue.setVolume(getIntent().getStringExtra("v"));
    	    
    	    layout_id = R.layout.search_journals;
    	    docListAdapter = new DocumentsListAdapter(this, R.layout.list_item_doc_toc, docResultList, true);
           	
    	    ws_param_search_expression = issue.getId();
    	    header_text = journal.getCollectionName() + " - " + issue.getIssueLabel(true);
    	    break;
        }
        
        
        setContentView(layout_id);
        initThreading();
        
        findViews();
        setAdapters();
        setListeners();
        setQuery(getIntent());

	    queueUpdate(1000);
	    
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
    	if (docListAdapter != null){
    		resultListView.setAdapter(docListAdapter);        		
    	}
    	if (pageListAdapter!=null){
	    	paginationGridView.setAdapter(pageListAdapter);
    	}
    	if (journalListAdapter != null){
    		resultListView.setAdapter(journalListAdapter);
    	}
    	if (issuesListAdapter !=null) {
    		resultListView.setAdapter(issuesListAdapter);
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
					dataType = SciELOAppsActivity.DATA_DOC;
					
					Intent docIntent = new Intent(v.getContext(), Search.class);
					docIntent.putExtra("data_type", dataType);
		            startActivity(docIntent);
								
			}
		};
    	OnClickListener j = new OnClickListener() {
			@Override
			public void onClick(View v) {				
					dataType = SciELOAppsActivity.DATA_JOURNAL;
					
					Intent docIntent = new Intent(v.getContext(), Search.class);
					docIntent.putExtra( "data_type", dataType);
		            startActivity(docIntent);
								
			}
		};
    	textSearchLetter.setOnClickListener(a);
    	textSearch.setOnClickListener(a);
    	textNavLetter.setOnClickListener(j);
    	textNav.setOnClickListener(j);
	    switch (dataType){
	    case SciELOAppsActivity.DATA_JOURNAL:
	    	listItemListener = new OnItemClickListener() {
			       @Override
				   public void onItemClick(AdapterView<?> _av, View _v, int _index, long id) {
			           Journal searched = journalResultList.get(_index);
			           Intent docIntent = new Intent(_v.getContext(), Search.class);
    		           docIntent.putExtra("data_type", SciELOAppsActivity.DATA_ISSUES);
			           docIntent.putExtra("id", searched.getId());
			           docIntent.putExtra("title", searched.getTitle());
			           docIntent.putExtra("collection", searched.getCollectionName());
			           docIntent.putExtra("collection_id", searched.getCollectionId());		           
			           startActivity(docIntent);
			       }
			};
	    	break;
	    case SciELOAppsActivity.DATA_DOC:
	    	listItemListener = new OnItemClickListener() {
	 	       @Override
	 		   public void onItemClick(AdapterView<?> _av, View _v, int _index, long id) {
	 	           Document searched = docResultList.get(_index);
	 	           //showDialog(SEARCH_RESULT_DIALOG);
	 	           
	 	           Intent docIntent = new Intent(_v.getContext(), DocumentActivity.class);
	 	           //docIntent.putExtra("DATA", selectedSearchResult);
	 	           docIntent.putExtra("query", "");
	 	           docIntent.putExtra("id", searched.getDocumentId());
	 	           docIntent.putExtra("title", searched.getDocumentTitle());
	 	           docIntent.putExtra("authors", searched.getDocumentAuthors());
	 	           ArticleURL articleURL = new ArticleURL(getResources().getString(R.string.pdf_and_log_url), getResources().getString(R.string.pdf_url), getResources().getString(R.string.article_url));
	 	    	    
	 	           if (searched.getPdf_url().length()==0){
	 	        	   searched.setPdf_url(articleURL.returnPDFURL(searched));
	 	           }
	 	           
	 	           String teste = searched.getCompl();
	 	           String teste2 = articleURL.returnFullTextURL(searched);
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
	 		
	 		break;
	    case SciELOAppsActivity.DATA_ISSUES:
	    	listItemListener = new OnItemClickListener() {
    		       @Override
    			   public void onItemClick(AdapterView<?> _av, View _v, int _index, long id) {
    		           Issue searched = issuesResultList.get(_index);
    		           //showDialog(SEARCH_RESULT_DIALOG);
    		           
    		           Intent docIntent = new Intent(_v.getContext(), Search.class);
    		           docIntent.putExtra("data_type", SciELOAppsActivity.DATA_TOC);
    		           docIntent.putExtra("id", searched.getId());
    		           docIntent.putExtra("date", searched.getDate());
    		           docIntent.putExtra("v", searched.getVolume());
    		           docIntent.putExtra("n", searched.getNumber());
    		           docIntent.putExtra("s", searched.getSuppl());
    		           docIntent.putExtra("collection_id", searched.getJournal().getCollectionId());
    		           docIntent.putExtra("collection", searched.getJournal().getCollectionName());
    		           docIntent.putExtra("title", searched.getJournal().getTitle());
    		           startActivity(docIntent);
    		       }
    		};
    	    break;
	    case SciELOAppsActivity.DATA_TOC:
	    	listItemListener = new OnItemClickListener() {
 		       @Override
 			   public void onItemClick(AdapterView<?> _av, View _v, int _index, long id) {
 		           Document searched = docResultList.get(_index);
 		           //showDialog(SEARCH_RESULT_DIALOG);
 		           
 		           Intent docIntent = new Intent(_v.getContext(), DocumentActivity.class);
 		           //docIntent.putExtra("DATA", selectedSearchResult);
 		           String q = "id:" + '"' + "art-" + searched.getDocumentId() + "^c" + searched.getCol().getId() + '"';
 		           docIntent.putExtra("query", q);
 		           startActivity(docIntent);
 		        }
	    	};
	    	break;
	    }
 		pageListener = new OnItemClickListener() {
	        
 			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	        	Page page = pagesList.get(position);
	        	ws_param_start = page.getIndex2search();
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
	      ws_param_search_expression = intent.getStringExtra(SearchManager.QUERY);
	      header_filter = "";
	      ws_param_filter = "";
	      header_search_expression = ws_param_search_expression;
	    } 

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
	            /*
	        case R.id.reset:
	        	//this.filterSelectionTracker = "";
	        	ws_param_filter = "";
				header_filter = "";
				
		    	queueUpdate(1000);
	            return true;*/
	        case R.id.menuItemNewSearchByCollection:
    		case R.id.menuItemNewSearchByInitial:
    		case R.id.menuItemNewSearchBySubject:
    			resetParameters();
    			
        			break;
    		
    		
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
        return true;
        
    }
	
	private void resetParameters() {
		// TODO Auto-generated method stub
		this.ws_param_filter ="";
		this.filterSelectionTracker="";
		this.header_tracking="";
	}

	protected void addFilter(String _filter, String caption, String sep){
		if (dataType == SciELOAppsActivity.DATA_DOC){
			addFilter4Articles( _filter,  caption,  sep);
		}
		if (dataType == SciELOAppsActivity.DATA_JOURNAL){
			addFilter4Journal( _filter,  caption,  sep);
		}
		
	}

	protected void addFilter4Articles(String _filter, String caption, String sep){
			String prefix = _filter.substring(0, 4);
			
			if (this.filterSelectionTracker.contains(prefix)){
				
				this.ws_param_filter = 	 _filter;
				this.filterSelectionTracker = 	 _filter;
				this.header_tracking = "";
				
				header_filter = "";
				/*
				
				this.ws_param_filter = _filter;
				this.filterSelectionTracker = _filter;
				header_tracking ="";
				*/
			} else {
				
				String concat = "";
				if (this.ws_param_filter.length()>0){
					concat = " AND ";
				}
				this.ws_param_filter = this.ws_param_filter + concat + _filter;		
				
				if (this.filterSelectionTracker.length()>0){
					concat = "|";
				}
				this.filterSelectionTracker = this.filterSelectionTracker + concat + _filter;
				header_filter = 	caption;
		    }
					
			
	    	ws_param_start = "0";
	    	
		
	}

	protected void addFilter4Journal(String _filter, String caption, String sep){
		String prefix = _filter.substring(0, 3);
		String toReplace = "";
		String captionToReplace = "";
		this.currentFilter = _filter;
		this.currentCaption = caption;
		
		this.bkp_ws_param_filter  = this.ws_param_filter;
		this.bkp_filterSelectionTracker = this.filterSelectionTracker;
		
		if (this.filterSelectionTracker.contains(prefix)){
			if (prefix.equals("le:")){
				toReplace = this.bkp_le ;
				this.bkp_le = _filter;
				captionToReplace = this.bkp_le_caption;
				this.bkp_le_caption = caption;
			}
			if (prefix.equals("ac:")){
				toReplace = this.bkp_ac ;
				this.bkp_ac = _filter;			
				captionToReplace = this.bkp_ac_caption;
				this.bkp_ac_caption = caption;
			}
			if (prefix.equals("in:")){
				toReplace = this.bkp_in ;
				this.bkp_in = _filter;
				captionToReplace = this.bkp_in_caption;
				this.bkp_in_caption = caption;
			}
			if (prefix.equals("la:")){
				toReplace = this.bkp_la ;
				this.bkp_la = _filter;
				captionToReplace = this.bkp_la_caption;
				this.bkp_la_caption = caption;
			}
			if (prefix.equals("ta_cluster:")){
				toReplace = this.bkp_ta ;
				this.bkp_ta = _filter;
				captionToReplace = this.bkp_ta_caption;
				this.bkp_ta_caption = caption;
			}
			if (prefix.equals("year_cluster:")){
				toReplace = this.bkp_y ;
				this.bkp_y = _filter;
				captionToReplace = this.bkp_y_caption;
				this.bkp_y_caption = caption;
			}
			this.ws_param_filter = 	this.ws_param_filter.replace(toReplace, _filter);
			this.filterSelectionTracker = 	this.filterSelectionTracker.replace(toReplace, _filter);
			//this.header_tracking = this.header_tracking.replace(captionToReplace, caption);
			
			int i = this.header_tracking.indexOf(header_slash + captionToReplace + ":");
			String c = this.header_tracking.substring(i+1);
			int k = c.indexOf(header_slash);
			if (k == -1){
				this.header_tracking =  this.header_tracking.substring(0,i);
			} else{
				String before = this.header_tracking.substring(0,i);
				String after = this.header_tracking.substring(i+k+1);
				this.header_tracking =  before+ after;
			}
			
			header_filter = caption;
			/*
			
			this.ws_param_filter = _filter;
			this.filterSelectionTracker = _filter;
			header_tracking ="";
			*/
		} else {
			if (prefix.equals("le:")){
				this.bkp_le = _filter;
				this.bkp_le_caption = caption;
			}
			if (prefix.equals("ac:")){
				this.bkp_ac = _filter;	
				this.bkp_ac_caption = caption;
			}
			if (prefix.equals("in:")){
				this.bkp_in = _filter;
				this.bkp_in_caption = caption;
			}
			if (prefix.equals("ta_cluster:")){
				this.bkp_ta= _filter;
				this.bkp_ta_caption = caption;
			}
			if (prefix.equals("year_cluster:")){
				this.bkp_y = _filter;	
				this.bkp_y_caption = caption;
			}
			if (prefix.equals("la:")){
				this.bkp_la = _filter;
				this.bkp_la_caption = caption;
			}
			String concat = "";
			if (this.ws_param_filter.length()>0){
				concat = " AND ";
			}
			this.ws_param_filter = this.ws_param_filter + concat + _filter;		
			
			if (this.filterSelectionTracker.length()>0){
				concat = "|";
			}
			this.filterSelectionTracker = this.filterSelectionTracker + concat + _filter;
			header_filter = 	caption;
	    }
				
		ws_param_start = "0";
    }
	
	
	private void restoreParameters(){
		this.ws_param_filter =this.bkp_ws_param_filter;
		this.filterSelectionTracker=this.bkp_filterSelectionTracker;
		
	}
	
    private void  setUrl() {
		// TODO Auto-generated method stub
    	
    	switch (dataType){
    	case SciELOAppsActivity.DATA_DOC:
    		DocumentsWS docWS;
    		docWS = new DocumentsWS();
    		header_update = (ws_param_start.equals("0"));
    		url = docWS.getURL(this.getResources().getString(R.string.search_feed), this.getResources().getString(R.string.search_doc_count), ws_param_search_expression, ws_param_filter, ws_param_start);
    		break;
    	case SciELOAppsActivity.DATA_JOURNAL:
    		header_update = true;
    		JournalsWS jWS = new JournalsWS();
    		IdAndValueObjects journal_ws_urls= new IdAndValueObjects();
    		journal_ws_urls.multiAdd(getResources().getStringArray(R.array.journal_urls_id),
    				getResources().getStringArray(R.array.journal_urls), false);
    		
    		url = jWS.getURL(journal_ws_urls, ws_param_filter);
    		break;
    	case SciELOAppsActivity.DATA_ISSUES:
    		IssuesWS issuesWS = new IssuesWS();
    		url = issuesWS.getURL(this.getResources().getString(R.string.search_feed_issues), ws_param_search_expression);
    		break;
    	case SciELOAppsActivity.DATA_TOC:
    		TOCWS ws = new TOCWS();
    		url = ws.getURL(this.getResources().getString(R.string.search_feed_issuetoc), ws_param_search_expression);
    		break;
    	}
    	
		
	}
    private void treatResult(String text){
    	
		switch (dataType){
    	case SciELOAppsActivity.DATA_DOC:
    		DocumentsWS docWS;
    		docWS = new DocumentsWS();
    		docWS.loadData(text, clusterCollection, docResultList, pagesList,this.getResources().getString(R.string.search_doc_count));
    		ws_result_total = docWS.getResultTotal();
    		if (SciELOAppsActivity.docs_total.equals("")) {
    			SciELOAppsActivity.docs_total = ws_result_total;
    		}
    		ws_docs_total = SciELOAppsActivity.docs_total;
    		break;
    	case SciELOAppsActivity.DATA_JOURNAL:
    		JournalsWS jWS = new JournalsWS();
    		jWS.loadData(text, clusterCodeOrder, clusterCollection, journalResultList, (ws_param_filter.length()==0));
    		
    		
    		
    		ws_result_total = jWS.getResult_total();
    		ws_docs_total = jWS.getDocs_total();
    		header_update = true;
			//updateFilterSelectionTracker();
			
    		
    		break;
    	case SciELOAppsActivity.DATA_ISSUES:
    		IssuesWS ws = new IssuesWS();
    		ws.loadData(text, journal, issuesResultList);
    		break;
    	case SciELOAppsActivity.DATA_TOC:
    		TOCWS tocws = new TOCWS();
    		tocws.loadData(text, issue, docResultList);
    		break;
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
			    	
			    	// Begin translation now but do not wait for it
			    	try {
			    		setUrl();
			    		headerText.setText(getResources().getString(R.string.loading_data_message) + "... ");
			    		SearchTask translateTask = new SearchTask(
			    				Search.this, // reference to activity
			    			url);
			    		resultIsReady = false;
			    		transPending = transThread.submit(translateTask);
			    		showProgressDialog();
			    		
			    	} catch (RejectedExecutionException e) {
			    		// Unable to start new task
			    		headerText.setText("error");
			    	}
			    
    		}    		
    	};
    }

	/** Request an update to start after a short delay */
    private void queueUpdate(long delayMillis) {
    	headerText.setText(getResources().getString(R.string.loading_data_message) + "... ");
		
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
    	resultIsReady =true;
    	treatResult(text);
   		if (header_update==true){
   			if (header_search_expression.length()>0){
   				header_tracking = header_slash + header_search_expression + header_sep + ws_result_total;
   				header_search_expression ="";
			}
			if (header_filter.length()>0) {
				header_tracking = header_tracking + header_slash + header_filter + header_sep + ws_result_total;
			}
				
			header_text = ws_docs_total + header_tracking ;
		}
		
    	
    	guiSetText(headerText, header_text);
    	if (docListAdapter!=null){
    		guiSetData(docListAdapter, docResultList);
    	}
    	if (journalListAdapter!=null){
    		guiSetData(journalListAdapter, journalResultList);
    	}
    	if (issuesListAdapter!=null){
    		guiSetData(issuesListAdapter, issuesResultList);
    	}
    	if (pageListAdapter!=null){
    		guiSetData(pageListAdapter, pagesList);	
    	}
    	dialog.hide();
       	
    	
    }
    
}