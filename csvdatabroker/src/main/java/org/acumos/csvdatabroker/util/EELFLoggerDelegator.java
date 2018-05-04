/*-
 * ===============LICENSE_START=======================================================
 * Acumos
 * ===================================================================================
 * Copyright (C) 2017 - 2018 AT&T Intellectual Property & Tech Mahindra. All rights reserved.
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

package org.acumos.csvdatabroker.util;

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

/**
 * Extends the EELF logger so the output includes the CLASS NAME, which the base
 * implementation does not provide by default. Example usage:
 * 
 * <pre>
 * private final EELFLoggerDelegator logger = EELFLoggerDelegator.getLogger(MyClass.class);
 * ..
 * void methodName() {
 *   try {
 *     String result = doWork();
 *     logger.debug(EELFLoggerDelegator.debugLogger, "methodName: result is {} ", result);
 *   }
 *   catch (Exception ex) {
 *     logger.error(EELFLoggerDelegator.errorLogger, "methodName failed", ex);
 *   }
 * }
 * </pre>
 *
 */
public class EELFLoggerDelegator extends SLF4jWrapper implements EELFLogger {

	public static final EELFLogger errorLogger = EELFManager.getInstance().getErrorLogger();
	public static final EELFLogger applicationLogger = EELFManager.getInstance().getApplicationLogger();
	public static final EELFLogger debugLogger = EELFManager.getInstance().getDebugLogger();
	// Usage of the audit and metrics loggers is required in certain environments
	public static final EELFLogger auditLogger = EELFManager.getInstance().getAuditLogger();
	public static final EELFLogger metricsLogger = EELFManager.getInstance().getMetricsLogger();

	private static final String MDC_CLASS_NAME = "ClassName";
	private String className;
	private static ConcurrentMap<String, EELFLoggerDelegator> classMap = new ConcurrentHashMap<>();

	/**
	 * Builds an instance for the specified class.
	 * 
	 * @param className
	 *            Class name
	 */
	public EELFLoggerDelegator(final String className) {
		super(className);
		this.className = className;
	}

	/**
	 * Convenience method that gets a logger for the specified class.
	 * 
	 * @see #getLogger(String)
	 * 
	 * @param clazz
	 *            class
	 * @return Instance of EELFLoggerDelegator
	 */
	public static EELFLoggerDelegator getLogger(Class<?> clazz) {
		return getLogger(clazz.getName());
	}

	/**
	 * Gets a logger for the specified class name. If the logger does not already
	 * exist in the map, this creates a new logger.
	 * 
	 * @param className
	 *            If null or empty, uses EELFLoggerDelegator as the class name.
	 * @return Instance of EELFLoggerDelegator
	 */
	public static EELFLoggerDelegator getLogger(final String className) {
		// Silence a Sonar complaint about reusing parameter.
		String displayClassName = className;
		if (displayClassName == null || displayClassName.length() == 0)
			displayClassName = EELFLoggerDelegator.class.getName();
		EELFLoggerDelegator delegate = classMap.get(displayClassName);
		if (delegate == null) {
			delegate = new EELFLoggerDelegator(displayClassName);
			classMap.put(displayClassName, delegate);
		}
		return delegate;
	}

	/**
	 * Logs a message at the lowest level: trace.
	 * 
	 * @param logger
	 *            Logger to use
	 * @param msg
	 *            message to log
	 */
	public void trace(EELFLogger logger, String msg) {
		MDC.put(MDC_CLASS_NAME, className);
		logger.trace(msg);
		MDC.remove(MDC_CLASS_NAME);
	}

	/**
	 * Logs a message with parameters at the lowest level: trace.
	 * 
	 * @param logger
	 *            Logger to use
	 * @param msg
	 *            message to log
	 * @param arguments
	 *            arguments to interpolate into message
	 */
	public void trace(EELFLogger logger, String msg, Object... arguments) {
		MDC.put(MDC_CLASS_NAME, className);
		logger.trace(msg, arguments);
		MDC.remove(MDC_CLASS_NAME);
	}

	/**
	 * Logs a message and throwable at the lowest level: trace.
	 * 
	 * @param logger
	 *            Logger to use
	 * @param msg
	 *            message to log
	 * @param th
	 *            throwable to show as a stack trace
	 */
	public void trace(EELFLogger logger, String msg, Throwable th) {
		MDC.put(MDC_CLASS_NAME, className);
		logger.trace(msg, th);
		MDC.remove(MDC_CLASS_NAME);
	}

	/**
	 * Logs a message at the second-lowest level: debug.
	 * 
	 * @param logger
	 *            Logger to use
	 * @param msg
	 *            message to log
	 */
	public void debug(EELFLogger logger, String msg) {
		MDC.put(MDC_CLASS_NAME, className);
		logger.debug(msg);
		MDC.remove(MDC_CLASS_NAME);
	}

