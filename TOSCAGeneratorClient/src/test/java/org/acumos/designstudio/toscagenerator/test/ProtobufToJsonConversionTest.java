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

import static org.junit.Assert.*;

import java.io.File;

import org.acumos.designstudio.toscagenerator.service.ProtobufGeneratorService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 
 *
 */
public class ProtobufToJsonConversionTest {
	private static final Logger logger = LoggerFactory.getLogger(ProtobufToJsonConversionTest.class);

	/**
	 * The test case is used to create protoBuf.json by using a method
	 * createProtoJson which consumes solutionId,version,localProtoFile as
	 * parameters and generates protoBuf data in a string format which is then
	 * stored in protoBuf.json. The file is utilized to create TGIF.json which is
	 * used by ds-composition engine to represent a model.
	 * 
	 * @exception org.acumos.designstudio.toscagenerator.exceptionhandler.ServiceException
	 *                Handles the exception occurred in service class.
	 */
	@Test
	public void getSolutions() throws Exception {
		ProtobufGeneratorService protoService = new ProtobufGeneratorService();
		File aggrProto = new File("aggregator-proto.proto");
		File alarmgenProto = new File("alarm-generator-proto.proto");
		File clasProto = new File("classifier-proto.proto");
		File predProto = new File("predictor-proto.proto");
		File testProto = new File("test-demo.proto");

		String aggrProto1 = protoService.createProtoJson("1234", "1.0.0", aggrProto);
		String alarmgenProto2 = protoService.createProtoJson("1234", "1.0.0", alarmgenProto);
		String clasProto3 = protoService.createProtoJson("1234", "1.0.0", clasProto);
		String predProto4 = protoService.createProtoJson("1234", "1.0.0", predProto);
		String testProto5 = protoService.createProtoJson("1234", "1.0.0", testProto);

		assertTrue(aggrProto.exists());
		assertTrue(alarmgenProto.exists());
		assertTrue(clasProto.exists());
		assertTrue(predProto.exists());
		assertTrue(testProto.exists());

		assertNotNull(aggrProto1);
		assertNotNull(alarmgenProto2);
		assertNotNull(clasProto3);
		assertNotNull(predProto4);
		assertNotNull(testProto5);

		logger.debug("Aggregator Result :" + aggrProto1);
		logger.debug("Alarm Generator Result :" + alarmgenProto2);
		logger.debug("Classifier Result :" + clasProto3);
		logger.debug("Predictor Result :" + predProto4);
		logger.debug("Test Demo Result :" + testProto5);
	}
}
