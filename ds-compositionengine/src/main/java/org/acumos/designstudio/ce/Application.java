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
import java.lang.invoke.MethodHandles;
import java.util.List;

import org.acumos.designstudio.ce.exceptionhandler.ServiceException;
import org.acumos.designstudio.ce.service.MatchingModelServiceImpl;
import org.acumos.designstudio.ce.service.SolutionServiceImpl;
import org.acumos.designstudio.ce.util.DSUtil;
import org.acumos.designstudio.ce.vo.matchingmodel.DSModelVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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


@SpringBootApplication
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class })
@EnableScheduling
public class Application {

	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	public static final String CONFIG_ENV_VAR_NAME = "SPRING_APPLICATION_JSON";
	
	
	@Autowired
	private MatchingModelServiceImpl matchingModelServiceImpl;
	
	@Autowired
	private SolutionServiceImpl solutionServiceImpl;
	
	/**
	 * This method is Execute the Cron job i.e triggers for every 6 hours
	 * @throws ServiceException
	 * 		This method throws Service Exception
	 */
	@Scheduled(cron = "0 0 0/6 * * *")
	public void ExecuteForHour() throws ServiceException {
		logger.debug("Scheduled on ExecuteForHour() Begin ");
		try {
			solutionServiceImpl.getUpdatedModelsbyDate();
		} catch (Exception e) {
			logger.error("Interrupted Exception Occured in ExecuteForHour() {}", e);
			throw new ServiceException("Failed for Creating the Cache");
		}
		logger.debug("Scheduled on ExecuteForHour() End ");
	}

	

	/**
	 * Stating point of Design Studio Application
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
			logger.debug("main: successfully parsed configuration from environment {}", CONFIG_ENV_VAR_NAME);
			JsonNode toscaNode = rootNode.path("tosca");
			String path = toscaNode.path("outputfolder").asText();
			logger.debug("main: Cleaning output folder {}", path);
			DSUtil.rmdir(new File(path));
			logger.debug("main: output folder is cleaned successfully {}", path);
		} else {
			logger.debug("main: no configuration found in environment {}", CONFIG_ENV_VAR_NAME);
		}
		SpringApplication.run(Application.class, args);
	}
	
	/**
	 * This method will do the event handling for ContextRefreshedEvent
	 * @param event
	 * 		This method accepts event as parameter
	 * @throws ServiceException
	 * 		This method throws Service Exception
	 */
	@EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) throws ServiceException {
		logger.debug("onApplicationEvent() Begin ");
		List<DSModelVO> dsModels = matchingModelServiceImpl.getPublicDSModels();
		if(dsModels != null && !dsModels.isEmpty()){
			matchingModelServiceImpl.populatePublicModelCacheForMatching(dsModels);
		} else {
			logger.debug("onApplicationEvent() : public model cache is empty");
		}
		logger.debug("onApplicationEvent() End ");
    }
	
}
