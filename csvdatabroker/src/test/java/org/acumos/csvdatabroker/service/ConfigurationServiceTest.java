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

package org.acumos.csvdatabroker.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.acumos.csvdatabroker.service.ConfigurationService;
import org.acumos.csvdatabroker.util.Constants;
import org.acumos.csvdatabroker.vo.Configuration;
import org.acumos.csvdatabroker.vo.DataBrokerMap;
import org.junit.Test;

public class ConfigurationServiceTest {

	
	@Test
    public void TestgetInstance(){

		ConfigurationService instance1 = ConfigurationService.getInstance();
		ConfigurationService instance2 = ConfigurationService.getInstance();
        // Does not pass
        assertSame(instance1, instance2);
    }
	
	@Test
	public void configurationServiceTest() throws CloneNotSupportedException{
		ConfigurationService confService = ConfigurationService.getInstance();
		Configuration conf = new Configuration();
		DataBrokerMap map = new DataBrokerMap();
		map.setFirst_row(Constants.FIRST_ROW_CONTAINS_FIELDNAMES);
		conf.setData_broker_map(map);
		confService.setConf(conf);
		confService.setResultsetSize(2);
		
		assertEquals(conf.getData_broker_map().getFirst_row(), ConfigurationService.getInstance().getConf().getData_broker_map().getFirst_row());
		assertEquals(2,confService.getResultsetSize());
		assertEquals(1, confService.getStart());
		assertEquals(false, confService.isShellFileCreated());
		
		confService.incrementStart();
		assertEquals(3, confService.getStart());
		
		confService.setShellFileCreated(true);
		assertEquals(true, confService.isShellFileCreated());
		
		confService.setStart(1);
		assertEquals(1, confService.getStart());
		
		conf.getData_broker_map().setFirst_row("");
		confService.setConf(conf);
		assertEquals(0, confService.getStart());
	}
}
