package org.scielo.search;



import android.content.Context;
//import android.graphics.Color;

import java.util.*;


import android.view.*;
import android.widget.*;

public class JournalAdapter extends ArrayAdapter<Journal> {

    int resource;
    
    public JournalAdapter(Context _context, 
                             int _resource, 
                             List<Journal> _items) {
        super(_context, _resource, _items);
        
        resource = _resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout resultView;

        Journal item = getItem(position);
        
        if (convertView == null) {
            resultView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(inflater);
            vi.inflate(resource, resultView, true);
        } else {
            resultView = (LinearLayout) convertView;
        }
        //convertView.setBackgroundColor((position & 1) == 1 ? Color.WHITE : Color.LTGRAY);

        TextView posView = (TextView)resultView.findViewById(R.id.TextViewPosition);
        posView.setText(item.getPosition());
        TextView titleView = (TextView)resultView.findViewById(R.id.TextViewTitle);
        titleView.setText(item.getTitle());
        TextView ISSNView = (TextView)resultView.findViewById(R.id.TextViewISSN);
        ISSNView.setText(item.getId());
        TextView colView = (TextView)resultView.findViewById(R.id.TextViewCollection);
        colView.setText(item.getCollection());
        TextView subjectView = (TextView)resultView.findViewById(R.id.TextViewSubjects);
        subjectView.setText(item.getSubjects());
        

        return resultView;
      }
}