package org.scielo.search;


public class Document {
	private String text;
	private String title;
	private String authors;
	private String url;
	private String pdf_url;
	private String html_url;
	private String id;
	private String position;
	private String abstracts;
	private String journalTitle;
	private String journalISSN;
	private String issueLabel;
	private JournalsCollection col;
	private String filename; 
	private String lang;
	private String compl;
	private String pubDate;
	
	

	Document() {
		text = "";
		title = "";
        authors = "";
    	url = "";
    	id = "";
    	position = "";
    	pdf_url = "";
    	html_url = "";
    	
    	abstracts = "";
    	journalTitle = "";
    	journalISSN = "";
    	issueLabel = "";
    	filename = "";
    	lang = "";
    	compl = "";
    	pubDate = "";
    	setCol(new JournalsCollection());
    	
	}
	
	
	public String getText(){
		return this.title + "\n" + this.authors + "\n" + this.url;
	}
	public void setDocumentId(String text){
		this.id = text;
	}
	public void setPubDate(String text){
		this.pubDate = text;
	}
	public String getPubDate(){
		return this.pubDate;
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
	public void setDocumentURL(String text){
		this.url = text;
	}
	public String getDocumentURL(){
		return this.url;
	}
	public void setDocumentAbstracts(String text){
		this.abstracts = text;
	}
	public String getDocumentAbstracts(){
		return this.abstracts;
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


	public void setPdf_url(String pdf_url) {
		this.pdf_url = pdf_url;
	}


	public String getPdf_url() {
		return pdf_url;
	}


	public void setHtml_url(String html_url) {
		this.html_url = html_url;
	}


	public String getHtml_url() {
		return html_url;
	}


	public void setCol(JournalsCollection col) {
		this.col = col;
	}


	public JournalsCollection getCol() {
		return col;
	}


	public void setFilename(String filename) {
		this.filename = filename;
	}


	public String getFilename() {
		return filename;
	}


	public void setLang(String lang) {
		this.lang = lang;
	}


	public String getLang() {
		return lang;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getAuthors() {
		return authors;
	}


	public void setAuthors(String authors) {
		this.authors = authors;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getAbstracts() {
		return abstracts;
	}


	public void setAbstracts(String abstracts) {
		this.abstracts = abstracts;
	}


	public void setText(String text) {
		this.text = text;
	}


	public void setCompl(String string) {
		// TODO Auto-generated method stub
		this.compl = string;
	}	
	public String getCompl(){
		return this.compl;
	}
}
