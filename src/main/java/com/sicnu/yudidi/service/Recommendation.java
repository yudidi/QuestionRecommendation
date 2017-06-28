package com.sicnu.yudidi.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONObject;
import com.sicnu.yudidi.crawler.CrawlerNoCookie;
import com.sicnu.yudidi.crawler.CrawlerWithCookie;
import com.sicnu.yudidi.dao.ClusterDao;
import com.sicnu.yudidi.dao.RecordDao;
import com.sicnu.yudidi.mybatis.pojo.Cluster;
import com.sicnu.yudidi.utils.collections.CollectionsUtils;
import com.sicnu.yudidi.utils.log.ExcepLogger;
import com.sicnu.yudidi.utils.task.TimeLimitTask;

public class Recommendation {

	private final static Logger log = Logger.getLogger(Recommendation.class);
	/**
	 * 限时推荐任务
	 * 
	 * @param nowcoderId
	 * @return
	 */
	public static String recommend(String nowcoderId) {
		Callable<String> runnable = new Callable<String>() {
			@Override
			public String call() throws Exception {
				String json = null;
				json = Recommendation.generateJson(nowcoderId);
				log.info(String.format("runnable|json == %s", json));
				return json;
			}
		};
		String json = TimeLimitTask.timeLimitTaskString(runnable, RecommendationConfig.LIMIT_RECOMMENDATION_TASK_SECONDS, TimeUnit.SECONDS);
		if (json == null) {
			json = String.format("{\"data\":[{\"subject\":\"<label class=\\\"warning\\\">Recommendation timeout, please check your network or try again.</label>\"}]}", nowcoderId);
		}
		return json;
	}

