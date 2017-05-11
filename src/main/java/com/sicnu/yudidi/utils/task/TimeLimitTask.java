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
	 * @param timeLimit
	 *            时间限制(单位: 秒)
	 * @param unit
	 *            时间的单位
	 * @return 任务返回的字符串
	 */
	public static String timeLimitTaskString(Callable<String> runnable, long timeLimit, TimeUnit unit) {
		String string = null;
		ExecutorService threadPool = Executors.newFixedThreadPool(1);
		Future<String> future = null;
		try {
			future = threadPool.submit(runnable);
			string = future.get(timeLimit, unit);
			logger.info(String.format("定时推荐任务成功结束，结果为%s", string));
			logger.debug(String.format("timeLimitTaskString result == %s", string));
		} catch (TimeoutException e) {
			logger.error("定时任务超时,取消任务");
			boolean cancelResult =  future.cancel(true);
			logger.debug(String.format("任务是否取消成功 %s", cancelResult));
		} catch (Exception e) {
			logger.error("定时任务处理失败");
			logger.error(e.getMessage());
		} finally {
			logger.info(String.format("尝试关闭线程池中的线程"));
			threadPool.shutdown();
			threadPool.shutdownNow();
			logger.info(String.format("ExecutorService.isShutdown() == %s", threadPool.isShutdown()));
		}
		return string;
	}

	/**
	 * 执行一个有时间限制的任务
	 * 
	 * @param runnable
	 *            待执行的任务
	 * @param seconds
	 *            超时时间(单位: 秒)
	 * @return
	 */
	public static Boolean timeLimitTask(Callable<Boolean> runnable, long timeLimit, TimeUnit unit) {
		Boolean result = Boolean.FALSE;
		ExecutorService threadPool = Executors.newFixedThreadPool(1);
		Future<Boolean> future = null;
		try {
			future = threadPool.submit(runnable);
			result = future.get(timeLimit, unit);
		} catch (TimeoutException e) {
			logger.error("定时任务超时,取消任务");
	        future.cancel(true);
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
