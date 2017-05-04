package com.sicnu.yudidi.crawler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/*
 * Single Thread.
 */
public class CrawlerOfEnties extends CrawlerBase{
	
	private static Logger log = Logger.getLogger(CrawlerOfEnties.class);
	static Set<String> questionsUrls = new HashSet<String>();
	
	/**
	 * 爬取热度指数排行前(SUBJECT_TOTAL)的题目地址,并保存到文件
	 */
	public static void crawlingSubjectUrls() {
		log.info("开始爬取"+CrawlerConfig.SUBJECT_TOTAL+"道题目的url");
		File file = Paths.get(System.getProperty("user.dir"), CrawlerConfig.OUTPUT_SUBJECTS_URLS_FILE_ABSOLUTE_PATH).toFile();
		if (!file.exists()) {
			setQuestionsUrlsUseRegex();
			saveQuestionsUrls(file);
			log.info("完成爬取"+CrawlerConfig.SUBJECT_TOTAL+"道题目的url");
		}
	}
	
	public static void setQuestionsUrlsUseRegex() {
		int page = 0;
		Pattern pattern = Pattern.compile("(?<=\\/)\\w+(?=\\?)");//
		boolean isFinish = false;
		while (!isFinish) {
			try {
				trustEveryone();
				String url = CrawlerConfig.ENTRY.replace("${page}", ++page + "");
				System.out.println("url : " + url);
				Connection conn = HttpConnection.connect(url);
				conn.timeout(CrawlerConfig.TIME_OUT);
				conn.header("Accept-Encoding", "gzip,deflate,sdch");
				conn.header("Connection", "close");
				conn.header("Cookies", CrawlerConfig.COOKIES);
				Document doc = conn.get();
				Elements aElements = doc.select("div.module-body table a");
				for (Element aElement : aElements) {
					Matcher matcher = pattern.matcher(aElement.attr("href"));
					if (matcher.find()) {
						String questionID = matcher.group(0);
						String questionTerminalPageUrl = CrawlerConfig.questionTerminal_PREFIX.replace("${questionID}", questionID);
						questionsUrls.add(questionTerminalPageUrl);
						System.out.printf("%d : %s \n", questionsUrls.size(), questionTerminalPageUrl);
						if (questionsUrls.size() == CrawlerConfig.SUBJECT_TOTAL) {
							isFinish = true;
							break;
						}
						continue;
					}
				}
				Thread.sleep(CrawlerConfig.CRAWLER_INTERVAL);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void saveQuestionsUrls(File file) {
		if (file.exists()) {
			file.delete();
		}
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(file));
			for (String url : questionsUrls) {
				writer.append(url);
				writer.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
