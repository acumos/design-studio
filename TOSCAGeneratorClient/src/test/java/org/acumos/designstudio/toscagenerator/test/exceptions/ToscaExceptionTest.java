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

import static org.junit.Assert.assertNotNull;

import org.acumos.designstudio.toscagenerator.exceptionhandler.ControllerException;
import org.acumos.designstudio.toscagenerator.exceptionhandler.DAOException;
import org.acumos.designstudio.toscagenerator.exceptionhandler.ServiceException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class ToscaExceptionTest {

	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void TOSCAExceptionSTest(){
		ControllerException exp = new ControllerException("Not Found", "404", "Msg Not Found");
		exp = new ControllerException("Not Found", "404", "Msg Not Found", new Throwable());
		exp = new ControllerException("Not Found", "404", "Msg Not Found", new Throwable(),new Object());
		assertNotNull(exp);
		
		DAOException daoExcption = new DAOException("Not Found", "404", "Msg Not Found");
		daoExcption = new DAOException("Not Found", "404", "Msg Not Found", new Throwable());
		daoExcption = new DAOException("Not Found", "404", "Msg Not Found", new Throwable(),new Object());
		assertNotNull(daoExcption);
		
		ServiceException serviceException = new ServiceException("Not Found", "404", "Msg Not Found");
		serviceException = new ServiceException("Not Found", "404", "Msg Not Found", new Throwable());
		serviceException = new ServiceException("Not Found", "404", "Msg Not Found", new Throwable(),new Object());
		assertNotNull(serviceException);
		
		
	}
}
