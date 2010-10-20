package org.scielo.search;

import android.app.Activity;
import android.os.Bundle;

import android.widget.TextView;

public class DocumentActivity extends Activity{
	TextView detail; 
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
	    setContentView(R.layout.search_result_details);
	    
	    String id = getIntent().getStringExtra("id");
	    detail = (TextView) findViewById(R.id.searchResultDetailsTextView);	    
	    detail.setText(id);
	    
	    
	}
}
