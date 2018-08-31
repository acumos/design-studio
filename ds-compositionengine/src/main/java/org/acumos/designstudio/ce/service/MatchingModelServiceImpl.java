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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.acumos.cds.client.CommonDataServiceRestClientImpl;
import org.acumos.cds.domain.MLPArtifact;
import org.acumos.cds.domain.MLPSolution;
import org.acumos.cds.domain.MLPSolutionRevision;
import org.acumos.cds.transport.RestPageRequest;
import org.acumos.cds.transport.RestPageResponse;
import org.acumos.designstudio.ce.exceptionhandler.ServiceException;
import org.acumos.designstudio.ce.util.ConfigurationProperties;
import org.acumos.designstudio.ce.util.EELFLoggerDelegator;
import org.acumos.designstudio.ce.util.ModelCacheForMatching;
import org.acumos.designstudio.ce.util.Properties;
import org.acumos.designstudio.ce.vo.cdump.ComplexType;
import org.acumos.designstudio.ce.vo.matchingmodel.DSModelVO;
import org.acumos.designstudio.ce.vo.matchingmodel.KeyVO;
import org.acumos.designstudio.ce.vo.matchingmodel.ModelDetailVO;
import org.acumos.designstudio.ce.vo.protobuf.MessageBody;
import org.acumos.designstudio.ce.vo.protobuf.MessageargumentList;
import org.acumos.designstudio.ce.vo.tgif.Call;
import org.acumos.designstudio.ce.vo.tgif.Provide;
import org.acumos.designstudio.ce.vo.tgif.Service;
import org.acumos.designstudio.ce.vo.tgif.Tgif;
import org.acumos.nexus.client.NexusArtifactClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonParser.Feature;


@Component
public class MatchingModelServiceImpl implements IMatchingModelService{
	private static EELFLoggerDelegator logger = EELFLoggerDelegator.getLogger(MatchingModelServiceImpl.class);
	
	@Autowired
	MatchingModelServiceComponent matchingModelServiceComponent;
	
	@Autowired
	CommonDataServiceRestClientImpl cmnDataService;
	
	@Autowired
	Properties props;
	
	@Autowired
	ConfigurationProperties confprops;
	
	@Autowired
	NexusArtifactClient nexusArtifactClient;
	
	@Autowired
	private ModelCacheForMatching modelCacheForMatching;

	private final ObjectMapper mapper = new ObjectMapper();
	
