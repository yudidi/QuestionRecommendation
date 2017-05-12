package com.sicnu.yudidi.crawler;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;

public class CrawlerWithCookie extends CrawlerBase {

	private final static Logger log = Logger.getLogger(CrawlerWithCookie.class);
	static {
		trustEveryone();
	}

	/**
	 * 直到成功才能返回
	 * @param url
	 * @param method
	 * @return
	 */
	public static Document getPageContent(String url, String method) {
		log.debug(String.format("url:%s|method:%s", url, method));
		Document doc = null;
		Connection conn = HttpConnection.connect(url).timeout(CrawlerConfig.TIME_OUT);
		conn.header("Accept-Encoding", "gzip,deflate,sdch");
		conn.header("Connection", "close");
		conn.header("Cookie", CrawlerConfig.COOKIES);
		do {
			sleep();
			log.debug(String.format("Thread %d|Thread.interrupted() == %s",Thread.currentThread().getId(), Thread.currentThread().isInterrupted()));
			try {
				doc = method.equals("get") ? conn.get() : conn.post();
			} catch (IOException e) {
				e.printStackTrace();
				log.debug("getPageContent失败");
			} catch (Exception e) {
				e.printStackTrace();
				log.debug("getPageContent失败");
			}
		} while (doc == null && !Thread.currentThread().isInterrupted());
		return doc;
	}
}
