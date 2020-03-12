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

/**
 * 
 */
package org.acumos.designstudio.toscagenerator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.acumos.designstudio.toscagenerator.exceptionhandler.AcumosException;
import org.acumos.designstudio.toscagenerator.exceptionhandler.ControllerException;
import org.acumos.designstudio.toscagenerator.service.ProtobufGeneratorService;
import org.acumos.designstudio.toscagenerator.service.TgifGeneratorService;
import org.acumos.designstudio.toscagenerator.service.ToscaGeneratorService;
import org.acumos.designstudio.toscagenerator.util.ConfigurationProperties;
import org.acumos.designstudio.toscagenerator.util.Properties;
import org.acumos.designstudio.toscagenerator.util.ToscaUtil;
import org.acumos.designstudio.toscagenerator.vo.Artifact;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is the main controller for the TOSCA Model Generator Client
 */
public class ToscaGeneratorClient {
	private static final Logger logger = LoggerFactory.getLogger(ToscaGeneratorClient.class);

	ToscaGeneratorService service = null;
	ProtobufGeneratorService protoService = null;
	TgifGeneratorService tgifService = null;

	/**
	 * 
	 * @param jsonstr
	 *            JSON string
	 */
	public ToscaGeneratorClient(String jsonstr) {
		service = new ToscaGeneratorService();
		protoService = new ProtobufGeneratorService();
		tgifService = new TgifGeneratorService();
		JSONParser parser = new JSONParser();
		try {
			JSONObject config = (JSONObject) parser.parse(jsonstr);
			ConfigurationProperties.init(config.get("toscaOutputFolder").toString(),
					config.get("toscaGeneratorEndPointURL").toString(), config.get("nexusEndPointURL").toString(),
					config.get("nexusUserName").toString(), config.get("nexusPassword").toString(),
					config.get("nexusGroupId").toString(), config.get("cmnDataSvcEndPoinURL").toString(),
					config.get("cmnDataSvcUser").toString(), config.get("cmnDataSvcPwd").toString());
		} catch (ParseException e) {
			logger.error("Exception in ToscaGeneratorClient()", e);
		}
	}

	/**
	 * This construct accepts the configuration passed as a parameter.
	 * 
	 * @param toscaOutputFolder
	 *            Output folder
	 * @param toscaGeneratorEndPointURL
	 *            URL
	 * @param nexusEndPointURL
	 *            URL
	 * @param nexusUserName
	 *            user name
	 * @param nexusPassword
	 *            password
	 * @param nexusGroupId
	 *            group ID
	 * @param cmnDataSvcEndPoinURL
	 *            URL
	 * @param cmnDataSvcUser
	 *            user name
	 * @param cmnDataSvcPwd
	 *            password
	 */
	public ToscaGeneratorClient(String toscaOutputFolder, String toscaGeneratorEndPointURL, String nexusEndPointURL,
			String nexusUserName, String nexusPassword, String nexusGroupId, String cmnDataSvcEndPoinURL,
			String cmnDataSvcUser, String cmnDataSvcPwd) {
		service = new ToscaGeneratorService();
		protoService = new ProtobufGeneratorService();
		tgifService = new TgifGeneratorService();
		ConfigurationProperties.init(toscaOutputFolder, toscaGeneratorEndPointURL, nexusEndPointURL, nexusUserName,
				nexusPassword, nexusGroupId, cmnDataSvcEndPoinURL, cmnDataSvcUser, cmnDataSvcPwd);
	}


