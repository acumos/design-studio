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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;

import org.acumos.designstudio.toscagenerator.util.ConfigurationProperties;
import org.acumos.designstudio.toscagenerator.util.ToscaUtil;
import org.junit.Test;

public class ToscaUtilTest {
	
	/**
	 * The method is used to write the protobuf data to a local.
	 * @throws Exception
	 */
	@Test
	public void writeDataToFileTest() throws Exception {
		try{
			String protobuf = "{\"syntax\":\"proto3\",\"packageName\":\"\",\"listOfMessages\":[{\"messageName\":\"DataFrame\",\"messageargumentList\":[{\"role\":\"repeated\",\"type\":\"DataFrameRow\",\"name\":\"rows\",\"tag\":\"1\",\"complexType\":{\"messageName\":\"DataFrameRow\",\"messageargumentList\":[{\"role\":\"\",\"type\":\"string\",\"name\":\"sepal_len\",\"tag\":\"1.1\"},{\"role\":\"\",\"type\":\"int32\",\"name\":\"sepal_wid\",\"tag\":\"1.2\"},{\"role\":\"\",\"type\":\"SubFrameRow\",\"name\":\"petal_len\",\"tag\":\"1.3\",\"complexType\":{\"messageName\":\"SubFrameRow\",\"messageargumentList\":[{\"role\":\"repeated\",\"type\":\"string\",\"name\":\"row_1\",\"tag\":\"1.3.1\"},{\"role\":\"repeated\",\"type\":\"string\",\"name\":\"row_2\",\"tag\":\"1.3.2\"}]}},{\"role\":\"\",\"type\":\"string\",\"name\":\"petal_wid\",\"tag\":\"1.4\"}]}},{\"role\":\"repeated\",\"type\":\"MyFrameRow\",\"name\":\"myRow\",\"tag\":\"2\",\"complexType\":{\"messageName\":\"MyFrameRow\",\"messageargumentList\":[{\"role\":\"repeated\",\"type\":\"int64\",\"name\":\"row_1\",\"tag\":\"2.1\"},{\"role\":\"repeated\",\"type\":\"string\",\"name\":\"row_2\",\"tag\":\"2.2\"}]}}]},{\"messageName\":\"Prediction\",\"messageargumentList\":[{\"role\":\"repeated\",\"type\":\"int64\",\"name\":\"myRow\",\"tag\":\"1\"}]}],\"service\":{\"name\":\"Model\",\"listOfOperations\":[{\"operationType\":\"rpc\",\"operatioName\":\"transform\",\"listOfInputMessages\":[{\"inputMessageName\":\"DataFrame\"}],\"listOfOutputMessages\":[{\"outPutMessageName\":\"Prediction\"}]}]}}";
			String localpath = "./src/test/resources/";
			ToscaUtil.writeDataToFile(localpath,"PROTOBUF", "json", protobuf);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	/**
	 * The method is used to read any local file.
	 * @throws Exception
	 */
	@Test
	public void readFileTest() throws Exception {
		try{
			String localpath = "./src/test/resources/PROTOBUF.json";
			String fileData = ToscaUtil.readFile(localpath);
			assertNotNull(fileData);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
	/**
	 * The method is used to read any local json file.
	 * @throws Exception
	 */
	@Test
	public void readJSONFileTest() throws Exception{
		try{
			String localpath = "./src/test/resources/Tgif.json";
			String fileData = ToscaUtil.readJSONFile(localpath);
			assertNotNull(fileData);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		
	}
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void deleteTest() throws IOException {
		try{
			File aggrProto = new File("aggregator-protoTest.proto");//Create aggregator-protoTest.proto file under ToscaGenerator project and make assertTrue(aggrProto.exists());
			assertFalse(aggrProto.exists());
			ToscaUtil.delete(aggrProto);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Test
	public void configurationTest(){
		ConfigurationProperties props = new ConfigurationProperties();
		props.getCmnDataSvcEndPoinURL();
		props.getCmnDataSvcUser();
		props.getCmnDataSvcPwd();
		props.getNexusEndPointURL();
		props.getNexusGroupId();
		props.getNexusPassword();
		props.getNexusUserName();
		props.getToscaGeneratorEndPointURL();
		props.getToscaOutputFolder();
		props.getConfigurationProperties();
		props.init("toscaOutputFolder", "toscaGeneratorEndPointURL", "nexusEndPointURL", "nexusUserName", "nexusPassword", "nexusGroupId", "cmnDataSvcEndPoinURL", "cmnDataSvcUser", "cmnDataSvcPwd");
		
	}
	
}
