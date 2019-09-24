/*
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

import org.acumos.designstudio.ce.util.DSLogConstants.MDCs;
import org.acumos.designstudio.ce.util.DSLogConstants.ResponseSeverity;
import org.acumos.designstudio.ce.util.DSLogConstants.ResponseStatus;
import org.slf4j.Logger;
import org.slf4j.MDC;
import org.slf4j.event.Level;

/**
* Extensible adapter for cheaply meeting logging obligations using an
* SLF4J facade.
*/
public class DSLogAdapter {

	/** String constant for messages ENTERING, EXITING, etc. */
	private static final String EMPTY_MESSAGE = "";

	/** Logger delegate. */
	private Logger mLogger;

	/**
	 * Overrideable descriptor for the response returned by the service doing the
	 * logging.
	 */
	private ResponseDescriptor mResponseDescriptor = new ResponseDescriptor();

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
	 * Report ENTERING marker.
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
	 * Report ENTERING marker.
	 *
	 * @param request
	 *            non-null incoming request.
	 * @return this.
	 */
	public DSLogAdapter entering(final HttpServletRequest request) {
		return this.entering(new HttpServletRequestAdapter(checkNotNull(request)), EMPTY_MESSAGE);
	}

	/**
	 *  Report ENTERING marker.
	 * @param request
	 * 		non-null incoming request.
	 * @param format
	 * 		SLF4J format string
	 * @param arguments
	 * 		Optional arguments as referenced in the format string
	 * @return
	 * 		DSLogAdapter
	 */
	public DSLogAdapter entering(final HttpServletRequest request, String format, Object... arguments) {
		return this.entering(new HttpServletRequestAdapter(checkNotNull(request)), format, arguments);
	}

	/**
	 * Report EXITING marker.
	 *
	 * @return this.
	 */
	public DSLogAdapter exiting() {
		
		MDC.clear();
		return this;
	}	

	/**
	 * Get descriptor, for setting response details.
	 * 
	 * @return non-null descriptor.
	 */
	public ResponseDescriptor getResponseDescriptor() {
		return checkNotNull(this.mResponseDescriptor);
	}