	/**
	 * This method is to generate the TOSCA files and update the SolutionRevision accordingly.
	 * 
	 * @param ownerID
	 *            owner ID
	 * @param solutionID
	 *            solution ID
	 * @param version
	 *            version
	 * @param solutionRevisionID
	 *            revision ID
	 * @param localProtobufFile
	 *            protobuf file
	 * @param localMetadataFile
	 *            metadata file
	 * @return 
	 *            json response as string
	 * @throws AcumosException
	 *            On failure
	 */
	public String generateTOSCA(String ownerID, String solutionID, String version, String solutionRevisionID,
			File localProtobufFile, File localMetadataFile) throws AcumosException {

		logger.info(" generateTOSCA() : Begin");
		String result = null;
		String success = "{solutionID:\"%s\",version:\"%s\"}";
		String error = "{errorCode : \"%s\", errorDescription : \"%s\"}";
		List<Artifact> toscaFiles = null;
		try {
			if (ownerID != null && solutionID != null && !solutionID.trim().isEmpty() && localProtobufFile != null
					&& localMetadataFile != null && version != null && !version.trim().isEmpty()) {
				toscaFiles = new ArrayList<Artifact>();
				// Define the switch to enable/disable the invocation of TOSCA Python Server.
				// 1. Integrate TOSCA Model Generator Python Web Service & 2. process the response.
				// 2. Decrypt the content of each file using Base64 &
				// 3. Store the decrypted content into corresponding file and store the file at configured location
				// Include the .proto file in the toscaFiles to be uploaded : Already uploaded by On Boarding.
				String path = Properties.getTempFolderPath(solutionID, version);

				// Generate Protobuf.json from protoData
				String protoJsonStr = protoService.createProtoJson(solutionID, version, localProtobufFile);
				ToscaUtil.writeDataToFile(path, "PROTOBUF", "json", protoJsonStr);
				Artifact protoJson = new Artifact("PROTOBUF", "json", solutionID, version, path, protoJsonStr.length());
				toscaFiles.add(protoJson);

				String metaData = ToscaUtil.readJSONFile(localMetadataFile.getCanonicalPath());
				// Create the tgif.json file and add it to toscaFiles list
				Artifact tgif = tgifService.createTgif(solutionID, version, protoJsonStr, metaData);
				toscaFiles.add(tgif);

				// 4. Invoke the library to store the files in Nexus :
				service.uploadFilesToRepository(solutionID, version, toscaFiles);

				logger.debug(" After uploading in Nexus ");
				if (toscaFiles != null && !toscaFiles.isEmpty()) {
					for (Artifact artifact : toscaFiles) {
						logger.debug("SolutionID :" + artifact.getSolutionID());
						logger.debug("version :" + artifact.getVersion());
						logger.debug("ArtifactType : " + artifact.getType());
						logger.debug("ArtifactType : " + artifact.getPayloadURI());
						logger.debug(artifact.getNexusURI());
					}
				}
				// 5. Invoke the Common Data Microservice putArtifact
				service.postArtifact(solutionID, solutionRevisionID, ownerID, toscaFiles);
				result = String.format(success, solutionID, version);
			} else {
				result = String.format(error, Properties.getMetaDataErrorCode(), Properties.getMetaDataErrorDesc());
			}
		} catch (AcumosException e) {
			logger.error("Exception in  TOSCA Model Generator Client", e);
			result = String.format(error, e.getErrorCode(), e.getErrorDesc());

		} catch (Exception e) {
			logger.error(" Exception in  TOSCA Model Generator Client ", e);
			result = String.format(error, Properties.getTOSCAFileGenerationErrorCode(),
					Properties.getTOSCAFileGenerationErrorDesc(), Properties.getTOSCAFileGenerationErrorDesc());
			throw new ControllerException(e.getMessage(), Properties.getTOSCAFileGenerationErrorCode(),
					Properties.getTOSCAFileGenerationErrorDesc(), e);

		} finally {
			//6. Delete the TOSCA File from payloadPath
			deleteTOSCAFiles(solutionID, version);
		}
		// 7. Construct the return JSON and send the response"
		logger.info("generateTOSCA() : End");
		return result;
	}
	
