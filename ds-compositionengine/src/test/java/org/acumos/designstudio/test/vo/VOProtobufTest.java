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
package org.acumos.designstudio.test.vo;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.acumos.designstudio.ce.config.LoggingHandlerInterceptor;
import org.acumos.designstudio.ce.config.SwaggerConfiguration;
import org.acumos.designstudio.ce.vo.cdump.ComplexType;
import org.acumos.designstudio.ce.vo.cdump.NodeType;
import org.acumos.designstudio.ce.vo.protobuf.MessageBody;
import org.acumos.designstudio.ce.vo.protobuf.MessageargumentList;
import org.apache.http.HttpException;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class VOProtobufTest {

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
			e.printStackTrace();
		}

	}

}
