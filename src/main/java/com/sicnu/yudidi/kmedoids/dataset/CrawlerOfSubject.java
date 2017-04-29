package com.sicnu.yudidi.kmedoids.dataset;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sicnu.yudidi.utils.crawler.CrawlerConfig;
import com.sicnu.yudidi.utils.crawler.CrawlerNoCookie;
import com.sicnu.yudidi.utils.file.FileEx;
import com.sicnu.yudidi.utils.task.TimeLimitTask;

public class CrawlerOfSubject {

	private static Logger log = Logger.getLogger(CrawlerOfSubject.class);

	static Queue<String> questionQueue = new LinkedList<String>();

	public static void main(String[] args) {
		crawlingSubjectPages();
	}

	public static void timer() {
		// https://www.oschina.net/code/snippet_854917_25830
	}

	private static void initQueue() {
		log.debug("开始装载题目的url");
		File file = new File(CrawlerConfig.OUTPUT_SUBJECTS_URLS_FILE_ABSOLUTE_PATH);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String url = null;
			while ((url = reader.readLine()) != null) {
				if (url.length() > 0)
					questionQueue.add(url);
			}
		} catch (FileNotFoundException e) {
			log.debug("装载失败");
			e.printStackTrace();
		} catch (IOException e) {
			log.debug("装载失败");
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		log.debug(String.format("完成装载题目url|%d个", questionQueue.size()));
	}