	/**
	 * This method is to generate the TOSCA files accepting Solution Name and Description accordingly.
	 * 
	 * @param ownerID
	 *            owner ID
	 * @param solutionID
	 *            solution ID
	 * @param version
	 *            version string
	 * @param solutionRevisionID
	 *            revision ID
	 * @param localProtobufFile
	 *            protobuf file
	 * @param solutionName
	 *            solutionName 
	 * @param description
	 *            description
	 * @return 
         *            json response as string
	 * @throws AcumosException
	 *            On failure
	 */
	public String generateTOSCA(String ownerID, String solutionID, String version, String solutionRevisionID,
			File localProtobufFile, String solutionName, String description) throws AcumosException {

		logger.info(" generateTOSCA() : Begin");
		String result = null;
		String success = "{solutionID:\"%s\",version:\"%s\"}";
		String error = "{errorCode : \"%s\", errorDescription : \"%s\"}";
		List<Artifact> toscaFiles = null;
		try {
			if (ownerID != null && solutionID != null && !solutionID.trim().isEmpty() && localProtobufFile != null
					&& solutionName != null && version != null && !version.trim().isEmpty()) {

				toscaFiles = new ArrayList<Artifact>();

				// Define the switch to enable/disable the invocation of TSOCA Python Server.
				// 1. Integrate TOSCA Model Generator Python Web Service & 2. process the response.
				// 2. Decrypt the content of each file using Base64 &
				// 3. Store the decrypted content into corresponding file and store the file at configured location
				// Include the .proto file in the toscaFiles to be uploaded : Already uploaded by On Boarding.
				String path = Properties.getTempFolderPath(solutionID, version);

				// Generate Protobuf.json from protoData
				String protoJsonStr = protoService.createProtoJson(solutionID, version, localProtobufFile);
				ToscaUtil.writeDataToFile(path, "PROTOBUF", "json", protoJsonStr);
				Artifact protoJson = new Artifact("PROTOBUF", "json", solutionID, version, path, protoJsonStr.length());
				toscaFiles.add(protoJson);

				//4.  Create the tgif.json file and add it to toscaFiles list
				Artifact tgif = tgifService.createTgif(solutionID, version, protoJsonStr, solutionName, description);
				toscaFiles.add(tgif);
				// 5. Invoke the library to store the files in Nexus :
				service.uploadFilesToRepository(solutionID, version, toscaFiles);

				logger.debug(" After uploading in Nexus ");
				if (toscaFiles != null && !toscaFiles.isEmpty()) {
					for (Artifact artifact : toscaFiles) {
						logger.debug("SolutionID :" + artifact.getSolutionID());
						logger.debug("version :" + artifact.getVersion());
						logger.debug("ArtifactType : " + artifact.getType());
						logger.debug("ArtifactType : " + artifact.getPayloadURI());
						logger.debug(artifact.getNexusURI());
					}
				}
				// 6. Invoke the Common Data Microservice putArtifact
				service.postArtifact(solutionID, solutionRevisionID, ownerID, toscaFiles);
				result = String.format(success, solutionID, version);
			} else {
				result = String.format(error, Properties.getMetaDataErrorCode(), Properties.getMetaDataErrorDesc());
			}
		} catch (AcumosException e) {
			logger.error(" Exception in  TOSCA Model Generator Client ", e);
			result = String.format(error, e.getErrorCode(), e.getErrorDesc());
		} catch (Exception e) {
			logger.error(" Exception in  TOSCA Model Generator Client ", e);
			result = String.format(error, Properties.getTOSCAFileGenerationErrorCode(),
					Properties.getTOSCAFileGenerationErrorDesc(), Properties.getTOSCAFileGenerationErrorDesc());
			throw new ControllerException(e.getMessage(), Properties.getTOSCAFileGenerationErrorCode(),
					Properties.getTOSCAFileGenerationErrorDesc(), e);
		} finally {
			// Delete the TOSCA File from payloadPath
			deleteTOSCAFiles(solutionID, version);
		}
		// 7. Construct the return JSON and send the response"
		logger.info(" generateTOSCA() : End");
		return result;
	}

	/**
	 * 
	 * @param solutionID
	 * @param version
	 * @throws AcumosException
	 */
	private static void deleteTOSCAFiles(String solutionID, String version) throws AcumosException {
		logger.info("--------  deleteTOSCAFiles() Started ------------");

		String path = Properties.getTempFolderPath(solutionID, version);
		File directory = new File(path);

		// make sure directory exists
		if (!directory.exists()) {
			logger.debug("----------- Directory does not exist. ----------");
		} else {
			try {
				ToscaUtil.delete(directory);
				logger.info("----------- deleteTOSCAFiles() Ended ----------------");
			} catch (Exception e) {
				logger.error("--------- Exception deleteTOSCAFiles() -------------", e);
				throw new ControllerException(e.getMessage(), Properties.getFileDeletionErrorCode(),
						Properties.getFileDeletionErrorDesc(), e);
			}
		}

	}
}
