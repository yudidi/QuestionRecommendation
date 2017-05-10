package com.sicnu.yudidi.utils.task;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;

public class TimeLimitTask {
	private final static Logger logger = Logger.getLogger(TimeLimitTask.class);

	/**
	 * 执行一个有时间限制的任务
	 * @param runnable
	 *            待执行的任务
	 * @param seconds
	 *            超时时间(单位: 秒)
	 * @return
	 */
	public static String timeLimitTaskString(Callable<String> runnable, long timeLimit, TimeUnit unit) {
		String string = null;
		ExecutorService threadPool = Executors.newCachedThreadPool();
		Future<String> future = null;
		try {
			future = threadPool.submit(runnable);
			string = future.get(timeLimit, unit);
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
	
	/**
	 * 执行一个有时间限制的任务
	 * @param runnable
	 *            待执行的任务
	 * @param seconds
	 *            超时时间(单位: 秒)
	 * @return
	 */
	public static Boolean timeLimitTask(Callable<Boolean> runnable, long timeLimit, TimeUnit unit) {
		Boolean result = Boolean.FALSE;
		ExecutorService threadPool = Executors.newCachedThreadPool();
		Future<Boolean> future = null;
		try {
			future = threadPool.submit(runnable);
			result = future.get(timeLimit, unit);
		} catch (TimeoutException e) {
			logger.error("定时任务超时");
			logger.error(e.getMessage());
		} catch (Exception e) {
			logger.error("定时任务处理失败");
			logger.error(e.getMessage());
		} finally {
			threadPool.shutdownNow();
		}
		return result;
	}

	/**
	 * 推迟initialDelay时间单位后,每隔delay执行一次任务
	 * 
	 * @param task
	 * @param initialDelay
	 *            从现在开始到第一次执行时,推迟的时间
	 * @param delay
	 *            两次任务执行间隔,即周期
	 * @param unit
	 *            时间的单位
	 */
	public static void periodicTask(Runnable task, long initialDelay, long delay, TimeUnit unit) {
		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
		service.scheduleAtFixedRate(task, 0, 1, TimeUnit.SECONDS);
	}

}
