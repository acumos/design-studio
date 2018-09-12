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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.acumos.sqldatabroker.exceptionhandler.ServiceException;
import org.acumos.sqldatabroker.jdbc.datasource.DataSourceFactory;
import org.acumos.sqldatabroker.vo.Configuration;
import org.acumos.sqldatabroker.vo.DataBrokerMap;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Qualifier;

@RunWith(MockitoJUnitRunner.class)
public class SQLDatabrokerServiceImplTest {
	
	@InjectMocks
	private SQLDatabrokerServiceImpl service;
	
	@Mock
	@Qualifier("ConfigurationServiceImpl")
	private ConfigurationServiceImpl confService;
	
	@Mock
	private DataSourceFactory dataSourceFactory;
	
	@Mock 
	private ProtobufService protoService;
	
	@Mock
	private BasicDataSource ds;
	
	@Mock
	private Connection con;
	
	@Mock
	private Statement stmt;
	
	@Mock
	private ResultSet rs;
	
	@Mock
	private ResultSetMetaData rsmd;
	
	private Configuration conf = null;
	private String str = null;
	private byte[] output = null;
	
	@Before
	public void setUp() throws Exception {
		conf = new Configuration();
		conf.setUserId("xyz");
		conf.setPassword("xyz");
		conf.setFirst_row("");
		conf.setTarget_system_url("jdbc:mysql://0.0.0.0:3306/test?useSSL=false");
		conf.setDatabaseName("test");
		conf.setDataSourceClassName("com.mysql.jdbc.Driver");
		
		str = "test";
		output = str.getBytes();
		
		Mockito.doCallRealMethod().when(confService).incrementStart();
		Mockito.doCallRealMethod().when(confService).setConf(conf);
		Mockito.doCallRealMethod().when(confService).setResultsetSize(1);
		Mockito.when(confService.getConf()).thenReturn(conf);
		//Mockito.when(executor.getData(Mockito.anyInt(), Mockito.anyString())).thenReturn(str.getBytes());
		Mockito.when(dataSourceFactory.getDataSource(conf)).thenReturn(ds);
		
		Mockito.when(ds.getConnection()).thenReturn(con);
		Mockito.when(con.createStatement()).thenReturn(stmt);
		Mockito.when(stmt.executeQuery(Mockito.anyString())).thenReturn(rs);
		Mockito.when(rs.getMetaData()).thenReturn(rsmd);
		Mockito.when(rsmd.getColumnCount()).thenReturn(5);
		Mockito.when(rs.next()).thenReturn(Boolean.TRUE);
		Mockito.when(rs.getString(Mockito.anyInt())).thenReturn(str);
		Mockito.when(protoService.convertToProtobufFormat(Mockito.anyString())).thenReturn(output);
	}
	
	@Test
	public void getOneRecord() throws Exception {
		confService.setConf(conf);
		confService.setResultsetSize(1);
		byte[] result = service.getOneRecord();
		assertEquals(output, result);
	}
	
	@Test(expected = ServiceException.class)
	public void getOneRecordException() throws Exception {
		confService.setConf(conf);
		confService.setResultsetSize(0);
		Mockito.when(stmt.executeQuery(Mockito.anyString())).thenReturn(null);
		byte[] result = service.getOneRecord();
		assertEquals(output, result);
	}
	
	@Test(expected = ServiceException.class)
	public void getOneRecordSQLException() throws Exception {
		confService.setConf(conf);
		confService.setResultsetSize(0);
		Mockito.when(stmt.executeQuery(Mockito.anyString())).thenThrow(SQLException.class);
		byte[] result = service.getOneRecord();
		assertEquals(output, result);
	}
	
	@Test(expected = ServiceException.class)
	public void getOneRecordCloneNotSupportedException() throws Exception {
		confService.setConf(conf);
		confService.setResultsetSize(0);
		Mockito.when(confService.getConf()).thenThrow(CloneNotSupportedException.class);
		byte[] result = service.getOneRecord();
		assertEquals(output, result);
		
	}
	
}
