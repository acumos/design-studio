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

import org.acumos.designstudio.ce.util.DSUtil;
import org.acumos.designstudio.ce.util.EELFLoggerDelegator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * 
 *
 */
@SpringBootApplication
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class })
public class Application {

	private static final EELFLoggerDelegator logger = EELFLoggerDelegator.getLogger(Application.class);
	public static final String CONFIG_ENV_VAR_NAME = "SPRING_APPLICATION_JSON";

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
		// Clean the output Folder :

		SpringApplication.run(Application.class, args);
	}
}
