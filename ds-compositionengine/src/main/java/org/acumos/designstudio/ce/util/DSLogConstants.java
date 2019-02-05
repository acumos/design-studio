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

/**
 * Constants for standard headers, MDCs, etc.
*/
public final class DSLogConstants {
	
	/**
	 * Hide and forbid construction.
	 */
	private DSLogConstants() {
		throw new UnsupportedOperationException();
	}	
	
	public static final class MDCs{
		
		/** MDC correlating messages for a logical transaction. */
		public static final String REQUEST_ID = "X-ACUMOS-Request-Id";
	}
	
	/**
	 * Header name constants.
	 */
	public static final class Headers {

		public static final String REQUEST_ID = "X-ACUMOS-Request-Id";
	}

}
