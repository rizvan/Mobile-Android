package org.scielo.search;


import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class SciELOApps extends TabActivity {
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.main);

	    Resources res = getResources(); // Resource object to get Drawables
	    TabHost tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab

	    // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, ListDocsActivity.class);

	    // Initialize a TabSpec for each tab and add it to the TabHost
	    
	    spec = tabHost.newTabSpec("tab_list_docs").setIndicator(	     
	    		          res.getString(R.string.tab_list_docs_name),
	                      res.getDrawable(R.drawable.document_preview))
	                  .setContent(intent);
	                  
	    tabHost.addTab(spec);

	    // Do the same for the other tabs
	    intent = new Intent().setClass(this, ListJournalsActivity.class);
	    spec = tabHost.newTabSpec("tab_list_journals").setIndicator(
	    				  res.getString(R.string.tab_list_journals_name),
	                      res.getDrawable(R.drawable.document_preview))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    intent = new Intent().setClass(this, ListIssuesActivity.class);
	    spec = tabHost.newTabSpec("tab_list_issues").setIndicator(
				  res.getString(R.string.tab_list_issues_name),
	                      res.getDrawable(R.drawable.document_preview))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    intent = new Intent().setClass(this, SettingsActivity.class);
	    spec = tabHost.newTabSpec("tab_settings").setIndicator(
				  res.getString(R.string.tab_settings_name),
	                      res.getDrawable(R.drawable.document_preview))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    tabHost.setup();
	    tabHost.setCurrentTab(0);
	    
	    
	    
	}
}