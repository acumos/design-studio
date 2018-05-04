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

package org.acumos.gdm.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.acumos.converter.Conversion;
import org.acumos.converter.ConversionEnum;
import org.acumos.converter.ConversionException;
import org.acumos.gdm.util.EELFLoggerDelegator;
import org.acumos.vo.DataVO.input;
import org.acumos.vo.DataVO.output;
import org.acumos.vo.mapping.DataMap;
import org.acumos.vo.mapping.InputField;
import org.acumos.vo.mapping.MapInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.Descriptors.FieldDescriptor;

@Service("GDMServiceImpl")
public class GDMServiceImpl implements IGDMService {

	private final EELFLoggerDelegator logger = EELFLoggerDelegator.getLogger(GDMServiceImpl.class);
	
	private static final String FIELD_MAPPING_FILE_NAME = "classpath:FieldMapping.json";
	
	private ResourceLoader resourceLoader;
	
	@Autowired
    public GDMServiceImpl(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

	
	@Override
	public OutputStream mapData(InputStream inputStream) throws Exception {
		OutputStream result = null;
		try {
			result = process(inputStream);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, "Error in mapData could not process successfully !!!", e);
			throw e;
		}
		return result;
	}
	
	private OutputStream process(InputStream inputStream)
			throws IOException, JsonParseException, JsonMappingException, ConversionException {
		logger.debug(EELFLoggerDelegator.debugLogger," process() : Begin"); 
		//1. Read the data in to input message.
		input.Builder inputMessage = input.newBuilder();
		inputMessage.mergeFrom(inputStream);
		
		DataMap dataMap = getMappingDetails();
		
		output.Builder outputMessage = transformInputToOutput(inputMessage, dataMap);
		
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		//4. Return the stream. 
		outputMessage.build().writeTo(result);
		logger.debug(EELFLoggerDelegator.debugLogger,	"process() : End");
		return result;
	}

	private output.Builder transformInputToOutput(input.Builder inputMessage, DataMap dataMap) throws ConversionException {
		//3. Start the conversion
		output.Builder outputMessage = output.newBuilder();
		
				
		List<MapInput> inputMaps = Arrays.asList(dataMap.getMap_inputs());
		List<InputField> inputMapFields = null;
		FieldDescriptor inputfieldDesc = null;
		FieldDescriptor outputfieldDesc = null;
		String conversionValue = null;
		for(MapInput mapInput : inputMaps) {
			inputMapFields = Arrays.asList(mapInput.getInput_fields());
			for(InputField inputField : inputMapFields){
				inputfieldDesc = inputMessage.getDescriptorForType().findFieldByNumber(Integer.valueOf(inputField.getTag()));
				Object inputvalue = inputMessage.getField(inputfieldDesc);
				Object outputvalue = null;
				logger.debug("inputField " + inputField.getTag() + " value : " + inputvalue);
				outputfieldDesc = outputMessage.getDescriptorForType().findFieldByNumber(Integer.valueOf(inputField.getMapped_to_field()));
				conversionValue = inputfieldDesc.getType() + "TO" + outputfieldDesc.getType();
				if(inputfieldDesc.isRepeated()){
					outputvalue = convertRepeatedValues(conversionValue, inputvalue);
				} else {
					outputvalue = Conversion.getInstance().executeConversion(inputvalue,ConversionEnum.valueOf(conversionValue));
				}
				
				outputMessage.setField(outputfieldDesc, outputvalue );
				logger.debug(EELFLoggerDelegator.debugLogger,"outputField " + inputField.getMapped_to_field() + " value : " + outputMessage.getField(outputfieldDesc));
			}
		}
		return outputMessage;
	}

	private Object convertRepeatedValues(String conversionValue, Object inputvalue)
			throws ConversionException {
		Object outputvalue;
		Conversion client = Conversion.getInstance();
		String clazz = inputvalue.getClass().getName();
		//Object[] inputvals = (Object[]) inputvalue;
		List<Object> inputvalues = (List<Object>) inputvalue; //Arrays.asList(inputvals);
		List<Object> outputValues = new ArrayList<Object>();
		for(Object obj : inputvalues){
			outputValues.add(client.executeConversion(obj,ConversionEnum.valueOf(conversionValue)));
		}
		outputvalue = (Object) outputValues;
		return outputvalue;
	}
	
	private DataMap getMappingDetails() throws IOException, JsonParseException, JsonMappingException {
		//2. Read the mapping details 
		//Get file from resources folder
		/*ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(FIELD_MAPPING_FILE_NAME).getFile());*/
		//File file = new ClassPathResource(FIELD_MAPPING_FILE_NAME).getFile();
		
		Resource resource = null;
		InputStream inputStream = null;
		DataMap dataMap = null;
		try {
		resource = resourceLoader.getResource(FIELD_MAPPING_FILE_NAME);
		//File file = resource.getFile();
		inputStream = resource.getInputStream();
		ObjectMapper mapper = new ObjectMapper();
		dataMap = mapper.readValue(inputStream, DataMap.class);
		} finally {
				if(null != inputStream)
					inputStream.close();
		}
		return dataMap;
	}
	
	}
