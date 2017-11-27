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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.acumos.designstudio.toscagenerator.ToscaGeneratorClient;
import org.acumos.designstudio.toscagenerator.util.Properties;
import org.acumos.designstudio.toscagenerator.vo.protobuf.InputMessage;
import org.acumos.designstudio.toscagenerator.vo.protobuf.MessageBody;
import org.acumos.designstudio.toscagenerator.vo.protobuf.MessageargumentList;
import org.acumos.designstudio.toscagenerator.vo.protobuf.Operation;
import org.acumos.designstudio.toscagenerator.vo.protobuf.Option;
import org.acumos.designstudio.toscagenerator.vo.protobuf.OutputMessage;
import org.acumos.designstudio.toscagenerator.vo.protobuf.ProtoBufClass;
import org.acumos.designstudio.toscagenerator.vo.protobuf.SortComparator;
import org.acumos.designstudio.toscagenerator.vo.protobuf.SortFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import java.util.StringTokenizer;

/**
 * 
 */

public class ProtobufGeneratorService {

	boolean isMessage = false;
	boolean isItservice = false;
	boolean isItMessage = false;
	int servicesLineCount = 0;
	
	private static final Logger logger = LoggerFactory.getLogger(ToscaGeneratorService.class);
    //Protobuf's variable declaration section started
	final Pattern pattern = Pattern.compile("\\((.*?)\\)");
	private List<MessageargumentList> messageargumentList = null;// new
	// ArrayList<MessageargumentList>();
	private List<InputMessage> listOfInputMessages = null;
	private List<OutputMessage> listOfOputPutMessages = null;
	private List<Operation> listOfOperation = null;//new ArrayList<Operation>();

	private ProtoBufClass protoBufClass= null;

	private MessageBody messageBody = null;
	private List<MessageBody> messageBodyList = null;//new ArrayList();
	private org.acumos.designstudio.toscagenerator.vo.protobuf.Service service = null;
	private Operation operation = null;
	private List<String> listOfInputAndOutputMessage = null;
	private List<Option> listOfOption = null;
    //Probuf's variable declaration section end
	
	public String createProtoJson(String solutionId, String version, File localMetadataFile){
		logger.debug("-------------- CreateProtoJson() strated ---------------");
		protoBufClass = new ProtoBufClass();
		messageBodyList = new ArrayList<MessageBody>();
		listOfInputAndOutputMessage =  new ArrayList<String>();
		listOfOption = new ArrayList<Option>();
		BufferedReader br = null;
		FileReader fr = null;
		String protoBufToJsonString = "";
		try {
			//String FILEPATH = Properties.getTempFolderPath(solutionId,version);
			fr = new FileReader(localMetadataFile);
			br = new BufferedReader(fr);
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				parseLine(sCurrentLine.replace("\t",""));
			}
			Gson gson = new Gson();
			boolean messageIsFound = false;
			int i;
			for(i = 0; i <protoBufClass.getListOfMessages().size(); i++){
				for(int k = 0; k<listOfInputAndOutputMessage.size();k++){
					if(listOfInputAndOutputMessage.get(k).equals(protoBufClass.getListOfMessages().get(i).getMessageName())){
						messageIsFound = true;
						break;
					}else{
						messageIsFound = false;
						
					}
				}
				if(!messageIsFound){
					protoBufClass.getListOfMessages().remove(i);	
					System.out.println("After remove"+protoBufClass.getListOfMessages());
					i=0;
					i--;
				}
				messageIsFound = false;
			}
			protoBufToJsonString = gson.toJson(protoBufClass);
			//ProtobufUtil.writeDataToFile(FILEPATH, "ProtobufToJson", "json", jsonInString);
			isMessage = false;
			isItservice = false;
			isItMessage = false;
			servicesLineCount = 0;
		logger.debug("-------------- CreateProtoJson() end ---------------");	
		} catch (Exception ex) {
			logger.error(" --------------- Exception Occured  CreateProtoJson() when Reading the protobuf file and generating protobuf json--------------", ex);
			ex.printStackTrace();
			

		}finally {
		 try {

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}


		
		return protoBufToJsonString;
		
	}

