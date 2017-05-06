package com.sicnu.yudidi.dao;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	public List<Record> getAllRecords() throws IOException {
		SqlSession session = DB.getSqlSession();
		List<Record> records = session.selectList("list_all_records");
		session.commit();
		session.close();
		return records;
	}

	public Map<String, String> getMap_id_tile() throws IOException {
		Map<String, String> idAndTitle = new HashMap<>();
		SqlSession session = DB.getSqlSession();
		List<Map<String, String>> mapList = session.selectList("get_map_id_and_title");
		for (Map<String, String> map : mapList) {
			idAndTitle.put(map.get("key"), map.get("value"));
		}
		session.commit();
		session.close();
		return idAndTitle;
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