	public static String generateJson(String nowcoderId) {
		if (!Pattern.matches("^[0-9]+$", nowcoderId)) {
			return String.format("{\"data\":[{\"subject\":\"Soryy, <label class=\\\"warning\\\">'%s'</label> is invalid, nowcoder ID should be pure numbers.\"}]}", nowcoderId);
		}
		if (Thread.currentThread().isInterrupted()) {
			return null;
		}
		if (!checkExistance(nowcoderId) || nowcoderId == null || nowcoderId.trim().length() == 0) {
			return String.format("{\"data\":[{\"subject\":\"Soryy, nowcoder ID <label class=\\\"warning\\\">%s</label> does not exist.\"}]}", nowcoderId);
		}
		if (Thread.currentThread().isInterrupted()) {
			return null;
		}
		int passed = canBeRecommended(nowcoderId);
		if (canBeRecommended(nowcoderId) < RecommendationConfig.MIN_ANSWERED_COUNT) {
			log.debug(String.format("%s只通过 %d道题，不满足推荐条件", nowcoderId, passed));
			return String.format("{\"data\":[{\"subject\":\"Sorry, <label class=\\\"warning\\\">user %s need to do %d more questions </label> to match our recommendation condition.\"}]}", nowcoderId,
					RecommendationConfig.MIN_ANSWERED_COUNT - passed, RecommendationConfig.MIN_ANSWERED_COUNT);
		}
		if (Thread.currentThread().isInterrupted()) {
			return null;
		}
		List<String> recommendedList = getRecommendedSubjectIdsList(nowcoderId);
		if (Thread.currentThread().isInterrupted()) {
			return null;
		}
		if (recommendedList == null) {
			log.debug("没有可以推荐的题目");
			return String.format("{\"data\":[{\"subject\":\"<label class=\\\"warning\\\">Maybe you are a Legendary.There is no questions we can recommended for you.</label>\"}]}", nowcoderId);
		}
		if (Thread.currentThread().isInterrupted()) {
			return null;
		}
		return generateJsonByRecommendSubject(recommendedList);
	}
	/**
	 * 获取推荐给该用户的试题id-list
	 * 
	 * @param nowcoderId
	 * @return
	 */
	public static List<String> getRecommendedSubjectIdsList(String nowcoderId) {
		// 获取最匹配的簇
		List<Cluster> clusters = null;
		do {
			if (Thread.currentThread().isInterrupted()) {
				return null;
			}
			try {
				clusters = new ClusterDao().listClusters();
			} catch (Exception e) {
				ExcepLogger.log(e, "ClusterDao查询数据库失败");
			}
		} while (clusters == null);

		List<String> passedSubjectIdList = getPassedSubjectIdList(nowcoderId);
		Map<Integer, List<String>> map = new HashMap<>();
		for (int i = 0; i < clusters.size(); i++) {
			if (Thread.currentThread().isInterrupted()) {
				return null;
			}
			List<String> oneClusterSubjectList = Arrays.asList(clusters.get(i).getSubject_id_join().split(","));
			log.debug(String.format("簇%s中试题列表%d,%s", i, oneClusterSubjectList.size(), oneClusterSubjectList));
			log.debug(String.format("用户的通过的试题列表%d,%s", passedSubjectIdList.size(), passedSubjectIdList));
			List<String> inters = CollectionsUtils.intersection(oneClusterSubjectList, passedSubjectIdList);
			log.debug(String.format("簇%s和答题记录列表的交集个数%d,交集%s:", i, inters.size(), inters));
			if (inters.size() > 0) {
				map.put(i, inters);
			}
		}
		log.debug(String.format("map<簇号,交集>:%s", map));
		// 都没有交集
		if (map.size() == 0) {
			return null;
		}
		List<Map.Entry<Integer, List<String>>> entries = new ArrayList<Map.Entry<Integer, List<String>>>(map.entrySet());
		Collections.sort(entries, new Comparator<Map.Entry<Integer, List<String>>>() {
			// 按照value.size()降序排列 //最长的lcs
			@Override
			public int compare(Entry<Integer, List<String>> v1, Entry<Integer, List<String>> v2) {
				return v2.getValue().size() - v1.getValue().size();
			}
		});
		log.debug(String.format("value.size()降序排列后map<簇号,交集>:%s", entries));
		List<String> recommendedSubjectIdList = new ArrayList<>();
		for (Entry<Integer, List<String>> entry : entries) {
			Cluster cluster = clusters.get(entry.getKey());
			List<String> subjectIdsOfCluster = Arrays.asList(cluster.getSubject_id_join().split(","));
			subjectIdsOfCluster = CollectionsUtils.subtract(subjectIdsOfCluster, passedSubjectIdList);// 排除已经做了的题目
			log.debug(String.format("排除交集后可以推荐的题目个数%d,可以推荐的题目%s", subjectIdsOfCluster.size(), subjectIdsOfCluster));
			if (subjectIdsOfCluster.size() == 0) {
				log.debug(String.format("簇%s没有题目可以推荐", cluster.getCluster_name()));
				continue;
			}
			log.debug(String.format("簇%s的可以推荐的试题数为%d", cluster.getCluster_name(), subjectIdsOfCluster.size()));
			recommendedSubjectIdList.clear();
			for (int i = 0; i < RecommendationConfig.MAX_RECOMMENDED_SUBJECTS && i < subjectIdsOfCluster.size(); i++) {
				recommendedSubjectIdList.add(subjectIdsOfCluster.get(i));
			}
			log.debug(String.format("簇%s中推荐%d道给%s", cluster.getCluster_name(), recommendedSubjectIdList.size(), nowcoderId));
			if (recommendedSubjectIdList.size() == RecommendationConfig.MAX_RECOMMENDED_SUBJECTS) {
				log.debug("推荐完毕");
				break;
			}
		}
		return recommendedSubjectIdList;
	}

	public static String generateJsonByRecommendSubject(List<String> recommendedList) {
		RecordDao recordDao = new RecordDao();
		Map<String, String> idAndTitleMap = null;
		try {
			idAndTitleMap = recordDao.getMap_id_tile();
		} catch (Exception e) {
			e.printStackTrace();
			ExcepLogger.log(e,"RecordDao层查询数据库失败");
		}
		StringBuffer tableJson = new StringBuffer();
		tableJson.append("{  \"data\": [");
		for (String subjectId : recommendedList) {
			String subjectTag = RecommendationConfig.SUBJECT_A_TAG_TEMPLATE.replace("{url}", RecommendationConfig.SUBJECT_URL_PREFIX + subjectId).replace("{title}", idAndTitleMap.get(subjectId));
			tableJson.append(subjectTag);
		}
		tableJson.subSequence(0, tableJson.length() - 1);
		tableJson.append("]}");
		JSONObject jsonObject = JSONObject.parseObject(tableJson.toString());
		return jsonObject.toJSONString();
	}

