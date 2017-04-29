package com.sicnu.yudidi.utils.crawler;

import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * # todo
 * write into a crawler-config.xml.
 */
public class CrawlerConfig {
	// public 
	public final static String userDir = Paths.get(System.getProperty("user.dir")).toString();
	public final static String PUBLIC_OUTPUT = Paths.get(System.getProperty("user.dir"),"output").toString();
	public final static long MAX_TIME = (long) 120;//minutes.
	public final static boolean OVERWRITE = true; //是否覆盖之前的数据
	// Arguments of crawler
	public final static String BASE_URI = "https://www.nowcoder.com/";
	public final static String ENTRY = "https://www.nowcoder.com/activity/oj?title=&tags=&order=submissionCount&asc=false&page=${page}";
	public final static int SUBJECT_TOTAL = 500;
	public final static int TIME_OUT = 5000;//ms
	public final static int CRAWLER_INTERVAL = 500;//ms
	public final static String COOKIES = "NOWCODERUID=025BCCE49ABBA60DFCBA6B49C8294C17; NOWCODERUID=025BCCE49ABBA60DFCBA6B49C8294C17; UM_distinctid=15b13eb707f9-0a1bbb8d2191468-1262694a-d7300-15b13eb7081143; CNZZDATA1253353781=1092614714-1490686423-https%253A%252F%252Fwww.nowcoder.com%252F%7C1491960198; NOWCODERCLINETID=C68CF3195EC6496F2C5857F8C8D164F4; NOWCODERCLINETID=C68CF3195EC6496F2C5857F8C8D164F4; SERVERID=547d00d82311952605c62ceac64f21fd|1491964411|1491962701; t=4BF7D787D207728023886623872D2F3F; t=4BF7D787D207728023886623872D2F3F";
	public final static String questionTerminal_PREFIX = "https://www.nowcoder.com/questionTerminal/${questionID}";
	// output of crawlers
	public final static String OUTPUT_SUBJECTS_URLS_FILE_ABSOLUTE_PATH = Paths.get(PUBLIC_OUTPUT, "questionsUrls.txt").toString();
	public final static String SUBJECTS_DATA = Paths.get(PUBLIC_OUTPUT,"subjects").toString();
	public final static String CRAWLED_URLS = Paths.get(PUBLIC_OUTPUT, "crawling-successed-urls.txt").toString();
	
	// setting of subject discussion page.
	public final static String ANSWERS_JSON_DATA_AJAX_URL = "https://www.nowcoder.com/comment/listByPage?pageSize=20&page={page}&entityId={entityId}&entityType=3&order=1";
	public final static String REPLY_JSON_DATA_AJAX_URL = "https://www.nowcoder.com/comment/list-by-page-v2?pageSize=10&page={page}&entityId={entityId}&entityType=2&order=1";
}