	private void parseLine(String line) {
      try{
		// construct syntax
		if (line.contains("syntax")) {
			protoBufClass.setSyntax(constructSyntax(line));
		}
		// construct package
		if (line.startsWith("package")) {
			protoBufClass.setPackageName(constructPackage(line));
		}
		System.out.println("isOptionKeywordRequirede"+Properties.isOptionKeywordRequirede());
		if(Properties.isOptionKeywordRequirede().equals("true")){
			if(line.startsWith("option")){
				constructOption(line);
				protoBufClass.setListOfOption(listOfOption);
			}
        }
		// start package

		if (line.startsWith("message")) {
			logger.debug("-------------- costructMessage() strated ---------------");
			isMessage = false;
			messageBody = new MessageBody();

			messageBody = costructMessage(line, messageBody, null);

		} else if (isMessage && !line.contains("}")) {
			messageargumentList = new ArrayList<MessageargumentList>();
			messageBody = costructMessage(line, messageBody, messageargumentList);
		} else if (isMessage && line.startsWith("}")) {
			messageBodyList.add(messageBody);
			protoBufClass.setListOfMessages(messageBodyList);
			isMessage = false;
			logger.debug("-------------- costructMessage() end ---------------");
		}
        
		
		if (line.startsWith("service")) {
			logger.debug("-------------- constructService() started ---------------");
			service = new org.acumos.designstudio.toscagenerator.vo.protobuf.Service();
			service = constructService(line, service);

		}else if (isItservice && !line.contains("}") && !line.isEmpty()) {
			operation = new Operation();
			listOfOperation = new ArrayList<Operation>();
			line = line.replace(";","").replace("\t", "").trim();
			String operationType = "";
			String OperatioName = "";
			String inputParameterString = "";
			String outPutParameterString = "";
			
			String line1 = line.split("returns")[0];
			operationType = line1.split(" ",2)[0].trim();
			String line2 = line1.split(" ",2)[1].replace(" " , "").replace("(", "%br%").replace(")","").trim();
			OperatioName = line2.split("%br%")[0].trim();
			inputParameterString = line2.split("%br%")[1].trim();
			outPutParameterString = line.split("returns")[1].replace("(", "").replace(")","").trim();
			
			
			
			/*
			 
			System.out.println("operationType : " + operationType);
			System.out.println("Operation Name : " + OperatioName);
			System.out.println("inputParameterString : " + inputParameterString);
			System.out.println("outPutParameterString : " + outPutParameterString);
			
			
			  String[] fields = line.split(" ");
			  for (int i = 0; i < fields.length; i++) {
				System.out.println(i + ") " + fields[i]);
				if (!fields[i].isEmpty()) {
					
					operationType = fields[i];
					OperatioName = fields[i + 1];
					inputParameterString = fields[i + 2];
					outPutParameterString = fields[i + 4];
					break;
				}
			}*/

			operation.setOperationType(operationType);
			operation.setOperatioName(OperatioName);
			listOfInputMessages = constructInputMessage(inputParameterString);
			listOfOputPutMessages = constructOutputMessage(outPutParameterString);
			
				
			operation.setListOfInputMessages(listOfInputMessages);
			operation.setListOfOutputMessages(listOfOputPutMessages);
			// input
			// messages
			if (service.getListOfOperations() != null && !service.getListOfOperations().isEmpty()) {
				service.getListOfOperations().add(operation);
			} else {
				listOfOperation.add(operation);
				service.setListOfOperations(listOfOperation);
				protoBufClass.setService(service);
			}
			isItservice = false;
			logger.debug("-------------- constructService() end ---------------");
		}
      }catch(Exception ex){
    	  logger.error(" --------------- Exception Occured  parseLine() --------------", ex);
			ex.printStackTrace();
			//throw new ServiceException(" --------------- Exception Occured  parseLine() -------------"+ex.getMessage());
					 
      }
		
	}