	// 检查该用户是否存在
	public static boolean checkExistance(String nowcoderId) {
		String url = "https://www.nowcoder.com/profile/" + nowcoderId;
		Document doc = CrawlerNoCookie.getPageContent(url, "get");
		if (doc.select(".side-profile-info").size() != 0 && doc.select(".menu-box").size() != 0) {
			return true;
		}
		return false;
	}

	// 通过检查第一页是否答满10题,判断是否可以推荐. [0,9]题,不推荐.
	public static int canBeRecommended(String userId) {
		String url = RecommendationConfig.cookBooksUrl.replace("${userId}", userId).replace("${page}", "1");
		Document doc = CrawlerNoCookie.getPageContent(url, "get");
		Elements trs = doc.select(".module-body tbody tr");
		return trs.size();
	}

	/**
	 * 获取用户所有答题记录subject-id-list,已经去重
	 * 
	 * @param userId
	 * @return
	 */
	public static List<String> getPassedSubjectIdList(String userId) {
		List<String> subjectIdsTotal = new ArrayList<>();
		int page = 1;
		while (!Thread.currentThread().isInterrupted()) {
			log.debug(String.format("Thread %d| Thread.currentThread().interrupt() == %s", Thread.currentThread().getId(), Thread.currentThread().isInterrupted()));
			if (page > RecommendationConfig.MAX_HISTORY_SUBJECTS_PAGES) {
				log.info(String.format("达到抓取历史答题记录的上限页数%d,停止抓取", RecommendationConfig.MAX_HISTORY_SUBJECTS_PAGES));
				break;
			}
			String url = RecommendationConfig.cookBooksUrl.replace("${userId}", userId).replace("${page}", page + "");
			List<String> subjectIdsOfOnePage = new ArrayList<>(getSubjectIdsOfOnePage(url));
			log.debug(String.format("获取url%s中获取subject-id-list", url, subjectIdsOfOnePage));
			if (subjectIdsOfOnePage.size() == 0) {
				log.debug(String.format("用户答题记录爬取完毕,最后一页是:%s,最后一页题目总数:%d", url, subjectIdsOfOnePage.size()));
				break;
			}
			subjectIdsTotal.addAll(subjectIdsOfOnePage);
			log.debug(String.format("codeBooks|页面%d|提取完毕|:%s", page, url));
			page++;
		}
		log.info(String.format("用户%s答题总数%d,%s", userId, subjectIdsTotal.size(), subjectIdsTotal));
		return subjectIdsTotal;
	}

	/**
	 * 获取一个答题记录页面中所有编程题的subject_id(去重后)
	 * 
	 * @param url
	 * @return
	 */
	public static Set<String> getSubjectIdsOfOnePage(String url) {
		Set<String> subjectIds = new HashSet<>();
		Document doc = CrawlerWithCookie.getPageContent(url, "get");
		Elements trs = doc.select(".module-body tbody tr");
		if (trs == null) {
			log.debug("不存在: .module-body tbody");
			return null;
		}
		for (int i = 0; i < trs.size(); i++) {
			Elements tds = trs.get(i).select("td");
			Element tdOne = tds.select("td.t-subject-title").first();
			String subMissionIdUrl = tdOne.select("a").attr("abs:href");
			subjectIds.add(extractSubjectId(subMissionIdUrl));
		}
		return subjectIds;
	}

	/**
	 * 根据subMissionIdUrl提取一道试题的subject_id
	 * 
	 * @param subMissionIdUrl
	 * @return
	 */
	public static String extractSubjectId(String subMissionIdUrl) {
		Document doc = CrawlerWithCookie.getPageContent(subMissionIdUrl, "get");
		String subjectUrl = doc.select("a.continue-challenge").attr("href");
		if (subjectUrl == null) {
			log.debug(String.format("extractSubjectId失败,不能定位subjectUrl,", subMissionIdUrl));
		}
		String subjectId = subjectUrl.substring(subjectUrl.lastIndexOf("/") + 1);
		log.debug(String.format("根据subMissionIdUrl:%s获取subjectject-url:%s|subject-id:%s", subMissionIdUrl, subjectUrl, subjectId));
		return subjectId;
	}
}
