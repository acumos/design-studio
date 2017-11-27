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

import org.acumos.designstudio.toscagenerator.exceptionhandler.ControllerException;
import org.acumos.designstudio.toscagenerator.exceptionhandler.CustomException;
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
import org.jsoup.parser.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  This class is the main controller for the TOSCA Model
 *         Generator Client
 */
public class ToscaGeneratorClient {
	private static final Logger logger = LoggerFactory.getLogger(ToscaGeneratorClient.class);
	
	ToscaGeneratorService service = null;
	ProtobufGeneratorService protoService = null;
	TgifGeneratorService tgifService = null; 
	
	/*public ToscaGeneratorClient(){
		service = new ToscaGeneratorService();
		protoService = new ProtobufGeneratorService();
		tgifService = new TgifGeneratorService();
	}*/
	
	/**
	 * This constructor accept the configuration in JSON Format as below : 
	 * {"toscaOutputFolder":"", "toscaGeneratorEndPointURL":"", "nexusEndPointURL":"", "nexusUserName":"", "nexusPassword":"", "nexusGroupId":"", "cmnDataSvcEndPoinURL":"", "cmnDataSvcUser":"", "cmnDataSvcPwd":""}
	 * @param jsonstr
	 */
	public ToscaGeneratorClient(String jsonstr){
		service = new ToscaGeneratorService();
		protoService = new ProtobufGeneratorService();
		tgifService = new TgifGeneratorService();
		JSONParser parser = new JSONParser();
		try {
			JSONObject config = (JSONObject) parser.parse(jsonstr);
			ConfigurationProperties.init(config.get("toscaOutputFolder").toString(), config.get("toscaGeneratorEndPointURL").toString(), config.get("nexusEndPointURL").toString(), config.get("nexusUserName").toString(), config.get("nexusPassword").toString(), config.get("nexusGroupId").toString(), config.get("cmnDataSvcEndPoinURL").toString(), config.get("cmnDataSvcUser").toString(), config.get("cmnDataSvcPwd").toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * This construct accept the configuration passed as a parameter.
	 * @param jsonstr
	 * @param j
	 */
	public ToscaGeneratorClient(String toscaOutputFolder, String toscaGeneratorEndPointURL, String nexusEndPointURL,
			String nexusUserName, String nexusPassword, String nexusGroupId, String cmnDataSvcEndPoinURL,
			String cmnDataSvcUser, String cmnDataSvcPwd){
		service = new ToscaGeneratorService();
		protoService = new ProtobufGeneratorService();
		tgifService = new TgifGeneratorService();
		ConfigurationProperties.init(toscaOutputFolder, toscaGeneratorEndPointURL, nexusEndPointURL, nexusUserName, nexusPassword, nexusGroupId, cmnDataSvcEndPoinURL, cmnDataSvcUser, cmnDataSvcPwd);
	}
	

	/**
	 * This method is to Generat the TOSCA files and update the SolutionRevision accordingly. 
	 * 
	 * @param ownerID
	 * @param solutionID
	 * @param version
	 * @param solutionRevisionID
	 * @param localProtobufFile
	 * @param localMetadataFile
	 * @return on success {solutionID:"solutionv id value",version:"version value"}
	 * @throws CustomException
	 */
	public String generateTOSCA(String ownerID, String solutionID, String version, String solutionRevisionID, File localProtobufFile, File localMetadataFile )
			throws CustomException {

		logger.info("-------------- generateTOSCA() ----- : Begin");
		String result = null;
		String success = "{solutionID:\"%s\",version:\"%s\"}";
		String error = "{errorCode : \"%s\", errorDescription : \"%s\"}";
		

		String response = null;
		List<Artifact> toscaFiles = null;
		try {
/*			if (ownerID != null && solutionID != null && !solutionID.trim().isEmpty() && metaData != null
					&& !metaData.trim().isEmpty() && version != null && !version.trim().isEmpty()) {*/
			if (ownerID != null && solutionID != null && !solutionID.trim().isEmpty() && localProtobufFile != null
					&& localMetadataFile != null && version != null && !version.trim().isEmpty()) {
				// create the file for modelMetaData 
				// ToscaUtil.writeDataToFile(path,
				// Properties.getMetaDataFileName(),
				// "json", modelMetaData);

				toscaFiles = new ArrayList<Artifact>();
				
				
				//TODO : Define the switch to enable/disable the invocation of TSOCA Python Server. 
				// 1. Integrate TOSCA Model Generator Python Web Service & 2.
				// process the response.
				//Disabling the call to TOSCA Generator.
				//response = service.getToscaModels(metaData);

				// 2. Decrypt the content of each file using Base64 &
				// 3. Store the decrypted content into corresponding file and
				// store
				// the file at configured location
				//Disabling the TOSCA file generation
				//toscaFiles.addAll(service.decryptAndWriteTofile(solutionID, version, response));
				
				
				//Include the .proto file in the toscaFiles to be uploaded : Already uploaded by On Boarding.
				String path = Properties.getTempFolderPath(solutionID, version);
				/*ToscaUtil.writeDataToFile(path, "model", "proto", new String(protoData));
				Artifact proto = new Artifact("model", "proto", solutionID, version, path, protoData.length());
				toscaFiles.add(proto);*/
				
			
				//Generate Protobuf.json from protoData
				String protoJsonStr = protoService.createProtoJson(solutionID, version, localProtobufFile);
				ToscaUtil.writeDataToFile(path, "PROTOBUF", "json", protoJsonStr);
				Artifact protoJson = new Artifact("PROTOBUF", "json", solutionID, version, path, protoJsonStr.length());
				toscaFiles.add(protoJson);
				
				
				String metaData = ToscaUtil.readJSONFile(localMetadataFile.getCanonicalPath());
				//Create the tgif.json file and add it to toscaFiles list
				Artifact tgif = tgifService.createTgif(solutionID, version, protoJsonStr, metaData);
				toscaFiles.add(tgif);
				
				// 5. Invoke the library to store the files in Nexus :
				service.uploadFilesToRepository(solutionID, version, toscaFiles);

				// Testing -- Begin
				logger.debug("-------- After uploading in Nexus ----------");
				if (toscaFiles != null && !toscaFiles.isEmpty()) {
					for (Artifact artifact : toscaFiles) {
						logger.debug("SolutionID :" + artifact.getSolutionID());
						logger.debug("version :" + artifact.getVersion());
						logger.debug("ArtifactType : " + artifact.getType());
						logger.debug("ArtifactType : " + artifact.getPayloadURI());
						logger.debug(artifact.getNexusURI());
					}
				}
				// Testing -- End

				// 6. Invoke the Common Data Microservice putArtifact
				// http://localhost:8090/putArtifact?solutionID=mysolution&version=1.1&artifactType=TOSCATEMPLATE&payloadURI=URI1
				service.postArtifact(solutionID, solutionRevisionID, ownerID, toscaFiles);

				
				result = String.format(success, solutionID, version);
			} else {
				result = String.format(error, Properties.getMetaDataErrorCode(), Properties.getMetaDataErrorDesc());

			}
			logger.info("--------------generateTOSCA() ----- : End");
		} catch (CustomException e) {
			logger.error("--------- Exception in  TOSCA Model Generator Client -----------", e);
			e.printStackTrace();
			result = String.format(error, e.getErrorCode(), e.getErrorDesc());

		} catch (Exception e) {
			result = String.format(error, Properties.getTOSCAFileGenerationErrorCode(),
					Properties.getTOSCAFileGenerationErrorDesc(), Properties.getTOSCAFileGenerationErrorDesc());
			throw new ControllerException(e.getMessage(), Properties.getTOSCAFileGenerationErrorCode(),
					Properties.getTOSCAFileGenerationErrorDesc(), e);
		} finally {
			// Delete the TOSCA File from payloadPath
			deleteTOSCAFiles(solutionID, version);
		}
		// 7. Construct the return JSON and send the response"
		return result;
	}


	private static void deleteTOSCAFiles(String solutionID, String version) throws CustomException {
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
				e.printStackTrace();
				throw new ControllerException(e.getMessage(), Properties.getFileDeletionErrorCode(),
						Properties.getFileDeletionErrorDesc(), e);
			}
		}

	}
	
public static void main(String[] args){
		


		String metaDataFile = "D:/RP00490596/TechM/Acumos/Kazi/30102017/aggregator-metadata.json";
		String protoDataFile = "D:/RP00490596/TechM/Acumos/Kazi/30102017/aggregator-proto.proto";
		String userId = "1234"; 
		String solutionID = "34ERT3566d";
		String version = "1.0.1";
		String solutionRevisionID = "234567iekslke1";
		System.out.println("argument : " + args);
		
			System.out.println("****************** Reassigning the value received from Command Prompt **********************");
			//userId = args[1];
			//solutionID = args[2];
			//version = args[3];
			//solutionRevisionID = args[4];
			//protoDataFile = args[5];
			//metaDataFile = args[6];
		
		System.out.println("User :" + userId);
		System.out.println("solutionID : " + solutionID);
		System.out.println("version : " + version);
		System.out.println("solutionRevisionID : " + solutionRevisionID);
		System.out.println("Proto Buf Path : " + protoDataFile);
		System.out.println("Meta Data file Path : " + metaDataFile);
		
		String result = null;
		String modelMetaData = null;
		String protoData = null;
		
		String toscaOutputFolder = "/D:/RP00490596/TechM/Acumos/Payload/";
		String toscaGeneratorEndPointURL = "http://acumos-dev1-vm01-core:8080/model_create";
		String nexusEndPointURL = "http://acumos_model_rw:not4you@acumos-nexus01:8081/repository/repo_acumos_model_maven/";
		String nexusUserName = "acumos_model_rw";
		String nexusPassword = "not4you";
		String nexusGroupId = "com.artifact";
		String cmnDataSvcEndPoinURL = "http://acumos-dev1-vm01-core:8000/ccds";
		String cmnDataSvcUser = "ccds_client";
		String cmnDataSvcPwd = "ccds_client";
		
		ToscaGeneratorClient client = new ToscaGeneratorClient(toscaOutputFolder,  toscaGeneratorEndPointURL,  nexusEndPointURL,
				 nexusUserName,  nexusPassword,  nexusGroupId,  cmnDataSvcEndPoinURL,
				 cmnDataSvcUser,  cmnDataSvcPwd);
		
		//ToscaGeneratorClient client = new ToscaGeneratorClient(" {\"toscaOutputFolder\":\"D:/VS00485966/Acumos/Studio/Sprint4/Protobuf_files/\", \"toscaGeneratorEndPointURL\":\"\", \"nexusEndPointURL\":\"http://acumos_model_rw:not4you@acumos-nexus01:8081/repository/repo_acumos_model_maven/\", \"nexusUserName\":\"acumos_model_rw\", \"nexusPassword\":\"not4you\", \"nexusGroupId\":\"com.artifact\", \"cmnDataSvcEndPoinURL\":\"\", \"cmnDataSvcUser\":\"\", \"cmnDataSvcPwd\":\"\"}");
		try {
			//modelMetaData = ToscaUtil.readJSONFile(jsonFilePath);
			File localMetaDataFile = new File(metaDataFile);
			File localProtoFile = new File(protoDataFile);
			result = client.generateTOSCA(userId, solutionID, version,solutionRevisionID,localProtoFile, localMetaDataFile);
			//deleteTOSCAFiles("solution11.1", "1.1");
			System.out.println("result : " + result);
		} catch (Exception ex) {
			System.out.println(" ------------ Exception Occured  generateTOSCA() ----------- " + ex);

		}
		System.out.println(" ------ DONE -------- " + result);
	
		
		

	}

}
