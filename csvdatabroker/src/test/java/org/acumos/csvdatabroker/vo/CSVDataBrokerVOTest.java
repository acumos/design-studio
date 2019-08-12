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
package org.acumos.csvdatabroker.vo;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.invoke.MethodHandles;

import org.acumos.csvdatabroker.util.LocalScriptExecutor;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CSVDataBrokerVOTest {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Test
	public void DataBrokerMapTest() {

		DataBrokerMap dataBrokerMap = new DataBrokerMap();
		dataBrokerMap.setCsv_file_field_separator("test");
		assertTrue(dataBrokerMap.getCsv_file_field_separator() == "test");
		dataBrokerMap.setData_broker_type("test");
		assertTrue(dataBrokerMap.getData_broker_type() == "test");
		dataBrokerMap.setFirst_row("test");
		assertTrue(dataBrokerMap.getFirst_row() == "test");
		dataBrokerMap.setLocal_system_data_file_path("test");
		assertTrue(dataBrokerMap.getLocal_system_data_file_path() == "test");
		dataBrokerMap.setScript("test");
		assertTrue(dataBrokerMap.getScript() == "test");
		dataBrokerMap.setTarget_system_url("test");
		assertTrue(dataBrokerMap.getTarget_system_url() == "test");

		DBInputField dbInputField = new DBInputField();
		dbInputField.setChecked("test");
		assertTrue(dbInputField.getChecked() == "test");
		dbInputField.setMapped_to_field("test");
		assertTrue(dbInputField.getMapped_to_field() == "test");
		dbInputField.setName("test");
		assertTrue(dbInputField.getName() == "test");
		dbInputField.setType("test");
		assertTrue(dbInputField.getType() == "test");

		DBMapInput dbMapInput = new DBMapInput();
		dbMapInput.setInput_field(dbInputField);
		assertTrue(dbMapInput.getInput_field() == dbInputField);
		DBMapInput[] map_inputs = { dbMapInput, dbMapInput };
		dataBrokerMap.setMap_inputs(map_inputs);
		assertTrue(dataBrokerMap.getMap_inputs() == map_inputs);

		DBOutputField dbOutputField = new DBOutputField();
		dbOutputField.setName("test");
		assertTrue(dbOutputField.getName() == "test");
		dbOutputField.setTag("test");
		assertTrue(dbOutputField.getTag() == "test");

		DBOTypeAndRoleHierarchy dbOTypeAndRoleHierarchy = new DBOTypeAndRoleHierarchy();
		dbOTypeAndRoleHierarchy.setName("test");
		assertTrue(dbOTypeAndRoleHierarchy.getName() == "test");
		dbOTypeAndRoleHierarchy.setRole("test");
		assertTrue(dbOTypeAndRoleHierarchy.getRole() == "test");

		DBOTypeAndRoleHierarchy[] type_and_role_hierarchy_list = { dbOTypeAndRoleHierarchy, dbOTypeAndRoleHierarchy };

		dbOutputField.setType_and_role_hierarchy_list(type_and_role_hierarchy_list);
		assertTrue(dbOutputField.getType_and_role_hierarchy_list() == type_and_role_hierarchy_list);

		DBMapOutput dBMapOutput = new DBMapOutput();
		DBMapOutput[] map_outputs = { dBMapOutput, dBMapOutput };
		dataBrokerMap.setMap_outputs(map_outputs);
		assertTrue(dataBrokerMap.getMap_outputs() == map_outputs);

		dBMapOutput.setOutput_field(dbOutputField);
		assertTrue(dBMapOutput.getOutput_field() == dbOutputField);

		ProtobufOption protobufOption = new ProtobufOption();
		protobufOption.setName("test");
		assertTrue(protobufOption.getName() == "test");
		protobufOption.setValue("test");
		assertTrue(protobufOption.getValue() == "test");
		protobufOption.toString();

		LocalScriptExecutor localScriptExecutor = new LocalScriptExecutor();
		try {
			localScriptExecutor.createScriptFile("test");
		} catch (IOException e) {
			logger.error("IOException occured in createScriptFile()");
		}

	}

}
