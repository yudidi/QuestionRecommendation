package com.sicnu.yudidi.utils.crawler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

import com.sicnu.yudidi.crawler.CrawlingSubjectTitle;
import com.sicnu.yudidi.mybatis.pojo.Record;

public class CrawlingSubjectTitleTest {

	@Test
	public void testCrawlingSubjectTilte() {
		fail("Not yet implemented");
	}

	@Test
	public void testInsertRecord() {
		Record record = new Record("subjectId", "编程题", "u1,u2");
		boolean isSuccess = false;
		try {
			CrawlingSubjectTitle.insertRecord(record);
			isSuccess = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertEquals(true, isSuccess);
	}

}
