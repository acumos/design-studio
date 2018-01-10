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

/**
 * 
 */
package org.acumos.designstudio.ce.exceptionhandler;

import org.acumos.designstudio.ce.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 *
 */
public class ServiceException extends AcumosException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	Properties props;

	/**
	 * 
	 */
	public ServiceException() {
		super();
	}

	/**
	 * 
	 * @param errmessage
	 *            Error message
	 */
	public ServiceException(String errmessage) {
		super(errmessage);
	}

	/**
	 * @param message
	 *            Error message
	 * @param errorCode
	 *            Error code
	 * @param errorDesc
	 *            Error description
	 * @param cause
	 *            Throwable
	 * @param param
	 *            Object
	 * 
	 */
	public ServiceException(String message, String errorCode, String errorDesc, Throwable cause, Object param) {
		super(message, errorCode, errorDesc, cause, param);
	}

	/**
	 * 
	 * @param message
	 *            Error message
	 * @param errorCode
	 *            Error code
	 * @param errorDesc
	 *            Error description
	 * @param cause
	 *            Throwable
	 * 
	 * 
	 */
	public ServiceException(String message, String errorCode, String errorDesc, Throwable cause) {
		super(message, errorCode, errorDesc, cause);
	}

	/**
	 * @param message
	 *            Error message
	 * @param errorCode
	 *            Error code
	 * @param errorDesc
	 *            Error description
	 * 
	 */
	public ServiceException(String message, String errorCode, String errorDesc) {
		super(message, errorCode, errorDesc);
	}

}
