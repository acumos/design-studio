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
package org.acumos.sqldatabroker.vo;

import org.acumos.sqldatabroker.exceptionhandler.ServiceException;
import org.junit.Test;

public class ExceptionTest {

	@Test
	public void ConfigurationTest() throws ServiceException {
		Configuration configuration = new Configuration();
		configuration.setLocal_system_data_file_path("test");
		configuration.setTarget_system_url("/www.google.com");
		configuration.getLocalFilePath();
		configuration.getRemoteDir();
		configuration.setTarget_system_url("www.google.com/");
		configuration.getRemoteDir();
		configuration.setTarget_system_url(".www.google.com");
		configuration.getRemoteDir();
		configuration.getRemoteFilePath();
	}

	@Test(expected = ServiceException.class)
	public void RemoteFilePathException() throws ServiceException {
		Configuration configuration = new Configuration();
		configuration.setLocal_system_data_file_path("http://foo.com");
		configuration.setTarget_system_url("http://localhost:8080/abc.html?xyz=hbkbkj|kjbjkbkj kjbkj");
		configuration.getRemoteFilePath();

	}
	@Test(expected = ServiceException.class)
	public void URISyntaxException() throws ServiceException {
		Configuration configuration = new Configuration();
		configuration.setLocal_system_data_file_path("http://foo.com");
		configuration.setTarget_system_url("http://localhost:8080/abc.html?xyz=hbkbkj|kjbjkbkj kjbkj");
		configuration.getRemoteDir();

	}

}
