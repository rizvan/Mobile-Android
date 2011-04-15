package org.scielo.search;


import java.util.ArrayList;
import java.util.HashMap;

import android.util.Log;

public class ClusterCollection {
	private HashMap<String,Cluster> clusterById;
	private ArrayList<String> arrayId = new ArrayList<String>();
	private HashMap<String,SearchFilter> filterByMenuId;
	
	
	ClusterCollection(){
		clusterById = new HashMap<String,Cluster>();	
		filterByMenuId = new HashMap<String,SearchFilter>();	
			
	}
	
	public boolean add(Cluster cluster){
		boolean r = false;
		Log.d("ClusterCollection.addCluster", "inicio" );
    	if (cluster != null){
    		Log.d("ClusterCollection.addCluster", "clusterValues.add" );        	    		
    		this.clusterById.put(cluster.getId(),cluster);    		
    		arrayId.add(arrayId.size(), cluster.getId());
    		
    		SearchFilter sf = null;
    		for (int i=0;i<cluster.getFilterCount();i++){
    			sf = cluster.getFilterByIndex(i);
    			
    			filterByMenuId.put( new Integer(sf.getSubmenuId()).toString(), sf);
    		}
    		r = true;
    	}
    	return r;
	}
	public Cluster getItemById(String id){		
		return clusterById.get(id);
	}
	public Cluster getItemByIndex(int index){		
		return clusterById.get(arrayId.get(index));
	}
	public int getCount(){
		return this.clusterById.size();
	}
	public void clear(){
		this.clusterById.clear();
		arrayId.clear();
		filterByMenuId.clear();
	}
	
	
	public SearchFilter getFilterBySubmenuId(int submenuId, String test){		
		return filterByMenuId.get(new Integer(submenuId).toString());
	}
	
}
