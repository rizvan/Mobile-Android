package org.scielo.search;

import java.util.ArrayList;


public class AlphabeticPagination {
	// Parse to get translated text
	private ArrayList<Page> pagesList;

	private String letters;
	
	AlphabeticPagination(){
    	this.letters="";    	
    }

	public ArrayList<Page> getPagesList(){
		return this.pagesList;
	}
	public void generatePages(ArrayList<Page> pagesList){
		int i;	    
	    Page p;
		String[] l = letters.substring(1).split(","); 
	    pagesList.clear();
	    for (i = 0; i < l.length; i++){
	    	p = new Page( l[i],  l[i]);
	    	pagesList.add(p);
	    }
	}
	public void addLetter(String letter){
		if (!letters.contains("," + letter )) {
			letters = letters + "," + letter ;
		}
	}

}
