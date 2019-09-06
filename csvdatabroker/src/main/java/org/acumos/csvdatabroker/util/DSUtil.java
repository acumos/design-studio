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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

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
	 * @param filePath
	 *            Path of file to read
	 * @return Contents of file
	 * 			This method returns Contents of file 
	 * @throws IOException
	 *             In case of any exception, this method throws the IOException
	 */
	public static String readFile(String filePath) throws IOException {
		logger.debug(EELFLoggerDelegator.debugLogger, "  readFile() started ");
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
			logger.debug(EELFLoggerDelegator.debugLogger, " readFile() ended ");
			return sb.toString();
		} finally {
			fr.close();
			br.close();
		}
	}

	/**
	 * 
	 * @param path
	 *            Directory path
	 * @param fileName
	 *            File name
	 * @param extension
	 *            File extension
	 * @param data
	 *            Data to write
	 * @throws IOException
	 * 			Throws IOException while writing data.
	 */
	public static void writeDataToFile(String path, String fileName, String extension, String data) throws IOException {
		logger.debug(EELFLoggerDelegator.debugLogger, " writeDataToFile() started ");
		FileWriter fileWriter = null;
		BufferedWriter bufferedWriter = null;
		
		String completeFileName = path + fileName;
		if (null != extension && !extension.trim().equals("")) {
			completeFileName = completeFileName + "." + extension;
		}
		try {
			fileWriter = new FileWriter(completeFileName);
			bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(data);

			logger.debug(EELFLoggerDelegator.debugLogger, " writeDataToFile() ended ");
		} finally {
			
			if (null != bufferedWriter) {
				bufferedWriter.close();
			}
			
			if (null != fileWriter) {
				fileWriter.close();
			}
		}
	}

	/**
	 * 
	 * @param filePath
	 * 		File path to parse
	 * @return Boolean
	 * 		This method returns boolean value
	 * @throws IOException 
	 * 		Throws IOException while writing data.
	 */
	public static boolean isValidJsonSchema(String filePath) throws IOException {
		
			String jsonString = readFile(filePath);
			JSONObject rawSchema = null;
			try {
				rawSchema = new JSONObject(new JSONTokener(jsonString));
				Schema schema = SchemaLoader.load(rawSchema);
				schema.validate(new JSONObject(jsonString)); // throws a ValidationException if this object is invalid
				return true;
			} catch (JSONException e) {
				logger.error(EELFLoggerDelegator.errorLogger,
						"Exception Occured in  isValidJsonSchema() ", e);
				return false;
			}
	}

	/**
	 * 
	 * @param userId
	 * 			This method accepts UserId
	 * @param path
	 * 			This method accepts path
	 * @return Path
	 * 			This method returns path
	 */
	public static String createCdumpPath(String userId, String path) {
		File parentfile = new File(path);
		if (!parentfile.exists()) {
			parentfile.mkdir(); // create parent folder if it does not exists
		}
		File subFile = new File(parentfile, userId);
		subFile.mkdir(); // create the subfolder for the userId.
		String result = subFile.getPath() + "/";
		return result;
	}

	/**
	 * 
	 * @param userId
	 * 			This method accepts UserId
	 * @param path
	 * 			This method accepts path
	 * @return Path
	 * 			This method returns path
	 */
	public static String readCdumpPath(String userId, String path) {
		// construct the user specific path
		String result = path + userId + "/";
		return result;
	}

	/**
	 * 
	 * @param path File path to delete
	 * 		This method accepts path
	 */
	public static void deleteFile(String path) {
		File file = new File(path);
		boolean deleted = false;
		if (file.exists()) {
			deleted = file.delete();
			if (deleted) {
				logger.debug(EELFLoggerDelegator.debugLogger, "file deleted successfully");
			}
		}
	}

	/**
	 * 
	 * @param folder Folder to remove
	 * 			This method accepts folder to remove
	 * 		
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
					if (fRemoved) {
						logger.debug(EELFLoggerDelegator.debugLogger, "temp folder removed");
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param path Directory to remove
	 * 		This method accepts path
	 */
	public static void rmUserdir(String path) {
		File parent = new File(path);
		File[] files = parent.listFiles();
		if (files.length == 0) {
			parent.delete();
		}
	}

	/**
	 * 
	 * @param requirements
	 * 				String to parse as JSON
	 * @param capabilities 
	 * 				String to parse as JSON
	 * @return requirements, capabilities
	 * 				This method returns requirements, capabilities
	 * 				
	 */
	public static String isValidJsonSchemaContents_List(String requirements, String capabilities) {
		StringBuffer sb = new StringBuffer("");
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.readTree(requirements);
		} catch (Exception ex) {
			logger.error(EELFLoggerDelegator.errorLogger,
					" Exception Occured   isValidJsonSchemaContents_List() ", ex);
			sb.append("requirements ");
		}
		try {
			mapper.readTree(capabilities);
		} catch (Exception ex) {
			logger.error(EELFLoggerDelegator.errorLogger,
					" Exception Occured   isValidJsonSchemaContents_List() ", ex);
			sb.append("capabilities");
		}
		return sb.toString();
	}

	/**
	 * 
	 * @param relationship String to parse as JSON
	 * @return Relationship
	 */
	public static String isValidJsonaddLink(String relationship) {
		String err = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.readTree(relationship);
		} catch (Exception ex) {
			logger.error(EELFLoggerDelegator.errorLogger,
					" Exception Occured   isValidJsonaddLink() ", ex);
			err = "relationship";
		}
		return err;
	}

	/**
	 * 
	 * @param jsonInString String 
	 * 			This method accepts JSON as String format
	 * @return Boolean if argument is valid JSON
	 * 			This method returns boolean value
	 */
	public static boolean isValidJSON(String jsonInString) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			mapper.readTree(jsonInString);
			return true;
		} catch (IOException e) {
			logger.error(EELFLoggerDelegator.errorLogger,
					" Exception Occured   isValidJsonaddLink() ", e);
			return false;
		}
	}

}
