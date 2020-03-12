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

package org.acumos.designstudio.toscagenerator.service;

import java.util.Iterator;

import org.acumos.designstudio.toscagenerator.exceptionhandler.AcumosException;
import org.acumos.designstudio.toscagenerator.exceptionhandler.ServiceException;
import org.acumos.designstudio.toscagenerator.util.Properties;
import org.acumos.designstudio.toscagenerator.util.ToscaUtil;
import org.acumos.designstudio.toscagenerator.vo.Artifact;
import org.acumos.designstudio.toscagenerator.vo.tgif.Auxiliary;
import org.acumos.designstudio.toscagenerator.vo.tgif.Call;
import org.acumos.designstudio.toscagenerator.vo.tgif.Parameter;
import org.acumos.designstudio.toscagenerator.vo.tgif.Provide;
import org.acumos.designstudio.toscagenerator.vo.tgif.Request;
import org.acumos.designstudio.toscagenerator.vo.tgif.Response;
import org.acumos.designstudio.toscagenerator.vo.tgif.Self;
import org.acumos.designstudio.toscagenerator.vo.tgif.Service;
import org.acumos.designstudio.toscagenerator.vo.tgif.Stream;
import org.acumos.designstudio.toscagenerator.vo.tgif.Tgif;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * 
 */
public class TgifGeneratorService {

	private static final Logger logger = LoggerFactory.getLogger(TgifGeneratorService.class);

