package org.scielo.search;


public class FollowingLetters {
	// Parse to get translated text

	IdAndValueObjects letters;
	IdAndValueObjects following;
	FollowingLetters(IdAndValueObjects letters){
		
		IdAndValue item ;
		
    	this.letters=letters;   
    	this.following = new IdAndValueObjects();
    	
    	int i =0;
    	for(i=0;i<letters.getCount()-1;i++){    		
    		item = new IdAndValue(letters.getItemByIndex(i).getId(), letters.getItemByIndex(i+1).getId());
    		this.following.add(item.getId(), item, false);
    	}
    	item = new IdAndValue(letters.getItemByIndex(i).getId(),letters.getItemByIndex(i).getId() + "ZZZZZZZZZZZZZZZZZZZZ");
		this.following.add(item.getId(), item, false);
    }

	String getFollowing(String id){
		return this.following.getItem(id).getValue();
	}

}
