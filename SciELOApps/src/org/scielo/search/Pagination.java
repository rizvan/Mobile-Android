package org.scielo.search;

import java.util.ArrayList;


public class Pagination {
	// Parse to get translated text
	protected String resultCount;
	
	protected String from;
	protected int currentItem;
	protected int itemsPerPage;
	private int type;
	private ArrayList<Page> pagesList;
	protected static final String TAG = "Pagination";
	private String letters;
	
    Pagination(){		    	
    }
    public void loadData(String from, String resultCount, int itemsPerPage, int type){		
    	this.from = from;
    	this.resultCount = resultCount;
    	this.itemsPerPage = itemsPerPage;   
    	this.type = type;
    	if (from.length() == 0 || from.equals("0")) {
			currentItem = 1;
		} else {
			currentItem = Integer.parseInt(from);	
		}
    	this.letters="";
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
	public int getType(){
		return this.type;
    }
	public ArrayList<Page> getPagesList(){
		return this.pagesList;
	}
	public String getPageSearchKey(int index){
		String r="";
		if (index < this.pagesList.size()){
			r = pagesList.get(index).getSearchKey();
		} else {
			if (this.type==2){
				r = "{}";
			}
		}
			
		return r;
	}
	public void generatePages(ArrayList<Page> pagesList){
		switch (type){
		case 1:
			generateNumericPages(pagesList);
			break;
		case 2:
			generateAlphabeticPages(pagesList);
			break;
		}
		this.pagesList = pagesList;
	}
	
	public void generateNumericPages(ArrayList<Page> pagesList){
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
	public void addLetter(String letter){
		if (!letters.contains("," + letter )) {
			letters = letters + "," + letter ;
		}
	}
	public void generateAlphabeticPages(ArrayList<Page> pagesList){
		int i;	    
	    Page p;
		String[] l = letters.substring(1).split(","); 
	    pagesList.clear();
	    for (i = 0; i < l.length; i++){
	    	p = new Page( l[i],  l[i]);
	    	pagesList.add(p);
	    }
	}
	
}
