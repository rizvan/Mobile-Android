package org.scielo.search;

import java.util.ArrayList;


public class NumericPagination {
	// Parse to get translated text
	protected String strResultsTotal;
	
	protected String from;
	protected int selectedDocIndex;
	protected int selectedDocNumber;
	protected int docsPerPage;
	
	private ArrayList<Page> pagesList;
	protected static final String TAG = "Pagination";
	static final int MAX_CELLS = 6;
	static final int MAX = 4;
	int q = 0;
	
	NumericPagination(){
		from = "";
		strResultsTotal = "";
		docsPerPage = 0;
		selectedDocIndex = 0;
		
	}
	
	void setData(String from, String resultCount, int itemsPerPage){		
    	this.from = from;
    	this.strResultsTotal = resultCount;
    	
    	this.docsPerPage = itemsPerPage;   
    	if (from.length() == 0 || from.equals("0")) {
			selectedDocIndex = 0;
		} else {
			selectedDocIndex = Integer.parseInt(from) ;	
		}
    	q = (MAX_CELLS-2) * docsPerPage;
    	
	}
	
    
	public String getFrom() {
		return from;
	}
	public int getSelectedItemIndex() {
		return selectedDocIndex;
	}
	public int getItemsPerPage() {
		return docsPerPage;
	}
	public String getResultCount(){
		return this.strResultsTotal;
    }
	public ArrayList<Page> getPagesList(){
		return this.pagesList;
	}
	public int returnSelectedItemNumber(){
		return selectedDocIndex + 1;
	}
	public String returnNumberAsString(int n){
		return new Integer(n).toString();
	}
	public int returnStringAsNumber(String s){
		return Integer.parseInt(s);
	}
	/*
	public String returnSearchStartParameter(int index){		
		String r = "";
		int item = 0;
		
		
		if (index == 0){
			r = pagesList.get(index).getSearchKey();					
			if (r.equals("0")){
				
			} else {
				item = Integer.parseInt(r) - ((MAX - 1) * itemsPerPage ) - 1;
				r = new Integer(item).toString();
			}			
		} else {
			if (index == (MAX - 1)) {
				r = pagesList.get(index).getSearchKey();
			} else {
				r = pagesList.get(0).getSearchKey();
			}
		}
		return r;
	}
	public void generatePages(ArrayList<Page> pagesList){
		
		int i;
	    int k;
	    int max;
	    boolean stop = false;
	    String pText;
	    Page p;
		
	    
	    pagesList.clear();
		i = ((currentItem - 1 )/ itemsPerPage) + 1;
		
		max = i + MAX -1;
		while (!stop && (i<=max)){
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
	
	*/
	public String returnSearchStartParameter(int index){
		String start;

		start = "0";
		if (index >=0){
			if (pagesList!=null){
				if (pagesList.size()>0) {
					start = pagesList.get(index).getIndex2search();
				}
			}
		}
		
		return start;
	}
	
	public int returnFirstDocIndexOfThePage(int pageIndex){
		return Integer.parseInt(pagesList.get(pageIndex).getIndex2search());
	}
	
	public void generatePages(ArrayList<Page> pagesList){
		int prev;
		int next;

		int total;
		int pageIndex;
		int start = 0;

		int newPrevIndex = 0;
		boolean stop = false;
		boolean reload = false;
		
		int docindex = 0;
		Page p;
		String label = "";
		
		// CONTINUAR AQUI
		newPrevIndex = 0;
		total = Integer.parseInt(this.strResultsTotal);
		
		if (pagesList.size()>0){
			// valores antes de selecionar a página nova
			prev =  returnFirstDocIndexOfThePage(0);
		    next =  returnFirstDocIndexOfThePage(MAX_CELLS - 1);
		    if ((selectedDocIndex==prev) || (selectedDocIndex==next)){
		    	
			    start = selectedDocIndex;
			    reload = true;		    	
		    }
		} else {			
			reload = true;
			start = 0;
		}
		if (reload){
    		
    		stop = false;
    		pageIndex = 1;
    		pagesList.clear();    		    		
			label =  "";	
    		
		    if (start > 0){
    			label = "<";
    			newPrevIndex = start - q;
		    }

    		p = new Page( label, new Integer(newPrevIndex).toString());
			pagesList.add(p);
			pageIndex++;
    		
    		
    		docindex = start;
			while ((!stop) && (pageIndex <= MAX_CELLS)){
				
				if (pageIndex == MAX_CELLS){
					label = ">";
				} else {
					label = new Integer(docindex+1).toString();
				}
				p = new Page( label, new Integer(docindex).toString());					
				pagesList.add(p);
				pageIndex++;

				docindex = docindex + docsPerPage;					
				if ( docindex + 1 >= total) {
					stop = true;
				}
			}
    	}
		this.pagesList = pagesList;
	}
	
	
}
