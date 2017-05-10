package com.sicnu.yudidi.service;

import java.io.IOException;
import java.io.Writer;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.sicnu.yudidi.utils.task.TimeLimitTask;

public class RecommendationServlet extends HttpServlet {

	private final static Logger logger = Logger.getLogger(TimeLimitTask.class);
	private static final long serialVersionUID = 1L;

	/**
	 * 应用层约定:
	 * 0:失败
	 * 01：用户不存在
	 * 02：答题数不足，不满足推荐条件
	 * 03：推荐超时
	 * 1:成功
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("utf-8");
		resp.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");
		final String userId = req.getParameter("uid");
		logger.debug(String.format("extract uid:%s", userId));
		Writer writer = resp.getWriter();
		String json = getJson(userId);
		if (json == null) {
			writer.write("{\"data:\"[],\"subject\":Time out,please try again.}");
			return;
		}
		writer.write(json);
	}
	public String getJson2(final String userId) {
		Callable<String> task = new Callable<String>() {
			@Override
			public String call() throws Exception {
				String json = null;
				json = Recommendation.generateJson(userId);
				return json;
			}
		};
		String json  = TimeLimitTask.timeLimitTaskString(task, 1, TimeUnit.MINUTES);
		return json;
	}
	public String getJson(final String userId) {
		Callable<String> task = new Callable<String>() {
			@Override
			public String call() throws Exception {
				String json = null;
				json = Recommendation.generateJson(userId);
				return json;
			}
		};
		String string = null;
		ExecutorService threadPool = Executors.newCachedThreadPool();
		Future<String> future = null;
		try {
			future = threadPool.submit(task);
			string = future.get(2, TimeUnit.MINUTES);
		} catch (TimeoutException e) {
			logger.error("定时任务超时");
			logger.error(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			logger.error("定时任务处理失败");
			logger.error(e.getMessage());
			e.printStackTrace();
		} finally {
			threadPool.shutdownNow();
		}
		return string;
	}
}
