package org.scielo.search;

public class SearchFilter {
	private int submenuId;
	private String caption;
	private String resultCount;
	private String code;
	private String clusterCode;
	
	SearchFilter(String _caption, String _resultCount, String _code, String _clusterCode) {
		this.caption = _caption;
		this.resultCount = _resultCount;
		this.code = _code;
		this.clusterCode = _clusterCode;
	}
	
	public void setSubmenuId(int _submenuId){
		this.submenuId = _submenuId;
	}
	public int getSubmenuId(){
		return this.submenuId;
	}
	public String getCaption(){
		return this.caption;
	}
	public String getResultCount(){
		return this.resultCount;
	}
	public String getCode(){
		return this.code;
	}
	public String getClusterCode(){
		return this.clusterCode;
	}	
	public String getFilterExpression(){
		return this.clusterCode + ":" + '"' + this.code + '"' ;
	}

	public String displayCaptionAndCount() {
		// TODO Auto-generated method stub
		String r = caption;
		if (!resultCount.equals("0")){
			r = r + "(" + resultCount + ")";
		}
		return r;
	}

	public void setCaption(String name) {
		// TODO Auto-generated method stub
		this.caption = name;
	}
}
