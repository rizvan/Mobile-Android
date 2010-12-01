package org.scielo.search;

import java.util.ArrayList;
import java.util.HashMap;

import android.util.Log;

public class ClusterCollection {
	private HashMap<String,Cluster> list;
	private ArrayList<Cluster> sorted;
	
	ClusterCollection(){
		list = new HashMap<String,Cluster>();	
		sorted = new ArrayList<Cluster>();		
	}
	
	public boolean add(Cluster cluster){
		boolean r = false;
		Log.d("ClusterCollection.addCluster", "inicio" );
    	if (cluster != null){
    		Log.d("ClusterCollection.addCluster", "list.add" );
        	
    		this.list.put(cluster.getId(),cluster);
    		sorted.add(cluster);
    		r = true;
    	}
    	return r;
	}
	public Cluster getItemById(String id){		
		return list.get(id);
	}
	public Cluster getItemByIndex(int index){		
		return sorted.get(index);
	}
	public int getCount(){
		return this.list.size();
	}
	public void clear(){
		this.list.clear();
		this.sorted.clear();
	}
	
	
	public SearchFilter getFilterBySubmenuId(int submenuId){
		int i = 0;
		boolean found = false;
		SearchFilter sf = null;
		
		while (i < getCount() && !found){
			sf = getItemByIndex(i).getFilterBySubmenuId(submenuId);
			found = (sf != null);
			i++;
		}
		if (found){
			return sf;	
		} else {
			return null;
		}		
	}
	
}
