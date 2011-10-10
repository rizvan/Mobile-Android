package org.scielo.search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.LinearLayout;
import android.widget.TextView;

public class SciELOAppsActivity extends Activity {
    /** Called when the activity is first created. */
    static MyAppConfig myConfig = new MyAppConfig();
    static String totalOfDocuments = "";
    TextView textNav;
    TextView textSearch;
    TextView textNavL;
    TextView textSearchL;
    LinearLayout layout;
    public static String currentSearchMainActivity = "";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
		
		JournalsCollectionsNetwork jcn;
		IdAndValueObjects languages;
		IdAndValueObjects subjects;
		IdAndValueObjects letters;
		
		super.onCreate(savedInstanceState);
    	setContentView(R.layout.main);
    	
    	jcn = new JournalsCollectionsNetwork();
		jcn.multiAdd(
  		    getResources().getStringArray(R.array.collections_code),
			getResources().getStringArray(R.array.collections_name), 
			getResources().getStringArray(R.array.log_collections_code), 
			getResources().getStringArray(R.array.collections_url) );		
		subjects = new IdAndValueObjects();
		subjects.multiAdd(getResources().getStringArray(R.array.subjects_id),
				getResources().getStringArray(R.array.subjects_name),false);
		languages = new IdAndValueObjects();
		languages.multiAdd(getResources().getStringArray(R.array.languages_id),
				getResources().getStringArray(R.array.languages_name), false);
		letters = new IdAndValueObjects();
		letters.multiAdd(getResources().getStringArray(R.array.initials),
				getResources().getStringArray(R.array.initials),false);

		myConfig.setJcn(jcn);
		myConfig.setSubjects(subjects);
		myConfig.setLanguages(languages);
		myConfig.setLetters(letters);
        
		
		
		
		
		
		textNav = (TextView) findViewById(R.id.call_naveg);
    	textSearch = (TextView) findViewById(R.id.call_search);
    	textNavL = (TextView) findViewById(R.id.call_naveg_letter);
    	textSearchL = (TextView) findViewById(R.id.call_search_letter);
    	
    	Intent docIntent = new Intent(textSearch.getContext(), SearchDocsActivity.class);		           
        startActivity(docIntent);	
        currentSearchMainActivity = "articles";
		
        textNav.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				currentSearchMainActivity = "journals";
				Intent docIntent = new Intent(v.getContext(), SearchJournalsActivity.class);
	            startActivity(docIntent);
			}
		});
    	
    	textSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				currentSearchMainActivity = "articles";
				Intent docIntent = new Intent(v.getContext(), SearchDocsActivity.class);		           
	            startActivity(docIntent);					
			}
		});
    	textNavL.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				currentSearchMainActivity = "journals";
				Intent docIntent = new Intent(v.getContext(), SearchJournalsActivity.class);
	            startActivity(docIntent);
			}
		});
    	
    	textSearchL.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				currentSearchMainActivity = "articles";
				Intent docIntent = new Intent(v.getContext(), SearchDocsActivity.class);		           
	            startActivity(docIntent);					
			}
		});
		

    }
}