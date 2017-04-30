package com.sicnu.yudidi.dao;

import java.io.IOException;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.sicnu.yudidi.mybatis.pojo.Cluster;
import com.sicnu.yudidi.utils.db.DB;

public class ClusterDao {
	public boolean exist(String cluster_name) throws IOException {
		SqlSession session = DB.getSqlSession();
		int i = session.selectOne("check_exsitance_cluster", cluster_name);
		session.commit();
		session.close();
		return i == 1 ? true : false;
	}

	public List<Cluster> listClusters() throws IOException {
		SqlSession session = DB.getSqlSession();
		List<Cluster> clusters = session.selectList("list_clusters");
		session.commit();
		session.close();
		return clusters;
	}
	
	public List<Cluster> listClustersDescByName() throws IOException {
		SqlSession session = DB.getSqlSession();
		List<Cluster> clusters = session.selectList("list_all_order_by_cluster_name_desc");
		session.commit();
		session.close();
		return clusters;
	}
	
	public int countAll() throws IOException {
		SqlSession session = DB.getSqlSession();
		int count = session.selectOne("count_all_clusters");
		session.commit();
		session.close();
		return count;
	}

	public int insert(Cluster cluster) throws IOException {
		SqlSession session = DB.getSqlSession();
		int affected = session.insert("add_clusters", cluster);
		session.commit();
		session.close();
		return affected;
	}
}
