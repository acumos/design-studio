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

import org.acumos.designstudio.ce.config.HandlerInterceptorConfiguration;
import org.acumos.designstudio.ce.exceptionhandler.ServiceException;
import org.acumos.designstudio.ce.service.GenericDataMapperServiceImpl;
import org.acumos.designstudio.ce.util.Properties;
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
import org.acumos.designstudio.ce.vo.cdump.datamapper.DataMap;
import org.acumos.designstudio.ce.vo.cdump.datamapper.DataMapInputField;
import org.acumos.designstudio.ce.vo.cdump.datamapper.DataMapOutputField;
import org.acumos.designstudio.ce.vo.cdump.datamapper.MapInputs;
import org.acumos.designstudio.ce.vo.cdump.datamapper.MapOutput;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(value = SpringJUnit4ClassRunner.class)
public class GenericDataMapperServiceImplTest {
	
	private static String PROTOBUF_TEMPLATE_NAME = "classpath:Protobuf_Template.txt";
	
	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	@InjectMocks
	private GenericDataMapperServiceImpl gdmServiceImpl;
	
	@Mock
	private Properties props;
	
	@Mock
	private ResourceLoader resourceLoader;
	
	@Mock
	private HandlerInterceptorConfiguration handlerInterceptorConfiguration;
	
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	
	
	@Test
	public void createDeployGDMTest(){
		Cdump cdump = new Cdump();
		Nodes node = getNode();
		List<Nodes> nodesList = new ArrayList<Nodes>();
		nodesList.add(node);
		cdump.setNodes(nodesList);
		String nodeId = "123";
		String userId = "123";
			when(props.getPackagepath()).thenReturn("org/acumos/vo/");
			when(props.getClassName()).thenReturn("DataVO");
			Resource resource1 = resourceLoader.getResource(PROTOBUF_TEMPLATE_NAME) ;
			when(resourceLoader.getResource("classpath:Protobuf_Template.txt")).thenReturn(resource1);
			// TODO : Need to Work on it (Need to solve NPE) WIP
			//gdmServiceImpl.createDeployGDM(cdump, nodeId, userId);
	}
	
	public Nodes getNode(){
		Nodes node = new Nodes();
		node.setName("Node");
		node.setNodeId("123");
		node.setNodeSolutionId("123");
		node.setNodeVersion("1");
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

		DataMap data_map = new DataMap();
		
		MapInputs[] map_inputs = new MapInputs[1];
		MapInputs map_inputsObj = new MapInputs();
		
		DataMapInputField[] input_fields = new DataMapInputField[1];
		DataMapInputField input_fieldsObj = new DataMapInputField();
		input_fieldsObj.setTag("1");
		input_fieldsObj.setType("int32");
		input_fieldsObj.setRole("repeated");
		input_fieldsObj.setName("name");
		input_fieldsObj.setMapped_to_field("yes");
		input_fieldsObj.setMapped_to_message("yes");
		input_fields[0] = input_fieldsObj;
		map_inputsObj.setInput_fields(input_fields);
		map_inputsObj.setMessage_name("Aggregation");
		map_inputs[0] = map_inputsObj;
		
		
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
		Property[] properties = new Property[1];
		properties[0] = property;
		node.setProperties(properties);
		return node;
	}

}
