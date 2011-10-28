package org.scielo.search;

import java.util.ArrayList;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;


public class Search extends Activity {
    /** Called when the activity is first created. */
	private Spinner fromSpinner;
	private Spinner toSpinner;
	
	
	
	private TextView resText;
	
	private TextWatcher textWatcher;
	private OnItemSelectedListener itemListener;
	
	private Handler guiThread;
	private ExecutorService transThread;
	private Runnable updateTask;
	private Future<?> transPending;
	private String url="http://www.google.com/search?q=";
	private TextView headerText;
	private String query="";
	private String query_id="";
	private EditText origText;
	private final int DATA_DOC = 0;
	private int dataType=0;
	private int pageIndex = -1;
	private String filter = "";
	private ClusterCollection clusterCollection = new ClusterCollection();
	private ArrayList<Document> resultList = new ArrayList<Document>();
	private ArrayList<Page> pagesList = new ArrayList<Page>();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_articles_draft);
        initThreading();
        
        findViews();
        setAdapters();
        setListeners();
        setQuery(getIntent());

	    queueUpdate(1000);
        
    }
    
    private void findViews(){
    	headerText = (TextView) findViewById(R.id.TextViewHeader);
    	resText = (TextView) findViewById(R.id.res);
    	
    }
    
    private void setAdapters(){
    	
    }
    
    private void setListeners() {
    }
    @Override
	protected void onNewIntent(Intent intent) {
	    setIntent(intent);
	    setQuery(intent);
	}

	protected void setQuery(Intent intent) {
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	      query = intent.getStringExtra(SearchManager.QUERY);
	    } else {
	    	query = query_id;
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
			    
				    headerText.setText("missing url");
			    
			    	// Let user know we re doing something
			    	headerText.setText("...");
			    	
			    	// Begin translation now but do not wait for it
			    	try {
			    		setUrl();
			    		resText.setText(url);
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
    private void  setUrl() {
		// TODO Auto-generated method stub
    	
    	switch (dataType){
    	case DATA_DOC:
    		DocManager docManager;
    		docManager = new DocManager();
    		url = docManager.getURL(this.getResources().getString(R.string.search_feed), this.getResources().getString(R.string.search_doc_count), query,filter ,pageIndex);
    		break;
    	}
    	
		
	}
    private void treatResult(String text){
    	
		switch (dataType){
    	case DATA_DOC:
    		DocManager docManager;
    		docManager = new DocManager();
    		docManager.loadData(text, clusterCollection, resultList, pagesList);
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
    /** Modify text on thhe screen (called from another thread) */
    public void setResult(String text) {
       	guiSetText(resText, text);
           	if (text.length()>0){
    		text = "resutados" ;
    	} else {
    		text ="No results found";
    	}
    	guiSetText(headerText, text);
    }
    
}