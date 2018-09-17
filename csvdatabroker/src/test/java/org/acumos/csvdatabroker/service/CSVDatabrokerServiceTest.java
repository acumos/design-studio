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

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.acumos.csvdatabroker.exceptionhandler.ServiceException;
import org.acumos.csvdatabroker.util.Constants;
import org.acumos.csvdatabroker.util.EELFLoggerDelegator;
import org.acumos.csvdatabroker.util.LocalScriptExecutor;
import org.acumos.csvdatabroker.util.RemoteScriptExecutor;
import org.acumos.csvdatabroker.vo.Configuration;
import org.acumos.csvdatabroker.vo.DataBrokerMap;
import org.acumos.csvdatabroker.vo.Protobuf;
import org.acumos.csvdatabroker.vo.ProtobufServiceOperation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import static org.mockito.Mockito.when;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Qualifier;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;


@RunWith(MockitoJUnitRunner.class)
public class CSVDatabrokerServiceTest {

	@InjectMocks
	private CSVDatabrokerServiceImpl service;
	
	@Mock
	@Qualifier("ConfigurationServiceImpl")
	private ConfigurationServiceImpl confService;
	
	Configuration conf = null;
	DataBrokerMap map = null;
	
	@Mock
	RemoteScriptExecutor executor; 
	
	@InjectMocks
	RemoteScriptExecutor remoteScriptExecutor;
	
	@InjectMocks
	LocalScriptExecutor localScriptExecutor;
	
	@Mock
	Protobuf protobuf;
	
	private static final EELFLoggerDelegator logger = EELFLoggerDelegator.getLogger(LocalScriptExecutor.class);
	
	@Before
	public void setUp() throws Exception {
		conf = new Configuration();
		conf.setUserId("xyz");
		conf.setPassword("xyz");
		conf.setFirst_row(Constants.FIRST_ROW_CONTAINS_FIELDNAMES);
		conf.setTarget_system_url("file://0.0.0.0:8080/home/user/temp/");
	}
	@Test
	//@Test(expected = ServiceException.class)
	public void writeDataToWithException() throws Exception {
		
		Session session = Mockito.mock(Session.class);
		
		OutputStream out = new FileOutputStream("test.txt");
		service.setRemoteScriptExecutor(null);
		Mockito.when(confService.getConf()).thenReturn(conf);
		Mockito.doNothing().when(session).connect();
		
		//Mockito.doNothing().when(executor).createshellFile(Mockito.anyString());
		//Mockito.doNothing().when(executor).getData(out,Mockito.anyString());
		try{
			service.writeDataTo(out);
			assertEquals(true,confService.isShellFileCreated());
		} catch(Exception e){
			
		}
	}
	
	
	@Test
	public void writeDataTo() throws Exception {
		OutputStream out = new FileOutputStream("test.txt");
		service.setRemoteScriptExecutor(executor);
		Mockito.when(confService.getConf()).thenReturn(conf);
		Mockito.doNothing().when(executor).getData(out, "/home/user/temp/");
		try{
			service.writeDataTo(out);		
			assertEquals(0,confService.getStart());
		} catch(Exception e){
			
		}
		
	}
	
	@Test
	public void getOneRecord() throws Exception {
		String str = "this is test";
		service.setRemoteScriptExecutor(executor);
		//Mockito.doCallRealMethod().when(confService).setShellFileCreated(true);
		//confService.setShellFileCreated(true);
		//Mockito.when(confService.isShellFileCreated()).thenCallRealMethod();
		Mockito.doCallRealMethod().when(confService).incrementStart();
		Mockito.doCallRealMethod().when(confService).setConf(conf);
		confService.setConf(conf);
		Mockito.doCallRealMethod().when(confService).setResultsetSize(1);
		confService.setResultsetSize(1);
		Mockito.when(confService.getConf()).thenReturn(conf);
		//Mockito.doNothing().when(executor).createshellFile(Mockito.anyString());
		Mockito.when(executor.getData(Mockito.anyInt(), Mockito.anyString())).thenReturn(str.getBytes());
		
		byte[] result = service.getOneRecord();
		assertEquals(0,confService.getStart());
	}
	
	@Test(expected = ServiceException.class)
	public void getOneRecordException() throws Exception {
		String str = "this is test";
		service.setRemoteScriptExecutor(null);
		Mockito.doCallRealMethod().when(confService).setShellFileCreated(true);
		confService.setShellFileCreated(true);
		Mockito.when(confService.isShellFileCreated()).thenCallRealMethod();
		Mockito.when(confService.getConf()).thenReturn(conf);
		//Mockito.doNothing().when(executor).createshellFile(Mockito.anyString());
		Mockito.when(executor.getData(Mockito.anyInt(),Mockito.anyString())).thenReturn(str.getBytes());
		
		byte[] result = service.getOneRecord();
		assertEquals("[B@548a102f",result.toString());
	}
	
	@Test()
	public void getData() throws Exception {
		
		JSch jSch = Mockito.mock(JSch.class);
		Session session = Mockito.mock(Session.class);
		ChannelSftp sftp = Mockito.mock(ChannelSftp.class);
		
		String localpath = "./src/test/resources/";
		OutputStream out = new FileOutputStream("test.txt");
		//Mockito.when(executor.getData(Mockito.anyInt(),Mockito.anyString())).thenReturn(str.getBytes());
		try{
			when(jSch.getSession("test", "xyz", 2300)).thenReturn(session);
	        when(session.openChannel("sftp")).thenReturn(sftp);
			remoteScriptExecutor.getData(out, localpath+"Test.csv");
			logger.debug("getData() : Succesfully Executed Test case");
		} catch(Exception e){
			logger.error("getData() : Exception in Test case : getData()");
		}
		
	}
	
	
	@Test()
	public void getData2() throws Exception {
		
		String localpath = ".\\src\\test\\resources\\C2ImportFamRelSample.csv";
		OutputStream out = new FileOutputStream("test.txt");
		List<ProtobufServiceOperation> operations = new ArrayList<ProtobufServiceOperation>();
		ProtobufServiceOperation protobufServiceOperation = new ProtobufServiceOperation();
		String inputname1 = "InputTest1";
		String inputname2 = "InputTest2";
		String inputname3 = "InputTest3";
		String outputname1 = "OutputTest1";
		String outputname2 = "OutputTest2";
		String outputname3 = "OutputTest3";
		try{
			protobufServiceOperation.setName("Demo");
			List<String> inputMessageNames = new ArrayList<String>();
			inputMessageNames.add(inputname1);
			inputMessageNames.add(inputname2);
			inputMessageNames.add(inputname3);
			List<String> outputMessageNames = new ArrayList<String>();
			outputMessageNames.add(outputname1);
			outputMessageNames.add(outputname2);
			outputMessageNames.add(outputname3);
			
			operations.add(protobufServiceOperation);
			protobufServiceOperation.setInputMessageNames(inputMessageNames);
			protobufServiceOperation.setOutputMessageNames(outputMessageNames);
			
			when(protobuf.getService().getOperations()).thenReturn(operations);		
			localScriptExecutor.getData(out, localpath);
			logger.debug("getData() : Succesfully executed Test case");
		} catch(Exception e){
			logger.error("getData() : Exception in Test case : getData()");
		}
		
	}
}
