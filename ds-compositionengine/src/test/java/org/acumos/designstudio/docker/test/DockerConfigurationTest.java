/*-
 * ===============LICENSE_START=======================================================
 * Acumos
 * ===================================================================================
 * Copyright (C) 2019 AT&T Intellectual
 * 						Property & Tech
 * 						Mahindra. All rights reserved.
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

package org.acumos.designstudio.docker.test;

import java.lang.invoke.MethodHandles;

import org.acumos.designstudio.ce.docker.DockerConfiguration;
import org.acumos.designstudio.ce.exceptionhandler.ServiceException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(MockitoJUnitRunner.class)
public class DockerConfigurationTest {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	@Before
	public void setUp() {
		 MockitoAnnotations.initMocks(this);
	}
	
	DockerConfiguration dockerConfiguration = new DockerConfiguration();
	
	@Test
	public void configurationTest(){
		dockerConfiguration.setHost("localhost");
		dockerConfiguration.getHost();
		dockerConfiguration.getImagetagPrefix();
		dockerConfiguration.getPort();
		dockerConfiguration.getRegistryUrl();
		dockerConfiguration.setPort(8080);
		dockerConfiguration.setRequestTimeout(10);
		dockerConfiguration.getMaxTotalConnections();
		dockerConfiguration.getRequestTimeout();
		dockerConfiguration.setRequestTimeout(10);
		dockerConfiguration.getCertPath();
		dockerConfiguration.getImagetagPrefix();
		dockerConfiguration.getRegistryEmail();
		dockerConfiguration.getRegistryPassword();
		dockerConfiguration.getRegistryUsername();
		dockerConfiguration.getCmdExecFactory();
		dockerConfiguration.getMaxPerRouteConnections();
		dockerConfiguration.isTlsVerify();
		dockerConfiguration.getConfig();
		dockerConfiguration.getApiVersion();
		dockerConfiguration.setSocket(true);
		dockerConfiguration.isSocket();
		try {
			dockerConfiguration.toUrl();
			dockerConfiguration.setHost(null);
			dockerConfiguration.toUrl();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		try {
			dockerConfiguration.setHost("localhost");
			dockerConfiguration.toUrl();
			dockerConfiguration.setPort(null);
			dockerConfiguration.toUrl();
		} catch (ServiceException e) {
			logger.error("Service Exception occured while Docker Configuration");
		}
		Assert.assertNotNull(dockerConfiguration);
	}
	


}
