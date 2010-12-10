package org.scielo.search;


public class Document {
	private String text;
	private String title;
	private String authors;
	private String link;
	private String id;
	private String position;
	private String colid;
	private String collection;
	private String abstracts;
	private String journalTitle;
	private String journalISSN;
	private String issueLabel;
	
	Document() {
		text = "";
		title = "";
        authors = "";
    	link = "";
    	id = "";
    	position = "";
    	colid = "";
    	collection = "";
    	abstracts = "";
    	journalTitle = "";
    	journalISSN = "";
    	issueLabel = "";
	}
	
	
	public String getText(){
		return this.title + "\n" + this.authors + "\n" + this.link;
	}
	public void setDocumentId(String text){
		this.id = text;
	}
	public void setPosition(String text){
		this.position = text;
	}
	public String getPosition(){
		return this.position;
	}
	public String getDocumentId(){
		return this.id;
	}
	public void setCollectionId(String text){
		this.colid = text;
	}
	public String getCollectionId(){
		return this.colid;
	}
	public void setDocumentTitle(String text){
		this.title = text;
	}
	public String getDocumentTitle(){
		return this.title;
	}
	public void setDocumentAuthors(String text){
		this.authors = text;
	}
	public String getDocumentAuthors(){
		return this.authors;
	}
	public void setDocumentPDFLink(String text){
		this.link = text;
	}
	public String getDocumentPDFLink(){
		return this.link;
	}
	public void setDocumentAbstracts(String text){
		this.abstracts = text;
	}
	public String getDocumentAbstracts(){
		return this.abstracts;
	}
	public void setDocumentCollection(String text){
		this.collection = text;
	}
	public String getDocumentCollection(){
		return this.collection;
	}
	public void setJournalTitle(String text){
		this.journalTitle = text;
	}
	public String getJournalTitle(){
		return this.journalTitle;
	}
	public void setJournalISSN(String text){
		this.journalISSN = text;
	}
	public String getJournalISSN(){
		return this.journalISSN;
	}
	public void setIssueLabel(String text){
		this.issueLabel = text;
	}
	public String getIssueLabel(){
		return this.issueLabel;
	}
	@Override
	  public String toString() {	    
	    return this.text;
	  }
}
