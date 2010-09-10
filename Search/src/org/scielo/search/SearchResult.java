package org.scielo.search;


public class SearchResult {
	private String text;
	
	SearchResult() {
		
	}
	
	public void setText(String text){
		this.text = text;
	}
	public String getText(){
		return this.text;
	}
	
	@Override
	  public String toString() {	    
	    return this.text;
	  }
}