	private String constructSyntax(String line) {
		logger.debug("-------------- constructSyntax() strated ---------------");
		String removequotes = "";
		try{
			if (line.startsWith("syntax")) {
				String[] fields = line.split("=");
				String removeSemicolon = fields[1].replace(";", "");
				String trimString = removeSemicolon.trim();
				removequotes = trimString.replaceAll("^\"|\"$", "");
				//System.out.println(removequotes);
	
			}
		}catch(Exception ex){
			logger.error(" --------------- Exception Occured  constructSyntax() --------------", ex);
			ex.printStackTrace();
		}
		logger.debug("-------------- constructSyntax() end ---------------");
		return removequotes;

	}

	private String constructPackage(String line) {
		logger.debug("-------------- constructPackage() strated ---------------");
		String[] fields = line.split(" ");
		logger.debug("-------------- constructPackage() end ---------------");
		return fields[1].replace(";", "");
		
	}
	private String constructOption(String line){
		Option optionInstance = new Option();
		StringTokenizer st = new StringTokenizer(line," ");
		String removequotes = "";
		while (st.hasMoreElements()) {
			  st.nextElement();
			  optionInstance.setKey(st.nextElement().toString());
			  st.nextElement();
			  String removeSemicolon = st.nextElement().toString().replace(";", "");
			  String trimString = removeSemicolon.trim();
			  removequotes = trimString.replaceAll("^\"|\"$", "");
			  optionInstance.setValue(removequotes);
		}
		listOfOption.add(optionInstance);
		System.out.println("----------"+st.countTokens());
		return "";
	}

	private MessageBody costructMessage(String line, MessageBody messageBody,
			List<MessageargumentList> messageargumentList) {
		try{
			if (line.startsWith("message")) {
				int openCurlybacketPosition;
				int messageLineCount = 0;
				String messageValue = "";
				openCurlybacketPosition = line.indexOf("{");
				messageValue = line.substring(8, openCurlybacketPosition);
				// System.out.println("messageValue-----------------"+messageValue.trim());
				messageBody.setMessageName(messageValue.trim());
			}
			if (isMessage) {
	
				// System.out.println("fields-----------------");
				if (line.endsWith(";") || line.contains(";")) {
				    StringTokenizer st = new StringTokenizer(line," ");
					String[] fields = line.split(" ");
					// System.out.println("fields-----------------"+fields[4]+fields[5]+fields[6]);
					MessageargumentList messageargumentListInstance = new MessageargumentList();
					
					for (int i = 0; i < fields.length; i++) {
						if (!fields[i].isEmpty()) {
							if(st.countTokens() == 5){//count total number of token in line.
						    messageargumentListInstance.setRule(fields[i]);
							messageargumentListInstance.setType(fields[i + 1]);
							messageargumentListInstance.setName(fields[i + 2]);
							messageargumentListInstance.setTag(fields[i + 4].replace(";", ""));
						}else{
							messageargumentListInstance.setRule("");
							messageargumentListInstance.setType(fields[i]);
							messageargumentListInstance.setName(fields[i + 1]);
							messageargumentListInstance.setTag(fields[i + 3].replace(";", ""));
						}
							break;
						}
					}
					SortComparator sortComparator = SortFactory.getComparator();
					if (messageBody.getMessageargumentList() != null) {
						messageBody.getMessageargumentList().add(messageargumentListInstance);
						Collections.sort(messageBody.getMessageargumentList(), sortComparator);
					} else {
						messageargumentList.add(messageargumentListInstance);
						messageBody.setMessageargumentList(messageargumentList);
					}
	
				}
			}
			
			if (line.contains("}")) {
				isMessage = false;
			} else {
				isMessage = true;
			}
		}catch(Exception ex){
			logger.error(" --------------- Exception Occured  costructMessage() --------------", ex);
			ex.printStackTrace();
		}
		return messageBody;
	}

	private org.acumos.designstudio.toscagenerator.vo.protobuf.Service constructService(String line, org.acumos.designstudio.toscagenerator.vo.protobuf.Service service) {
		try{
			String servicesName = "";
			int openCurlybacketPosition;
			openCurlybacketPosition = line.indexOf("{");
			servicesName = line.substring(8, openCurlybacketPosition);
			service.setName(servicesName.trim());
			if (line.contains("}")) {
				isItservice = false;
			} else {
				isItservice = true;
			}
		}catch(Exception ex){
			logger.error(" --------------- Exception Occured  constructService() --------------", ex);
			ex.printStackTrace();	
		}
		return service;

	}

