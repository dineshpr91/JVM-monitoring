package com.mbean.stats.main;

import com.mbean.stats.starter.CustomMbeanStarter;

/**
 * This is the main class to test the MBeans functionality.
 * @author dinesh.prajapati
 *
 */
public class MbeanTestMethod {

	/**
	 * This method is just for testing the MBeans functionality, do not use this in running application. Steps to see the Mbeans working: </br>
	 * 1) Run this method </br>
	 * 2) Start the Jconsole </br>
	 * 3) In Local process click on com.mbean.stats.main.MbeanTestMethod and connect to it</br>
	 * 4) Go to MBeans Tab -> Expand ThreadMbeanStats --> ThreadPoolAgent --> Click on operations</br>
	 * 5) Click on ThreadPoolDump --> Provide the full path of the file where you want to get the dump, e.g. C:/logs/thread_dump.txt --> Click on the getThreadPoolDump button</br>
	 * 6) Now go to the path C:/logs/thread_dump.txt and you will see all the Thread pools in the application along with the percent of various threads in a pool</br>
	 * 
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		//The constructor call will register the Mbean "ThreadMbeanStats:name=ThreadPoolAgent"
		CustomMbeanStarter cms = new CustomMbeanStarter();
		for(;;) {
			Thread.sleep(10000);
		}
	}
}