	public static boolean crawlingSubjectPages() {
		initQueue();
		Callable<Boolean> task = new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				boolean result = false;
				while (!questionQueue.isEmpty()) {
					String subjectUrl = questionQueue.peek();
					Map<String, String> map = null;
					try {
						map = extractBasicInfoOfOneSuject(subjectUrl);
					} catch (Exception e) {
						log.warn(String.format("抓取试题页面脚本信息失败:%s", subjectUrl));
					}
					if (map == null || map.size() != 3) {
						subjectUrl = questionQueue.poll();
						questionQueue.add(subjectUrl);
						log.warn("页面脚本信息提取有误,重新入队:" + subjectUrl);
					} else {
						log.info(String.format("成功获取试题%s的三个基本信息| uuid:%s| entityId%s| answersCount|%s", subjectUrl, map.get("uuid"), map.get("entityId"), map.get("answersCount")));
						if (saveAllAnswersAndRepliesOfOneSubject(map)) {
							questionQueue.remove();// 爬取完成后出队
						} else {
							subjectUrl = questionQueue.poll();
							questionQueue.add(subjectUrl);
							log.warn("试题回复或评论数据下载失败,重新入队:" + subjectUrl);
						}
					}
					log.info("队列剩余元素个数:" + questionQueue.size());
				}
				result = true;
				return result;
			}
		};
		boolean result = false;
		result = TimeLimitTask.timeLimitTask(task, CrawlerConfig.MAX_TIME, TimeUnit.MINUTES);

		// 主线程自动等待任务结束.
		log.debug("任务结束后队列剩余元素个数:" + questionQueue.size());
		log.info(result == true ? "爬虫正常结束" : "爬虫超时退出");
		return result;
	}

	/**
	 * 获取subject的基本信息: entityId(异步请求时题目的标识), uuid(题目的url中题目的标识),
	 * answersCount(总评论数)
	 * 
	 * @param subjectUrl
	 * @return
	 */
	public static Map<String, String> extractBasicInfoOfOneSuject(String subjectUrl) {
		Map<String, String> map = new HashMap<>();
		Document doc = CrawlerNoCookie.getPageContent(subjectUrl, "get");
		Elements scripts = doc.getElementsByTag("script");
		for (Element sc : scripts) {
			// 取得<script>中 的JS变量数组
			String[] data = sc.data().split("var");
			// 取得单个JS变量
			for (String variable : data) {
				if (variable.contains("window.comment")) {
					for (String line : variable.split("\n")) {
						if (line.startsWith("id:")) {
							map.put("entityId", line.substring(line.indexOf(':') + 1, line.lastIndexOf(',')).trim());
						} else if (line.startsWith("uuid:")) {
							map.put("uuid", line.substring(line.indexOf('\'') + 1, line.lastIndexOf('\'')).trim());
						} else if (line.startsWith("count:")) {
							map.put("answersCount", line.substring(line.indexOf(':') + 1, line.lastIndexOf(',')).trim());
						}
					}
					break;
				}
			}
		}

		return map;
	}

	/**
	 * 保存试题所有的answers和所有的replies
	 * 
	 * @param map
	 */
	public static boolean saveAllAnswersAndRepliesOfOneSubject(Map<String, String> map) {
		boolean isSuccess = true;
		String uuid = map.get("uuid");
		int answersCount = Integer.valueOf(map.get("answersCount"));
		int pages = (answersCount % 20 == 0) ? answersCount / 20 : (answersCount / 20 + 1);
		String entityId = map.get("entityId");
		for (int i = 1; i <= pages; i++) {
			File file = Paths.get(CrawlerConfig.SUBJECTS_DATA, uuid, "answers" + answersCount, entityId, "page-" + i + ".json").toFile();
			String ajaxUrl = CrawlerConfig.ANSWERS_JSON_DATA_AJAX_URL.replace("{entityId}", entityId).replace("{page}", i + "");
			try {
				String answersJson = CrawlerNoCookie.getJsonContent(ajaxUrl, "post");
				FileEx.saveByWriter(answersJson, file);
				log.info(String.format("成功保存试题:%s|第%d页的answers|保存路径:%s", uuid, i, file.toString()));
				crawlingAllReplies(uuid, answersJson, i);
			} catch (Exception e) {
				isSuccess = false;
				log.warn(String.format("下载试题相关信息失败|uuid:%s", map.get("uuid")));
			}
		}
		return isSuccess;
	}

	/**
	 * 保存试题的回答对应的所有的replies
	 * 
	 * @param uuid
	 * @param answersJson
	 */
	public static void crawlingAllReplies(String uuid, String answersJson, int answerPage) {
		String repliesDir = Paths.get(CrawlerConfig.SUBJECTS_DATA, uuid, "replies").toString();
		Map<Integer, Integer> commentIdAndComentmCnt = extractAnswerIdAndCommentCntByJson(answersJson);
		for (Integer commentId : commentIdAndComentmCnt.keySet()) {
			int totalCnt = commentIdAndComentmCnt.get(commentId);
			if (totalCnt > 0) {
				int page = 1;
				String ajaxUrl = null;
				do {
					ajaxUrl = CrawlerConfig.REPLY_JSON_DATA_AJAX_URL.replace("{entityId}", commentId + "").replace("{page}", page + "");
					File file = Paths.get(repliesDir, "to-" + commentId + "(" + totalCnt + ")", "page-" + page + ".json").toFile();
					if (file.exists()) {
						if (CrawlerConfig.OVERWRITE) {
							FileEx.saveByWriter(CrawlerNoCookie.getJsonContent(ajaxUrl, "excute"), file);
						}
					} else {
						FileEx.saveByWriter(CrawlerNoCookie.getJsonContent(ajaxUrl, "excute"), file);
					}
					log.debug(String.format("试题uuid:%s|回答:%s|第%d页回复下载成功|保存路径:%s", uuid, commentId, page, file));
				} while (page++ * 10 < totalCnt);
			} else {
				File file = Paths.get(repliesDir, "to-" + commentId + "(0)").toFile();
				if (!file.exists()) {
					FileEx.createFile(file);
				}
				log.debug(String.format("试题uuid:%s|回答%s|回复为0", uuid, commentId));
			}
		}
		log.info(String.format("成功保存试题%s第%d页的replies", uuid, answerPage));
	}

	/**
	 * 提取每个answer的id和对应的回复总数
	 * 
	 * @param answersJson
	 *            answers的json数据
	 * @return
	 */
	public static Map<Integer, Integer> extractAnswerIdAndCommentCntByJson(String answersJson) {
		String formatJson = answersJson.replaceAll("(?<=\"content\":).*?(?=\"id\":)", "\"\",");
		Map<Integer, Integer> commentIdAndComentmCnt = new HashMap<>();
		try {
			JSONObject jsonObject = JSONObject.parseObject(formatJson);
			JSONArray comments = jsonObject.getJSONArray("comments");
			for (int i = 0; i < comments.size(); i++) {
				JSONObject comment = (JSONObject) comments.get(i);
				commentIdAndComentmCnt.put(Integer.valueOf(comment.getString("id")), Integer.valueOf(comment.getString("commentCnt")));
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			log.error("Json解析失败");
			log.error("格式化之前的answersJson");
			log.error(answersJson);
			log.error("格式化之后的answersJson");
			log.error(formatJson);
			commentIdAndComentmCnt = extractAnswerIdAndCommentCntByRegex(answersJson);
		}
		return commentIdAndComentmCnt;
	}

	/**
	 * 使用正则表示提取每个answer的id和对应的回复总数
	 * 
	 * @param answersJson
	 * @return
	 */
	public static Map<Integer, Integer> extractAnswerIdAndCommentCntByRegex(String answersJson) {
		Map<Integer, Integer> commentIdAndComentmCnt = new HashMap<>();
		Pattern pattern_entityId = Pattern.compile("(?<=\"id\":)[\\s0-9]*(?=,)");
		Matcher matcher1 = pattern_entityId.matcher(answersJson);
		Pattern pattern_commentCnt = Pattern.compile("(?<=\"commentCnt\":)[\\s0-9]*(?=,)");
		Matcher matcher2 = pattern_commentCnt.matcher(answersJson);
		int count = 0;
		while (matcher1.find() && matcher2.find()) {
			int entityId = Integer.valueOf(matcher1.group());
			int commentCnt = Integer.valueOf(matcher2.group());
			log.warn(String.format("entityId:%d|commentCnt:%d", entityId, commentCnt));
			commentIdAndComentmCnt.put(entityId, commentCnt);
			count++;
		}
		if (count == commentIdAndComentmCnt.size()) {
			log.warn("正则提取json中数据成功");
		} else {
			log.warn("正则提取json中数据失败");
		}
		return commentIdAndComentmCnt;
	}
}
