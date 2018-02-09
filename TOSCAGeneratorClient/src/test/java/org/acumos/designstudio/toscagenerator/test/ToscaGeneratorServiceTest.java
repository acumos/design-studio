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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.Properties;

import org.acumos.cds.client.ICommonDataServiceRestClient;
import org.acumos.designstudio.toscagenerator.ToscaGeneratorClient;
import org.junit.Test;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToscaGeneratorServiceTest {

	private static final Logger logger = LoggerFactory.getLogger(ToscaGeneratorServiceTest.class);

	private String metaDataFile = "./metadata.json";
	private String protoDataFile = "./aggregator-proto.proto";
	private String userId = "c4e4d366-8ed8-40e1-b61e-1e103de9699b";
	private String solutionID = "3842ba62-8a0c-40cf-817a-ae9986e78aaf";
	private String version = "1.0.0";
	private String solutionRevisionID = "be647000-6ccf-4ec7-bf25-af538bc481ef";
	private String result = null;

	public static Properties CONFIG = new Properties();

	@Mock
	private ICommonDataServiceRestClient cmnDataService;

	/**
	 * The test case is used to create TGIF.json by using a method generateTOSCA
	 * which consumes userId, solutionID, version, solutionRevisionID,
	 * localProtoFile, localMetaDataFile as parameters and generates TGIF data
	 * in a string format which is then stored in TGIF.json. The file is used by
	 * ds-composition engine to represent a model.
	 * 
	 * @exception org.acumos.designstudio.toscagenerator.exceptionhandler.AcumosException
	 *                Handles the all the custom exception occurred in or passed
	 *                to controller class.
	 */
	@Test
	public void testCountTransport() throws Exception {

		try {
			ToscaGeneratorClient mockito = mock(ToscaGeneratorClient.class);
			logger.info("Toscagenerator client start");
			File localMetaDataFile = new File(metaDataFile);
			File localProtoFile = new File(protoDataFile);
			when(mockito.generateTOSCA(userId, solutionID, version, solutionRevisionID, localProtoFile,
					localMetaDataFile)).thenReturn("Success");
			logger.info("result : " + result);
		} catch (Exception ex) {
			logger.error("------------ Exception Occured  generateTOSCA() ----------- ", ex);
			throw ex;
		}
		logger.info("Toscagenerator client end");
	}

}
