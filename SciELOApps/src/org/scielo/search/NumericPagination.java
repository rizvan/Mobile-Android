package org.scielo.search;

import java.util.ArrayList;


public class NumericPagination {
	// Parse to get translated text
	protected String resultCount;
	
	protected String from;
	protected int currentItem;
	protected int itemsPerPage;
	
	private ArrayList<Page> pagesList;
	protected static final String TAG = "Pagination";
	
	NumericPagination(){
		from = "";
		resultCount = "";
		itemsPerPage = 0;
		currentItem = 1;
	}
	
	
	
	void setData(String from, String resultCount, int itemsPerPage){		
    	this.from = from;
    	this.resultCount = resultCount;
    	this.itemsPerPage = itemsPerPage;   
    	if (from.length() == 0 || from.equals("0")) {
			currentItem = 1;
		} else {
			currentItem = Integer.parseInt(from);	
		}
	}
	
    
	public String getFrom() {
		return from;
	}
	public int getCurrentItem() {
		return currentItem;
	}
	public int getItemsPerPage() {
		return itemsPerPage;
	}
	public String getResultCount(){
		return this.resultCount;
    }
	public ArrayList<Page> getPagesList(){
		return this.pagesList;
	}
	public String getPageSearchKey(int index){		
		return pagesList.get(index).getSearchKey();
	}
	public void generatePages(ArrayList<Page> pagesList){
		
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
		this.pagesList = pagesList;
	}
	
	
}
