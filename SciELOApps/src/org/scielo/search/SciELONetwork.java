package org.scielo.search;

import java.util.HashMap;

public class SciELONetwork {
	
	
	private HashMap<String,SciELOCollection> network;
	/*
	 ids = getResources().getStringArray(R.array.subject_id);
	 names = getResources().getStringArray(R.array.subject_name);
		
	 */
	SciELONetwork(String[] _colCodes, String[] _colNames, String[]  _colAppNames, String[]  _colURLs){
		SciELOCollection item;
		network = new HashMap<String,SciELOCollection>();
		
		for(int i=0; i < _colCodes.length;i++){
			item = new SciELOCollection();
			item.setId(_colCodes[i]);
			item.setName(_colNames[i]);
			item.setNickname(_colAppNames[i]);
			item.setUrl(_colURLs[i]);
			network.put(_colCodes[i], item);
		}
			  
	}
	public SciELOCollection getItem(String id){
		return network.get(id);
	}

	
	
}
