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

package org.acumos.designstudio.toscagenerator.test;

import org.acumos.designstudio.toscagenerator.ToscaGeneratorClient;
import org.acumos.designstudio.toscagenerator.service.TgifGeneratorService;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TgifGeneratorServiceTest {
	private static final Logger logger = LoggerFactory.getLogger(TgifGeneratorServiceTest.class);
	@Test
	public void testCountTransport() {
		String metaData = "{\"schema\": \"acumos.schema.model:0.3.0\", \"runtime\": {\"name\": \"python\", \"encoding\": \"protobuf\", \"version\": \"3.6.1\", \"dependencies\": {\"pip\": {\"indexes\": [], \"requirements\": [{\"name\": \"cloudpickle\", \"version\": \"0.2.2\"}, {\"name\": \"numpy\", \"version\": \"1.12.1\"}, {\"name\": \"scikit-learn\", \"version\": \"0.18.1\"}, {\"name\": \"acumos-client\", \"version\": \"0.3.9\"}]}, \"conda\": {\"channels\": [], \"requirements\": []}}}, \"name\": \"Aggregator\", \"methods\": {\"transform\": {\"input\": \"DataFrame\", \"output\": \"Prediction\", \"description\": \"Transforms a DataFrame to a Prediction\"}}}";
		String protobuf1 = "{\"syntax\":\"proto3\",\"packageName\":\"\",\"listOfMessages\":[{\"messageName\":\"DataFrame\",\"messageargumentList\":[{\"firstToken\":\"repeated\",\"type\":\"int64\",\"name\":\"day\",\"tag\":\"1\"},{\"firstToken\":\"repeated\",\"type\":\"int64\",\"name\":\"weekday\",\"tag\":\"2\"},{\"firstToken\":\"repeated\",\"type\":\"int64\",\"name\":\"hour\",\"tag\":\"3\"},{\"firstToken\":\"repeated\",\"type\":\"int64\",\"name\":\"minute\",\"tag\":\"4\"},{\"firstToken\":\"repeated\",\"type\":\"double\",\"name\":\"hist_1D\",\"tag\":\"5\"},{\"firstToken\":\"repeated\",\"type\":\"int64\",\"name\":\"VM_ID\",\"tag\":\"6\"}]},{\"messageName\":\"Prediction\",\"messageargumentList\":[{\"firstToken\":\"repeated\",\"type\":\"double\",\"name\":\"predictions\",\"tag\":\"1\"}]}],\"service\":{\"name\":\"Model \",\"listOfOperations\":[{\"operationType\":\"rpc\",\"operatioName\":\"transform\",\"listOfInputMessages\":[{\"inputMessageName\":\"DataFrame\"}],\"listOfOutputMessages\":[{\"outPutMessageName\":\"Prediction\"}]}]}}";
		try {
			logger.info("Tgif file creation is  started");
			ToscaGeneratorClient client = new ToscaGeneratorClient(" {\"toscaOutputFolder\":\"D:/ab00343130/Acumos/Studio/Sprint4/temp/\", \"toscaGeneratorEndPointURL\":\"\", \"nexusEndPointURL\":\"\", \"nexusUserName\":\"\", \"nexusPassword\":\"\", \"nexusGroupId\":\"\", \"cmnDataSvcEndPoinURL\":\"\", \"cmnDataSvcUser\":\"\", \"cmnDataSvcPwd\":\"\"}");
			TgifGeneratorService tgifGeneratorService = new TgifGeneratorService();
			tgifGeneratorService.createTgif("KDFOAIDFJ@#IJDFLKS", "1.1.1", protobuf1, metaData);
			logger.info("Tgif file is created");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}


}
