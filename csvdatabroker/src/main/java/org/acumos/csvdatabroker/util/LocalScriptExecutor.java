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

package org.acumos.csvdatabroker.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import org.acumos.csvdatabroker.service.ConfigurationService;
import org.acumos.csvdatabroker.service.ConfigurationServiceImpl;
import org.acumos.csvdatabroker.service.ProtobufService;
import org.acumos.csvdatabroker.service.ProtobufServiceImpl;
import org.acumos.csvdatabroker.vo.Configuration;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.acumos.csvdatabroker.exceptionhandler.ServiceException;

public class LocalScriptExecutor {
	
	private static final EELFLoggerDelegator logger = EELFLoggerDelegator.getLogger(LocalScriptExecutor.class);
	private static String OS = System.getProperty("os.name").toLowerCase();
	
	private String localPath; 
	private String scriptFileName; 
	
	private ProtobufService protoService;
	
	/**
	 * LocalScriptExecutor construct accept the localdir path and the script file name to be created.
	 * @param localPath
	 * 			The local file path where the data file is placed
	 * @param protoService
	 * 			The Proto Service
	 */
	public LocalScriptExecutor(String localPath, ProtobufService protoService) { 
		this.localPath = localPath; 
		this.scriptFileName = "default";
		this.protoService = protoService;
	}
	
	/**
	 * Included for test case purpose.
	 */
	public LocalScriptExecutor() {
		
	}
	/**
	 * This method reads the file and returns the records at "start" converting it into protobuf binary format.
	 * @param start
	 * 		This method accepts script.
	 * @param filePath
	 * 	    The local data file path.
	 * @return byte[]
	 * 		This method returns byte[]
	 * @throws Exception
	 * 		This method throws the Exception
	 */
	public byte[] getData(int start, String filePath) throws Exception {
		long startTime = System.nanoTime();
		byte[] result = null;
		
		File file = new File(filePath);
		int cnt = 0;
		String line = null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
 
        try {
        while ( (line = reader.readLine()) != null) {
        	if(cnt < start){
        		cnt++;
        		continue;
        	}
        	if(cnt == start ){
        		result = protoService.convertToProtobufFormat(line);
        		break;
        	}
        }
        long endTime = System.nanoTime();
        long elapsedTimeInMillis = TimeUnit.MILLISECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS);
        logger.debug("File processed in : " + elapsedTimeInMillis + " ms");
        } finally {
        	if(null != reader){
				try {
					reader.close();
				} catch (IOException e) {
					logger.error(EELFLoggerDelegator.errorLogger, "Exception in getData()", e);
				}
			}
        }
        
