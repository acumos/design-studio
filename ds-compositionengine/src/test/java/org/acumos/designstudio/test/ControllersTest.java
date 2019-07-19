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
import static org.mockito.Mockito.when;

import java.lang.invoke.MethodHandles;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acumos.cds.client.CommonDataServiceRestClientImpl;
import org.acumos.designstudio.ce.config.HandlerInterceptorConfiguration;
import org.acumos.designstudio.ce.controller.AdminController;
import org.acumos.designstudio.ce.controller.SolutionController;
import org.acumos.designstudio.ce.exceptionhandler.AcumosException;
import org.acumos.designstudio.ce.service.ICompositeSolutionService;
import org.acumos.designstudio.ce.service.SolutionServiceImpl;
import org.acumos.designstudio.ce.vo.cdump.Argument;
import org.acumos.designstudio.ce.vo.cdump.Capabilities;
import org.acumos.designstudio.ce.vo.cdump.CapabilityTarget;
import org.acumos.designstudio.ce.vo.cdump.DataConnector;
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
import org.acumos.designstudio.ce.vo.cdump.databroker.DataBrokerMap;
import org.acumos.designstudio.ce.vo.cdump.datamapper.DataMap;
import org.acumos.designstudio.ce.vo.cdump.datamapper.DataMapInputField;
import org.acumos.designstudio.ce.vo.cdump.datamapper.FieldMap;
import org.acumos.designstudio.ce.vo.cdump.datamapper.MapInputs;
import org.acumos.designstudio.ce.vo.cdump.datamapper.MapOutput;
import org.acumos.designstudio.ce.vo.cdump.splitter.SplitterInputField;
import org.acumos.designstudio.ce.vo.cdump.splitter.SplitterMap;
import org.acumos.designstudio.ce.vo.cdump.splitter.SplitterMapInput;
import org.acumos.designstudio.ce.vo.cdump.splitter.SplitterMapOutput;
import org.acumos.designstudio.ce.vo.cdump.splitter.SplitterOutputField;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import com.jayway.jsonpath.InvalidJsonException;

public class ControllersTest {
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	// CCDS TechMDev(8003) UserId, change it if the CCDS port changes.
	String userId = "8fcc3384-e3f8-4520-af1c-413d9495a154";
	// The local path folder which is there in local project Directory.
	String localpath = "./src/test/resources/";
	// For meanwhile hard coding the sessionID.
	String sessionId = "4f91545a-e674-46af-a4ad-d6514f41de9b";
	
	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	@InjectMocks
	SolutionController solutionController;
	
	@Mock
	SolutionServiceImpl solutionService;
	
	@Mock
	ICompositeSolutionService compositeServiceImpl;
	
	@Mock
	org.acumos.designstudio.ce.util.Properties props;
	
	@Mock
    CommonDataServiceRestClientImpl cmnDataService;
	
	@Mock
	HandlerInterceptorConfiguration handlerInterceptorConfiguration;
	
	@InjectMocks
	AdminController adminController;
	
	private HttpServletResponse response = new MockHttpServletResponse();
	
	@Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

	@Test
	/**
	 * The test case is used to create a new composite solution. The test case
	 * uses createNewCompositeSolution method which consumes userId and returns
	 * cid(generated as UUID) and creation time in a string format which is then
	 * stored in CDUMP.json.The file is used by ds-composition engine to
	 * represent a composite solution made by connecting models.
	 * 
	 */
	public void createNewCompositeSolution() {
			InterceptorRegistry registry = new InterceptorRegistry();
			Mockito.doNothing().when(handlerInterceptorConfiguration).addInterceptors(registry);
			try {
				when(solutionService.createNewCompositeSolution(userId)).thenReturn(
						"{\"cid\":\"5e12e047-08b6-4c6e-aa13-e5bf4f1ea4b1\",\"success\":\"true\",\"errorMessage\":\"\"}");
			} catch (AcumosException e) {
				logger.error("Exception in createNewCompositeSolution() testcase: ",e);
			}
			String results = solutionController.createNewCompositeSolution(userId);
			assertEquals("{\"cid\":\"5e12e047-08b6-4c6e-aa13-e5bf4f1ea4b1\",\"success\":\"true\",\"errorMessage\":\"\"}", results);
	}


