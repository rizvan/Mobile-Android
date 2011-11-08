package org.scielo.search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.LinearLayout;
import android.widget.TextView;

public class HomeActivity extends Activity {
    
/** Called when the activity is first created. */
    TextView textNav;
    TextView textSearch;
    TextView textNavL;
    TextView textSearchL;
    
    
    LinearLayout layout;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
		
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.main);
    	
    	SciELOAppsActivity.currentSearchMainActivity = "HOME";
		
		textNav = (TextView) findViewById(R.id.call_naveg);
    	textSearch = (TextView) findViewById(R.id.call_search);
    	textNavL = (TextView) findViewById(R.id.call_naveg_letter);
    	textSearchL = (TextView) findViewById(R.id.call_search_letter);
    	
    	/*
    	Intent docIntent = new Intent(textSearch.getContext(), SearchDocsActivity.class);		           
        startActivity(docIntent);	
        currentSearchMainActivity = "articles";
		*/
    	
    	

       
        	OnClickListener a = new OnClickListener() {
    			@Override
    			public void onClick(View v) {				
    					
    					Intent docIntent = new Intent(v.getContext(), Search.class);
    					docIntent.putExtra("SciELOAppsActivity.DATA_type", SciELOAppsActivity.DATA_DOC);
    		            startActivity(docIntent);
    								
    			}
    		};
        	OnClickListener j = new OnClickListener() {
    			@Override
    			public void onClick(View v) {				
    					Intent docIntent = new Intent(v.getContext(), Search.class);
    					docIntent.putExtra("SciELOAppsActivity.DATA_type", SciELOAppsActivity.DATA_JOURNAL);
    		            startActivity(docIntent);
    				}				
    			
    		};
        	textSearchL.setOnClickListener(a);
        	textSearch.setOnClickListener(a);
        	textNavL.setOnClickListener(j);
        	textNav.setOnClickListener(j);
    			

    }
}