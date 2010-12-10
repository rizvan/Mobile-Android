package org.scielo.search;

public class Pair {
    private String id;
    private String value;
	public Pair(String id, String value) {
		super();
		this.id = id;
		this.value = value;
	}
    public String getId(){
    	return id;
    }
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public void setId(String id) {
		this.id = id;
	}
}
