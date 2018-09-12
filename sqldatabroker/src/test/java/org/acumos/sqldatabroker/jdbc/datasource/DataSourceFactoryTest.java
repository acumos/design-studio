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

package org.acumos.sqldatabroker.jdbc.datasource;

import static org.junit.Assert.assertEquals;

import javax.sql.DataSource;

import org.acumos.sqldatabroker.exceptionhandler.ServiceException;
import org.acumos.sqldatabroker.vo.Configuration;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DataSourceFactoryTest {

	@InjectMocks
	private DataSourceFactory dsFactory;
	
	@Mock
	private BasicDataSource ds;
	
	private Configuration conf = null;
	
	@Before
	public void setUp() throws Exception {
		conf = new Configuration();
		conf.setUserId("xyz");
		conf.setPassword("xyz");
		conf.setFirst_row("");
		conf.setTarget_system_url("jdbc:mysql://0.0.0.0:3306/test?useSSL=false");
		conf.setDatabaseName("test");
		conf.setDataSourceClassName("com.mysql.jdbc.Driver");
		
	}
	
	@Test
	public void getDataSourceMySQL() throws ServiceException{
		BasicDataSource ds = dsFactory.getDataSource(conf);
		assertEquals(conf.getDataSourceClassName(), ds.getDriverClassName());
	}
	
	@Test
	public void getDataSourceOracle() throws ServiceException{
		conf.setDataSourceClassName("oracle.jdbc.driver.OracleDriver");
		BasicDataSource ds = dsFactory.getDataSource(conf);
		assertEquals(conf.getDataSourceClassName(), ds.getDriverClassName());
	}
	
	
	@Test
	public void getDataSourceNull() throws ServiceException{
		conf.setDataSourceClassName("oracle.jdbc.driver.OracleDriver12");
		BasicDataSource ds = dsFactory.getDataSource(conf);
		Assert.assertNull(ds);
	}
	
	@Test(expected = ServiceException.class)
	public void getDataSourceException() throws Exception { 
		conf = null;
		BasicDataSource ds = dsFactory.getDataSource(conf);
		Assert.assertNull(ds);
	}
	
	
}
