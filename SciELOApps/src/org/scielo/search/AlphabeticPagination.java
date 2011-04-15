package org.scielo.search;

import java.util.ArrayList;


public class AlphabeticPagination {
	// Parse to get translated text
	private String letters;
	
	private IdAndValueObjects currentAndNextLetters = new IdAndValueObjects();

	private ArrayList<Page> localPagesList;

	
	AlphabeticPagination(){
    	this.letters="";   
    	currentAndNextLetters = new IdAndValueObjects();
    }

	public void resetLetters(){
		letters = "";
	}
	public void loadPagesList(ArrayList<Page> pagesList){
		int i;	    
	    Page p;
	    IdAndValue item ;
	    
	    letters = letters + letters.substring(letters.length()-1) + String.valueOf((char)91);
		String[] l = letters.substring(1).split(","); 
	    pagesList.clear();
	    for (i = 0; i < (l.length-1); i++){
	    	p = new Page( l[i],  l[i]);
	    	pagesList.add(p);
	    	
	    	item = new IdAndValue(l[i], l[i+1]);
	    	currentAndNextLetters.add(l[i], item, false);
	    	
	    }
	    this.localPagesList = pagesList;
	}
	public void addLetter(String letter){
		if (!letters.contains("," + letter )) {
			letters = letters + "," + letter ;
			
		}
	}
	
	public IdAndValue getLetterInfo(int index ){
		
		String currentLetter = localPagesList.get(index).getSearchKey();
		String nextLetter = currentAndNextLetters.getItem( currentLetter ).getValue();
		IdAndValue item = new IdAndValue(currentLetter, nextLetter);
		return item;
	}
	public String getNextLetter(String id ){
		
		return currentAndNextLetters.getItem( id ).getValue();
		
	}
	
	public void generateLetters(){
		String letter;
		
		letters = "";
		for (int i=65;i<91;i++){
			letter = String.valueOf((char)i);
			
			addLetter(letter);
		}
	}

}
