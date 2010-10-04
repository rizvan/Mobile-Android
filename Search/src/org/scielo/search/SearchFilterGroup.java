package org.scielo.search;

import java.util.ArrayList;

public class SearchFilterGroup {
	private ArrayList<SearchFilter> list;
	private int id;
	private String filterGroupName;
	
	SearchFilterGroup(ArrayList<SearchFilter> _list, int _menuId, String _filterGroupName){
		this.list = _list;
		this.id = _menuId;
		this.filterGroupName = _filterGroupName;
	}
	public int getMenuId(){
		return this.id;
	}
	public String getFilterGroupName(){
		return this.filterGroupName;
	}
	public void add(SearchFilter filter){		
		this.list.add( filter);
	}
	public SearchFilter getItem(int index){		
		return this.list.get(index);
	}
	public int length(){
		return this.list.size();
	}
	
}
