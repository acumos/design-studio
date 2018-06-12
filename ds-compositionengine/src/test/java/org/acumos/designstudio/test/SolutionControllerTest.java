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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.acumos.cds.AccessTypeCode;
import org.acumos.cds.ModelTypeCode;
import org.acumos.cds.ValidationStatusCode;
import org.acumos.cds.client.CommonDataServiceRestClientImpl;
import org.acumos.cds.client.ICommonDataServiceRestClient;
import org.acumos.cds.domain.MLPArtifact;
import org.acumos.cds.domain.MLPSolution;
import org.acumos.cds.domain.MLPSolutionRevision;
import org.acumos.cds.domain.MLPUser;
import org.acumos.cds.transport.RestPageRequest;
import org.acumos.cds.transport.RestPageResponse;
import org.acumos.designstudio.ce.exceptionhandler.AcumosException;
import org.acumos.designstudio.ce.exceptionhandler.ServiceException;
import org.acumos.designstudio.ce.service.AcumosCatalogServiceImpl;
import org.acumos.designstudio.ce.service.CompositeSolutionServiceImpl;
import org.acumos.designstudio.ce.service.DataBrokerServiceImpl;
import org.acumos.designstudio.ce.service.GenericDataMapperServiceImpl;
import org.acumos.designstudio.ce.service.SolutionServiceImpl;
import org.acumos.designstudio.ce.util.ConfigurationProperties;
import org.acumos.designstudio.ce.util.DSUtil;
import org.acumos.designstudio.ce.util.EELFLoggerDelegator;
import org.acumos.designstudio.ce.vo.Artifact;
import org.acumos.designstudio.ce.vo.DSCompositeSolution;
import org.acumos.designstudio.ce.vo.cdump.Argument;
import org.acumos.designstudio.ce.vo.cdump.Capabilities;
import org.acumos.designstudio.ce.vo.cdump.CapabilityTarget;
import org.acumos.designstudio.ce.vo.cdump.Cdump;
import org.acumos.designstudio.ce.vo.cdump.Message;
import org.acumos.designstudio.ce.vo.cdump.Ndata;
import org.acumos.designstudio.ce.vo.cdump.Nodes;
import org.acumos.designstudio.ce.vo.cdump.Property;
import org.acumos.designstudio.ce.vo.cdump.ReqCapability;
import org.acumos.designstudio.ce.vo.cdump.Requirements;
import org.acumos.designstudio.ce.vo.cdump.Target;
import org.acumos.designstudio.ce.vo.cdump.Type;
import org.acumos.designstudio.ce.vo.cdump.collator.CollatorInputField;
import org.acumos.designstudio.ce.vo.cdump.collator.CollatorMap;
import org.acumos.designstudio.ce.vo.cdump.collator.CollatorMapInput;
import org.acumos.designstudio.ce.vo.cdump.collator.CollatorMapOutput;
import org.acumos.designstudio.ce.vo.cdump.collator.CollatorOutputField;
import org.acumos.designstudio.ce.vo.cdump.databroker.DBInputField;
import org.acumos.designstudio.ce.vo.cdump.databroker.DBMapInput;
import org.acumos.designstudio.ce.vo.cdump.databroker.DBMapOutput;
import org.acumos.designstudio.ce.vo.cdump.databroker.DBOTypeAndRoleHierarchy;
import org.acumos.designstudio.ce.vo.cdump.databroker.DBOutputField;
import org.acumos.designstudio.ce.vo.cdump.databroker.DataBrokerMap;
import org.acumos.designstudio.ce.vo.cdump.datamapper.DataMap;
import org.acumos.designstudio.ce.vo.cdump.datamapper.DataMapInputField;
import org.acumos.designstudio.ce.vo.cdump.datamapper.DataMapOutputField;
import org.acumos.designstudio.ce.vo.cdump.datamapper.FieldMap;
import org.acumos.designstudio.ce.vo.cdump.datamapper.MapInputs;
import org.acumos.designstudio.ce.vo.cdump.datamapper.MapOutput;
import org.acumos.designstudio.ce.vo.cdump.splitter.SplitterInputField;
import org.acumos.designstudio.ce.vo.cdump.splitter.SplitterMap;
import org.acumos.designstudio.ce.vo.cdump.splitter.SplitterMapInput;
import org.acumos.designstudio.ce.vo.cdump.splitter.SplitterMapOutput;
import org.acumos.designstudio.ce.vo.cdump.splitter.SplitterOutputField;
import org.acumos.nexus.client.NexusArtifactClient;
import org.acumos.nexus.client.RepositoryLocation;
import org.acumos.nexus.client.data.UploadArtifactInfo;
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
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

/**
 *
 *
 */
public class SolutionControllerTest {
	private static EELFLoggerDelegator logger = EELFLoggerDelegator.getLogger(SolutionControllerTest.class);
	private String url = "";
	private String user = "";
	private String pass = "";
	
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
	 * This method is used to set default values for the instance of
	 * ICommonDataServiceRestClient and NexusArtifactClient by passing common
	 * data service and nexus url, username and password respectively
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
	
	@Mock
	ResourceLoader resourceLoader;
	
	@Mock
	Resource resource;
	
	@Mock
	NexusArtifactClient nexusArtifactClient;

	@InjectMocks
	SolutionServiceImpl solutionService;
	
	@InjectMocks
	AcumosCatalogServiceImpl  acumosCatalogServiceImpl;

	@InjectMocks
	CompositeSolutionServiceImpl compositeService;
	
	@InjectMocks
	DataBrokerServiceImpl dataBrokerServiceImpl;
	
	@Autowired
	ConfigurationProperties confprops1;

	@Mock
	org.acumos.designstudio.ce.util.Properties properties;

	@InjectMocks
	GenericDataMapperServiceImpl gdmService;
	
	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();

	@Test
	/**
	 * The test case is used to create a new composite solution. The test case
	 * uses createNewCompositeSolution method which consumes userId and returns
	 * cid(generated as UUID) and creation time in a string format which is then
	 * stored in CDUMP.json.The file is used by ds-composition engine to
	 * represent a composite solution made by connecting models.
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
				cdump.setProbeIndicator("false");
				assertNotNull(cdump);
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
				throw new ServiceException("Unable to create new CDUMP file");
			}
		} catch (ServiceException e) {
			logger.error(EELFLoggerDelegator.errorLogger,
					" Exception Occured in createNewCompositeSolution() ",e);
			throw e;
		}
	}

	@Test
	/**
	 * The test case is used to add node(a model)to create composite solution.
	 * The test case uses addNode method which consumes userId, solutionId,
	 * version, cid, node and returns the node data stored in CDUMP.json.
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
		node.setProtoUri("com/org/xyz");
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

		Type type = new Type();
		type.setName("xyz1");
		node.setType(type);
		
		assertNotNull(node);
		assertNotNull(req);
		assertNotNull(reqCap);
		assertNotNull(target);
		assertEquals("Node1", node.getName());
		assertEquals("1", cap.getId());
		assertEquals("ReqName", req.getName());
		assertEquals("200", data.getNtype());

		when(confprops.getToscaOutputFolder()).thenReturn(localpath);
		String result = solutionService.addNode(userId, null, null, sessionId, node);
		assertNotNull(result);
		logger.debug(EELFLoggerDelegator.debugLogger, result);
	}

	@Test
	/**
	 * The test case is used to add node(a model)to create composite solution.
	 * The test case uses addNode method which consumes userId, solutionId,
	 * version, cid, node and returns the node data stored in CDUMP.json.
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
		node.setProtoUri("com/org/xyz");
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
		Type type = new Type();
		type.setName("xyz1");
		node.setType(type);
		
		assertNotNull(node);
		assertNotNull(req);
		assertNotNull(reqCap);
		assertNotNull(target);
		assertEquals("Node2", node.getName());
		assertEquals("1", cap.getId());
		assertEquals("ReqName", req.getName());
		assertEquals("200", data.getNtype());

		when(confprops.getToscaOutputFolder()).thenReturn(localpath);
		String result = solutionService.addNode(userId, null, null, sessionId, node);
		assertNotNull(result);
		logger.debug(EELFLoggerDelegator.debugLogger, result);
	}

	@Test
	/**
	 * The test case is used to add node(a model)to create composite solution.
	 * The test case uses addNode method which consumes userId, solutionId,
	 * version, cid, node and returns the node data stored in CDUMP.json.
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
		node.setProtoUri("com/org/xyz");
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
		Type type = new Type();
		type.setName("xyz1");
		node.setType(type);

		assertNotNull(node);
		assertNotNull(req);
		assertNotNull(reqCap);
		assertNotNull(target);
		assertEquals("DM", node.getName());
		assertEquals("1", cap.getId());
		assertEquals("ReqName", req.getName());
		assertEquals("200", data.getNtype());

		when(confprops.getToscaOutputFolder()).thenReturn(localpath);
		String result = solutionService.addNode(userId, null, null, sessionId, node);
		assertNotNull(result);
		logger.debug(EELFLoggerDelegator.debugLogger, result);
	}

	@Test
	/**
	 * The test case is used to add node(a model)to create composite solution.
	 * The test case uses addNode method which consumes userId, solutionId,
	 * version, cid, node and returns the node data stored in CDUMP.json.
	 * 
	 * @throws Exception
	 */
	public void addNode3() throws Exception {
		Nodes node = getNode();

		assertNotNull(node);
		assertEquals("Node9", node.getName());

		when(confprops.getToscaOutputFolder()).thenReturn(localpath);
		String result = solutionService.addNode(userId, null, null, sessionId, node);
		assertNotNull(result);
		logger.debug(EELFLoggerDelegator.debugLogger, result);
	}

	@Test
	/**
	 * The test case is used to link two models to create composite solution.
	 * The test case uses addLink method which consumes userId, solutionId,
	 * version, linkName, linkId, sourceNodeName, sourceNodeId, targetNodeName,
	 * targetNodeId, sourceNodeRequirement, targetNodeCapabilityName, cid,
	 * property and updates the relation between the source and target node
	 * stored in CDUMP.json.
	 * 
	 * @throws Exception
	 */
	public void linkShouldGetAddedBetweenModels() throws Exception {
		Property property = new Property();
		assertNull(property.getData_map());
		when(confprops.getToscaOutputFolder()).thenReturn(localpath);
		boolean result = solutionService.addLink(userId, null, null, "Node1 to Node2", "101", "Node1", "1", "Node2",
				"2", "Req2", "Cap2", sessionId, property);
		//assertTrue(result);
	}

