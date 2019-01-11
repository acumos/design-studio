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

package org.acumos.designstudio.ce.service;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.acumos.cds.client.CommonDataServiceRestClientImpl;
import org.acumos.cds.domain.MLPArtifact;
import org.acumos.cds.domain.MLPSolutionRevision;
import org.acumos.designstudio.ce.exceptionhandler.ServiceException;
import org.acumos.designstudio.ce.util.EELFLoggerDelegator;
import org.acumos.designstudio.ce.vo.compositeproto.Protobuf;
import org.acumos.designstudio.ce.vo.compositeproto.ProtobufMessage;
import org.acumos.designstudio.ce.vo.compositeproto.ProtobufMessageField;
import org.acumos.designstudio.ce.vo.compositeproto.ProtobufOption;
import org.acumos.designstudio.ce.vo.compositeproto.ProtobufService;
import org.acumos.designstudio.ce.vo.compositeproto.ProtobufServiceOperation;
import org.acumos.nexus.client.NexusArtifactClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompositeSolutionProtoFileGeneratorServiceImpl implements ICompositeSolutionProtoFileGeneratorService {
	private static EELFLoggerDelegator logger = EELFLoggerDelegator.getLogger(CompositeSolutionProtoFileGeneratorServiceImpl.class);
	@Autowired
	private CommonDataServiceRestClientImpl cdmsClient;
	@Autowired
	private NexusArtifactClient nexusArtifactClient;
	
	/**
        * This method returns Nexus URI for the specified solution artifact type.
        *
        * @param solutionId
        *	solutionId of the artifact
        * @param version
        *	solution version
        * @param artifactType
        *	solution artifact type for which UIR need to be retrieved
        * @param fileExtension
        *	The artifact type Nexus file extension
        * @return string
        *	returns the Nexus proto URI
        *
	* @throws ServiceException 
        *	In case of any error throws ServiceException
        */
	@Override
	public String getProtoUrl(String solutionId, String version, String artifactType, String fileExtension) throws ServiceException {
		String result = "";
		List<MLPSolutionRevision> mlpSolutionRevisionList;
		String solutionRevisionId = null;
		ByteArrayOutputStream byteArrayOutputStream = null;
		List<MLPArtifact> mlpArtifactList;
		try {
			// 1. Get the list of SolutionRevision for the solutionId.
			mlpSolutionRevisionList = cdmsClient.getSolutionRevisions(solutionId);

			// 2. Match the version with the SolutionRevision and get the
			// solutionRevisionId.
			if (null != mlpSolutionRevisionList && !mlpSolutionRevisionList.isEmpty()) {
				solutionRevisionId = mlpSolutionRevisionList.stream().filter(mlp -> mlp.getVersion().equals(version))
						.findFirst().get().getRevisionId();
				logger.debug(EELFLoggerDelegator.debugLogger," SolutionRevisonId for Version :  {} ", solutionRevisionId );
			}
		} catch (NoSuchElementException | NullPointerException e) {
			logger.error(EELFLoggerDelegator.errorLogger, "Error : Exception in getProtoUrl() : Failed to fetch the Solution Revision Id",
					e);
			throw new NoSuchElementException("Failed to fetch the Solution Revision Id of the solutionId for the user");
		} 
		
		if (null != solutionRevisionId) {
			// 3. Get the list of Artifact for the SolutionId and SolutionRevisionId.
			mlpArtifactList = getSolutionArtifacts(solutionId, solutionRevisionId);
			String nexusURI = "";
			if (null != mlpArtifactList && !mlpArtifactList.isEmpty()) {
				try {
					nexusURI = mlpArtifactList.stream()
							.filter(mlpArt -> mlpArt.getArtifactTypeCode().equalsIgnoreCase(artifactType)).findFirst()
							.get().getUri();
					for(MLPArtifact mlpArt : mlpArtifactList){
						if( null != fileExtension ){
							if(mlpArt.getArtifactTypeCode().equalsIgnoreCase(artifactType) && mlpArt.getName().contains(fileExtension)){
								nexusURI = mlpArt.getUri();
								break;
							}
						}
					}

					logger.debug(EELFLoggerDelegator.debugLogger, " Nexus URI :  {} ", nexusURI );

					if (null != nexusURI) {
						byteArrayOutputStream = getPayload(nexusURI);
						logger.debug(EELFLoggerDelegator.debugLogger,
								" Response in String Format :  {} ", byteArrayOutputStream.toString() );
						result = byteArrayOutputStream.toString();
					}
				} catch (NoSuchElementException | NullPointerException e) {
					logger.error(EELFLoggerDelegator.errorLogger,"Error : Exception in getProtoUrl() : Failed to fetch the artifact URI for artifactType",e);
					throw new NoSuchElementException(
							"Could not search the artifact URI for artifactType " + artifactType);
				} finally {
					try {
						if (byteArrayOutputStream != null) {
							byteArrayOutputStream.close();
						}
					} catch (Exception e) {
						logger.error(EELFLoggerDelegator.errorLogger,
								"Error : Exception in getProtoUrl() : Failed to close the byteArrayOutputStream", e);
						throw new ServiceException("  Exception in getProtoUrl() ", "201",
								"Failed to close the byteArrayOutputStream");
					}
				}
			}
		}
		logger.debug(EELFLoggerDelegator.debugLogger, "getProtoUrl() : End");
		
		return result;
	}

	private List<MLPArtifact> getSolutionArtifacts(String solutionId, String solutionRevisionId) throws ServiceException {
		logger.debug(EELFLoggerDelegator.debugLogger, "getSolutionArtifacts : Begin");
		List<MLPArtifact> mlpSolutionRevisions = null;
		List<MLPArtifact> artifactList = new ArrayList<MLPArtifact>();
		try {
			mlpSolutionRevisions = cdmsClient.getSolutionRevisionArtifacts(solutionId, solutionRevisionId);
			if(mlpSolutionRevisions != null) {
				for (MLPArtifact artifact : mlpSolutionRevisions) {
					String[] st = artifact.getUri().split("/");
					String name = st[st.length-1];
					artifact.setName(name);
					artifactList.add(artifact);
				}
			}
		} catch (IllegalArgumentException e) {
			throw new ServiceException("  Exception in getSolutionArtifacts() ", "201",
					"Not able to get the Artifacts for the Solution Revision");
		} 
		logger.debug(EELFLoggerDelegator.debugLogger, "getSolutionArtifacts : End");
		return artifactList;
	}
	
	private ByteArrayOutputStream getPayload(String uri) {

		ByteArrayOutputStream outputStream = null;
		try {
			outputStream = nexusArtifactClient.getArtifact(uri);
		} catch (Exception ex) {
			logger.error(EELFLoggerDelegator.errorLogger, " Exception in getListOfArtifacts() ", ex);
		}
		return outputStream;
	}

	/**
         * This method parse the data and returns the Protobuf
	 * @param protoData1
         *	.proto file content
         *
	 * @return Protobuf
         *	Converts the input and returns the insance of Protobuf class.
         *
	 */
	public Protobuf parseProtobuf(String protoData1) {
			Scanner scanner = new Scanner(protoData1);
			Protobuf protobuf= new Protobuf();
			boolean serviceBegin = false; 
			boolean serviceDone = false;
			
			boolean messageBegin = false;
			
			StringBuilder serviceStr = null;
			StringBuilder messageStr = null;
			
			while (scanner.hasNextLine()) {
			  String line = scanner.nextLine().trim();
			  
			  if(serviceBegin && !serviceDone){
				  serviceStr.append(line);
				  serviceStr.append("\n");
				  if(line.contains("}")){
					  serviceBegin = false;
					  ProtobufService service = parserService(serviceStr.toString().trim());
					  protobuf.setService(service);
					  serviceDone = true;
				  }
			  } else if(messageBegin) {
				  messageStr.append(line);
				  messageStr.append("\n");
				  if(line.contains("}")){
					  messageBegin = false;
					  ProtobufMessage message = parseMessage(messageStr.toString().trim());
					  protobuf.getMessages().add(message);
				  }
			  } else {
				  if(line.startsWith("service") && !serviceDone){
					   serviceBegin = true; 
					   serviceStr = new StringBuilder();
					   serviceStr.append(line);
					   serviceStr.append("\n");
				  }
				  
				  if(line.startsWith("message")){
					  messageBegin = true;
					  messageStr = new StringBuilder();
					  messageStr.append(line);
					  messageStr.append("\n");
				  }
				  if(line.startsWith("syntax")){
					  String value = line.substring(line.indexOf("=")+1, line.length()-1);
					  protobuf.setSyntax(value.replace("\"", "").trim());
				  }
				  
				  if(line.startsWith("option")){
					  ProtobufOption option = parseOption(line.trim());
					  protobuf.getOptions().add(option);
				  }
				  if(line.startsWith("package")){
					  String value = line.substring(line.indexOf(" ")+1, line.length()-1);
					  protobuf.setPackageName(value.replace("\"", "").trim());
				  }
				  
			  }
			}
			scanner.close();
			return protobuf;
		}
	private static ProtobufService parserService(String serviceStr) {
		Scanner scanner = new Scanner(serviceStr);
		ProtobufService service = new ProtobufService();
		while (scanner.hasNextLine()) {
			  String line = scanner.nextLine().trim();
			  if(line.startsWith("service")){
				  String name = line.replace("\t", "").replace("service", "").trim();
				  if(name.contains("{")){
					  name = name.substring(0, name.lastIndexOf("{")).trim();
				  } else {
					  name = name.trim();
				  }
				  service.setName(name);
			  } else if(line.length() > 1){
				  if(line.indexOf("{") > - 1){
					  line = line.replace("{", "").trim();
				  }
				  if(line.contains("}")){
					  line = line.replace("}", "").trim();
				  }
				  ProtobufServiceOperation operation = parseServiceOperation(line);
				  service.getOperations().add(operation);
			  }
		}
		return service;
	}
	
	private static ProtobufServiceOperation parseServiceOperation(String line) {
		ProtobufServiceOperation operation = new ProtobufServiceOperation();
		line = line.replace("\t", "").trim();
		line = line.replace(";", "").replace("\t", "").trim();
		String operationType = "";
		String operationName = "";
		String inputParameterString = "";
		String outputParameterString = "";

		String line1 = line.split("returns")[0];
		operationType = line1.split(" ", 2)[0].trim();
		String line2 = line1.split(" ", 2)[1].replace(" ", "").replace("(", "%br%").replace(")", "").trim();
		operationName = line2.split("%br%")[0].trim();
		inputParameterString = line2.split("%br%")[1].trim();
		outputParameterString = line.split("returns")[1].replace("(", "").replace(")", "").trim();
		String[] inputParamArray = inputParameterString.split(",");
		String[] outputParamArray = outputParameterString.split(",");
		int inputParamSize = inputParamArray.length;
		int outputParamSize = outputParamArray.length;
		List<String> inputParamList = new ArrayList<String>();
		List<String> outputParamList = new ArrayList<String>();
		for(int i =0 ; i < inputParamSize ; i++ ){
			inputParamList.add(inputParamArray[i].trim());
		}
		for(int i =0 ; i < outputParamSize ; i++ ){
			outputParamList.add(outputParamArray[i].trim());
		}
		operation.setName(operationName);
		operation.setType(operationType);
		operation.setInputMessageNames(inputParamList);
		operation.setOutputMessageNames(outputParamList);
		return operation;
	}
	
	/**
         * This method parse the input protobuf message 
         * 
         * @param messageStr
         *	input as string format
         *
         * @return ProtobufMessage
         *	converts and returns the ProtobufMessage. 
         */
	public static ProtobufMessage parseMessage(String messageStr) {
		Scanner scanner = new Scanner(messageStr);
		ProtobufMessage message = new ProtobufMessage();
		ProtobufMessageField field = null;
		while (scanner.hasNextLine()) {
			  String line = scanner.nextLine().trim();
			  if(line.startsWith("message")){
				  String name = null;
				  line = line.replace("\t", "").replace("message", "");
				  if(line.contains("{")){
					  name = line.substring(0, line.lastIndexOf("{")).trim();
					  if(line.contains(";")){
						  line = line.substring(line.lastIndexOf("{")+1, line.length()).trim();
						  field = parseMessageField(line);
						  message.getFields().add(field);
					  }
				  } else {
					  name = line.trim();
				  }
				  message.setName(name);
			  } else if(line.length() > 1){
				  if(line.indexOf("{") > - 1){
					  line = line.replace("{", "").trim();
				  }
				  if(line.contains("}")){
					  line = line.replace("}", "").trim();
				  }
				  field = parseMessageField(line);
				  message.getFields().add(field);
			  }
		}
		scanner.close();
		return message;
	}
	
	private static ProtobufMessageField parseMessageField(String line) {
		ProtobufMessageField field = new ProtobufMessageField();
		line = line.replace(";", "").trim();
		
		String[] fields = line.split(" ");
		int size = fields.length;
		if(size == 5){
			field.setRole(fields[0]);
			field.setType(fields[1]);
			field.setName(fields[2]);
			field.setTag(Integer.valueOf(fields[4]));
		} else if( size == 4){
			field.setRole("");
			field.setType(fields[0]);
			field.setName(fields[1]);
			field.setTag(Integer.valueOf(fields[3]));
		} else {
			field = null;
		}
		return field;
	}
	
	private static ProtobufOption parseOption(String line) {
		ProtobufOption option = new ProtobufOption();
		line = line.replace("\t", "").trim();
		line = line.replace("option", "").trim();
		line = line.trim();
		String name = line.substring(0,line.indexOf("=")-1).trim();
		String value = line.substring(line.indexOf("=")+1, line.length());
		option.setName(name.trim());
		option.setValue(value.replace(";", "").replace("\"", "").trim());
		return option;
	}
	
}