	List<DSModelVO> models = null;
	
	
	@Override
	public List<DSModelVO> getPublicDSModels() throws ServiceException {
		logger.debug(EELFLoggerDelegator.debugLogger, " getPublicDSModels() Begin ");
		List<DSModelVO>  modelsList = new ArrayList<DSModelVO>();
		List<MLPSolution> mlpSolutionsList = null;
		Map<String, Object> queryParameters = new HashMap<>();
		queryParameters.put("active", Boolean.TRUE);
		RestPageResponse<MLPSolution> pageResponse = cmnDataService.searchSolutions(queryParameters, false,
				new RestPageRequest(0, props.getSolutionResultsetSize()));
		mlpSolutionsList = pageResponse.getContent();
		if (null == mlpSolutionsList) {
			logger.debug(EELFLoggerDelegator.debugLogger, " CommonDataService returned null Solution list");
		} else if (mlpSolutionsList.isEmpty()) {
			logger.debug(EELFLoggerDelegator.debugLogger, " CommonDataService returned empty Solution list");
		} else {
			String compoSolnTlkitTypeCode = props.getCompositSolutiontoolKitTypeCode();
			String pbAccessTypeCode = props.getPublicAccessTypeCode();
			String orAccessTypeCode = props.getOrganizationAccessTypeCode();
			List<MLPSolutionRevision> mlpSolutionRevisionList = null;
			for (MLPSolution mlpsolution : mlpSolutionsList) {
				if (null != mlpsolution.getToolkitTypeCode()
						&& (!mlpsolution.getToolkitTypeCode().equals(compoSolnTlkitTypeCode))) {
					List<MLPSolutionRevision> mlpSolRevisions = cmnDataService.getSolutionRevisions(mlpsolution.getSolutionId());
					mlpSolutionRevisionList = new ArrayList<MLPSolutionRevision>();
					for (MLPSolutionRevision mlpSolRevision : mlpSolRevisions) {
						String accessTypeCode = mlpSolRevision.getAccessTypeCode();
						if (accessTypeCode.equals(pbAccessTypeCode)) {
							mlpSolutionRevisionList.add(mlpSolRevision);
						} else if (accessTypeCode.equals(orAccessTypeCode)) {
							mlpSolutionRevisionList.add(mlpSolRevision);
						}
					}
					if(mlpSolutionRevisionList.size() > 0 ){
						DSModelVO modelVO = new DSModelVO();
						modelVO.setMlpSolution(mlpsolution);
						modelVO.setMlpSolutionRevisions(mlpSolutionRevisionList);
						modelsList.add(modelVO);
					}
				}
			}
		}
		logger.debug(EELFLoggerDelegator.debugLogger, " getPublicDSModels() End ");
		return modelsList;
	}
	
	
	@Override
	public List<DSModelVO> getPrivateDSModels(String userId) throws ServiceException {
		logger.debug(EELFLoggerDelegator.debugLogger, " getPrivateDSModels() Begin ");
		List<DSModelVO> modelsList = new ArrayList<DSModelVO>();
		List<MLPSolution> mlpSolutionsList = null;
		Map<String, Object> queryParameters = new HashMap<>();
		queryParameters.put("active", Boolean.TRUE);
		RestPageResponse<MLPSolution> pageResponse = cmnDataService.searchSolutions(queryParameters, false,
				new RestPageRequest(0, props.getSolutionResultsetSize()));
		mlpSolutionsList = pageResponse.getContent();
		if (null == mlpSolutionsList) {
			logger.debug(EELFLoggerDelegator.debugLogger, " CommonDataService returned null Solution list");
		} else if (mlpSolutionsList.isEmpty()) {
			logger.debug(EELFLoggerDelegator.debugLogger, " CommonDataService returned empty Solution list");
		} else {
			String compoSolnTlkitTypeCode = props.getCompositSolutiontoolKitTypeCode();
			String prAccessTypeCode = props.getPrivateAccessTypeCode();
			List<MLPSolutionRevision> mlpSolutionRevisionList = null;
			for (MLPSolution mlpsolution : mlpSolutionsList) {
				if (mlpsolution.getToolkitTypeCode() != null
						&& (!mlpsolution.getToolkitTypeCode().equals(compoSolnTlkitTypeCode))) {
					List<MLPSolutionRevision> mlpSolRevisions = cmnDataService
							.getSolutionRevisions(mlpsolution.getSolutionId());
					for (MLPSolutionRevision mlpSolRevision : mlpSolRevisions) {
						String accessTypeCode = mlpSolRevision.getAccessTypeCode();
						mlpSolutionRevisionList = new ArrayList<MLPSolutionRevision>();
						if (accessTypeCode.equals(prAccessTypeCode)) {
							mlpSolutionRevisionList.add(mlpSolRevision);
						}
					}
					DSModelVO modelVO = new DSModelVO();
					modelVO.setMlpSolution(mlpsolution);
					modelVO.setMlpSolutionRevisions(mlpSolutionRevisionList);
					modelsList.add(modelVO);
				}
			}
		}
		logger.debug(EELFLoggerDelegator.debugLogger, " getPrivateDSModels() End ");
		return modelsList;
	}
	
	
	@Override
	public void populatePublicModelCacheForMatching(List<DSModelVO> models) throws ServiceException {
		logger.debug(EELFLoggerDelegator.debugLogger, " populatePublicModelCacheForMatching() Begin ");
		HashMap<KeyVO, List<ModelDetailVO>> result = null;
		
		try {
			HashMap<KeyVO, List<ModelDetailVO>> modelCache = constructModelCache(models);
			result = (HashMap<KeyVO, List<ModelDetailVO>>) modelCacheForMatching.getPublicModelCache();
			if(null == result){
				result = new HashMap<KeyVO, List<ModelDetailVO>>();
			}
			result.putAll(modelCache);
			modelCacheForMatching.setPublicModelCache(result);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, " Exception in populatePublicModelCacheForMatching() ", e);
			throw new ServiceException("Failed to read the Model");
		}
		logger.debug(EELFLoggerDelegator.debugLogger, " populatePublicModelCacheForMatching() End ");
	}
	
	
	@Override
	public void removePublicModelCacheForMatching(List<DSModelVO> models) throws ServiceException {
		logger.debug(EELFLoggerDelegator.debugLogger, " removePublicModelCacheForMatching() Begin ");
		HashMap<KeyVO, List<ModelDetailVO>> modelCache;
		try {
			modelCache = removeModelFromCache(models);
			modelCacheForMatching.setPublicModelCache(modelCache);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, " Exception in populatePublicModelCacheForMatching() ", e);
			throw new ServiceException("Failed to read the Model");
		}
		logger.debug(EELFLoggerDelegator.debugLogger, " removePublicModelCacheForMatching() End ");
	}
	
	
	@Override
	public void populatePrivateModelCacheForMatching(String userId, List<DSModelVO> models) throws ServiceException {
	    logger.debug(EELFLoggerDelegator.debugLogger, " populatePrivateModelCacheForMatching() Begin ");
		Map<KeyVO, List<ModelDetailVO>> privateModelCache = null;
		privateModelCache = modelCacheForMatching.getPrivateModelCache(userId);
		
		HashMap<KeyVO, List<ModelDetailVO>> modelCache = constructModelCache(models);
		if(null == privateModelCache){
			privateModelCache = new HashMap<KeyVO, List<ModelDetailVO>>();
		}
		privateModelCache.putAll(modelCache);
		modelCacheForMatching.setUserPrivateModelCache(userId, privateModelCache);
		Date lastExceutionTime = modelCacheForMatching.getUserPrivateModelUpdateTime(userId);
		logger.debug(EELFLoggerDelegator.debugLogger, " lastExceutionTime : " + lastExceutionTime);
		logger.debug(EELFLoggerDelegator.debugLogger, " populatePrivateModelCacheForMatching() End ");
	}
	
	private  HashMap<KeyVO, List<ModelDetailVO>> removeModelFromCache(List<DSModelVO> dsModels) throws ServiceException {
		logger.debug(EELFLoggerDelegator.debugLogger, " removeModelFromCache() Begin ");
		HashMap<KeyVO, List<ModelDetailVO>> result = null;
		result = (HashMap<KeyVO, List<ModelDetailVO>>) modelCacheForMatching.getPublicModelCache();
		
		if(null != dsModels	&& !dsModels.isEmpty() && null != result && !result.isEmpty()){
			
			List<MLPArtifact> mlpArtifacts = null;
			Tgif tgif = null;
			KeyVO key = null;
			MessageBody[] messages = null;
			Provide[] inputs = null;
			ByteArrayOutputStream byteArrayOutputStream = null;
			
			String tgifFileNexusURI = null;
			boolean isNestedMessage = false;
			int numberOfFields = 0;
			
			for(DSModelVO model : models ){
				List<MLPSolutionRevision> mlpSolRevisions = model.getMlpSolutionRevisions();
				for(MLPSolutionRevision mlpSolRevision : mlpSolRevisions ){
					mlpArtifacts = cmnDataService.getSolutionRevisionArtifacts(mlpSolRevision.getSolutionId(), mlpSolRevision.getRevisionId());
					for (MLPArtifact mlpArtifact : mlpArtifacts) {
						if (props.getArtifactType().equalsIgnoreCase(mlpArtifact.getArtifactTypeCode())) { //get TGIF file
							try {
								tgifFileNexusURI = mlpArtifact.getUri();
								logger.debug(EELFLoggerDelegator.debugLogger,  " TgifFileNexusURI  : " + tgifFileNexusURI );
								byteArrayOutputStream = getPayload(tgifFileNexusURI);
								if(null != byteArrayOutputStream && !byteArrayOutputStream.toString().isEmpty()){
									mapper.configure(Feature.AUTO_CLOSE_SOURCE, true);
									tgif = mapper.readValue(byteArrayOutputStream.toString(), Tgif.class);
								}
							} catch (Exception e) {
								logger.error(EELFLoggerDelegator.errorLogger, " exception occured in getModelCache()", e);
								throw new ServiceException("Failed to read the Model");
							}
						}
					}
					if(null != tgif){
						Service service = tgif.getServices();
						if (service != null) {
							//1. process input messages
							if(service.getProvides() != null & service.getProvides().length != 0 ){
								inputs = service.getProvides();
								
								for(Provide provide : inputs){
									//For every provide generate the keyVO 
									try {
										//Assuming that only one message as a input parameter. 
										//Currently multi input message parameter is not supported.
										messages = mapper.readValue(provide.getRequest().getFormat().toJSONString(),
												MessageBody[].class);
										for(MessageBody msgBody : messages){ //Assumption : only input message parameter
											numberOfFields = msgBody.getMessageargumentList().size(); //Number of fields.
											//Check if nested message 
											isNestedMessage = getIsNested(msgBody.getMessageargumentList());
											
											//Construct KeyVO 
											key = new KeyVO();
											key.setNestedMessage(isNestedMessage);
											key.setNumberofFields(numberOfFields);
											key.setPortType(props.getMatchingInputPortType());
											
											//check if key is present in result 
											if(result.containsKey(key)){
												result.remove(key);
											} 
										}
									} catch (IOException e) {
										logger.error(EELFLoggerDelegator.errorLogger, " exception occured in getModelCache()", e);
										throw new ServiceException("Failed to read the Model");
									}
								}
							}
							
							//2. process output messages
							if (service.getCalls() != null && service.getCalls().length != 0) {
								Call[] calls = service.getCalls(); 
								for(Call call : calls){
									//For every call generate the keyVO 
									try {
										//Assuming that only one message as a output parameter. 
										messages = mapper.readValue(call.getRequest().getFormat().toJSONString(), MessageBody[].class);
										for(MessageBody msgBody : messages){
											numberOfFields = msgBody.getMessageargumentList().size();
											isNestedMessage =  getIsNested(msgBody.getMessageargumentList());
											
											//Construct KeyVO 
											key = new KeyVO();
											key.setNestedMessage(isNestedMessage);
											key.setNumberofFields(numberOfFields);
											key.setPortType(props.getMatchingOutputPortType());
											
											//check if key is present in result
											if(result.containsKey(key)){
												result.remove(key);
											}
										}
									} catch (IOException e) {
										logger.error(EELFLoggerDelegator.errorLogger, " exception occured in getModelCache()", e);
										throw new ServiceException("Failed to read the Model");
									}
								}
							}
						}
					}
				}
			}
		}
		logger.debug(EELFLoggerDelegator.debugLogger, " removeModelFromCache() End ");
		return result; 
	}
	
	private HashMap<KeyVO, List<ModelDetailVO>> constructModelCache(List<DSModelVO> models) throws ServiceException {
		logger.debug(EELFLoggerDelegator.debugLogger, " constructModelCache() Begin ");
		HashMap<KeyVO, List<ModelDetailVO>> result = null;
		List<ModelDetailVO> modelDetailVOs = null;
		List<MLPArtifact> mlpArtifacts = null;
		ByteArrayOutputStream byteArrayOutputStream = null;
		Tgif tgif = null;
		Provide[] inputs = null;
		KeyVO key = null;
		ModelDetailVO value = null;
		MessageBody[] messages = null;
		
		boolean isNestedMessage = false;
		int numberOfFields = 0;
		String tgifFileNexusURI = null;
		
		result = new HashMap<KeyVO, List<ModelDetailVO>>();
		for(DSModelVO model : models) {
			MLPSolution solution = model.getMlpSolution();
			List<MLPSolutionRevision> mlpSolRevisions = model.getMlpSolutionRevisions();
			for(MLPSolutionRevision mlpSolRevision : mlpSolRevisions ){
				mlpArtifacts = cmnDataService.getSolutionRevisionArtifacts(mlpSolRevision.getSolutionId(), mlpSolRevision.getRevisionId());
				for (MLPArtifact mlpArtifact : mlpArtifacts) {
					if (props.getArtifactType().equalsIgnoreCase(mlpArtifact.getArtifactTypeCode())) { //get TGIF file
						try {
							tgifFileNexusURI = mlpArtifact.getUri();
							logger.debug(EELFLoggerDelegator.debugLogger,  " TgifFileNexusURI 1  : " + tgifFileNexusURI );
							byteArrayOutputStream = getPayload(tgifFileNexusURI);
							if(null != byteArrayOutputStream && !byteArrayOutputStream.toString().isEmpty()){
								mapper.configure(Feature.AUTO_CLOSE_SOURCE, true);
								tgif = mapper.readValue(byteArrayOutputStream.toString(), Tgif.class);
							}
							if(null != tgif){
								Service service = tgif.getServices();
								if (service != null) {
									//1. process input messages
									if(service.getProvides() != null & service.getProvides().length != 0 ){
										inputs = service.getProvides();
										
										for(Provide provide : inputs){
											//For every provide generate the keyVO 
											try {
												//Assuming that only one message as a input parameter. 
												//Currently multi input message parameter is not supported.
												messages = mapper.readValue(provide.getRequest().getFormat().toJSONString(),
														MessageBody[].class);
												for(MessageBody msgBody : messages){ //Assumption : onlye input message parameter
													numberOfFields = msgBody.getMessageargumentList().size(); //Number of fields.
													//Check if nested message 
													isNestedMessage = getIsNested(msgBody.getMessageargumentList());
													
													//Construct KeyVO 
													key = new KeyVO();
													key.setNestedMessage(isNestedMessage);
													key.setNumberofFields(numberOfFields);
													key.setPortType(props.getMatchingInputPortType());
													
													//Construct ValueVO 
													value = new ModelDetailVO();
													value.setModelId(mlpSolRevision.getSolutionId());
													value.setModelName(solution.getName());
													value.setProtobufJsonString(provide.getRequest().getFormat().toJSONString());
													value.setRevisionId(mlpSolRevision.getRevisionId());
													value.setTgifFileNexusURI(tgifFileNexusURI);
													value.setVersion(mlpSolRevision.getVersion());
													
													//check if key is present in result 
													if(result.containsKey(key)){
														modelDetailVOs = result.get(key);
													} else {
														modelDetailVOs = new ArrayList<ModelDetailVO>();
													}
													modelDetailVOs.add(value);
													result.put(key, modelDetailVOs);
												}
											} catch (IOException e) {
												logger.error(EELFLoggerDelegator.errorLogger, " exception occured in getModelCache()", e);
												throw new ServiceException("Failed to read the Model");
											}
										}
									}
									
									//2. process output messages
									if (service.getCalls() != null && service.getCalls().length != 0) {
										Call[] calls = service.getCalls(); 
										for(Call call : calls){
											//For every call generate the keyVO 
											try {
												//Assuming that only one message as a output parameter. 
												messages = mapper.readValue(call.getRequest().getFormat().toJSONString(), MessageBody[].class);
												for(MessageBody msgBody : messages){
													numberOfFields = msgBody.getMessageargumentList().size();
													isNestedMessage =  getIsNested(msgBody.getMessageargumentList());
													
													//Construct KeyVO 
													key = new KeyVO();
													key.setNestedMessage(isNestedMessage);
													key.setNumberofFields(numberOfFields);
													key.setPortType(props.getMatchingOutputPortType());
													
													//Construct ValueVO 
													value = new ModelDetailVO();
													value.setModelId(mlpSolRevision.getSolutionId());
													value.setModelName(solution.getName());
													value.setProtobufJsonString(call.getRequest().getFormat().toJSONString());
													value.setRevisionId(mlpSolRevision.getRevisionId());
													value.setTgifFileNexusURI(tgifFileNexusURI);
													value.setVersion(mlpSolRevision.getVersion());
													
													//check if key is present in result
													if(result.containsKey(key)){
														modelDetailVOs = result.get(key);
													}else {
														modelDetailVOs = new ArrayList<ModelDetailVO>();
													}
													modelDetailVOs.add(value);
													result.put(key,modelDetailVOs);
												}
											} catch (IOException e) {
												logger.error(EELFLoggerDelegator.errorLogger, " exception occured in getModelCache()", e);
												throw new ServiceException("Failed to read the Model");
											}
											
										}
									}
								}
							}
							
						} catch (Exception e) {
							logger.error(EELFLoggerDelegator.errorLogger, " exception occured in getModelCache()", e);
							throw new ServiceException("Exception Occured while reading the TGIF");
						}
					}
				}
				
			}
		}
		logger.debug(EELFLoggerDelegator.debugLogger, " constructModelCache() End ");
		return result;
	}
	
	private boolean getIsNested(List<MessageargumentList> messagearguments) {
		logger.debug(EELFLoggerDelegator.debugLogger, " getIsNested() Begin ");
		boolean isNestedMessage = false;
		ComplexType complexType = null;
		for(MessageargumentList msgargument : messagearguments) {
			complexType = msgargument.getComplexType();
			if(null != complexType) { 
				isNestedMessage = true;
				break;
			}
		}
		logger.debug(EELFLoggerDelegator.debugLogger, " getIsNested() End ");
		return isNestedMessage;
	}
	
	private ByteArrayOutputStream getPayload(String uri) throws Exception{
		ByteArrayOutputStream outputStream = null;
		try {
			outputStream = nexusArtifactClient.getArtifact(uri);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, "Exception in getPayload()", e);
			throw new ServiceException("Exception Occured in getPayload() method");
		}
		return outputStream;
	}


	

	
	
	
	
}