	@Test
	/**
	 * The test case is used to link Input port of DM to model to create
	 * composite solution. The test case uses addLink method which consumes
	 * userId, solutionId, version, linkName, linkId, sourceNodeName,
	 * sourceNodeId, targetNodeName, targetNodeId, sourceNodeRequirement,
	 * targetNodeCapabilityName, cid, property and updates the relation between
	 * the source and target node stored in CDUMP.json.
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
		assertEquals("Prediction", map_inputsObj.getMessage_name());
		assertTrue(map_inputs.length == 1);

		when(confprops.getToscaOutputFolder()).thenReturn(localpath);
		boolean result = solutionService.addLink(userId, null, null, "Node1 to DM", "202", "Node1", "1", "DM", "3",
				"Req2", "Cap2", sessionId, property);
		//assertFalse(result);
		logger.debug(EELFLoggerDelegator.debugLogger, "true");
	}

	@Test
	/**
	 * The test case is used to link output port of DM to a model to create
	 * composite solution. The test case uses addLink method which consumes
	 * userId, solutionId, version, linkName, linkId, sourceNodeName,
	 * sourceNodeId, targetNodeName, targetNodeId, sourceNodeRequirement,
	 * targetNodeCapabilityName, cid, property and updates the relation between
	 * the source and target node stored in CDUMP.json.
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
		assertEquals("1", output_fieldsObj.gettag());
		assertEquals("Classification", map_outputsObj.getMessage_name());
		assertTrue(output_fields.length == 1);

		when(confprops.getToscaOutputFolder()).thenReturn(localpath);
		boolean result = solutionService.addLink(userId, null, null, "DM to Node2", "303", "DM", "3", "Model 2", "2",
				"Req2", "Cap2", sessionId, property);
		assertFalse(result);
		logger.debug(EELFLoggerDelegator.debugLogger, "true {} ", result);
	}

	@Test
	/**
	 * The test case is used to modify node(a model)in a composite solution. The
	 * test case uses modifyNode method which consumes userId, solutionId,
	 * version, cid, nodeId, nodeName, ndata, fieldmap and returns the modified
	 * node data stored in CDUMP.json.
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
		DataBrokerMap databrokerMap = new DataBrokerMap();
		databrokerMap.setScript("this is the script");
		databrokerMap.setCsv_file_field_separator(",");
		databrokerMap.setData_broker_type("SQLDataBroker1");
		databrokerMap.setFirst_row("Yes");
		databrokerMap.setLocal_system_data_file_path("localpath");
		//databrokerMap.setMap_action("mapaction");
		databrokerMap.setTarget_system_url("remoteurl");
		
		// DataBrokerMap Input List
		List<DBMapInput> dbmapInputLst = new ArrayList<DBMapInput>();
		DBMapInput dbMapInput = new DBMapInput();
		DBInputField dbInField = new DBInputField();
		
		dbInField.setChecked("Yes");
		dbInField.setMapped_to_field("1.2");
		dbInField.setName("Name of SourceField");
		dbInField.setType("Float");
		
		dbMapInput.setInput_field(dbInField);
		dbmapInputLst.add(dbMapInput);
		DBMapInput[] dbMapInArr = new DBMapInput[dbmapInputLst.size()];
		dbMapInArr = dbmapInputLst.toArray(dbMapInArr);
		databrokerMap.setMap_inputs(dbMapInArr);
		
		// DataBrokerMap Output List
		List<DBMapOutput> dbmapOutputLst = new ArrayList<DBMapOutput>();
		DBMapOutput dbMapOutput = new DBMapOutput();
		DBOutputField dbOutField = new DBOutputField();
		
		dbOutField.setName("sepal_len");
		dbOutField.setTag("1.3");
		
		List<DBOTypeAndRoleHierarchy> dboList = new ArrayList<DBOTypeAndRoleHierarchy>();
		DBOTypeAndRoleHierarchy dboTypeAndRole = new DBOTypeAndRoleHierarchy();
		DBOTypeAndRoleHierarchy[] dboTypeAndRoleHierarchyArr = null;
		
		dboTypeAndRole.setName("DataFrameRow");
		dboTypeAndRole.setRole("Repeated");
		dboList.add(dboTypeAndRole);
		dboTypeAndRoleHierarchyArr = new DBOTypeAndRoleHierarchy[dboList.size()];
		dboTypeAndRoleHierarchyArr = dboList.toArray(dboTypeAndRoleHierarchyArr);
		
		dbOutField.setType_and_role_hierarchy_list(dboTypeAndRoleHierarchyArr);
		dbMapOutput.setOutput_field(dbOutField);
		dbmapOutputLst.add(dbMapOutput);
		
		DBMapOutput[] dbMapOutputArr = new DBMapOutput[dbmapOutputLst.size()];
		dbMapOutputArr = dbmapOutputLst.toArray(dbMapOutputArr);
		databrokerMap.setMap_outputs(dbMapOutputArr);
		
		// CollatorMap
		CollatorMap collatorMap = new CollatorMap();
		collatorMap.setCollator_type("Array-based");
		
		CollatorMapInput cmi = new CollatorMapInput();
		
		CollatorInputField cmif = new CollatorInputField();
		
		cmif.setMapped_to_field("1.2");
		cmif.setParameter_name("ParamName");
		cmif.setParameter_tag("1");
		cmif.setParameter_type("DataFrame");
		cmif.setSource_name("Aggregator");
		cmif.setError_indicator("Yes");
		cmi.setInput_field(cmif);
		List<CollatorMapInput> cmiList = new ArrayList<CollatorMapInput>();
		cmiList.add(cmi);
		CollatorMapInput[] cmiArray =  cmiList.toArray(new CollatorMapInput[cmiList.size()]);
		//CollatorMapInputs
		collatorMap.setMap_inputs(cmiArray);
		// CollatorMapOutputs
		
		CollatorMapOutput cmo = new CollatorMapOutput();
		CollatorOutputField cof = new CollatorOutputField();
		cof.setParameter_name("ParamName");
		cof.setParameter_rule("ParamRule");
		cof.setParameter_tag("ParamTag");
		cof.setParameter_type("ParamType");
		cmo.setOutput_field(cof);
		
		List<CollatorMapOutput> cmoList = new ArrayList<CollatorMapOutput>();
		cmoList.add(cmo);
		CollatorMapOutput[] cmoArray = cmoList.toArray(new CollatorMapOutput[cmoList.size()]);
		collatorMap.setMap_outputs(cmoArray);
		
		//SplitterMap
		SplitterMap splitterMap = new SplitterMap();
		splitterMap.setInput_message_signature("Json Format of input msg Signature");
		splitterMap.setSplitter_type("Copy-based");
		
		// SplitterMapInputs
		SplitterMapInput smi = new SplitterMapInput();
		// need to set Input Field
		SplitterInputField sif = new SplitterInputField();
		sif.setParameter_name("parameter name in Source Protobuf file");
		smi.setInput_field(sif);
		// Take a List of SplitterMapInput to convert it into Array
		List<SplitterMapInput> smiList = new ArrayList<SplitterMapInput>();
		smiList.add(smi);
		SplitterMapInput[] smiArr = smiList.toArray(new SplitterMapInput[smiList.size()]);
		splitterMap.setMap_inputs(smiArr);
		
		// SplitterMapOutput
		SplitterMapOutput smo = new SplitterMapOutput();
		SplitterOutputField sof = new SplitterOutputField();
		sof.setTarget_name("parameter name in Source Protobuf file");
		smo.setOutput_field(sof);
		List<SplitterMapOutput> smoList = new ArrayList<SplitterMapOutput>();
		smoList.add(smo);
		SplitterMapOutput[] smoArr = smoList.toArray(new SplitterMapOutput[smoList.size()]);
		splitterMap.setMap_outputs(smoArr);	
		

		assertNotNull(fieldMap);
		assertNotNull(databrokerMap);
		assertNotNull(splitterMap);
		assertNotNull(collatorMap);
		
		// assertEquals for collatorMap
		assertEquals("Array-based", collatorMap.getCollator_type());
		assertEquals("Json Format of Output msg Signature", collatorMap.getOutput_message_signature());

		// assertEquals for CollatorInputField
		assertEquals("1.2", cmif.getMapped_to_field());
		assertEquals("ParamName", cmif.getParameter_name());
		assertEquals("1", cmif.getParameter_tag());
		assertEquals("DataFrame", cmif.getParameter_type());
		assertEquals("Aggregator", cmif.getSource_name());
		assertEquals("False", cmif.getError_indicator());

		// assertEquals for CollatorOutputField
		assertEquals("ParamName", cof.getParameter_name());
		assertEquals("ParamRule", cof.getParameter_rule());
		assertEquals("ParamTag", cof.getParameter_tag());
		assertEquals("ParamType", cof.getParameter_type());

		// assertEquals for SplitterMap
		assertEquals("Json Format of input msg Signature", splitterMap.getInput_message_signature());
		assertEquals("Copy-based", splitterMap.getSplitter_type());

		// assertEquals for SplitterInputField
		assertEquals("parameter name in Source Protobuf file", sif.getParameter_name());
		assertEquals("parameter tag", sif.getParameter_tag());
		assertEquals("name of parameter", sif.getParameter_type());

		// assertEquals for SplitterOutputField
		assertEquals("parameter name in Source Protobuf file", sof.getTarget_name());
		assertEquals("parameter name", sof.getParameter_name());
		assertEquals("name of parameter", sof.getParameter_type());
		assertEquals("tag number", sof.getParameter_tag());
		assertEquals("tag number of the field", sof.getMapped_to_field());
		assertEquals("False", sof.getError_indicator());

		assertEquals("Prediction", fieldMap.getInput_field_message_name());
		
		assertEquals("Prediction", fieldMap.getInput_field_message_name());
		assertEquals("1", fieldMap.getInput_field_tag_id());
		assertEquals("Add", fieldMap.getMap_action());
		assertEquals("Classification", fieldMap.getOutput_field_message_name());

		when(confprops.getToscaOutputFolder()).thenReturn(localpath);
		String result = solutionService.modifyNode(userId, null, null, sessionId, "1", "Node1", ndata, fieldMap, databrokerMap,collatorMap,splitterMap);
		assertNotNull(result);
		logger.debug(EELFLoggerDelegator.debugLogger, result);
	}

	@Test
	/**
	 * The test case is used to modify node(a model)in a composite solution. The
	 * test case uses modifyNode method which consumes userId, solutionId,
	 * version, cid, nodeId, nodeName, ndata, fieldmap and returns the modified
	 * node data stored in CDUMP.json.
	 * 
	 * @throws Exception
	 */
	public void modifyNode1() throws Exception {
		String ndata = "{\"ntype\":\"500\",\"px\":\"500\",\"py\":\"500\",\"radius\":\"500\",\"fixed\":false}";
		FieldMap fieldMap = new FieldMap();
		DataBrokerMap databrokerMap = new DataBrokerMap();
		databrokerMap.setScript("this is the script");
		databrokerMap.setCsv_file_field_separator(",");
		databrokerMap.setData_broker_type("SQLDataBroker1");
		databrokerMap.setFirst_row("Yes");
		databrokerMap.setLocal_system_data_file_path("localpath");
		//databrokerMap.setMap_action("mapaction");
		databrokerMap.setTarget_system_url("remoteurl");
		
		// DataBrokerMap Input List
		List<DBMapInput> dbmapInputLst = new ArrayList<DBMapInput>();
		DBMapInput dbMapInput = new DBMapInput();
		DBInputField dbInField = new DBInputField();
		
		dbInField.setChecked("Yes");
		dbInField.setMapped_to_field("1.2");
		dbInField.setName("Name of SourceField");
		dbInField.setType("Float");
		
		dbMapInput.setInput_field(dbInField);
		dbmapInputLst.add(dbMapInput);
		DBMapInput[] dbMapInArr = new DBMapInput[dbmapInputLst.size()];
		dbMapInArr = dbmapInputLst.toArray(dbMapInArr);
		databrokerMap.setMap_inputs(dbMapInArr);
		
		// DataBrokerMap Output List
		List<DBMapOutput> dbmapOutputLst = new ArrayList<DBMapOutput>();
		DBMapOutput dbMapOutput = new DBMapOutput();
		DBOutputField dbOutField = new DBOutputField();
		
		dbOutField.setName("sepal_len");
		dbOutField.setTag("1.3");
		
		List<DBOTypeAndRoleHierarchy> dboList = new ArrayList<DBOTypeAndRoleHierarchy>();
		DBOTypeAndRoleHierarchy dboTypeAndRole = new DBOTypeAndRoleHierarchy();
		DBOTypeAndRoleHierarchy[] dboTypeAndRoleHierarchyArr = null;
		
		dboTypeAndRole.setName("DataFrameRow");
		dboTypeAndRole.setRole("Repeated");
		dboList.add(dboTypeAndRole);
		dboTypeAndRoleHierarchyArr = new DBOTypeAndRoleHierarchy[dboList.size()];
		dboTypeAndRoleHierarchyArr = dboList.toArray(dboTypeAndRoleHierarchyArr);
		
		dbOutField.setType_and_role_hierarchy_list(dboTypeAndRoleHierarchyArr);
		dbMapOutput.setOutput_field(dbOutField);
		dbmapOutputLst.add(dbMapOutput);
		
		DBMapOutput[] dbMapOutputArr = new DBMapOutput[dbmapOutputLst.size()];
		dbMapOutputArr = dbmapOutputLst.toArray(dbMapOutputArr);
		databrokerMap.setMap_outputs(dbMapOutputArr);
		
		
		when(confprops.getToscaOutputFolder()).thenReturn(localpath);
		assertNotNull(fieldMap);
		assertNotNull(databrokerMap);
	//	String result = solutionService.modifyNode(userId, null, null, sessionId, "2", "Node8", ndata, fieldMap, databrokerMap);
	//	assertNotNull(result);
	//	logger.debug(EELFLoggerDelegator.debugLogger, result);
	}

