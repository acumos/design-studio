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
package org.acumos.sqldatabroker.vo;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.acumos.sqldatabroker.exceptionhandler.ServiceException;
import org.junit.Test;

public class DataBrokerMapVOTest {

	@Test
	public void DataBrokerMapTest() {
		DataBrokerMap dataBrokerMap = new DataBrokerMap();
		dataBrokerMap.setUserId("test");
		assertTrue(dataBrokerMap.getUserId() == "test");
		dataBrokerMap.setPassword("test");
		assertTrue(dataBrokerMap.getPassword() == "test");
		dataBrokerMap.setProtobufFile("test");
		assertTrue(dataBrokerMap.getProtobufFile() == "test");
		dataBrokerMap.setDatabaseName("test");
		assertTrue(dataBrokerMap.getDatabaseName() == "test");
		dataBrokerMap.setTableName("test");
		assertTrue(dataBrokerMap.getTableName() == "test");
		dataBrokerMap.setDataSourceClassName("test");
		assertTrue(dataBrokerMap.getDataSourceClassName() == "test");
		dataBrokerMap.setScript("test");
		assertTrue(dataBrokerMap.getScript() == "test");
		dataBrokerMap.setTarget_system_url("test");
		assertTrue(dataBrokerMap.getTarget_system_url() == "test");
		dataBrokerMap.setLocal_system_data_file_path("test");
		assertTrue(dataBrokerMap.getLocal_system_data_file_path() == "test");
		dataBrokerMap.setFirst_row("test");
		assertTrue(dataBrokerMap.getFirst_row() == "test");
		dataBrokerMap.setFirst_row("test");
		assertTrue(dataBrokerMap.getFirst_row() == "test");
		dataBrokerMap.setCsv_file_field_separator("test");
		assertTrue(dataBrokerMap.getCsv_file_field_separator() == "test");
		dataBrokerMap.setData_broker_type("test");
		assertTrue(dataBrokerMap.getData_broker_type() == "test");
		DBMapInput[] map_inputs = { new DBMapInput(), new DBMapInput() };
		dataBrokerMap.setMap_inputs(map_inputs);
		assertTrue(dataBrokerMap.getMap_inputs() == map_inputs);
		DBMapOutput[] map_outputs = { new DBMapOutput(), new DBMapOutput() };
		dataBrokerMap.setMap_outputs(map_outputs);
		assertTrue(dataBrokerMap.getMap_outputs() == map_outputs);

	}

	@Test
	public void DBOTypeRoleTest() {
		DBOTypeAndRoleHierarchy dboTypeAndRoleHierarchy = new DBOTypeAndRoleHierarchy();
		dboTypeAndRoleHierarchy.setName("test");
		assertTrue(dboTypeAndRoleHierarchy.getName() == "test");
		dboTypeAndRoleHierarchy.setRole("test");
		assertTrue(dboTypeAndRoleHierarchy.getRole() == "test");
	}

	@Test
	public void ResultTest() {
		Result result = new Result();
		result.setStatus(0);
		assertTrue(result.getStatus() == 0);
		result.setMessage(result);

	}

	@Test
	public void DBInputField() {
		DBInputField dbInputField = new DBInputField();
		dbInputField.setChecked("test");
		assertTrue(dbInputField.getChecked() == "test");
		dbInputField.setName("test");
		assertTrue(dbInputField.getName() == "test");
		dbInputField.setType("test");
		assertTrue(dbInputField.getType() == "test");

	}

	@Test
	public void ProtobufMessageFieldTest() {
		ProtobufMessage protobufMessage = new ProtobufMessage();
		ProtobufMessageField protobufMessageField = new ProtobufMessageField();
		protobufMessageField.setName("fsdusd");
		protobufMessageField.setRole("fgusdfi");
		protobufMessageField.setTag(12);
		protobufMessageField.setType("Type");
		protobufMessage.setName("name");
		List<ProtobufMessageField> pmfList = new ArrayList<>();
		pmfList.add(protobufMessageField);
		protobufMessage.setFields(pmfList);
		protobufMessage.toString();
	}

	@Test
	public void ProtobufOptionTest() {
		ProtobufOption protobufOption = new ProtobufOption();
		protobufOption.setName("test");
		assertTrue(protobufOption.getName() == "test");
		protobufOption.setValue("test");
		assertTrue(protobufOption.getValue() == "test");
		protobufOption.toString();
	}

	@Test
	public void configuration() throws ServiceException {
		Configuration cofiguration = new Configuration();
		cofiguration.setData_broker_type("test");
		assertTrue(cofiguration.getData_broker_type() == "test");
		cofiguration.setLocal_system_data_file_path("test");
		assertTrue(cofiguration.getLocal_system_data_file_path() == "test");
		cofiguration.setScript("test");
		assertTrue(cofiguration.getScript() == "test");
		assertTrue(cofiguration.isRemoteFile() == false);
		cofiguration.setTarget_system_url("/www.google.com");
		cofiguration.getLocalFilePath();
		cofiguration.setTarget_system_url("\\www.google.com");
		cofiguration.getLocalFilePath();
		cofiguration.setTarget_system_url("file\\www.google.com");
		cofiguration.getLocalFilePath();
		cofiguration.setTarget_system_url("/www.google.com");
		cofiguration.getLocalPath();
		cofiguration.setTarget_system_url("\\www.google.com");
		cofiguration.getLocalPath();
		cofiguration.setTarget_system_url(".\\www.google.com");
		cofiguration.getLocalPath();
		cofiguration.setTarget_system_url(".\\www.google.com/");
		cofiguration.getLocalPath();
		cofiguration.setTarget_system_url("file/www.google.com/");
		cofiguration.getLocalPath();

	}


}
