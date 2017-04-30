package com.sicnu.yudidi.utils.crawler;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;

public class CrawlerWithCookies extends CrawlerBase {

	private final static Logger log = Logger.getLogger(CrawlerWithCookies.class);
	static {
		HTTPCommonUtil.trustEveryone();
	}

	public static Document getPageContent(String url, String method) {
		log.debug(String.format("url:%s|method:%s", url, method));
		sleep();
		Document doc = null;
		Connection conn = HttpConnection.connect(url).timeout(CrawlerConfig.TIME_OUT);
		conn.header("Accept-Encoding", "gzip,deflate,sdch");
		conn.header("Connection", "close");
		conn.header("Cookie", CrawlerConfig.COOKIES);
		try {
			doc = method.equals("get") ? conn.get() : conn.post();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return doc;
	}
}
