package org.scielo.search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SciELOAppsActivity extends Activity {
    /** Called when the activity is first created. */
    static MyAppConfig myConfig = new MyAppConfig();
    static String totalOfDocuments = "";
    public static String currentSearchMainActivity = "";
    TextView banner;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.main);
    	
		JournalsCollectionsNetwork jcn;
		IdAndValueObjects languages;
		IdAndValueObjects subjects;
		IdAndValueObjects letters;
		
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

		banner = (TextView) findViewById(R.id.banner);
		Intent docIntent = new Intent(banner.getContext(), SearchDocsActivity.class);		           
        startActivity(docIntent);	

    }
       
}