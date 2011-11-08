package org.scielo.search;

import android.app.Activity;
import android.app.ProgressDialog;


public abstract class MyActivity   extends Activity  {

	protected ProgressDialog dialog;
	protected boolean resultIsReady = false;
	
	
	public abstract void  setResult(String text);
	
	
	public void showProgressDialog(){
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			if (!resultIsReady){
    			dialog.show();
    		}
		}
}
