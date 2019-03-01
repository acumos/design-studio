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
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.invoke.MethodHandles;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 *
 *
 */
public class DSUtil {
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	/**
	 * 
	 */
	public DSUtil() {
		super();
	}

	/**
	 * 
	 * @param filePath
	 *            Path of file to read
	 * @return Contents of file
	 * @throws IOException
	 *             On failure to read
	 */
	public static String readFile(String filePath) throws IOException {
		logger.debug("readFile() started ");
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
			logger.debug("readFile() ended ");
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
	 */
	public static void writeDataToFile(String path, String fileName, String extension, String data) {
		logger.debug("writeDataToFile() started ");
		PrintWriter writer = null;
		String completeFileName = path + fileName;
		if (null != extension && !extension.trim().equals("")) {
			completeFileName = completeFileName + "." + extension;
		}
		try {
			writer = new PrintWriter(completeFileName, "UTF-8");
			writer.write(data);

			logger.debug("writeDataToFile() ended ");
		} catch (Exception e) {
			logger.error("Exception Occured   writeDataToFile() {} " , fileName, e);
		} finally {
			if (null != writer) {
				writer.close();
			}
		}
	}

	/**
	 * 
	 * @param filePath
	 * File path to parse
	 * @return Boolean
	 */
	public static boolean isValidJsonSchema(String filePath) {
		try {
			String josnString = readFile(filePath);
			JSONObject rawSchema = null;
			try {
				rawSchema = new JSONObject(new JSONTokener(josnString));
				Schema schema = SchemaLoader.load(rawSchema);
				schema.validate(new JSONObject(josnString)); // throws a ValidationException if this object is invalid
			} catch (JSONException e) {
				logger.error("Exception Occured   isValidJsonSchema() ", e);
				return false;
			}
		} catch (IOException e) {
			logger.error("Exception Occured   isValidJsonSchema() ", e);
		} catch (Exception ex) {
			logger.error("Exception Occured   isValidJsonSchema() ", ex);
		}
		return true;
	}

	/**
	 * 
	 * @param userId
	 * User ID
	 * @param path
	 * Path
	 * @return Path
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
	 * User ID
	 * @param path
	 * Path
	 * @return Path
	 */
	public static String readCdumpPath(String userId, String path) {
		// Upload the file to Nexus for the solution.
		// construct the user specific path
		String result = path + userId + "/";
		return result;
	}

	/**
	 * 
	 * @param path File path to delete
	 */
	public static void deleteFile(String path) {
		File file = new File(path);
		boolean deleted = false;
		if (file.exists()) {
			deleted = file.delete();
			if (deleted) {
				logger.debug("file deleted successfully");
			}
		}
	}

	/**
	 * 
	 * @param folder Folder to remove
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
						logger.debug("temp folder removed");
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param path Directory to remove
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
	 * String to parse as JSON
	 * @param capabilities 
	 * String to parse as JSON
	 * @return requirements, capabilities
	 */
	public static String isValidJsonSchemaContents_List(String requirements, String capabilities) {
		StringBuffer sb = new StringBuffer("");
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.readTree(requirements);
		} catch (Exception ex) {
			logger.error("Exception Occured   isValidJsonSchemaContents_List() ", ex);
			sb.append("requirements ");
		}
		try {
			mapper.readTree(capabilities);
		} catch (Exception ex) {
			logger.error("Exception Occured   isValidJsonSchemaContents_List() ", ex);
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
			logger.error("Exception Occured   isValidJsonaddLink() ", ex);
			err = "relationship";
		}
		return err;
	}

	/**
	 * 
	 * @param jsonInString String 
	 * @return Boolean if argument is valid JSON
	 */
	public static boolean isValidJSON(String jsonInString) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			mapper.readTree(jsonInString);
			return true;
		} catch (IOException e) {
			logger.error("Exception Occured   isValidJsonaddLink() ", e);
			return false;
		}
	}

	/**
	 * 
	 * @param cmd
	 * Command to run
	 * @return
	 * Exit code
	 * @throws Exception On failure
	 */
	public static int runCommand(String cmd) throws Exception {
		logger.debug("Exec:  {} ", cmd);
		Process p = null;
		int exitVal = 1;
		try {
			p = Runtime.getRuntime().exec(cmd);

			// get the error stream of the process and print it
			InputStream error = p.getErrorStream();
			for (int i = 0; i < error.available(); i++) {
				logger.debug("{} ", error.read());
			}

			exitVal = p.waitFor();
			logger.debug("Exit Value:  {} ", exitVal);
		} catch (Throwable t) {
			logger.error("Exception Occured   runCommand() ", t);
			StringWriter stack = new StringWriter();
			t.printStackTrace(new PrintWriter(stack));
			logger.info(stack.toString());
			logger.error("Exception Occured   runCommand() ", t);
		}
		return exitVal;
	}
	
}
