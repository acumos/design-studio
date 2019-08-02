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

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.acumos.designstudio.ce.util.ConfigurationProperties;
import org.acumos.designstudio.ce.vo.DSSolution;
import org.acumos.designstudio.ce.vo.MatchingModel;
import org.acumos.designstudio.ce.vo.SuccessErrorMessage;
import org.acumos.designstudio.ce.vo.blueprint.BPCollatorMap;
import org.acumos.designstudio.ce.vo.blueprint.BPDataBrokerMap;
import org.acumos.designstudio.ce.vo.blueprint.BPSplitterMap;
import org.acumos.designstudio.ce.vo.blueprint.BaseOperationSignature;
import org.acumos.designstudio.ce.vo.blueprint.BluePrint;
import org.acumos.designstudio.ce.vo.blueprint.Container;
import org.acumos.designstudio.ce.vo.blueprint.DataSource;
import org.acumos.designstudio.ce.vo.blueprint.Node;
import org.acumos.designstudio.ce.vo.blueprint.NodeOperationSignature;
import org.acumos.designstudio.ce.vo.blueprint.OperationSignatureList;
import org.acumos.designstudio.ce.vo.blueprint.ProbeIndicator;
import org.acumos.designstudio.ce.vo.cdump.Capability;
import org.acumos.designstudio.ce.vo.cdump.collator.CollatorInputField;
import org.acumos.designstudio.ce.vo.cdump.collator.CollatorMapInput;
import org.acumos.designstudio.ce.vo.cdump.collator.CollatorMapOutput;
import org.acumos.designstudio.ce.vo.cdump.collator.CollatorOutputField;
import org.acumos.designstudio.ce.vo.cdump.databroker.DBInputField;
import org.acumos.designstudio.ce.vo.cdump.databroker.DBMapInput;
import org.acumos.designstudio.ce.vo.cdump.databroker.DBMapOutput;
import org.acumos.designstudio.ce.vo.cdump.databroker.DBOTypeAndRoleHierarchy;
import org.acumos.designstudio.ce.vo.cdump.databroker.DBOutputField;
import org.acumos.designstudio.ce.vo.cdump.splitter.SplitterInputField;
import org.acumos.designstudio.ce.vo.cdump.splitter.SplitterMapInput;
import org.acumos.designstudio.ce.vo.cdump.splitter.SplitterMapOutput;
import org.acumos.designstudio.ce.vo.cdump.splitter.SplitterOutputField;
import org.acumos.designstudio.ce.vo.compositeproto.Protobuf;
import org.acumos.designstudio.ce.vo.compositeproto.ProtobufMessage;
import org.acumos.designstudio.ce.vo.compositeproto.ProtobufMessageField;
import org.acumos.designstudio.ce.vo.compositeproto.ProtobufOption;
import org.acumos.designstudio.ce.vo.compositeproto.ProtobufService;
import org.acumos.designstudio.ce.vo.compositeproto.ProtobufServiceOperation;
import org.acumos.designstudio.ce.vo.tgif.Artifact;
import org.acumos.designstudio.ce.vo.tgif.Auxiliary;
import org.acumos.designstudio.ce.vo.tgif.Call;
import org.acumos.designstudio.ce.vo.tgif.Provide;
import org.acumos.designstudio.ce.vo.tgif.Request;
import org.acumos.designstudio.ce.vo.tgif.Response;
import org.json.simple.JSONArray;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class VOTest  {
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private String nexusURI = "";
	private String payloadURI ="";
	
	public static Properties CONFIG = new Properties();
	@Before
	/**
	 * This method is used to set default values for the instance of
	 * ICommonDataServiceRestClient and NexusArtifactClient by passing common
	 * data service and nexus url, username and password respectively
	 * 
	 * @throws Exception
	 */
	public void createClient() throws Exception {
	    CONFIG.load(GetMatchingModelTest.class.getResourceAsStream("/application.properties"));
	    payloadURI = CONFIG.getProperty("nexus.nexusendpointurlTest");
	    nexusURI = CONFIG.getProperty("nexus.nexusUriTestt");
	}
	
	@Test
	/**
	 * The test case is used to test the representation of TGIF Artifact in the
	 * desired format.
	 * 
	 */
	public void testTgifArtifact(){
		Artifact arti = new Artifact();
		arti.setType("docker image");
		arti.setUri("fake.nexus.org/dcae/kpi_anomaly:1.0.0");
		arti.getType();
		arti.getUri();
		assertNotNull(arti);
		assertEquals("docker image", arti.getType());
		assertEquals("fake.nexus.org/dcae/kpi_anomaly:1.0.0", arti.getUri());
		logger.info(arti.toString());
	}
	
	@Test
	/**
	 * The test case is used to test the representation of VO Artifact in the
	 * desired format.
	 * 
	 */
	public void testVOArtifact(){
		org.acumos.designstudio.ce.vo.Artifact artifact = new org.acumos.designstudio.ce.vo.Artifact();
		artifact.setContentLength(100);
		artifact.setExtension(".any");
		artifact.setName("Nexus Repo");
		artifact.setNexusURI(nexusURI);
		artifact.setPayloadURI(payloadURI);
		artifact.setSolutionID("0016c81e-6fe7-4d93-9763-a68c76c4ce8a");
		artifact.setType("TOSCAPROTOBUF");
		artifact.setVersion("1.0.0");
		artifact.getContentLength();
		artifact.getExtension();
		artifact.getName();
		artifact.getNexusURI();
		artifact.getPayloadURI();
		artifact.getSolutionID();
		artifact.getType();
		artifact.getVersion();
		assertNotNull(artifact);
		assertEquals(".any", artifact.getExtension());
		assertEquals("Nexus Repo", artifact.getName());
		assertEquals("TOSCAPROTOBUF", artifact.getType());
		assertEquals("1.0.0", artifact.getVersion());
		logger.info(artifact.toString());
		
	}
	
	@Test
	/**
	 * The test case is used to test the representation of TGIF Auxiliary in the
	 * desired format.
	 * 
	 */
	public void testTgifAuxiliary(){
		Auxiliary aux = new Auxiliary();
		aux.setTemp("Temp");
		aux.getTemp();
		assertNotNull(aux);
		assertEquals("Temp", aux.getTemp());
		logger.info(aux.toString());
	}
	
	@Test
	/**
	 * The test case is used to test the representation of VO BluePrint in the
	 * desired format.
	 * 
	 */
	public void testVOBluePrint(){
		BluePrint bp = new BluePrint();
		
		bp.setName("BluePrint");
		bp.getName();
		bp.setVersion("1.0.0");
		bp.getVersion();
		
		Node n = new Node();
		List<Node> nodeList = new ArrayList<>();
		
		n.setContainer_name("Blueprint Orchestrator");
		n.getContainer_name();
		n.setImage("BPDI");
		n.getImage();
		n.setNode_type("MLModel");
		n.getNode_type();
		n.setProto_uri("ProtoURI");
		n.getProto_uri();
		
		
		
		BPCollatorMap bpcm = new BPCollatorMap();
		bpcm.setCollator_type("Array-based");
		bpcm.getCollator_type();
		bpcm.setOutput_message_signature("output");
		bpcm.getOutput_message_signature();
		
		CollatorMapInput cmi = new CollatorMapInput();
		CollatorInputField cif = new CollatorInputField();
		cif.setError_indicator("false");
		cif.getError_indicator();
		cif.setMapped_to_field("Yes");
		cif.getMapped_to_field();
		cif.setMessage_signature("signature");
		cif.getMessage_signature();
		cif.setParameter_name("ParamName");
		cif.getParameter_name();
		cif.setParameter_role("Role");
		cif.getParameter_role();
		cif.setParameter_tag("Tag");
		cif.getParameter_tag();
		cif.setParameter_type("Type");
		cif.getParameter_type();
		cif.setSource_name("Source");
		cif.getSource_name();
		cmi.setInput_field(cif);
		cmi.getInput_field();
		List<CollatorMapInput> cmiList = new ArrayList<>();
		cmiList.add(cmi);
		
		CollatorMapInput[] cmiArray = new CollatorMapInput[cmiList.size()];
		cmiArray = cmiList.toArray(cmiArray);
		bpcm.setMap_inputs(cmiArray);
		bpcm.getMap_inputs();
		
		CollatorMapOutput cmo = new CollatorMapOutput();
		CollatorOutputField cof = new CollatorOutputField();
		cof.setParameter_name("Name");
		cof.getParameter_name();
		cof.setParameter_role("Role");
		cof.getParameter_role();
		cof.setParameter_tag("Tag");
		cof.getParameter_tag();
		cof.setParameter_type("Type");
		cof.getParameter_type();
		cmo.setOutput_field(cof);
		cmo.getOutput_field();
		List<CollatorMapOutput> cmoList = new ArrayList<>();
		cmoList.add(cmo);
		
		CollatorMapOutput[] cmoArray = new CollatorMapOutput[cmoList.size()];
		cmoArray = cmoList.toArray(cmoArray);
		bpcm.setMap_outputs(cmoArray);
		bpcm.getMap_outputs();
		n.setCollator_map(bpcm);
		n.getCollator_map();
		
		BPSplitterMap splMap = new BPSplitterMap();
		splMap.setInput_message_signature("input_msg_signature");
		splMap.getInput_message_signature();
		splMap.setSplitter_type("Array-Based");
		splMap.getSplitter_type();

		// SplitterMapInputs
		SplitterMapInput smi = new SplitterMapInput();
		// need to set Input Field
		SplitterInputField sif = new SplitterInputField();
		sif.setParameter_name("PName");
		sif.getParameter_name();
		sif.setParameter_tag("PTag");
		sif.getParameter_tag();
		sif.setParameter_type("PType");
		sif.getParameter_type();
		sif.setParameter_role("PRole");
		sif.getParameter_role();
		smi.setInput_field(sif);
		smi.getInput_field();
		// Take a List of SplitterMapInput to convert it into Array
		List<SplitterMapInput> smiList = new ArrayList<SplitterMapInput>();
		smiList.add(smi);
		SplitterMapInput[] map_inputs = smiList.toArray(new SplitterMapInput[smiList.size()]);

		splMap.setMap_inputs(map_inputs);
		splMap.getMap_inputs();

		// SplitterMapOutput
		SplitterMapOutput smo = new SplitterMapOutput();
		SplitterOutputField sof = new SplitterOutputField();
		sof.setTarget_name("parameter name in Source Protobuf file");
		sof.getTarget_name();
		sof.setParameter_type("name of parameter");
		sof.getParameter_type();
		sof.setParameter_name("parameter name");
		sof.getParameter_name();
		sof.setParameter_tag("tag number");
		sof.getParameter_tag();
		sof.setError_indicator("False");
		sof.getError_indicator();
		sof.setParameter_role("Role");
		sof.getParameter_role();
		sof.setMapped_to_field("tag number of the field");
		sof.getMapped_to_field();
		sof.setMessage_signature("MsgSignature");
		sof.getMessage_signature();
		smo.setOutput_field(sof);
		smo.getOutput_field();
		List<SplitterMapOutput> smoList = new ArrayList<SplitterMapOutput>();
		smoList.add(smo);
		SplitterMapOutput[] map_outputs = smoList.toArray(new SplitterMapOutput[smoList.size()]);
		splMap.setMap_outputs(map_outputs);
		splMap.getMap_outputs();
			
		n.setSplitter_map(splMap);
		n.getSplitter_map();
		
		BPDataBrokerMap bpDBMap = new BPDataBrokerMap();
		bpDBMap.setScript("this is the script");
		bpDBMap.getScript();
		bpDBMap.setCsv_file_field_separator(",");
		bpDBMap.getCsv_file_field_separator();
		bpDBMap.setData_broker_type("SQLDataBroker1");
		bpDBMap.getData_broker_type();
		bpDBMap.setFirst_row("Yes");
		bpDBMap.getFirst_row();
		bpDBMap.setLocal_system_data_file_path("localpath");
		bpDBMap.getLocal_system_data_file_path();
		bpDBMap.setTarget_system_url("remoteurl");
		bpDBMap.getTarget_system_url();
		bpDBMap.setUser_id("UserId");
		bpDBMap.getUser_id();
		bpDBMap.setDatabase_name("FileSystemDataBase");
		bpDBMap.getDatabase_name();
		bpDBMap.setJdbc_driver_data_source_class_name("openjpa.jdbc.Schema");
		bpDBMap.getJdbc_driver_data_source_class_name();
		bpDBMap.setLocal_system_data_file_path("localpath");
		bpDBMap.getLocal_system_data_file_path();
		bpDBMap.setPassword("password");
		bpDBMap.getPassword();
		bpDBMap.setProtobufFile("protobufFile");
		bpDBMap.getProtobufFile();
		bpDBMap.setTable_name("MLPSOLUTION");
		bpDBMap.getTable_name();

		// DataBrokerMap Input List
		List<DBMapInput> dbmapInputLst = new ArrayList<DBMapInput>();
		DBMapInput dbMapInput = new DBMapInput();
		DBInputField dbInField = new DBInputField();

		dbInField.setChecked("Yes");
		dbInField.getChecked();
		dbInField.setMapped_to_field("1.2");
		dbInField.getMapped_to_field();
		dbInField.setName("Name of SourceField");
		dbInField.getName();
		dbInField.setType("Float");
		dbInField.getType();
		dbInField.toString();
		dbMapInput.setInput_field(dbInField);
		dbMapInput.getInput_field();
		dbMapInput.toString();
		dbmapInputLst.add(dbMapInput);
		DBMapInput[] dbMap_Inputs = new DBMapInput[dbmapInputLst.size()];
		dbMap_Inputs = dbmapInputLst.toArray(dbMap_Inputs);

		bpDBMap.setMap_inputs(dbMap_Inputs);
		bpDBMap.getMap_inputs();
		bpDBMap.toString();

		// DataBrokerMap Output List
		List<DBMapOutput> dbmapOutputLst = new ArrayList<DBMapOutput>();
		DBMapOutput dbMapOutput = new DBMapOutput();
		DBOutputField dbOutField = new DBOutputField();

		dbOutField.setName("sepal_len");
		dbOutField.getName();
		dbOutField.setTag("1.3");
		dbOutField.getTag();
		dbOutField.toString();

		List<DBOTypeAndRoleHierarchy> dboList = new ArrayList<DBOTypeAndRoleHierarchy>();
		DBOTypeAndRoleHierarchy dboTypeAndRole = new DBOTypeAndRoleHierarchy();
		DBOTypeAndRoleHierarchy[] dboTypeAndRoleHierarchyArr = null;

		dboTypeAndRole.setName("DataFrameRow");
		dboTypeAndRole.getName();
		dboTypeAndRole.setRole("Repeated");
		dboTypeAndRole.getRole();
		dboTypeAndRole.toString();
		dboList.add(dboTypeAndRole);
		dboTypeAndRoleHierarchyArr = new DBOTypeAndRoleHierarchy[dboList.size()];
		dboTypeAndRoleHierarchyArr = dboList.toArray(dboTypeAndRoleHierarchyArr);

		dbOutField.setType_and_role_hierarchy_list(dboTypeAndRoleHierarchyArr);
		dbOutField.getType_and_role_hierarchy_list();
		dbMapOutput.setOutput_field(dbOutField);
		dbMapOutput.getOutput_field();
		dbMapOutput.toString();
		dbmapOutputLst.add(dbMapOutput);

		DBMapOutput[] dbMapOutputArr = new DBMapOutput[dbmapOutputLst.size()];
		dbMapOutputArr = dbmapOutputLst.toArray(dbMapOutputArr);

		bpDBMap.setMap_outputs(dbMapOutputArr);
		bpDBMap.getMap_outputs();
		bpDBMap.toString();
		n.setData_broker_map(bpDBMap);
		n.getData_broker_map();
		
		
		
		bp.setNodes(nodeList);
		bp.getNodes();
		
	
		Container con = new Container();
		con.setContainer_name("BluePrint");
		con.getContainer_name();
		
		List<Container> containerList = new ArrayList<>();
		containerList.add(con);
		bp.setInput_ports(containerList);
		bp.getInput_ports();
		
	
		
		ProbeIndicator probe = new ProbeIndicator();
		probe.setValue("1");
		probe.getValue();
		List<ProbeIndicator> probeList = new ArrayList<>();
		probeList.add(probe);
		bp.setProbeIndicator(probeList);
		bp.getProbeIndicator();
		
		NodeOperationSignature nos = new NodeOperationSignature();
		nos.setInput_message_name("Input");
		nos.getInput_message_name();
		nos.setOperation_name("Operation");
		nos.getOperation_name();
		nos.setOutput_message_name("output");
		nos.getOutput_message_name();
		
		BaseOperationSignature bos = new BaseOperationSignature();
		bos.setOperation_name("transform");
		bos.getOperation_name();
		
		con.setOperation_signature(bos);
		con.getOperation_signature();
		
		DataSource ds = new DataSource();
		ds.setName("DataSource");
		ds.getName();
		ds.setOperation_signature(bos);
		ds.getOperation_signature();
		List<DataSource> dataSourceList = new ArrayList<>();
		dataSourceList.add(ds);
		
		List<Container> donList = new ArrayList<>();
		donList.add(con);
		
		OperationSignatureList osl = new OperationSignatureList();
		osl.setConnected_to(containerList);
		osl.getConnected_to();
		osl.setOperation_signature(nos);
		osl.getOperation_signature();
		List<OperationSignatureList> oslList = new ArrayList<>();
		oslList.add(osl);
		n.setOperation_signature_list(oslList);
		n.getOperation_signature_list();
		
	
		n.setData_sources(dataSourceList);
		n.getData_sources();
		
		nodeList.add(n);
		assertNotNull(bp);
		assertEquals("BluePrint", bp.getName());

		assertNotNull(n);
		assertEquals("BPDI", n.getImage());

		assertNotNull(con);
	}
	

	@Test
	/**
	 * The test case is used to test the representation of TGIF Call in the
	 * desired format.
	 * 
	 */
	public void testTgifCall(){
		Call call = new Call();
		call.setConfig_key("transform");
		call.getConfig_key();
		
		Request req = new Request();
		JSONArray reqFormat = new JSONArray();
		
		req.setFormat(reqFormat);
		req.getFormat();
		
		call.setRequest(req);
		call.getRequest();
		
		Response res = new Response();
		
		JSONArray formt = new JSONArray();
		res.setFormat(formt);
		res.getFormat();
		res.setVersion("1.0.0");
		res.getVersion();
		
		call.setResponse(res);
		call.getResponse();
		assertNotNull(call);
		assertEquals("transform", call.getConfig_key());

		assertNotNull(res);
		assertEquals("1.0.0", res.getVersion());
		logger.info(call.toString());
	}
	
	@Test
	/**
	 * The test case is used to test the representation of TGIF Parameter in the
	 * desired format.
	 * 
	 */
	public void testTgifParameter(){
		String s1 = "module.arg.model";
		String s2 = "TGIF";
		String s3 = "data/102915pySGDonelinec2_0_ffq_2.mod";
		org.acumos.designstudio.ce.vo.tgif.Parameter param = new org.acumos.designstudio.ce.vo.tgif.Parameter();
		param.setDescription(s1);
		param.setName(s2);
		param.setValue(s3);
		Assert.assertEquals(s1, param.getDescription());
		Assert.assertEquals(s2, param.getName());
		Assert.assertEquals(s3, param.getValue());
		logger.info(param.toString());
	}
	
	@Test
	/**
	 * The test case is used to test the representation of TGIF Provide in the
	 * desired format.
	 * 
	 */
	public void testTgifProvide(){
		Provide provide = new Provide();
		
		Request re = new Request();
		JSONArray reqFormat = new JSONArray();
		re.setFormat(reqFormat);
		re.getFormat();
		provide.setRequest(re);
		
		Response res = new Response();
		JSONArray formt = new JSONArray();
		res.setFormat(formt);
		res.getFormat();
		res.setVersion("1.0.0");
		res.getVersion();
		provide.setResponse(res);
		provide.setRoute("IGNORE");
		assertNotNull(re);

		assertNotNull(res);
		assertEquals("1.0.0", res.getVersion());

		assertNotNull(provide);
		assertEquals("IGNORE", provide.getRoute());
		logger.info(provide.toString());
		
	}
	
	@Test
	/**
	 * The test case is used to test the representation of Cdump Capability in the
	 * desired format.
	 * 
	 */
	public void testCdumpCapability(){
		String s1 = "123456";
		String s2 = "ReqCapability";
		Capability cap = new Capability();
		cap.setId(s1);
		cap.setName(s2);
		Assert.assertEquals(s1, cap.getId());
		Assert.assertEquals(s2, cap.getName());
		logger.info(cap.toString());
	}
	
	@Test
	/**
	 * The test case is used to test the representation of DSSolution in the
	 * desired format.
	 * 
	 */
	public void testDSSolution(){
		String s1 = "1";
		String s2 = "1";
		String s3 = "1";
		String s4 = "1";
		String s5 = "1";
		String s6 = "1";
		String s7 = "1";
		String s8 = "1";
		String s9 = "1";
		String s10 = "1";
		String s11 = "1";
		String s12 = "1";
		String s13 = "1";
		String s14 = "1";
		boolean s15 = false;
		DSSolution ds = new DSSolution(s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12, s13, s14, s15);
		logger.info(ds.toString());
		DSSolution dss = new DSSolution();
		dss.setAuthor(s1);
		Assert.assertEquals(s1, dss.getAuthor());
		dss.setCategory(s2);
		Assert.assertEquals(s2, dss.getCategory());
		dss.setCreatedDate(s3);
		Assert.assertEquals(s3, dss.getCreatedDate());
		dss.setDescription(s4);
		Assert.assertEquals(s4, dss.getDescription());
		dss.setIcon(s5);
		Assert.assertEquals(s5,dss.getIcon());
		dss.setModifiedDate(s6);
		Assert.assertEquals(s6,dss.getModifiedDate());
		dss.setOnBoarder(s7);
		Assert.assertEquals(s7,dss.getOnBoarder());
		dss.setProvider(s8);
		Assert.assertEquals(s8,dss.getProvider());
		dss.setSolutionId(s9);
		Assert.assertEquals(s9,dss.getSolutionId());
		dss.setSolutionName(s10);
		Assert.assertEquals(s10,dss.getSolutionName());
		dss.setSolutionRevisionId(s11);
		Assert.assertEquals(s11,dss.getSolutionRevisionId());
		dss.setToolKit(s12);
		Assert.assertEquals(s12,dss.getToolKit());
		dss.setVersion(s13);
		Assert.assertEquals(s13,dss.getVersion());
		dss.setVisibilityLevel(s14);
		Assert.assertEquals(s14,dss.getVisibilityLevel());
		logger.info(dss.toString());
	}
	
	@Test
	/**
	 * The test case is used to set the Util Configuration Properties in the
	 * ConfigurationProperties class
	 * 
	 */
	public void testUtilConfigurationProperties(){
		ConfigurationProperties confProps = new ConfigurationProperties();
		confProps.getCmndatasvcendpoinurl();
		confProps.getCmndatasvcpwd();
		confProps.getCmndatasvcuser();
		confProps.getDateFormat();
		confProps.getNexusendpointurl();
		confProps.getNexusgroupid();
		confProps.getNexuspassword();
		confProps.getNexususername();
		confProps.getToscaOutputFolder();
		assertNotNull(confProps);
		logger.info(confProps.toString());
	}
	
	@Test
	/**
	 * Test for matching model name
	 * 
	 */
	public void matchingModelTest(){
		MatchingModel matchingModel = new MatchingModel();
		matchingModel.getMatchingModelName();
		matchingModel.setMatchingModelName("Test");
		matchingModel.getTgifFileNexusURI();
		matchingModel.setTgifFileNexusURI("https://localhost:8082/repo/test");
		
		assertNotNull(matchingModel);
	}
	
	@Test
	/**
	 * Test for SuccessErrroMessgae
	 * 
	 */
	public void successErrroMessgaeTest(){
		SuccessErrorMessage successErrorMessage = new SuccessErrorMessage("200","400");
		successErrorMessage.getSuccess();
		successErrorMessage.setSuccess("success");
		successErrorMessage.getErrorMessage();
		successErrorMessage.setErrorMessage("400");
		
		assertNotNull(successErrorMessage);
	}
	
	@Test
	public void protobufTest(){
		Protobuf proto = new Protobuf();
		ProtobufMessage pMsg = new ProtobufMessage();
		pMsg.setName("RegionDetectionSet");
		pMsg.getName();
		ProtobufMessageField pmf = new ProtobufMessageField();
		pmf.setName("RegionDetections");
		pmf.getName();
		pmf.setRole("repeated");
		pmf.getRole();
		pmf.setTag(1);
		pmf.getTag();
		pmf.setType("RegionDetection");
		pmf.getType();
		List<ProtobufMessageField> pmfList = new ArrayList<>();
		pmfList.add(pmf);
		pMsg.setFields(pmfList);
		pMsg.getFields();
		
		List<ProtobufMessage> pmList = new ArrayList<>();
		pmList.add(pMsg);
		proto.setMessages(pmList);
		proto.getMessages();
		
		ProtobufOption pOption = new ProtobufOption();
		pOption.setName("RegionDetections");
		pOption.getName();
		pOption.setValue("1");
		pOption.getValue();
		List<ProtobufOption> poList = new ArrayList<>();
		poList.add(pOption);
		proto.setOptions(poList);
		proto.getOptions();
		
		proto.setPackageName("UbENTnRTKDmvMUwJokNTcYjStUylzrBO");
		proto.getPackageName();
		
		ProtobufService service = new ProtobufService();
		service.setName("RegionDetectionData");
		service.getName();
		
		ProtobufServiceOperation pso = new ProtobufServiceOperation();
		pso.setName("xfer_face_data");
		pso.getName();
		pso.setType("rpc");
		pso.getType();
		List<String> outputList = new ArrayList<String>();
		outputList.add("RegionDetectionSet");
		pso.setOutputMessageNames(outputList);
		pso.getOutputMessageNames();
		List<String> inputList = new ArrayList<>();
		inputList.add("RegionDetectionData");
		pso.setInputMessageNames(inputList);
		pso.getInputMessageNames();
		List<ProtobufServiceOperation> psoList = new ArrayList<>();
		psoList.add(pso);
		service.setOperations(psoList);
		service.getOperations();
		proto.setService(service);
		proto.getService();
		proto.setSyntax("proto3");
		proto.getSyntax();
		proto.getMessage(".RegionDetectionSet");
		proto.toString();
	}
	
}
