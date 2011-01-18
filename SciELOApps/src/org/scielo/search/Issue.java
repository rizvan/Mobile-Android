package org.scielo.search;

public class Issue {
	private String id;
	private Journal journal;
	private String volume;
	private String number;
	private String suppl;
	private String date;
	
	Issue(){
		id = "";
		journal = new Journal();
		volume = "";
		number = "";
		suppl = "";
		date = "";
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Journal getJournal() {
		return journal;
	}
	public void setJournal(Journal journal) {
		this.journal = journal;
	}
	public String getVolume() {
		return volume;
	}
	public void setVolume(String volume) {
		this.volume = volume;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getSuppl() {
		return suppl;
	}
	public void setSuppl(String suppl) {
		this.suppl = suppl;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getText(){
		String r = journal.getCollection() + "\n" + journal.getTitle() + " ";
		r = r + format("v.", " ", volume);
		r = r + format("n.", " ", number);
		r = r + format("s.", " ", suppl);
		r = r + format("", " ", date);
		
		return r;
	}
	public String getIssueLabel(boolean displayTitle){
		String r = "";
		if (displayTitle){
			r = journal.getTitle() + " ";	
		}
		
		r = r + format("v.", " ", volume);
		r = r + format("n.", " ", number);
		r = r + format("s.", " ", suppl);
		r = r + format("", " ", date);
		
		return r;
	}
	private String format(String prefix, String suffix, String s){
		String r = s;
		if (s.length()>0){
			r = prefix + s + suffix;
		}
		return r;
	}
}
