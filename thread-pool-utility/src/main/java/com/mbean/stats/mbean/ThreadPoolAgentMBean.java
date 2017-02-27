package com.mbean.stats.mbean;

/**
 * This Interface is used as Mbean
 * 
 * @author dinesh.prajapati
 *
 */

public interface ThreadPoolAgentMBean {
	// "This method creates a thread pool dump to specified file path. If file
	// is not specified then the dump goes to <MULE_HOME>/logs"
	String getThreadPoolDump(String filePath);
	
	String getThreadPoolDump();

	String getThreadDump(String filePath);

	String getThreadInfo(String threadName);

	String getThreadStats();
}
