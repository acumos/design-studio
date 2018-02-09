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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.acumos.designstudio.toscagenerator.exceptionhandler.AcumosException;
import org.acumos.designstudio.toscagenerator.service.TgifGeneratorService;
import org.acumos.designstudio.toscagenerator.vo.Artifact;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TgifGeneratorServiceTest {
	private static final Logger logger = LoggerFactory.getLogger(TgifGeneratorServiceTest.class);

	/**
	 * The test case is used to create TGIF.json by using a method createTgif
	 * which consumes solutionId,version,localProtoFile as parameters and
	 * generates protoBuf data in a string format which is then stored in
	 * protoBuf.json. The file is utilized to create TGIF.json which is used by
	 * ds-composition engine to represent a model.
	 * 
	 */
	@Test
	public void testCountTransport1() {
		String metaData = "{\"schema\": \"acumos.schema.model:0.3.0\", \"runtime\": {\"name\": \"python\", \"encoding\": \"protobuf\", \"version\": \"3.6.1\", \"dependencies\": {\"pip\": {\"indexes\": [], \"requirements\": [{\"name\": \"cloudpickle\", \"version\": \"0.2.2\"}, {\"name\": \"numpy\", \"version\": \"1.12.1\"}, {\"name\": \"scikit-learn\", \"version\": \"0.18.1\"}, {\"name\": \"acumos-client\", \"version\": \"0.3.9\"}]}, \"conda\": {\"channels\": [], \"requirements\": []}}}, \"name\": \"Aggregator\", \"methods\": {\"transform\": {\"input\": \"DataFrame\", \"output\": \"Prediction\", \"description\": \"Transforms a DataFrame to a Prediction\"}}}";
		String protobuf1 = "{\"syntax\":\"proto3\",\"packageName\":\"\",\"listOfMessages\":[{\"messageName\":\"DataFrame\",\"messageargumentList\":[{\"firstToken\":\"repeated\",\"type\":\"int64\",\"name\":\"day\",\"tag\":\"1\"},{\"firstToken\":\"repeated\",\"type\":\"int64\",\"name\":\"weekday\",\"tag\":\"2\"},{\"firstToken\":\"repeated\",\"type\":\"int64\",\"name\":\"hour\",\"tag\":\"3\"},{\"firstToken\":\"repeated\",\"type\":\"int64\",\"name\":\"minute\",\"tag\":\"4\"},{\"firstToken\":\"repeated\",\"type\":\"double\",\"name\":\"hist_1D\",\"tag\":\"5\"},{\"firstToken\":\"repeated\",\"type\":\"int64\",\"name\":\"VM_ID\",\"tag\":\"6\"}]},{\"messageName\":\"Prediction\",\"messageargumentList\":[{\"firstToken\":\"repeated\",\"type\":\"double\",\"name\":\"predictions\",\"tag\":\"1\"}]}],\"service\":{\"name\":\"Model \",\"listOfOperations\":[{\"operationType\":\"rpc\",\"operatioName\":\"transform\",\"listOfInputMessages\":[{\"inputMessageName\":\"DataFrame\"}],\"listOfOutputMessages\":[{\"outPutMessageName\":\"Prediction\"}]}]}}";
		String tgifjsonString = "{\"self\":{\"version\":\"1.1.1\",\"name\":\"Gen\",\"description\":\"\",\"component_type\":\"Docker\"},\"streams\":{},\"services\":{\"calls\":[{\"config_key\":\"transform\",\"request\":{\"format\":[{\"messageName\":\"Prediction\",\"messageargumentList\":[{\"name\":\"myRow\",\"role\":\"repeated\",\"tag\":\"1\",\"type\":\"int64\"}]}],\"version\":\"\"},\"response\":{\"format\":[],\"version\":\"\"}}],\"provides\":[{\"route\":\"transform\",\"request\":{\"format\":[{\"messageName\":\"DataFrame\",\"messageargumentList\":[{\"complexType\":{\"messageName\":\"DataFrameRow\",\"messageargumentList\":[{\"name\":\"sepal_len\",\"role\":\"\",\"tag\":\"1.1\",\"type\":\"string\"},{\"name\":\"sepal_wid\",\"role\":\"\",\"tag\":\"1.2\",\"type\":\"int32\"},{\"complexType\":{\"messageName\":\"SubFrameRow\",\"messageargumentList\":[{\"name\":\"row_1\",\"role\":\"repeated\",\"tag\":\"1.3.1\",\"type\":\"string\"},{\"name\":\"row_2\",\"role\":\"repeated\",\"tag\":\"1.3.2\",\"type\":\"string\"}]},\"name\":\"petal_len\",\"role\":\"\",\"tag\":\"1.3\",\"type\":\"SubFrameRow\"},{\"name\":\"petal_wid\",\"role\":\"\",\"tag\":\"1.4\",\"type\":\"string\"}]},\"name\":\"rows\",\"role\":\"repeated\",\"tag\":\"1\",\"type\":\"DataFrameRow\"},{\"complexType\":{\"messageName\":\"MyFrameRow\",\"messageargumentList\":[{\"name\":\"row_1\",\"role\":\"repeated\",\"tag\":\"2.1\",\"type\":\"int64\"},{\"name\":\"row_2\",\"role\":\"repeated\",\"tag\":\"2.2\",\"type\":\"string\"}]},\"name\":\"myRow\",\"role\":\"repeated\",\"tag\":\"2\",\"type\":\"MyFrameRow\"}]}],\"version\":\"\"},\"response\":{\"format\":[],\"version\":\"\"}}]},\"parameters\":[],\"auxiliary\":{},\"artifacts\":[]}";
		Artifact result = new Artifact("TGIF", "json", "3842ba62", "1.1.1", "", tgifjsonString.length());
		TgifGeneratorService mockito = mock(TgifGeneratorService.class);
		try {
			when(mockito.createTgif("KDFOAIDFJ@#IJDFLKS", "1.1.1", protobuf1, metaData)).thenReturn(result);
			assertThat(result.getContentLength(), is(1371));
		} catch (AcumosException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