	/**
	 * Override {@link ResponseDescriptor}.
	 * 
	 * @param d
	 *            non-null override.
	 * @return this.
	 */
	public DSLogAdapter setResponseDescriptor(final ResponseDescriptor d) {
		this.mResponseDescriptor = checkNotNull(d);
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
		final String partnerName = defaultToEmpty(request.getHeader(DSLogConstants.Headers.PARTNER_NAME));
		final String targetEntity = defaultToEmpty(request.getHeader(DSLogConstants.MDCs.TARGET_ENTITY));
		final String targetServiceName = defaultToEmpty(request.getHeader(DSLogConstants.MDCs.TARGET_SERVICE_NAME));
		
		// Get the UserName
		final String userName = defaultToEmpty(request.getUser());
		MDC.put(DSLogConstants.MDCs.USER,userName);
		
		DSLogAdapter.setDefaultMDCs();

		// Set standard MDCs. 
		MDC.put(DSLogConstants.MDCs.REQUEST_ID, requestID);
		if (!partnerName.isEmpty())
			MDC.put(DSLogConstants.MDCs.PARTNER_NAME, partnerName);
		MDC.put(DSLogConstants.MDCs.CLIENT_IP_ADDRESS, defaultToEmpty(request.getClientAddress()));
		MDC.put(DSLogConstants.MDCs.SERVER_FQDN, defaultToEmpty(request.getServerAddress()));
		
		MDC.put(DSLogConstants.MDCs.TARGET_ENTITY, targetEntity);
		MDC.put(DSLogConstants.MDCs.TARGET_SERVICE_NAME, targetServiceName);
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
	 * Response is different in that response MDCs are normally only reported once,
	 * for a single log message. (But there's no method for clearing them, because
	 * this is only expected to be called during exiting.)
	 */
	public static class ResponseDescriptor {

		/** Response errorcode. */
		protected String mCode;

		/** Response description. */
		protected String mDescription;

		/** Response severity. */
		protected Level mSeverity;

		/** Response status, of {COMPLETED, ERROR}. */
		protected DSLogConstants.ResponseStatus mStatus;

		/**
		 * Setter.
		 *
		 * @param code
		 *            response (error) code.
		 * @return this.
		 */
		public ResponseDescriptor setResponseCode(final String code) {
			this.mCode = code;
			return this;
		}

		/**
		 * Setter.
		 *
		 * @param description
		 *            response description.
		 * @return this.
		 */
		public ResponseDescriptor setResponseDescription(final String description) {
			this.mDescription = description;
			return this;
		}

		/**
		 * Setter.
		 *
		 * @param severity
		 *            response outcome severity.
		 * @return this.
		 */
		public ResponseDescriptor setResponseSeverity(final Level severity) {
			this.mSeverity = severity;
			return this;
		}

		/**
		 * Setter.
		 *
		 * @param status
		 *            response overall status.
		 * @return this.
		 */
		public ResponseDescriptor setResponseStatus(final DSLogConstants.ResponseStatus status) {
			this.mStatus = status;
			return this;
		}

		/**
		 * Overrideable method to set MDCs based on property values.
		 */
		public void setMDCs() {
			String response = defaultToEmpty(this.mCode);
			String description = defaultToEmpty(this.mDescription);
			String severity = defaultToEmpty(this.mSeverity);
			String status = defaultToEmpty(this.mStatus);
			if (!response.isEmpty())
				MDC.put(DSLogConstants.MDCs.RESPONSE_CODE, response);
			if (!description.isEmpty())
				MDC.put(DSLogConstants.MDCs.RESPONSE_DESCRIPTION, description);
			if (!severity.isEmpty())
				MDC.put(DSLogConstants.MDCs.RESPONSE_SEVERITY, severity);
			if (!status.isEmpty())
				MDC.put(DSLogConstants.MDCs.RESPONSE_STATUS_CODE, status);
		}
	}

	/**
	 * Adapter for reading information from an incoming HTTP request.
	 *
	 * Incoming is generally easy, because in most cases you'll be able to get your
	 * hands on the HttpServletRequest.
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

		/**
		 * Get client address.
		 * 
		 * @return address, if available.
		 */
		String getClientAddress();

		/**
		 * Get server address.
		 * 
		 * @return address, if available.
		 */
		String getServerAddress();

		/**
		 * Get default service name, from service URI.
		 * 
		 * @return service name default.
		 */
		String getRequestURI();
		
		/**
		 * Get UserName
		 * 
		 * @return service name default.
		 */
		Object getUser();
	}
	
	/**
	 * Overrideable method to set MDCs based on property values.
	 */
	public static void setDefaultMDCs() {
		MDC.put(MDCs.RESPONSE_CODE, "200");
		MDC.put(MDCs.RESPONSE_DESCRIPTION, "200 OK");
		MDC.put(MDCs.RESPONSE_SEVERITY, ResponseSeverity.INFO.toString());
		MDC.put(MDCs.RESPONSE_STATUS_CODE, ResponseStatus.INPROGRESS.toString());
	}
	
	/**
	 * RequestAdapter for reading information from an incoming HTTPServlet request.
	 *
	 * Incoming is generally easy, because in most cases you'll be able to get your
	 * hands on the HttpServletRequestAdapter.
	 *
	 * Perhaps should be generalized to refer to constants instead of requiring the
	 * implementation of specific methods.
	 *
	 */
	
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

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getClientAddress() {
			return this.mRequest.getRemoteAddr();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getServerAddress() {
			return this.mRequest.getServerName();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getRequestURI() {
			return this.mRequest.getRequestURI();
		}

		@Override
		public Object getUser() {
			return this.mRequest.getAttribute(DSLogConstants.MDCs.USER);
		}		
	}	
}