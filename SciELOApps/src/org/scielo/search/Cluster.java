package org.scielo.search;

import java.util.ArrayList;
import java.util.HashMap;

public class Cluster {
	private HashMap<String,SearchFilter> list;
	private ArrayList<SearchFilter> array;
	private String id;
	
	Cluster(String _id){
		list = new HashMap<String,SearchFilter>();
		array = new ArrayList<SearchFilter>();
		id = _id;
	}
	
	public String getId(){
		return this.id;
	}
	public void addFilter(SearchFilter filter){	
		if (filter!=null){		
		   this.list.put(filter.getCode(),filter);
		   this.array.add(filter);
		}
	}
	public SearchFilter getFilterById(String id){		
		return this.list.get(id);
	}
	public SearchFilter getFilterByIndex(int index){		
		return this.array.get(index);
	}
	public SearchFilter getFilterBySubmenuId(int submenuId){		
		int i = 0;
		boolean found = false;
		SearchFilter sf = null;
		
		while (i < getFilterCount() && !found){
			if (getFilterByIndex(i).getSubmenuId() == submenuId){
				found = true;
				sf = getFilterByIndex(i);
			}			
			i++;
		}
		if (found){
			return sf;	
		} else {
			return null;
		}		
	}
	
	public int getFilterCount(){
		return this.list.size();
	}
	
}