	@Test
	/**
	 * The test case is used to add node(a model)to create composite solution.
	 * The test case uses addNode method which consumes userId, solutionId,
	 * version, cid, node and returns the node data which is stored in CDUMP.json.
	 * 
	 */
	public void addNode() {
		try {
			InterceptorRegistry registry = new InterceptorRegistry();
			Mockito.doNothing().when(handlerInterceptorConfiguration).addInterceptors(registry);
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
			Type type = new Type();
			type.setName("xyz1");
			node.setType(type);
			when(solutionService.addNode(userId, null, null, sessionId, node))
					.thenReturn("{\"success\" : \"true\", \"errorDescription\" : \"\"}");
			String result = solutionController.addNode(userId, null, null, sessionId, node);
			assertEquals("{\"success\" : \"true\", \"errorDescription\" : \"\"}", result);
		} catch (InvalidJsonException e) {
			logger.error("Exception in addNode() testcase: JSON schema not valid", e);
		}
	}

	@Test
	/**
	 * The test case is used to link two nodes to create composite solution. The
	 * test case uses addLink method which consumes userId, solutionId, version,
	 * linkName, linkId, sourceNodeName, sourceNodeId, targetNodeName,
	 * targetNodeId, sourceNodeRequirement, targetNodeCapabilityName, cid,
	 * property and updates the relation between the source and target node
	 * stored in CDUMP.json.
	 * 
	 */
	public void addLink() {
			InterceptorRegistry registry = new InterceptorRegistry();
			Mockito.doNothing().when(handlerInterceptorConfiguration).addInterceptors(registry);
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

			when(solutionService.addLink(userId, null, null, "Model to DM", "201", "Model 1", "1", "DM", "3",
					"sourceNodeRequirement", "targetNodeCapabilityName", sessionId, property)).thenReturn(true);
			String result = solutionController.addLink(userId, null, null, sessionId, "Model to DM", "201", "Model 1", "1", "DM", "3",
					"sourceNodeRequirement", "targetNodeCapabilityName", property);
			assertEquals("{\"success\" : \"true\", \"errorDescription\" : \"\"}", result);
			
			result = solutionController.addLink(userId, null, null, sessionId, "Model to DM", "101", "Model 1", "1", "DM", "3",
					"sourceNodeRequirement", "targetNodeCapabilityName", property);
			assertEquals("{\"success\" : \"false\", \"errorDescription\" : \"Link not added\"}", result);
			
			result = solutionController.addLink(userId, null, null, sessionId, null, null, null, null, null, null,
					null, null, null);
			assertNotNull(result);
			assertEquals("[Source Node name missing , Link missing , target Node name missing , targetNodeCapabilityName mising , sourceNodeId mising ]", result);
	}

	@Test
	/**
	 * The test case is used to delete a node.The test case uses deleteNode
	 * method which consumes userId, solutionId, version, cid, nodeId and
	 * updates CDUMP.json by deleting the node and its relation with other
	 * nodes.
	 * 
	 */
	public void deleteNode() {
			InterceptorRegistry registry = new InterceptorRegistry();
			Mockito.doNothing().when(handlerInterceptorConfiguration).addInterceptors(registry);
			try {
				when(solutionService.deleteNode(userId, null, null, sessionId, "1")).thenReturn(true);
			} catch (AcumosException e) {
				logger.error("Exception in deleteNode() testcase: ",e);
			}
			String result = solutionController.deleteNode(userId, null, null, sessionId, "1");
			assertEquals("{\"success\":\"true\", \"errorMessage\":\"\"}", result);
	}

