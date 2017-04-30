package com.sicnu.yudidi.service;

public class RecommendationConfig {

	public final static int MIN_ANSWERED_COUNT = 10;
	public final static int MAX_RECOMMENDED_SUBJECTS = 3;
	public final static String  SUBJECT_URL_PREFIX = "https://www.nowcoder.com/practice/";
	public final static String SUBJECT_A_TAG_TEMPLATE = "{\"subject\":\"<a target=\\\"_blank\\\" href=\\\"{url}\\\">{title}</a>\"},";
	public final static String cookBooksUrl = "https://www.nowcoder.com/profile/${userId}/codeBooks?page=${page}";
}
