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
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.acumos.cds.client.CommonDataServiceRestClientImpl;
import org.acumos.designstudio.ce.controller.SolutionController;
import org.acumos.designstudio.ce.exceptionhandler.ServiceException;
import org.acumos.designstudio.ce.service.ICompositeSolutionService;
import org.acumos.designstudio.ce.service.SolutionServiceImpl;
import org.acumos.designstudio.ce.util.EELFLoggerDelegator;
import org.acumos.designstudio.ce.vo.DSCompositeSolution;
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
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import com.jayway.jsonpath.InvalidJsonException;

public class ControllersTest {
	private static EELFLoggerDelegator logger = EELFLoggerDelegator.getLogger(ControllersTest.class);
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
		try {
			when(solutionService.createNewCompositeSolution(userId)).thenReturn(
					"{\"cid\":\"5e12e047-08b6-4c6e-aa13-e5bf4f1ea4b1\",\"success\":\"true\",\"errorMessage\":\"\"}");
			String results = solutionController.createNewCompositeSolution(userId);
			assertNotNull(results);
			if (results.contains("true")) {
				logger.debug(EELFLoggerDelegator.debugLogger, results);
			} else {
				throw new ServiceException("Not created", "4xx", "Unable to create composite solution");
			}
		} catch (ServiceException e) {
			logger.error(EELFLoggerDelegator.errorLogger, "CDUMP file not created", e);
			throw e;
		}
	}

	@Test
	/**
	 * The test case is used to add node(a model)to create composite solution.
	 * The test case uses addNode method which consumes userId, solutionId,
	 * version, cid, node and returns the node data which is stored in CDUMP.json.
	 * 
	 * @throws Exception
	 */
	public void addNode() throws Exception {
		try {
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

			assertNotNull(data);
			assertNotNull(node);
			assertNotNull(msg);
			assertEquals("200", data.getNtype());
			assertEquals("1", node.getNodeId());
			assertEquals("DataFrame", msg.getMessageName());

			when(solutionService.addNode(userId, null, null, sessionId, node))
					.thenReturn("{\"success\" : \"true\", \"errorDescription\" : \"\"}");
			String results = solutionController.addNode(userId, null, null, sessionId, node);
			logger.debug(EELFLoggerDelegator.debugLogger, results);
		} catch (InvalidJsonException e) {
			logger.error(EELFLoggerDelegator.errorLogger, "Exception in addNode() testcase: JSON schema not valid", e);
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
	 * @throws Exception
	 */
	public void addLink() throws Exception {
		try {
			Property property = new Property();
			when(solutionService.addLink(userId, null, null, "Model to Model", "101", "Model 1", "1", "Model 2", "2",
					"sourceNodeRequirement", "targetNodeCapabilityName", sessionId, null)).thenReturn(true);
			String results = solutionController.addLink(userId, null, null, "Model to Model", "101", "Model 1", "1",
					"Model 2", "2", "sourceNodeRequirement", "targetNodeCapabilityName", sessionId, property);
			assertNotNull(results);
			logger.debug(EELFLoggerDelegator.debugLogger, results);
			if (results.contains("true")) {
				logger.debug(EELFLoggerDelegator.debugLogger, results);
			} else {
				throw new FileNotFoundException();
			}
		} catch (FileNotFoundException e) {
			logger.error(EELFLoggerDelegator.errorLogger, "Exception in addLink() testcase: CDUMP file not found", e);
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
	 * @throws Exception
	 */
	public void addLink1() throws Exception {
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

			assertNotNull(property);
			assertNotNull(data_map);
			assertNotNull(map_inputsObj);
			assertNotNull(input_fieldsobj);
			assertTrue(map_inputs.length == 1);
			assertEquals("1", input_fieldsobj.getTag());
			assertEquals("Prediction", map_inputsObj.getMessage_name());

			when(solutionService.addLink(userId, null, null, "Model to DM", "201", "Model 1", "1", "DM", "3",
					"sourceNodeRequirement", "targetNodeCapabilityName", sessionId, null)).thenReturn(false);
			String results = solutionController.addLink(userId, null, null, "Model to Model", "101", "Model 1", "1",
					"Model 2", "2", "sourceNodeRequirement", "targetNodeCapabilityName", sessionId, property);
			assertNotNull(results);
			if (results.contains("false")) {
				logger.debug(EELFLoggerDelegator.debugLogger, results);
			} else {
				throw new FileNotFoundException();
			}
		} catch (FileNotFoundException e) {
			logger.error(EELFLoggerDelegator.errorLogger, "Exception in addLink1() testcase: Cdump file not found", e);
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
	public void deleteNode() throws Exception {
		try {
			when(solutionService.deleteNode(userId, null, null, sessionId, "1")).thenReturn(true);
			String results = solutionController.deleteNode(userId, null, null, sessionId, "1");
			assertNotNull(results);
			logger.debug(EELFLoggerDelegator.debugLogger, results);
			if (results.contains("true")) {
				logger.debug(EELFLoggerDelegator.debugLogger, results);
			} else {
				throw new FileNotFoundException();
			}
		} catch (FileNotFoundException e) {
			logger.error(EELFLoggerDelegator.errorLogger, "Exception in deleteNode() testcase: CDUMP file not found",
					e);
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
	public void deleteNode1() throws Exception {
		try {
			when(solutionService.deleteNode(userId, null, null, sessionId, "1")).thenReturn(false);
			String results = solutionController.deleteNode(userId, null, null, sessionId, "1");
			assertNotNull(results);
			logger.debug(EELFLoggerDelegator.debugLogger, results);
			if (results.contains("false")) {
				logger.debug(EELFLoggerDelegator.debugLogger, results);
			} else {
				throw new FileNotFoundException();
			}
		} catch (FileNotFoundException e) {
			logger.error(EELFLoggerDelegator.errorLogger, "Exception in deleteNode1() testcase: CDUMP file not found",
					e);
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
	public void deleteLink() throws Exception {
		try {
			when(solutionService.deleteLink(userId, null, null, sessionId, "101")).thenReturn(true);
			String results = solutionController.deleteLink(userId, null, null, sessionId, "101");
			assertNotNull(results);
			if (results.contains("true")) {
				logger.debug(EELFLoggerDelegator.debugLogger, results);
			} else {
				throw new FileNotFoundException();
			}
		} catch (FileNotFoundException e) {
			logger.error(EELFLoggerDelegator.errorLogger, "Exception in deleteLink() testcase: CDUMP file not found",
					e);
		}
	}

	@Test
	/**
	 * The test case is used to clear the composite solution.
	 * 
	 * @throws Exception
	 */
	public void clearCompositeSolution() throws Exception {
		try {
			when(compositeServiceImpl.clearCompositeSolution(userId, null, "1.0.0", sessionId))
					.thenReturn("Grapg cleared");
			String results = solutionController.clearCompositeSolution(userId, null, "1.0.0", sessionId);
			assertNotNull(results);
			if (results.contains("cleared")) {
				logger.debug(EELFLoggerDelegator.debugLogger, results);
			} else {
				throw new FileNotFoundException();
			}
		} catch (FileNotFoundException e) {
			logger.error(EELFLoggerDelegator.errorLogger,
					"Exception in clearCompositeSolution() testcase: CDUMP file not found", e);
		}
	}

	@Test
	/**
	 * The test case is used to close the composite solution.
	 * 
	 * @throws Exception
	 */
	public void closeCompositeSolution() throws Exception {
		try {
			when(compositeServiceImpl.closeCompositeSolution(userId, null, "1.0.0", sessionId))
					.thenReturn("Graph closed");
			String results = solutionController.closeCompositeSolution(userId, null, "1.0.0", sessionId);
			assertNotNull(results);
			if (results.contains("closed")) {
				logger.debug(EELFLoggerDelegator.debugLogger, results);
			} else {
				throw new FileNotFoundException();
			}
		} catch (FileNotFoundException e) {
			logger.error(EELFLoggerDelegator.errorLogger,
					"Exception in closeCompositeSolution() testcase: CDUMP file not found", e);
		}
	}

	@Test
	/**
	 * The test case is used to delete the composite solution from nexus
	 * repository as well as the database
	 * 
	 * @throws Exception
	 */
	public void deleteCompositeColution() throws Exception {
		try {
			when(compositeServiceImpl.deleteCompositeSolution(userId, sessionId, "1.0.0")).thenReturn(true);
			String results = solutionController.deleteCompositeSolution(userId, sessionId, "1.0.0");
			assertNotNull(results);
			logger.debug(EELFLoggerDelegator.debugLogger, results);
		} catch (ServiceException e) {
			logger.error(EELFLoggerDelegator.errorLogger,
					"Exception in deleteCompositeColution() testcase: Not deleted", e);
			throw e;
		}
	}

	@Test
	/**
	 * The test case is used to delete the composite solution from nexus
	 * repository as well as the database
	 * 
	 * @throws Exception
	 */
	public void deleteCompositeColution1() throws Exception {
		try {
			when(compositeServiceImpl.deleteCompositeSolution(userId, sessionId, "1.0.0")).thenReturn(false);
			String results = solutionController.deleteCompositeSolution(userId, sessionId, "1.0.0");
			assertNotNull(results);
			if (results.contains("false")) {
				logger.debug(EELFLoggerDelegator.debugLogger, results);
			} else {
				throw new ServiceException("Not deleted", "4xx", "Exception : Unable to delete requested Solution");
			}
		} catch (ServiceException e) {
			logger.error(EELFLoggerDelegator.errorLogger, "Unable to delete requested Solution", e);
			throw e;
		}
	}

	@Test
	/**
	 * The test case is used to get the list of public or private or
	 * organization or all composite solutions accessible by user
	 * 
	 * @throws Exception
	 */
	public void getCompositeSolutions() throws Exception {
		try {
			when(compositeServiceImpl.getCompositeSolutions(userId, "PV")).thenReturn("10 Composite solution found");
			String results = solutionController.getCompositeSolutions(userId, "OR");
			assertNotNull(results);
			if (results.contains("Composite solution")) {
				logger.debug(EELFLoggerDelegator.debugLogger, results);
			} else {
				throw new ServiceException("Empty List returned", "4xx", "No composite solutions found");
			}
		} catch (ServiceException e) {
			logger.error(EELFLoggerDelegator.errorLogger, "No composite solutions found", e);
		}
	}

	@Test
	/**
	 * The test case is used to get the list of all the solutions that belongs
	 * to the user from the database
	 * 
	 * @throws Exception
	 */
	public void getSolutions() throws Exception {
		try {
			when(solutionService.getSolutions(userId)).thenReturn(
					"{\"items\" : [{\"solutionId\":\"1c1d1316-6884-4574-a8fc-749de037b965\", \"solutionName\":\"AlarmGenerator\", \"version\":\"1\", \"ownerId\":\"testdev testdev\", \"provider\":\"null\", \"toolKit\":\"H2\", \"category\":\"null\", \"description\":\"AlarmGenerator\", \"visibilityLevel\":\"PB\", \"created\":\"2017-11-15-17-34-57-000\", \"icon\":\"null\"},{\"solutionId\":\"264aca36-4fa8-4885-bcd3-7661bb9de54b\", \"solutionName\":\"Predictor-DM\", \"version\":\"1\", \"ownerId\":\"testdev testdev\", \"provider\":\"null\", \"toolKit\":\"H2\", \"category\":\"PR\", \"description\":\"Predictor-DM\", \"visibilityLevel\":\"PB\", \"created\":\"2017-11-16-16-09-22-000\", \"icon\":\"null\"},{\"solutionId\":\"4f151b2d-52a9-4cbd-b80c-131d52fbaab9\", \"solutionName\":\"Classifier\", \"version\":\"1\", \"ownerId\":\"testdev testdev\", \"provider\":\"null\", \"toolKit\":\"H2\", \"category\":\"CL\", \"description\":\"Classifier\", \"visibilityLevel\":\"PB\", \"created\":\"2017-11-16-15-52-07-000\", \"icon\":\"null\"},{\"solutionId\":\"51120ed4-73de-4d1b-b34c-e86cd6d30da7\", \"solutionName\":\"Classifier-R\", \"version\":\"1\", \"ownerId\":\"testdev testdev\", \"provider\":\"null\", \"toolKit\":\"H2\", \"category\":\"CL\", \"description\":\"Classifier-R\", \"visibilityLevel\":\"PB\", \"created\":\"2017-11-16-15-56-32-000\", \"icon\":\"null\"},{\"solutionId\":\"54edb103-83ec-40ad-a357-a7060a2778cf\", \"solutionName\":\"Aggregator\", \"version\":\"1\", \"ownerId\":\"testdev testdev\", \"provider\":\"null\", \"toolKit\":\"H2\", \"category\":\"DT\", \"description\":\"Aggregator\", \"visibilityLevel\":\"PB\", \"created\":\"2017-11-15-18-33-40-000\", \"icon\":\"null\"},{\"solutionId\":\"7266d859-458d-464b-9d6e-4461f94835dd\", \"solutionName\":\"GenDataMapper\", \"version\":\"1\", \"ownerId\":\"testdev testdev\", \"provider\":\"null\", \"toolKit\":\"H2\", \"category\":\"DT\", \"description\":\"GenDataMapper\", \"visibilityLevel\":\"PB\", \"created\":\"2017-11-16-12-00-33-000\", \"icon\":\"null\"},{\"solutionId\":\"b027f1d8-b61c-4484-bd9a-c81fe40245d1\", \"solutionName\":\"Predictor\", \"version\":\"1\", \"ownerId\":\"testdev testdev\", \"provider\":\"null\", \"toolKit\":\"H2\", \"category\":\"PR\", \"description\":\"Predictor\", \"visibilityLevel\":\"PB\", \"created\":\"2017-11-16-16-07-00-000\", \"icon\":\"null\"},{\"solutionId\":\"b1d60b2a-16dd-4464-bd0e-3692a377a546\", \"solutionName\":\"DataMapper\", \"version\":\"1\", \"ownerId\":\"testdev testdev\", \"provider\":\"null\", \"toolKit\":\"H2\", \"category\":\"DT\", \"description\":\"Cell_Tower-SFO\", \"visibilityLevel\":\"PR\", \"created\":\"2017-11-16-08-36-38-000\", \"icon\":\"null\"},{\"solutionId\":\"cbaf417b-5442-4873-bdec-914b55fe7f9a\", \"solutionName\":\"Classifier-DM\", \"version\":\"1\", \"ownerId\":\"testdev testdev\", \"provider\":\"null\", \"toolKit\":\"H2\", \"category\":\"CL\", \"description\":\"Classifier-DM\", \"visibilityLevel\":\"PB\", \"created\":\"2017-11-16-16-03-30-000\", \"icon\":\"null\"},{\"solutionId\":\"dd01f926-eae2-4d2e-bcfd-80430490c2e2\", \"solutionName\":\"Predictor-R\", \"version\":\"1\", \"ownerId\":\"testdev testdev\", \"provider\":\"null\", \"toolKit\":\"H2\", \"category\":\"PR\", \"description\":\"Predictor-R\", \"visibilityLevel\":\"PB\", \"created\":\"2017-11-16-15-59-58-000\", \"icon\":\"null\"}]}");
			String results = solutionController.getSolutions(userId);
			assertNotNull(results);
			if (results.contains("solutionId")) {
				logger.debug(EELFLoggerDelegator.debugLogger, results);
			} else {
				throw new ServiceException("Empty result", "4xx", "No Models found");
			}
		} catch (ServiceException e) {
			logger.error(EELFLoggerDelegator.errorLogger, "No Models found", e);
			throw e;
		}
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
		try {
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
			cmif.setError_indicator("Yes");
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
			cof.setParameter_rule("ParamRule");
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
			sif.setOther_attributes("For parameter based only");
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
			sof.setOther_attributes("For parameter based only");
			sof.setTarget_name("parameter name in Source Protobuf file");
			smo.setOutput_field(sof);
			List<SplitterMapOutput> smoList = new ArrayList<SplitterMapOutput>();
			smoList.add(smo);
			SplitterMapOutput[] smoArr = smoList.toArray(new SplitterMapOutput[smoList.size()]);
			splitterMap.setMap_outputs(smoArr);	

			assertNotNull(databrokerMap);
			assertNotNull(collatorMap);
			assertNotNull(splitterMap);
			assertNotNull(fieldMap);
			
			assertEquals("ParamName", cof.getParameter_name());
			assertEquals("ParamRule", cof.getParameter_rule());
			
			assertEquals("ParamTag", cof.getParameter_tag());
			assertEquals("ParamType", cof.getParameter_type());
			
			assertEquals("Json Format of input msg Signature", splitterMap.getInput_message_signature());
			assertEquals("Copy-based", splitterMap.getSplitter_type());
			
			assertEquals("For parameter based only", sif.getOther_attributes());
			assertEquals("parameter name in Source Protobuf file", sif.getParameter_name());
			
			assertEquals("For parameter based only", sof.getOther_attributes());
			assertEquals("parameter name in Source Protobuf file", sof.getTarget_name());
			
			assertEquals("Prediction", fieldMap.getInput_field_message_name());

			DataConnector dataConnector = new DataConnector();
			dataConnector.setDatabrokerMap(databrokerMap);;
			dataConnector.setFieldMap(fieldMap);
			dataConnector.setCollatorMap(collatorMap);
			dataConnector.setSplitterMap(splitterMap);
			
			when(solutionService.modifyNode(userId, null, null, sessionId, "1", "New Node", ndata, fieldMap, databrokerMap,collatorMap,splitterMap))
					.thenReturn("Node Modified");
			String results = solutionController.modifyNode(userId, null, null, sessionId, "1", "New Node", ndata,
					dataConnector);
			assertNotNull(results);
			if (results.contains("Modified")) {
				logger.debug(EELFLoggerDelegator.debugLogger, results);
			} else {
				throw new FileNotFoundException();
			}
		} catch (FileNotFoundException e) {
			logger.error(EELFLoggerDelegator.errorLogger, "Exception in modifyNode(): CDUMP file not found", e);
		}
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
		try {
			when(solutionService.modifyLink(userId, sessionId, null, null, "202", "Model to DM"))
					.thenReturn("Link Modified");
			String results = solutionController.modifyLink(userId, sessionId, null, null, "202", "Model to DM");
			assertNotNull(results);
			if (results.contains("Modified")) {
				logger.debug(EELFLoggerDelegator.debugLogger, results);
			} else {
				throw new FileNotFoundException();
			}
		} catch (FileNotFoundException e) {
			logger.error(EELFLoggerDelegator.errorLogger, "Exception in modifyLink: CDUMP file not found", e);
		}
	}

	@Test
	/**
	 * The test case is used to save the composite solution and store it in nexus
	 * repository as well as the database
	 * 
	 * @throws Exception
	 */
	public void saveCompositeSolution() throws Exception {
		DSCompositeSolution dscs = new DSCompositeSolution();
		dscs.setAuthor(userId);
		dscs.setSolutionName("solutionName");
		dscs.setSolutionId("solutionId");
		dscs.setVersion("version");
		dscs.setOnBoarder(userId);
		dscs.setDescription("description"); 
		dscs.setProvider("Test");
		dscs.setToolKit("CP");
		dscs.setVisibilityLevel("PV");
		dscs.setcId(sessionId);
		dscs.setIgnoreLesserVersionConflictFlag(true);
		assertNotNull(dscs);
		assertEquals("solutionName", dscs.getSolutionName());
		when(props.getProvider()).thenReturn("Test");
		when(props.getToolKit()).thenReturn("CP");
		when(props.getVisibilityLevel()).thenReturn("PV");
		when(compositeServiceImpl.saveCompositeSolution(dscs)).thenReturn("Solution saved : " + sessionId);
		HttpServletRequest request = null;
		Object results = solutionController.saveCompositeSolution(request, userId, "Test", "1.0.0", null, "Test",
				sessionId, true);
		assertNotNull(dscs);
		assertEquals("CP", dscs.getToolKit());
		assertEquals("PV", dscs.getVisibilityLevel());
		assertEquals("description", dscs.getDescription());
		logger.debug(EELFLoggerDelegator.debugLogger, "results");
	}
	
}