	@Test
	/**
	 * The test case is used to delete a node.The test case uses deleteNode
	 * method which consumes userId, solutionId, version, cid, nodeId and
	 * updates CDUMP.json by deleting the node and its relation with other
	 * nodes.
	 * 
	 */
	public void deleteNode1() {
			InterceptorRegistry registry = new InterceptorRegistry();
			Mockito.doNothing().when(handlerInterceptorConfiguration).addInterceptors(registry);
			try {
				when(solutionService.deleteNode(userId, null, null, sessionId, "1")).thenReturn(false);
			} catch (AcumosException e) {
				logger.error("Exception in deleteNode1() testcase: ",e);
			}
			String result = solutionController.deleteNode(userId, null, null, sessionId, "1");
			assertEquals("{\"success\":\"false\", \"errorMessage\":\"Invalid Node Id – not found\"}", result);
	}

	@Test
	/**
	 * The test case is used to delete a link between the two nodes.The test
	 * case uses deleteLink method which consumes cid, solutionId, version,
	 * linkId and updates CDUMP.json by deleting relation between the source and
	 * target nodes
	 * 
	 */
	public void deleteLink() {
			InterceptorRegistry registry = new InterceptorRegistry();
			Mockito.doNothing().when(handlerInterceptorConfiguration).addInterceptors(registry);
			when(solutionService.deleteLink(userId, null, null, sessionId, "101")).thenReturn(true);
			//TODO : Include code for deleteLink success
			
			String result = solutionController.deleteLink(userId, null, null, sessionId, "101");
			assertEquals("{\"success\":\"false\", \"errorMessage\":\"Invalid Link Id – not found\"}", result);
	}

	@Test
	/**
	 * The test case is used to clear the composite solution.
	 * 
	 */
	public void clearCompositeSolution() {
			InterceptorRegistry registry = new InterceptorRegistry();
			Mockito.doNothing().when(handlerInterceptorConfiguration).addInterceptors(registry);
			when(compositeServiceImpl.clearCompositeSolution(userId, null, "1.0.0", sessionId))
					.thenReturn("Graph cleared");
			String result = solutionController.clearCompositeSolution(userId, null, "1.0.0", sessionId);
			assertEquals("Graph cleared", result);
	}

	@Test
	/**
	 * The test case is used to close the composite solution.
	 * 
	 */
	public void closeCompositeSolution() throws Exception {
			InterceptorRegistry registry = new InterceptorRegistry();
			Mockito.doNothing().when(handlerInterceptorConfiguration).addInterceptors(registry);
			when(compositeServiceImpl.closeCompositeSolution(userId, null, "1.0.0", sessionId))
					.thenReturn("Graph closed");
			String result = solutionController.closeCompositeSolution(userId, null, "1.0.0", sessionId);
			assertEquals("Graph closed", result);
	}

	@Test
	/**
	 * The test case is used to delete the composite solution from nexus
	 * repository as well as the database
	 * 
	 */
	public void deleteCompositeColution() {
			InterceptorRegistry registry = new InterceptorRegistry();
			Mockito.doNothing().when(handlerInterceptorConfiguration).addInterceptors(registry);
			try {
				when(compositeServiceImpl.deleteCompositeSolution(userId, sessionId, "1.0.0")).thenReturn(true);
			} catch (AcumosException | JSONException e) {
				logger.error("Exception in deleteCompositeColution() testcase: ",e);
			}
			String result = solutionController.deleteCompositeSolution(userId, sessionId, "1.0.0");
			assertEquals("{\"success\":\"true\",\"errorMessage\":\"\"}", result);
			
			
			try {
				when(compositeServiceImpl.deleteCompositeSolution(userId, sessionId, "1.0.0")).thenReturn(false);
			} catch (AcumosException | JSONException e) {
				logger.error("Exception in deleteCompositeColution() testcase: ",e);
			}
			result = solutionController.deleteCompositeSolution(userId, sessionId, "1.0.0");
			assertEquals("{\"success\":\"false\",\"errorMessage\":\"Requested Solution Not Found\"}",result);
	}

	@Test
	/**
	 * The test case is used to get the list of public or private or
	 * organization or all composite solutions accessible by user
	 * 
	 */
	public void getCompositeSolutions() {
			InterceptorRegistry registry = new InterceptorRegistry();
			Mockito.doNothing().when(handlerInterceptorConfiguration).addInterceptors(registry);
			try {
				when(compositeServiceImpl.getCompositeSolutions(userId, "PV")).thenReturn("[dssolution1,dssolution2]");
			} catch (AcumosException e) {
				logger.error("Exception in deleteCompositeColution() testcase: ",e);
			}
			String result = solutionController.getCompositeSolutions(userId, "PV");
			assertEquals("{\"items\" : [dssolution1,dssolution2]}",result);
			
	}

