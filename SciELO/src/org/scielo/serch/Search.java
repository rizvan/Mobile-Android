package org.scielo.serch;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;


public class Search extends Activity {
    /** Called when the activity is first created. */
	private Spinner fromSpinner;
	private Spinner toSpinner;
	private EditText origText;
	
	private TextView transText;
	private TextView retransText;
	
	private TextWatcher textWatcher;
	private OnItemSelectedListener itemListener;
	
	private Handler guiThread;
	private ExecutorService transThread;
	private Runnable updateTask;
	private Future<?> transPending;
	private String url="http://www.scielo.br";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_translate);
        
        initThreading();
        findViews();
        setAdapters();
        setListeners();
    }
    
    private void findViews(){
    	fromSpinner = (Spinner) findViewById(R.id.from_language);
    	toSpinner = (Spinner) findViewById(R.id.to_language);
    	origText = (EditText) findViewById(R.id.original_text);
    	transText = (TextView) findViewById(R.id.translated_text);
    	retransText = (TextView) findViewById(R.id.retranslated_text);
    }
    
    private void setAdapters(){
    	ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
    			this, R.array.languages, android.R.layout.simple_spinner_item);
    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	fromSpinner.setAdapter(adapter);
    	toSpinner.setAdapter(adapter);
    	
    	// Automatically select two spinner items
    	fromSpinner.setSelection(1); // language 1
    	toSpinner.setSelection(2); // language 2
    }
    
    private void setListeners() {
    	// Define event listeners
    	textWatcher = new TextWatcher() {
    		public void beforeTextChanged(CharSequence s, int start, int cunt, int after){
    			/* Do nothing */
    		}
    		public void onTextChanged(CharSequence s, int start, int before, int count){
    			queueUpdate(1000 /* milliseconds */);
    		}
    		public void afterTextChanged(Editable s){
    			/* Do nothing */
    		}
    	};
    	itemListener = new OnItemSelectedListener() {
    		public void onItemSelected(AdapterView<?> parent, View v, int position, long id){
    			queueUpdate(200 /* milliseconds */);
    		}
    		public void onNothingSelected(AdapterView<?> parent){
    			/* Do nothing */
    		}
    	};
    	// set listeners on graphical user interface widgets
    	origText.addTextChangedListener(textWatcher);
    	fromSpinner.setOnItemSelectedListener(itemListener);
    	toSpinner.setOnItemSelectedListener(itemListener);
    	
    }
    
    private void initThreading() {
    	guiThread = new Handler();
    	transThread = Executors.newSingleThreadExecutor();
    	// This task does a translation and updates the screen
    	updateTask = new Runnable() {
    		

			public void run() {
    		    // Get text to translate
    			String original = origText.getText().toString().trim();
    			   
		        // Cancel prvious translation if there was one
		        if (transPending != null)
			        transPending.cancel(true);
			    // Take care of the easy case
			    
				    transText.setText("missing url");
			    
			    	// Let user know we re doing something
			    	transText.setText("...");
			    	
			    	// Begin translation now but do not wait for it
			    	try {
			    		SearchTask translateTask = new SearchTask(
			    				Search.this, // reference to activity
			    				original,
			    				getLang(fromSpinner), // from
			    				getLang(toSpinner) // to language
			    				);
			    		transPending = transThread.submit(translateTask);
			    	} catch (RejectedExecutionException e) {
			    		// Unable to start new task
			    		transText.setText("error");
			    	}
			    
    		}    		
    	};
    }
    /** Extract the language code from the current spinner item */
    private String getLang(Spinner spinner){
    	String result = spinner.getSelectedItem().toString();
    	int lparen = result.indexOf('(');
    	int rparen = result.indexOf(')');
    	result = result.substring(lparen + 1, rparen);
    	return result;
    }
    /** Request an update to start after a short delay */
    private void queueUpdate(long delayMillis) {
    	// Cancel previous update if it has no started yet
    	guiThread.removeCallbacks(updateTask);
    	// Start an update if nothing happens after a few milliseconds
    	guiThread.postDelayed(updateTask, delayMillis);
    	
    }
    /** Modify text on thhe screen (called from another thread) */
    public void setTranslated(String text) {
    	guiSetText(transText, text);
    }
    
    /** All changes to the GUI must be done in the GUI thread */
    public void setRetranslated(String text) {
    	guiSetText(retransText, text);
    }
    
    /** All changes to the GUI must be done in the GUI thread */
    private void guiSetText(final TextView view, final String text) {
    	guiThread.post(new Runnable() {
    		public void run() {
    			view.setText(text);
    		}
    	});
    }
    
}