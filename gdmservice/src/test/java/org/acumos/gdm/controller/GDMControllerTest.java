/*-
 * ===============LICENSE_START=======================================================
 * Acumos
 * ===================================================================================
 * Copyright (C) 2017 - 2018 AT&T Intellectual Property & Tech Mahindra. All rights reserved.
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

package org.acumos.gdm.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acumos.gdm.service.IGDMService;
import org.acumos.gdm.util.EELFLoggerDelegator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(MockitoJUnitRunner.class)
public class GDMControllerTest {
	
	private static final EELFLoggerDelegator logger = EELFLoggerDelegator.getLogger(GDMControllerTest.class);
	
	@InjectMocks
	private GDMController gdmController;
	
	
	@Mock
	private IGDMService gdmService;
	
	final HttpServletResponse response = new MockHttpServletResponse();
	final HttpServletRequest request = new MockHttpServletRequest();
	
	@Test
	public void convertToBinary() throws Exception {
		
	}

	@Test
	public void mapData() throws Exception {
		byte[] inputData = "vaibhavNehaRaman	Debashree".getBytes();
		byte[] result = "vaibhavNehaRaman	Debashree".getBytes();
		result = gdmController.mapData(inputData);
		Assert.assertNotNull(result);
		Assert.assertNotEquals(1, result.length);
		
		
	}
	
	@Test
	public void mapDataNotNullData() throws Exception {
		byte[] inputData = "vaibhavNehaRaman	Debashree".getBytes();
		byte[] result = "vaibhavNehaRaman	Debashree".getBytes();
				
		OutputStream bOutput = new ByteArrayOutputStream();
		bOutput.write(result);
		InputStream inputStream = new ByteArrayInputStream(inputData);
		Mockito.when(gdmService.mapData(inputStream)).thenReturn(bOutput);
		byte[] result2 = gdmController.mapData(inputData);
		Assert.assertNotNull(result2);
		Assert.assertEquals(0, result2.length);
	}
}
