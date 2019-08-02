/*-
 * ===============LICENSE_START=======================================================
 * Acumos
 * ===================================================================================
 * Copyright (C) 2018 - 2019 AT&T Intellectual Property & Tech Mahindra. All rights reserved.
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

import org.junit.Test;

public class CSVdatabrokerVOTest {

	@Test
	public void csvdatabrokerVOTest() {
		DataBrokerMap dataBrokerMap = new DataBrokerMap();
		dataBrokerMap.setScript("test");
		assertTrue(dataBrokerMap.getScript() == "test");
		dataBrokerMap.setTarget_system_url("test");
		assertTrue(dataBrokerMap.getTarget_system_url() == "test");
		dataBrokerMap.setCsv_file_field_separator("test");
		assertTrue(dataBrokerMap.getCsv_file_field_separator() == "test");
		dataBrokerMap.setData_broker_type("test");
		assertTrue(dataBrokerMap.getData_broker_type() == "test");
		dataBrokerMap.setFirst_row("test");
		assertTrue(dataBrokerMap.getFirst_row() == "test");
		dataBrokerMap.setLocal_system_data_file_path("test");
		assertTrue(dataBrokerMap.getLocal_system_data_file_path() == "test");
		DBInputField dbInputField = new DBInputField();
		dbInputField.setChecked("test");
		dbInputField.setMapped_to_field("test");
		dbInputField.setName("test");
		dbInputField.setType("test");
		DBMapInput dbMapInput = new DBMapInput();
		dbMapInput.setInput_field(dbInputField);
		DBMapInput[] map_inputs = { dbMapInput, dbMapInput };
		dataBrokerMap.setMap_inputs(map_inputs);
		assertTrue(dataBrokerMap.getMap_inputs() == map_inputs);
		DBOutputField dbOutputField = new DBOutputField();
		dbOutputField.setName("test");
		dbOutputField.setTag("test");
		DBOTypeAndRoleHierarchy dboTypeAndRoleHierarchy = new DBOTypeAndRoleHierarchy();
		dboTypeAndRoleHierarchy.setName("test");
		dboTypeAndRoleHierarchy.setRole("test");
		DBOTypeAndRoleHierarchy[] type_and_role_hierarchy_list = { dboTypeAndRoleHierarchy, dboTypeAndRoleHierarchy };
		dbOutputField.setType_and_role_hierarchy_list(type_and_role_hierarchy_list);
		DBMapOutput dbMapOutput = new DBMapOutput();
		dbMapOutput.setOutput_field(dbOutputField);
		DBMapOutput[] map_outputs = { dbMapOutput, dbMapOutput };
		dataBrokerMap.setMap_outputs(map_outputs);
		assertTrue(dataBrokerMap.getMap_outputs() == map_outputs);
		assertTrue(dbMapInput.getInput_field() == dbInputField);
		assertTrue(dbInputField.getChecked() == "test");
		assertTrue(dbInputField.getMapped_to_field() == "test");
		assertTrue(dbInputField.getName() == "test");
		assertTrue(dbInputField.getType() == "test");
		assertTrue(dbMapOutput.getOutput_field() == dbOutputField);
		assertTrue(dbOutputField.getName() == "test");
		assertTrue(dbOutputField.getTag() == "test");
		assertTrue(dbOutputField.getType_and_role_hierarchy_list() == type_and_role_hierarchy_list);
		assertTrue(dboTypeAndRoleHierarchy.getName() == "test");
		assertTrue(dboTypeAndRoleHierarchy.getRole() == "test");

	}

	@Test
	public void ProtobufOption() {

		ProtobufOption protobufOption = new ProtobufOption();
		protobufOption.setName("test");
		assertTrue(protobufOption.getName() == "test");
		protobufOption.setValue("test");
		assertTrue(protobufOption.getValue() == "test");
		protobufOption.toString();
	}
}
