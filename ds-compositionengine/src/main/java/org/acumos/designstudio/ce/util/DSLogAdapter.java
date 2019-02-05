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

package org.acumos.designstudio.ce.util;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.MDC;

/**
* Extensible adapter for cheaply meeting logging obligations using an
* SLF4J facade.
*/
public class DSLogAdapter {

	/** String constant for messages <tt>ENTERING</tt>, <tt>EXITING</tt>, etc. */
	private static final String EMPTY_MESSAGE = "";

	/** Logger delegate. */
	private Logger mLogger;

	/**
	 * Construct adapter.
	 *
	 * @param logger
	 *            non-null logger.
	 */
	public DSLogAdapter(final Logger logger) {
		this.mLogger = checkNotNull(logger);
	}

	/**
	 * Get logger.
	 *
	 * @return unwrapped logger.
	 */
	public Logger unwrap() {
		return this.mLogger;
	}

	/**
	 * Report <tt>ENTERING</tt> marker.
	 *
	 * @param request
	 *            non-null incoming request (wrapper).
	 * @param format
	 *            SLF4J format string
	 * @param arguments
	 *            Optional arguments as referenced in the format string
	 * @return this.
	 */
	public DSLogAdapter entering(final RequestAdapter<?> request, String format, Object... arguments) {
		checkNotNull(request);
		// Default the service name.
		this.setEnteringMDCs(request);
		/** Marker To be Implemented in Release B **/
		//this.mLogger.info(LogConstants.Markers.ENTRY, format, arguments);
		return this;
	}

	/**
	 * Report <tt>ENTERING</tt> marker.
	 *
	 * @param request
	 *            non-null incoming request.
	 * @return this.
	 */
	public DSLogAdapter entering(final HttpServletRequest request) {
		return this.entering(new HttpServletRequestAdapter(checkNotNull(request)), EMPTY_MESSAGE);
	}

	public DSLogAdapter entering(final HttpServletRequest request, String format, Object... arguments) {
		return this.entering(new HttpServletRequestAdapter(checkNotNull(request)), format, arguments);
	}

	/**
	 * Report <tt>EXITING</tt> marker.
	 *
	 * @return this.
	 */
	public DSLogAdapter exiting() {
		
		MDC.clear();
		return this;
	}	

	/**
	 * Set MDCs that persist for the duration of an invocation.
	 *
	 * It would be better to roll this into {@link #entering}, like with
	 * {@link #exiting}. Then it would be easier to do, but it would mean more work.
	 *
	 * @param request
	 *            incoming HTTP request.
	 * @return this.
	 */
	public DSLogAdapter setEnteringMDCs(final RequestAdapter<?> request) {

		// Extract MDC values from standard HTTP headers.
		final String requestID = defaultToUUID(request.getHeader(DSLogConstants.Headers.REQUEST_ID));
		
		// Set standard MDCs. 
		MDC.put(DSLogConstants.MDCs.REQUEST_ID, requestID);
		return this;
	}

	/**
	 * Dependency-free nullcheck.
	 *
	 * @param in
	 *            to be checked.
	 * @param <T>
	 *            argument (and return) type.
	 * @return input arg.
	 */
	protected static <T> T checkNotNull(final T in) {
		if (in == null) {
			throw new NullPointerException();
		}
		return in;
	}

	/**
	 * Dependency-free string default.
	 *
	 * @param in
	 *            to be filtered.
	 * @return input string or null.
	 */
	protected static String defaultToEmpty(final Object in) {
		if (in == null) {
			return "";
		}
		return in.toString();
	}

	/**
	 * Dependency-free string default.
	 *
	 * @param in
	 *            to be filtered.
	 * @return input string or null.
	 */
	protected static String defaultToUUID(final String in) {
		if (in == null) {
			return UUID.randomUUID().toString();
		}
		return in;
	}
	
	/**
	 * Adapter for reading information from an incoming HTTP request.
	 *
	 * Incoming is generally easy, because in most cases you'll be able to get your
	 * hands on the <tt>HttpServletRequest</tt>.
	 *
	 * Perhaps should be generalized to refer to constants instead of requiring the
	 * implementation of specific methods.
	 *
	 * @param <T>
	 *            type, for chaining.
	 */
	public interface RequestAdapter<T extends RequestAdapter<?>> {

		/**
		 * Get header by name.
		 * 
		 * @param name
		 *            header name.
		 * @return header value, or null.
		 */
		String getHeader(String name);
	}

	
	public static class HttpServletRequestAdapter implements RequestAdapter<HttpServletRequestAdapter> {

		/** Wrapped HTTP request. */
		private final HttpServletRequest mRequest;

		/**
		 * Construct adapter for HTTP request.
		 * 
		 * @param request
		 *            to be wrapped;
		 */
		public HttpServletRequestAdapter(final HttpServletRequest request) {
			this.mRequest = checkNotNull(request);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getHeader(final String name) {
			return this.mRequest.getHeader(name);
		}



	}	
}