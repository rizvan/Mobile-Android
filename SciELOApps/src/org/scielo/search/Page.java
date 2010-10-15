package org.scielo.search;

public class Page {
	private String label;
	private String position;
	
	Page(String _label, String _position){
		this.label = _label;
		this.position = _position;
	}
	public String getLabel(){
		return this.label;
	}
	public String getPosition(){
		return this.position;
	}
}
