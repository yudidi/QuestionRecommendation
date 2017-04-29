package com.sicnu.yudidi.utils.crawler;

public class CrawlerBase {

	protected static  void sleep() {
		try {
			Thread.sleep(CrawlerConfig.CRAWLER_INTERVAL);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
