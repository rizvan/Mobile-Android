package org.scielo.search;



import android.content.Context;
//import android.graphics.Color;

import java.util.*;


import android.view.*;
import android.widget.*;

public class DocumentAdapter extends ArrayAdapter<Document> {

    int resource;
    
    public DocumentAdapter(Context _context, 
                             int _resource, 
                             List<Document> _items) {
        super(_context, _resource, _items);
        
        resource = _resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout resultView;

        Document item = getItem(position);
        String abstr;
        
        if (convertView == null) {
            resultView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(inflater);
            vi.inflate(resource, resultView, true);
        } else {
            resultView = (LinearLayout) convertView;
        }
        //convertView.setBackgroundColor((position & 1) == 1 ? Color.WHITE : Color.LTGRAY);

        TextView positionView = (TextView)resultView.findViewById(R.id.TextViewDocumentPosition);
        TextView titleView = (TextView)resultView.findViewById(R.id.TextViewDocumentTitle);
        TextView authorsView = (TextView)resultView.findViewById(R.id.TextViewDocumentAuthors);
        TextView abstrView = (TextView)resultView.findViewById(R.id.TextViewDocumentAbstract);
        //TextView linkView = (TextView)resultView.findViewById(R.id.TextViewDocumentPDFLink);
        positionView.setText(item.getPosition());
        titleView.setText(item.getDocumentTitle());
        authorsView.setText(item.getDocumentAuthors());
        abstr = item.getDocumentAbstracts();
        if (abstr.length()>300){
        	abstr = abstr.substring(1, 300) + "...";
        }
        abstrView.setText(abstr);
        //linkView.setText( item.getDocumentPDFLink());
        

        return resultView;
      }
}