		return result;
	}
		
		
		
	/**
	 * This method execute script on local machine and return the records at "start" converting it into protobuf binary format.
	 * @param start
	 * 		This method accepts script
	 * @return byte[]
	 * 		This method returns byte[]
	 * @throws Exception
	 * 		This method throws the Exception
	 */
	@Deprecated
	public byte[] executeScript(int start) throws Exception {
		byte[] result = null;
		ProcessBuilder processBuilder = null;
		Process process = null;
		BufferedReader reader = null;
		try {
			processBuilder = getProcessBuilder();
			if(processBuilder != null) {
				File dir = new File(localPath);
	            processBuilder.directory(dir);
	            process = processBuilder.start();
				String line = null;
	            int cnt = 0;
	            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
	            while ( (line = reader.readLine()) != null) {
	            	if(cnt < start){
	            		cnt++;
	            		continue;
	            	}
	            	if(cnt == start ){
	            		result = protoService.convertToProtobufFormat(line);
	            		break;
	            	}
	            }
			} else {
				logger.error("No ProcessBuilder found for OS : "+ OS);
			}
		} finally {
			if(null != process){
				process.waitFor(10, TimeUnit.SECONDS);
				process.destroy();
			}
			if(null != reader){
				try {
					reader.close();
				} catch (IOException e) {
					logger.error(EELFLoggerDelegator.errorLogger, "Exception in executeScript()", e);
				}
			}
		}
		
		return result;
	}
	
	
	/**
	 * This method read the file on local machine and writes data to the OutputStream
	 * @param out
	 * 		This method accepts out
	 * @throws Exception
	 * 		This method throws the Exception
	 * 
	 */
	/**
	 * This method read the file on local machine and writes data to the OutputStream
	 * @param out
	 * 			This method accepts out
	 * @param filePath
	 * 			This method accepts filePath
	 * @throws Exception
	 * 			Throws Exception on case of any failure
	 */
	public void getData(OutputStream out, String filePath) throws Exception {
		BufferedReader reader = null;
		long startTime = System.nanoTime();
		byte[] result = null;
		
		File file = new File(filePath);
		int cnt = 0;
		String line = null;
        reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        byte[] output = null;
        try {
        while ( (line = reader.readLine()) != null) {
        	logger.debug(line);
			output = protoService.convertToProtobufFormat(line);
			out.write(output);
			out.flush();
        }
        long endTime = System.nanoTime();
        long elapsedTimeInMillis = TimeUnit.MILLISECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS);
        logger.debug("File processed in : " + elapsedTimeInMillis + " ms");
        } finally {
        	if(null != reader){
				try {
					reader.close();
				} catch (IOException e) {
					logger.error(EELFLoggerDelegator.errorLogger, "Exception in getData()", e);
				}
			}
        }
	}
	
	/**
	 * This method execute script on local machine and writes data to the OutputStream
	 * @param out
	 * 		This method accepts out
	 * @throws Exception
	 * 		This method throws the Exception
	 * 
	 */
	@Deprecated
	public void executeScript(OutputStream out) throws Exception {
		BufferedReader reader = null;
		ProcessBuilder processBuilder = null;
		Process process = null;

		try {
			processBuilder = getProcessBuilder();
			if(processBuilder != null){
				process = processBuilder.start();
				String line = null;
				byte[] output = null;
				reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				while ((line = reader.readLine()) != null) {
					logger.debug(line);
					output = protoService.convertToProtobufFormat(line);
					out.write(output);
					out.flush();
				}
			} else {
				logger.error("No ProcessBuilder found for OS : "+ OS);
			}
		} finally {
			if(null != process){
				process.waitFor(10, TimeUnit.SECONDS);
				process.destroy();
			}
			if(null != reader){
				try {
					reader.close();
				} catch (IOException e) {
					logger.error(EELFLoggerDelegator.errorLogger, "Exception in executeScript()", e);
				}
			}
		}

	}
	
	
	/**
	 * This method create the script depending on the runtime environment.
	 * @param script
	 * 	The script to be put in to script file. 
	 * 
	 * @throws IOException
	 * 	Throws IOException is any IO error occurs. 
	 * 
	 */
	@Deprecated
	public void createScriptFile(String script) throws IOException {
		if (isWindows()) {
            logger.debug("This is Windows");
            //create .bat file 
            DSUtil.writeDataToFile(localPath, scriptFileName, "bat", script);
        } else if (isMac()) {
        	logger.debug("This is Mac");
        	DSUtil.writeDataToFile(localPath, scriptFileName, "sh", script);
        } else if (isUnix()) {
        	logger.debug("This is Unix or Linux");
        	DSUtil.writeDataToFile(localPath, scriptFileName, "sh", script);
        } else if (isSolaris()) {
        	logger.debug("This is Solaris");
        	DSUtil.writeDataToFile(localPath, scriptFileName, "sh", script);
        } else {
        	logger.debug("Your OS is not support!!");
        }
		
	}
	
	
	private ProcessBuilder getProcessBuilder() {
		ProcessBuilder processBuilder = null;
		if (isWindows()) {
			logger.debug("This is Windows");
			processBuilder = new ProcessBuilder("cmd", "/c", "default.bat");
			
		} else if (isMac()) {
			logger.debug("Mac OS is not supported");
		} else if (isUnix()) {
			logger.debug("This is Unix or Linux");
			processBuilder = new ProcessBuilder("sh", "-c", "default.sh");
		} else if (isSolaris()) {
			logger.debug("Solaris OS is not supported");
		} else {
			logger.debug("Your OS is not support!!");
		}
		File dir = new File(localPath);
		
		if(null != processBuilder ) 
			processBuilder.directory(dir);
		
		return processBuilder;
	}
	
	
	private static boolean isWindows() {
        return (OS.indexOf("win") >= 0);
    }

	private static boolean isMac() {
        return (OS.indexOf("mac") >= 0);
    }

	private static boolean isUnix() {
        return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );
    }

	private static boolean isSolaris() {
        return (OS.indexOf("sunos") >= 0);
    }
}
