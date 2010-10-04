package org.scielo.search;

import java.util.ArrayList;

public class SearchFilterGroupList {
	private ArrayList<SearchFilterGroup> list;
	private ArrayList<SearchFilter> sorted;
	
	SearchFilterGroupList(ArrayList<SearchFilterGroup> _list, ArrayList<SearchFilter> _sorted){
		this.list = _list;
		this.sorted = _sorted;
	}
	
	public void add(SearchFilterGroup filterList){
		this.list.add(filterList);
		for (int i=0;i<filterList.length();i++){
			this.sorted.add(filterList.getItem(i).getId(), filterList.getItem(i));	
		}
		
	}
	public SearchFilterGroup getItem(int index){		
		return this.list.get(index);
	}
	public int length(){
		return this.list.size();
	}
	public void clear(){
		this.list.clear();
		this.sorted.clear();
	}
	public SearchFilter getSearchFilter(int id){
		return this.sorted.get(id);
	}
	/*
	public SearchFilter getSearchFilter(int id){
		int i = 0;
		int j = 0;
		boolean found = false;
		SearchFilter sf = null;
		
		while (i< length() && !found){
			while (j< length() && !found){
				sf = this.getItem(i).getItem(j);
				if (sf.getId() == id){
					found = true;
				}
				j++;
			}
			i++;
		}
		if (found){
			return sf;	
		} else {
			return null;
		}		
	}*/
}
