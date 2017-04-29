package com.sicnu.yudidi.dao;

import java.io.IOException;

import org.apache.ibatis.session.SqlSession;

import com.sicnu.yudidi.mybatis.pojo.Record;
import com.sicnu.yudidi.utils.db.DB;

public class RecordDao {
	public  boolean exist(String subject_id) throws IOException {
		SqlSession session = DB.getSqlSession();
		int i = session.selectOne("check_exsitance", subject_id);
		session.commit();
		session.close();
		return i == 1 ? true : false;
	}

	public  int countAll() throws IOException {
		SqlSession session = DB.getSqlSession();
		int count = session.selectOne("count_all");
		session.commit();
		session.close();
		return count;
	}
	
	public  void insert(Record record) throws IOException {
		SqlSession session = DB.getSqlSession();
		session.insert("add_record", record);
		session.commit();
		session.close();
	}

}
