package org.scielo.search;

public class JournalsCollection {
	private String id;
	private String nickname;
	private String name;
	private String url;
	
	public JournalsCollection() {
		super();
		id = "";
		nickname = "";
		name = "";
		url = "";
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
}
