package com.mbean.stats.mbean;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mbean.stats.model.CustomThreadPoolInfo;
import com.mbean.stats.utils.JsonUtils;

/**
 * This class is an MBean agent which exposes the operations to get Thread Pool
 * dump, thread dump and particular Thread info
 * 
 * @author dinesh.prajapati
 *
 */
public class ThreadPoolAgent implements ThreadPoolAgentMBean, Runnable {

	Logger logger = LoggerFactory.getLogger(ThreadPoolAgent.class);

	public static final String NEWLINE = System.getProperty("line.separator");

	public static final String REGEX = "(\\d{1}|\\d{2})";
	public static final String BLANK_SPACE = "";
	public static final String MULE_HOME = "MULE_HOME";
	public static final String THREAD_POOL_DUMP = "THREAD_POOL_DUMP";
	public static final String THREAD_DUMP = "THREAD_DUMP";
	public static final String LOGS = "logs";
	public static final String FILE_DATE_FORMAT = "yyyy-MM-dd-HH-mm-ss";
	public static final String DUMP_FILE_EXT = ".tdump";
	public static final String THREAD_DUMP_FILE_PREFIX = "thread-dump-";
	public static final String THREAD_POOL_DUMP_FILE_PREFIX = "thread-pool-dump-";

	/**
	 * This method dumps the Thread pools info in the file given at filePath
	 * 
	 * @param filePath
	 *            is the full path of the file where the thread poll dump is to
	 *            be taken
	 * @return the message indicating total active threads and thread pool count
	 */
	@Override
	public String getThreadPoolDump(String filePath) {
		logger.info("getAllThreadPools method started.");

		long time = System.nanoTime();

		Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
		int totalThreads = threadSet.size();

		Map<String, CustomThreadPoolInfo> threadPoolMap = getThreadPoolMap(threadSet);

		String fullFilePath = getFilePath(filePath, THREAD_POOL_DUMP);

		writeTofile(fullFilePath, JsonUtils.objectToJson(threadPoolMap.values()));

		int threadPoolCount = threadPoolMap.size();

		// Clear Maps
		threadPoolMap.clear();
		threadSet.clear();

		logger.info("getAllThreadPools method completed.");
		logger.info("Time taken in processing threadpool map " + (System.nanoTime() - time) + " ns");

		return "Total active thread : " + totalThreads + ", total thread pools : " + threadPoolCount
				+ ", Please see the thread dump at path : " + fullFilePath;
	}

	
	/**
	 * This is the JMX attribute which gives the Thread pool dump
	 */
	@Override
	public String getThreadPoolDump() {
		logger.info("getThreadPoolDump method started.");

		long time = System.nanoTime();

		Set<Thread> threadSet = Thread.getAllStackTraces().keySet();

		Map<String, CustomThreadPoolInfo> threadPoolMap = getThreadPoolMap(threadSet);

		logger.info("getThreadPoolDump method completed.");

		logger.info("Time taken in processing threadpool map " + (System.nanoTime() - time) + " ns");

		// Clear Maps
		threadSet.clear();

		return JsonUtils.objectToJson(threadPoolMap.values());
	}

	private Map<String, CustomThreadPoolInfo> getThreadPoolMap(Set<Thread> threadSet) {

		logger.info("Active threads count : " + threadSet.size());

		Map<String, CustomThreadPoolInfo> threadPoolMap = new HashMap<>();

		for (Thread th : threadSet) {
			// To map pools
			String thName = th.getName();

			if (threadPoolMap.containsKey(thName.replaceAll(REGEX, BLANK_SPACE))) {

				CustomThreadPoolInfo thPoolInfo = threadPoolMap.get(thName.replaceAll(REGEX, BLANK_SPACE));

				thPoolInfo.getThreads().add(thName);
				setThreadCounts(thPoolInfo, th);

			} else {

				CustomThreadPoolInfo thPoolInfo = new CustomThreadPoolInfo();
				thPoolInfo.getThreads().add(thName);

				setThreadCounts(thPoolInfo, th);

				threadPoolMap.put(th.getName().replaceAll(REGEX, BLANK_SPACE), thPoolInfo);
			}
		}

		return threadPoolMap;
	}

	/**
	 * This method sets the count of various threads in CustomThreadPoolInfo
	 * model
	 * 
	 * @param thPoolInfo
	 *            The CustomThreadPoolInfo object
	 * @param th
	 *            The current Thread object
	 */
	private void setThreadCounts(CustomThreadPoolInfo thPoolInfo, Thread th) {

		thPoolInfo.setTotalThreads(thPoolInfo.getTotalThreads() + 1);

		if (th.getState().equals(Thread.State.RUNNABLE)) {
			thPoolInfo.setRunnableThreads(thPoolInfo.getRunnableThreads() + 1);
		}

		else if (th.getState().equals(Thread.State.NEW)) {
			thPoolInfo.setNewThreads((thPoolInfo.getNewThreads() + 1));
		}

		else if (th.getState().equals(Thread.State.WAITING)) {
			thPoolInfo.setWaitingThreads((thPoolInfo.getWaitingThreads() + 1));
		}

		else if (th.getState().equals(Thread.State.TIMED_WAITING)) {
			thPoolInfo.setTimedWaitingThreads((thPoolInfo.getTimedWaitingThreads() + 1));
		}

		else if (th.getState().equals(Thread.State.BLOCKED)) {
			thPoolInfo.setBlockedThreads((thPoolInfo.getBlockedThreads() + 1));
		}

		else if (th.getState().equals(Thread.State.TERMINATED)) {
			thPoolInfo.setTerminatedThreads((thPoolInfo.getTerminatedThreads() + 1));
		}
	}

