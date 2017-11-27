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

import java.io.File;

import org.acumos.designstudio.toscagenerator.service.ProtobufGeneratorService;
import org.junit.Test;

public class ProtobufToJsonConversionTest {
	@Test
	public void getSolutions() throws Exception {
		ProtobufGeneratorService protobufGeneratorService = new ProtobufGeneratorService();

		ProtobufGeneratorService protoService = new ProtobufGeneratorService();

		File localProtofile1 = new File("D:/Acumos/OnsiteAcumos/Sprint4/Kazi/protobufFiles/aggregator-proto.proto");

		File localProtofile2 = new File("D:/Acumos/OnsiteAcumos/Sprint4/Kazi/protobufFiles/alarm-generator-proto.proto");

		File localProtofile3 = new File("D:/Acumos/OnsiteAcumos/Sprint4/Kazi/protobufFiles/classifier-proto.proto");

		File localProtofile4 = new File("D:/Acumos/OnsiteAcumos/Sprint4/Kazi/protobufFiles/predictor-proto.proto");

		String protoJSON = protoService.createProtoJson("1234", "1.0.0", localProtofile1);

		String protoJSON2 = protoService.createProtoJson("1234", "1.0.0", localProtofile2);

		String protoJSON3 = protoService.createProtoJson("1234", "1.0.0", localProtofile3);

		String protoJSON4 = protoService.createProtoJson("1234", "1.0.0", localProtofile4);

		System.out.println("Proto JSON 1 : " + protoJSON);

		System.out.println("Proto JSON 2 : " + protoJSON2);

		System.out.println("Proto JSON 3 : " + protoJSON3);

		System.out.println("Proto JSON 4 : " + protoJSON4);

	}
}
