package org.acumos.csvdatabroker.service;

import static org.junit.Assert.*;

import java.io.FileOutputStream;
import java.io.OutputStream;

import org.acumos.csvdatabroker.exceptionhandler.ServiceException;
import org.acumos.csvdatabroker.util.Constants;
import org.acumos.csvdatabroker.util.RemoteScriptExecutor;
import org.acumos.csvdatabroker.vo.Configuration;
import org.acumos.csvdatabroker.vo.DataBrokerMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.SpyBean;


@RunWith(MockitoJUnitRunner.class)
public class CSVDatabrokerServiceTest {

	/*class TestRemoteScriptExecutor extends RemoteScriptExecutor {

		public TestRemoteScriptExecutor(String host, Integer port, String user, String password, String remoteDir,
				String shellFileName) {
			super(host, port, user, password, remoteDir, shellFileName);
			// TODO Auto-generated constructor stub
		}
		
		public TestRemoteScriptExecutor() throws ServiceException {
			//super(Mockito.anyString(),Mockito.anyInt(),Mockito.anyString(),Mockito.anyString(),Mockito.anyString(),Mockito.anyString());
			super(null,null,null,null,null,null);
			///super(conf.getHost(), conf.getPort(), conf.getUserName(), conf.getPassword(),
			//conf.getRemoteDir(), "default.sh");
		}
	}*/
	
	@InjectMocks
	private CSVDatabrokerServiceImpl service;
	
	@Mock
	@Qualifier("ConfigurationServiceImpl")
	private ConfigurationServiceImpl confService;
	
	Configuration conf = null;
	DataBrokerMap map = null;
	
	@Mock
	RemoteScriptExecutor executor; 
	
	@Before
	public void setUp() throws Exception {
		conf = new Configuration();
		conf.setHost("0.0.0.0");
		conf.setPort(80);
		conf.setUserName("xyz");
		conf.setPassword("xyz");
		
		map = new DataBrokerMap();
		map.setFirst_row(Constants.FIRST_ROW_CONTAINS_FIELDNAMES);
		map.setTarget_system_url("file://0.0.0.0:8080/home/user/temp/");
		
		
		conf.setData_broker_map(map);
	}
	
	@Test(expected = ServiceException.class)
	public void writeDataToWithException() throws Exception {
		OutputStream out = new FileOutputStream("test.txt");
		service.setRemoteScriptExecutor(null);
		Mockito.when(confService.getConf()).thenReturn(conf);
		Mockito.doNothing().when(executor).createshellFile(Mockito.anyString());
		Mockito.doNothing().when(executor).executeShell(out);
		service.writeDataTo(out);
		assertEquals(true,confService.isShellFileCreated());
	}
	
	
	@Test
	public void writeDataTo() throws Exception {
		OutputStream out = new FileOutputStream("test.txt");
		service.setRemoteScriptExecutor(executor);
		Mockito.doCallRealMethod().when(confService).setShellFileCreated(true);
		confService.setShellFileCreated(true);
		Mockito.when(confService.isShellFileCreated()).thenCallRealMethod();
		Mockito.when(confService.getConf()).thenReturn(conf);
		Mockito.doNothing().when(executor).createshellFile(Mockito.anyString());
		Mockito.doNothing().when(executor).executeShell(out);
		service.writeDataTo(out);
		
		assertEquals(true,confService.isShellFileCreated());
	}
	
	@Test
	public void getOneRecord() throws Exception {
		String str = "this is test";
		service.setRemoteScriptExecutor(executor);
		Mockito.doCallRealMethod().when(confService).setShellFileCreated(true);
		confService.setShellFileCreated(true);
		Mockito.when(confService.isShellFileCreated()).thenCallRealMethod();
		Mockito.doCallRealMethod().when(confService).incrementStart();
		Mockito.doCallRealMethod().when(confService).setConf(conf);
		confService.setConf(conf);
		Mockito.doCallRealMethod().when(confService).setResultsetSize(1);
		confService.setResultsetSize(1);
		Mockito.when(confService.getConf()).thenReturn(conf);
		Mockito.doNothing().when(executor).createshellFile(Mockito.anyString());
		Mockito.when(executor.executeShell(Mockito.anyInt())).thenReturn(str.getBytes());
		
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
		Mockito.doNothing().when(executor).createshellFile(Mockito.anyString());
		Mockito.when(executor.executeShell(Mockito.anyInt())).thenReturn(str.getBytes());
		
		byte[] result = service.getOneRecord();
		System.out.println(result.toString());
		assertEquals("[B@548a102f",result.toString());
	}
}
