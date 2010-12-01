package org.scielo.search;

import android.app.Activity;
import android.content.Intent;
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
	private TextView TextViewPDF; 
	private TextView TextViewAbstract; 
	private TextView TextViewCollection; 
	
	
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
	    setContentView(R.layout.doc);
	    
	    TextViewIssue = (TextView) findViewById(R.id.TextViewDocumentPosition);	    
	    TextViewID = (TextView) findViewById(R.id.TextViewDocumentID);	    
	    TextViewTitle = (TextView) findViewById(R.id.TextViewDocumentTitle);
	    TextViewAuthors = (TextView) findViewById(R.id.TextViewDocumentAuthors);
	    TextViewPDF = (TextView) findViewById(R.id.TextViewDocumentPDFLink);	
	    TextViewAbstract = (TextView) findViewById(R.id.TextViewDocumentAbstract);
	    TextViewCollection = (TextView) findViewById(R.id.TextViewDocumentCollection);	
	    
	    TextViewIssue.setText(getIntent().getStringExtra("issue"));
	    TextViewID.setText(getIntent().getStringExtra("id"));
	    TextViewTitle.setText(getIntent().getStringExtra("title"));
	    TextViewAuthors.setText(getIntent().getStringExtra("authors"));
	    TextViewPDF.setText(getIntent().getStringExtra("pdf"));
	    TextViewAbstract.setText(getIntent().getStringExtra("abstract"));
	    TextViewCollection.setText(getIntent().getStringExtra("collection"));
	}
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
      
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.menu_details, menu);
      
    	return true;
    }
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {    
      
      // item.getItemId()
        switch (item.getItemId()) {
        case R.id.menuItemSendEmail:
    	    final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
			emailIntent.setType("text/plain");
			emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, TextViewTitle.getText());
			emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, TextViewTitle.getText() + "\n" + TextViewAbstract.getText()+ "\n" + TextViewPDF.getText() );
			startActivity(Intent.createChooser(emailIntent, "Email:"));

			
            return true;
      //case R.id.menuItemSaveResult:
          //quit();
      //    return true;
      //case R.id.menuItemSaveBookmark:
          //quit();
    	  
				
       //   return true;
      }

      return true;
    }}
