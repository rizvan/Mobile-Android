package org.scielo.search;


public class IdAndValueObjects {
	private IndexedObjects indexed;
	private boolean normalized;
	
	IdAndValueObjects(){
		indexed = new IndexedObjects();
	}
	public void multiAdd(String[] idArray, String[] nameArray, boolean normalizeId ){
		IdAndValue item;
		for(int i=0; i < idArray.length;i++){
			item = new IdAndValue(idArray[i], nameArray[i]);
			add(item.getId(), item, normalizeId);
		}
		normalized = normalizeId;
	}
	public int getCount(){
		return indexed.getCount();
	}
	public IdAndValue getItem(String id){
		IdAndValue item;
		
		item = (IdAndValue)indexed.getItemById(id);
		if (item == null){
			item = new IdAndValue(id, id);
			add(item.getId(), item, normalized);
		}
		
		return item;
	}
	public void add(String id, IdAndValue item, boolean normalizeId){
		if (normalizeId){
			id = id.toLowerCase().replace(" ","_");
			item.setId(id);
		}		
		indexed.add(id, item);
	}
	public IdAndValue getItemByIndex(int index) {
		// TODO Auto-generated method stub
		return (IdAndValue)indexed.getItemByIndex(index);
	}
	
}
