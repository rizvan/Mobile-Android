package org.scielo.search;

public class SearchFilter {
	private int id;
	private String name;
	private String count;
	
	SearchFilter(int _id, String _name, String _count) {
		this.id = _id;
		this.name = _name;
		this.count = _count;
	}
	
	
	public int getId(){
		return this.id;
	}
	public String getName(){
		return this.name;
	}
	public String getCount(){
		return this.count;
	}
	
}
