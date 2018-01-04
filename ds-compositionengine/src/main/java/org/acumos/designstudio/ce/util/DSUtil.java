/*-
 * ===============LICENSE_START=======================================================
 * Acumos
 * ===================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property & Tech Mahindra. All rights reserved.
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

package org.acumos.designstudio.ce.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.acumos.designstudio.ce.controller.SolutionController;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 *
 *
 */
public class DSUtil {
	private static final EELFLoggerDelegator logger = EELFLoggerDelegator.getLogger(DSUtil.class);
	
	
	
	/**
	 * 
	 */
	public DSUtil() {
		super();
	}
	/**
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static String readFile(String filePath) throws IOException {
		logger.debug(EELFLoggerDelegator.debugLogger,"-----------  readFile() started --------------");
		FileReader fr = new FileReader(filePath);
		BufferedReader br = new BufferedReader(fr);
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append("\n");
				line = br.readLine();
			}
			logger.debug(EELFLoggerDelegator.debugLogger,"----------- readFile() ended ------------");
			return sb.toString();
		} finally {
			fr.close();
			br.close();
		}
	}
	/**
	 * 
	 * @param path
	 * @param fileName
	 * @param extension
	 * @param data
	 */
	public static void writeDataToFile(String path, String fileName, String extension, String data) {
		logger.debug("-------------- writeDataToFile() started --------------");
		PrintWriter writer = null;
		String completeFileName = path + fileName; 
		if(null != extension && !extension.trim().equals("")){
			completeFileName = completeFileName + "." + extension;
		}
		try {
			writer = new PrintWriter(completeFileName, "UTF-8");
			writer.write(data);

			logger.debug("-------------- writeDataToFile() ended -------------");
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger,
					" --------------- Exception Occured   writeDataToFile() ----------------" + fileName, e);
		} finally {
			if(null != writer){
				writer.close();
			}
		}
	}
    /**
     * 
     * @param filePath
     * @return
     */
	public static boolean isValidJsonSchema(String filePath){
		 try {
			    String josnString = readFile(filePath);
				JSONObject rawSchema = null;
				try {
					rawSchema = new JSONObject(new JSONTokener(josnString));
					Schema schema = SchemaLoader.load(rawSchema);
					schema.validate(new JSONObject(josnString)); // throws a ValidationException if this object is invalid
				} catch (JSONException e) {
					logger.error(EELFLoggerDelegator.errorLogger," --------------- Exception Occured   isValidJsonSchema() ----------------",e);
					return false;
				}
		}catch (IOException e) {
			logger.error(EELFLoggerDelegator.errorLogger," --------------- Exception Occured   isValidJsonSchema() ----------------",e);
		}catch(Exception ex){
			logger.error(EELFLoggerDelegator.errorLogger," --------------- Exception Occured   isValidJsonSchema() ----------------",ex);
		}
		 return true;
	}
	/**
	 * 
	 * @param userId
	 * @param path
	 * @return
	 */
	public static String createCdumpPath(String userId, String path) {
		File parentfile = new File(path);
		if(!parentfile.exists()){
			parentfile.mkdir(); //create parent folder if it does not exists
		}
		File subFile = new File(parentfile, userId); 
		subFile.mkdir(); //create the subfolder for the userId.
		String result = subFile.getPath()+"/";
		return result;
	}
	/**
	 * 
	 * @param userId
	 * @param path
	 * @return
	 */
	public static String readCdumpPath(String userId, String path) {
		// Upload the file to Nexus for the solution.
		//construct the user specific path 
		String result = path + userId + "/";
		return result;
	}
	/**
	 * 
	 * @param path
	 */
	public static void deleteFile(String path) {
		File file = new File(path);
		boolean deleted = false;
		if(file.exists()){
			deleted = file.delete();
			if(deleted){
				logger.debug(EELFLoggerDelegator.debugLogger, "file deleted successfully");
			}
		}
	}
	/**
	 * 
	 * @param folder
	 */
	public static void rmdir(final File folder) {
	      // check if folder file is a real folder
		boolean fRemoved = false;
	      if (folder.isDirectory()) {
	          File[] list = folder.listFiles();
	          if (list != null) {
	              for (int i = 0; i < list.length; i++) {
	                  File tmpF = list[i];
	                  if (tmpF.isDirectory()) {
	                      rmdir(tmpF);
	                  }
	                  fRemoved = tmpF.delete();
	                  if(fRemoved){
	                	  logger.debug(EELFLoggerDelegator.debugLogger, "temp folder removed");
	                  }
	              }
	          }
	      }
	  }
	
	/**
	 * 
	 * @param path
	 */
	public static void rmUserdir(String path){
		File parent = new File(path);
		File[] files = parent.listFiles();
		if(files.length ==0){
			parent.delete();
		}
	}
	/**
	 * 
	 * @param requirements
	 * @param capabilities
	 * @return
	 */
	public static String isValidJsonSchemaContents_List(String requirements, String capabilities) {
		StringBuffer sb = new StringBuffer("");
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.readTree(requirements);
		} catch (Exception ex) {
			logger.error(EELFLoggerDelegator.errorLogger," --------------- Exception Occured   isValidJsonSchemaContents_List() ----------------",ex);
			sb.append("requirements ");
		}
		try {
			mapper.readTree(capabilities);
		} catch (Exception ex) {
			logger.error(EELFLoggerDelegator.errorLogger," --------------- Exception Occured   isValidJsonSchemaContents_List() ----------------",ex);
			sb.append("capabilities");
		}
		return sb.toString();
	}
	
	/**
	 * 
	 * @param relationship
	 * @return
	 */
	public static String isValidJsonaddLink(String relationship) {
		String err = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.readTree(relationship);
		} catch (Exception ex) {
			logger.error(EELFLoggerDelegator.errorLogger," --------------- Exception Occured   isValidJsonaddLink() ----------------",ex);
			err = "relationship";
		}
		return err;
	}

	/**
	 * 
	 * @param jsonInString
	 * @return
	 */
	public static boolean isValidJSON(String jsonInString) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			mapper.readTree(jsonInString);
			return true;
		} catch (IOException e) {
			logger.error(EELFLoggerDelegator.errorLogger,
					" --------------- Exception Occured   isValidJsonaddLink() ----------------", e);
			return false;
		}
	}
	
	
	/**
	 * 
	 * @param cmd
	 * @return
	 * @throws Exception
	 */
	public static int runCommand(String cmd) throws Exception {
		logger.debug("Exec: " + cmd);
		Process p = null;
		int exitVal = 1;
		try {
			 p = Runtime.getRuntime().exec(cmd);

				// get the error stream of the process and print it
				InputStream error = p.getErrorStream();
				for (int i = 0; i < error.available(); i++) {
					logger.debug("" + error.read());
				}

				exitVal = p.waitFor();
				logger.debug("Exit Value: " + exitVal);
		} catch (Throwable t){
			logger.error(" --------------- Exception Occured   runCommand() ----------------",t);
			StringWriter stack = new StringWriter();
			t.printStackTrace(new PrintWriter(stack));
			logger.debug(stack.toString());
			logger.error(EELFLoggerDelegator.errorLogger,
					" --------------- Exception Occured   runCommand() ----------------", t);
		}
		return exitVal;
	}
	
	public static void main(String[] args){
		String[] cmds = {"cd"," D:/VS00485966/ATT/Cognita/gerritRepository/design-studio/docs/"};
		
		try {
			rmdir(new File("D:/VS00485966/ATT/Cognita/Temp/output/123456/"+"org"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
