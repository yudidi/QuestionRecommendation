package com.sicnu.yudidi.mybatis.pojo;

public class Cluster {
	private String cluster_name;
	private String subject_id_join;
	public Cluster(String cluster_name, String subject_id_join) {
		super();
		this.cluster_name = cluster_name;
		this.subject_id_join = subject_id_join;
	}
	public String getCluster_name() {
		return cluster_name;
	}
	public void setCluster_name(String cluster_name) {
		this.cluster_name = cluster_name;
	}
	public String getSubject_id_join() {
		return subject_id_join;
	}
	public void setSubject_id_join(String subject_id_join) {
		this.subject_id_join = subject_id_join;
	}
}
