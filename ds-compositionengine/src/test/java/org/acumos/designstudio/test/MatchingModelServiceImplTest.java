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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.invoke.MethodHandles;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.acumos.cds.client.CommonDataServiceRestClientImpl;
import org.acumos.cds.domain.MLPSolution;
import org.acumos.cds.domain.MLPSolutionRevision;
import org.acumos.cds.domain.MLPTask;
import org.acumos.cds.domain.MLPUser;
import org.acumos.cds.transport.RestPageRequest;
import org.acumos.cds.transport.RestPageResponse;
import org.acumos.designstudio.ce.config.HandlerInterceptorConfiguration;
import org.acumos.designstudio.ce.exceptionhandler.ServiceException;
import org.acumos.designstudio.ce.service.MatchingModelServiceImpl;
import org.acumos.designstudio.ce.util.ConfigurationProperties;
import org.acumos.designstudio.ce.util.Properties;
import org.acumos.designstudio.ce.vo.matchingmodel.DSModelVO;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

public class MatchingModelServiceImplTest {
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	@InjectMocks
	private MatchingModelServiceImpl matchingModelServiceImpl;
	
	@Mock
	private ConfigurationProperties confprops;
	
	@Mock
	private Properties props;
	
	@Mock
	private HandlerInterceptorConfiguration handlerInterceptorConfiguration;
	
	@Mock
    private CommonDataServiceRestClientImpl cmnDataService;
	private String userId = "12345678-abcd-90ab-cdef-1234567890ab";
	
	private String solutionId = "84874435-d103-44c1-9451-d2b660fae766";
	
	private String revisionId = "84874435-d103-44c1-9451-d2b660fae766";
	
	@Before
	public void setUp() {
		 MockitoAnnotations.initMocks(this);
		 cmnDataService = mock(CommonDataServiceRestClientImpl.class);
	}

	//@Test
	public void getPublicDSModelsTest(){
		
		List<MLPSolution> mlpSolList = new ArrayList<MLPSolution>();
		MLPSolution mlpSol = mlpSolutionCommon();
		mlpSolList.add(mlpSol);
		
		MLPUser user = new MLPUser();
		user.setFirstName("Test");
		user.setLastName("Dev");
		List<MLPSolutionRevision> mlpSolRevList = new ArrayList<MLPSolutionRevision>();
		MLPSolutionRevision mlpSolRev = mlpSolutionRevisionsCommon();
		mlpSolRevList.add(mlpSolRev);
		
		MLPTask mlpTask = new MLPTask();
		mlpTask.setCreated(Instant.now());
		mlpTask.setModified(Instant.now());
		mlpTask.setName("MLPTask");
		mlpTask.setRevisionId(revisionId);
		mlpTask.setSolutionId(solutionId);
		mlpTask.setStatusCode("FS");
		mlpTask.setTaskCode("FS");
		mlpTask.setTaskId(12345L);
		mlpTask.setTrackingId("123");
		mlpTask.setUserId(userId);
		List<MLPTask> mlpTaskList = new ArrayList<>();
		mlpTaskList.add(mlpTask);
		
		try {
			when(confprops.getCdsCheckAttempt()).thenReturn(10);
			when(confprops.getCdsCheckInterval()).thenReturn(20000);
			when(props.getCompositSolutiontoolKitTypeCode()).thenReturn("CP");
			InterceptorRegistry registry = new InterceptorRegistry();
			Mockito.doNothing().when(handlerInterceptorConfiguration).addInterceptors(registry);
			
			Map<String, Object> queryParameters = new HashMap<>();
			queryParameters.put("active", Boolean.TRUE);
			Pageable pageable = new PageRequest(0, 1000);
			
			RestPageRequest restPageRequest = new RestPageRequest(0, 1000);
			RestPageResponse<MLPSolution> pageResponse = new RestPageResponse<MLPSolution>(mlpSolList, pageable, 1);
			//Mockito.when(cmnDataService.searchSolutions(queryParameters, false, restPageRequest)).thenReturn(new RestPageResponse<MLPSolution>(mlpSolList, pageable, 1));
			when(cmnDataService.searchSolutions(queryParameters, false, restPageRequest)).thenReturn(pageResponse);
			when(cmnDataService.getSolutionRevisions(solutionId)).thenReturn(mlpSolRevList);
			
			Map<String, Object> queryParams = new HashMap<String, Object>();
			queryParameters.put("solutionId", solutionId);
			queryParameters.put("revisionId", revisionId);
			RestPageResponse<MLPTask> mlpTaskResponse = new RestPageResponse<MLPTask>(mlpTaskList, pageable, 1);
			when(cmnDataService.searchTasks(queryParams, false, restPageRequest)).thenReturn(mlpTaskResponse);
			List<DSModelVO> dsModelVOList = matchingModelServiceImpl.getPublicDSModels();
			assertNotNull(dsModelVOList);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	
	
	//@Test
	public void populatePublicModelCacheForMatchingTest(){
		DSModelVO models = new DSModelVO();
		MLPSolution mlpSol = mlpSolutionCommon();
		models.setMlpSolution(mlpSol);
		MLPSolutionRevision mlpSolRev = mlpSolutionRevisionsCommon();
		List<MLPSolutionRevision> mlpSolRevList = new ArrayList<MLPSolutionRevision>();
		mlpSolRevList.add(mlpSolRev);
		models.setMlpSolutionRevisions(mlpSolRevList);
		List<DSModelVO> modelsList = new ArrayList<DSModelVO>();
		modelsList.add(models);
		try {
			matchingModelServiceImpl.populatePublicModelCacheForMatching(modelsList);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	
	private MLPSolutionRevision mlpSolutionRevisionsCommon() {
		MLPSolutionRevision mlpSolRev = new MLPSolutionRevision();
		mlpSolRev.setRevisionId(revisionId);
		mlpSolRev.setVersion("1");
		mlpSolRev.setCreated(Instant.now());
		mlpSolRev.setModified(Instant.now());
		mlpSolRev.setOnboarded(Instant.now());
		mlpSolRev.setMetadata("Meta");
		mlpSolRev.setPublisher("techmdev");
		mlpSolRev.setSolutionId("111");
		mlpSolRev.setOrigin("Origin");
		return mlpSolRev;
	}

	private MLPSolution mlpSolutionCommon() {
		MLPSolution mlpSol = new MLPSolution();
		mlpSol.setSolutionId(solutionId);
		mlpSol.setName("testPubVer");
		mlpSol.setToolkitTypeCode("CP");
		mlpSol.setModelTypeCode("CL");
		mlpSol.setUserId(userId);
		mlpSol.setActive(true);
		mlpSol.setCreated(Instant.now());
		mlpSol.setModified(Instant.now());
		mlpSol.setMetadata("MetaData");
		return mlpSol;
	}
	
}
