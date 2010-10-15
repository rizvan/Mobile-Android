package org.scielo.search;

import java.util.ArrayList;

public class Cluster {
	private ArrayList<SearchFilter> list;
	private String code;
	
	Cluster(ArrayList<SearchFilter> _list, String _code){
		this.list = _list;
		this.code = _code;
	}
	
	public String getCode(){
		return this.code;
	}
	public void addFilter(SearchFilter filter){	
		if (filter!=null){		
		   this.list.add(filter);
		}
	}
	public SearchFilter getFilter(int index){		
		return this.list.get(index);
	}
	public int getFilterCount(){
		return this.list.size();
	}
	public SearchFilter getFilterById(int id){
		int j = 0;
		boolean found = false;
		SearchFilter sf = null;
		
		j = 0;
		while (j < getFilterCount() && !found){
			sf = getFilter(j);
			if (sf.getSubmenuId() == id){
				found = true;
			}
			j++;
		}
		if (found){
			return sf;	
		} else {
			return null;
		}		
	}
}
