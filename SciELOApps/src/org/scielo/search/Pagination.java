package org.scielo.search;

import java.util.ArrayList;


public class Pagination {
	// Parse to get translated text
	protected String resultCount;
	protected String from;
	protected int currentItem;
	protected int itemsPerPage;
	
	protected static final String TAG = "SearchServiceData";
	
    Pagination(){		    	
    }
    public void loadData(String from, String resultCount, int currentItem, int itemsPerPage){		
    	this.from = from;
    	this.resultCount = resultCount;
    	this.currentItem = currentItem;
    	this.itemsPerPage = itemsPerPage;    	
	}
	
	public String getResultCount(){
		return this.resultCount;
	}
	
	public void generatePageList(ArrayList<Page> pagesList){
		int i;
	    int k;
	    boolean stop = false;
	    String pText;
	    Page p;
		
	    pagesList.clear();
		i = 1;
		while (!stop && (i<=6)){
			k = i * itemsPerPage;
			pText = new Integer( k - itemsPerPage + 1).toString();
			if ( Integer.parseInt(this.resultCount) > k) {
			} else {
				stop = true;
			}
			p = new Page( pText, pText);
			pagesList.add(p);

			i++;
		}
		
	}
	
	
}
