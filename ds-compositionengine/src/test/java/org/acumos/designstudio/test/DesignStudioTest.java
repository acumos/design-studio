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

import org.acumos.designstudio.ce.docker.DockerClientFactory;
import org.acumos.designstudio.ce.docker.DockerConfiguration;
import org.acumos.designstudio.ce.exceptionhandler.ServiceException;
import org.acumos.designstudio.ce.util.DSLogConstants;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DesignStudioTest {
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Test
	public void designStudioTest() {

		DockerConfiguration config = new DockerConfiguration();
		config.setHost("test");
		config.setPort(0);
		assertTrue(config.getPort() == 0);
		config.setRequestTimeout(0);
		assertTrue(config.getRequestTimeout() == 0);
		config.setSocket(false);
		assertTrue(config.isSocket() == false);

		try {
			DockerClientFactory.getDockerClient(config);
		} catch (ServiceException e) {
			logger.error("ServiceException occured in getDockerClient()");
		}
		assertTrue(DSLogConstants.MDCs.REQUEST_ID == "X-ACUMOS-Request-Id");
		assertTrue(DSLogConstants.MDCs.REQUEST_ID == "X-ACUMOS-Request-Id");
		assertTrue(DSLogConstants.MDCs.TARGET_ENTITY == "TargetEntity");
		assertTrue(DSLogConstants.MDCs.PARTNER_NAME == "PartnerName");
		assertTrue(DSLogConstants.MDCs.SERVICE_NAME == "ServiceName");
		assertTrue(DSLogConstants.MDCs.TARGET_SERVICE_NAME == "TargetServiceName");
		assertTrue(DSLogConstants.MDCs.INSTANCE_UUID == "InstanceUUID");
		assertTrue(DSLogConstants.MDCs.CLIENT_IP_ADDRESS == "ClientIPAddress");
		assertTrue(DSLogConstants.MDCs.SERVER_FQDN == "ServerFQDN");
		assertTrue(DSLogConstants.MDCs.ENTRY_TIMESTAMP == "EntryTimestamp");
		assertTrue(DSLogConstants.MDCs.RESPONSE_CODE == "ResponseCode");
		assertTrue(DSLogConstants.MDCs.RESPONSE_DESCRIPTION == "ResponseDescription");
		assertTrue(DSLogConstants.MDCs.RESPONSE_SEVERITY == "Severity");
		assertTrue(DSLogConstants.MDCs.RESPONSE_STATUS_CODE == "StatusCode");
		assertTrue(DSLogConstants.MDCs.USER == "User");

		assertTrue(DSLogConstants.Headers.REQUEST_ID == "X-ACUMOS-Request-Id");
		assertTrue(DSLogConstants.Headers.INVOCATION_ID == "Invocation-ID");
		assertTrue(DSLogConstants.Headers.PARTNER_NAME == "PartnerName");
		assertTrue(DSLogConstants.Headers.RESPONSE_CODE == "ResponseCode");
		assertTrue(DSLogConstants.Headers.RESPONSE_DESCRIPTION == "ResponseDescription");
		assertTrue(DSLogConstants.Headers.RESPONSE_SEVERITY == "ResponseSeverity");
		assertTrue(DSLogConstants.Headers.RESPONSE_STATUS_CODE == "ResponseStatusCode");

	}

}
