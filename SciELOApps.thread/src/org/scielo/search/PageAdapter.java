package org.scielo.search;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PageAdapter extends BaseAdapter {

	private ArrayList<TextView> views;
	private ArrayList<Page> page;

	public PageAdapter(Context c, int resource, ArrayList<Page> pagesList) {
		LinearLayout resultView = new LinearLayout(c);
        String inflater = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater vi = (LayoutInflater)c.getSystemService(inflater);
        vi.inflate(resource, resultView, true);
        
		TextView t;
		
	    page = pagesList;
	    views = new ArrayList<TextView>();
	    for (int i = 0; i < pagesList.size() ; i++){
	    	t = (TextView)resultView.findViewById(R.id.page);
	    	t.setText(pagesList.get(i).getLabel());
	        
	    }
	        
	}

	

	@Override
	public int getCount() {
	    return views.size();
	}

	@Override
	public Object getItem(int position) {
	    return views.get(position);
	}   

	@Override
	public long getItemId(int position) {
	    return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    return views.get(position);
	}

	public ArrayList<Page> getSeason() {
	    return page;
	}

	}