	@Test
	/**
	 * The test case is used to get the list of all the solutions that belongs
	 * to the user from the database
	 * 
	 */
	public void getSolutions() {
			InterceptorRegistry registry = new InterceptorRegistry();
			String results = null;
			Mockito.doNothing().when(handlerInterceptorConfiguration).addInterceptors(registry);
			try {
				when(solutionService.getSolutions(userId)).thenReturn(
						"{\"items\" : [{\"solutionId\":\"1c1d1316-6884-4574-a8fc-749de037b965\", \"solutionName\":\"AlarmGenerator\", \"version\":\"1\", \"ownerId\":\"testdev testdev\", \"provider\":\"null\", \"toolKit\":\"H2\", \"category\":\"null\", \"description\":\"AlarmGenerator\", \"visibilityLevel\":\"PB\", \"created\":\"2017-11-15-17-34-57-000\", \"icon\":\"null\"},{\"solutionId\":\"264aca36-4fa8-4885-bcd3-7661bb9de54b\", \"solutionName\":\"Predictor-DM\", \"version\":\"1\", \"ownerId\":\"testdev testdev\", \"provider\":\"null\", \"toolKit\":\"H2\", \"category\":\"PR\", \"description\":\"Predictor-DM\", \"visibilityLevel\":\"PB\", \"created\":\"2017-11-16-16-09-22-000\", \"icon\":\"null\"},{\"solutionId\":\"4f151b2d-52a9-4cbd-b80c-131d52fbaab9\", \"solutionName\":\"Classifier\", \"version\":\"1\", \"ownerId\":\"testdev testdev\", \"provider\":\"null\", \"toolKit\":\"H2\", \"category\":\"CL\", \"description\":\"Classifier\", \"visibilityLevel\":\"PB\", \"created\":\"2017-11-16-15-52-07-000\", \"icon\":\"null\"},{\"solutionId\":\"51120ed4-73de-4d1b-b34c-e86cd6d30da7\", \"solutionName\":\"Classifier-R\", \"version\":\"1\", \"ownerId\":\"testdev testdev\", \"provider\":\"null\", \"toolKit\":\"H2\", \"category\":\"CL\", \"description\":\"Classifier-R\", \"visibilityLevel\":\"PB\", \"created\":\"2017-11-16-15-56-32-000\", \"icon\":\"null\"},{\"solutionId\":\"54edb103-83ec-40ad-a357-a7060a2778cf\", \"solutionName\":\"Aggregator\", \"version\":\"1\", \"ownerId\":\"testdev testdev\", \"provider\":\"null\", \"toolKit\":\"H2\", \"category\":\"DT\", \"description\":\"Aggregator\", \"visibilityLevel\":\"PB\", \"created\":\"2017-11-15-18-33-40-000\", \"icon\":\"null\"},{\"solutionId\":\"7266d859-458d-464b-9d6e-4461f94835dd\", \"solutionName\":\"GenDataMapper\", \"version\":\"1\", \"ownerId\":\"testdev testdev\", \"provider\":\"null\", \"toolKit\":\"H2\", \"category\":\"DT\", \"description\":\"GenDataMapper\", \"visibilityLevel\":\"PB\", \"created\":\"2017-11-16-12-00-33-000\", \"icon\":\"null\"},{\"solutionId\":\"b027f1d8-b61c-4484-bd9a-c81fe40245d1\", \"solutionName\":\"Predictor\", \"version\":\"1\", \"ownerId\":\"testdev testdev\", \"provider\":\"null\", \"toolKit\":\"H2\", \"category\":\"PR\", \"description\":\"Predictor\", \"visibilityLevel\":\"PB\", \"created\":\"2017-11-16-16-07-00-000\", \"icon\":\"null\"},{\"solutionId\":\"b1d60b2a-16dd-4464-bd0e-3692a377a546\", \"solutionName\":\"DataMapper\", \"version\":\"1\", \"ownerId\":\"testdev testdev\", \"provider\":\"null\", \"toolKit\":\"H2\", \"category\":\"DT\", \"description\":\"Cell_Tower-SFO\", \"visibilityLevel\":\"PR\", \"created\":\"2017-11-16-08-36-38-000\", \"icon\":\"null\"},{\"solutionId\":\"cbaf417b-5442-4873-bdec-914b55fe7f9a\", \"solutionName\":\"Classifier-DM\", \"version\":\"1\", \"ownerId\":\"testdev testdev\", \"provider\":\"null\", \"toolKit\":\"H2\", \"category\":\"CL\", \"description\":\"Classifier-DM\", \"visibilityLevel\":\"PB\", \"created\":\"2017-11-16-16-03-30-000\", \"icon\":\"null\"},{\"solutionId\":\"dd01f926-eae2-4d2e-bcfd-80430490c2e2\", \"solutionName\":\"Predictor-R\", \"version\":\"1\", \"ownerId\":\"testdev testdev\", \"provider\":\"null\", \"toolKit\":\"H2\", \"category\":\"PR\", \"description\":\"Predictor-R\", \"visibilityLevel\":\"PB\", \"created\":\"2017-11-16-15-59-58-000\", \"icon\":\"null\"}]}");
				results = solutionController.getSolutions(userId);
				assertNotNull(results);
			} catch (AcumosException e) {
				logger.error("Exception in deleteCompositeColution() testcase: ",e);
			}
	}