	@Test
	/**
	 * The test case is used to modify DM node(a model)in a composite solution.
	 * The test case uses modifyNode method which consumes userId, solutionId,
	 * version, cid, nodeId, nodeName, ndata, fieldmap and returns the modified
	 * node data stored in CDUMP.json.
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
		DataBrokerMap databrokerMap = new DataBrokerMap();
		databrokerMap.setScript("this is the script");
		databrokerMap.setCsv_file_field_separator(",");
		databrokerMap.setData_broker_type("SQLDataBroker1");
		databrokerMap.setFirst_row("Yes");
		databrokerMap.setLocal_system_data_file_path("localpath");
		//databrokerMap.setMap_action("mapaction");
		databrokerMap.setTarget_system_url("remoteurl");
		
		// DataBrokerMap Input List
		List<DBMapInput> dbmapInputLst = new ArrayList<DBMapInput>();
		DBMapInput dbMapInput = new DBMapInput();
		DBInputField dbInField = new DBInputField();
		
		dbInField.setChecked("Yes");
		dbInField.setMapped_to_field("1.2");
		dbInField.setName("Name of SourceField");
		dbInField.setType("Float");
		
		dbMapInput.setInput_field(dbInField);
		dbmapInputLst.add(dbMapInput);
		DBMapInput[] dbMapInArr = new DBMapInput[dbmapInputLst.size()];
		dbMapInArr = dbmapInputLst.toArray(dbMapInArr);
		databrokerMap.setMap_inputs(dbMapInArr);
		
		// DataBrokerMap Output List
		List<DBMapOutput> dbmapOutputLst = new ArrayList<DBMapOutput>();
		DBMapOutput dbMapOutput = new DBMapOutput();
		DBOutputField dbOutField = new DBOutputField();
		
		dbOutField.setName("sepal_len");
		dbOutField.setTag("1.3");
		
		List<DBOTypeAndRoleHierarchy> dboList = new ArrayList<DBOTypeAndRoleHierarchy>();
		DBOTypeAndRoleHierarchy dboTypeAndRole = new DBOTypeAndRoleHierarchy();
		DBOTypeAndRoleHierarchy[] dboTypeAndRoleHierarchyArr = null;
		
		dboTypeAndRole.setName("DataFrameRow");
		dboTypeAndRole.setRole("Repeated");
		dboList.add(dboTypeAndRole);
		dboTypeAndRoleHierarchyArr = new DBOTypeAndRoleHierarchy[dboList.size()];
		dboTypeAndRoleHierarchyArr = dboList.toArray(dboTypeAndRoleHierarchyArr);
		
		dbOutField.setType_and_role_hierarchy_list(dboTypeAndRoleHierarchyArr);
		dbMapOutput.setOutput_field(dbOutField);
		dbmapOutputLst.add(dbMapOutput);
		
		DBMapOutput[] dbMapOutputArr = new DBMapOutput[dbmapOutputLst.size()];
		dbMapOutputArr = dbmapOutputLst.toArray(dbMapOutputArr);
		databrokerMap.setMap_outputs(dbMapOutputArr);
		
		
		assertNotNull(fieldMap);
		assertNotNull(databrokerMap);
		assertEquals("Prediction", fieldMap.getInput_field_message_name());
		assertEquals("Classification", fieldMap.getOutput_field_message_name());

		when(confprops.getToscaOutputFolder()).thenReturn(localpath);
	//	String result = solutionService.modifyNode(userId, null, null, sessionId, "8", "Node8", ndata, fieldMap, databrokerMap);
	//	assertNotNull(result);
	//	logger.debug(EELFLoggerDelegator.debugLogger, result);
	}

	@Test
	/**
	 * The test case is used to modify link in a composite solution. The test
	 * case uses modifyLink method which consumes userId, solutionId, version,
	 * linkId, linkName and updates the relation between the source and target
	 * node stored in CDUMP.json.
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
	 * The test case is used to modify link in a composite solution. The test
	 * case uses modifyLink method which consumes userId, solutionId, version,
	 * linkId, linkName and updates the relation between the source and target
	 * node stored in CDUMP.json.
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
	 * The test case is used to delete a link between the two models.The test
	 * case uses deleteLink method which consumes cid, solutionId, version,
	 * linkId and updates CDUMP.json by deleting relation between the source and
	 * target nodes
	 * 
	 * @throws Exception
	 */
	public void linkBetweenTwoModelShouldGetDeleted() throws Exception {
		try {
			when(confprops.getToscaOutputFolder()).thenReturn(localpath);
			boolean result = solutionService.deleteLink(userId, null, null, sessionId, "101");
			assertNotNull(result);
			if (result == true) {
				logger.debug(EELFLoggerDelegator.debugLogger, "Link deleted  {} ", result);
			} else {
				throw new ServiceException("Link Not Deleted");
			}
		} catch (ServiceException e) {
			logger.error(EELFLoggerDelegator.errorLogger, "Action Failed", e);
		}
	}

	@Test
	/**
	 * The test case is used to delete a link between the Model and DM.The test
	 * case uses deleteLink method which consumes cid, solutionId, version,
	 * linkId and updates CDUMP.json by deleting relation between the source and
	 * target nodes
	 * 
	 * @throws Exception
	 */
	public void linkBetweenIpOfDataMapperandModelShouldGetDeleted() throws Exception {
		try {
			when(confprops.getToscaOutputFolder()).thenReturn(localpath);
			boolean result = solutionService.deleteLink(userId, null, null, sessionId, "202");
			assertNotNull(result);
			if (result == true) {
				logger.debug(EELFLoggerDelegator.debugLogger, "Link deleted  {} ", result);
			} else {
				throw new ServiceException("Link Not Deleted");
			}
		} catch (ServiceException e) {
			logger.error(EELFLoggerDelegator.errorLogger, "Action Failed", e);
		}
	}

	@Test
	/**
	 * The test case is used to delete a link between the DM and Model.The test
	 * case uses deleteLink method which consumes cid, solutionId, version,
	 * linkId and updates CDUMP.json by deleting relation between the source and
	 * target nodes
	 * 
	 * @throws Exception
	 */
	public void linkBetweenPpOfDataMapperandModelShouldGetDeleted() throws Exception {
		try {
			when(confprops.getToscaOutputFolder()).thenReturn(localpath);
			boolean result = solutionService.deleteLink(userId, null, null, sessionId, "303");
			assertNotNull(result);
			if (result == true) {
				logger.debug(EELFLoggerDelegator.debugLogger, "Link deleted  {} ", result);
			} else {
				throw new ServiceException("Link Not Deleted");
			}
		} catch (ServiceException e) {
			logger.error(EELFLoggerDelegator.errorLogger, "Action Failed", e);
		}
	}

	@Test
	/**
	 * The test case is used to delete a link between the two nodes.The test
	 * case uses deleteLink method which consumes cid, solutionId, version,
	 * linkId and updates CDUMP.json by deleting relation between the source and
	 * target nodes
	 * 
	 * @throws Exception
	 */
	public void linkShouldNotGetDeleted() throws Exception {
		try {
			when(confprops.getToscaOutputFolder()).thenReturn(localpath);
			boolean result = solutionService.deleteLink(userId, null, null, sessionId, "404");
			assertNotNull(result);
			if (result == true) {
				logger.debug(EELFLoggerDelegator.debugLogger, "Link deleted  {} ", result);
			} else {
				throw new ServiceException("Link Not Deleted");
			}
		} catch (ServiceException e) {
			logger.error(EELFLoggerDelegator.errorLogger, "Action Failed", e);
		}
	}

	@Test
	/**
	 * The test case is used to delete a node.The test case uses deleteNode
	 * method which consumes userId, solutionId, version, cid, nodeId and
	 * updates CDUMP.json by deleting the node and its relation with other
	 * nodes.
	 * 
	 * @throws Exception
	 */
	public void dMnodeShouldGetDeleted() throws Exception {
		try {
			when(confprops.getToscaOutputFolder()).thenReturn(localpath);
			boolean result = solutionService.deleteNode(userId, null, null, sessionId, "3");
			assertNotNull(result);
			if (result == true) {
				logger.debug(EELFLoggerDelegator.debugLogger, "Node deleted  {} ", result);
			} else {
				throw new ServiceException("Node Not Deleted");
			}
		} catch (ServiceException e) {
			logger.error(EELFLoggerDelegator.errorLogger, "Action Failed", e);
		}
	}

	@Test
	/**
	 * The test case is used to delete a node.The test case uses deleteNode
	 * method which consumes userId, solutionId, version, cid, nodeId and
	 * updates CDUMP.json by deleting the node and its relation with other
	 * nodes.
	 * 
	 * @throws Exception
	 */
	public void modelnodeShouldGetDeleted() throws Exception {
		try {
			when(confprops.getToscaOutputFolder()).thenReturn(localpath);
			boolean result = solutionService.deleteNode(userId, null, null, sessionId, "2");
			if (result == true) {
				logger.debug(EELFLoggerDelegator.debugLogger, "Node deleted  {} ", result);
			} else {
				throw new ServiceException("Node Not Deleted");
			}
		} catch (ServiceException e) {
			logger.error(EELFLoggerDelegator.errorLogger, "Action Failed", e);
		}
	}

	@Test
	/**
	 * The test case is used to delete a node.The test case uses deleteNode
	 * method which consumes userId, solutionId, version, cid, nodeId and
	 * updates CDUMP.json by deleting the node and its relation with other
	 * nodes.
	 * 
	 * @throws Exception
	 */
	public void nodeShouldGetDeleted() throws Exception {
		try {
			when(confprops.getToscaOutputFolder()).thenReturn(localpath);
			boolean result = solutionService.deleteNode(userId, null, null, sessionId, "5");
			if (result == true) {
				logger.debug(EELFLoggerDelegator.debugLogger, "Node deleted  {} ", result);
			} else {
				throw new ServiceException("Node Not Deleted");
			}
		} catch (ServiceException e) {
			logger.error(EELFLoggerDelegator.errorLogger, "Action Failed", e);
		}
	}

