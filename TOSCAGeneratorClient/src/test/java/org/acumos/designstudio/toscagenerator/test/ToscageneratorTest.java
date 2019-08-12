/*-
 * ===============LICENSE_START=======================================================
 * Acumos
 * ===================================================================================
 * Copyright (C) 2019 AT&T Intellectual Property & Tech Mahindra. All rights reserved.
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

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.MalformedURLException;
import java.net.ProtocolException;

import org.acumos.designstudio.toscagenerator.ToscaGeneratorClient;
import org.acumos.designstudio.toscagenerator.exceptionhandler.AcumosException;
import org.acumos.designstudio.toscagenerator.service.TgifGeneratorService;
import org.acumos.designstudio.toscagenerator.service.ToscaGeneratorService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToscageneratorTest {
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Test
	public void ToscaGeneratorTest() throws AcumosException, MalformedURLException, ProtocolException, IOException {
		ToscaGeneratorClient toscaGeneratorClient = new ToscaGeneratorClient("test");

		try {
			toscaGeneratorClient.generateTOSCA("test", "test", "test", "test", new File("test"), new File("test"));
		} catch (Exception e) {
			logger.error("Exception occured in generateTOSCA()");
		}

		ToscaGeneratorService toscaGeneratorService = new ToscaGeneratorService();
		try {
			toscaGeneratorService.getToscaModels("test");
		} catch (Exception e) {
			logger.error("Exception occured in getToscaModels()");
		}
		try {
			toscaGeneratorService.decryptAndWriteTofile("test", "test", "test");
		} catch (Exception e) {
			logger.error("Exception occured in decryptAndWriteTofile()");
		}
		TgifGeneratorService tgifGeneratorService = new TgifGeneratorService();

		try {
			tgifGeneratorService.createTgif("test", "test", "test", "test");
		}

		catch (Exception e) {
			logger.error("Exception occured in createTgif()");
		}
		try {
			tgifGeneratorService.createTgif1("test", "test", "test");
		}

		catch (Exception e) {
			logger.error("Exception occured in createTgif1()");
		}

	}

}
