package com.mbean.stats.model;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Custom classed created for getting ThreadPool statistics. This class gives
 * the total threads, percent of runnable, waiting, timed_waiting, new, blocked
 * and terminated threads
 * 
 * @author dinesh.prajapati
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomThreadPoolInfo {

	@JsonIgnore
	private List<String> threads = new ArrayList<>();

	@JsonIgnore
	private int runnableThreads;

	@JsonIgnore
	private int newThreads;

	@JsonIgnore
	private int waitingThreads;

	@JsonIgnore
	private int timedWaitingThreads;

	@JsonIgnore
	private int blockedThreads;

	@JsonIgnore
	private int terminatedThreads;

	/**
	 * The first thread of the thread pool
	 */
	@JsonProperty(value = "threadPoolName")
	private String threadPoolName;

	/**
	 * Total no of threads in a Thread pool
	 */
	@JsonProperty(value = "threadPoolSize")
	private int totalThreads;

	@JsonProperty(value = "percent_runnable_threads")
	private double percent_runnable_threads;

	@JsonProperty(value = "percent_new_threads")
	private double percent_new_threads;

	@JsonProperty(value = "percent_waiting_threads")
	private double percent_waiting_threads;

	@JsonProperty(value = "percent_timed_waiting_threads")
	private double percent_timed_waiting_threads;

	@JsonProperty(value = "percent_blocked_threads")
	private double percent_blocked_threads;

	@JsonProperty(value = "percent_terminated_threads")
	private double percent_terminated_threads;

	
	public List<String> getThreads() {
		return threads;
	}

	public void setThreads(List<String> threads) {
		this.threads = threads;
	}

	public int getTotalThreads() {
		return this.totalThreads;
	}

	public void setTotalThreads(int totalThreads) {
		this.totalThreads = totalThreads;
	}

	public int getRunnableThreads() {
		return runnableThreads;
	}

	public void setRunnableThreads(int runnableThreads) {
		this.runnableThreads = runnableThreads;
	}

	public int getNewThreads() {
		return newThreads;
	}

	public void setNewThreads(int newThreads) {
		this.newThreads = newThreads;
	}

	public int getWaitingThreads() {
		return waitingThreads;
	}

	public void setWaitingThreads(int waitingThreads) {
		this.waitingThreads = waitingThreads;
	}

	public int getTimedWaitingThreads() {
		return timedWaitingThreads;
	}

	public void setTimedWaitingThreads(int timedWaitingThreads) {
		this.timedWaitingThreads = timedWaitingThreads;
	}

	public int getBlockedThreads() {
		return blockedThreads;
	}

	public void setBlockedThreads(int blockedThreads) {
		this.blockedThreads = blockedThreads;
	}

	public int getTerminatedThreads() {
		return terminatedThreads;
	}

	public void setTerminatedThreads(int terminatedThreads) {
		this.terminatedThreads = terminatedThreads;
	}

	public double getPercent_runnable_threads() {
		return ((double) this.runnableThreads / (double) this.totalThreads) * 100;
	}

	public double getPercent_waiting_threads() {
		return ((double) this.waitingThreads / (double) this.totalThreads) * 100;
	}

	public double getPercent_blocked_threads() {
		return ((double) this.blockedThreads / (double) this.totalThreads) * 100;
	}

	public double getPercent_new_threads() {
		return ((double) this.newThreads / (double) this.totalThreads) * 100;
	}

	public double getPercent_timed_waiting_threads() {
		return ((double) this.timedWaitingThreads / (double) this.totalThreads) * 100;
	}

	public double getPercent_terminated_threads() {
		return ((double) this.terminatedThreads / (double) this.totalThreads) * 100;
	}

	public String getThreadPoolName() {
		return this.threads.get(0);
	}

	@Override
	public String toString() {
		return "{Thread pool : " + this.threads + ", Thread Pool Size : " + this.totalThreads
				+ ", percent_runnable_threads : " + this.getPercent_runnable_threads() + ", percent_new_thread : "
				+ this.getPercent_new_threads() + ", percent_waiting_thread : " + this.getPercent_waiting_threads()
				+ ", percent_timed_waiting_threads : " + this.getPercent_timed_waiting_threads()
				+ ", percent_blocked_threads : " + this.getPercent_blocked_threads() + ", percent_terminated_threads : "
				+ this.getPercent_terminated_threads() + "} \n";
	}

}
