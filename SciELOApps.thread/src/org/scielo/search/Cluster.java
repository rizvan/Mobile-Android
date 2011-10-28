package org.scielo.search;


import java.util.ArrayList;
import java.util.HashMap;

public class Cluster {
	private HashMap<String,SearchFilter> values;
	//private HashMap<String,SearchFilter> menuId;
	private ArrayList<String> arrayId = new ArrayList<String>();
	private String id;
	
	Cluster(String _id){
		//menuId = new HashMap<String,SearchFilter>();
		values = new HashMap<String,SearchFilter>();		
		
		id = _id;
	}
	
	public String getId(){
		return this.id;
	}
	public void addFilter(SearchFilter filter, int menuId, String filterCode){	
		if (filter!=null){		
		   this.values.put(filterCode,filter);
		   arrayId.add(arrayId.size(), filterCode);		   
		   //this.menuId.put(new Integer(menuId).toString() ,filter);
		}
	}
	public SearchFilter getFilterById(String id){		
		return this.values.get(id);
	}
	public SearchFilter getFilterByIndex(int index){		
		return this.values.get(arrayId.get(index));
	}
	//public SearchFilter getFilterBySubmenuId(int submenuId){		
	//	return this.menuId.get(new Integer(submenuId).toString());
	//}
	public String display(){
		String r = "";
		SearchFilter filter;
		for (int i=0;i<arrayId.size();i++){
			filter = values.get(arrayId.get(i));
			r = r + new Integer( filter.getSubmenuId()).toString() + "; " ;
			r = r + filter.getClusterCode()+ "; " ;
			r = r + filter.getCode() + "; " ;
			r = r + filter.getCaption() + "; " ;
			r = r + "\n";
		}
		return r;
	}
	public int getFilterCount(){
		return this.values.size();
	}
	
}
