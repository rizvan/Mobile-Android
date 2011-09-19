package org.scielo.search;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import android.widget.TextView;

public class DocumentActivity extends Activity{
	private TextView TextViewIssue; 
	private TextView TextViewID;
	private TextView TextViewTitle; 
	private TextView TextViewAuthors; 
	//private TextView TextViewPDF; 
	private TextView TextViewAbstract; 
	private TextView TextViewCollection; 
	
	private ArrayList<Document> searchResultList =  new ArrayList<Document>();
	private ArrayList<Page> pagesList =  new ArrayList<Page>();

	private DocSearcher ssData;
	private SearchService ss;
	private String pdf_url;
	private String fulltext_url;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
	    setContentView(R.layout.doc);

	    ClusterCollection clusterCollection = new ClusterCollection();
	    ssData = new DocSearcher(this.getResources().getString(R.string.search_feed), clusterCollection , searchResultList, pagesList);
	    ss = new SearchService();
		
	    TextViewIssue = (TextView) findViewById(R.id.TextViewDocumentPosition);	    
	    TextViewID = (TextView) findViewById(R.id.TextViewDocumentID);	    
	    TextViewTitle = (TextView) findViewById(R.id.TextViewDocumentTitle);
	    TextViewAuthors = (TextView) findViewById(R.id.TextViewDocumentAuthors);
	    //TextViewPDF = (TextView) findViewById(R.id.TextViewDocumentPDFLink);	
	    TextViewAbstract = (TextView) findViewById(R.id.TextViewDocumentAbstract);
	    TextViewCollection = (TextView) findViewById(R.id.TextViewDocumentCollection);	
	    if (getIntent().getStringExtra("query").length()>0){
	    	String queryurl = ssData.getURL(getIntent().getStringExtra("query"), "20", "", 0);
			String result = ss.call(queryurl);
			ssData.genLoadData(result);
			
			searchResultList = ssData.getSearchResultList();

			Document doc = searchResultList.get(0);
			TextViewIssue.setText(doc.getIssueLabel());
		    TextViewID.setText(doc.getDocumentId());
		    TextViewTitle.setText(doc.getDocumentTitle());
		    TextViewAuthors.setText(doc.getDocumentAuthors());
		    //TextViewPDF.setText(doc.getDocumentPDFLink());
		    
		    ArticleURL articleURL = new ArticleURL(getResources().getString(R.string.pdf_and_log_url), getResources().getString(R.string.pdf_url), getResources().getString(R.string.article_url));
		    
		    
		    if (doc.getPdf_url().length()==0){
		    	doc.setPdf_url(articleURL.returnPDFURL(doc));
	        }
	        if (doc.getHtml_url().length()==0){
	        	doc.setHtml_url(articleURL.returnFullTextURL(doc));
	        }
		    
	        pdf_url = doc.getPdf_url();
	        fulltext_url = doc.getHtml_url();
		    
		       
		    TextViewAbstract.setText(doc.getDocumentAbstracts());
		    TextViewCollection.setText(doc.getCol().getName());
	    } else {
		    TextViewIssue.setText(getIntent().getStringExtra("issue"));
		    TextViewID.setText(getIntent().getStringExtra("id"));
		    TextViewTitle.setText(getIntent().getStringExtra("title"));
		    TextViewAuthors.setText(getIntent().getStringExtra("authors"));
		    //TextViewPDF.setText(getIntent().getStringExtra("pdf"));
		    TextViewAbstract.setText(getIntent().getStringExtra("abstract"));
		    TextViewCollection.setText(getIntent().getStringExtra("collection"));	  
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
			emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, TextViewTitle.getText());
			
			
			
			emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, formatText(TextViewTitle.getText()) + formatText(TextViewAbstract.getText()) + formatText(fulltext_url) + formatText(pdf_url) );
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
    }}