	@Test
	/**
	 * The test case is used to modify node(a model)in a composite solution. The
	 * test case uses modifyNode method which consumes userId, solutionId,
	 * version, cid, nodeId, nodeName, ndata, fieldmap and returns the modified
	 * node data stored in CDUMP.json.
	 * 
	 */
	public void modifyNode() {
			InterceptorRegistry registry = new InterceptorRegistry();
			Mockito.doNothing().when(handlerInterceptorConfiguration).addInterceptors(registry);
			String ndata = "{\"ntype\":\"500\",\"px\":\"500\",\"py\":\"500\",\"radius\":\"500\",\"fixed\":false}";
			FieldMap fieldMap = new FieldMap();
			fieldMap.setInput_field_message_name("Prediction");
			fieldMap.setInput_field_tag_id("1");
			fieldMap.setMap_action("Add");
			fieldMap.setOutput_field_message_name("Classification");
			fieldMap.setOutput_field_tag_id("2");
			DataBrokerMap databrokerMap = new DataBrokerMap();
			databrokerMap.setScript("this is the script");
			
			// CollatorMap
			CollatorMap collatorMap = new CollatorMap();
			collatorMap.setCollator_type("Array-based");
			collatorMap.setOutput_message_signature("Json Format of Output msg Signature");
			
			CollatorMapInput cmi = new CollatorMapInput();
			
			CollatorInputField cmif = new CollatorInputField();
			
			cmif.setMapped_to_field("1.2");
			cmif.setParameter_name("ParamName");
			cmif.setParameter_tag("1");
			cmif.setParameter_type("DataFrame");
			cmif.setSource_name("Aggregator");
			cmif.setError_indicator("False");
			cmi.setInput_field(cmif);
			List<CollatorMapInput> cmiList = new ArrayList<CollatorMapInput>();
			cmiList.add(cmi);
			CollatorMapInput[] cmiArray =cmiList.toArray(new CollatorMapInput[cmiList.size()]);
			//CollatorMapInputs
			collatorMap.setMap_inputs(cmiArray);
			// CollatorMapOutputs
			
			CollatorMapOutput cmo = new CollatorMapOutput();
			CollatorOutputField cof = new CollatorOutputField();
			cof.setParameter_name("ParamName");
			cof.setParameter_tag("ParamTag");
			cof.setParameter_type("ParamType");
			cmo.setOutput_field(cof);
			
			List<CollatorMapOutput> cmoList = new ArrayList<CollatorMapOutput>();
			cmoList.add(cmo);
			CollatorMapOutput[] cmoArray =cmoList.toArray(new CollatorMapOutput[cmoList.size()]);
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
			sif.setParameter_tag("parameter tag");
			sif.setParameter_type("name of parameter");
			smi.setInput_field(sif);
			// Take a List of SplitterMapInput to convert it into Array
			List<SplitterMapInput> smiList = new ArrayList<SplitterMapInput>();
			smiList.add(smi);
			SplitterMapInput[] smiArr = smiList.toArray(new SplitterMapInput[smiList.size()]);
			splitterMap.setMap_inputs(smiArr);
			
			// SplitterMapOutput
			SplitterMapOutput smo = new SplitterMapOutput();
			SplitterOutputField sof = new SplitterOutputField();
			sof.setParameter_name("parameter name");
			sof.setParameter_tag("tag number");
			sof.setParameter_type("name of parameter");
			sof.setTarget_name("parameter name in Source Protobuf file");
			sof.setMapped_to_field("tag number of the field");
			sof.setError_indicator("False");
			smo.setOutput_field(sof);
			List<SplitterMapOutput> smoList = new ArrayList<SplitterMapOutput>();
			smoList.add(smo);
			SplitterMapOutput[] smoArr = smoList.toArray(new SplitterMapOutput[smoList.size()]);
			splitterMap.setMap_outputs(smoArr);	

			DataConnector dataConnector = new DataConnector();
			dataConnector.setDatabrokerMap(databrokerMap);;
			dataConnector.setFieldMap(fieldMap);
			dataConnector.setCollatorMap(collatorMap);
			dataConnector.setSplitterMap(splitterMap);
			
			when(solutionService.modifyNode(userId, null, null, sessionId, "1", "New Node", ndata, fieldMap, databrokerMap,collatorMap,splitterMap))
					.thenReturn("Node Modified");
			String results = solutionController.modifyNode(userId, null, null, sessionId, "1", "New Node", ndata,
					dataConnector);
			assertEquals("Node Modified",results);
			
	}

