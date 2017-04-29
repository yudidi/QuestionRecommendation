package com.sicnu.yudidi.mybatis.pojo;

public class Record {
	private String subjectId;
	private String subjectTitle;
	private String answersIdList; //join by ","
	
	public Record(String subjectId, String subjectTitle, String answersIdList) {
		super();
		this.subjectId = subjectId;
		this.subjectTitle = subjectTitle;
		this.answersIdList = answersIdList;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public String getSubjectTitle() {
		return subjectTitle;
	}
	public void setSubjectTitle(String subjectTitle) {
		this.subjectTitle = subjectTitle;
	}
	public String getAnswersIdList() {
		return answersIdList;
	}
	public void setAnswersIdList(String answersIdList) {
		this.answersIdList = answersIdList;
	}
}