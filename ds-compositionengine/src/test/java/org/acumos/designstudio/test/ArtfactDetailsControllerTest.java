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

import static org.junit.Assert.*;

import java.util.Properties;

import org.acumos.designstudio.ce.exceptionhandler.AcumosException;
import org.acumos.designstudio.ce.service.AcumosCatalogServiceImpl;
import org.acumos.designstudio.ce.util.EELFLoggerDelegator;
import org.acumos.nexus.client.RepositoryLocation;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * 
 *
 */
public class ArtfactDetailsControllerTest {
	private static EELFLoggerDelegator logger = EELFLoggerDelegator.getLogger(ArtfactDetailsControllerTest.class);
	private String url = "";
	private String user ="";
	private String pass = "";
	
	public static Properties CONFIG = new Properties();
	RepositoryLocation repositoryLocation = null;
	private final String solutionId = "010646b2-0298-4d25-9571-b775c3737bb6"; 
	private final String version = "1";
	private final String userId = "e57490ed-8462-4a77-ab39-157138dfbda8";
	private final String artifactType = "TG";

	@Before
	/**
	 * 
	 * @throws Exception
	 */
	public void createClient() throws Exception {
	    CONFIG.load(ArtfactDetailsControllerTest.class.getResourceAsStream("/application.properties"));
		url = CONFIG.getProperty("cmndatasvc.cmndatasvcendpoinurlTest");
		user = CONFIG.getProperty("cmndatasvc.cmndatasvcuserTest");
		pass = CONFIG.getProperty("cmndatasvc.cmndatasvcpwdTest");
		repositoryLocation = getRepositoryLocationInstance();
	}
	
	@Test
	public void fetchJsonTOSCA() {

		AcumosCatalogServiceImpl acumosCatalogService = new AcumosCatalogServiceImpl();
		acumosCatalogService.getRestClient(url, user, pass);
		acumosCatalogService.getNexusClient(repositoryLocation);
		String result = "";
		try {
			result = acumosCatalogService.readArtifact(userId, solutionId, version, artifactType);
			assertNotNull(result);
			logger.info("fetchJsonTOSCA" + result);
		} catch (AcumosException e) {
			logger.debug(EELFLoggerDelegator.errorLogger, e.getMessage());
		}

	}

	/**
	 * 
	 * @return
	 */
	private RepositoryLocation getRepositoryLocationInstance() {
		repositoryLocation = new RepositoryLocation();
		repositoryLocation.setId("1");
		repositoryLocation.setUrl(CONFIG.getProperty("nexus.nexusendpointurlTest"));
		repositoryLocation.setUsername(CONFIG.getProperty("nexus.nexususernameTest"));
		repositoryLocation.setPassword(CONFIG.getProperty("nexus.nexuspasswordTest"));
		return repositoryLocation;
	}
}