	@Test
	/**
	 * The test case is used to validate the composite solution and create a
	 * Blueprint.josn file to be used by end user.The test case uses
	 * validateCompositeSolution method which consumes userId solutionName,
	 * solutionId, version and returns success message as well as stores the
	 * bluprint.josn file in nexus repository and creates an artifact of the
	 * type "BP" against the passed solutionId and version in the database
	 * 
	 * @throws Exception
	 */
	public void validateCompositeSolution()  {
		CompositeSolutionServiceImpl csimpl = new CompositeSolutionServiceImpl();
		
		
		List<MLPSolutionRevision> mlpSolRevisions = new ArrayList<MLPSolutionRevision>();
		MLPSolutionRevision mlpSolutionRevision = new MLPSolutionRevision();
		mlpSolutionRevision.setSolutionId("222");
		mlpSolutionRevision.setDescription("Testing Save Function");
		mlpSolutionRevision.setOwnerId(userId);
		mlpSolutionRevision.setVersion("1.0.0");
		mlpSolutionRevision.setRevisionId("111");
		
		mlpSolRevisions.add(mlpSolutionRevision);
		List<MLPArtifact> artifacts = new ArrayList<MLPArtifact>();
		MLPArtifact artifact = new MLPArtifact();
		artifact.setArtifactTypeCode("DI");
		artifact.setUri("xyz");
		artifacts.add(artifact);
		csimpl.getRestCCDSClient((CommonDataServiceRestClientImpl) cmnDataService);
		csimpl.getNexusClient(nexusArtifactClient, confprops, properties);
		csimpl.setGenericDataMapperServiceImpl(gdmService);
		
		when(cmnDataService.getSolutionRevisions("222")).thenReturn(mlpSolRevisions);
		when(cmnDataService.getSolutionRevisionArtifacts("222", null)).thenReturn(artifacts);
		when(confprops.getToscaOutputFolder()).thenReturn(localpath);
		when(confprops.getNexusgroupid()).thenReturn("xyz");
		
		String cdumpFileName = "acumos-cdump" + "-" + "4f91545a-e674-46af-a4ad-d6514f41de9b";
		String path = DSUtil.readCdumpPath(userId, localpath);
		UploadArtifactInfo artifactInfo = null;
		FileInputStream fileInputStream ; //= mock(FileInputStream.class);
		Artifact cdumpArtifact = null;
		Gson gson = new Gson();
		
		Cdump cdump = new Cdump();
		cdump.setCid(sessionId);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		cdump.setCtime(sdf.format(new Date()));
		cdump.setProbeIndicator("false");
		//cdump.setSolutionId("8fcc3384-e3f8-4520-af1c-413d9495a154");
		List<Nodes> nodes = new ArrayList<Nodes>();
		Nodes node = getNode();
		nodes.add(node);
		cdump.setNodes(nodes);
		
		ObjectMapper mapper = new ObjectMapper();
		InputStream inputStream = null;
		
		try {
			Resource resourse1 = resourceLoader.getResource("classpath:Protobuf_Template.txt") ;
			when(props.getPackagepath()).thenReturn("/src/main/java");
			when(resourceLoader.getResource("classpath:Protobuf_Template.txt")).thenReturn(resourse1);
			when(resource.getInputStream()).thenReturn(inputStream);
			String result1 = gdmService.createDeployGDM(cdump, "54321", "8fcc3384-e3f8-4520-af1c-413d9495a154");
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger,
					" Exception Occured in createDeployGDM ", e);
		}
		String payload = gson.toJson(cdump);
		MLPArtifact mlpArtifact = null;
		try {
			/*fileInputStream = new FileInputStream(
					"./src/test/resources/8fcc3384-e3f8-4520-af1c-413d9495a154/BluePrint-4f91545a-e674-46af-a4ad-d6514f41de9b.json");
			PowerMockito.whenNew(FileInputStream.class).withAnyArguments().thenReturn(fileInputStream);
			artifactInfo = new UploadArtifactInfo("xyz", "1234", "1.0.0", "com.org.acumos", "com.org.acumos", 559);
			cdumpArtifact = new Artifact(cdumpFileName, "json", "4f91545a-e674-46af-a4ad-d6514f41de9b", "1.0.0", path,
					payload.length());
			PowerMockito.whenNew(Artifact.class).withAnyArguments().thenReturn(cdumpArtifact);
			when(nexusArtifactClient.uploadArtifact("xyz",
					cdumpArtifact.getSolutionID() + "_" + cdumpArtifact.getType(), cdumpArtifact.getVersion(),
					cdumpArtifact.getExtension(), cdumpArtifact.getContentLength(), fileInputStream))
							.thenReturn(artifactInfo);*/

			
			mlpArtifact = new MLPArtifact();
			mlpArtifact.setArtifactTypeCode("BP");
			mlpArtifact.setDescription("BluePrint File for : Test for SolutionID : "
					+ mlpSolutionRevision.getSolutionId() + " with version : " + mlpSolutionRevision.getVersion());
			mlpArtifact.setUri("xyz.com");
			mlpArtifact.setName("test");
			mlpArtifact.setOwnerId("8fcc3384-e3f8-4520-af1c-413d9495a154");
			mlpArtifact.setVersion("1.0.0");
			mlpArtifact.setSize(1);
			mlpArtifact.setArtifactId("333");
			//PowerMockito.whenNew(MLPArtifact.class).withAnyArguments().thenReturn(mlpArtifact);
			when(cmnDataService.createArtifact(mlpArtifact)).thenReturn(mlpArtifact);

			//CommonDataServiceRestClientImpl cmnDataService2 = mock(CommonDataServiceRestClientImpl.class);
			Mockito.doNothing().when(cmnDataService).addSolutionRevisionArtifact(Mockito.isA(String.class), Mockito.isA(String.class), Mockito.isA(String.class));
			cmnDataService.addSolutionRevisionArtifact(mlpSolutionRevision.getSolutionId(), mlpSolutionRevision.getRevisionId(),mlpArtifact.getArtifactId());
		    
			String result = csimpl.validateCompositeSolution(userId, "NewModel", "4f91545a-e674-46af-a4ad-d6514f41de9b", "1.0.0");
			//assertNotNull(result);
			logger.debug(EELFLoggerDelegator.debugLogger, result);
			
		} catch (Exception e1) {
			logger.error(EELFLoggerDelegator.errorLogger,
					" Exception Occured in uploadArtifact() ", e1);
		}
		
		
		
	}

	@Test
	/**
	 * The test case is used to clear the composite solution.The test case uses
	 * clearCompositeSolution method which consumes userId, solutionId,
	 * solutionVersion, cid and returns success message as true
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
			logger.error(EELFLoggerDelegator.errorLogger,
					" Exception Occured in clearCompositeSolution() ", e);
		}
	}

	@Test
	/**
	 * The test case is used to close the composite solution.The test case uses
	 * closeCompositeSolution method which consumes userId, solutionId,
	 * solutionVersion, cid and returns success message as true
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
			logger.error(EELFLoggerDelegator.errorLogger,
					" Exception Occured in closeCompositeSolution() ", e);
		}
	}

	@Test
	/**
	 * The test case is used to save the composite solution and store it in
	 * nexus repository as well as the database. The test case uses
	 * saveCompositeSolution method which consumes DSCompositeSolution object
	 * and returns solutionId and version or error message in string format
	 * 
	 * @throws JSONException
	 */
	public void saveCompositeSolution() throws JSONException {

		CompositeSolutionServiceImpl compositeServiceImpl = new CompositeSolutionServiceImpl();
		compositeServiceImpl.getRestCCDSClient((CommonDataServiceRestClientImpl) cmnDataService);
		compositeServiceImpl.getNexusClient(nexusArtifactClient, confprops, properties);
		SolutionServiceImpl solutionServiceImpl = new SolutionServiceImpl();
		solutionServiceImpl.getRestCCDSClient((CommonDataServiceRestClientImpl) cmnDataService);
		solutionServiceImpl.getNexusClient(nexusArtifactClient, confprops, properties);

		DSCompositeSolution dscs = new DSCompositeSolution();

		dscs.setcId(sessionId);
		dscs.setAuthor(userId);
		dscs.setSolutionName(RandomStringUtils.randomAlphanumeric(5) + "-Test");
		dscs.setSolutionName("Test");
		dscs.setSolutionId(null);
		dscs.setVersion("1.0.0");
		dscs.setOnBoarder(userId);
		dscs.setDescription("Testing Save Function");
		dscs.setProvider(properties.getProvider());
		dscs.setToolKit(properties.getToolKit());
		dscs.setVisibilityLevel(properties.getVisibilityLevel());
		dscs.setIgnoreLesserVersionConflictFlag(false);
		
		ArrayList<MLPSolution> mlpSols = new ArrayList<MLPSolution>();
		MLPSolution mlpSolution = new MLPSolution();
		mlpSolution.setSolutionId("1234");
		mlpSolution = new MLPSolution();
		mlpSolution.setName(dscs.getSolutionName());
		mlpSolution.setDescription(dscs.getDescription());
		mlpSolution.setOwnerId(dscs.getAuthor());
		mlpSolution.setValidationStatusCode(ValidationStatusCode.IP.toString());
		mlpSolution.setProvider(dscs.getProvider());
		mlpSolution.setAccessTypeCode(AccessTypeCode.PR.toString());
		mlpSolution.setModelTypeCode(ModelTypeCode.PR.toString());
		mlpSolution.setToolkitTypeCode("CP");
		
		MLPSolutionRevision mlpSolutionRevision = new MLPSolutionRevision();
		mlpSolutionRevision.setSolutionId(mlpSolution.getSolutionId());
		mlpSolutionRevision.setDescription(dscs.getDescription());
		mlpSolutionRevision.setOwnerId(dscs.getAuthor());
		mlpSolutionRevision.setVersion(dscs.getVersion());
		
		
		mlpSols.add(mlpSolution);
		RestPageResponse<MLPSolution> pageResponse = new RestPageResponse<MLPSolution>(mlpSols);
		when(properties.getProvider()).thenReturn("Acumos");
		when(properties.getToolKit()).thenReturn("CP");
		when(properties.getVisibilityLevel()).thenReturn("PR");
		when(confprops.getToscaOutputFolder()).thenReturn(localpath);
		when(confprops.getDateFormat()).thenReturn("yyyy-MM-dd HH:mm:ss.SSS");
		when(properties.getArtifactTypeCode()).thenReturn("CD");
		when(confprops.getNexusgroupid()).thenReturn("com.artifact");
		when(properties.getAskToUpdateExistingCompSolnMsg())
				.thenReturn("Do you want to update a previous version of this solution?");
		when(properties.getSolutionResultsetSize()).thenReturn(10);
		
		String result = null;

		

		Map<String, Object> queryParameters = new HashMap<String, Object>();
		queryParameters.put("name", "Test");
		RestPageRequest restPageRequets = new RestPageRequest(0,10);
		
		
		try {
			/*PowerMockito.whenNew(Map.class).withAnyArguments().thenReturn(queryParameters);
			PowerMockito.whenNew(RestPageRequest.class).withAnyArguments().thenReturn(restPageRequets);*/
			
			when(cmnDataService.searchSolutions(queryParameters, false, restPageRequets)).thenReturn(pageResponse);
			// CASE 1 : where New Composite Solution : CID exist and SolutionID
			// is
			// missing.
			
			when(cmnDataService.createSolution(mlpSolution)).thenReturn(mlpSolution);
			when(cmnDataService.createSolutionRevision(mlpSolutionRevision)).thenReturn(mlpSolutionRevision);
			when(confprops.getToscaOutputFolder()).thenReturn(localpath);
			
			result = compositeServiceImpl.saveCompositeSolution(dscs);
			logger.debug(EELFLoggerDelegator.debugLogger, "Result of Save Composite Solution :  {} ", result);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, "Exception in saveCompositeSolution" , e);
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
							"Result of update existing Composite Solution :  {} ", result);
				} catch (AcumosException e) {
					logger.error(EELFLoggerDelegator.errorLogger, "Exception in saveCompositeSolution", e);
				}

				// CASE 3 :where user tries to update the existing solution
				// with new version
				dscs.setVersion("1.0.1");
				try {
					result = compositeServiceImpl.saveCompositeSolution(dscs);
					logger.debug(EELFLoggerDelegator.debugLogger,
							"Result of update existing Composite Solution with new version :  {} ", result);
				} catch (AcumosException e) {
					logger.error(EELFLoggerDelegator.errorLogger, "Exception in saveCompositeSolution", e);
				}

				// CASE 4 :where user tries to update the previous version
				dscs.setVersion("1.0.0");
				try {
					result = compositeServiceImpl.saveCompositeSolution(dscs);
					logger.debug(EELFLoggerDelegator.debugLogger,
							"Result of update previous version of the Composite Solution :  {} ", result);
				} catch (AcumosException e) {
					logger.error(EELFLoggerDelegator.errorLogger,"Exception in saveCompositeSolution", e);
				}
				// CASE 5 :where user gives a command to update the previous
				// version of the solution by setting
				// ignoreLesserVersionConflictFlag as true
				dscs.setIgnoreLesserVersionConflictFlag(true);
				dscs.setVersion("1.0.0");
				try {
					result = compositeServiceImpl.saveCompositeSolution(dscs);
					logger.debug(EELFLoggerDelegator.debugLogger,
							"Result of update previous version of the Composite Solution :  {} ", result);
				} catch (AcumosException e) {
					logger.error(EELFLoggerDelegator.errorLogger, "Exception in saveCompositeSolution", e);
				}

				// NEW CASE :When user tries to update the existing solution
				// with a different name
				dscs.setSolutionName(RandomStringUtils.randomAlphanumeric(8));
				try {
					result = compositeServiceImpl.saveCompositeSolution(dscs);

					logger.debug(EELFLoggerDelegator.debugLogger,
							"Result of update existing Composite Solution with new solution name :  {} ", result);
				} catch (AcumosException e) {
					logger.error(EELFLoggerDelegator.errorLogger, "Exception in saveCompositeSolution", e);
				}
			}
		}
	}

	@Test
	/**
	 * The test case is used to delete the composite solution from nexus
	 * repository as well as the database.The test case uses
	 * deleteCompositeSolution method which consumes userId, solutionId, version
	 * and returns success message as true.
	 * 
	 * @throws Exception
	 */
	public void deleteCompositeSolution() throws Exception {
		try {
			boolean isSolutionDeleted = false;
			
			String version = "1.0.0";
			CompositeSolutionServiceImpl iCompositeSolutionService = new CompositeSolutionServiceImpl();
			iCompositeSolutionService.getRestCCDSClient((CommonDataServiceRestClientImpl) cmnDataService);
			iCompositeSolutionService.getNexusClient(nexusArtifactClient, confprops1, properties);
			MLPSolution mlpSolution = new MLPSolution();
			mlpSolution.setSolutionId("710d881b-e926-4412-831c-10b0bf04c354yyy");
			mlpSolution.setName("Test");
			mlpSolution.setDescription("sample");
			mlpSolution.setOwnerId(userId);
			mlpSolution.setValidationStatusCode(ValidationStatusCode.IP.toString());
			mlpSolution.setAccessTypeCode(AccessTypeCode.PR.toString());
			mlpSolution.setModelTypeCode(ModelTypeCode.PR.toString());
			mlpSolution.setToolkitTypeCode("CP");
			
			MLPSolutionRevision mlpSolutionRevision = new MLPSolutionRevision();
			mlpSolutionRevision.setRevisionId("84874435-d103-44c1-9451-d2b660fae766");
			mlpSolutionRevision.setSolutionId(mlpSolution.getSolutionId());
			mlpSolutionRevision.setDescription("test"); 
			mlpSolutionRevision.setOwnerId(userId);
			mlpSolutionRevision.setVersion("1.0.0");
			
			CompositeSolutionServiceImpl csimpl = new CompositeSolutionServiceImpl();
			List<MLPArtifact> artifacts = new ArrayList<MLPArtifact>();
			MLPArtifact artifact = new MLPArtifact();
			artifact.setArtifactTypeCode("DI");
			artifact.setUri("xyz");
			artifacts.add(artifact);
			csimpl.getRestCCDSClient((CommonDataServiceRestClientImpl) cmnDataService);
			csimpl.getNexusClient(nexusArtifactClient, confprops, properties);
			csimpl.setGenericDataMapperServiceImpl(gdmService);
			
			List<MLPSolutionRevision> mlpSolutionRevisionList = new ArrayList<MLPSolutionRevision>();
			mlpSolutionRevisionList.add(mlpSolutionRevision);
			when(cmnDataService.getSolution(mlpSolution.getSolutionId())).thenReturn(mlpSolution);	
			when(cmnDataService.getSolutionRevisions(mlpSolution.getSolutionId())).thenReturn(mlpSolutionRevisionList);
			when(cmnDataService.getSolutionRevisionArtifacts(mlpSolution.getSolutionId(), mlpSolutionRevision.getRevisionId())).thenReturn(artifacts);
			isSolutionDeleted = iCompositeSolutionService.deleteCompositeSolution(userId, mlpSolution.getSolutionId(), version);
			//assertTrue(isSolutionDeleted);
			logger.info(EELFLoggerDelegator.applicationLogger, "deleteCompositeSolution {}", isSolutionDeleted);
		} catch (AcumosException ex) {
			logger.error(EELFLoggerDelegator.errorLogger, "Exception in deleteCompositeSolution", ex);
		}
	}

	@Test
	/**
	 * The test case is used to search and enlist the models that are compatible
	 * to connect to the selected port(input / output) of a model. The test case
	 * uses getMatchingModels method which consumes userId, portType,
	 * protobufJsonString and returns the list of matching models in string
	 * format. ds-composition engine utilizes these models to be dragged and
	 * connect to the desired matching port of the selected model.
	 * 
	 * @throws JSONException
	 */
	public void getMatchingModels() {
		SolutionServiceImpl solutionServiceImpl = new SolutionServiceImpl();
		solutionServiceImpl.getRestCCDSClient((CommonDataServiceRestClientImpl) cmnDataService);
		solutionServiceImpl.getNexusClient(nexusArtifactClient, confprops1, properties);
		String userId = "";
		String portType = "output";
		String protobufJsonString = "[{\"role\":\"repeated\",\"tag\":\"1\",\"type\":\"string\"},{\"role\":\"repeated\",\"tag\":\"2\",\"type\":\"string\"}]";
		try {
			JSONArray protobufJsonString1 = new JSONArray(protobufJsonString);
			String getMatchingModelsResult = solutionServiceImpl.getMatchingModels(userId, portType,
					protobufJsonString1);
			Assert.assertNotNull(getMatchingModelsResult);
			logger.info(EELFLoggerDelegator.applicationLogger, "getMatchingModelsResult {}", getMatchingModelsResult);
			logger.debug(EELFLoggerDelegator.debugLogger, getMatchingModelsResult);
		} catch (JSONException je) {
			logger.error(EELFLoggerDelegator.errorLogger, "Exception in getMatchingModels", je);
		} catch (Exception ex) {
			logger.error(EELFLoggerDelegator.errorLogger, "Exception in getMatchingModels",  ex);
		}
	}

	@Test
	/**
	 * The test case is used to search and enlist the models that are compatible
	 * to connect to the selected port(input / output) of a model. The test case
	 * uses getMatchingModels method which consumes userId, portType,
	 * protobufJsonString and returns the list of matching models in string
	 * format. ds-composition engine utilizes these models to be dragged and
	 * connect to the desired matching port of the selected model.
	 * 
	 * @throws JSONException
	 */
	public void getMatchingModels1() {
		SolutionServiceImpl solutionServiceImpl = new SolutionServiceImpl();
		solutionServiceImpl.getRestCCDSClient((CommonDataServiceRestClientImpl) cmnDataService);
		solutionServiceImpl.getNexusClient(nexusArtifactClient, confprops1, properties);
		String userId = "";
		String portType = "input";
		String protobufJsonString = "[{\"role\":\"repeated\",\"tag\":\"1\",\"type\":\"string\"},{\"role\":\"repeated\",\"tag\":\"2\",\"type\":\"string\"}]";
		try {
			JSONArray protobufJsonString1 = new JSONArray(protobufJsonString);
			String getMatchingModelsResult = solutionServiceImpl.getMatchingModels(userId, portType,
					protobufJsonString1);
			Assert.assertNotNull(getMatchingModelsResult);
			logger.debug(EELFLoggerDelegator.debugLogger, getMatchingModelsResult);
		} catch (JSONException je) {
			logger.error(EELFLoggerDelegator.errorLogger, "Exception in getMatchingModels1" , je);
		} catch (Exception ex) {
			logger.error(EELFLoggerDelegator.errorLogger, "Exception in getMatchingModels1" ,ex);
		}

	}

	@Test
	/**
	 * The test case is used to read the composite solution by fetching the uri
	 * using cds and calling nexus API to get the CDUMP.json.The test case uses
	 * readCompositeSolutionGraph method which consumes userId, solutionID,
	 * version and returns the list of matching models in string format.
	 * 
	 * @throws Exception
	 */
	public void readCompositeSolutionGraph() throws AcumosException {
		String sId = "040fe8f7-14f7-45e1-b46d-2de505e9d52d";
		String version = "1.0.0";
		MLPSolution mlpSolution = new MLPSolution();
		mlpSolution.setSolutionId(sId);
		mlpSolution.setName("Test");
		mlpSolution.setDescription("sample");
		mlpSolution.setOwnerId(userId);
		mlpSolution.setValidationStatusCode(ValidationStatusCode.IP.toString());
		mlpSolution.setAccessTypeCode(AccessTypeCode.PR.toString());
		mlpSolution.setModelTypeCode(ModelTypeCode.PR.toString());
		mlpSolution.setToolkitTypeCode("CP");
		
		when(props.getArtifactTypeCode()).thenReturn("CD");
		when(cmnDataService.getSolution(sId)).thenReturn(mlpSolution);
		when(confprops.getToscaOutputFolder()).thenReturn(localpath);
		solutionService.getRestCCDSClient((CommonDataServiceRestClientImpl) cmnDataService);
		solutionService.getNexusClient(nexusArtifactClient, confprops, props);
		when(confprops.getToscaOutputFolder()).thenReturn(localpath);
		String result = solutionService.readCompositeSolutionGraph(userId, sId, version);
		Assert.assertNotNull(result);
		logger.info(EELFLoggerDelegator.applicationLogger, "readCompositeSolutionGraph {}", result);
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
	/**
	 * The test case is used to get the list of public or private or
	 * organization or all composite solutions accessible by user.The test case uses
	 * getCompositeSolutions method which consumes userId,visibilityLevel and returns the list of
	 * composite solutions in string format.
	 * 
	 */
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
			logger.info(EELFLoggerDelegator.applicationLogger, "getCompositeSolutions {}", result);
		} catch (AcumosException e) {
			logger.error(EELFLoggerDelegator.errorLogger, "Exception in getCompositeSolutions" , e);
		}
	}

	/**
	 * This method is used to set default values for the instance of
	 * NexusArtifactClient by passing RepositoryLocation object which will
	 * accept nexus url, username and password.
	 * 
	 * @return reference of NexusArtifactClient
	 */
	private NexusArtifactClient getNexusClient() {
		try {
			RepositoryLocation repositoryLocation = new RepositoryLocation();
			repositoryLocation.setId("1");
			repositoryLocation.setUrl(CONFIG.getProperty("nexus.nexusendpointurlTest"));
			repositoryLocation.setUsername(CONFIG.getProperty("nexus.nexususernameTest"));
			repositoryLocation.setPassword(CONFIG.getProperty("nexus.nexuspasswordTest"));
			nexusArtifactClient = new NexusArtifactClient(repositoryLocation);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger,"exception in getNexusClient", e);
		}
		return nexusArtifactClient;
	}
	
	@Test
	/**
	 * The test case is used to update the composite solution and store it in
	 * nexus repository as well as the database. The test case uses
	 * updatePublicCompositeSolution method which consumes DSCompositeSolution object
	 * and returns solutionId and version or error message in string format
	 * Check for Same Solution Name and same version
	 * @throws JSONException
	 */
	public void updatePublicCompositeSolutionSameName() throws JSONException {

		CompositeSolutionServiceImpl compositeServiceImpl = new CompositeSolutionServiceImpl();
		compositeServiceImpl.getRestCCDSClient((CommonDataServiceRestClientImpl) cmnDataService);
		compositeServiceImpl.getNexusClient(nexusArtifactClient, confprops, properties);
		SolutionServiceImpl solutionServiceImpl = new SolutionServiceImpl();
		solutionServiceImpl.getRestCCDSClient((CommonDataServiceRestClientImpl) cmnDataService);
		solutionServiceImpl.getNexusClient(nexusArtifactClient, confprops, properties);

		DSCompositeSolution dscs = new DSCompositeSolution();

		//dscs.setcId(sessionId);
		dscs.setAuthor(userId);
		dscs.setSolutionName(RandomStringUtils.randomAlphanumeric(5) + "-Test");
		dscs.setSolutionName("Test");
		dscs.setSolutionId("12345");
		dscs.setVersion("1.0.0");
		dscs.setOnBoarder(userId);
		dscs.setDescription("Testing Save Function");
		dscs.setProvider(properties.getProvider());
		dscs.setToolKit(properties.getToolKit());
		dscs.setVisibilityLevel("PB");
		dscs.setIgnoreLesserVersionConflictFlag(false);
		
		ArrayList<MLPSolution> mlpSols = new ArrayList<MLPSolution>();
		MLPSolution mlpSolution = new MLPSolution();
		mlpSolution.setSolutionId("12345");
		mlpSolution.setName("Test");
		mlpSolution.setDescription(dscs.getDescription());
		mlpSolution.setOwnerId(dscs.getAuthor());
		mlpSolution.setValidationStatusCode(ValidationStatusCode.IP.toString());
		mlpSolution.setProvider(dscs.getProvider());
		mlpSolution.setAccessTypeCode(AccessTypeCode.PB.toString());
		mlpSolution.setModelTypeCode(ModelTypeCode.PR.toString());
		mlpSolution.setToolkitTypeCode("CP");
		
		MLPSolutionRevision mlpSolutionRevision = new MLPSolutionRevision();
		mlpSolutionRevision.setSolutionId(mlpSolution.getSolutionId());
		mlpSolutionRevision.setDescription(dscs.getDescription()); 
		mlpSolutionRevision.setOwnerId(dscs.getAuthor());
		mlpSolutionRevision.setVersion("1.0.0");
		
		
		List<MLPSolutionRevision> mlpSolutionRevisionList = new ArrayList<MLPSolutionRevision>();
		mlpSolutionRevisionList.add(mlpSolutionRevision);
		mlpSols.add(mlpSolution);
		
		MLPSolution mlpSolNew = new MLPSolution();
		mlpSolNew.setName("sample1");
		mlpSolNew.setDescription(dscs.getDescription());
		mlpSolNew.setOwnerId(dscs.getAuthor());
		mlpSolNew.setValidationStatusCode(ValidationStatusCode.IP.toString());
		mlpSolNew.setProvider(dscs.getProvider());
		mlpSolNew.setAccessTypeCode(AccessTypeCode.PR.toString());
		mlpSolNew.setModelTypeCode(ModelTypeCode.PR.toString());
		mlpSolNew.setToolkitTypeCode("CP");
		
		MLPSolution mlpSolNew1 = new MLPSolution();
		mlpSolNew1.setSolutionId("545545a-e674-46af-a4ad-d6514f41de9b");
		mlpSolNew1.setName("sample1");
		mlpSolNew1.setDescription(dscs.getDescription());
		mlpSolNew1.setOwnerId(dscs.getAuthor());
		mlpSolNew1.setValidationStatusCode(ValidationStatusCode.IP.toString());
		mlpSolNew1.setProvider(dscs.getProvider());
		mlpSolNew1.setAccessTypeCode(AccessTypeCode.PR.toString());
		mlpSolNew1.setModelTypeCode(ModelTypeCode.PR.toString());
		mlpSolNew1.setToolkitTypeCode("CP");
		
		String result = null;

		try {
			
			when(cmnDataService.getSolution(dscs.getSolutionId())).thenReturn(mlpSolution);
			when(cmnDataService.getSolutionRevisions(mlpSolution.getSolutionId())).thenReturn(mlpSolutionRevisionList);
			when(cmnDataService.createSolution(mlpSolNew)).thenReturn(mlpSolNew1);
			when(cmnDataService.createSolutionRevision(mlpSolutionRevision)).thenReturn(mlpSolutionRevision);
			when(confprops.getToscaOutputFolder()).thenReturn(localpath);
			
			result = compositeServiceImpl.updateCompositeSolution(dscs); 
			logger.debug(EELFLoggerDelegator.debugLogger, "Result of Save Composite Solution :  {} ", result);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, "Exception in saveCompositeSolution" , e);
		}
		
	}
	
	@Test
	/**
	 * The test case is used to update the composite solution and store it in
	 * nexus repository as well as the database. The test case uses
	 * updatePublicCompositeSolution method which consumes DSCompositeSolution object
	 * and returns solutionId and version or error message in string format
	 *  Check for Different Solution Name
	 * @throws JSONException
	 */
	public void updatePublicCompositeSolutionDiffName() throws JSONException {

		CompositeSolutionServiceImpl compositeServiceImpl = new CompositeSolutionServiceImpl();
		compositeServiceImpl.getRestCCDSClient((CommonDataServiceRestClientImpl) cmnDataService);
		compositeServiceImpl.getNexusClient(nexusArtifactClient, confprops, properties);
		SolutionServiceImpl solutionServiceImpl = new SolutionServiceImpl();
		solutionServiceImpl.getRestCCDSClient((CommonDataServiceRestClientImpl) cmnDataService);
		solutionServiceImpl.getNexusClient(nexusArtifactClient, confprops, properties);

		DSCompositeSolution dscs = new DSCompositeSolution();

		//dscs.setcId(sessionId);
		dscs.setAuthor(userId);
		dscs.setSolutionName(RandomStringUtils.randomAlphanumeric(5) + "-Test");
		dscs.setSolutionName("Test");
		dscs.setSolutionId("12345");
		dscs.setVersion("1.0.0");
		dscs.setOnBoarder(userId);
		dscs.setDescription("Testing Save Function");
		dscs.setProvider(properties.getProvider());
		dscs.setToolKit(properties.getToolKit());
		dscs.setVisibilityLevel("PB");
		dscs.setIgnoreLesserVersionConflictFlag(false);
		
		ArrayList<MLPSolution> mlpSols = new ArrayList<MLPSolution>();
		MLPSolution mlpSolution = new MLPSolution();
		mlpSolution.setSolutionId("12345");
		mlpSolution.setName("Test2");
		mlpSolution.setDescription(dscs.getDescription());
		mlpSolution.setOwnerId(dscs.getAuthor());
		mlpSolution.setValidationStatusCode(ValidationStatusCode.IP.toString());
		mlpSolution.setProvider(dscs.getProvider());
		mlpSolution.setAccessTypeCode(AccessTypeCode.PB.toString());
		mlpSolution.setModelTypeCode(ModelTypeCode.PR.toString());
		mlpSolution.setToolkitTypeCode("CP");
		
		MLPSolutionRevision mlpSolutionRevision = new MLPSolutionRevision();
		mlpSolutionRevision.setSolutionId(mlpSolution.getSolutionId());
		mlpSolutionRevision.setDescription(dscs.getDescription()); 
		mlpSolutionRevision.setOwnerId(dscs.getAuthor());
		mlpSolutionRevision.setVersion("1.0.0");
		
		
		List<MLPSolutionRevision> mlpSolutionRevisionList = new ArrayList<MLPSolutionRevision>();
		mlpSolutionRevisionList.add(mlpSolutionRevision);
		mlpSols.add(mlpSolution);
		
		MLPSolution mlpSolNew = new MLPSolution();
		mlpSolNew.setName("sample1");
		mlpSolNew.setDescription(dscs.getDescription());
		mlpSolNew.setOwnerId(dscs.getAuthor());
		mlpSolNew.setValidationStatusCode(ValidationStatusCode.IP.toString());
		mlpSolNew.setProvider(dscs.getProvider());
		mlpSolNew.setAccessTypeCode(AccessTypeCode.PR.toString());
		mlpSolNew.setModelTypeCode(ModelTypeCode.PR.toString());
		mlpSolNew.setToolkitTypeCode("CP");
		
		MLPSolution mlpSolNew1 = new MLPSolution();
		mlpSolNew1.setSolutionId("545545a-e674-46af-a4ad-d6514f41de9b");
		mlpSolNew1.setName("sample1");
		mlpSolNew1.setDescription(dscs.getDescription());
		mlpSolNew1.setOwnerId(dscs.getAuthor());
		mlpSolNew1.setValidationStatusCode(ValidationStatusCode.IP.toString());
		mlpSolNew1.setProvider(dscs.getProvider());
		mlpSolNew1.setAccessTypeCode(AccessTypeCode.PR.toString());
		mlpSolNew1.setModelTypeCode(ModelTypeCode.PR.toString());
		mlpSolNew1.setToolkitTypeCode("CP");
		
		String result = null;

		try {
			
			when(cmnDataService.getSolution(dscs.getSolutionId())).thenReturn(mlpSolution);
			when(cmnDataService.getSolutionRevisions(mlpSolution.getSolutionId())).thenReturn(mlpSolutionRevisionList);
			when(cmnDataService.createSolution(mlpSolNew)).thenReturn(mlpSolNew1);
			when(cmnDataService.createSolutionRevision(mlpSolutionRevision)).thenReturn(mlpSolutionRevision);
			when(confprops.getToscaOutputFolder()).thenReturn(localpath);
			
			result = compositeServiceImpl.updateCompositeSolution(dscs); 
			logger.debug(EELFLoggerDelegator.debugLogger, "Result of Save Composite Solution :  {} ", result);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, "Exception in saveCompositeSolution" , e);
		}
		
	}
	
	@Test
	/**
	 * The test case is used to update the composite solution and store it in
	 * nexus repository as well as the database. The test case uses
	 * updatePublicCompositeSolution method which consumes DSCompositeSolution object
	 * and returns solutionId and version or error message in string format
	 *  Check for Different Solution Name
	 * @throws JSONException
	 */
	public void updatePublicCompositeSolutionSameNameDiffVersion() throws JSONException {

		CompositeSolutionServiceImpl compositeServiceImpl = new CompositeSolutionServiceImpl();
		compositeServiceImpl.getRestCCDSClient((CommonDataServiceRestClientImpl) cmnDataService);
		compositeServiceImpl.getNexusClient(nexusArtifactClient, confprops, properties);
		SolutionServiceImpl solutionServiceImpl = new SolutionServiceImpl();
		solutionServiceImpl.getRestCCDSClient((CommonDataServiceRestClientImpl) cmnDataService);
		solutionServiceImpl.getNexusClient(nexusArtifactClient, confprops, properties);

		DSCompositeSolution dscs = new DSCompositeSolution();

		//dscs.setcId(sessionId);
		dscs.setAuthor(userId);
		dscs.setSolutionName(RandomStringUtils.randomAlphanumeric(5) + "-Test");
		dscs.setSolutionName("Test");
		dscs.setSolutionId("12345");
		dscs.setVersion("1.0.2");
		dscs.setOnBoarder(userId);
		dscs.setDescription("Testing Save Function");
		dscs.setProvider(properties.getProvider());
		dscs.setToolKit(properties.getToolKit());
		dscs.setVisibilityLevel("PB");
		dscs.setIgnoreLesserVersionConflictFlag(false);
		
		ArrayList<MLPSolution> mlpSols = new ArrayList<MLPSolution>();
		MLPSolution mlpSolution = new MLPSolution();
		mlpSolution.setSolutionId("12345");
		mlpSolution.setName("Test");
		mlpSolution.setDescription(dscs.getDescription());
		mlpSolution.setOwnerId(dscs.getAuthor());
		mlpSolution.setValidationStatusCode(ValidationStatusCode.IP.toString());
		mlpSolution.setProvider(dscs.getProvider());
		mlpSolution.setAccessTypeCode(AccessTypeCode.PB.toString());
		mlpSolution.setModelTypeCode(ModelTypeCode.PR.toString());
		mlpSolution.setToolkitTypeCode("CP");
		
		MLPSolutionRevision mlpSolutionRevision = new MLPSolutionRevision();
		mlpSolutionRevision.setSolutionId(mlpSolution.getSolutionId());
		mlpSolutionRevision.setDescription(dscs.getDescription()); 
		mlpSolutionRevision.setOwnerId(dscs.getAuthor());
		mlpSolutionRevision.setVersion("1.0.0");
		
		
		List<MLPSolutionRevision> mlpSolutionRevisionList = new ArrayList<MLPSolutionRevision>();
		mlpSolutionRevisionList.add(mlpSolutionRevision);
		mlpSols.add(mlpSolution);
		
		MLPSolution mlpSolNew = new MLPSolution();
		mlpSolNew.setName("sample1");
		mlpSolNew.setDescription(dscs.getDescription());
		mlpSolNew.setOwnerId(dscs.getAuthor());
		mlpSolNew.setValidationStatusCode(ValidationStatusCode.IP.toString());
		mlpSolNew.setProvider(dscs.getProvider());
		mlpSolNew.setAccessTypeCode(AccessTypeCode.PR.toString());
		mlpSolNew.setModelTypeCode(ModelTypeCode.PR.toString());
		mlpSolNew.setToolkitTypeCode("CP");
		
		MLPSolution mlpSolNew1 = new MLPSolution();
		mlpSolNew1.setSolutionId("545545a-e674-46af-a4ad-d6514f41de9b");
		mlpSolNew1.setName("sample1");
		mlpSolNew1.setDescription(dscs.getDescription());
		mlpSolNew1.setOwnerId(dscs.getAuthor());
		mlpSolNew1.setValidationStatusCode(ValidationStatusCode.IP.toString());
		mlpSolNew1.setProvider(dscs.getProvider());
		mlpSolNew1.setAccessTypeCode(AccessTypeCode.PR.toString());
		mlpSolNew1.setModelTypeCode(ModelTypeCode.PR.toString());
		mlpSolNew1.setToolkitTypeCode("CP");
		
		String result = null;

		try {
			
			when(cmnDataService.getSolution(dscs.getSolutionId())).thenReturn(mlpSolution);
			when(cmnDataService.getSolutionRevisions(mlpSolution.getSolutionId())).thenReturn(mlpSolutionRevisionList);
			when(cmnDataService.createSolution(mlpSolNew)).thenReturn(mlpSolNew1);
			when(cmnDataService.createSolutionRevision(mlpSolutionRevision)).thenReturn(mlpSolutionRevision);
			when(confprops.getToscaOutputFolder()).thenReturn(localpath);
			
			result = compositeServiceImpl.updateCompositeSolution(dscs); 
			logger.debug(EELFLoggerDelegator.debugLogger, "Result of Save Composite Solution :  {} ", result);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, "Exception in saveCompositeSolution" , e);
		}
		
	}
	
	@Test
	/**
	 * The test case is used to save the composite solution and store it in
	 * nexus repository as well as the database. The test case uses
	 * saveCompositeSolution method which consumes DSCompositeSolution object
	 * and returns solutionId and version or error message in string format
	 * 
	 * @throws JSONException
	 */
	public void updatePrivateCompositeSolutionSameName() throws JSONException {

		CompositeSolutionServiceImpl compositeServiceImpl = new CompositeSolutionServiceImpl();
		compositeServiceImpl.getRestCCDSClient((CommonDataServiceRestClientImpl) cmnDataService);
		compositeServiceImpl.getNexusClient(nexusArtifactClient, confprops, properties);
		SolutionServiceImpl solutionServiceImpl = new SolutionServiceImpl();
		solutionServiceImpl.getRestCCDSClient((CommonDataServiceRestClientImpl) cmnDataService);
		solutionServiceImpl.getNexusClient(nexusArtifactClient, confprops, properties);

		DSCompositeSolution dscs = new DSCompositeSolution();

		//dscs.setcId(sessionId);
		dscs.setAuthor(userId);
		dscs.setSolutionName(RandomStringUtils.randomAlphanumeric(5) + "-Test");
		dscs.setSolutionName("Test");
		dscs.setSolutionId("1234");
		dscs.setVersion("1.0.0");
		dscs.setOnBoarder(userId);
		dscs.setDescription("Testing Save Function");
		dscs.setProvider(properties.getProvider());
		dscs.setToolKit(properties.getToolKit());
		dscs.setVisibilityLevel(properties.getVisibilityLevel());
		dscs.setIgnoreLesserVersionConflictFlag(false);
		
		ArrayList<MLPSolution> mlpSols = new ArrayList<MLPSolution>();
		MLPSolution mlpSolution = new MLPSolution();
		mlpSolution.setSolutionId("1234");
		mlpSolution.setName("Test");
		mlpSolution.setDescription(dscs.getDescription());
		mlpSolution.setOwnerId(dscs.getAuthor());
		mlpSolution.setValidationStatusCode(ValidationStatusCode.IP.toString());
		mlpSolution.setProvider(dscs.getProvider());
		mlpSolution.setAccessTypeCode(AccessTypeCode.PR.toString());
		mlpSolution.setModelTypeCode(ModelTypeCode.PR.toString());
		mlpSolution.setToolkitTypeCode("CP");
		
		MLPSolutionRevision mlpSolutionRevision = new MLPSolutionRevision();
		mlpSolutionRevision.setSolutionId(mlpSolution.getSolutionId());
		mlpSolutionRevision.setDescription(dscs.getDescription()); 
		mlpSolutionRevision.setOwnerId(dscs.getAuthor());
		mlpSolutionRevision.setVersion(dscs.getVersion());
		
		
		List<MLPSolutionRevision> mlpSolutionRevisionList = new ArrayList<MLPSolutionRevision>();
		mlpSolutionRevisionList.add(mlpSolutionRevision);
		mlpSols.add(mlpSolution);
		
		MLPSolution mlpSolNew = new MLPSolution();
		mlpSolNew.setName("sample1");
		mlpSolNew.setDescription(dscs.getDescription());
		mlpSolNew.setOwnerId(dscs.getAuthor());
		mlpSolNew.setValidationStatusCode(ValidationStatusCode.IP.toString());
		mlpSolNew.setProvider(dscs.getProvider());
		mlpSolNew.setAccessTypeCode(AccessTypeCode.PR.toString());
		mlpSolNew.setModelTypeCode(ModelTypeCode.PR.toString());
		mlpSolNew.setToolkitTypeCode("CP");
		
		MLPSolution mlpSolNew1 = new MLPSolution();
		mlpSolNew1.setSolutionId("545545a-e674-46af-a4ad-d6514f41de9b");
		mlpSolNew1.setName("sample1");
		mlpSolNew1.setDescription(dscs.getDescription());
		mlpSolNew1.setOwnerId(dscs.getAuthor());
		mlpSolNew1.setValidationStatusCode(ValidationStatusCode.IP.toString());
		mlpSolNew1.setProvider(dscs.getProvider());
		mlpSolNew1.setAccessTypeCode(AccessTypeCode.PR.toString());
		mlpSolNew1.setModelTypeCode(ModelTypeCode.PR.toString());
		mlpSolNew1.setToolkitTypeCode("CP");
		
		String result = null;

		try {
			
			when(cmnDataService.getSolution(dscs.getSolutionId())).thenReturn(mlpSolution);
			when(cmnDataService.getSolutionRevisions(mlpSolution.getSolutionId())).thenReturn(mlpSolutionRevisionList);
			when(cmnDataService.createSolution(mlpSolNew)).thenReturn(mlpSolNew1);
			when(cmnDataService.createSolutionRevision(mlpSolutionRevision)).thenReturn(mlpSolutionRevision);
			when(confprops.getToscaOutputFolder()).thenReturn(localpath);
			
			result = compositeServiceImpl.updateCompositeSolution(dscs); 
			logger.debug(EELFLoggerDelegator.debugLogger, "Result of Save Composite Solution :  {} ", result);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, "Exception in saveCompositeSolution" , e);
		}
		
	}
	
	@Test
	/**
	 * The test case is used to save the composite solution and store it in
	 * nexus repository as well as the database. The test case uses
	 * saveCompositeSolution method which consumes DSCompositeSolution object
	 * and returns solutionId and version or error message in string format
	 * 
	 * @throws JSONException
	 */
	public void updatePrivateCompositeSolutionDiffName() throws JSONException {

		CompositeSolutionServiceImpl compositeServiceImpl = new CompositeSolutionServiceImpl();
		compositeServiceImpl.getRestCCDSClient((CommonDataServiceRestClientImpl) cmnDataService);
		compositeServiceImpl.getNexusClient(nexusArtifactClient, confprops, properties);
		SolutionServiceImpl solutionServiceImpl = new SolutionServiceImpl();
		solutionServiceImpl.getRestCCDSClient((CommonDataServiceRestClientImpl) cmnDataService);
		solutionServiceImpl.getNexusClient(nexusArtifactClient, confprops, properties);

		DSCompositeSolution dscs = new DSCompositeSolution();

		//dscs.setcId(sessionId);
		dscs.setSolutionId("33egfed24242");
		dscs.setAuthor(userId);
		dscs.setSolutionName(RandomStringUtils.randomAlphanumeric(5) + "-Test");
		dscs.setSolutionName("Test");
		dscs.setSolutionId(null);
		dscs.setVersion("1.0.0");
		dscs.setOnBoarder(userId);
		dscs.setDescription("Testing Save Function");
		dscs.setProvider(properties.getProvider());
		dscs.setToolKit(properties.getToolKit());
		dscs.setVisibilityLevel(properties.getVisibilityLevel());
		dscs.setIgnoreLesserVersionConflictFlag(false);
		
		ArrayList<MLPSolution> mlpSols = new ArrayList<MLPSolution>();
		MLPSolution mlpSolution = new MLPSolution();
		mlpSolution.setSolutionId("1234");
		mlpSolution.setName("Test2");
		mlpSolution.setDescription(dscs.getDescription());
		mlpSolution.setOwnerId(dscs.getAuthor());
		mlpSolution.setValidationStatusCode(ValidationStatusCode.IP.toString());
		mlpSolution.setProvider(dscs.getProvider());
		mlpSolution.setAccessTypeCode(AccessTypeCode.PR.toString());
		mlpSolution.setModelTypeCode(ModelTypeCode.PR.toString());
		mlpSolution.setToolkitTypeCode("CP");
		
		MLPSolutionRevision mlpSolutionRevision = new MLPSolutionRevision();
		mlpSolutionRevision.setSolutionId(dscs.getSolutionId());
		mlpSolutionRevision.setDescription(dscs.getDescription()); 
		mlpSolutionRevision.setOwnerId(dscs.getAuthor());
		mlpSolutionRevision.setVersion(dscs.getVersion());
		
		
		List<MLPSolutionRevision> mlpSolutionRevisionList = new ArrayList<MLPSolutionRevision>();
		mlpSolutionRevisionList.add(mlpSolutionRevision);
		mlpSols.add(mlpSolution);
		
		MLPSolution mlpSolNew = new MLPSolution();
		mlpSolNew.setName("sample1");
		mlpSolNew.setDescription(dscs.getDescription());
		mlpSolNew.setOwnerId(dscs.getAuthor());
		mlpSolNew.setValidationStatusCode(ValidationStatusCode.IP.toString());
		mlpSolNew.setProvider(dscs.getProvider());
		mlpSolNew.setAccessTypeCode(AccessTypeCode.PR.toString());
		mlpSolNew.setModelTypeCode(ModelTypeCode.PR.toString());
		mlpSolNew.setToolkitTypeCode("CP");
		
		MLPSolution mlpSolNew1 = new MLPSolution();
		mlpSolNew1.setSolutionId("545545a-e674-46af-a4ad-d6514f41de9b");
		mlpSolNew1.setName("sample1");
		mlpSolNew1.setDescription(dscs.getDescription());
		mlpSolNew1.setOwnerId(dscs.getAuthor());
		mlpSolNew1.setValidationStatusCode(ValidationStatusCode.IP.toString());
		mlpSolNew1.setProvider(dscs.getProvider());
		mlpSolNew1.setAccessTypeCode(AccessTypeCode.PR.toString());
		mlpSolNew1.setModelTypeCode(ModelTypeCode.PR.toString());
		mlpSolNew1.setToolkitTypeCode("CP");
		
		String result = null;
		try {
			
			when(cmnDataService.getSolution(dscs.getSolutionId())).thenReturn(mlpSolution);		
			when(cmnDataService.createSolution(mlpSolNew)).thenReturn(mlpSolNew1);
			when(cmnDataService.createSolutionRevision(mlpSolutionRevision)).thenReturn(mlpSolutionRevision);
			when(confprops.getToscaOutputFolder()).thenReturn(localpath);
			when(cmnDataService.getSolutionRevisions(dscs.getSolutionId())).thenReturn(mlpSolutionRevisionList);
			
			result = compositeServiceImpl.updateCompositeSolution(dscs); 
			logger.debug(EELFLoggerDelegator.debugLogger, "Result of Save Composite Solution :  {} ", result);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, "Exception in saveCompositeSolution" , e);
		}
		
	}
	
	@Test
	/**
	 * The test case is used to save the composite solution and store it in
	 * nexus repository as well as the database. The test case uses
	 * saveCompositeSolution method which consumes DSCompositeSolution object
	 * and returns solutionId and version or error message in string format
	 * 
	 * @throws JSONException
	 */
	public void updatePrivateCompositeSolutionDiffVersion() throws JSONException {

		CompositeSolutionServiceImpl compositeServiceImpl = new CompositeSolutionServiceImpl();
		compositeServiceImpl.getRestCCDSClient((CommonDataServiceRestClientImpl) cmnDataService);
		compositeServiceImpl.getNexusClient(nexusArtifactClient, confprops, properties);
		SolutionServiceImpl solutionServiceImpl = new SolutionServiceImpl();
		solutionServiceImpl.getRestCCDSClient((CommonDataServiceRestClientImpl) cmnDataService);
		solutionServiceImpl.getNexusClient(nexusArtifactClient, confprops, properties);

		DSCompositeSolution dscs = new DSCompositeSolution();

		//dscs.setcId(sessionId);
		dscs.setSolutionId("33egfed24242");
		dscs.setAuthor(userId);
		dscs.setSolutionName(RandomStringUtils.randomAlphanumeric(5) + "-Test");
		dscs.setSolutionName("Test");
		dscs.setSolutionId(null);
		dscs.setVersion("1.0.0");
		dscs.setOnBoarder(userId);
		dscs.setDescription("Testing Save Function");
		dscs.setProvider(properties.getProvider());
		dscs.setToolKit(properties.getToolKit());
		dscs.setVisibilityLevel(properties.getVisibilityLevel());
		dscs.setIgnoreLesserVersionConflictFlag(false);
		
		ArrayList<MLPSolution> mlpSols = new ArrayList<MLPSolution>();
		MLPSolution mlpSolution = new MLPSolution();
		mlpSolution.setSolutionId("1234");
		mlpSolution.setName("Test");
		mlpSolution.setDescription(dscs.getDescription());
		mlpSolution.setOwnerId(dscs.getAuthor());
		mlpSolution.setValidationStatusCode(ValidationStatusCode.IP.toString());
		mlpSolution.setProvider(dscs.getProvider());
		mlpSolution.setAccessTypeCode(AccessTypeCode.PR.toString());
		mlpSolution.setModelTypeCode(ModelTypeCode.PR.toString());
		mlpSolution.setToolkitTypeCode("CP");
		
		MLPSolutionRevision mlpSolutionRevision = new MLPSolutionRevision();
		mlpSolutionRevision.setSolutionId(dscs.getSolutionId());
		mlpSolutionRevision.setDescription(dscs.getDescription()); 
		mlpSolutionRevision.setOwnerId(dscs.getAuthor());
		mlpSolutionRevision.setVersion("1.0.2");
		
		
		List<MLPSolutionRevision> mlpSolutionRevisionList = new ArrayList<MLPSolutionRevision>();
		mlpSolutionRevisionList.add(mlpSolutionRevision);
		mlpSols.add(mlpSolution);
		
		MLPSolution mlpSolNew = new MLPSolution();
		mlpSolNew.setName("sample1");
		mlpSolNew.setDescription(dscs.getDescription());
		mlpSolNew.setOwnerId(dscs.getAuthor());
		mlpSolNew.setValidationStatusCode(ValidationStatusCode.IP.toString());
		mlpSolNew.setProvider(dscs.getProvider());
		mlpSolNew.setAccessTypeCode(AccessTypeCode.PR.toString());
		mlpSolNew.setModelTypeCode(ModelTypeCode.PR.toString());
		mlpSolNew.setToolkitTypeCode("CP");
		
		MLPSolution mlpSolNew1 = new MLPSolution();
		mlpSolNew1.setSolutionId("545545a-e674-46af-a4ad-d6514f41de9b");
		mlpSolNew1.setName("sample1");
		mlpSolNew1.setDescription(dscs.getDescription());
		mlpSolNew1.setOwnerId(dscs.getAuthor());
		mlpSolNew1.setValidationStatusCode(ValidationStatusCode.IP.toString());
		mlpSolNew1.setProvider(dscs.getProvider());
		mlpSolNew1.setAccessTypeCode(AccessTypeCode.PR.toString());
		mlpSolNew1.setModelTypeCode(ModelTypeCode.PR.toString());
		mlpSolNew1.setToolkitTypeCode("CP");
		
		String result = null;
		try {
			
			when(cmnDataService.getSolution(dscs.getSolutionId())).thenReturn(mlpSolution);		
			when(cmnDataService.createSolution(mlpSolNew)).thenReturn(mlpSolNew1);
			when(cmnDataService.createSolutionRevision(mlpSolutionRevision)).thenReturn(mlpSolutionRevision);
			when(confprops.getToscaOutputFolder()).thenReturn(localpath);
			when(cmnDataService.getSolutionRevisions(dscs.getSolutionId())).thenReturn(mlpSolutionRevisionList);
			
			result = compositeServiceImpl.updateCompositeSolution(dscs); 
			logger.debug(EELFLoggerDelegator.debugLogger, "Result of Save Composite Solution :  {} ", result);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, "Exception in saveCompositeSolution" , e);
		}
		
	}
	
	@Test
	/**
	 * The test case is used to Fetch Json Tosca file and return the file
	 */
	public void fetchJsonTOSCA()  {
		
		String solutionID = "710d881b-e926-4412-831c-10b0bf04c354yyy";
		String version = "1.0.0";
		List<MLPSolutionRevision> mlpSolRevisions = new ArrayList<MLPSolutionRevision>();
		MLPSolutionRevision mlpSolutionRevision = new MLPSolutionRevision();
		mlpSolutionRevision.setSolutionId(solutionID);
		mlpSolutionRevision.setDescription("Testing Save Function");
		mlpSolutionRevision.setOwnerId(userId);
		mlpSolutionRevision.setVersion(version);
		mlpSolutionRevision.setRevisionId("111");
		
		List<MLPArtifact> artifacts = new ArrayList<MLPArtifact>();
		MLPArtifact artifact = new MLPArtifact();
		artifact.setArtifactTypeCode("DI");
		artifact.setUri("xyz");
		artifacts.add(artifact);
		mlpSolRevisions.add(mlpSolutionRevision);
		String result = null;
		try {
			
			when(cmnDataService.getSolutionRevisions(solutionID)).thenReturn(mlpSolRevisions);
			when(cmnDataService.getSolutionRevisionArtifacts("222", null)).thenReturn(artifacts);
			result = acumosCatalogServiceImpl.fetchJsonTOSCA(solutionID, version);
			assertNotNull(result);
			when(props.getArtifactType()).thenReturn("DI");
			when(cmnDataService.getSolutionRevisionArtifacts(solutionID, mlpSolutionRevision.getRevisionId())).thenReturn(artifacts);
			String artifactResult = acumosCatalogServiceImpl.readArtifact(userId, solutionID, version, props.getArtifactType().trim());
			assertNotNull(artifactResult);
			logger.debug(EELFLoggerDelegator.debugLogger, "Result of Save Composite Solution :  {} ", result);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, "Exception in saveCompositeSolution" , e);
		}
		
	}
	
	@Test
	/**
	 * The test case is used to Create & Deploy Data bRoker
	 */
	public void createDeployDataBroker() throws ServiceException{

		String solutionID = "710d881b-e926-4412-831c-10b0bf04c354yyy";
		String version = "1.0.0";
		Cdump cdump = new Cdump();
		cdump.setCid(sessionId);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		cdump.setCtime(sdf.format(new Date()));
		cdump.setProbeIndicator("false");
		String artifactResult = dataBrokerServiceImpl.createDeployDataBroker(cdump,"Node8", userId);
	}
	
	public Nodes getNode(){
		Nodes node = new Nodes();
		node.setName("Node9");
		node.setNodeId("54321");
		node.setNodeSolutionId("333");
		node.setNodeVersion("1.0.0");
		node.setTypeInfo(null);
		node.setProperties(null);
		node.setProtoUri("com/org/xyz");
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
		Type type = new Type();
		type.setName("xyz1");
		node.setType(type);
		
		Property property = new Property();
		DataBrokerMap databrokerMap = new DataBrokerMap();
		databrokerMap.setScript("this is the script");

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
		property.setData_broker_map(databrokerMap);
		Property[] properties = new Property[1];
		properties[0] = property;
		node.setProperties(properties);
		return node;
	}
	

}

