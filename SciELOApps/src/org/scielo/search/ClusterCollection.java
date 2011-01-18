package org.scielo.search;

import java.util.ArrayList;
import java.util.HashMap;

import android.util.Log;

public class ClusterCollection {
	private HashMap<String,Cluster> clusterValues;
	private ArrayList<Cluster> sortedClusters;
	
	ClusterCollection(){
		clusterValues = new HashMap<String,Cluster>();	
		sortedClusters = new ArrayList<Cluster>();		
	}
	
	public boolean add(Cluster cluster){
		boolean r = false;
		Log.d("ClusterCollection.addCluster", "inicio" );
    	if (cluster != null){
    		Log.d("ClusterCollection.addCluster", "clusterValues.add" );
        	
    		this.clusterValues.put(cluster.getId(),cluster);
    		sortedClusters.add(cluster);
    		r = true;
    	}
    	return r;
	}
	public Cluster getItemById(String id){		
		return clusterValues.get(id);
	}
	public Cluster getItemByIndex(int index){		
		return sortedClusters.get(index);
	}
	public int getCount(){
		return this.clusterValues.size();
	}
	public void clear(){
		this.clusterValues.clear();
		this.sortedClusters.clear();
	}
	
	
	public SearchFilter getFilterBySubmenuId(int submenuId, String test){
		int i = 0;
		boolean found = false;
		SearchFilter sf = null;
		test = "";
		
		while (i < getCount() && !found){
			sf = getItemByIndex(i).getFilterBySubmenuId(submenuId);
			test = test + getItemByIndex(i).display();
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
