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

/**
 * 
 */
package org.acumos.designstudio.test;

import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.acumos.cds.client.CommonDataServiceRestClientImpl;
import org.acumos.cds.client.ICommonDataServiceRestClient;
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
import org.acumos.designstudio.ce.exceptionhandler.CustomException;
import org.acumos.designstudio.ce.service.CompositeServiceImpl;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;

/**
 * 
 *
 */
public class SolutionControllerTest {
	private static EELFLoggerDelegator logger = EELFLoggerDelegator.getLogger(SolutionControllerTest.class);
	private final String url = "http://localhost:8003/ccds";
	private final String user = "ccds_client";
	private final String pass = "ccds_client";
	private NexusArtifactClient nexusArtifactClient = null;
	private ICommonDataServiceRestClient cmnDataService;

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
	public void createClient() throws Exception {
		nexusArtifactClient = getNexusClient();
		cmnDataService = CommonDataServiceRestClientImpl.getInstance(url.toString(), user, pass);
	}

	@Mock
	ConfigurationProperties confprops;
	@Mock
	org.acumos.designstudio.ce.util.Properties props;

	@InjectMocks
	SolutionServiceImpl solutionService;

	@InjectMocks
	CompositeServiceImpl compositeService;

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
				Gson gson = new Gson();
				String emptyCdumpJson = gson.toJson(cdump);
				String path = DSUtil.createCdumpPath(userId, localpath);
				DSUtil.writeDataToFile(path, "acumos-cdump" + "-" + sessionId, "json", emptyCdumpJson);
				logger.debug(EELFLoggerDelegator.debugLogger, emptyCdumpJson);
				response = "{\"cid\":\"" + sessionId + "\",\"success\":\"true\",\"errorMessage\":\"\"}";
			} else {
				response = "{\"cid\":\"" + sessionId
						+ "\",\"success\":\"false\",\"errorMessage\":\"User Id Required\"}";
				logger.debug(EELFLoggerDelegator.debugLogger, response);
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger,
					"------- Exception Occured in createNewCompositeSolution() --------");
		}
		logger.debug(EELFLoggerDelegator.debugLogger, "------ createNewCompositeSolution() : End -------");
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
		arg.setRule("repeated");
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
		a.setRule("Rule 1");
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
		when(confprops.getToscaOutputFolder()).thenReturn(localpath);
		String result = solutionService.addNode(userId, null, null, sessionId, node);
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
		arg.setRule("repeated");
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
		a.setRule("Rule 1");
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
		when(confprops.getToscaOutputFolder()).thenReturn(localpath);
		String result = solutionService.addNode(userId, null, null, sessionId, node);
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
		arg.setRule("repeated");
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
		a.setRule("Rule 1");
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
		when(confprops.getToscaOutputFolder()).thenReturn(localpath);
		String result = solutionService.addNode(userId, null, null, sessionId, node);
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
		arg.setRule("repeated");
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
		a.setRule("Rule 1");
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
		when(confprops.getToscaOutputFolder()).thenReturn(localpath);
		String result = solutionService.addNode(userId, null, null, sessionId, node);
		logger.debug(EELFLoggerDelegator.debugLogger, result);
	}

	@Test
	/**
	 * 
	 * @throws Exception
	 */
	public void linkShouldGetAddedBetweenModels() throws Exception {
		try {

			Property property = new Property();
			when(confprops.getToscaOutputFolder()).thenReturn(localpath);
			boolean result = solutionService.addLink(userId, null, null, "Node1 to Node2", "101", "Node1", "1", "Node2",
					"2", "Req2", "Cap2", sessionId, property);
			System.out.println(result);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger,
					"------- Exception Occured in linkShouldGetAddedBetweenModels() --------", e.getMessage());
		}
	}

	@Test
	/**
	 * 
	 * @throws Exception
	 */
	public void linkShouldGetAddedBetweenInputOfDMtoModel() throws Exception {
		try {
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
			when(confprops.getToscaOutputFolder()).thenReturn(localpath);
			boolean result = solutionService.addLink(userId, null, null, "Node1 to DM", "202", "Node1", "1", "DM", "3",
					"Req2", "Cap2", sessionId, property);
			logger.debug(EELFLoggerDelegator.debugLogger, "true" + result);
			logger.debug(EELFLoggerDelegator.debugLogger, "true");
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger,
					"------- Exception Occured in linkShouldGetAddedBetweenInputOfDMtoModel() --------",
					e.getMessage());
		}
	}

	@Test
	/**
	 * 
	 * @throws Exception
	 */
	public void linkShouldGetAddedBetweenOuputOfDMtoModel() throws Exception {
		try {
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
			when(confprops.getToscaOutputFolder()).thenReturn(localpath);
			boolean result = solutionService.addLink(userId, null, null, "DM to Node2", "303", "DM", "3", "Model 2",
					"2", "Req2", "Cap2", sessionId, property);
			logger.debug(EELFLoggerDelegator.debugLogger, "true" + result);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger,
					"------- Exception Occured in linkShouldGetAddedBetweenOuputOfDMtoModel() --------",
					e.getMessage());
		}
	}

	@Test
	/**
	 * 
	 * @throws Exception
	 */
	public void modifyNode() throws Exception {
		String ndata = "{\"ntype\":\"500\",\"px\":\"500\",\"py\":\"500\",\"radius\":\"500\",\"fixed\":false}";
		try {
			FieldMap fieldMap = new FieldMap();
			fieldMap.setInput_field_message_name("Prediction");
			fieldMap.setInput_field_tag_id("1");
			fieldMap.setMap_action("Add");
			fieldMap.setOutput_field_message_name("Classification");
			fieldMap.setOutput_field_tag_id("2");
			when(confprops.getToscaOutputFolder()).thenReturn(localpath);
			String result = solutionService.modifyNode(userId, null, null, sessionId, "1", "Node1", ndata, fieldMap);
			logger.debug(EELFLoggerDelegator.debugLogger, result);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, "------- Exception Occured in modifyNode() --------",
					e.getMessage());
		}
	}

	@Test
	/**
	 * 
	 * @throws Exception
	 */
	public void modifyNode1() throws Exception {
		String ndata = "{\"ntype\":\"500\",\"px\":\"500\",\"py\":\"500\",\"radius\":\"500\",\"fixed\":false}";
		try {
			FieldMap fieldMap = new FieldMap();
			when(confprops.getToscaOutputFolder()).thenReturn(localpath);
			String result = solutionService.modifyNode(userId, null, null, sessionId, "2", "Node8", ndata, fieldMap);
			logger.debug(EELFLoggerDelegator.debugLogger, result);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, "------- Exception Occured in modifyNode1() --------",
					e.getMessage());
		}
	}

	@Test
	/**
	 * 
	 * @throws Exception
	 */
	public void modifyNode2() throws Exception {
		String ndata = "{\"ntype\":\"500\",\"px\":\"500\",\"py\":\"500\",\"radius\":\"500\",\"fixed\":false}";
		try {
			FieldMap fieldMap = new FieldMap();
			fieldMap.setInput_field_message_name("Prediction");
			fieldMap.setInput_field_tag_id("1");
			fieldMap.setMap_action("Add");
			fieldMap.setOutput_field_message_name("Classification");
			fieldMap.setOutput_field_tag_id("2");
			when(confprops.getToscaOutputFolder()).thenReturn(localpath);
			String result = solutionService.modifyNode(userId, null, null, sessionId, "8", "Node8", ndata, fieldMap);
			logger.debug(EELFLoggerDelegator.debugLogger, result);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, "------- Exception Occured in modifyNode2() --------",
					e.getMessage());
		}
	}

	@Test
	/**
	 * 
	 * @throws Exception
	 */
	public void modifyLink() throws Exception {
		try {
			when(confprops.getToscaOutputFolder()).thenReturn(localpath);
			String result = solutionService.modifyLink(userId, sessionId, null, null, "101", "Link2");
			logger.debug(EELFLoggerDelegator.debugLogger, result);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, "------- Exception Occured in modifyLink() --------",
					e.getMessage());
		}
	}

	@Test
	/**
	 * 
	 * @throws Exception
	 */
	public void modifyLink1() throws Exception {
		try {
			when(confprops.getToscaOutputFolder()).thenReturn(localpath);
			String result = solutionService.modifyLink(userId, sessionId, null, null, "606", "Link9");
			logger.debug(EELFLoggerDelegator.debugLogger, result);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, "------- Exception Occured in modifyLink1() --------",
					e.getMessage());
		}
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
			logger.debug(EELFLoggerDelegator.debugLogger, "true" + result);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger,
					"------- Exception Occured in linkBetweenTwoModelShouldGetDeleted() --------", e.getMessage());
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
			logger.debug(EELFLoggerDelegator.debugLogger, "true" + result);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger,
					"------- Exception Occured in linkBetweenIpOfDataMapperandModelShouldGetDeleted() --------",
					e.getMessage());

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
			logger.debug(EELFLoggerDelegator.debugLogger, "true" + result);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger,
					"------- Exception Occured in linkBetweenPpOfDataMapperandModelShouldGetDeleted() --------",
					e.getMessage());
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
			logger.debug(EELFLoggerDelegator.debugLogger, "false" + result);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger,
					"------- Exception Occured in linkShouldNotGetDeleted() --------", e.getMessage());
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
			logger.debug(EELFLoggerDelegator.debugLogger, "false" + result);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger,
					"------- Exception Occured in dMnodeShouldGetDeleted() --------", e.getMessage());
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
			logger.debug(EELFLoggerDelegator.debugLogger, "true" + result);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger,
					"------- Exception Occured in modelnodeShouldGetDeleted() --------", e.getMessage());
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
			logger.debug(EELFLoggerDelegator.debugLogger, "true" + result);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger,
					"------- Exception Occured in nodeShouldGetDeleted() --------", e.getMessage());
		}
	}

	@Test
	/**
	 * 
	 * @throws Exception
	 */
	public void validateCompositeSolution() throws Exception {

		try {
			CompositeServiceImpl csimpl = new CompositeServiceImpl();
			csimpl.getRestCCDSClient((CommonDataServiceRestClientImpl) cmnDataService);
			csimpl.getNexusClient(nexusArtifactClient, confprops, properties);
			when(confprops.getToscaOutputFolder()).thenReturn(localpath);
			String result = csimpl.validateCompositeSolution(userId, "NewModel", "solutionId", "1.0.0");
			logger.debug(EELFLoggerDelegator.debugLogger, result);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger,
					"------- Exception Occured in validateCompositeSolution() --------", e.getMessage());
		}
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
			logger.debug(EELFLoggerDelegator.debugLogger, result);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger,
					"------- Exception Occured in clearCompositeSolution() --------", e.getMessage());
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
			logger.debug(EELFLoggerDelegator.debugLogger, result);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger,
					"------- Exception Occured in closeCompositeSolution() --------", e.getMessage());
		}
	}

	@Test
	public void saveCompositeSolution() throws JSONException {

		CompositeServiceImpl compositeServiceImpl = new CompositeServiceImpl();
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
		} catch (CustomException e) {
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
				} catch (CustomException e) {
					logger.error(EELFLoggerDelegator.errorLogger, e.getMessage());
				}

				// CASE 3 :where user tries to update the existing solution
				// with new version
				dscs.setVersion("1.0.1");
				try {
					result = compositeServiceImpl.saveCompositeSolution(dscs);
					logger.debug(EELFLoggerDelegator.debugLogger,
							"Result of update existing Composite Solution with new version : " + result);
				} catch (CustomException e) {
					logger.error(EELFLoggerDelegator.errorLogger, e.getMessage());
				}

				// CASE 4 :where user tries to update the previous version
				dscs.setVersion("1.0.0");
				try {
					result = compositeServiceImpl.saveCompositeSolution(dscs);
					logger.debug(EELFLoggerDelegator.debugLogger,
							"Result of update previous version of the Composite Solution : " + result);
				} catch (CustomException e) {
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
				} catch (CustomException e) {
					logger.error(EELFLoggerDelegator.errorLogger, e.getMessage());
				}

				// NEW CASE :When user tries to update the existing solution
				// with a different name
				dscs.setSolutionName(RandomStringUtils.randomAlphanumeric(8));
				try {
					result = compositeServiceImpl.saveCompositeSolution(dscs);

					logger.debug(EELFLoggerDelegator.debugLogger,
							"Result of update existing Composite Solution with new solution name : " + result);
				} catch (CustomException e) {
					logger.error(EELFLoggerDelegator.errorLogger, e.getMessage());
				}
			}
		}
	}

	@Test
	public void deleteCompositeSolution() {
		try {
			boolean isSolutionDeleted = false;
			String userId = "e57490ed-8462-4a77-ab39-157138dfbda8";
			String solutionId = "710d881b-e926-4412-831c-10b0bf04c354yyy"; // correct
																			// solutionId
																			// 710d881b-e926-4412-831c-10b0bf04c354
			String version = "1.0.0";
			CompositeServiceImpl iCompositeSolutionService = new CompositeServiceImpl();
			iCompositeSolutionService.getRestCCDSClient((CommonDataServiceRestClientImpl) cmnDataService);
			iCompositeSolutionService.getNexusClient(nexusArtifactClient, confprops1, properties);
			isSolutionDeleted = iCompositeSolutionService.deleteCompositeSolution(userId, solutionId, version);
			Assert.assertNotNull(isSolutionDeleted);
			logger.info("deleteCompositeSolution {}", isSolutionDeleted);

		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, e.getMessage());
		}
	}

	@Test
	public void getMatchingModels() {
		SolutionServiceImpl solutionServiceImpl = new SolutionServiceImpl();
		solutionServiceImpl.getRestCCDSClient((CommonDataServiceRestClientImpl) cmnDataService);
		solutionServiceImpl.getNexusClient(nexusArtifactClient, confprops1, properties);
		String userId = "";
		String portType = "output";
		String protobufJsonString = "[{\"rule\":\"repeated\",\"tag\":\"1\",\"type\":\"string\"},{\"rule\":\"repeated\",\"tag\":\"2\",\"type\":\"string\"}]";
		try {
			JSONArray protobufJsonString1 = new JSONArray(protobufJsonString);
			String getMatchingModelsResult = solutionServiceImpl.getMatchingModels(userId, portType,
					protobufJsonString1);
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
		String protobufJsonString = "[{\"rule\":\"repeated\",\"tag\":\"1\",\"type\":\"string\"},{\"rule\":\"repeated\",\"tag\":\"2\",\"type\":\"string\"}]";
		try {
			JSONArray protobufJsonString1 = new JSONArray(protobufJsonString);
			String getMatchingModelsResult = solutionServiceImpl.getMatchingModels(userId, portType,
					protobufJsonString1);
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
		try {
			String sId = "040fe8f7-14f7-45e1-b46d-2de505e9d52d";
			String version = "1.0.0";
			when(props.getArtifactTypeCode()).thenReturn("CD");
			when(confprops.getToscaOutputFolder()).thenReturn(localpath);
			solutionService.getRestCCDSClient((CommonDataServiceRestClientImpl) cmnDataService);
			solutionService.getNexusClient(nexusArtifactClient, confprops, props);
			String result = solutionService.readCompositeSolutionGraph(userId, sId, version);
			Assert.assertNotNull(result);
			logger.info("readCompositeSolutionGraph {}", result);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, e.getMessage());
		}
	}

	@Test
	public void getSolutions() throws Exception {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			sdf.format(new Date());
			when(confprops.getDateFormat()).thenReturn(sdf.format(new Date()));
			when(props.getCompositSolutiontoolKitTypeCode()).thenReturn("CP");
			when(props.getPublicAccessTypeCode()).thenReturn("PB");
			when(props.getPrivateAccessTypeCode()).thenReturn("PV");
			when(props.getOrganizationAccessTypeCode()).thenReturn("OR");
			solutionService.getRestCCDSClient((CommonDataServiceRestClientImpl) cmnDataService);
			String result = solutionService.getSolutions(userId);
			System.out.println(result);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, e.getMessage());
		}
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
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, e.getMessage());
		}

	}

	private NexusArtifactClient getNexusClient() {
		try {
			RepositoryLocation repositoryLocation = new RepositoryLocation();
			repositoryLocation.setId("1");
			repositoryLocation.setUrl(
					"http://acumos_model_rw:not4you@nexus.acumos.com:8081/repository/repo_acumos_model_maven");
			repositoryLocation.setUsername("acumos_model_rw");
			repositoryLocation.setPassword("not4you");
			// repositoryLocation.setProxy("http://one.proxy.att.com:8080");
			nexusArtifactClient = new NexusArtifactClient(repositoryLocation);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, e.getMessage());
		}

		return nexusArtifactClient;
	}
}
