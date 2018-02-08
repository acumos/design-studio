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

package org.acumos.designstudio.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.acumos.cds.client.CommonDataServiceRestClientImpl;
import org.acumos.cds.client.ICommonDataServiceRestClient;
import org.acumos.cds.domain.MLPSolution;
import org.acumos.cds.domain.MLPSolutionRevision;
import org.acumos.cds.domain.MLPUser;
import org.acumos.cds.transport.RestPageRequest;
import org.acumos.cds.transport.RestPageResponse;
import org.acumos.designstudio.cdump.Argument;
import org.acumos.designstudio.cdump.Capabilities;
import org.acumos.designstudio.cdump.CapabilityTarget;
import org.acumos.designstudio.cdump.Cdump;
import org.acumos.designstudio.cdump.DataMap;
import org.acumos.designstudio.cdump.DataMapInputField;
import org.acumos.designstudio.cdump.DataMapOutputField;
import org.acumos.designstudio.cdump.FieldMap;
import org.acumos.designstudio.cdump.MapInputs;
import org.acumos.designstudio.cdump.MapOutput;
import org.acumos.designstudio.cdump.Message;
import org.acumos.designstudio.cdump.Ndata;
import org.acumos.designstudio.cdump.Nodes;
import org.acumos.designstudio.cdump.Property;
import org.acumos.designstudio.cdump.ReqCapability;
import org.acumos.designstudio.cdump.Requirements;
import org.acumos.designstudio.cdump.Target;
import org.acumos.designstudio.ce.exceptionhandler.AcumosException;
import org.acumos.designstudio.ce.exceptionhandler.ServiceException;
import org.acumos.designstudio.ce.service.CompositeSolutionServiceImpl;
import org.acumos.designstudio.ce.service.SolutionServiceImpl;
import org.acumos.designstudio.ce.util.ConfigurationProperties;
import org.acumos.designstudio.ce.util.DSUtil;
import org.acumos.designstudio.ce.util.EELFLoggerDelegator;
import org.acumos.designstudio.ce.vo.DSCompositeSolution;
import org.acumos.nexus.client.NexusArtifactClient;
import org.acumos.nexus.client.RepositoryLocation;
import org.apache.commons.lang.RandomStringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;

