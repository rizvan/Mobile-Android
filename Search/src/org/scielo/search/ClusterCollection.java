package org.scielo.search;

import java.util.ArrayList;

import android.util.Log;

public class ClusterCollection {
	private ArrayList<Cluster> list;
	//private ArrayList<SearchFilter> sorted;
	
	ClusterCollection(ArrayList<Cluster> _list){
		
		//, ArrayList<SearchFilter> _sorted
		this.list = _list;
		//this.sorted = _sorted;
	}
	
	public boolean addCluster(Cluster cluster){
		boolean r = false;
		Log.d("ClusterCollection.addCluster", "inicio" );
    	if (cluster != null){
    		Log.d("ClusterCollection.addCluster", "list.add" );
        	
    		this.list.add(cluster);
    		
    		r = true;
    	}
    	return r;
	}
	public Cluster getCluster(int index){		
		return this.list.get(index);
	}
	public int getClusterCount(){
		return this.list.size();
	}
	public void clear(){
		this.list.clear();
		//this.sorted.clear();
	}
	/*
	public SearchFilter getFilterBySubmenuId(int submenuId){
		return this.sorted.get(submenuId);
	}*/
	
	public SearchFilter getFilterById(int submenuId){
		int i = 0;
		boolean found = false;
		SearchFilter sf = null;
		
		while (i< getClusterCount() && !found){
			sf = getCluster(i).getFilterById(submenuId);
			found = (sf != null);
			i++;
		}
		if (found){
			return sf;	
		} else {
			return null;
		}		
	}
	public Cluster getClusterByCode(String code){
		int i = 0;
		boolean found = false;
		Cluster cluster = null;
		
		while (i< getClusterCount() && !found){
			cluster = getCluster(i);
			found = (cluster.getCode().equals(code));
			i++;
			Log.d("xx", cluster.getCode() + " " + code);
		}
		if (found){
			return cluster;	
		} else {
			return null;
		}		
	}
}
