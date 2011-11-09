package org.scielo.search;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import android.widget.TextView;

public class DocumentActivity extends   MyActivity{
	private TextView TextViewIssue; 
	private TextView TextViewTitle; 
	private TextView TextViewAuthors; 
	//private TextView TextViewPDF; 
	private TextView TextViewAbstract; 
	private TextView TextViewCollection;
	private String fulltext_url;
	private String pdf_url; 
	
	private Handler guiThread;
	private ExecutorService transThread;
	private Runnable updateTask;
	private Future<?> transPending;
	protected String url;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
	    setContentView(R.layout.doc);

	    TextViewIssue = (TextView) findViewById(R.id.TextViewDocumentIssueLabel);	    
	    //TextViewID = (TextView) findViewById(R.id.TextViewDocumentID);	    
	    TextViewTitle = (TextView) findViewById(R.id.TextViewDocumentTitle);
	    TextViewAuthors = (TextView) findViewById(R.id.TextViewDocumentAuthors);
	    //TextViewPDF = (TextView) findViewById(R.id.TextViewDocumentPDFLink);	
	    TextViewAbstract = (TextView) findViewById(R.id.TextViewDocumentAbstract);
	    TextViewCollection = (TextView) findViewById(R.id.TextViewDocumentCollection);	
	    
	    
	    if (getIntent().getStringExtra("query").length()>0){
	    	DocumentsWS docWS = new DocumentsWS();
	    	url=docWS.getURL(this.getResources().getString(R.string.search_feed), "1", getIntent().getStringExtra("query"), "", "0");
	    	initThreading();
	    	queueUpdate(1000);
	    } else {
	    	guiThread = new Handler();
	    	guiSetText(TextViewIssue, getIntent().getStringExtra("issue"));
			guiSetText(TextViewTitle, getIntent().getStringExtra("title"));
			guiSetText(TextViewAuthors, getIntent().getStringExtra("authors"));
			guiSetText(TextViewAbstract, getIntent().getStringExtra("abstract"));
			guiSetText(TextViewCollection, getIntent().getStringExtra("collection"));
			

			pdf_url = getIntent().getStringExtra("pdf_url");
		    fulltext_url = getIntent().getStringExtra("fulltext_url");
	    }
	}
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
      
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.menu_details, menu);
      
    	
    	return true;
    }
	@Override
    public boolean onPrepareOptionsMenu(Menu menu) {
    	
    	MenuItem menuItem;
    	//MenuItem subMenuItem;
       
        if (pdf_url.length()==0){
            for (int index=0; index < menu.size(); index++){
            	menuItem = menu.getItem(index);
        		if (menuItem.getItemId()==R.id.menuItemDownloadPDF){
        			menuItem.setVisible(false);
        		}
        	}        	
        }
        
    	return true;
    }
	
	private String formatText(CharSequence charSequence){
		if (charSequence.length()>0){
			charSequence =  charSequence + "\n\n";
		}
		return (String) charSequence;
	}
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	boolean res = false;
    	final Intent docIntent;
      // item.getItemId()
        switch (item.getItemId()) {
        case R.id.menuItemSendEmail:
        	//final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        	final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        	emailIntent.setType("text/plain");
        	//emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, "roberta.takenaka@scielo.org");
			emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "[SciELO Android] " + TextViewTitle.getText());
			emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, formatText(this.getResources().getString(R.string.email_message_title))+  formatText(TextViewTitle.getText() + ". " + TextViewIssue.getText()) + formatText(fulltext_url) + formatText(pdf_url) + formatText(TextViewAbstract.getText())  );
			startActivity(Intent.createChooser(emailIntent, "Email:"));
            res = true;
            break;
        case R.id.menuItemDownloadPDF:
        	docIntent = new Intent(android.content.Intent.ACTION_VIEW , Uri.parse(pdf_url) );        	
			startActivity(docIntent);
        	res= true;
        	break;
        case R.id.menuItemFulltext:
        	docIntent = new Intent(android.content.Intent.ACTION_VIEW , Uri.parse(fulltext_url) );        	
			startActivity(docIntent);
        	res= true;
        	break;
      //case R.id.menuItemSaveResult:
          //quit();
      //    return true;
      //case R.id.menuItemSaveBookmark:
          //quit();
    	  
				
       //   return true;
      }

      return res;
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

			    		SearchTask translateTask = new SearchTask(
			    				DocumentActivity.this, // reference to activity
			    			url);
			    		transPending = transThread.submit(translateTask);
			    	} catch (RejectedExecutionException e) {
			    		// Unable to start new task
			    		
			    	}
			    
    		}    		
    	};
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
    /** Modify text on the screen (called from another thread) */
    public void setResult(String text) {
       	if (text.length()>0){
       		ClusterCollection clusterCollection = new ClusterCollection();
       		DocumentsWS docws = new DocumentsWS();
       		ArrayList<Page> pagesList = new ArrayList<Page>();
        	ArrayList<Document> resultList = new ArrayList<Document>();
			docws.loadData(text, clusterCollection, resultList, pagesList, "1") ;

			Document doc = resultList.get(0);
			ArticleURL articleURL = new ArticleURL(getResources().getString(R.string.pdf_and_log_url), getResources().getString(R.string.pdf_url), getResources().getString(R.string.article_url));

			if (doc.getPdf_url().length()==0){
		    	doc.setPdf_url(articleURL.returnPDFURL(doc));
	        }
	        if (doc.getHtml_url().length()==0){
	        	doc.setHtml_url(articleURL.returnFullTextURL(doc));
	        }
		    pdf_url = doc.getPdf_url();
	        fulltext_url = doc.getHtml_url();
		    
			guiSetText(TextViewIssue, doc.getIssueLabel());
			guiSetText(TextViewTitle, doc.getDocumentTitle());
			guiSetText(TextViewAuthors, doc.getAuthors());
			guiSetText(TextViewAbstract, doc.getDocumentAbstracts());
			guiSetText(TextViewCollection, doc.getCol().getName());
			
       	}

       	
    	
    }
        
}
