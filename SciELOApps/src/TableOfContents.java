import java.util.ArrayList;

import org.scielo.search.Document;


public class TableOfContents {
	private Issue issue;
	private ArrayList<Document> docs =  new ArrayList<Document>();
	public Issue getIssue() {
		return issue;
	}
	public void setIssue(Issue issue) {
		this.issue = issue;
	}
	public ArrayList<Document> getDocs() {
		return docs;
	}
	public void setDocs(ArrayList<Document> docs) {
		this.docs = docs;
	}
}
