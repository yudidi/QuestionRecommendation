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

	//TODO--是否增加限时功能
	//TODO--是否增加直到成功才能返回的参数
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
			log.debug("getPageContent失败");
		} catch (Exception e) {
			e.printStackTrace();
			log.debug("getPageContent失败");
		}
		return doc;
	}
}
