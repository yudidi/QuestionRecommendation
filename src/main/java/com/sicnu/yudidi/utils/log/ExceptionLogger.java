package com.sicnu.yudidi.utils.log;

import org.apache.log4j.Logger;

import com.sicnu.yudidi.crawler.CrawlerWithCookie;

public class ExceptionLogger {

	private final static Logger log = Logger.getLogger(CrawlerWithCookie.class);

	public static void logException(Exception e) {
		for (StackTraceElement elem : e.getStackTrace()) {
			log.error(elem);
		}
	}
}
