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
import java.util.Set;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONObject;
import com.sicnu.yudidi.dao.ClusterDao;
import com.sicnu.yudidi.dao.RecordDao;
import com.sicnu.yudidi.mybatis.pojo.Cluster;
import com.sicnu.yudidi.utils.collections.CollectionsUtils;
import com.sicnu.yudidi.utils.crawler.CrawlerNoCookie;
import com.sicnu.yudidi.utils.crawler.CrawlerWithCookies;

public class Recommendation {

	private final static Logger log = Logger.getLogger(Recommendation.class);

	public static String generateJson(String nowcoderId) {
		if (!checkExistance(nowcoderId)) {
			return "{msg:\"not found\"}";
		}
		if (!canBeRecommended(nowcoderId)) {
			return "{msg:\"not enough\",min_answered:10}";
		}
		String json = generateJsonByRecommendSubject(getRecommendedSubjectIdsList(nowcoderId));
		return json;
	}
	/**
	 * 获取推荐给该用户的试题id-list
	 * 
	 * @param nowcoderId
	 * @return
	 */
	public static List<String> getRecommendedSubjectIdsList(String nowcoderId) {
		// 获取可以最匹配的簇
		List<Cluster> clusters = null;
		try {
			clusters = new ClusterDao().listClusters();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("ClusterDao查询数据库失败");
		}
		List<String> passedSubjectIdList = new ArrayList<>(getPassedSubjectIdSet(nowcoderId));
		Map<Integer, List<String>> map = new HashMap<>();
		for (int i = 0; i < clusters.size(); i++) {
			List<String> oneClusterSubjectList = Arrays.asList(clusters.get(i).getSubject_id_join().split(","));
			List<String> inters = CollectionsUtils.intersection(oneClusterSubjectList, passedSubjectIdList);
			map.put(i, inters);
		}
		List<Map.Entry<Integer, List<String>>> entries = new ArrayList<Map.Entry<Integer, List<String>>>(map.entrySet());
		Collections.sort(entries, new Comparator<Map.Entry<Integer, List<String>>>() {
			// 按照value.size()降序排列  //最长的lcs
			@Override
			public int compare(Entry<Integer, List<String>> v1, Entry<Integer, List<String>> v2) {
				return v2.getValue().size() - v1.getValue().size();
			}
		});
		List<String> recommendedSubjectIdList = new ArrayList<>();
		for (Entry<Integer, List<String>> entry : entries) {
			Cluster cluster = clusters.get(entry.getKey());
			List<String> tobeRecommendedSubjectIds = Arrays.asList(cluster.getSubject_id_join().split(","));
			tobeRecommendedSubjectIds.removeAll(Arrays.asList(passedSubjectIdList));// 排除已经做了的题目
			if (tobeRecommendedSubjectIds.size() == 0) {
				log.debug(String.format("簇%s没有题目可以推荐", cluster.getCluster_name()));
				continue;
			}
			log.debug(String.format("簇%s的可以推荐的试题数为%d", cluster.getCluster_name(), tobeRecommendedSubjectIds.size()));
			recommendedSubjectIdList.clear();
			for (int i = 0; i < RecommendationConfig.MAX_RECOMMENDED_SUBJECTS && i < tobeRecommendedSubjectIds.size(); i++) {
				recommendedSubjectIdList.add(tobeRecommendedSubjectIds.get(i));
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
			log.error("RecordDao层查询数据库失败");
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
		if (doc.select(".side-profile-info").size() != 0 && doc.select(".menu-box").size() != 0)  {
			return true;
		}
		return false;
	}

	// 通过检查第一页是否答满10题,判断是否可以推荐. [0,9]题,不推荐.
	public static boolean canBeRecommended(String userId) {
		String url = RecommendationConfig.cookBooksUrl.replace("${userId}", userId).replace("${page}", "1");
		Document doc = CrawlerNoCookie.getPageContent(url, "get");
		Elements trs = doc.select(".module-body tbody tr");
		return trs.size() >= RecommendationConfig.MIN_ANSWERED_COUNT ? true : false;
	}

	// 抓取去重后的所有刷题记录
	public static Set<String> getPassedSubjectIdSet(String userId) {
		Set<String> set  =  new HashSet<>(getPassedSubjectIdList(userId));
		log.info(String.format("用户%s去重后的答题记录数%d", userId,set.size()));
		return set;
	}

	// 抓取该用户所有答题记录,未去重,subject_id-list
	public static List<String> getPassedSubjectIdList(String userId) {
		List<String> subjectIdsTotal = new ArrayList<>();
		int page = 1;
		while (true) {
			String url = RecommendationConfig.cookBooksUrl.replace("${userId}", userId).replace("${page}", page + "");
			List<String> subjectIdsOfOnePage = getSubjectIdsOfOnePage(url);
			if (subjectIdsOfOnePage == null) {
				break;
			}
			subjectIdsTotal.addAll(subjectIdsOfOnePage);
			log.debug(String.format("codeBooks|页面%d|提取完毕|:%s", page, url));
			if (subjectIdsOfOnePage.size() < 10) {
				log.debug(String.format("用户答题记录爬取完毕,最后一页是:%s,最后一页题目数:%d", url, subjectIdsOfOnePage.size()));
				break;
			}
			page++;
		}
		log.debug(String.format("用户%s,通过题目总数%d", userId,subjectIdsTotal.size()));
		return subjectIdsTotal;
	}

	// 获取一个页面通过编程题的subject_id
	public static List<String> getSubjectIdsOfOnePage(String url) {
		List<String> subjectIds = new ArrayList<>();
		Document doc = CrawlerWithCookies.getPageContent(url, "get");
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

	// 根据subMissionIdUrl提取subjectId
	public static String extractSubjectId(String subMissionIdUrl) {
		Document doc = CrawlerWithCookies.getPageContent(subMissionIdUrl, "get");
		String subjectUrl = doc.select("div.result-subject-item a.continue-challenge").attr("href");//
		return subjectUrl.substring(subjectUrl.lastIndexOf("/") + 1);
	}
}