	/**
	 * This method writes content to the file
	 * 
	 * @param filePath
	 * @param content
	 */
	private void writeTofile(String filePath, String content) {

		try (FileWriter fw = new FileWriter(filePath); BufferedWriter bw = new BufferedWriter(fw);) {
			bw.write(content);
		} catch (Exception e) {
			logger.error("Error while writing thread pool info to file {} : {}", new Object[] { filePath, e });
		}
	}

	/**
	 * This method gives the Thread dump for all the threads
	 */
	@Override
	public String getThreadDump(String filePath) {
		logger.info("GetThreadDump method started.");
		long time = System.nanoTime();

		ThreadMXBean bean = ManagementFactory.getThreadMXBean();
		logger.info("file path : " + filePath);
		int threadCount = bean.getThreadCount();
		logger.info("Thread count is : {}", threadCount);

		String fullFilePath = getFilePath(filePath, THREAD_DUMP);

		List<ThreadInfo> threadInfoList = Arrays.asList(bean.dumpAllThreads(true, true));

		writeTofile(fullFilePath, threadInfoList.toString());
		
		logger.info("Time taken in processing threadpool map " + (System.nanoTime() - time) + " ns");

		logger.info("GetThreadDump method completed.");

		return "Total no of threads : " + threadCount + ", Please see the thread dump at path : " + fullFilePath;

	}

	/**
	 * This method gives the Information of a particular thread
	 */
	@Override
	public String getThreadInfo(String threadName) {
		String threadInfo = "This thread doesn't exist";
		for (Thread t : Thread.getAllStackTraces().keySet()) {

			if (t.getName().equals(threadName)) {

				threadInfo = String.format(
						"Thread state is %s, thread group %s, is thread live : %b, is deamon thread : %b ",
						t.getState(), t.getThreadGroup(), t.isAlive(), t.isDaemon());
			}
		}
		return threadInfo;
	}

	@Override
	public void run() {
		// Do nothing
	}

	/**
	 * This method computes the file path where we need to write thread dump.
	 * 
	 * @param filePath
	 * @param fileType
	 * @return
	 */
	private String getFilePath(String filePath, String fileType) {
		if ((filePath != null && filePath.trim() != BLANK_SPACE)
				&& !(filePath != null && filePath.trim().equalsIgnoreCase("string"))) {
			return filePath;
		}

		// If the path is not present
		String folder = System.getenv(MULE_HOME);
		String timeStamp = new SimpleDateFormat(FILE_DATE_FORMAT).format(new Date());

		Path path = null;

		if (fileType.equals(THREAD_DUMP)) {
			path = Paths.get(folder, LOGS, THREAD_DUMP_FILE_PREFIX + timeStamp + DUMP_FILE_EXT);
		} else {
			path = Paths.get(folder, LOGS, THREAD_POOL_DUMP_FILE_PREFIX + timeStamp + DUMP_FILE_EXT);
		}
		return path.toString();
	}

	public String getThreadStats() {
		logger.info("getThreadStats method started.");
		long time = System.nanoTime();

		ThreadMXBean bean = ManagementFactory.getThreadMXBean();
		int peakThreadCount = bean.getPeakThreadCount();
		int totalThreadCount = bean.getThreadCount();

		int newThreads = 0;
		int runnableThreads = 0;
		int waitingThreads = 0;
		int timedWaitingThreads = 0;
		int blockedThreads = 0;
		int terminatedThreads = 0;

		// ThreadInfo [] threads = bean.dumpAllThreads(true, true);
		List<ThreadInfo> threadInfoList = Arrays.asList(bean.dumpAllThreads(true, true));

		for (ThreadInfo thInfo : threadInfoList) {
			Thread.State state = thInfo.getThreadState();

			if (state.equals(Thread.State.NEW)) {
				newThreads++;

			} else if (state.equals(Thread.State.RUNNABLE)) {
				runnableThreads++;

			} else if (state.equals(Thread.State.WAITING)) {
				waitingThreads++;

			} else if (state.equals(Thread.State.TIMED_WAITING)) {
				timedWaitingThreads++;

			} else if (state.equals(Thread.State.BLOCKED)) {
				blockedThreads++;

			} else if (state.equals(Thread.State.TERMINATED)) {
				terminatedThreads++;
			}
		}

		logger.info("Time taken in getting getThreadStats " + (System.nanoTime() - time) + " ns");

		logger.info("getThreadStats method completed.");

		return String.format(
				"Total thread count : %d, Peak thread count : %d, New threads : %d, Runnable threads : %d, Waiting threads : %d, "
						+ "Timed Waiting threads : %d, Blocked threads : %d, Terminated threads : %d",
				totalThreadCount, peakThreadCount, newThreads, runnableThreads, waitingThreads, timedWaitingThreads,
				blockedThreads, terminatedThreads);
	}
}
