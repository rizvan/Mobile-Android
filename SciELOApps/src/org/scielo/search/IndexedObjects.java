package org.scielo.search;

import java.util.ArrayList;
import java.util.HashMap;

public class IndexedObjects {
	private HashMap<String,Object> objects;
	private ArrayList<String> arrayId = new ArrayList<String>();
	
	IndexedObjects(){
		objects = new HashMap<String,Object>();
		arrayId = new ArrayList<String>();
	}
	public void add(String id, Object obj){
		objects.put(id, obj);
		arrayId.add(arrayId.size(), id);
	}
	public Object getItemById(String id){
		return objects.get(id);
	}
	public Object getItemByIndex(int index){
		return objects.get(arrayId.get(index));
	}
	public int getCount(){
		return objects.size();
	}
	
	
}
