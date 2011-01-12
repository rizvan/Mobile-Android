package org.scielo.search;


import android.content.Context;

import java.util.*;


import android.view.*;
import android.widget.*;


public class ButtonPageAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<Page> pagesList;
	private int selectedButton=0;
	
	
	// Gets the context so it can be used later
	public ButtonPageAdapter(Context c) {
		mContext = c;
	}

	public String getPageSelected(){
		String r = "1";
		if (pagesList.size()>0){
			r = pagesList.get(selectedButton).getLabel();
		}
		return r;
	}
	public void setList(ArrayList<Page> pagesList){
		this.pagesList = pagesList;
	}
	// Total number of things contained within the adapter
	public int getCount() {
		return pagesList.size();
	}

	// Require for structure, not really used in my code.
	public Page getItem(int position) {
		return pagesList.get(position);
	}

	// Require for structure, not really used in my code. Can
	// be used to get the id of an item in the adapter for 
	// manual control. 
	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		Button btn;
		//Page item = getItem(position);
		if (convertView == null) {  
			// if it's not recycled, initialize some attributes
			btn = new Button(mContext);			
			btn.setOnClickListener(new View.OnClickListener() {

			      @Override
			      public void onClick(View view) {
			    	  //Log.d("onClick","position ["+position+"]");
			    	  
			    	  selectedButton = (int)view.getId();
			      }

			    });

		} 
		else {
			btn = (Button) convertView;
		}
	  
		btn.setText(getItem(position).getLabel()); 
		// filenames is an array of strings
		//btn.setTextColor(Color.WHITE);
		//btn.setBackgroundResource(R.drawable.button);
		btn.setId(position);
      
		// filenames is an array of strings
	  
	  return btn;
	 }
}
