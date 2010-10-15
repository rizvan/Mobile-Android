package org.scielo.search;



import android.content.Context;
//import android.graphics.Color;

import java.util.*;


import android.view.*;
import android.widget.*;

public class ResultItemAdapter extends ArrayAdapter<SearchResult> {

    int resource;
    
    public ResultItemAdapter(Context _context, 
                             int _resource, 
                             List<SearchResult> _items) {
        super(_context, _resource, _items);
        
        resource = _resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout resultView;

        SearchResult item = getItem(position);
        
        if (convertView == null) {
            resultView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(inflater);
            vi.inflate(resource, resultView, true);
        } else {
            resultView = (LinearLayout) convertView;
        }
        //convertView.setBackgroundColor((position & 1) == 1 ? Color.WHITE : Color.LTGRAY);

        TextView titleView = (TextView)resultView.findViewById(R.id.TextViewDocumentTitle);
        TextView authorsView = (TextView)resultView.findViewById(R.id.TextViewDocumentAuthors);
        //TextView linkView = (TextView)resultView.findViewById(R.id.TextViewDocumentPDFLink);
          
        titleView.setText(item.getDocumentTitle());
        authorsView.setText(item.getDocumentAuthors());
        //linkView.setText( item.getDocumentPDFLink());
        

        return resultView;
      }
}