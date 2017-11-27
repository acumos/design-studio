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

import java.util.ArrayList;
import java.util.List;

import org.acumos.designstudio.cdump.Capability;
import org.acumos.designstudio.ce.exceptionhandler.DAOException;
import org.acumos.designstudio.ce.util.ConfigurationProperties;
import org.acumos.designstudio.ce.util.EELFLoggerDelegator;
import org.acumos.designstudio.ce.vo.DSSolution;
import org.acumos.designstudio.ce.vo.blueprint.BluePrint;
import org.acumos.designstudio.ce.vo.blueprint.Depends_On;
import org.acumos.designstudio.ce.vo.blueprint.InputOperationSignatures;
import org.acumos.designstudio.ce.vo.blueprint.Node;
import org.acumos.designstudio.ce.vo.blueprint.OperationSignature;
import org.acumos.designstudio.ce.vo.tgif.Artifact;
import org.acumos.designstudio.ce.vo.tgif.Auxiliary;
import org.acumos.designstudio.ce.vo.tgif.Call;
import org.acumos.designstudio.ce.vo.tgif.Provide;
import org.acumos.designstudio.ce.vo.tgif.Request;
import org.acumos.designstudio.ce.vo.tgif.Response;
import org.json.simple.JSONArray;
import org.junit.Assert;
import org.junit.Test;



/**
 * 
 *
 */
public class VOTest  {
	private static final EELFLoggerDelegator logger = EELFLoggerDelegator.getLogger(VOTest.class);
	
	@Test
	public void testTgifArtifact(){
		Artifact arti = new Artifact();
		arti.setType("docker image");
		arti.setUri("fake.nexus.att.com/dcae/kpi_anomaly:1.0.0");
		arti.getType();
		arti.getUri();
		logger.debug(EELFLoggerDelegator.debugLogger, arti.toString());
	}
	
	@Test
	public void testVOArtifact(){
		org.acumos.designstudio.ce.vo.Artifact artifact = new org.acumos.designstudio.ce.vo.Artifact();
		artifact.setContentLength(100);
		artifact.setExtension(".any");
		artifact.setName("Nexus Repo");
		artifact.setNexusURI("http://nexus.acumos.com:8081/#browse/welcome");
		artifact.setPayloadURI("http://nexus.acumos.com:8081/#browse/browse/components:repo_acumos_model_maven");
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
		logger.debug(EELFLoggerDelegator.debugLogger, artifact.toString());
		
	}
	
	@Test
	public void testTgifAuxiliary(){
		Auxiliary aux = new Auxiliary();
		aux.setTemp("Temp");
		aux.getTemp();
		logger.debug(EELFLoggerDelegator.debugLogger, aux.toString());
	}
	
	@Test
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
		
		nodeList.add(n);
		
		bp.setNodes(nodeList);
		bp.getNodes();
		
		Depends_On don = new Depends_On();
		don.setName("BluePrint");
		don.getName();
		
		OperationSignature osig = new OperationSignature();
		osig.setOperation("transform");
		osig.getOperation();
		
		don.setOperation_signature(osig);
		don.getOperation_signature();
		
		
		List<Depends_On> donList = new ArrayList<>();
		donList.add(don);
		
		n.setDepends_on(donList);
		n.getDepends_on();
		
		InputOperationSignatures ios = new InputOperationSignatures();
		ios.setOperation("Auxialiary");
		ios.getOperation();
		
		List<InputOperationSignatures> iosList = new ArrayList<>();
		iosList.add(ios);
		
		bp.setInput_operation_signatures(iosList);
		bp.getInput_operation_signatures();
		logger.debug(EELFLoggerDelegator.debugLogger, bp.toString());
	}
	

	@Test
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
		logger.debug(EELFLoggerDelegator.debugLogger, call.toString());
	}
	
	@Test
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
		logger.debug(EELFLoggerDelegator.debugLogger, "---- " + param.toString());
	}
	
	@Test
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
		logger.debug(EELFLoggerDelegator.debugLogger, "---- " + provide.toString());
		
	}
	
	@Test
	public void testCdumpCapability(){
		String s1 = "123456";
		String s2 = "ReqCapability";
		Capability cap = new Capability();
		cap.setId(s1);
		cap.setName(s2);
		Assert.assertEquals(s1, cap.getId());
		Assert.assertEquals(s2, cap.getName());
		logger.debug(EELFLoggerDelegator.debugLogger, "---- " + cap,toString());
	}
	
	@Test
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
		DSSolution ds = new DSSolution(s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12, s13, s14);
		logger.debug(EELFLoggerDelegator.debugLogger, "---- " + ds.toString());
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
		logger.debug(EELFLoggerDelegator.debugLogger, "---- " + dss.toString());
	}
	
	@Test
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
		logger.debug(EELFLoggerDelegator.debugLogger, "---- " + confProps.toString());
	}
	
	//@Test
	public void testDAOException(){
		
		String s1 = "Desc";
		String s2 = "400";
		String s3 = "Invalid";
		DAOException daoe = new DAOException(s1, s2, s3);
		logger.debug(EELFLoggerDelegator.debugLogger, "---- " + daoe.toString());
	}

}
