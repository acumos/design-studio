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

package org.acumos.designstudio.ce;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.acumos.cds.client.CommonDataServiceRestClientImpl;
import org.acumos.cds.domain.MLPSolution;
import org.acumos.cds.domain.MLPSolutionRevision;
import org.acumos.cds.transport.RestPageRequest;
import org.acumos.cds.transport.RestPageResponse;
import org.acumos.designstudio.ce.exceptionhandler.ServiceException;
import org.acumos.designstudio.ce.service.MatchingModelServiceImpl;
import org.acumos.designstudio.ce.service.SolutionServiceImpl;
import org.acumos.designstudio.ce.util.DSUtil;
import org.acumos.designstudio.ce.util.EELFLoggerDelegator;
import org.acumos.designstudio.ce.util.ModelCacheForMatching;
import org.acumos.designstudio.ce.util.Properties;
import org.acumos.designstudio.ce.vo.matchingmodel.DSModelVO;
import org.acumos.designstudio.ce.vo.matchingmodel.KeyVO;
import org.acumos.designstudio.ce.vo.matchingmodel.ModelDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * 
 *
 */
@SpringBootApplication
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class })
@EnableScheduling
public class Application {

	private static final EELFLoggerDelegator logger = EELFLoggerDelegator.getLogger(Application.class);
	public static final String CONFIG_ENV_VAR_NAME = "SPRING_APPLICATION_JSON";
	
	
	@Autowired
	private MatchingModelServiceImpl matchingModelServiceImpl;
	
	@Autowired
	private SolutionServiceImpl solutionServiceImpl;
	
	/**
	 * 
	 * @throws ServiceException
	 * 		This method throws Service Exception
	 */
	@Scheduled(cron = "* * 6 * * *")
	public void ExecuteForHour() throws ServiceException {
		logger.debug(EELFLoggerDelegator.debugLogger, " Scheduled on ExecuteForHour() Begin ");
		try {
			solutionServiceImpl.getCacheMechanism();
		} catch (Exception e) {
			logger.error("Interrupted Exception Occured in ExecuteForHour() {}", e);
			throw new ServiceException("Failed for Creating the Cache");
		}
		logger.debug(EELFLoggerDelegator.debugLogger, " Scheduled on ExecuteForHour() End ");
	}

	

	/**
	 * 
	 * @param args
	 *            Command-line arguments
	 * @throws Exception
	 *             On failure
	 */
	public static void main(String[] args) throws Exception {
		final String springApplicationJson = System.getenv(CONFIG_ENV_VAR_NAME);
		if (springApplicationJson != null && springApplicationJson.contains("{")) {
			final ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = mapper.readTree(springApplicationJson);
			logger.info("main: successfully parsed configuration from environment {}", CONFIG_ENV_VAR_NAME);
			JsonNode toscaNode = rootNode.path("tosca");
			String path = toscaNode.path("outputfolder").asText();
			logger.info("main: Cleaning output folder {}", path);
			DSUtil.rmdir(new File(path));
			logger.info("main: output folder is cleaned successfully {}", path);
		} else {
			logger.warn("main: no configuration found in environment {}", CONFIG_ENV_VAR_NAME);
		}
		SpringApplication.run(Application.class, args);
	}
	
	/**
	 * 
	 * @param event
	 * 		This method accepts event as parameter
	 * @throws ServiceException
	 * 		This method throws Service Exception
	 */
	@EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) throws ServiceException {
		logger.debug(EELFLoggerDelegator.debugLogger, " onApplicationEvent() Begin ");
		List<DSModelVO> dsModels = matchingModelServiceImpl.getPublicDSModels();
		matchingModelServiceImpl.populatePublicModelCacheForMatching(dsModels);
		logger.debug(EELFLoggerDelegator.debugLogger, " onApplicationEvent() begin ");
		
    }
	
	
}
