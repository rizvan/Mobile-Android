package org.scielo.search;


import android.content.Context;
//import android.graphics.Color;

import java.util.*;


import android.view.*;
import android.widget.*;

public class PaginationAdapter extends ArrayAdapter<Page> {

    int resource;
    
    public PaginationAdapter(Context _context, 
                             int _resource, 
                             List<Page> _items) {
        super(_context, _resource, _items);
        
        resource = _resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout resultView;

        Page item = getItem(position);
        
        if (convertView == null) {
            resultView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(inflater);
            vi.inflate(resource, resultView, true);
        } else {
            resultView = (LinearLayout) convertView;
        }
        //convertView.setBackgroundColor((position & 1) == 1 ? Color.WHITE : Color.LTGRAY);

        TextView pageView = (TextView)resultView.findViewById(R.id.page);
        
        pageView.setText(item.getLabel());
        //linkView.setText( item.getDocumentPDFLink());
        

        return resultView;
    }


}