package com.sicnu.yudidi.mybatis.pojo;

import java.util.List;

public class Cluster {
	private String clusterId;
	private List<String> subjectIdList;
	/**
	 * @param clusterId
	 * @param subjectIdList
	 */
	public Cluster(String clusterId, List<String> subjectIdList) {
		super();
		this.clusterId = clusterId;
		this.subjectIdList = subjectIdList;
	}
}
