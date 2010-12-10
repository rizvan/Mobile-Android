package org.scielo.search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.widget.TextView;

public class JournalActivity extends Activity{
	private TextView TextViewTitle; 
	private TextView TextViewCollection; 
	
	
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
	    setContentView(R.layout.doc);
	    
	    TextViewTitle = (TextView) findViewById(R.id.TextViewTitle);
	    TextViewCollection = (TextView) findViewById(R.id.TextViewCollection);	
	    
	    TextViewTitle.setText(getIntent().getStringExtra("title"));
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
