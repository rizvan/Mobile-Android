package org.scielo.search;

public class Journal {
	private String id;
	private String title;
	private String collection;
	private String collectionId;
	private String subjects;
	private String position;
	public Journal() {
		super();
		id = "";
		title = "";
		collection ="";
		collectionId = "";
		subjects = "";
		position = "";
	}
	
	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getSubjects() {
		return subjects;
	}
	public void setSubjects(String subjects) {
		this.subjects = subjects;
	}
	public String getCollectionName() {
		return collection;
	}
	public void setCollectionName(String collection) {
		this.collection = collection;
	}
	public String getCollectionId() {
		return collectionId;
	}
	public void setCollectionId(String collectionId) {
		this.collectionId = collectionId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
}
