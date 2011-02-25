package org.scielo.search;


import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
//import android.graphics.Color;
import android.os.Bundle;

import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

public class SciELOApps extends TabActivity implements OnTabChangeListener{
	TabHost tabHost;
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.main);
	    
	    Resources res = getResources(); // Resource object to get Drawables
	    
	    tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab
	    
	    // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, SearchDocsActivity.class);
	    spec = tabHost.newTabSpec("tab_search_docs").setIndicator(res.getString(R.string.tab_list_docs_name)).setContent(intent);
	    tabHost.addTab(spec);
	    
	    intent = new Intent().setClass(this, SearchJournalsActivity.class);
	    spec = tabHost.newTabSpec("tab_list_journals").setIndicator(res.getString(R.string.tab_list_journals_name)).setContent(intent);
	    tabHost.addTab(spec);

	    
	    tabHost.setup();
	    tabHost.setCurrentTab(0);
	    
	    /*
	    for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
        {
	    	tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#7392B5"));
	    	tabHost.getTabWidget().getChildAt(i).setPadding(3, 3, 1, 0);
	    	
        }
        
        tabHost.getTabWidget().setCurrentTab(1);
        tabHost.getTabWidget().getChildAt(1).setBackgroundColor(Color.parseColor("#4E4E9C"));
        */
    }

    
    
	@Override
	public void onTabChanged(String tabId) {
		// TODO Auto-generated method stub
		for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
        {
        	tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#7392B5"));
        } 
				
		tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundColor(Color.parseColor("#4E4E9C"));
	}
}
