/*-
 * ===============LICENSE_START=======================================================
 * Acumos
 * ===================================================================================
 * Copyright (C) 2019 AT&T Intellectual Property & Tech Mahindra. All rights reserved.
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

import static org.junit.Assert.assertTrue;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.acumos.cds.domain.MLPSolution;
import org.acumos.cds.domain.MLPTag;
import org.acumos.designstudio.ce.Application;
import org.acumos.designstudio.ce.config.HandlerInterceptorConfiguration;
import org.acumos.designstudio.ce.config.LoggingHandlerInterceptor;
import org.acumos.designstudio.ce.config.SwaggerConfiguration;
import org.acumos.designstudio.ce.docker.NoImplSslConfig;
import org.acumos.designstudio.ce.exceptionhandler.DAOException;
import org.acumos.designstudio.ce.service.MatchingModelServiceComponent;
import org.acumos.designstudio.ce.service.MatchingModelServiceImpl;
import org.acumos.designstudio.ce.util.ModelCacheForMatching;
import org.acumos.designstudio.ce.vo.cdump.ComplexType;
import org.acumos.designstudio.ce.vo.cdump.NodeType;
import org.acumos.designstudio.ce.vo.cdump.Relationship;
import org.acumos.designstudio.ce.vo.cdump.Typeinfo;
import org.acumos.designstudio.ce.vo.matchingmodel.DSModelVO;
import org.acumos.designstudio.ce.vo.protobuf.MessageBody;
import org.acumos.designstudio.ce.vo.protobuf.MessageargumentList;
import org.acumos.designstudio.ce.vo.protobuf.ProtoBufClass;
import org.apache.http.HttpException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class VOProtobufTest {
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Test
	public void voProtobufTest() {
		List<MessageargumentList> messageargumentList1 = null;
		MessageargumentList messageargumentList = new MessageargumentList();
		messageargumentList.setName("test");
		assertTrue(messageargumentList.getName() == "test");
		messageargumentList.setRole("test");
		assertTrue(messageargumentList.getRole() == "test");
		messageargumentList.setTag("test");
		assertTrue(messageargumentList.getTag() == "test");
		messageargumentList.setType("test");
		assertTrue(messageargumentList.getType() == "test");

		ComplexType complexType1 = new ComplexType();
		complexType1.setMessageName("test");
		assertTrue(complexType1.getMessageName() == "test");

		ComplexType complexType = new ComplexType();
		complexType.setMessageName("test");
		assertTrue(complexType.getMessageName() == "test");
		complexType.setMessageargumentList(messageargumentList1);
		assertTrue(complexType.getMessageargumentList() == messageargumentList1);
		messageargumentList.setComplexType(complexType);
		assertTrue(messageargumentList.getComplexType() == complexType);

		MessageBody messageBody = new MessageBody();
		messageBody.setMessageName("test");
		assertTrue(messageBody.getMessageName() == "test");
		messageBody.setMessageargumentList(messageargumentList1);
		assertTrue(messageBody.getMessageargumentList() == messageargumentList1);
		messageargumentList.equals(messageargumentList);
		messageargumentList.hashCode();

		SwaggerConfiguration swaggerConfiguration = new SwaggerConfiguration();
		swaggerConfiguration.api();
		NodeType nodeType = new NodeType();
		nodeType.setNodeTypeName("test");
		assertTrue(nodeType.getNodeTypeName() == "test");

		LoggingHandlerInterceptor loggingHandlerInterceptor = new LoggingHandlerInterceptor();
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter("test", "test");
		MockHttpServletResponse response = new MockHttpServletResponse();
		response.addHeader("test", "test");
		Object handler = "test";
		Exception ex = new HttpException();
		try {
			loggingHandlerInterceptor.afterCompletion(request, response, handler, ex);
		} catch (Exception e) {
			logger.error("Exception occured in LoggingHandlerInterceptor ");
		}

	}

	@Test
	public void DETest() throws Exception {

		Application application = new Application();

		DSModelVO dSModelVO = new DSModelVO();
		MLPSolution mlpSolution = new MLPSolution();
		mlpSolution.setSourceId("test");
		Set<MLPTag> tags = null;
		mlpSolution.setTags(tags);
		dSModelVO.setMlpSolution(mlpSolution);
		assertTrue(dSModelVO.getMlpSolution() == mlpSolution);
		HandlerInterceptorConfiguration handlerInterceptorConfiguration = new HandlerInterceptorConfiguration();
		MatchingModelServiceComponent matchingModelServiceComponent = new MatchingModelServiceComponent();
		List<MLPSolution> matchingModelsolutionList = new ArrayList<MLPSolution>();
		MLPSolution element = new MLPSolution();
		element.setSourceId("test");
		matchingModelServiceComponent.setMatchingModelsolutionList(matchingModelsolutionList);
		assertTrue(matchingModelServiceComponent.getMatchingModelsolutionList() == matchingModelsolutionList);

		MatchingModelServiceImpl matchingModelServiceImpl = new MatchingModelServiceImpl();

		try {

			matchingModelServiceImpl.getPrivateDSModels("test");

		} catch (Exception e) {
			logger.error("Exception occured in MatchingModelServiceImpl ");
		}

		try {

			matchingModelServiceImpl.getPublicDSModels();

		} catch (Exception e) {
			logger.error("Exception occured in MatchingModelServiceImpl ");
		}

		NoImplSslConfig noImplSslConfig = new NoImplSslConfig();
		noImplSslConfig.getSSLContext();

		ProtoBufClass protoBufClass = new ProtoBufClass();
		List<MessageBody> listOfMessages = new ArrayList<>();
		MessageBody element1 = new MessageBody();
		element1.setMessageName("test");
		try {

			matchingModelServiceImpl.getPublicDSModels();

		} catch (Exception e) {
			logger.error("Exception occured in MatchingModelServiceImpl ");
		}

		protoBufClass.setListOfMessages(listOfMessages);
		assertTrue(protoBufClass.getListOfMessages() == listOfMessages);

		Relationship relationship = new Relationship();

		Typeinfo typeinfo = new Typeinfo();
		ModelCacheForMatching modelCacheForMatching = new ModelCacheForMatching();
		modelCacheForMatching.getPrivateModelCache("test");

		try {

			Exception dAOException = new DAOException();

		} catch (Exception e) {
			logger.error("Exception occured in DAOException ");
		}

		modelCacheForMatching.getUserPrivateModelUpdateTime("test");

	}

}
