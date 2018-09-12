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

package org.acumos.sqldatabroker.service;

import static org.junit.Assert.assertEquals;

import org.acumos.sqldatabroker.vo.Configuration;
import org.acumos.sqldatabroker.vo.DataBrokerMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ConfigurationServiceTest {
	
	@InjectMocks
	private ConfigurationServiceImpl confService;
	
	Configuration conf = null;
	DataBrokerMap map = null;
	
	
	
	@Before
	public void setUp() throws Exception {
		conf = new Configuration();
		conf.setFirst_row("");
	}
	
	@Test //(expected = CloneNotSupportedException.class)
	public void getConf() throws CloneNotSupportedException{
		confService.setConf(conf);
		confService.getConf();
		
		confService.setResultsetSize(1);
		assertEquals(1,confService.getResultsetSize());
		
		confService.setStart(1);
		assertEquals(1,confService.getStart());
		
		confService.setShellFileCreated(true);
		assertEquals(true,confService.isShellFileCreated());
		
		confService.incrementStart();
		assertEquals(2,confService.getStart());
		
		conf.setFirst_row("");
		confService.setConf(conf);
		assertEquals(0,confService.getStart());
		
	}
	
}
