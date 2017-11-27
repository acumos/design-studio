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

import org.acumos.designstudio.ce.exceptionhandler.CustomException;
import org.acumos.designstudio.ce.service.AcumosCatalogServiceImpl;
import org.acumos.designstudio.ce.util.EELFLoggerDelegator;
import org.acumos.nexus.client.RepositoryLocation;
import org.junit.Test;

/**
 * 
 * 
 *
 */
public class ArtfactDetailsControllerTest {
	private static EELFLoggerDelegator logger = EELFLoggerDelegator.getLogger(ArtfactDetailsControllerTest.class);
	private final String url = "http://localhost:8000/ccds";
	private final String user = "ccds_client";
	private final String pass = "ccds_client";

	RepositoryLocation repositoryLocation = null;
	private final String solutionId = "010646b2-0298-4d25-9571-b775c3737bb6"; 
	private final String version = "1";
	private final String userId = "e57490ed-8462-4a77-ab39-157138dfbda8";
	private final String artifactType = "TG";

	@Test
	public void fetchJsonTOSCA() {

		AcumosCatalogServiceImpl acumosCatalogService = new AcumosCatalogServiceImpl();

		acumosCatalogService.getRestClient(url, user, pass);
		acumosCatalogService.getNexusClient(getRepositoryLocationInstance());
		String result = "";
		try {
			result = acumosCatalogService.readArtifact(userId, solutionId, version, artifactType);
			System.out.println("fetchJsonTOSCA=========" + result);
			logger.info("fetchJsonTOSCA" + result);
		} catch (CustomException e) {
			logger.debug(EELFLoggerDelegator.errorLogger, e.getMessage());
		}

	}

	@Test
	public void saveCompositeSolution() {
		AcumosCatalogServiceImpl acumosCatalogService = new AcumosCatalogServiceImpl();
		String result = acumosCatalogService.saveCompositeSolution();
		logger.info("saveCompositeSolution()" + result);
	}

	/**
	 * 
	 * @return
	 */
	private RepositoryLocation getRepositoryLocationInstance() {
		repositoryLocation = new RepositoryLocation();
		repositoryLocation.setId("1");
		repositoryLocation.setUrl(
				"http://localhost:8081/repository/repo_acumos_model_maven");
		repositoryLocation.setUsername("acumos_model_rw");
		repositoryLocation.setPassword("not4you");
		return repositoryLocation;
	}

}
