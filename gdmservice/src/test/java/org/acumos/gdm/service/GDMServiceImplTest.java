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

package org.acumos.gdm.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.acumos.gdm.util.EELFLoggerDelegator;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class GDMServiceImplTest {

	private static final EELFLoggerDelegator logger = EELFLoggerDelegator.getLogger(GDMServiceImplTest.class);
	
	@Mock
	ResourceLoader resourceLoader;
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Test
	public void mapData() throws Exception {
		byte[] inputData = "abcabcdlmnop	Testdata1".getBytes();
		InputStream inputStream = new ByteArrayInputStream(inputData);
				
		Resource testResource = getLocalTestSitePropertyResouce("classpath:FieldMapping.json");
		Mockito.when(resourceLoader.getResource("classpath:FieldMapping.json")).thenReturn(testResource);
		IGDMService service = new GDMServiceImpl(resourceLoader);
		OutputStream outputStream = service.mapData(inputStream);
		Assert.assertNotNull(outputStream);
		
	}
	
	@Test(expected = IOException.class)
	public void mapDataTestException() throws Exception {
		byte[] inputData = "abcabcdlmnop	Testdata1".getBytes();
		InputStream inputStream = new ByteArrayInputStream(inputData);
		
		//exception.expect(AssertionError.class);
		Resource testResource = getLocalTestSitePropertyResouce("classpath:FieldMapping1.json");
		Mockito.when(resourceLoader.getResource("classpath:FieldMapping.json")).thenReturn(testResource);
		IGDMService service = new GDMServiceImpl(resourceLoader);
		OutputStream outputStream = service.mapData(inputStream);
		Assert.assertNotNull(outputStream);
		
	}
	
	private static Resource getLocalTestSitePropertyResouce(String fileName)
	{
	    ResourceLoader testResourceLoader = new FileSystemResourceLoader();
	    Resource testResource = testResourceLoader.getResource( fileName );

	    if ( !testResource.exists() )
	    {
	    	logger.debug(EELFLoggerDelegator.debugLogger,"Could not load test resource: " + testResource );
	    }
	    return testResource;
	}
}
