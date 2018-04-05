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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.acumos.designstudio.ce.util.ConfigurationProperties;
import org.acumos.designstudio.ce.util.EELFLoggerDelegator;
import org.acumos.designstudio.ce.vo.DSSolution;
import org.acumos.designstudio.ce.vo.blueprint.BaseOperationSignature;
import org.acumos.designstudio.ce.vo.blueprint.BluePrint;
import org.acumos.designstudio.ce.vo.blueprint.Container;
import org.acumos.designstudio.ce.vo.blueprint.Node;
import org.acumos.designstudio.ce.vo.cdump.Capability;
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


public class VOTest  {
	private static final EELFLoggerDelegator logger = EELFLoggerDelegator.getLogger(VOTest.class);
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
		logger.debug(EELFLoggerDelegator.debugLogger, arti.toString());
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
		logger.debug(EELFLoggerDelegator.debugLogger, artifact.toString());
		
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
		logger.debug(EELFLoggerDelegator.debugLogger, aux.toString());
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
		
		nodeList.add(n);
		
		bp.setNodes(nodeList);
		bp.getNodes();
		
		Container con = new Container();
		con.setContainer_name("BluePrint");
		con.getContainer_name();
		
		BaseOperationSignature bos = new BaseOperationSignature();
		bos.setOperation_name("transform");
		bos.getOperation_name();
		
		con.setOperation_signature(bos);
		con.getOperation_signature();
		
		
		List<Container> donList = new ArrayList<>();
		donList.add(con);
		
		n.setContainer_name("Aggregate");
		
		logger.debug(EELFLoggerDelegator.debugLogger, bp.toString());
		
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
		logger.debug(EELFLoggerDelegator.debugLogger, call.toString());
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
		logger.debug(EELFLoggerDelegator.debugLogger,   param.toString());
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
		logger.debug(EELFLoggerDelegator.debugLogger,   provide.toString());
		
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
		logger.debug(EELFLoggerDelegator.debugLogger,   cap.toString());
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
		DSSolution ds = new DSSolution(s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12, s13, s14);
		logger.debug(EELFLoggerDelegator.debugLogger,   ds.toString());
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
		logger.debug(EELFLoggerDelegator.debugLogger,   dss.toString());
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
		logger.debug(EELFLoggerDelegator.debugLogger,   confProps.toString());
	}
	

}
