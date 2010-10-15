package org.scielo.search;

public class JournalCollections {
	String[] colCodes;
	String[] colNames;
	String[] colAppNames;
	String[] colURLs;
	
	JournalCollections(String[] _colCodes, String[] _colNames, String[]  _colAppNames, String[]  _colURLs){
		this.colCodes = _colCodes;
		this.colNames = _colNames;
		this.colAppNames = _colAppNames;
		this.colURLs = _colURLs;        
	}
	public int getIndex(String code){
		int i = -1;
		int r = -1;
		boolean found = false;
		
		while (!found && i<this.colCodes.length-1){
			i = i + 1;
			found = (code.equals(this.colCodes[i]));			
		}
		if (found) {
			r = i;
		}
		return r;
	}
	public String getCollectionName(String code){		
		return this.colNames[this.getIndex(code)];
	}
	public String getCollectionAppName(String code){
		return this.colAppNames[this.getIndex(code)];
	}
	public String getCollectionUrl(String code){
		return this.colURLs[this.getIndex(code)];
	}
}
