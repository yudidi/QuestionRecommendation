package com.sicnu.yudidi.mybatis.pojo;

public class Record {
	private String subjectId;
	private String subjectTitle;
	private String answersIdList;//join by ","
	
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