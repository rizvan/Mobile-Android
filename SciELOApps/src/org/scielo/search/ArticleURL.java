package org.scielo.search;

public class ArticleURL {
	String generic_PDF_URL;
	String generic_article_url;
	String generic_pdf_and_log_url;
	
	URLTester urlTester = new URLTester();
//, this.getResources().getString(R.string.pdf_url), this.getResources().getString(R.string.article_url)	
	ArticleURL(String _generic_pdf_and_log_url, String _generic_pdf_url, String _generic_article_url){
		this.generic_pdf_and_log_url = _generic_pdf_and_log_url;
		this.generic_PDF_URL = _generic_pdf_url;
		this.generic_article_url = _generic_article_url;
	}
	
	public String returnPDFURL(Document doc){
		String url;
		
		url = returnURL(this.generic_PDF_URL, doc);
		if (urlTester.check(url)){
			//url = url.replace("REPLACE_PDF_URL", url);
			doc.setPdf_url(url);
			url = returnURL(this.generic_pdf_and_log_url, doc);
		} 
		return url;
	}
	public String returnFullTextURL(Document doc){		
		return returnURL(this.generic_article_url, doc);
	}
	public String returnURL(String generic_url, Document doc){
		String url;
		String f = "";
		String[] a;
		String filename = "";
		
		int i;
		
		url = generic_url;
		
		if (url.contains("REPLACE_PDF_FILENAME")) {
			filename = doc.getFilename();
			if (filename.length()>0){
				filename = filename.replace( (char)92 , (char)47);
				filename = filename.replace(".htm", "").replace(".html", "").replace(".xml","");
				
				filename = filename.replace( "//", "/");
				filename = filename.toLowerCase();
				
				a = filename.split("/");
				i = a.length;
							
				f = a[i-4] + "/" + a[i-3] + "/" + a[i-1];
				
				url = url.replace("REPLACE_PDF_FILENAME", f);
			}			
		}
		url = url.replace("REPLACE_COLLECTION_URL", doc.getCol().getUrl());
		url = url.replace("REPLACE_PID", doc.getDocumentId());
		url = url.replace("REPLACE_LANG", doc.getLang());
		url = url.replace("REPLACE_APP", doc.getCol().getNickname());
		
		url = url.replace("REPLACE_PDF_URL", doc.getPdf_url());
		if (urlTester.check(url)==false){
			url = "";
		}
		return url;
	}
}
