package org.scielo.search;

public final class MyAppConfig {

	private JournalsCollectionsNetwork jcn;
	private IdAndValueObjects languages;
	private IdAndValueObjects subjects;
	private IdAndValueObjects letters;
	
	public IdAndValueObjects getLetters() {
		return letters;
	}
	public void setLetters(IdAndValueObjects letters) {
		this.letters = letters;
	}
	public JournalsCollectionsNetwork getJcn() {
		return jcn;
	}
	public void setJcn(JournalsCollectionsNetwork jcn) {
		this.jcn = jcn;
	}
	public IdAndValueObjects getLanguages() {
		return languages;
	}
	public void setLanguages(IdAndValueObjects languages) {
		this.languages = languages;
	}
	public IdAndValueObjects getSubjects() {
		return subjects;
	}
	public void setSubjects(IdAndValueObjects subjects) {
		this.subjects = subjects;
	}
	
	
}
