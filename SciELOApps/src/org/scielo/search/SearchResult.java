package org.scielo.search;


public class SearchResult {
	private String text;
	private String title;
	private String authors;
	private String link;
	private String id;
	SearchResult() {
		
	}
	
	
	public String getText(){
		return this.title + "\n" + this.authors + "\n" + this.link;
	}
	public void setDocumentId(String text){
		this.id = text;
	}
	public String getDocumentId(){
		return this.id;
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
	
	@Override
	  public String toString() {	    
	    return this.text;
	  }
}
