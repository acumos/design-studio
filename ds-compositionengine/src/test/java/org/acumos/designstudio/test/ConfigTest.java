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

package org.acumos.designstudio.test;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.lang.invoke.MethodHandles;

import org.acumos.cds.client.CommonDataServiceRestClientImpl;
import org.acumos.designstudio.ce.config.AppConfig;
import org.acumos.designstudio.ce.config.HandlerInterceptorConfiguration;
import org.acumos.designstudio.ce.util.ConfigurationProperties;
import org.acumos.nexus.client.NexusArtifactClient;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

public class ConfigTest {
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	/*// CCDS TechMDev(8003) UserId, change it if the CCDS port changes.
	String userId = "8fcc3384-e3f8-4520-af1c-413d9495a154";
	// The local path folder which is there in local project Directory.
	String localpath = "./src/test/resources/";
	// For meanwhile hard coding the sessionID.
	String sessionId = "4f91545a-e674-46af-a4ad-d6514f41de9b";*/
	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	@InjectMocks
	AppConfig appConfig;
	
	@Mock
	org.acumos.designstudio.ce.util.Properties props;
	
	@Mock
    CommonDataServiceRestClientImpl cmnDataService;
	
	@Mock
	ConfigurationProperties confprops;
	
	@Mock
	HandlerInterceptorConfiguration handlerInterceptorConfiguration;
	
	@Test
	/**
	 * 	 Test Nexus Client Configuration class
	 */
	public void nexusArtifactClientTest(){
		try {
			InterceptorRegistry registry = new InterceptorRegistry();
			Mockito.doNothing().when(handlerInterceptorConfiguration).addInterceptors(registry);
			when(confprops.getNexusendpointurl()).thenReturn("http://localhost:8081/repository/repo_cognita_model_maven/");
			when(confprops.getNexususername()).thenReturn("cognita");
			when(confprops.getNexuspassword()).thenReturn("cognita");
			NexusArtifactClient nexusArtifactClient = appConfig.nexusArtifactClient();
			assertNotNull(nexusArtifactClient);
		} catch (Exception e) {
			logger.error("Exception in NexusArtifactClientTest() testcase", e);
		}
	}

	
}
