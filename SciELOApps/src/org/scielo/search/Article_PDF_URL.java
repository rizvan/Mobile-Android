package org.scielo.search;

public class Article_PDF_URL {
	String generic_PDF_URL;
	
	
	Article_PDF_URL(String _generic_pdf_url){
		this.generic_PDF_URL = _generic_pdf_url;
	}
	
	public String getURL(String _appUrl, String _appName, String _pid, String _filename, String _lang){
		String pdfUrl;
		String f;
		String[] a;
		int i;
		
		_filename = _filename.replace( (char)92 , (char)47);
		_filename = _filename.replace(".htm", "").replace(".html", "").replace(".xml","");
		
		_filename = _filename.replace( "//", "/");
		_filename = _filename.toLowerCase();
		
		a = _filename.split("/");
		i = a.length;
		
		
		f = a[i-4] + "/" + a[i-3] + "/" + a[i-1];
		pdfUrl = this.generic_PDF_URL;
		pdfUrl = pdfUrl.replace("REPLACE_COLLECTION_URL", _appUrl);
		pdfUrl = pdfUrl.replace("REPLACE_PDF_FILENAME", f);
		pdfUrl = pdfUrl.replace("REPLACE_APP", _appName);
		pdfUrl = pdfUrl.replace("REPLACE_PID", _pid);
		pdfUrl = pdfUrl.replace("REPLACE_LANG", _lang);
		return pdfUrl;
	}
	
	
}
