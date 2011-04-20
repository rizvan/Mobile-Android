package org.scielo.search;

public class Page {
	private String label;
	private String searchkey;
	
	Page(String _label, String _searchkey){
		this.label = _label;
		this.searchkey = _searchkey;
	}
	public String getLabel(){
		return  this.label ;
	}
	public String getIndex2search(){
		return this.searchkey;
	}
}
