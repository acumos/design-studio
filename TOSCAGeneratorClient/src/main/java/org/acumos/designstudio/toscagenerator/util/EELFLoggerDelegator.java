/*-
 * ===============LICENSE_START=======================================================
 * Acumos
 * ===================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property & Tech Mahindra. All rights reserved.
 * ===================================================================================
 * This Acumos software file is distributed by AT&T and Tech Mahindra
 * under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * This file is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ===============LICENSE_END=========================================================
 */

package org.acumos.designstudio.toscagenerator.util;

import static com.att.eelf.configuration.Configuration.MDC_SERVER_FQDN;
import static com.att.eelf.configuration.Configuration.MDC_SERVER_IP_ADDRESS;
import static com.att.eelf.configuration.Configuration.MDC_SERVICE_INSTANCE_ID;

import java.net.InetAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.MDC;

import com.att.eelf.configuration.EELFLogger;
import com.att.eelf.configuration.EELFManager;
import com.att.eelf.configuration.SLF4jWrapper;

public class EELFLoggerDelegator extends SLF4jWrapper implements EELFLogger {

	public static EELFLogger errorLogger = EELFManager.getInstance().getErrorLogger();
	public static EELFLogger applicationLogger = EELFManager.getInstance().getApplicationLogger();
	public static EELFLogger debugLogger = EELFManager.getInstance().getDebugLogger();

	private static final String MDC_CLASS_NAME = "ClassName";
	private String className;
	private static ConcurrentMap<String, EELFLoggerDelegator> classMap = new ConcurrentHashMap<String, EELFLoggerDelegator>();

	/**
	 * 
	 * @param className
	 *            Class name
	 */
	public EELFLoggerDelegator(String className) {
		super(className);
		this.className = className;
	}

	/**
	 * 
	 * @param clazz
	 *            Class
	 * @return Logger for specified class
	 */
	public static EELFLoggerDelegator getLogger(Class<?> clazz) {
		return getLogger(clazz.getName());
	}

	/**
	 * Gets a logger for the specified class name. If the logger does not already
	 * exist in the map, this creates a new logger.
	 * 
	 * @param className
	 *            Class name
	 * @return Logger for specified class
	 */
	public static EELFLoggerDelegator getLogger(String className) {
		if (className == null || className == "")
			className = EELFLoggerDelegator.class.getName();
		EELFLoggerDelegator delegate = classMap.get(className);
		if (delegate == null) {
			delegate = new EELFLoggerDelegator(className);
			classMap.put(className, delegate);
		}
		return delegate;
	}

	/**
	 * Logs a message at the lowest level: trace.
	 * 
	 * @param logger
	 *            EELFLogger
	 * @param msg
	 *            Message to log
	 */
	public void trace(EELFLogger logger, String msg) {
		if (logger.isTraceEnabled()) {
			MDC.put(MDC_CLASS_NAME, className);
			logger.trace(msg);
			MDC.remove(MDC_CLASS_NAME);
		}
	}

	/**
	 * 
	 * @param logger
	 *            EELFLogger
	 * @param msg
	 *            Message to log
	 * @param arguments
	 *            Values to interpolate
	 */
	public void trace(EELFLogger logger, String msg, Object... arguments) {
		if (logger.isTraceEnabled()) {
			MDC.put(MDC_CLASS_NAME, className);
			logger.trace(msg, arguments);
			MDC.remove(MDC_CLASS_NAME);
		}
	}

	/**
	 * 
	 * @param logger
	 *            EELFLogger
	 * @param msg
	 *            Message to log
	 * @param th
	 *            Throwable to log
	 */
	public void trace(EELFLogger logger, String msg, Throwable th) {
		if (logger.isTraceEnabled()) {
			MDC.put(MDC_CLASS_NAME, className);
			logger.trace(msg, th);
			MDC.remove(MDC_CLASS_NAME);
		}
	}

	/**
	 * 
	 * @param logger
	 *            EELFLogger
	 * @param msg
	 *            Message to log
	 */
	public void debug(EELFLogger logger, String msg) {
		if (logger.isDebugEnabled()) {
			MDC.put(MDC_CLASS_NAME, className);
			logger.debug(msg);
			MDC.remove(MDC_CLASS_NAME);
		}
	}

	/**
	 * 
	 * @param logger
	 *            EELFLogger
	 * @param msg
	 *            Message to log
	 * @param arguments
	 *            Values to interpolate
	 */
	public void debug(EELFLogger logger, String msg, Object... arguments) {
		if (logger.isDebugEnabled()) {
			MDC.put(MDC_CLASS_NAME, className);
			logger.debug(msg, arguments);
			MDC.remove(MDC_CLASS_NAME);
		}
	}