/**
 *
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class SolutionControllerTest {
	private static EELFLoggerDelegator logger = EELFLoggerDelegator.getLogger(SolutionControllerTest.class);
	private String url = "";
	private String user ="";
	private String pass = "";
	private NexusArtifactClient nexusArtifactClient = null;
	private ICommonDataServiceRestClient cmnDataService2;
	public static Properties CONFIG = new Properties();

	// CCDS TechMDev(8003) UserId, change it if the CCDS port changes.
	String userId = "8fcc3384-e3f8-4520-af1c-413d9495a154";
	// The local path folder which is there in local project Directory.
	String localpath = "./src/test/resources/";

	// For meanwhile hard coding the sessionID.
	String sessionId = "4f91545a-e674-46af-a4ad-d6514f41de9b";

	@Before
	/**
	 * 
	 * @throws Exception
	 */
	public void setUp() throws Exception {
	    /*CONFIG.load(SolutionControllerTest.class.getResourceAsStream("/application.properties"));
		url = CONFIG.getProperty("cmndatasvc.cmndatasvcendpoinurlTest");
		user = CONFIG.getProperty("cmndatasvc.cmndatasvcuserTest");
		pass = CONFIG.getProperty("cmndatasvc.cmndatasvcpwdTest");
		nexusArtifactClient = getNexusClient();
		cmnDataService2 = CommonDataServiceRestClientImpl.getInstance(url.toString(), user, pass);*/
		MockitoAnnotations.initMocks(this);
	}

	@Mock
	ConfigurationProperties confprops;
	
	@Mock
    CommonDataServiceRestClientImpl cmnDataService;

	@Mock
	org.acumos.designstudio.ce.util.Properties props;

	@InjectMocks
	SolutionServiceImpl solutionService;

	@InjectMocks
	CompositeSolutionServiceImpl compositeService;

	@Autowired
	ConfigurationProperties confprops1;

	@Mock
	org.acumos.designstudio.ce.util.Properties properties;

	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();

	@Test
	/**
	 * 
	 * @throws Exception
	 */
	public void createNewCompositeSolution() throws Exception {
		String response = "";
		try {
			if (userId != null) {
				Cdump cdump = new Cdump();
				cdump.setCid(sessionId);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
				cdump.setCtime(sdf.format(new Date()));
				assertNotNull(cdump);
				Gson gson = new Gson();
				String emptyCdumpJson = gson.toJson(cdump);
				String path = DSUtil.createCdumpPath(userId, localpath);
				DSUtil.writeDataToFile(path, "acumos-cdump" + "-" + sessionId, "json", emptyCdumpJson);
				logger.debug(EELFLoggerDelegator.debugLogger, emptyCdumpJson);
				response = "{\"cid\":\"" + sessionId + "\",\"success\":\"true\",\"errorMessage\":\"\"}";
			} else {
				response = "{\"cid\":\"" + sessionId+ "\",\"success\":\"false\",\"errorMessage\":\"User Id Required\"}";
				logger.debug(EELFLoggerDelegator.debugLogger, response);
				throw new ServiceException("Unable to create new CDUMP file");
			}
		} catch (ServiceException e) {
			logger.error(EELFLoggerDelegator.errorLogger,"------- Exception Occured in createNewCompositeSolution() --------");
			throw e;
		}
	}

	@Test
	/**
	 * 
	 * @throws Exception
	 */
	public void addNode() throws Exception {
		Nodes node = new Nodes();
		node.setName("Node1");
		node.setNodeId("1");
		node.setNodeSolutionId("111");
		node.setNodeVersion("1.0.0");
		node.setTypeInfo(null);
		node.setProperties(null);
		Capabilities cap = new Capabilities();
		cap.setId("1");
		cap.setName("cap1");
		CapabilityTarget ct = new CapabilityTarget();
		ct.setId("transform");
		Message msg = new Message();
		msg.setMessageName("DataFrame");
		List<Argument> al = new ArrayList<Argument>();
		Argument arg = new Argument();
		arg.setRole("repeated");
		arg.setTag("1");
		arg.setType("float");
		al.add(arg);
		Argument[] Argument = al.toArray(new Argument[al.size()]);
		msg.setMessageargumentList(Argument);
		List<Message> ml = new ArrayList<Message>();
		ml.add(msg);
		Message[] mal = ml.toArray(new Message[ml.size()]);
		ct.setName(mal);
		cap.setTarget(ct);
		cap.setTarget_type("capability");
		cap.setProperties(null);
		List<Capabilities> cal = new ArrayList<Capabilities>();
		cal.add(cap);
		Capabilities[] caa = cal.toArray(new Capabilities[cal.size()]);
		node.setCapabilities(caa);
		Requirements req = new Requirements();
		req.setName("ReqName");
		req.setId("Req1");
		req.setRelationship("ManyToMany");
		req.setTarget_type("Node");
		Target target = new Target();
		target.setName("Target1");
		target.setDescription("TargetDescription");
		req.setTarget(target);
		ReqCapability reqCap = new ReqCapability();
		reqCap.setId("1");
		Message m = new Message();
		m.setMessageName("Prediction");
		Argument a = new Argument();
		a.setRole("Role 1");
		a.setTag("Tag1");
		a.setType("Tag1");
		List<Argument> ArgList = new ArrayList<Argument>();
		ArgList.add(a);
		Argument[] ArgArray = ArgList.toArray(new Argument[ArgList.size()]);
		m.setMessageargumentList(ArgArray);
		List<Message> msgList = new ArrayList<Message>();
		msgList.add(m);
		Message[] magArg = msgList.toArray(new Message[msgList.size()]);
		reqCap.setName(magArg);
		req.setCapability(reqCap);
		List<Requirements> reqList = new ArrayList<Requirements>();
		reqList.add(req);
		Requirements[] reqArgs = reqList.toArray(new Requirements[reqList.size()]);
		node.setRequirements(reqArgs);
		Ndata data = new Ndata();
		data.setFixed(false);
		data.setNtype("200");
		data.setPx("100");
		data.setPy("100");
		data.setRadius("100");
		node.setNdata(data);
		
		assertNotNull(node);
		assertNotNull(req);
		assertNotNull(reqCap);
		assertNotNull(target);
		assertEquals("Node1", node.getName());
		assertEquals("1", cap.getId());
		assertEquals("ReqName", req.getName());
		assertEquals("200",data.getNtype());
		
		when(confprops.getToscaOutputFolder()).thenReturn(localpath);
		String result = solutionService.addNode(userId, null, null, sessionId, node);
		assertNotNull(result);
		logger.debug(EELFLoggerDelegator.debugLogger, result);
	}

	@Test
	/**
	 * 
	 * @throws Exception
	 */
	public void addNode1() throws Exception {
		Nodes node = new Nodes();
		node.setName("Node2");
		node.setNodeId("2");
		node.setNodeSolutionId("222");
		node.setNodeVersion("1.0.0");
		node.setTypeInfo(null);
		node.setProperties(null);
		Capabilities cap = new Capabilities();
		cap.setId("1");
		cap.setName("cap1");
		CapabilityTarget ct = new CapabilityTarget();
		ct.setId("transform");
		Message msg = new Message();
		msg.setMessageName("DataFrame");
		List<Argument> al = new ArrayList<Argument>();
		Argument arg = new Argument();
		arg.setRole("repeated");
		arg.setTag("1");
		arg.setType("float");
		al.add(arg);
		Argument[] Argument = al.toArray(new Argument[al.size()]);
		msg.setMessageargumentList(Argument);
		List<Message> ml = new ArrayList<Message>();
		ml.add(msg);
		Message[] mal = ml.toArray(new Message[ml.size()]);
		ct.setName(mal);
		cap.setTarget(ct);
		cap.setTarget_type("capability");
		cap.setProperties(null);
		List<Capabilities> cal = new ArrayList<Capabilities>();
		cal.add(cap);
		Capabilities[] caa = cal.toArray(new Capabilities[cal.size()]);
		node.setCapabilities(caa);
		Requirements req = new Requirements();
		req.setName("ReqName");
		req.setId("Req1");
		req.setRelationship("ManyToMany");
		req.setTarget_type("Node");
		Target target = new Target();
		target.setName("Target1");
		target.setDescription("TargetDescription");
		req.setTarget(target);
		ReqCapability reqCap = new ReqCapability();
		reqCap.setId("1");
		Message m = new Message();
		m.setMessageName("Prediction");
		Argument a = new Argument();
		a.setRole("Role 1");
		a.setTag("Tag1");
		a.setType("Tag1");
		List<Argument> ArgList = new ArrayList<Argument>();
		ArgList.add(a);
		Argument[] ArgArray = ArgList.toArray(new Argument[ArgList.size()]);
		m.setMessageargumentList(ArgArray);
		List<Message> msgList = new ArrayList<Message>();
		msgList.add(m);
		Message[] magArg = msgList.toArray(new Message[msgList.size()]);
		reqCap.setName(magArg);
		req.setCapability(reqCap);
		List<Requirements> reqList = new ArrayList<Requirements>();
		reqList.add(req);
		Requirements[] reqArgs = reqList.toArray(new Requirements[reqList.size()]);
		node.setRequirements(reqArgs);
		Ndata data = new Ndata();
		data.setFixed(false);
		data.setNtype("200");
		data.setPx("100");
		data.setPy("100");
		data.setRadius("100");
		node.setNdata(data);
		
		assertNotNull(node);
		assertNotNull(req);
		assertNotNull(reqCap);
		assertNotNull(target);
		assertEquals("Node2", node.getName());
		assertEquals("1", cap.getId());
		assertEquals("ReqName", req.getName());
		assertEquals("200",data.getNtype());
		
		when(confprops.getToscaOutputFolder()).thenReturn(localpath);
		String result = solutionService.addNode(userId, null, null, sessionId, node);
		assertNotNull(result);
		logger.debug(EELFLoggerDelegator.debugLogger, result);
	}

	@Test
	/**
	 * 
	 * @throws Exception
	 */
	public void addNode2() throws Exception {
		Nodes node = new Nodes();
		node.setName("DM");
		node.setNodeId("3");
		node.setNodeSolutionId("333");
		node.setNodeVersion("1.0.0");
		node.setTypeInfo(null);
		node.setProperties(null);
		Capabilities cap = new Capabilities();
		cap.setId("1");
		cap.setName("cap1");
		CapabilityTarget ct = new CapabilityTarget();
		ct.setId("transform");
		Message msg = new Message();
		msg.setMessageName("DataFrame");
		List<Argument> al = new ArrayList<Argument>();
		Argument arg = new Argument();
		arg.setRole("repeated");
		arg.setTag("1");
		arg.setType("float");
		al.add(arg);
		Argument[] Argument = al.toArray(new Argument[al.size()]);
		msg.setMessageargumentList(Argument);
		List<Message> ml = new ArrayList<Message>();
		ml.add(msg);
		Message[] mal = ml.toArray(new Message[ml.size()]);
		ct.setName(mal);
		cap.setTarget(ct);
		cap.setTarget_type("capability");
		cap.setProperties(null);
		List<Capabilities> cal = new ArrayList<Capabilities>();
		cal.add(cap);
		Capabilities[] caa = cal.toArray(new Capabilities[cal.size()]);
		node.setCapabilities(caa);
		Requirements req = new Requirements();
		req.setName("ReqName");
		req.setId("Req1");
		req.setRelationship("ManyToMany");
		req.setTarget_type("Node");
		Target target = new Target();
		target.setName("Target1");
		target.setDescription("TargetDescription");
		req.setTarget(target);
		ReqCapability reqCap = new ReqCapability();
		reqCap.setId("1");
		Message m = new Message();
		m.setMessageName("Prediction");
		Argument a = new Argument();
		a.setRole("Role 1");
		a.setTag("Tag1");
		a.setType("Tag1");
		List<Argument> ArgList = new ArrayList<Argument>();
		ArgList.add(a);
		Argument[] ArgArray = ArgList.toArray(new Argument[ArgList.size()]);
		m.setMessageargumentList(ArgArray);
		List<Message> msgList = new ArrayList<Message>();
		msgList.add(m);
		Message[] magArg = msgList.toArray(new Message[msgList.size()]);
		reqCap.setName(magArg);
		req.setCapability(reqCap);
		List<Requirements> reqList = new ArrayList<Requirements>();
		reqList.add(req);
		Requirements[] reqArgs = reqList.toArray(new Requirements[reqList.size()]);
		node.setRequirements(reqArgs);
		Ndata data = new Ndata();
		data.setFixed(false);
		data.setNtype("200");
		data.setPx("100");
		data.setPy("100");
		data.setRadius("100");
		node.setNdata(data);
		
		assertNotNull(node);
		assertNotNull(req);
		assertNotNull(reqCap);
		assertNotNull(target);
		assertEquals("DM", node.getName());
		assertEquals("1", cap.getId());
		assertEquals("ReqName", req.getName());
		assertEquals("200",data.getNtype());
		
		when(confprops.getToscaOutputFolder()).thenReturn(localpath);
		String result = solutionService.addNode(userId, null, null, sessionId, node);
		assertNotNull(result);
		logger.debug(EELFLoggerDelegator.debugLogger, result);
	}

	@Test
	/**
	 * 
	 * @throws Exception
	 */
	public void addNode3() throws Exception {
		Nodes node = new Nodes();
		node.setName("Node9");
		node.setNodeId("1");
		node.setNodeSolutionId("333");
		node.setNodeVersion("1.0.0");
		node.setTypeInfo(null);
		node.setProperties(null);
		Capabilities cap = new Capabilities();
		cap.setId("1");
		cap.setName("cap1");
		CapabilityTarget ct = new CapabilityTarget();
		ct.setId("transform");
		Message msg = new Message();
		msg.setMessageName("DataFrame");
		List<Argument> al = new ArrayList<Argument>();
		Argument arg = new Argument();
		arg.setRole("repeated");
		arg.setTag("1");
		arg.setType("float");
		al.add(arg);
		Argument[] Argument = al.toArray(new Argument[al.size()]);
		msg.setMessageargumentList(Argument);
		List<Message> ml = new ArrayList<Message>();
		ml.add(msg);
		Message[] mal = ml.toArray(new Message[ml.size()]);
		ct.setName(mal);
		cap.setTarget(ct);
		cap.setTarget_type("capability");
		cap.setProperties(null);
		List<Capabilities> cal = new ArrayList<Capabilities>();
		cal.add(cap);
		Capabilities[] caa = cal.toArray(new Capabilities[cal.size()]);
		node.setCapabilities(caa);
		Requirements req = new Requirements();
		req.setName("ReqName");
		req.setId("Req1");
		req.setRelationship("ManyToMany");
		req.setTarget_type("Node");
		Target target = new Target();
		target.setName("Target1");
		target.setDescription("TargetDescription");
		req.setTarget(target);
		ReqCapability reqCap = new ReqCapability();
		reqCap.setId("1");
		Message m = new Message();
		m.setMessageName("Prediction");
		Argument a = new Argument();
		a.setRole("Role 1");
		a.setTag("Tag1");
		a.setType("Tag1");
		List<Argument> ArgList = new ArrayList<Argument>();
		ArgList.add(a);
		Argument[] ArgArray = ArgList.toArray(new Argument[ArgList.size()]);
		m.setMessageargumentList(ArgArray);
		List<Message> msgList = new ArrayList<Message>();
		msgList.add(m);
		Message[] magArg = msgList.toArray(new Message[msgList.size()]);
		reqCap.setName(magArg);
		req.setCapability(reqCap);
		List<Requirements> reqList = new ArrayList<Requirements>();
		reqList.add(req);
		Requirements[] reqArgs = reqList.toArray(new Requirements[reqList.size()]);
		node.setRequirements(reqArgs);
		Ndata data = new Ndata();
		data.setFixed(false);
		data.setNtype("200");
		data.setPx("100");
		data.setPy("100");
		data.setRadius("100");
		node.setNdata(data);
		
		assertNotNull(node);
		assertNotNull(req);
		assertNotNull(reqCap);
		assertNotNull(target);
		assertEquals("Node9", node.getName());
		assertEquals("1", cap.getId());
		assertEquals("ReqName", req.getName());
		assertEquals("200",data.getNtype());
		
		when(confprops.getToscaOutputFolder()).thenReturn(localpath);
		String result = solutionService.addNode(userId, null, null, sessionId, node);
		assertNotNull(result);
		logger.debug(EELFLoggerDelegator.debugLogger, result);
	}

	@Test
	/**
	 * 
	 * @throws Exception
	 */
	public void linkShouldGetAddedBetweenModels() throws Exception {
			Property property = new Property();
			assertNull(property.getData_map());
			when(confprops.getToscaOutputFolder()).thenReturn(localpath);
			boolean result = solutionService.addLink(userId, null, null, "Node1 to Node2", "101", "Node1", "1", "Node2","2", "Req2", "Cap2", sessionId, property);
			assertTrue(result);
	}

	@Test
	/**
	 * 
	 * @throws Exception
	 */
	public void linkShouldGetAddedBetweenInputOfDMtoModel() throws Exception {
			Property property = new Property();
			DataMap data_map = new DataMap();
			MapInputs[] map_inputs = new MapInputs[1];
			MapInputs map_inputsObj = new MapInputs();
			MapOutput[] map_outputs = new MapOutput[0];
			DataMapInputField[] input_fields = new DataMapInputField[1];
			DataMapInputField input_fieldsobj = new DataMapInputField();
			input_fieldsobj.setTag("1");
			input_fieldsobj.setRole("repeated");
			input_fieldsobj.setName("name");
			input_fieldsobj.setType("int32");
			input_fieldsobj.setMapped_to_message("");
			input_fieldsobj.setMapped_to_field("");
			input_fields[0] = input_fieldsobj;
			map_inputsObj.setMessage_name("Prediction");
			map_inputsObj.setInput_fields(input_fields);
			map_inputs[0] = map_inputsObj;
			data_map.setMap_inputs(map_inputs);
			data_map.setMap_outputs(map_outputs);
			property.setData_map(data_map);
			
			assertNotNull(property);
			assertNotNull(data_map);
			assertNotNull(input_fieldsobj);
			assertNotNull(map_inputsObj);
			assertEquals("1", input_fieldsobj.getTag());
			assertEquals("Prediction",map_inputsObj.getMessage_name());
			assertTrue(map_inputs.length == 1);
			
			when(confprops.getToscaOutputFolder()).thenReturn(localpath);
			boolean result = solutionService.addLink(userId, null, null, "Node1 to DM", "202", "Node1", "1", "DM", "3","Req2", "Cap2", sessionId, property);
			assertTrue(result);
			logger.debug(EELFLoggerDelegator.debugLogger, "true");
	}

	@Test
	/**
	 * 
	 * @throws Exception
	 */
	public void linkShouldGetAddedBetweenOuputOfDMtoModel() throws Exception {
			Property property = new Property();
			DataMap data_map = new DataMap();
			MapInputs[] map_inputs = new MapInputs[0];
			MapOutput[] map_outputs = new MapOutput[1];
			MapOutput map_outputsObj = new MapOutput();
			DataMapOutputField[] output_fields = new DataMapOutputField[1];
			DataMapOutputField output_fieldsObj = new DataMapOutputField();
			output_fieldsObj.settag("1");
			output_fieldsObj.setrole("repeated");
			output_fieldsObj.setname("name");
			output_fieldsObj.settype("int32");
			output_fields[0] = output_fieldsObj;
			map_outputsObj.setOutput_fields(output_fields);
			map_outputsObj.setMessage_name("Classification");
			map_outputs[0] = map_outputsObj;
			data_map.setMap_inputs(map_inputs);
			data_map.setMap_outputs(map_outputs);
			property.setData_map(data_map);
			
			assertNotNull(property);
			assertNotNull(data_map);
			assertNotNull(output_fieldsObj);
			assertNotNull(map_outputsObj);
			assertEquals("1",output_fieldsObj.gettag());
			assertEquals("Classification",map_outputsObj.getMessage_name());
			assertTrue(output_fields.length == 1);
			
			when(confprops.getToscaOutputFolder()).thenReturn(localpath);
			boolean result = solutionService.addLink(userId, null, null, "DM to Node2", "303", "DM", "3", "Model 2","2", "Req2", "Cap2", sessionId, property);
			assertTrue(result);
			logger.debug(EELFLoggerDelegator.debugLogger, "true" + result);
		}
	
	@Test
	/**
	 * 
	 * @throws Exception
	 */
	public void modifyNode() throws Exception {
		String ndata = "{\"ntype\":\"500\",\"px\":\"500\",\"py\":\"500\",\"radius\":\"500\",\"fixed\":false}";
			FieldMap fieldMap = new FieldMap();
			fieldMap.setInput_field_message_name("Prediction");
			fieldMap.setInput_field_tag_id("1");
			fieldMap.setMap_action("Add");
			fieldMap.setOutput_field_message_name("Classification");
			fieldMap.setOutput_field_tag_id("2");
			
			assertNotNull(fieldMap);
			assertEquals("Prediction",fieldMap.getInput_field_message_name());
			assertEquals("1",fieldMap.getInput_field_tag_id());
			assertEquals("Add",fieldMap.getMap_action());
			assertEquals("Classification",fieldMap.getOutput_field_message_name());
			
			when(confprops.getToscaOutputFolder()).thenReturn(localpath);
			String result = solutionService.modifyNode(userId, null, null, sessionId, "1", "Node1", ndata, fieldMap);
			assertNotNull(result);
			logger.debug(EELFLoggerDelegator.debugLogger, result);
	}

	@Test
	/**
	 * 
	 * @throws Exception
	 */
	public void modifyNode1() throws Exception {
		String ndata = "{\"ntype\":\"500\",\"px\":\"500\",\"py\":\"500\",\"radius\":\"500\",\"fixed\":false}";
		FieldMap fieldMap = new FieldMap();
		when(confprops.getToscaOutputFolder()).thenReturn(localpath);
		assertNotNull(fieldMap);
		String result = solutionService.modifyNode(userId, null, null, sessionId, "2", "Node8", ndata, fieldMap);
		assertNotNull(result);
		logger.debug(EELFLoggerDelegator.debugLogger, result);
	}

	@Test
	/**
	 * 
	 * @throws Exception
	 */
	public void modifyNode2() throws Exception {
		String ndata = "{\"ntype\":\"500\",\"px\":\"500\",\"py\":\"500\",\"radius\":\"500\",\"fixed\":false}";
			FieldMap fieldMap = new FieldMap();
			fieldMap.setInput_field_message_name("Prediction");
			fieldMap.setInput_field_tag_id("1");
			fieldMap.setMap_action("Add");
			fieldMap.setOutput_field_message_name("Classification");
			fieldMap.setOutput_field_tag_id("2");
			
			assertNotNull(fieldMap);
			assertEquals("Prediction",fieldMap.getInput_field_message_name());
			assertEquals("Classification",fieldMap.getOutput_field_message_name());
			
			when(confprops.getToscaOutputFolder()).thenReturn(localpath);
			String result = solutionService.modifyNode(userId, null, null, sessionId, "8", "Node8", ndata, fieldMap);
			assertNotNull(result);
			logger.debug(EELFLoggerDelegator.debugLogger, result);
	}

	@Test
	/**
	 * 
	 * @throws Exception
	 */
	public void modifyLink() throws Exception {
			when(confprops.getToscaOutputFolder()).thenReturn(localpath);
			String result = solutionService.modifyLink(userId, sessionId, null, null, "101", "Link2");
			assertNotNull(result);
			logger.debug(EELFLoggerDelegator.debugLogger, result);
	}

	@Test
	/**
	 * 
	 * @throws Exception
	 */
	public void modifyLink1() throws Exception {
			when(confprops.getToscaOutputFolder()).thenReturn(localpath);
			String result = solutionService.modifyLink(userId, sessionId, null, null, "606", "Link9");
			assertNotNull(result);
			logger.debug(EELFLoggerDelegator.debugLogger, result);
	}

	@Test
	/**
	 * 
	 * @throws Exception
	 */
	public void linkBetweenTwoModelShouldGetDeleted() throws Exception {
		try {
			when(confprops.getToscaOutputFolder()).thenReturn(localpath);
			boolean result = solutionService.deleteLink(userId, null, null, sessionId, "101");
			assertNotNull(result);
			if (result == true) {
				logger.debug(EELFLoggerDelegator.debugLogger, "Link deleted " + result);
			} else {
				throw new ServiceException("Link Not Deleted");
			}
		} catch (ServiceException e) {
			logger.error(EELFLoggerDelegator.errorLogger, "Action Failed", e.getMessage());
		}
	}

	@Test
	/**
	 * 
	 * @throws Exception
	 */
	public void linkBetweenIpOfDataMapperandModelShouldGetDeleted() throws Exception {
		try {
			when(confprops.getToscaOutputFolder()).thenReturn(localpath);
			boolean result = solutionService.deleteLink(userId, null, null, sessionId, "202");
			assertNotNull(result);
			if (result == true) {
				logger.debug(EELFLoggerDelegator.debugLogger, "Link deleted " + result);
			} else {
				throw new ServiceException("Link Not Deleted");
			}
		} catch (ServiceException e) {
			logger.error(EELFLoggerDelegator.errorLogger, "Action Failed", e.getMessage());
		}
	}

	@Test
	/**
	 * 
	 * @throws Exception
	 */
	public void linkBetweenPpOfDataMapperandModelShouldGetDeleted() throws Exception {
		try {
			when(confprops.getToscaOutputFolder()).thenReturn(localpath);
			boolean result = solutionService.deleteLink(userId, null, null, sessionId, "303");
			assertNotNull(result);
			if (result == true) {
				logger.debug(EELFLoggerDelegator.debugLogger, "Link deleted " + result);
			} else {
				throw new ServiceException("Link Not Deleted");
			}
		} catch (ServiceException e) {
			logger.error(EELFLoggerDelegator.errorLogger, "Action Failed", e.getMessage());
		}
	}

	@Test
	/**
	 * 
	 * @throws Exception
	 */
	public void linkShouldNotGetDeleted() throws Exception {
		try {
			when(confprops.getToscaOutputFolder()).thenReturn(localpath);
			boolean result = solutionService.deleteLink(userId, null, null, sessionId, "404");
			assertNotNull(result);
			if (result == true) {
				logger.debug(EELFLoggerDelegator.debugLogger, "Link deleted " + result);
			} else {
				throw new ServiceException("Link Not Deleted");
			}
		} catch (ServiceException e) {
			logger.error(EELFLoggerDelegator.errorLogger, "Action Failed", e.getMessage());
		}
	}

	@Test
	/**
	 * 
	 * @throws Exception
	 */
	public void dMnodeShouldGetDeleted() throws Exception {
		try {
			when(confprops.getToscaOutputFolder()).thenReturn(localpath);
			boolean result = solutionService.deleteNode(userId, null, null, sessionId, "3");
			assertNotNull(result);
			if (result == true) {
				logger.debug(EELFLoggerDelegator.debugLogger, "Node deleted " + result);
			} else {
				throw new ServiceException("Node Not Deleted");
			}
		} catch (ServiceException e) {
			logger.error(EELFLoggerDelegator.errorLogger, "Action Failed", e.getMessage());
		}
	}

	@Test
	/**
	 * 
	 * @throws Exception
	 */
	public void modelnodeShouldGetDeleted() throws Exception {
		try {
			when(confprops.getToscaOutputFolder()).thenReturn(localpath);
			boolean result = solutionService.deleteNode(userId, null, null, sessionId, "2");
			if (result == true) {
				logger.debug(EELFLoggerDelegator.debugLogger, "Node deleted " + result);
			} else {
				throw new ServiceException("Node Not Deleted");
			}
		} catch (ServiceException e) {
			logger.error(EELFLoggerDelegator.errorLogger, "Action Failed", e.getMessage());
		}
	}

	@Test
	/**
	 * 
	 * @throws Exception
	 */
	public void nodeShouldGetDeleted() throws Exception {
		try {
			when(confprops.getToscaOutputFolder()).thenReturn(localpath);
			boolean result = solutionService.deleteNode(userId, null, null, sessionId, "5");
			if (result == true) {
				logger.debug(EELFLoggerDelegator.debugLogger, "Node deleted " + result);
			} else {
				throw new ServiceException("Node Not Deleted");
			}
		} catch (ServiceException e) {
			logger.error(EELFLoggerDelegator.errorLogger, "Action Failed", e.getMessage());
		}
	}

	@Test
	/**
	 * 
	 * @throws Exception
	 */
	public void validateCompositeSolution() throws Exception {
			CompositeSolutionServiceImpl csimpl = new CompositeSolutionServiceImpl();
			csimpl.getRestCCDSClient((CommonDataServiceRestClientImpl) cmnDataService);
			csimpl.getNexusClient(nexusArtifactClient, confprops, properties);
			when(confprops.getToscaOutputFolder()).thenReturn(localpath);
			String result = csimpl.validateCompositeSolution(userId, "NewModel", "solutionId", "1.0.0");
			assertNotNull(result);
			logger.debug(EELFLoggerDelegator.debugLogger, result);
	}

	@Test
	/**
	 * 
	 * @throws Exception
	 */
	public void clearCompositeSolution() throws Exception {
		try {
			Cdump cd = new Cdump();
			when(confprops.getDateFormat()).thenReturn("yyyy-MM-dd HH:mm:ss.SSS");
			SimpleDateFormat sdf = new SimpleDateFormat(confprops.getDateFormat());
			cd.setMtime(sdf.format(new Date()));
			when(confprops.getToscaOutputFolder()).thenReturn(localpath);
			String result = compositeService.clearCompositeSolution(userId, null, null, sessionId);
			assertNotNull(result);
			if (result.contains("true")) {
				logger.debug(EELFLoggerDelegator.debugLogger, result);
			} else {
				throw new FileNotFoundException();
			}
		} catch (FileNotFoundException e) {
			logger.error(EELFLoggerDelegator.errorLogger,"------- Exception Occured in clearCompositeSolution() --------", e);
		}
	}

	@Test
	/**
	 * 
	 * @throws Exception
	 */
	public void closeCompositeSolution() throws Exception {
		try {
			when(confprops.getToscaOutputFolder()).thenReturn(localpath);
			String result = compositeService.closeCompositeSolution(userId, null, null, sessionId);
			assertNotNull(result);
			if (result.contains("true")) {
				logger.debug(EELFLoggerDelegator.debugLogger, result);
			} else {
				throw new FileNotFoundException();
			}
		} catch (FileNotFoundException e) {
			logger.error(EELFLoggerDelegator.errorLogger,"------- Exception Occured in closeCompositeSolution() --------", e);
		}
	}

	@Test
	public void saveCompositeSolution() throws JSONException {

		CompositeSolutionServiceImpl compositeServiceImpl = new CompositeSolutionServiceImpl();
		compositeServiceImpl.getRestCCDSClient((CommonDataServiceRestClientImpl) cmnDataService);
		compositeServiceImpl.getNexusClient(nexusArtifactClient, confprops, properties);
		SolutionServiceImpl solutionServiceImpl = new SolutionServiceImpl();
		solutionServiceImpl.getRestCCDSClient((CommonDataServiceRestClientImpl) cmnDataService);
		solutionServiceImpl.getNexusClient(nexusArtifactClient, confprops, properties);

		when(properties.getProvider()).thenReturn("Acumos");
		when(properties.getToolKit()).thenReturn("CP");
		when(properties.getVisibilityLevel()).thenReturn("PR");
		when(confprops.getToscaOutputFolder()).thenReturn(localpath);
		when(confprops.getDateFormat()).thenReturn("yyyy-MM-dd HH:mm:ss.SSS");
		when(properties.getArtifactTypeCode()).thenReturn("CD");
		when(confprops.getNexusgroupid()).thenReturn("com.artifact");
		when(properties.getAskToUpdateExistingCompSolnMsg())
				.thenReturn("Do you want to update a previous version of this solution?");
		String result = null;

		DSCompositeSolution dscs = new DSCompositeSolution();

		dscs.setcId(sessionId);
		dscs.setAuthor(userId);
		dscs.setSolutionName(RandomStringUtils.randomAlphanumeric(5) + "-Test");
		dscs.setSolutionId(null);
		dscs.setVersion("1.0.0");
		dscs.setOnBoarder(userId);
		dscs.setDescription("Testing Save Function");
		dscs.setProvider(properties.getProvider());
		dscs.setToolKit(properties.getToolKit());
		dscs.setVisibilityLevel(properties.getVisibilityLevel());
		dscs.setIgnoreLesserVersionConflictFlag(false);

		// CASE 1 : where New Composite Solution : CID exist and SolutionID
		// is
		// missing.
		try {
			result = compositeServiceImpl.saveCompositeSolution(dscs);
			logger.debug(EELFLoggerDelegator.debugLogger, "Result of Save Composite Solution : " + result);
		} catch (AcumosException e) {
			logger.error(EELFLoggerDelegator.errorLogger, e.getMessage());
		}

		if (null != result) {
			JSONObject jsonResponse = new JSONObject(result);
			if (!jsonResponse.isNull("solutionId") && null != jsonResponse.getString("solutionId")
					&& !jsonResponse.isNull("version") && null != jsonResponse.getString("version")) {
				dscs.setSolutionId(jsonResponse.getString("solutionId"));
				dscs.setVersion(jsonResponse.getString("version"));
			}
			dscs.setcId(null);

			// CASE 2 :where user tries to update the existing solution
			if (null != dscs.getSolutionId()) {
				try {
					result = compositeServiceImpl.saveCompositeSolution(dscs);
					logger.debug(EELFLoggerDelegator.debugLogger,
							"Result of update existing Composite Solution : " + result);
				} catch (AcumosException e) {
					logger.error(EELFLoggerDelegator.errorLogger, e.getMessage());
				}

				// CASE 3 :where user tries to update the existing solution
				// with new version
				dscs.setVersion("1.0.1");
				try {
					result = compositeServiceImpl.saveCompositeSolution(dscs);
					logger.debug(EELFLoggerDelegator.debugLogger,
							"Result of update existing Composite Solution with new version : " + result);
				} catch (AcumosException e) {
					logger.error(EELFLoggerDelegator.errorLogger, e.getMessage());
				}

				// CASE 4 :where user tries to update the previous version
				dscs.setVersion("1.0.0");
				try {
					result = compositeServiceImpl.saveCompositeSolution(dscs);
					logger.debug(EELFLoggerDelegator.debugLogger,
							"Result of update previous version of the Composite Solution : " + result);
				} catch (AcumosException e) {
					logger.error(EELFLoggerDelegator.errorLogger, e.getMessage());
				}
				// CASE 5 :where user gives a command to update the previous
				// version of the solution by setting
				// ignoreLesserVersionConflictFlag as true
				dscs.setIgnoreLesserVersionConflictFlag(true);
				dscs.setVersion("1.0.0");
				try {
					result = compositeServiceImpl.saveCompositeSolution(dscs);
					logger.debug(EELFLoggerDelegator.debugLogger,
							"Result of update previous version of the Composite Solution : " + result);
				} catch (AcumosException e) {
					logger.error(EELFLoggerDelegator.errorLogger, e.getMessage());
				}

				// NEW CASE :When user tries to update the existing solution
				// with a different name
				dscs.setSolutionName(RandomStringUtils.randomAlphanumeric(8));
				try {
					result = compositeServiceImpl.saveCompositeSolution(dscs);

					logger.debug(EELFLoggerDelegator.debugLogger,
							"Result of update existing Composite Solution with new solution name : " + result);
				} catch (AcumosException e) {
					logger.error(EELFLoggerDelegator.errorLogger, e.getMessage());
				}
			}
		}
	}

	@Test
	public void deleteCompositeSolution() throws Exception{
		try{
			boolean isSolutionDeleted = false;
			String userId = "e57490ed-8462-4a77-ab39-157138dfbda8";
			String solutionId = "710d881b-e926-4412-831c-10b0bf04c354yyy"; // correct solutionId 710d881b-e926-4412-831c-10b0bf04c354
			String version = "1.0.0";
			CompositeSolutionServiceImpl iCompositeSolutionService = new CompositeSolutionServiceImpl();
			iCompositeSolutionService.getRestCCDSClient((CommonDataServiceRestClientImpl) cmnDataService);
			iCompositeSolutionService.getNexusClient(nexusArtifactClient, confprops1, properties);
			isSolutionDeleted = iCompositeSolutionService.deleteCompositeSolution(userId, solutionId, version);
			//assertTrue(isSolutionDeleted);
			logger.info("deleteCompositeSolution {}", isSolutionDeleted);
		} catch (AcumosException ex) {
			logger.error(EELFLoggerDelegator.errorLogger, ex.getMessage());
		}
	}

	@Test
	public void getMatchingModels() {
		SolutionServiceImpl solutionServiceImpl = new SolutionServiceImpl();
		solutionServiceImpl.getRestCCDSClient((CommonDataServiceRestClientImpl) cmnDataService);
		solutionServiceImpl.getNexusClient(nexusArtifactClient, confprops1, properties);
		String userId = "";
		String portType = "output";
		String protobufJsonString = "[{\"role\":\"repeated\",\"tag\":\"1\",\"type\":\"string\"},{\"role\":\"repeated\",\"tag\":\"2\",\"type\":\"string\"}]";
		try {
			JSONArray protobufJsonString1 = new JSONArray(protobufJsonString);
			String getMatchingModelsResult = solutionServiceImpl.getMatchingModels(userId, portType,protobufJsonString1);
			Assert.assertNotNull(getMatchingModelsResult);
			logger.info("getMatchingModelsResult {}", getMatchingModelsResult);
			logger.debug(EELFLoggerDelegator.debugLogger, getMatchingModelsResult);
		} catch (JSONException je) {
			logger.error(EELFLoggerDelegator.errorLogger, je.getMessage());
		} catch (Exception ex) {
			logger.error(EELFLoggerDelegator.errorLogger, ex.getMessage());
		}
	}

	@Test
	public void getMatchingModels1() {
		SolutionServiceImpl solutionServiceImpl = new SolutionServiceImpl();
		solutionServiceImpl.getRestCCDSClient((CommonDataServiceRestClientImpl) cmnDataService);
		solutionServiceImpl.getNexusClient(nexusArtifactClient, confprops1, properties);
		String userId = "";
		String portType = "input";
		String protobufJsonString = "[{\"role\":\"repeated\",\"tag\":\"1\",\"type\":\"string\"},{\"role\":\"repeated\",\"tag\":\"2\",\"type\":\"string\"}]";
		try {
			JSONArray protobufJsonString1 = new JSONArray(protobufJsonString);
			String getMatchingModelsResult = solutionServiceImpl.getMatchingModels(userId, portType,protobufJsonString1);
			Assert.assertNotNull(getMatchingModelsResult);
			logger.debug(EELFLoggerDelegator.debugLogger, getMatchingModelsResult);
		} catch (JSONException je) {
			logger.error(EELFLoggerDelegator.errorLogger, je.getMessage());
		} catch (Exception ex) {
			logger.error(EELFLoggerDelegator.errorLogger, ex.getMessage());
		}

	}

	@Test
	public void readCompositeSolutionGraph() {
			String sId = "040fe8f7-14f7-45e1-b46d-2de505e9d52d";
			String version = "1.0.0";
			when(props.getArtifactTypeCode()).thenReturn("CD");
			when(confprops.getToscaOutputFolder()).thenReturn(localpath);
			solutionService.getRestCCDSClient((CommonDataServiceRestClientImpl) cmnDataService);
			solutionService.getNexusClient(nexusArtifactClient, confprops, props);
			String result = solutionService.readCompositeSolutionGraph(userId, sId, version);
			Assert.assertNotNull(result);
			logger.info("readCompositeSolutionGraph {}", result);
	}

	@Test
    public void getSolutionsReturnEmptySolList() throws Exception {
           SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
           sdf.format(new Date());
           when(confprops.getDateFormat()).thenReturn(sdf.format(new Date()));
           when(props.getCompositSolutiontoolKitTypeCode()).thenReturn("CP");
           when(props.getPublicAccessTypeCode()).thenReturn("PB");
           when(props.getPrivateAccessTypeCode()).thenReturn("PV");
           when(props.getOrganizationAccessTypeCode()).thenReturn("OR");
           when(props.getSolutionResultsetSize()).thenReturn(10000);
           // solutionService.getRestCCDSClient((CommonDataServiceRestClientImpl)cmnDataService);
           Map<String, Object> queryParameters = new HashMap<>();
           queryParameters.put("active", Boolean.TRUE);
           RestPageResponse<MLPSolution> pageResponse = new RestPageResponse<MLPSolution>(new ArrayList<MLPSolution>());
           when(cmnDataService.searchSolutions(queryParameters, false, new RestPageRequest(0,props.getSolutionResultsetSize()))).thenReturn(pageResponse);
          /* String result = solutionService.getSolutions(userId);
           assertNotNull(result);*/
    }

    @Test
    public void getSolutionsReturnNoSolution() throws Exception {
           SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
           sdf.format(new Date());
           when(confprops.getDateFormat()).thenReturn(sdf.format(new Date()));
           when(props.getCompositSolutiontoolKitTypeCode()).thenReturn("CP");
           when(props.getPublicAccessTypeCode()).thenReturn("PB");
           when(props.getPrivateAccessTypeCode()).thenReturn("PV");
           when(props.getOrganizationAccessTypeCode()).thenReturn("OR");
           when(props.getSolutionResultsetSize()).thenReturn(10000);
           // solutionService.getRestCCDSClient((CommonDataServiceRestClientImpl)
           // cmnDataService);
           Map<String, Object> queryParameters = new HashMap<>();
           queryParameters.put("active", Boolean.TRUE);
           when(cmnDataService.searchSolutions(queryParameters, false, new RestPageRequest(0,props.getSolutionResultsetSize()))).thenReturn(null);
           /*String result = solutionService.getSolutions(userId);
           assertNotNull(result);*/
    }
    
    @Test
    public void getSolutionsReturnSolution() throws Exception {
           SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
           sdf.format(new Date());
           // solutionService.getRestCCDSClient((CommonDataServiceRestClientImpl)
           // cmnDataService);
           Map<String, Object> queryParameters = new HashMap<>();
           queryParameters.put("active", Boolean.TRUE);

           ArrayList<MLPSolution> mlpSolList = new ArrayList<MLPSolution>();
           MLPSolution image_classifier = new MLPSolution();
           image_classifier.setSolutionId("1cb9e77e-43dd-4add-bf29-648f2420c621");
           image_classifier.setName("image_classifier");
           image_classifier.setProvider(null);
           image_classifier.setToolkitTypeCode("SK");
           image_classifier.setModelTypeCode("CL");
           image_classifier.setDescription("image_classifier");
           image_classifier.setAccessTypeCode("PB");
           image_classifier.setOwnerId("c4e4d366-8ed8-40e1-b61e-1e103de9699b");
           assertNotNull(image_classifier);
           mlpSolList.add(image_classifier);

           MLPUser user = new MLPUser();
           user.setFirstName("Test");
           user.setLastName("Dev");
           assertNotNull(user);
           ArrayList<MLPSolutionRevision> mlpSolRevList = new ArrayList<MLPSolutionRevision>();
           MLPSolutionRevision mlpSolRev = new MLPSolutionRevision();
           mlpSolRev.setRevisionId("84874435-d103-44c1-9451-d2b660fae766");
           mlpSolRev.setVersion("1");
           mlpSolRev.setCreated(new Date());
           assertNotNull(mlpSolRev);
           mlpSolRevList.add(mlpSolRev);
           
           cmnDataService = mock(CommonDataServiceRestClientImpl.class);
           RestPageResponse<MLPSolution> pageResponse = new RestPageResponse<MLPSolution>(mlpSolList);
           when(confprops.getDateFormat()).thenReturn(sdf.format(new Date()));
           when(props.getCompositSolutiontoolKitTypeCode()).thenReturn("CP");
           when(props.getPublicAccessTypeCode()).thenReturn("PB");
           when(props.getPrivateAccessTypeCode()).thenReturn("PV");
           when(props.getOrganizationAccessTypeCode()).thenReturn("OR");
           when(props.getSolutionResultsetSize()).thenReturn(10000);
           when(cmnDataService.getSolutionRevisions(image_classifier.getSolutionId())).thenReturn(mlpSolRevList);
           when(cmnDataService.searchSolutions(queryParameters, false, new RestPageRequest(0,props.getSolutionResultsetSize()))).thenReturn(pageResponse);
           when(cmnDataService.getUser("c4e4d366-8ed8-40e1-b61e-1e103de9699b")).thenReturn(user);
           /*String result = solutionService.getSolutions("c4e4d366-8ed8-40e1-b61e-1e103de9699b");

           assertNotNull(result);*/
    }


	@Test
	public void getCompositeSolutions() {
		try {
			String visibilityLevel = "PR";
			compositeService.getRestCCDSClient((CommonDataServiceRestClientImpl) cmnDataService);
			compositeService.getNexusClient(nexusArtifactClient, confprops, props);
			when(props.getToolKit()).thenReturn("CP");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			sdf.format(new Date());
			when(confprops.getDateFormat()).thenReturn(sdf.format(new Date()));
			String result = compositeService.getCompositeSolutions(userId, visibilityLevel);
			Assert.assertNotNull(result);
			System.out.println(result);
			logger.info("getCompositeSolutions {}", result);
		} catch (AcumosException e) {
			logger.error(EELFLoggerDelegator.errorLogger, e.getMessage());
		}
	}

	private NexusArtifactClient getNexusClient() {
		try {
			RepositoryLocation repositoryLocation = new RepositoryLocation();
			repositoryLocation.setId("1");
			repositoryLocation.setUrl(CONFIG.getProperty("nexus.nexusendpointurlTest"));
			repositoryLocation.setUsername(CONFIG.getProperty("nexus.nexususernameTest"));
			repositoryLocation.setPassword(CONFIG.getProperty("nexus.nexuspasswordTest"));
			nexusArtifactClient = new NexusArtifactClient(repositoryLocation);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, e.getMessage());
		}
		return nexusArtifactClient;
	}
}
