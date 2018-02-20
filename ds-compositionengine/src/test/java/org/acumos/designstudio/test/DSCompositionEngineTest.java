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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.acumos.cds.client.CommonDataServiceRestClientImpl;
import org.acumos.cds.client.ICommonDataServiceRestClient;
import org.acumos.cds.domain.MLPSolution;
import org.acumos.cds.domain.MLPSolutionRevision;
import org.acumos.cds.domain.MLPUser;
import org.acumos.designstudio.ce.util.EELFLoggerDelegator;
import org.acumos.designstudio.ce.vo.DSSolution;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * 
 *
 */
public class DSCompositionEngineTest {
	private static EELFLoggerDelegator logger = EELFLoggerDelegator.getLogger(DSCompositionEngineTest.class);
	private String url = "";
	private String user = "";
	private String pass = "";
	public static Properties CONFIG = new Properties();
	ICommonDataServiceRestClient client = null;

	@Before
	/**
	 * This method is used to set default values for the instance of
	 * ICommonDataServiceRestClient and NexusArtifactClient by passing common
	 * data service and nexus url, username and password respectively
	 * 
	 * @throws Exception
	 */
	public void createClient() throws Exception {
		CONFIG.load(SolutionControllerTest.class.getResourceAsStream("/application.properties"));

		url = CONFIG.getProperty("cmndatasvc.cmndatasvcendpoinurlTest");
		user = CONFIG.getProperty("cmndatasvc.cmndatasvcuserTest");
		pass = CONFIG.getProperty("cmndatasvc.cmndatasvcpwdTest");
		client = CommonDataServiceRestClientImpl.getInstance(url.toString(), user, pass);
	}

	/**
	 * 
	 * The test case is used to get the list of all the active solutions that
	 * belongs to the user from the database.
	 * 
	 * @throws Exception
	 */
	/*
	@Test
	public void testGetSolutions() {
		String result = "[";
		List<MLPSolution> mlpSolutions = null;
		ICommonDataServiceRestClient cmnDataService = client;
		Map<String, Object> queryParameters = new HashMap<>();
		queryParameters.put("active", Boolean.TRUE);
		mlpSolutions = cmnDataService.searchSolutions(queryParameters, false);
		DSSolution dssolution = null;
		List<DSSolution> dsSolutions = new ArrayList<DSSolution>();
		List<String> solutionIds = new ArrayList<String>();
		List<MLPSolutionRevision> mlpSolRevisions = null;
		String solutionId = null;
		StringBuilder strBuilder = new StringBuilder();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		if (null == mlpSolutions) {
			assertNull(mlpSolutions);
			logger.debug(EELFLoggerDelegator.debugLogger,
					" CommonDataService returned null Solution list");
		} else if (mlpSolutions.isEmpty()) {
			logger.debug(EELFLoggerDelegator.debugLogger,
					" CommonDataService returned empty Solution list");
		} else {
			logger.debug(EELFLoggerDelegator.debugLogger,
					" CommonDataService returned Solution list of size : " + mlpSolutions.size());
			mlpSolRevisions = new ArrayList<MLPSolutionRevision>();
			for (MLPSolution mlpsol : mlpSolutions) {
				solutionId = mlpsol.getSolutionId();
				solutionIds.add(solutionId);
				mlpSolRevisions = cmnDataService.getSolutionRevisions(solutionId);
				String userId = mlpsol.getOwnerId();
				MLPUser user = cmnDataService.getUser(userId);
				String userName = user.getFirstName() + " " + user.getLastName();
				if (null == mlpSolRevisions) {
					logger.debug(EELFLoggerDelegator.debugLogger,
							" CommonDataService returned null SolutionRevision list");
				} else if (mlpSolRevisions.isEmpty()) {
					logger.debug(EELFLoggerDelegator.debugLogger,
							" CommonDataService returned empty SolutionRevision list");
				} else {
					logger.debug(EELFLoggerDelegator.debugLogger,
							" CommonDataService returned SolutionRevision list of size : "
									+ mlpSolRevisions.size());
					for (MLPSolutionRevision mlpSolRevision : mlpSolRevisions) {
						dssolution = new DSSolution();
						dssolution.setSolutionId(mlpsol.getSolutionId());
						dssolution.setSolutionRevisionId(mlpSolRevision.getRevisionId());
						dssolution.setCreatedDate(sdf.format(mlpSolRevision.getCreated().getTime()));
						dssolution.setIcon(null);
						dssolution.setSolutionName(mlpsol.getName());
						dssolution.setProvider(mlpsol.getProvider());
						dssolution.setToolKit(mlpsol.getToolkitTypeCode());
						dssolution.setCategory(mlpsol.getModelTypeCode());
						dssolution.setDescription(mlpsol.getDescription());
						dssolution.setVisibilityLevel(mlpsol.getAccessTypeCode());
						dssolution.setVersion(mlpSolRevision.getVersion());
						dssolution.setOnBoarder(userName);
						dssolution.setAuthor(userName);
						dsSolutions.add(dssolution);
						strBuilder.append(dssolution.toJsonString());
						strBuilder.append(",");
						assertNotNull(dssolution);
					}
				}
			}
		}
		assertNotNull(strBuilder);
		if (strBuilder.length() > 1) {
			result = result + strBuilder.substring(0, strBuilder.length() - 1);
		}
		result = result + "]";
	}*/
}