	@Test
	/**
	 * The test case is used to modify link in a composite solution. The test
	 * case uses modifyLink method which consumes userId, solutionId, version,
	 * linkId, linkName and updates the relation between the source and target
	 * node stored in CDUMP.json.
	 * 
	 */
	public void modifyLink() {
			InterceptorRegistry registry = new InterceptorRegistry();
			Mockito.doNothing().when(handlerInterceptorConfiguration).addInterceptors(registry);
			when(solutionService.modifyLink(userId, sessionId, null, null, "202", "Model to DM"))
					.thenReturn("Link Modified");
			String results = solutionController.modifyLink(userId, sessionId, null, null, "202", "Model to DM");
			assertEquals("Link Modified",results);
	}

	@Test
	/**
	 * The test case is used to save the composite solution and store it in nexus
	 * repository as well as the database
	 * 
	 */
	public void saveCompositeSolution() {
		InterceptorRegistry registry = new InterceptorRegistry();
		Mockito.doNothing().when(handlerInterceptorConfiguration).addInterceptors(registry);
		when(props.getProvider()).thenReturn("Test");
		when(props.getToolKit()).thenReturn("CP");
		when(props.getVisibilityLevel()).thenReturn("PV");
		try {
			when(compositeServiceImpl.saveCompositeSolution(Mockito.any())).thenReturn("Solution saved Successfully");
			HttpServletRequest request = null;
			String result = (String) solutionController.saveCompositeSolution(request, userId, "Test", "1.0.0", null, "Test",
					sessionId, true);
		} catch (AcumosException | JSONException | URISyntaxException e) {
			logger.error("Exception in saveCompositeSolution() testcase: ",e);
		}
	}
	
	@Test
	/**
	 * This test case is used to display the design studio version in the UI
	 * 
	 */
	public void getVersionTest() {
			InterceptorRegistry registry = new InterceptorRegistry();
			Mockito.doNothing().when(handlerInterceptorConfiguration).addInterceptors(registry);
			logger.debug("Calling Controller Method to display version");
			String result = adminController.getVersion(response);
			assertEquals("0.0.0",result);
	}
	
	
	
}
