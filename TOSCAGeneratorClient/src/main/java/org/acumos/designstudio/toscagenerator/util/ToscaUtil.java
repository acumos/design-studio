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

package org.acumos.designstudio.toscagenerator.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToscaUtil {
	private static final Logger logger = LoggerFactory.getLogger(Properties.class);

	public static void writeDataToFile(String path, String fileName, String extension, String data) {
		logger.debug("-------------- writeDataToFile() started --------------");
		try {
			PrintWriter writer = new PrintWriter(path + fileName + "." + extension, "UTF-8");
			writer.write(data);
			// writer.println("The second line");
			writer.close();
			logger.debug("-------------- writeDataToFile() ended -------------");
		} catch (Exception e) {
			logger.error(" --------------- Exception Occured   writeDataToFile() ----------------" + fileName, e);
			e.printStackTrace();
		}

	}

	public static String readFile(String filePath) throws IOException {
		logger.debug("-----------  readFile() started --------------");
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append("\n");
				line = br.readLine();
			}
			logger.debug("----------- readFile() ended ------------");
			return sb.toString();
		} finally {
			br.close();
		}
	}

	public static String readJSONFile(String filePath) {
		logger.debug("------------- readJSONFile() started -------------");
		JSONParser parser = new JSONParser();
		JSONObject jsonObject = null;
		try {
			Object obj = parser.parse(new FileReader(filePath));
			jsonObject = (JSONObject) obj;
			logger.debug("JSON Object : " + jsonObject.toJSONString());

		} catch (FileNotFoundException e) {
			logger.error(" ---------- Exception Occured  readJSONFile() -----------", e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("----------- Exception Occured readJSONFile() -------------", e);
			e.printStackTrace();
		} catch (org.json.simple.parser.ParseException e) {
			logger.error(" ------------- Exception Occured readJSONFile()-----------", e);
			e.printStackTrace();
		}
		logger.debug("-----------  readJSONFile() ended ------------");
		return jsonObject.toJSONString();
	}

	public static void delete(File file) throws IOException {
		logger.debug("------------  delete() started ------------");
		try {
			if (file.isDirectory()) {

				// directory is empty, then delete it
				if (file.list().length == 0) {

					file.delete();
					logger.debug("--------- Directory is deleted : ----------- " + file.getAbsolutePath());

				} else {

					// list all the directory contents
					String files[] = file.list();

					if (files != null && files.length != 0) {
						for (String temp : files) {
							// construct the file structure
							File fileDelete = new File(file, temp);

							// recursive delete
							delete(fileDelete);
						}
					}

					// check the directory again, if empty then delete it
					if (file.list().length == 0) {
						file.delete();
						logger.debug(" -------Directory is deleted :------------- " + file.getAbsolutePath());
					}
				}

			} else {
				// if file, then delete it
				file.delete();
				logger.debug("---------File is deleted :----------- " + file.getAbsolutePath());
			}
			logger.debug("------------- delete() ended ---------------");
		} catch (Exception ex) {
			logger.error("----------- Exceptoin Occured delete() ---------------", ex);

		}
	}

	public static void main(String[] args) {
		logger.debug("---------- main() started -------------");
		String path = Properties.getTempFolderPath("solution1", "1.1/");

		// writeDataToFile(path,"toscaTesting", "json", "this can be any
		// thing");
		String jsonFilePath = "D:/RP00490596/TechM/JSON/cuda.json";
		// String jsonFielPath =
		// "D:/VS00485966/Acumos/Studio/shushiatt-tosca_server-4b8e0fd4376d/shushiatt-tosca_server-4b8e0fd4376d/cuda.json";
		try {
			readJSONFile(jsonFilePath);
			logger.debug("-------------  main() ended --------------");
		} catch (Exception e) {
			logger.error(" -------------- Exception Occured : --------------", e);
			e.printStackTrace();
		}

	}
}
