package org.scielo.serch;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SciELOAppsActivity extends Activity {
    /** Called when the activity is first created. */
	TextView banner;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        banner = (TextView) findViewById(R.id.banner);
        
        Intent docIntent = new Intent(banner.getContext(), Search.class);
		
        startActivity(docIntent);	

    }
}