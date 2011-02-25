package org.scielo.search;

public class Page {
	private String label;
	private String searchkey;
	
	Page(String _label, String _searchkey){
		this.label = _label;
		this.searchkey = _searchkey;
	}
	public String getLabel(){
		return "[ " + this.label + " ]";
	}
	public String getSearchKey(){
		return this.searchkey;
	}
}
