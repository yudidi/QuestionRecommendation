package com.sicnu.yudidi.service;

public class RecommendationConfig {

	public final static int MIN_ANSWERED_COUNT = 10;
	public final static int MAX_RECOMMENDED_SUBJECTS = 3;
	/**
	 * 推荐任务最大时长
	 */
	public final static int LIMIT_RECOMMENDATION_TASK_SECONDS = 120;
	/**
	 * 抓取的历史答题记录页数上限
	 */
	public final static int MAX_HISTORY_SUBJECTS_PAGES = 5;
	public final static String  SUBJECT_URL_PREFIX = "https://www.nowcoder.com/practice/";
	public final static String SUBJECT_A_TAG_TEMPLATE = "{\"subject\":\"<a target=\\\"_blank\\\" href=\\\"{url}\\\">{title}</a>\"},";
	public final static String cookBooksUrl = "https://www.nowcoder.com/profile/${userId}/codeBooks?page=${page}";
}
