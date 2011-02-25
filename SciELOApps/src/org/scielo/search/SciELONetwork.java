package org.scielo.search;


public class SciELONetwork {
	private IndexedObjects indexed;

	SciELONetwork(){	
		indexed = new IndexedObjects();
	}
	
	public void multiAdd(String[] _colCodes, String[] _colNames, String[]  _colAppNames, String[]  _colURLs){
		SciELOCollection item;
		for(int i=0; i < _colCodes.length;i++){
			item = new SciELOCollection();
			item.setId(_colCodes[i]);
			item.setName(_colNames[i]);
			item.setNickname(_colAppNames[i]);
			item.setUrl(_colURLs[i]);
			add(item.getId(), item);
		}
	}

	public void add(String id, SciELOCollection item){			
		indexed.add(id, item);
	}
	public SciELOCollection getItem(String id){		
		SciELOCollection item;
		
		item = (SciELOCollection)indexed.getItemById(id);
		if (item==null){
			item = new SciELOCollection();
			item.setId(id);
			item.setName(id);
			item.setNickname(id);
			item.setUrl("");
			add(item.getId(), item);
		}
		return item;
	}
	public SciELOCollection getItemByIndex(int index) {
		// TODO Auto-generated method stub
		return (SciELOCollection)indexed.getItemByIndex(index);
	}
	public int getCount(){
		return indexed.getCount();
	}
	
}