	/**
	 * Logs a message with parameters at the second-lowest level: debug.
	 * 
	 * @param logger
	 *            Logger to use
	 * @param msg
	 *            message to log
	 * @param arguments
	 *            arguments to interpolate into message
	 */
	public void debug(EELFLogger logger, String msg, Object... arguments) {
		MDC.put(MDC_CLASS_NAME, className);
		logger.debug(msg, arguments);
		MDC.remove(MDC_CLASS_NAME);
	}

	/**
	 * Logs a message and throwable at the second-lowest level: debug.
	 * 
	 * @param logger
	 *            Logger to use
	 * @param msg
	 *            message to log
	 * @param th
	 *            throwable to show as a stack trace
	 */
	public void debug(EELFLogger logger, String msg, Throwable th) {
		MDC.put(MDC_CLASS_NAME, className);
		logger.debug(msg, th);
		MDC.remove(MDC_CLASS_NAME);
	}

	/**
	 * Logs a message at info level.
	 * 
	 * @param logger
	 *            Logger to use
	 * @param msg
	 *            message to log
	 */
	public void info(EELFLogger logger, String msg) {
		MDC.put(MDC_CLASS_NAME, className);
		logger.info(msg);
		MDC.remove(MDC_CLASS_NAME);
	}

	/**
	 * Logs a message with parameters at info level.
	 *
	 * @param logger
	 *            Logger to use
	 * @param msg
	 *            message to log
	 * @param arguments
	 *            arguments to interpolate into message
	 */
	public void info(EELFLogger logger, String msg, Object... arguments) {
		MDC.put(MDC_CLASS_NAME, className);
		logger.info(msg, arguments);
		MDC.remove(MDC_CLASS_NAME);
	}

	/**
	 * Logs a message and throwable at info level.
	 * 
	 * @param logger
	 *            Logger to use
	 * @param msg
	 *            message to log
	 * @param th
	 *            throwable to show as a stack trace
	 */
	public void info(EELFLogger logger, String msg, Throwable th) {
		MDC.put(MDC_CLASS_NAME, className);
		logger.info(msg, th);
		MDC.remove(MDC_CLASS_NAME);
	}

	/**
	 * Logs a message at warn level.
	 * 
	 * @param logger
	 *            Logger to use
	 * @param msg
	 *            message to log
	 */
	public void warn(EELFLogger logger, String msg) {
		MDC.put(MDC_CLASS_NAME, className);
		logger.warn(msg);
		MDC.remove(MDC_CLASS_NAME);
	}

	/**
	 * Logs a message with parameters at warn level.
	 * 
	 * @param logger
	 *            Logger to use
	 * @param msg
	 *            message to log
	 * @param arguments
	 *            arguments to interpolate into message
	 */
	public void warn(EELFLogger logger, String msg, Object... arguments) {
		MDC.put(MDC_CLASS_NAME, className);
		logger.warn(msg, arguments);
		MDC.remove(MDC_CLASS_NAME);
	}

	/**
	 * Logs a message and throwable at warn level.
	 * 
	 * @param logger
	 *            Logger to use
	 * @param msg
	 *            message to log
	 * @param th
	 *            throwable to show as a stack trace
	 */
	public void warn(EELFLogger logger, String msg, Throwable th) {
		MDC.put(MDC_CLASS_NAME, className);
		logger.warn(msg, th);
		MDC.remove(MDC_CLASS_NAME);
	}

	/**
	 * Logs a message at error level.
	 * 
	 * @param logger
	 *            Logger to use
	 * @param msg
	 *            message to log
	 */
	public void error(EELFLogger logger, String msg) {
		MDC.put(MDC_CLASS_NAME, className);
		logger.error(msg);
		MDC.remove(MDC_CLASS_NAME);
	}

	/**
	 * Logs a message with parameters at error level.
	 * 
	 * @param logger
	 *            Logger to use
	 * @param msg
	 *            message to log
	 * @param arguments
	 *            arguments to interpolate into message
	 */
	public void error(EELFLogger logger, String msg, Object... arguments) {
		MDC.put(MDC_CLASS_NAME, className);
		logger.error(msg, arguments);
		MDC.remove(MDC_CLASS_NAME);
	}

	/**
	 * Logs a message and throwable at error level.
	 * 
	 * @param logger
	 *            Logger to use
	 * @param msg
	 *            message to log
	 * @param th
	 *            throwable to show as a stack trace
	 */
	public void error(EELFLogger logger, String msg, Throwable th) {
		MDC.put(MDC_CLASS_NAME, className);
		logger.error(msg, th);
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
			error(errorLogger, "setGlobalLoggingContext failed", e);
		}
	}

}