	/**
	 * 
	 * @param solutionID
	 *            solution ID
	 * @param version
	 *            version
	 * @param modelMetaData
	 *            model meta data
	 * @return Artifact
	 * @throws AcumosException
	 *             On failure
	 */
	public Artifact createTgif1(String solutionID, String version, String modelMetaData) throws AcumosException {
		logger.debug("--------  createTgif() Started ------------");
		Artifact result = null;
		String path = Properties.getTempFolderPath(solutionID, version);
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(modelMetaData);
			JSONObject jsonObject = (JSONObject) obj;
			String jsonString = jsonObject.toJSONString();
			ToscaUtil.writeDataToFile(path, "tgif", "json", jsonString);
			result = new Artifact("tgif", "json", solutionID, version, path, jsonString.length());

		} catch (Exception e) {
			logger.error("------------- Exception Occured  createTgif() -------------", e);
			throw new ServiceException(" --------------- Exception Occured decryptAndWriteTofile() --------------",
					Properties.getDecryptionErrorCode(), Properties.getDecryptionErrorDesc(), e.getCause());
		}
		logger.debug("--------  createTgif() End ------------");
		return result;
	}

	/**
	 * To create the TGIF file
	 * @param solutionID
	 *            solution ID
	 * @param version
	 *            version
	 * @param protobuf
	 *            Protobuf spec
	 * @param metaData
	 *            metadata
	 * @return 
	 *    		  the TGIF as JSON
	 * @throws AcumosException
	 *             on Failure
	 */
	public Artifact createTgif(String solutionID, String version, String protobuf, String metaData)
			throws AcumosException {
		logger.debug("--------  createTgif() Started ------------");
		Artifact result = null;
		String path = Properties.getTempFolderPath(solutionID, version);
		JSONParser parser = new JSONParser();
		String jsonString = "";
		try {
			Object obj = parser.parse(metaData.replace("\t", ""));
			JSONObject metaDataJson = (JSONObject) obj;
			obj = parser.parse(protobuf.replace("\t", ""));
			JSONObject protobufJson = (JSONObject) obj;
			@SuppressWarnings("unchecked")
			String solutionName = metaDataJson.getOrDefault("name", "Key not found").toString();
			@SuppressWarnings("unchecked")
			String description = metaDataJson.getOrDefault("description", "").toString();
			Tgif tgif = populateTgif(version, solutionName, description, protobufJson);
			// convert Tgif to json
			ObjectMapper mapper = new ObjectMapper();
			jsonString = mapper.writeValueAsString(tgif);
			jsonString = jsonString.replace("[null]", "[]");
			jsonString = jsonString.replace("null", "{}");
			logger.debug("Generated TGIF.json : " + jsonString);
			ToscaUtil.writeDataToFile(path, "TGIF", "json", jsonString);
			result = new Artifact("TGIF", "json", solutionID, version, path, jsonString.length());

		} catch (Exception e) {
			logger.error("------------- Exception Occured in createTgif() -------------", e);
			logger.error("metaData : " + metaData);
			logger.error("protobuf : " + protobuf);
			logger.error("tgif : " + jsonString);
			throw new ServiceException(
					" --------------- Exception Occurred parsing either metaData or protobuf --------------",
					Properties.getDecryptionErrorCode(), "Error creating TGIF details", e.getCause());
		}
		logger.debug("--------  createTgif() End ------------");
		return result;
	}
	/**
	 * To create TGIF artifact for Solution
	 * 
	 * @param solutionID
	 * 			solution Id
	 * @param version
	 * 			version
	 * @param protobuf
	 * 			Protobuf spec 			
	 * @param solutionName
	 * 			solution name
	 * @param description
	 * 			description
	 * @return
	 * 			Artifact
	 * @throws AcumosException
	 * 			on Failure
	 */
	public Artifact createTgif(String solutionID, String version, String protobuf, String solutionName,String description)
			throws AcumosException {
		logger.debug("--------  createTgif() Started ------------");
		Artifact result = null;
		String path = Properties.getTempFolderPath(solutionID, version);
		JSONParser parser = new JSONParser();
		String jsonString = "";
		try {
			Object obj = parser.parse(protobuf.replace("\t", ""));
			JSONObject protobufJson = (JSONObject) obj;
			Tgif tgif = populateTgif(version, solutionName,description, protobufJson);
			// convert Tgif to json
			ObjectMapper mapper = new ObjectMapper();
			jsonString = mapper.writeValueAsString(tgif);
			jsonString = jsonString.replace("[null]", "[]");
			jsonString = jsonString.replace("null", "{}");
			logger.debug("Generated TGIF.json : " + jsonString);
			ToscaUtil.writeDataToFile(path, "TGIF", "json", jsonString);
			result = new Artifact("TGIF", "json", solutionID, version, path, jsonString.length());

		} catch (Exception e) {
			logger.error("------------- Exception Occured in createTgif() -------------", e);
			logger.error("solutionName : " + solutionName);
			logger.error("protobuf : " + protobuf);
			logger.error("tgif : " + jsonString);
			throw new ServiceException(
					" --------------- Exception Occurred parsing either metaData or protobuf --------------",
					Properties.getDecryptionErrorCode(), "Error creating TGIF details", e.getCause());
		}
		logger.debug("--------  createTgif() End ------------");
		return result;
	}

	/**
	 * 
	 * @param version
	 * @param metaDataJson
	 * @param protobufJson
	 * @return
	 */
	private Tgif populateTgif(String version, String solutionName, String description, JSONObject protobufJson) {
		logger.debug("--------  populateTgif() Begin ------------");

		String COMPONENT_TYPE = "Docker";
		// Set Self
		Self self = new Self(version, solutionName, description, COMPONENT_TYPE);

		// Set empty Stream
		Stream streams = null;

		// Set services
		Call[] scalls = getCalls(protobufJson);
		Provide[] sprovides = getProvides(protobufJson);

		Service services = new Service(scalls, sprovides);

		// Set array of Parameters
		Parameter[] parameters = getParameters(protobufJson);

		// Set Auxilary
		Auxiliary auxiliary = null;

		// Set array of artifacts
		org.acumos.designstudio.toscagenerator.vo.tgif.Artifact[] artifacts = getArtifacts(protobufJson);

		Tgif result = new Tgif(self, streams, services, parameters, auxiliary, artifacts);

		logger.debug("--------  populateTgif() End ------------");
		return result;
	}

	/**
	 * 
	 * @param protobufJson
	 * @return
	 */
	private org.acumos.designstudio.toscagenerator.vo.tgif.Artifact[] getArtifacts(JSONObject protobufJson) {
		org.acumos.designstudio.toscagenerator.vo.tgif.Artifact[] result = new org.acumos.designstudio.toscagenerator.vo.tgif.Artifact[1];

		return result;
	}

	/**
	 * 
	 * @param protobufJson
	 * @return
	 */
	private Parameter[] getParameters(JSONObject protobufJson) {
		Parameter[] result = new Parameter[1];
		return result;
	}

	/**
	 * 
	 * @param protobufJson
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Provide[] getProvides(JSONObject protobufJson) {
		JSONObject service = (JSONObject) protobufJson.get("service");
		JSONArray listOfOperations = (JSONArray) service.get("listOfOperations");
		JSONArray listOfMessages = (JSONArray) protobufJson.get("listOfMessages");

		Provide[] result = new Provide[listOfOperations.size()];

		JSONObject operation = null;
		JSONArray listOfInputMessages = null;
		String operationName = null;
		int operationCnt = 0;

		Iterator<JSONObject> itr = listOfOperations.iterator();
		Iterator<JSONObject> inputMsgItr = null;
		JSONObject inputMessage = null;
		String inputMsgName = null;
		JSONArray inputMsgJsonArray = null;

		Request request = null;
		Response response = null;
		Provide provide = null;
		@SuppressWarnings("unused")
		int inputMsgCnt = 0;

		while (itr.hasNext()) {
			operation = itr.next();
			operationName = operation.get("operationName").toString();
			// Construct the format : for each output Message get the message name and then
			// get collect the message details
			listOfInputMessages = (JSONArray) operation.get("listOfInputMessages");
			inputMsgItr = listOfInputMessages.iterator();
			inputMsgCnt = 0;
			inputMsgJsonArray = new JSONArray();
			while (inputMsgItr.hasNext()) {
				inputMessage = (JSONObject) inputMsgItr.next();
				inputMsgName = (String) inputMessage.get("inputMessageName");
				inputMsgJsonArray.add(getMsgJson(inputMsgName, listOfMessages));
			}
			request = new Request(inputMsgJsonArray, "");
			response = new Response(new JSONArray(), "");
			provide = new Provide(operationName, request, response);
			result[operationCnt] = provide;
			operationCnt++;
		}

		return result;
	}

	/**
	 * 
	 * @param protobufJson
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Call[] getCalls(JSONObject protobufJson) {

		JSONObject service = (JSONObject) protobufJson.get("service");
		JSONArray listOfOperations = (JSONArray) service.get("listOfOperations");
		JSONArray listOfMessages = (JSONArray) protobufJson.get("listOfMessages");

		Call[] result = new Call[listOfOperations.size()];

		JSONObject operation = null;

		JSONArray listOfOutputMessages = null;

		String operationName = null;
		int operationCnt = 0;

		Iterator<JSONObject> itr = listOfOperations.iterator();
		Iterator<JSONObject> outputMsgItr = null;
		JSONObject outputMessage = null;
		String outputMsgName = null;
		JSONArray outputMsgJsonArray = null;

		Request request = null;
		Response response = null;
		Call call = null;
		@SuppressWarnings("unused")
		int outputMsgCnt = 0;

		while (itr.hasNext()) {
			operation = itr.next();
			operationName = operation.get("operationName").toString();
			// Construct the format : for each output Message get the message name and then
			// get collect the message details
			listOfOutputMessages = (JSONArray) operation.get("listOfOutputMessages");
			outputMsgJsonArray = new JSONArray();
			outputMsgItr = listOfOutputMessages.iterator();
			outputMsgCnt = 0;
			while (outputMsgItr.hasNext()) {
				outputMessage = (JSONObject) outputMsgItr.next();
				outputMsgName = (String) outputMessage.get("outPutMessageName");
				outputMsgJsonArray.add(getMsgJson(outputMsgName, listOfMessages));
			}
			request = new Request(outputMsgJsonArray, "");
			response = new Response(new JSONArray(), "");
			call = new Call(operationName, request, response);
			result[operationCnt] = call;
			operationCnt++;
		}
		return result;
	}

	/**
	 * 
	 * @param msgName
	 * @param listOfMessages
	 * @return
	 */
	private JSONObject getMsgJson(String msgName, JSONArray listOfMessages) {
		@SuppressWarnings("unchecked")
		Iterator<JSONObject> itr = listOfMessages.iterator();
		JSONObject message = null;
		String messageName = null;
		while (itr.hasNext()) {
			message = (JSONObject) itr.next();
			messageName = (String) message.get("messageName");
			if (messageName.equals(msgName)) {
				logger.debug("message : " + message.toJSONString());
				break;
			}
		}
		return message;
	}
}
