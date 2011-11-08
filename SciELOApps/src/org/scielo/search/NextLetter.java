package org.scielo.search;


public class NextLetter {
	// Parse to get translated text

	
	private IdAndValueObjects nextLetter;
	NextLetter(IdAndValueObjects letters){
		IdAndValue item ;
		this.nextLetter = new IdAndValueObjects();
    	int i =0;
    	for(i=0;i<letters.getCount()-1;i++){    		
    		item = new IdAndValue(letters.getItemByIndex(i).getId(), letters.getItemByIndex(i+1).getId());
    		this.nextLetter.add(item.getId(), item, false);
    	}
    	item = new IdAndValue(letters.getItemByIndex(i).getId(),letters.getItemByIndex(i).getId() + "ZZZZZZZZZZZZZZZZZZZZ");
		this.nextLetter.add(item.getId(), item, false);
    }

	String getLetter(String id){
		return this.nextLetter.getItem(id).getValue();
	}

}
