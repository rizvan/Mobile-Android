package org.scielo.search;


import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;


public class SciELOApps extends Activity {
	
	

	static MyAppConfig myConfig = new MyAppConfig();
	
	public void onCreate(Bundle savedInstanceState) {
		
		JournalsCollectionsNetwork jcn;
		IdAndValueObjects languages;
		IdAndValueObjects subjects;
		IdAndValueObjects letters;
		
		super.onCreate(savedInstanceState);
    	setContentView(R.layout.maintabless);
    	
    	jcn = new JournalsCollectionsNetwork();
		jcn.multiAdd(
  		    getResources().getStringArray(R.array.collections_code),
			getResources().getStringArray(R.array.collections_name), 
			getResources().getStringArray(R.array.log_collections_code), 
			getResources().getStringArray(R.array.collections_url) );		
		subjects = new IdAndValueObjects();
		subjects.multiAdd(getResources().getStringArray(R.array.subjects_id),
				getResources().getStringArray(R.array.subjects_name),true);
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
		
		//Intent docIntent = new Intent(SciELOApps.this, SearchJournalsActivity.class);
		Intent docIntent = new Intent(SciELOApps.this, SearchDocsActivity.class);
		startActivity(docIntent);

				           
        //startActivity(docIntent);					
    
    }

    
    
	
}
