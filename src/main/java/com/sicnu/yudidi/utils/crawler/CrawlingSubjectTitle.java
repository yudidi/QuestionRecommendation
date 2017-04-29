package com.sicnu.yudidi.utils.crawler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;

import com.sicnu.yudidi.mybatis.pojo.Record;
import com.sicnu.yudidi.utils.file.FileEx;

public class CrawlingSubjectTitle {

  private final	 static Logger log = Logger.getLogger(CrawlingSubjectTitle.class);
  public static SqlSession session;
  
	public static void main(String[] args) {
		crawlingSubjectTilte();
	}
	
	public static void crawlingSubjectTilte() {
		String prefix = "https://www.nowcoder.com/questionTerminal/";
		File dataset = Paths.get(CrawlerConfig.userDir,"src/main/java/com/sicnu/yudidi/utils/crawler/dataset.csv").toFile();
		
		for (String line : FileEx.readLineByReader(dataset)) {
			String subjectId = line.split(",")[0];
			String url = prefix+subjectId;
			Document page = CrawlerNoCookie.getPageContent(url, "get");
			String subjectTitle = page.select(".subject-box .subject-title").text();
			String answersIdList = line.substring(line.indexOf(","+1));
			Record record = new Record(subjectId, subjectTitle, answersIdList);		
			try {
				insertRecord(record);
			} catch (IOException e) {
				e.printStackTrace();
				log.info("Record插入失败");
			}
		}
	}
	
	public static void insertRecord(Record record) throws IOException {
		String resource = "mybatis-config.xml";
		InputStream inputStream =  Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		session = sqlSessionFactory.openSession();
		session.insert("add_record",record);
		session.commit();
		session.close();
	}
	
}
