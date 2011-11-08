package org.scielo.search;



import android.content.Context;
//import android.graphics.Color;

import java.util.*;


import android.view.*;
import android.widget.*;

public class DocumentsListAdapter extends ArrayAdapter<Document> {

    int resource;
    private boolean isTOC = false;
    

	public DocumentsListAdapter(Context _context, 
                             int _resource, 
                             List<Document> _items, boolean isTOC) {
        super(_context, _resource, _items);
        this.isTOC = isTOC;
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
        TextView titleView = (TextView)resultView.findViewById(R.id.TextViewDocumentTitle);
        TextView authorsView = (TextView)resultView.findViewById(R.id.TextViewDocumentAuthors);
        
        
        if (! this.isTOC){
        	TextView positionView = (TextView)resultView.findViewById(R.id.TextViewDocumentPosition);
            TextView abstrView = (TextView)resultView.findViewById(R.id.TextViewDocumentAbstract);
            TextView colView = (TextView)resultView.findViewById(R.id.TextViewDocumentCollection);
            TextView issueView = (TextView)resultView.findViewById(R.id.TextViewIssue);
            
            colView.setText(item.getCol().getName());
            issueView.setText(item.getIssueLabel());
            positionView.setText(item.getPosition());
            abstr = item.getDocumentAbstracts();
            if (abstr.length()>300){
            	abstr = abstr.substring(1, 300) + "...";
            }
            abstrView.setText(abstr);
        }
        
        titleView.setText(item.getDocumentTitle());
        authorsView.setText(item.getDocumentAuthors());
        //linkView.setText( item.getDocumentPDFLink());
        

        return resultView;
      }
}