	private List<InputMessage> constructInputMessage(String inputParameterString) {
		logger.debug("-------------- constructInputMessage() strated ---------------");
		listOfInputMessages = new ArrayList<InputMessage>();
		try{
			//TODO : Ashis please share details for below logic.
			/*Matcher inPutMatcher = null;
			inPutMatcher = pattern.matcher(inputParameterString);
			inPutMatcher.find();
			String[] inPutParameterArray = inPutMatcher.group(1).split(",");*/
			
			String[] inPutParameterArray = inputParameterString.split(",");
			for (int i = 0; i < inPutParameterArray.length; i++) {
				InputMessage inputMessage = new InputMessage();
				System.out.println("Input Message : " + inPutParameterArray[i]);
				inputMessage.setInputMessageName(inPutParameterArray[i]);
				listOfInputMessages.add(inputMessage);
				listOfInputAndOutputMessage.add(inputMessage.getInputMessageName());
			}
		}catch(Exception ex){
			logger.error(" --------------- Exception Occured  constructInputMessage() --------------", ex);
			ex.printStackTrace();
		}
		logger.debug("-------------- constructInputMessage() end ---------------");
		return listOfInputMessages;

	}

	private List<OutputMessage> constructOutputMessage(String outPutParameterString) {
		logger.debug("-------------- constructOutputMessage() strated ---------------");
		listOfOputPutMessages = new ArrayList<OutputMessage>();
		try{
		/*Matcher outPutMatcher = null;
		outPutMatcher = pattern.matcher(outPutParameterString);
		outPutMatcher.find();
		String[] outPutParameterArray = outPutMatcher.group(1).split(",");*/
			String[] outPutParameterArray = outPutParameterString.split(",");
		for (int i = 0; i < outPutParameterArray.length; i++) {
			OutputMessage outputMessage = new OutputMessage();
			System.out.println("output Message : " + outPutParameterArray[i]);
			outputMessage.setOutPutMessageName(outPutParameterArray[i]);
			listOfOputPutMessages.add(outputMessage);
			listOfInputAndOutputMessage.add(outputMessage.getOutPutMessageName());
		}
		}catch(Exception ex){
			logger.error("-------------- constructOutputMessage() end ---------------",ex);
			ex.printStackTrace();
		}
		logger.debug("-------------- constructOutputMessage() end ---------------");
		return listOfOputPutMessages;

	}
	

				
	public static void main(String args[]) {
		ProtobufGeneratorService protobufGeneratorService = new ProtobufGeneratorService();

		ProtobufGeneratorService protoService = new ProtobufGeneratorService();

		File localProtofile1 = new File(
				"D:/VS00485966/Acumos/Acumos/Studio/Sprint6/Temp/default.proto");

		/*File localProtofile2 = new File(
		"D:/VS00485966/Acumos/Acumos/Studio/Sprint4/Protobuf_files/alarm-generator-proto.proto");
		
		File localProtofile3 = new File(
				"D:/VS00485966/Acumos/Acumos/Studio/Sprint4/Protobuf_files/classifier-proto.proto");
		
		File localProtofile4 = new File(
				"D:/VS00485966/Acumos/Acumos/Studio/Sprint4/Protobuf_files/predictor-proto.proto");*/

		String protoJSON = protoService.createProtoJson("1234", "1.0.0", localProtofile1);

		/*String protoJSON2 = protoService.createProtoJson("1234", "1.0.0", localProtofile2);
		
		String protoJSON3 = protoService.createProtoJson("1234", "1.0.0", localProtofile3);
		
		String protoJSON4 = protoService.createProtoJson("1234", "1.0.0", localProtofile4);*/

		System.out.println("Proto JSON 1 : " + protoJSON);

		/*System.out.println("Proto JSON 2 : " + protoJSON2);
		
		System.out.println("Proto JSON 3 : " + protoJSON3);
		
		System.out.println("Proto JSON 4 : " + protoJSON4);*/

	}

		
		//uploadTGIF(args);

	

}
