package com.sicnu.yudidi.kmedoids.clustering;

import java.util.ArrayList;
import java.util.List;

public class Cluster {
	private String clusterName; //簇名
	private Record centralRecord; //簇的中心记录
	private List<Record> recordsList = new ArrayList<>(); //簇中包含的记录列表
	
	public Cluster() {
	}
	
	public Cluster(String clusterName) {
		super();
		this.clusterName = clusterName;
	}

	public Cluster(String clusterName, List<Record> recordsList) {
		super();
		this.clusterName = clusterName;
		this.recordsList = recordsList;
	}
	
	public void addRecord(Record record) {
		recordsList.add(record);
	}

	public void clear() {
		recordsList.clear();
	}

	public Record getCentralRecord() {
		return centralRecord;
	}

	public void setCentralRecord(Record centralRecord) {
		this.centralRecord = centralRecord;
	}

	/* getter/setter */
	public String getClusterName() {
		return clusterName;
	}

	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}

	public List<Record> getRecordsList() {
		return recordsList;
	}

	public void setRecordsList(List<Record> recordsList) {
		this.recordsList = recordsList;
	}

}
