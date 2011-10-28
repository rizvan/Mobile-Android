package org.scielo.search;


public class JournalsCollectionsNetwork {
	private IndexedObjects indexed;

	JournalsCollectionsNetwork(){	
		indexed = new IndexedObjects();
	}
	
	public void multiAdd(String[] _colCodes, String[] _colNames, String[]  _colAppNames, String[]  _colURLs){
		JournalsCollection item;
		for(int i=0; i < _colCodes.length;i++){
			item = new JournalsCollection();
			item.setId(_colCodes[i]);
			item.setName(_colNames[i]);
			item.setNickname(_colAppNames[i]);
			item.setUrl(_colURLs[i]);
			add(item.getId(), item);
		}
	}

	public void add(String id, JournalsCollection item){			
		indexed.add(id, item);
	}
	public JournalsCollection getItem(String id){		
		JournalsCollection item;
		
		item = (JournalsCollection)indexed.getItemById(id);
		if (item==null){
			item = new JournalsCollection();
			item.setId(id);
			item.setName(id);
			item.setNickname(id);
			item.setUrl("");
			add(item.getId(), item);
		}
		return item;
	}
	public JournalsCollection getItemByIndex(int index) {
		// TODO Auto-generated method stub
		return (JournalsCollection)indexed.getItemByIndex(index);
	}
	public int getCount(){
		return indexed.getCount();
	}
	
}