	/**
	 * 
	 * @param logger
	 *            EELFLogger
	 * @param msg
	 *            Message to log
	 * @param th
	 *            Throwable to log
	 */
	public void debug(EELFLogger logger, String msg, Throwable th) {
		if (logger.isDebugEnabled()) {
			MDC.put(MDC_CLASS_NAME, className);
			logger.debug(msg, th);
			MDC.remove(MDC_CLASS_NAME);
		}
	}

	/**
	 * 
	 * @param logger
	 *            EELFLogger
	 * @param msg
	 *            Message to log
	 */
	public void info(EELFLogger logger, String msg) {
		MDC.put(MDC_CLASS_NAME, className);
		logger.info(msg);
		MDC.remove(MDC_CLASS_NAME);
	}

	/**
	 * 
	 * @param logger
	 *            EELFLogger
	 * @param msg
	 *            Message to log
	 * @param arguments
	 *            Values to interpolate
	 */
	public void info(EELFLogger logger, String msg, Object... arguments) {
		MDC.put(MDC_CLASS_NAME, className);
		logger.info(msg, arguments);
		MDC.remove(MDC_CLASS_NAME);
	}

	/**
	 * 
	 * @param logger
	 *            EELFLogger
	 * @param msg
	 *            Message to log
	 * @param th
	 *            Throwable to log
	 */
	public void info(EELFLogger logger, String msg, Throwable th) {
		MDC.put(MDC_CLASS_NAME, className);
		logger.info(msg, th);
		MDC.remove(MDC_CLASS_NAME);
	}

	/**
	 * 
	 * @param logger
	 *            EELFLogger
	 * @param msg
	 *            Message to log
	 */
	public void warn(EELFLogger logger, String msg) {
		MDC.put(MDC_CLASS_NAME, className);
		logger.warn(msg);
		MDC.remove(MDC_CLASS_NAME);
	}

	/**
	 * 
	 * @param logger
	 *            EELFLogger
	 * @param msg
	 *            Message to log
	 * @param arguments
	 *            Values to interpolate
	 */
	public void warn(EELFLogger logger, String msg, Object... arguments) {
		MDC.put(MDC_CLASS_NAME, className);
		logger.warn(msg, arguments);
		MDC.remove(MDC_CLASS_NAME);
	}

	/**
	 * 
	 * @param logger
	 *            EELFLogger
	 * @param msg
	 *            Message to log
	 * @param th
	 *            Throwable to log
	 */
	public void warn(EELFLogger logger, String msg, Throwable th) {
		MDC.put(MDC_CLASS_NAME, className);
		logger.warn(msg, th);
		MDC.remove(MDC_CLASS_NAME);
	}

	/**
	 * 
	 * @param logger
	 *            EELFLogger
	 * @param msg
	 *            Message to log
	 */
	public void error(EELFLogger logger, String msg) {
		MDC.put(MDC_CLASS_NAME, className);
		logger.error(msg);
		MDC.remove(MDC_CLASS_NAME);
	}

	/**
	 * 
	 * @param logger
	 *            EELFLogger
	 * @param msg
	 *            Message to log
	 * @param arguments
	 *            Values to interpolate
	 */
	public void error(EELFLogger logger, String msg, Object... arguments) {
		MDC.put(MDC_CLASS_NAME, className);
		logger.warn(msg, arguments);
		MDC.remove(MDC_CLASS_NAME);
	}

	/**
	 * 
	 * @param logger
	 *            EELFLogger
	 * @param msg
	 *            Message to log
	 * @param th
	 *            Throwable to log
	 */
	public void error(EELFLogger logger, String msg, Throwable th) {
		MDC.put(MDC_CLASS_NAME, className);
		logger.warn(msg, th);
		MDC.remove(MDC_CLASS_NAME);
	}

	/**
	 * Initializes the logger context.
	 */
	public void init() {
		setGlobalLoggingContext();
		final String msg = "############################ Logging is started. ############################";
		info(applicationLogger, msg);
		error(errorLogger, msg);
		debug(debugLogger, msg);
	}

	/**
	 * Loads all the default logging fields into the MDC context.
	 */
	private void setGlobalLoggingContext() {
		MDC.put(MDC_SERVICE_INSTANCE_ID, "");
		try {
			MDC.put(MDC_SERVER_FQDN, InetAddress.getLocalHost().getHostName());
			MDC.put(MDC_SERVER_IP_ADDRESS, InetAddress.getLocalHost().getHostAddress());
		} catch (Exception e) {
		}
	}

}
