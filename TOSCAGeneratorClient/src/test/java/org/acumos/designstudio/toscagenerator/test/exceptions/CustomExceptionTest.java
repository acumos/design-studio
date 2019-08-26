/*-
 * ===============LICENSE_START=======================================================
 * Acumos
 * ===================================================================================
 * Copyright (C) 2019 AT&T Intellectual Property & Tech Mahindra. All rights reserved.
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

package org.acumos.designstudio.toscagenerator.test.exceptions;

import static org.junit.Assert.assertTrue;

import org.acumos.designstudio.toscagenerator.exceptionhandler.CustomException;
import org.junit.Test;

public class CustomExceptionTest extends CustomException {

	private static final long serialVersionUID = 5798679514458088970L;

	@Test
	public void customExceptionTest() {
		CustomExceptionTest customExceptionTest = new CustomExceptionTest();
		customExceptionTest.setErrorCode("test");
		assertTrue(customExceptionTest.getErrorCode() == "test");
		customExceptionTest.setErrorDesc("test");
		assertTrue(customExceptionTest.getErrorDesc() == "test");
	}
	

}
