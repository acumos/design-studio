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

import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.acumos.designstudio.cdump.Argument;
import org.acumos.designstudio.cdump.Capabilities;
import org.acumos.designstudio.cdump.CapabilityTarget;
import org.acumos.designstudio.cdump.DataMap;
import org.acumos.designstudio.cdump.DataMapInputField;
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
import org.acumos.designstudio.ce.controller.SolutionController;
import org.acumos.designstudio.ce.service.ICompositeSolutionService;
import org.acumos.designstudio.ce.service.SolutionServiceImpl;
import org.acumos.designstudio.ce.util.EELFLoggerDelegator;
import org.acumos.designstudio.ce.vo.DSCompositeSolution;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

/**
 * 
 * 
 *
 */
public class ControllersTest {

	private static EELFLoggerDelegator logger = EELFLoggerDelegator.getLogger(SolutionControllerTest.class);

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

	@Test
	/**
	 * 
	 * @throws Exception
	 */
	public void createNewCompositeSolution() throws Exception {
		try {
			when(solutionService.createNewCompositeSolution(userId)).thenReturn(
					"{\"cid\":\"5e12e047-08b6-4c6e-aa13-e5bf4f1ea4b1\",\"success\":\"true\",\"errorMessage\":\"\"}");
			String results = solutionController.createNewCompositeSolution(userId);
			logger.debug(EELFLoggerDelegator.debugLogger, results);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, e.getMessage());
		}
	}

	@Test
	/**
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
			when(solutionService.addNode(userId, null, null, sessionId, node))
					.thenReturn("{\"success\" : \"true\", \"errorDescription\" : \"\"}");
			String results = solutionController.addNode(userId, null, null, sessionId, node);
			logger.debug(EELFLoggerDelegator.debugLogger, results);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, e.getMessage());
		}
	}

	@Test
	/**
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
			logger.debug(EELFLoggerDelegator.debugLogger, results);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, e.getMessage());
		}
	}

	@Test
	/**
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
			when(solutionService.addLink(userId, null, null, "Model to DM", "201", "Model 1", "1", "DM", "3",
					"sourceNodeRequirement", "targetNodeCapabilityName", sessionId, null)).thenReturn(false);
			String results = solutionController.addLink(userId, null, null, "Model to Model", "101", "Model 1", "1",
					"Model 2", "2", "sourceNodeRequirement", "targetNodeCapabilityName", sessionId, property);
			logger.debug(EELFLoggerDelegator.debugLogger, results);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, e.getMessage());
		}
	}

	@Test
	/**
	 * 
	 * @throws Exception
	 */
	public void deleteNode() throws Exception {
		try {
			when(solutionService.deleteNode(userId, null, null, sessionId, "1")).thenReturn(true);
			String results = solutionController.deleteNode(userId, null, null, sessionId, "1");
			logger.debug(EELFLoggerDelegator.debugLogger, results);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, e.getMessage());
		}
	}

	@Test
	/**
	 * 
	 * @throws Exception
	 */
	public void deleteNode1() throws Exception {
		try {
			when(solutionService.deleteNode(userId, null, null, sessionId, "1")).thenReturn(false);
			String results = solutionController.deleteNode(userId, null, null, sessionId, "1");
			logger.debug(EELFLoggerDelegator.debugLogger, results);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, e.getMessage());
		}
	}

	@Test
	/**
	 * 
	 * @throws Exception
	 */
	public void deleteLink() throws Exception {
		try {
			when(solutionService.deleteLink(userId, null, null, sessionId, "101")).thenReturn(true);
			String results = solutionController.deleteLink(userId, null, null, sessionId, "101");
			logger.debug(EELFLoggerDelegator.debugLogger, results);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, e.getMessage());
		}
	}

	@Test
	/**
	 * 
	 * @throws Exception
	 */
	public void clearCompositeSolution() throws Exception {
		try {
			when(compositeServiceImpl.clearCompositeSolution(userId, null, "1.0.0", sessionId))
					.thenReturn("Grapg cleared");
			String results = solutionController.clearCompositeSolution(userId, null, "1.0.0", sessionId);
			logger.debug(EELFLoggerDelegator.debugLogger, results);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, e.getMessage());
		}
	}

	@Test
	/**
	 * 
	 * @throws Exception
	 */
	public void closeCompositeSolution() throws Exception {
		try {
			when(compositeServiceImpl.closeCompositeSolution(userId, null, "1.0.0", sessionId))
					.thenReturn("Graph closed");
			String results = solutionController.closeCompositeSolution(userId, null, "1.0.0", sessionId);
			logger.debug(EELFLoggerDelegator.debugLogger, results);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, e.getMessage());
		}
	}

	@Test
	/**
	 * 
	 * @throws Exception
	 */
	public void deleteCompositeColution() throws Exception {
		try {
			when(compositeServiceImpl.deleteCompositeSolution(userId, sessionId, "1.0.0")).thenReturn(true);
			String results = solutionController.deleteCompositeSolution(userId, sessionId, "1.0.0");
			logger.debug(EELFLoggerDelegator.debugLogger, results);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, e.getMessage());
		}
	}

	@Test
	/**
	 * 
	 * @throws Exception
	 */
	public void deleteCompositeColution1() throws Exception {
		try {
			when(compositeServiceImpl.deleteCompositeSolution(userId, sessionId, "1.0.0")).thenReturn(false);
			String results = solutionController.deleteCompositeSolution(userId, sessionId, "1.0.0");
			logger.debug(EELFLoggerDelegator.debugLogger, results);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, e.getMessage());
		}
	}

	@Test
	/**
	 * 
	 * @throws Exception
	 */
	public void getCompositeSolutions() throws Exception {
		try {
			when(compositeServiceImpl.getCompositeSolutions(userId, "PV")).thenReturn("10 Composite solution found");
			String results = solutionController.getCompositeSolutions(userId, "PV");
			logger.debug(EELFLoggerDelegator.debugLogger, results);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, e.getMessage());
		}
	}

	@Test
	/**
	 * 
	 * @throws Exception
	 */
	public void getSolutions() throws Exception {
		try {
			when(solutionService.getSolutions(userId)).thenReturn(
					"{\"items\" : [{\"solutionId\":\"1c1d1316-6884-4574-a8fc-749de037b965\", \"solutionName\":\"AlarmGenerator\", \"version\":\"1\", \"ownerId\":\"testdev testdev\", \"provider\":\"null\", \"toolKit\":\"H2\", \"category\":\"null\", \"description\":\"AlarmGenerator\", \"visibilityLevel\":\"PB\", \"created\":\"2017-11-15-17-34-57-000\", \"icon\":\"null\"},{\"solutionId\":\"264aca36-4fa8-4885-bcd3-7661bb9de54b\", \"solutionName\":\"Predictor-DM\", \"version\":\"1\", \"ownerId\":\"testdev testdev\", \"provider\":\"null\", \"toolKit\":\"H2\", \"category\":\"PR\", \"description\":\"Predictor-DM\", \"visibilityLevel\":\"PB\", \"created\":\"2017-11-16-16-09-22-000\", \"icon\":\"null\"},{\"solutionId\":\"4f151b2d-52a9-4cbd-b80c-131d52fbaab9\", \"solutionName\":\"Classifier\", \"version\":\"1\", \"ownerId\":\"testdev testdev\", \"provider\":\"null\", \"toolKit\":\"H2\", \"category\":\"CL\", \"description\":\"Classifier\", \"visibilityLevel\":\"PB\", \"created\":\"2017-11-16-15-52-07-000\", \"icon\":\"null\"},{\"solutionId\":\"51120ed4-73de-4d1b-b34c-e86cd6d30da7\", \"solutionName\":\"Classifier-R\", \"version\":\"1\", \"ownerId\":\"testdev testdev\", \"provider\":\"null\", \"toolKit\":\"H2\", \"category\":\"CL\", \"description\":\"Classifier-R\", \"visibilityLevel\":\"PB\", \"created\":\"2017-11-16-15-56-32-000\", \"icon\":\"null\"},{\"solutionId\":\"54edb103-83ec-40ad-a357-a7060a2778cf\", \"solutionName\":\"Aggregator\", \"version\":\"1\", \"ownerId\":\"testdev testdev\", \"provider\":\"null\", \"toolKit\":\"H2\", \"category\":\"DT\", \"description\":\"Aggregator\", \"visibilityLevel\":\"PB\", \"created\":\"2017-11-15-18-33-40-000\", \"icon\":\"null\"},{\"solutionId\":\"7266d859-458d-464b-9d6e-4461f94835dd\", \"solutionName\":\"GenDataMapper\", \"version\":\"1\", \"ownerId\":\"testdev testdev\", \"provider\":\"null\", \"toolKit\":\"H2\", \"category\":\"DT\", \"description\":\"GenDataMapper\", \"visibilityLevel\":\"PB\", \"created\":\"2017-11-16-12-00-33-000\", \"icon\":\"null\"},{\"solutionId\":\"b027f1d8-b61c-4484-bd9a-c81fe40245d1\", \"solutionName\":\"Predictor\", \"version\":\"1\", \"ownerId\":\"testdev testdev\", \"provider\":\"null\", \"toolKit\":\"H2\", \"category\":\"PR\", \"description\":\"Predictor\", \"visibilityLevel\":\"PB\", \"created\":\"2017-11-16-16-07-00-000\", \"icon\":\"null\"},{\"solutionId\":\"b1d60b2a-16dd-4464-bd0e-3692a377a546\", \"solutionName\":\"DataMapper\", \"version\":\"1\", \"ownerId\":\"testdev testdev\", \"provider\":\"null\", \"toolKit\":\"H2\", \"category\":\"DT\", \"description\":\"Cell_Tower-SFO\", \"visibilityLevel\":\"PR\", \"created\":\"2017-11-16-08-36-38-000\", \"icon\":\"null\"},{\"solutionId\":\"cbaf417b-5442-4873-bdec-914b55fe7f9a\", \"solutionName\":\"Classifier-DM\", \"version\":\"1\", \"ownerId\":\"testdev testdev\", \"provider\":\"null\", \"toolKit\":\"H2\", \"category\":\"CL\", \"description\":\"Classifier-DM\", \"visibilityLevel\":\"PB\", \"created\":\"2017-11-16-16-03-30-000\", \"icon\":\"null\"},{\"solutionId\":\"dd01f926-eae2-4d2e-bcfd-80430490c2e2\", \"solutionName\":\"Predictor-R\", \"version\":\"1\", \"ownerId\":\"testdev testdev\", \"provider\":\"null\", \"toolKit\":\"H2\", \"category\":\"PR\", \"description\":\"Predictor-R\", \"visibilityLevel\":\"PB\", \"created\":\"2017-11-16-15-59-58-000\", \"icon\":\"null\"}]}");
			String results = solutionController.getSolutions(userId);
			logger.debug(EELFLoggerDelegator.debugLogger, results);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, e.getMessage());
		}
	}

	@Test
	/**
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
			when(solutionService.modifyNode(userId, null, null, sessionId, "1", "New Node", ndata, fieldMap))
					.thenReturn("Node Modified");
			String results = solutionController.modifyNode(userId, null, null, sessionId, "1", "New Node", ndata,
					fieldMap);
			logger.debug(EELFLoggerDelegator.debugLogger, results);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, e.getMessage());
		}
	}

	@Test
	/**
	 * 
	 * @throws Exception
	 */
	public void modifyLink() throws Exception {
		try {
			when(solutionService.modifyLink(userId, sessionId, null, null, "202", "Model to DM"))
					.thenReturn("Link Modified");
			String results = solutionController.modifyLink(userId, sessionId, null, null, "202", "Model to DM");
			logger.debug(EELFLoggerDelegator.debugLogger, results);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, e.getMessage());
		}
	}

	@Test
	/**
	 * 
	 * @throws Exception
	 */
	public void saveCompositeSolution() throws Exception {
		try {
			DSCompositeSolution dscs = new DSCompositeSolution();
			dscs.setAuthor(userId);
			dscs.setSolutionName("solutionName");
			dscs.setSolutionId("solutionId");
			dscs.setVersion("version");
			dscs.setOnBoarder(userId);
			dscs.setDescription("description");
			dscs.setProvider("TEst");
			dscs.setToolKit("CP");
			dscs.setVisibilityLevel("PV");
			dscs.setcId(sessionId);
			dscs.setIgnoreLesserVersionConflictFlag(true);
			when(props.getProvider()).thenReturn("Test");
			when(props.getToolKit()).thenReturn("CP");
			when(props.getVisibilityLevel()).thenReturn("PV");
			when(compositeServiceImpl.saveCompositeSolution(dscs)).thenReturn("Solution saved : " + sessionId);
			HttpServletRequest request = null;
			@SuppressWarnings("unused")
			Object results = solutionController.saveCompositeSolution(request, userId, "Test", "1.0.0", null, "Test",
					sessionId, true);
			logger.debug(EELFLoggerDelegator.debugLogger,"results");
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, e.getMessage());
		}
	}

}
