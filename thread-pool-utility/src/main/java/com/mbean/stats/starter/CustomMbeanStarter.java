package com.mbean.stats.starter;

import java.lang.management.ManagementFactory;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mbean.stats.mbean.ThreadPoolAgent;

/**
 * This is a Java class which regiters the MBeans for Thread & Thread Pool dump
 * 
 * @author dinesh.prajapati
 *
 */
public class CustomMbeanStarter {
	Logger logger = LoggerFactory.getLogger(CustomMbeanStarter.class);

	public static final String JMX_MBEAN_NAME = "ThreadMbeanStats:name=ThreadPoolAgent";

	/**
	 * This constructor is loaded at server startup. This will register the
	 * Mbean for Thread Pool info
	 */
	public CustomMbeanStarter() {
		super();
		logger.info("Constructor CustomMbeanStarter call started.");
		try {
			init();
		} catch (Exception e) {
			logger.error("start() method encountered Error : {}" + new Object[] { e });
			e.printStackTrace();
		}

		logger.info("Constructor CustomMbeanStarter call completed.");
	}

	/**
	 * This method registers the MBean
	 * 
	 * @throws MalformedObjectNameException
	 * @throws InstanceAlreadyExistsException
	 * @throws MBeanRegistrationException
	 * @throws NotCompliantMBeanException
	 * @throws InterruptedException
	 */
	public void init() throws MalformedObjectNameException, InstanceAlreadyExistsException, MBeanRegistrationException,
			NotCompliantMBeanException, InterruptedException {
		MBeanServer mbs = null;
		mbs = ManagementFactory.getPlatformMBeanServer();
		ThreadPoolAgent agent = new ThreadPoolAgent();

		ObjectName agentName;
		agentName = new ObjectName(JMX_MBEAN_NAME);

		if (mbs.isRegistered(agentName)) {
			logger.info("Mbean {} is already registred.", new Object[] { JMX_MBEAN_NAME });
		} else {
			mbs.registerMBean(agent, agentName);
			logger.info("Mbean {} registered sucessfully.", new Object[] { JMX_MBEAN_NAME });
		}

	}
}
