package org.scielo.search;

import java.util.HashMap;

public class PairsList {
	private HashMap<String,Pair> pairs;
	//private String defaultID;
	/*
	 ids = getResources().getStringArray(R.array.subject_id);
	 names = getResources().getStringArray(R.array.subject_name);
		
	 */
	PairsList(String[] idArray, String[] nameArray){
		Pair item;
		
		pairs = new HashMap<String,Pair>();
		
		for(int i=0; i < idArray.length;i++){
			item = new Pair( idArray[i].toLowerCase().replace(" ","_"), nameArray[i]);
			pairs.put(item.getId(), item);
		}
			  
	}
	public Pair getItem(String id){
		String newId;
		newId = id.toLowerCase().replace(" ","_");
		Pair r = pairs.get(newId);
		if (r == null){
			r = new Pair(newId, id);
			pairs.put(newId, r);			
		}
		return r;
	}
	public void add(String id, Pair item){
		pairs.put(id.toLowerCase().replace(" ","_"), item);
	}
	
}
