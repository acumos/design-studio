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

package org.acumos.designstudio.toscagenerator.exceptionhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 
 *
 */
public abstract class AcumosException extends Exception {

	private static final long serialVersionUID = 5967973881519668649L;

	private static final Logger logger = LoggerFactory.getLogger(AcumosException.class);

	private  transient Object param;
	private  final String errorCode;
	private  final String errorDesc;

	public Object getParam() {
		return param;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getErrorDesc() {
		return errorDesc;
	}

	public AcumosException(String message, String errorCode, String errorDesc) {
		super(message);
		logger.debug(message);
		this.errorCode = errorCode;
		this.errorDesc = errorDesc;
	}

	public AcumosException(String message, String errorCode, String errorDesc, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
		this.errorDesc = errorDesc;
	}

	public AcumosException(String message, String errorCode, String errorDesc, Throwable cause, Object param) {
		super(message, cause);
		this.param = param;
		this.errorCode = errorCode;
		this.errorDesc = errorDesc;
	}

	public String getMessage1() {
		return (String) this.param;

	}

}
