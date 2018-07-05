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
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.acumos.cds.client.CommonDataServiceRestClientImpl;
import org.acumos.cds.domain.MLPArtifact;
import org.acumos.cds.domain.MLPSolution;
import org.acumos.cds.domain.MLPSolutionRevision;
import org.acumos.cds.domain.MLPUser;
import org.acumos.cds.transport.RestPageRequest;
import org.acumos.cds.transport.RestPageResponse;
import org.acumos.designstudio.ce.exceptionhandler.AcumosException;
import org.acumos.designstudio.ce.exceptionhandler.ServiceException;
import org.acumos.designstudio.ce.util.ConfigurationProperties;
import org.acumos.designstudio.ce.util.DSUtil;
import org.acumos.designstudio.ce.util.EELFLoggerDelegator;
import org.acumos.designstudio.ce.util.Properties;
import org.acumos.designstudio.ce.vo.DSSolution;
import org.acumos.designstudio.ce.vo.MatchingModel;
import org.acumos.designstudio.ce.vo.cdump.Cdump;
import org.acumos.designstudio.ce.vo.cdump.Ndata;
import org.acumos.designstudio.ce.vo.cdump.Nodes;
import org.acumos.designstudio.ce.vo.cdump.Property;
import org.acumos.designstudio.ce.vo.cdump.Relations;
import org.acumos.designstudio.ce.vo.cdump.collator.CollatorInputField;
import org.acumos.designstudio.ce.vo.cdump.collator.CollatorMap;
import org.acumos.designstudio.ce.vo.cdump.collator.CollatorMapInput;
import org.acumos.designstudio.ce.vo.cdump.collator.CollatorMapOutput;
import org.acumos.designstudio.ce.vo.cdump.databroker.DBInputField;
import org.acumos.designstudio.ce.vo.cdump.databroker.DBMapInput;
import org.acumos.designstudio.ce.vo.cdump.databroker.DBMapOutput;
import org.acumos.designstudio.ce.vo.cdump.databroker.DataBrokerMap;
import org.acumos.designstudio.ce.vo.cdump.datamapper.DataMap;
import org.acumos.designstudio.ce.vo.cdump.datamapper.DataMapInputField;
import org.acumos.designstudio.ce.vo.cdump.datamapper.FieldMap;
import org.acumos.designstudio.ce.vo.cdump.datamapper.MapInputs;
import org.acumos.designstudio.ce.vo.cdump.datamapper.MapOutput;
import org.acumos.designstudio.ce.vo.cdump.splitter.SplitterMap;
import org.acumos.designstudio.ce.vo.cdump.splitter.SplitterMapInput;
import org.acumos.designstudio.ce.vo.cdump.splitter.SplitterMapOutput;
import org.acumos.designstudio.ce.vo.cdump.splitter.SplitterOutputField;
import org.acumos.designstudio.ce.vo.protobuf.MessageBody;
import org.acumos.designstudio.ce.vo.protobuf.MessageargumentList;
import org.acumos.designstudio.ce.vo.tgif.Call;
import org.acumos.designstudio.ce.vo.tgif.Provide;
import org.acumos.designstudio.ce.vo.tgif.Tgif;
import org.acumos.nexus.client.NexusArtifactClient;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;

@Service("solutionServiceImpl")
public class SolutionServiceImpl implements ISolutionService {
	private static EELFLoggerDelegator logger = EELFLoggerDelegator.getLogger(SolutionServiceImpl.class);
	
	@Autowired
	MatchingModelServiceComponent matchingModelServiceComponent;

	@Autowired
	Properties props;

	@Autowired
	ConfigurationProperties confprops;

	@Autowired
	CommonDataServiceRestClientImpl cmnDataService;

	@Autowired
	NexusArtifactClient nexusArtifactClient;
	
	private final ObjectMapper mapper = new ObjectMapper();

	@Override
	public String getSolutions(String userID) throws ServiceException {

		logger.debug(EELFLoggerDelegator.debugLogger, " getSolutions() Begin ");
		String result = null;
		List<MLPSolution> mlpSolutionsList = null;
		String solutionId = null;
		DSSolution dssolution = null;
		List<DSSolution> dsSolutionList = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat(confprops.getDateFormat());
		
		try {
			mapper.setSerializationInclusion(Include.NON_NULL);
			
			Map<String, Object> queryParameters = new HashMap<>();
			queryParameters.put("active", Boolean.TRUE);
			// Code changes are to match the change in the CDS API Definition searchSolution in version 1.13.x
			RestPageResponse<MLPSolution> pageResponse = cmnDataService.searchSolutions(queryParameters, false,
					new RestPageRequest(0, props.getSolutionResultsetSize()));
			mlpSolutionsList = pageResponse.getContent();
			logger.debug(EELFLoggerDelegator.debugLogger, " The Date Format :  {} ", confprops.getDateFormat());
			if (null == mlpSolutionsList) {
				logger.debug(EELFLoggerDelegator.debugLogger," CommonDataService returned null Solution list");
			} else if (mlpSolutionsList.isEmpty()) {
				logger.debug(EELFLoggerDelegator.debugLogger," CommonDataService returned empty Solution list");
			} else {
				logger.debug(EELFLoggerDelegator.debugLogger," CommonDataService returned Solution list of size :  {} ", mlpSolutionsList.size());
				List<MLPSolution> matchingModelsolutionList  = new ArrayList<MLPSolution>();
				// Get the TypeCodes from Properties file
				String compoSolnTlkitTypeCode = props.getCompositSolutiontoolKitTypeCode();
				String pbAccessTypeCode = props.getPublicAccessTypeCode();
				String prAccessTypeCode = props.getPrivateAccessTypeCode();
				String orAccessTypeCode = props.getOrganizationAccessTypeCode();
				// For each solution where toolkittypeCode is not null and not equal to "CP".
				for (MLPSolution mlpsolution : mlpSolutionsList) {
					if (mlpsolution.getToolkitTypeCode() != null && (!mlpsolution.getToolkitTypeCode().equals(compoSolnTlkitTypeCode))) {
						List<MLPSolutionRevision> mlpSolRevisions = cmnDataService.getSolutionRevisions(solutionId);
						for (MLPSolutionRevision mlpSolRevision : mlpSolRevisions) {
							String accessTypeCode = mlpSolRevision.getAccessTypeCode();
							if ((accessTypeCode.equals(pbAccessTypeCode)) || (mlpSolRevision.getOwnerId().equals(userID) && accessTypeCode.equals(prAccessTypeCode)) 
									 || (accessTypeCode.equals(orAccessTypeCode)) ) {
								String revisionUserID = mlpSolRevision.getOwnerId();
								MLPUser mlpUser = cmnDataService.getUser(revisionUserID);
								String userName = mlpUser.getFirstName() + " " + mlpUser.getLastName();
								dsSolutionList.add(populateDsSolution(mlpsolution, sdf, userName, mlpSolRevision));
								matchingModelsolutionList.add(mlpsolution);
							}
						}
					}
				}
				matchingModelServiceComponent.setMatchingModelsolutionList(matchingModelsolutionList);
				matchingModelServiceComponent.setMatchingModelRevisionList(null);
				matchingModelServiceComponent.setMatchingModelArtifactTigifFileList(null);
			}

			if (dsSolutionList.size() > 1) {
				dsSolutionList = checkDuplicateSolution(dsSolutionList);
				result = mapper.writeValueAsString(dsSolutionList);
			} else {
				result = props.getSolutionErrorDescription();
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, " Exception in getSolutions() ", e);
			throw new ServiceException("  Exception in getSolutions() ", props.getSolutionErrorCode(),
					props.getSolutionErrorDesc());
		}
		logger.debug(EELFLoggerDelegator.debugLogger, " getSolutions() End ");
		return result;
	}

	private List<DSSolution> checkDuplicateSolution(List<DSSolution> dsSolutionList) {
		//Check for solutions with same name and version
		List<DSSolution> clonedSolution = new ArrayList<DSSolution>();
		clonedSolution.addAll(dsSolutionList);
		List<DSSolution> result = new ArrayList<DSSolution>();
		int cnt = 0;
		for(DSSolution dss : dsSolutionList){
			//check if it appears twice in clone
			cnt = 0;
			logger.debug(EELFLoggerDelegator.debugLogger, " dss.getSolutionName() " + dss.getSolutionName());
            logger.debug(EELFLoggerDelegator.debugLogger, " dss.getVersion() " + dss.getVersion());
            if(null == dss.getSolutionName() && null == dss.getVersion() ){
            	break;
            }
			for(DSSolution dss1 : clonedSolution ){
                
                if(null == dss1.getSolutionName() && null == dss1.getVersion()){
                	break;
                } else if(dss.getSolutionName().equals(dss1.getSolutionName()) && dss.getVersion().equals(dss1.getVersion())){
                	logger.debug(EELFLoggerDelegator.debugLogger, " dss1.getSolutionName() " + dss1.getSolutionName());
                    logger.debug(EELFLoggerDelegator.debugLogger, " dss1.getVersion() " + dss1.getVersion());
					cnt++;
				}
				if(cnt == 2){  //indicating that same solution name and version appeared twice, so no need to check further 
					break;
				}
				
			}
			if(cnt == 1){
				dss.setSolutionRevisionId("");
			}
			result.add(dss);
		}
		return result;
	}

	private List<DSSolution> buildSolutionDetails(MLPSolution mlpsolution, CommonDataServiceRestClientImpl cmnDataService,
			String solutionId, SimpleDateFormat sdf) {
		logger.debug(EELFLoggerDelegator.debugLogger, " buildSolutionDetails() Begin ");
		DSSolution dssolution = null;
		List<DSSolution> dsSolutions = new ArrayList<DSSolution>();
		try {
			solutionId = mlpsolution.getSolutionId();
			List<MLPSolutionRevision>  mlpSolRevisionList = cmnDataService.getSolutionRevisions(solutionId);
			String userId = mlpsolution.getOwnerId();
			MLPUser user = cmnDataService.getUser(userId);
			String userName = user.getFirstName() + " " + user.getLastName();
			if (null == mlpSolRevisionList) {
				logger.debug(EELFLoggerDelegator.debugLogger," CommonDataService returned null SolutionRevision list");
			} else if (mlpSolRevisionList.isEmpty()) {
				logger.debug(EELFLoggerDelegator.debugLogger," CommonDataService returned empty SolutionRevision list");
			} else {
				logger.debug(EELFLoggerDelegator.debugLogger," CommonDataService returned SolutionRevision list of size : " + mlpSolRevisionList.size() );

				for (MLPSolutionRevision mlpSolRevision : mlpSolRevisionList) {
					dssolution = populateDsSolution(mlpsolution, sdf, userName, mlpSolRevision);
					dsSolutions.add(dssolution);
				}
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, " Exception in buildSolutionDetails() ",e);
		}
		logger.debug(EELFLoggerDelegator.debugLogger, " buildSolutionDetails() Ends ");
		return dsSolutions;
	}

	private DSSolution populateDsSolution(MLPSolution mlpsolution, SimpleDateFormat sdf, String userName,
			MLPSolutionRevision mlpSolRevision) {
		DSSolution dssolution;
		dssolution = new DSSolution();
		// 1. SolutionId
		dssolution.setSolutionId(mlpsolution.getSolutionId());
		// 2. Solution Created Date
		dssolution.setCreatedDate(sdf.format(mlpSolRevision.getCreated().getTime()));
		// 3. Solution Icon
		dssolution.setIcon(null);
		// 4. Solution Name
		dssolution.setSolutionName(mlpsolution.getName());
		// 5. Solution Provider
		dssolution.setProvider(mlpsolution.getProvider());
		// 6. Solution Tool Kit
		dssolution.setToolKit(mlpsolution.getToolkitTypeCode());
		// 7. Solution Category
		dssolution.setCategory(mlpsolution.getModelTypeCode());
		// 8. Solution Description
		dssolution.setDescription(mlpsolution.getDescription());
		// 9. Solution Visibility
		dssolution.setVisibilityLevel(mlpSolRevision.getAccessTypeCode());
		// 10. Solution Version
		dssolution.setVersion(mlpSolRevision.getVersion());
		// 11. Solution On boarder
		dssolution.setOnBoarder(userName);
		// 12. Solution Author
		dssolution.setAuthor(userName);
		// 13. Set RevisionId 
		dssolution.setSolutionRevisionId(mlpSolRevision.getRevisionId());
		return dssolution;
	}

	@Override
	public String createNewCompositeSolution(String userId) throws AcumosException {
		logger.debug(EELFLoggerDelegator.debugLogger, " createNewCompositeSolution() : Begin ");
		String response = null;
		String sessionId = "";
		try {
			if (userId != null) {
				Cdump cdump = new Cdump();
				UUID id = UUID.randomUUID();
				sessionId = id.toString();
				cdump.setCid(sessionId);
				SimpleDateFormat sdf = new SimpleDateFormat(confprops.getDateFormat());
				cdump.setCtime(sdf.format(new Date()));
				cdump.setProbeIndicator("false");
				cdump.setValidSolution(false);
				Gson gson = new Gson();
				String emptyCdumpJson = gson.toJson(cdump);
				String path = DSUtil.createCdumpPath(userId, confprops.getToscaOutputFolder());
				DSUtil.writeDataToFile(path, "acumos-cdump" + "-" + id, "json", emptyCdumpJson);
				logger.debug(EELFLoggerDelegator.debugLogger, emptyCdumpJson);
				response = "{\"cid\":\"" + sessionId + "\",\"success\":\"true\",\"errorMessage\":\"\"}";
			} else {
				response = "{\"cid\":\"" + sessionId
						+ "\",\"success\":\"false\",\"errorMessage\":\"User Id Required\"}";
				logger.debug(EELFLoggerDelegator.debugLogger, response);
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger,
					" Exception Occured in createNewCompositeSolution() ", e);
			throw new NoSuchElementException("Failed to create NewCompositeSolution");
		}
		logger.debug(EELFLoggerDelegator.debugLogger, " createNewCompositeSolution() : End ");
		return response;
	}

	@Override
	public String addNode(String userId, String solutionId, String version, String cid, Nodes node) {
		String results = "";
		String resultTemplate = "{\"success\" : \"%s\", \"errorDescription\" : \"%s\"}";
		logger.debug(EELFLoggerDelegator.debugLogger, " addNode() : Begin  ");
		Property[] propertyarray = node.getProperties();
		try {
			mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
			mapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
			String id = "";
			if (null != cid && null == solutionId) {
				id = cid;
			} else if (null == cid) {
				id = solutionId;
			}
			String cdumpFileName = "acumos-cdump" + "-" + id + ".json";
			String path = DSUtil.readCdumpPath(userId, confprops.getToscaOutputFolder());
			Cdump cdump = mapper.readValue(new File(path.concat(cdumpFileName)), Cdump.class);
			List<Nodes> nodes = cdump.getNodes();
			ArrayList<String> idList = new ArrayList<>();
			Nodes node1 = new Nodes();
			if (nodes != null) {
				for (Nodes n : nodes) {
					idList.add(n.getNodeId());
				}
				if (idList.contains(node.getNodeId())) {
					results = String.format(resultTemplate, false,
							"Node Id already exists – cannot perform the requested operation");
				} else {
					node1.setNodeId(node.getNodeId());
					node1.setName(node.getName());
					node1.setRequirements(node.getRequirements());
					node1.setCapabilities(node.getCapabilities());
					node1.setNdata(node.getNdata());
					node1.setNodeSolutionId(node.getNodeSolutionId());
					node1.setNodeVersion(node.getNodeVersion());
					node1.setProtoUri(
							getProtoUrl(node.getNodeSolutionId(), node.getNodeVersion(), props.getModelImageArtifactType(), props.getProtobuffFileExtention()));
					if (node.getProperties() == null) {
						node1.setProperties(propertyarray);
					} else {
						node1.setProperties(propertyarray);
					}
					if (node.getTypeInfo() == null) {
						node1.setTypeInfo(null);
					} else {
						node1.setTypeInfo(node.getTypeInfo());
					}
					if (node.getType() == null) {
						node1.setType(null);
					} else {
						node1.setType(node.getType());
					}
					nodes.add(node1);
					cdump.setNodes(nodes);
					results = String.format(resultTemplate, true, "");
				}
			} else {
				node1.setNodeId(node.getNodeId());
				node1.setName(node.getName());
				node1.setRequirements(node.getRequirements());
				node1.setCapabilities(node.getCapabilities());
				node1.setNdata(node.getNdata());
				node1.setNodeSolutionId(node.getNodeSolutionId());
				node1.setNodeVersion(node.getNodeVersion());
				node1.setProtoUri(
						getProtoUrl(node.getNodeSolutionId(), node.getNodeVersion(), props.getModelImageArtifactType(), props.getProtobuffFileExtention()));
				if (node.getProperties() == null) {
					node1.setProperties(propertyarray);
				} else {
					node1.setProperties(propertyarray);
				}
				if (node.getTypeInfo() == null) {
					node1.setTypeInfo(null);
				} else {
					node1.setTypeInfo(node.getTypeInfo());
				}
				if (node.getType() == null) {
					node1.setType(null);
				} else {
					node1.setType(node.getType());
				}
				List<Nodes> nodeList = new ArrayList<>();
				nodeList.add(node1);
				cdump.setNodes(nodeList);
				results = String.format(resultTemplate, true, "");
			}
			cdump.setCid(cid);
			cdump.setSolutionId(solutionId);
			cdump.setVersion(version);
			mapper.writeValue(new File(path.concat(cdumpFileName)), cdump);

		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, " Exception in addNode() ", e);
			results = String.format(resultTemplate, false, "Node not Added");
		}
		logger.debug(EELFLoggerDelegator.debugLogger, " addNode() : End  ");
		return results;
	}

	@Override
	public String readCompositeSolutionGraph(String userId, String solutionID, String version) throws AcumosException {
		boolean isValidVersion = false;
		String solutionRevisionId = "";
		logger.debug(EELFLoggerDelegator.debugLogger, " fetchJsonTOSCA()  : Begin ");
		String result = "";
		ByteArrayOutputStream byteArrayOutputStream = null;
		try {
			if (isSolutionIdValid(solutionID)) {
				List<MLPSolutionRevision> rev = getSolutionRevisions(solutionID);
				if (null != rev && !rev.isEmpty()) {
					for (MLPSolutionRevision mlp : rev) {
						if (mlp.getVersion().equalsIgnoreCase(version)) {
							solutionRevisionId = mlp.getRevisionId();
							isValidVersion = true;
							logger.debug(EELFLoggerDelegator.debugLogger,
									" SolutionRevisonId for Version :  {} ", solutionRevisionId );
							break;
						}
					}
				}

			} else {
				return "{\"error\": \"Requested Solution Not Found\"}";
			}
			if (false == isValidVersion) {
				return "{\"error\": \"Requested Version Not Found\"}";
			}
			List<MLPArtifact> mlpArtifact = getListOfArtifacts(solutionID, solutionRevisionId);
			String nexusURI = "";
			if (null != mlpArtifact && !mlpArtifact.isEmpty()) {
				for (MLPArtifact mlpArti : mlpArtifact) {
					if (mlpArti.getArtifactTypeCode().equalsIgnoreCase(props.getArtifactTypeCode())) {
						nexusURI = mlpArti.getUri();
						logger.debug(EELFLoggerDelegator.debugLogger, " Nexus URI :  {} ", nexusURI );
						break;
					}
				}
			}

			if (null != nexusURI && !"".equals(nexusURI)) {
				byteArrayOutputStream = getPayload(nexusURI);
				result = byteArrayOutputStream.toString();
				String path = DSUtil.createCdumpPath(userId, confprops.getToscaOutputFolder());
				DSUtil.writeDataToFile(path, "acumos-cdump" + "-" + solutionID, "json", result);
				logger.debug(EELFLoggerDelegator.debugLogger,
						" Response in String Format :  {} ", result );

			} else {
				result = "{\"error\": \"CDUMP Artifact Not Found for this solution\"}";
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, " exception occured in readCompositeSolutionGraph()",
					e);
			throw new ServiceException("Failed to read the CompositeSolution");
		} finally {
			try {
				if (byteArrayOutputStream != null) {
					byteArrayOutputStream.close();
				}
			} catch (IOException e) {
				logger.error(EELFLoggerDelegator.errorLogger,
						"Error : Exception in readArtifact() : Failed to close the byteArrayOutputStream", e);
				throw new ServiceException("Failed to read the CompositeSolution");
			}
		}
		logger.debug(EELFLoggerDelegator.debugLogger, " fetchJsonTOSCA()  : End ");
		return result;
	}

	private List<MLPSolutionRevision> getSolutionRevisions(String solutionId) throws Exception {
		logger.debug(EELFLoggerDelegator.debugLogger, " getSolutionRevisions() : Begin ");
		List<MLPSolutionRevision> solRevisions = null;
		try {
			solRevisions = cmnDataService.getSolutionRevisions(solutionId);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, " Exception in getSolutionRevisions() ", e);
			throw e;
		}
		logger.debug(EELFLoggerDelegator.debugLogger, " getSolutionRevisions() : End ");
		return solRevisions;
	}

	private List<MLPArtifact> getListOfArtifacts(String solutionId, String solutionRevisionId) throws Exception{
		List<MLPArtifact> mlpArtifacts = null;
		try {
			mlpArtifacts = cmnDataService.getSolutionRevisionArtifacts(solutionId, solutionRevisionId);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, " Exception in getListOfArtifacts() ", e);
			throw e;
		}
		return mlpArtifacts;
	}

	private ByteArrayOutputStream getPayload(String uri) throws Exception{
		ByteArrayOutputStream outputStream = null;
		try {
			outputStream = nexusArtifactClient.getArtifact(uri);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, "Exception in getPayload()", e);
			throw e;
		}
		return outputStream;
	}

	private boolean isSolutionIdValid(String solutionId) {
		try {
			MLPSolution mLPSolution = cmnDataService.getSolution(solutionId);
			if (null != mLPSolution.getSolutionId()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			logger.error(EELFLoggerDelegator.errorLogger, " Exception in isSolutionIdValid() ", ex);
			return false;
		}
	}

	@Override
	public String modifyNode(String userId, String solutionId, String version, String cid, String nodeId,
			String nodeName, String ndata, FieldMap fieldmap, DataBrokerMap databrokerMap, CollatorMap collatorMap, SplitterMap splitterMap) {
		logger.debug(EELFLoggerDelegator.debugLogger, " modifyNode() : Begin");
		String results = "";
		String resultTemplate = "{\"success\" : \"%s\", \"errorDescription\" : \"%s\"}";
		try {
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
			mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
			mapper.setSerializationInclusion(Include.NON_NULL);
			Cdump cdump = null;
			String id = "";
			if (null != cid && null == solutionId) {
				id = cid;
			} else if (null == cid) {
				id = solutionId;
			}
			String cdumpFileName = "acumos-cdump" + "-" + id;
			String path = DSUtil.readCdumpPath(userId, confprops.getToscaOutputFolder());
			cdump = mapper.readValue(new File(path.concat(cdumpFileName).concat(".json")), Cdump.class);
			List<Nodes> cdumpNodeList = cdump.getNodes();

			if (null == cdumpNodeList || cdumpNodeList.isEmpty()) {
				results = String.format(resultTemplate, false, "Invalid Node Id – not found");
			} else {
				for (Nodes nodesData : cdumpNodeList) {
					if (nodesData.getNodeId().equals(nodeId)) {
						if (null != nodeName) {
							nodesData.setName(nodeName);
						}
						if (null != ndata && ndata.length() != 0) {
							Ndata data1 = new Gson().fromJson(ndata, Ndata.class);
							Ndata dat = nodesData.getNdata();
							dat.setPx(data1.getPx());
							dat.setNtype(data1.getNtype());
							dat.setFixed(data1.isFixed());
							dat.setPy(data1.getPy());
							dat.setRadius(data1.getRadius());
							nodesData.setNdata(dat);
						}
						// Code to modify the link if node names are changed
						List<Relations> relations = cdump.getRelations();
						if (null != relations && !relations.isEmpty()) {
							// iterate through relation
							for (Relations relation : relations) {
								// check if the relation contains the specified nodeId
								if (relation.getSourceNodeId().equals(nodeId)) {
									relation.setSourceNodeName(nodeName);
								} else if (relation.getTargetNodeId().equals(nodeId)) {
									relation.setTargetNodeName(nodeName);
								}
							}
						}
						Property properties[] = nodesData.getProperties();
						// to update the DataMapper
						if (null != fieldmap && fieldmap.toString().length() != 0) {
							updateDataMapper(fieldmap, properties);
						}
						// to update the DataBroker
						if (null != databrokerMap && databrokerMap.toString().length() != 0) {
							updateDataBroker(databrokerMap, nodesData);
						}
						// to update the CollatorMap
						if (null != collatorMap && collatorMap.toString().length() != 0) {
							updateCollatorMap(collatorMap, nodesData);
						}
						// to update the SplitterMap
						if (null != splitterMap && splitterMap.toString().length() != 0) {
							updateSplitterMap(splitterMap, nodesData);
						}
						results = String.format(resultTemplate, true, "");
						break;
					} else {
						results = String.format(resultTemplate, false, "Invalid Node Id – not found");
					}
				}
				mapper.writeValue(new File(path.concat(cdumpFileName).concat(".json")), cdump);
				logger.debug(EELFLoggerDelegator.debugLogger, " Node Modified Successfully ");
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, "Exception in  modifyNode() ", e);
			results = String.format(resultTemplate, false, "Not able to modify the Node");
		}
		logger.debug(EELFLoggerDelegator.debugLogger, " modifyNode()   : End");
		return results;
	}

	private void updateSplitterMap(SplitterMap splitterMap, Nodes nodesData) {
		logger.debug(EELFLoggerDelegator.debugLogger, " modifyNode()  : Begin");
		Property properties[] = nodesData.getProperties();
		Property newProperty = null;
		// If In case of properties Contains SplitterMap 
		ArrayList<Property> propertyList = new ArrayList<Property>(Arrays.asList(properties));
		if (null != properties && properties.length != 0) {
			for (Property p : propertyList) {
				if (p.getSplitter_map() != null) {
					SplitterMap sMap = p.getSplitter_map();
					if (null != splitterMap.getSplitter_type()) {
						sMap.setSplitter_type(splitterMap.getSplitter_type());
					}
					if (null != splitterMap.getMap_inputs()) {
						sMap.setMap_inputs(splitterMap.getMap_inputs());
					}
					if (null != splitterMap.getMap_outputs()) {
						sMap.setMap_outputs(splitterMap.getMap_outputs());
					}
					if(null != splitterMap.getInput_message_signature()){
						sMap.setInput_message_signature(splitterMap.getInput_message_signature());
					}
					p.setSplitter_map(sMap);
				} 
			}
			nodesData.setProperties(properties);
		} else {  //if in case properties for the node is empty. 
			newProperty = new Property();
			newProperty.setSplitter_map(splitterMap);
			Property newproperties[] = new Property[1];
			newproperties[0] = newProperty;
			nodesData.setProperties(newproperties);
			logger.debug(EELFLoggerDelegator.debugLogger, " modifyNode() : End");
		}
	}

	private void updateCollatorMap(CollatorMap collatorMap, Nodes nodesData) {
		logger.debug(EELFLoggerDelegator.debugLogger, " modifyNode()  : Begin");
		Property properties[] = nodesData.getProperties();
		Property newProperty = null;
		ArrayList<Property> propertyList = new ArrayList<Property>(Arrays.asList(properties));
		// If In case of properties Contains CollatorMap 
		if (null != properties && properties.length != 0) {
			for (Property p : propertyList) {
				if (p.getCollator_map() != null) {
					CollatorMap cMap = p.getCollator_map();
					if (null != collatorMap.getCollator_type()) {
						cMap.setCollator_type(collatorMap.getCollator_type());
					}
					if (null != collatorMap.getMap_inputs()) {
						cMap.setMap_inputs(collatorMap.getMap_inputs());
					}
					if (null != collatorMap.getMap_outputs()) {
						cMap.setMap_outputs(collatorMap.getMap_outputs());
					}
					if(null != collatorMap.getOutput_message_signature()){
						cMap.setOutput_message_signature(collatorMap.getOutput_message_signature());
					}
					p.setCollator_map(cMap);
				} 
			}
			nodesData.setProperties(properties);
		} else {  //if in case properties for the node is empty. 
			newProperty = new Property();
			newProperty.setCollator_map(collatorMap);
			Property newproperties[] = new Property[1];
			newproperties[0] = newProperty;
			nodesData.setProperties(newproperties);
			logger.debug(EELFLoggerDelegator.debugLogger, " modifyNode() : End");
		}
	}

	private void updateDataBroker(DataBrokerMap databrokerMap, Nodes nodesData) {
		logger.debug(EELFLoggerDelegator.debugLogger, " modifyNode()  : Begin");
		Property properties[] = nodesData.getProperties();
		Property newProperty = null;
		if(null != properties){
			// For New Solution Create the Property[]
			newProperty = new Property();
			newProperty.setData_broker_map(databrokerMap);
			
			//check if the databrokerMap already exist. 
			ArrayList<Property> propertyList = new ArrayList<Property>(Arrays.asList(properties));
			
			if(propertyList.size() == 0){ //if the properties is empty then add the new property with databroket map. 
				propertyList.add(newProperty);
			} else {
				for(Property p : propertyList){
					if(p.getData_broker_map() != null){ //else if data broker map exist and update the same.
						DataBrokerMap dataBrokerMap = p.getData_broker_map();
						
						if(null != databrokerMap.getCsv_file_field_separator()){
							dataBrokerMap.setCsv_file_field_separator(databrokerMap.getCsv_file_field_separator());
						}
						if(null != databrokerMap.getData_broker_type()){
							dataBrokerMap.setData_broker_type(databrokerMap.getData_broker_type());
						}
						if(null != databrokerMap.getFirst_row()){
							dataBrokerMap.setFirst_row(databrokerMap.getFirst_row());
						}
						if(null != databrokerMap.getLocal_system_data_file_path()){
							dataBrokerMap.setLocal_system_data_file_path(databrokerMap.getLocal_system_data_file_path());
						}
						if(null != databrokerMap.getScript()){
							dataBrokerMap.setScript(databrokerMap.getScript());
						}
						if(null != databrokerMap.getTarget_system_url()){
							dataBrokerMap.setTarget_system_url(databrokerMap.getTarget_system_url());
						}
						if (null != databrokerMap.getMap_inputs()
								&& null != databrokerMap.getMap_outputs()) {
							dataBrokerMap.setMap_inputs(databrokerMap.getMap_inputs());
							dataBrokerMap.setMap_outputs(databrokerMap.getMap_outputs());
						}
						p.setData_broker_map(dataBrokerMap);
					} else {
						propertyList.add(newProperty); //else add the new databroker map to non empty properties. 
					}
				}
			}
			properties = propertyList.toArray(new Property[propertyList.size()]);
			int cnt = 0;
			for(Property p : propertyList){
				properties[cnt] = p;
				cnt++;
			}
			logger.debug(EELFLoggerDelegator.debugLogger, " modifyNode()  : End");
			nodesData.setProperties(properties);
		}
	}

	private void updateDataMapper(FieldMap fieldmap, Property[] properties) {
		logger.debug(EELFLoggerDelegator.debugLogger, " modifyNode(): Begin");
		if (null != properties && properties.length != 0) {
			// iterate through each property
			for (Property props : properties) {
				DataMap datamap = props.getData_map();
				if (null != datamap && !datamap.toString().isEmpty()) {
					MapInputs[] mapInputs = datamap.getMap_inputs();
					if (null != mapInputs && mapInputs.length != 0) {
						// iterate through mapinputs
						for (MapInputs mapInput : mapInputs) {
							// Check if the input message name matches with the provided input meassage name
							if (mapInput.getMessage_name()
									.equals(fieldmap.getInput_field_message_name())) {
								DataMapInputField[] dataMapInputFieldList = mapInput
										.getInput_fields();
								if (null != dataMapInputFieldList
										&& dataMapInputFieldList.length != 0) {
									// iterate through dataMapInput fields
									for (DataMapInputField dataMapInputField : dataMapInputFieldList) {
										//check if the output/destination tag id is already linked to any source 
										//node remove the destination node from the object
										if (dataMapInputField.getMapped_to_field()
												.equals(fieldmap.getOutput_field_tag_id())) {
											// delete the mapping if any
											dataMapInputField.setMapped_to_message("");
											dataMapInputField.setMapped_to_field("");
										}
										// check if the input source tagid matches with the provided input/source tagid
										if (dataMapInputField.getTag()
												.equals(fieldmap.getInput_field_tag_id())) {
											// update the object with the latest mapping
											dataMapInputField.setMapped_to_message(
													fieldmap.getOutput_field_message_name());
											dataMapInputField.setMapped_to_field(
													fieldmap.getOutput_field_tag_id());
										}
									}
								}
							}
						}
					}
				}
			}
		}
		logger.debug(EELFLoggerDelegator.debugLogger, " modifyNode()  : End");
	}

	@Override
	public String modifyLink(String userId, String cid, String solutionId, String version, String linkId,
			String linkName) {
		logger.debug(EELFLoggerDelegator.debugLogger, " modifyLink()  : Begin");
		String results = "";
		String resultTemplate = "{\"success\" : \"%s\", \"errorDescription\" : \"%s\"}";
		try {
			Cdump cdump = null;
			String id = "";
			if (null != cid && null == solutionId) {
				id = cid;
			} else if (null == cid) {
				id = solutionId;
			}
			String cdumpFileName = "acumos-cdump" + "-" + id;
			String path = DSUtil.readCdumpPath(userId, confprops.getToscaOutputFolder());
			cdump = mapper.readValue(new File(path.concat(cdumpFileName).concat(".json")), Cdump.class);
			List<Relations> relations = cdump.getRelations();
			if (null == relations || relations.isEmpty()) {
				results = String.format(resultTemplate, false, "Invalid Link Id – not found");
			} else {
				for (Relations relation : relations) {
					if (relation.getLinkId().equals(linkId)) {
						relation.setLinkName(linkName);
						results = String.format(resultTemplate, true, "");
						break;
					} else {
						results = String.format(resultTemplate, false, "Invalid Link Id – not found");
					}
				}
				mapper.writeValue(new File(path.concat(cdumpFileName).concat(".json")), cdump);
				logger.debug(EELFLoggerDelegator.debugLogger, " Link Modified Successfully ");
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, "Exception in  modifyLink() ", e);
			results = String.format(resultTemplate, false, "Not able to modify the Link");
		}
		logger.debug(EELFLoggerDelegator.debugLogger, " modifyLink()  : End");
		return results;
	}

	@Override
	public boolean deleteNode(String userId, String solutionId, String version, String cid, String nodeId)
			throws AcumosException {
		logger.debug(EELFLoggerDelegator.debugLogger, " deleteNode() in SolutionServiceImpl : Begin ");
		boolean deletedNode = false;
		try {
			String id = "";
			if (null != cid && null == solutionId) {
				id = cid;
			} else if (null == cid && null != solutionId) {
				id = solutionId;
			}
			String cdumpFileName = "acumos-cdump" + "-" + id + ".json";
			String path = DSUtil.readCdumpPath(userId, confprops.getToscaOutputFolder());
			File file = new File(path.concat(cdumpFileName));
			if (file.exists()) {
				try {
					Cdump cdump = mapper.readValue(new File(path.concat(cdumpFileName)), Cdump.class);
					List<Nodes> nodesList = cdump.getNodes();
					List<Relations> relationsList = cdump.getRelations();
					if (nodesList == null || nodesList.isEmpty()) {
						deletedNode = false;
					} else {
						// Deleting node if exists
						Iterator<Nodes> nodeitr = nodesList.iterator();
						while (nodeitr.hasNext()) {
							Nodes node = nodeitr.next();
							if (node.getNodeId().equals(nodeId)) {
								deletedNode = true;
								nodeitr.remove();
								break;
							}
						}
						if (null != relationsList) {
							for (Relations relations : relationsList) {
								if (relations.getTargetNodeId().equals(nodeId)) {
									// delete the LinkId related TargetNodeId
									deleteLinksTargetNode(nodeId, nodesList, relations);
								}
								if (relations.getSourceNodeId().equals(nodeId)) {
									// delete the LinkId related SourceNodeId
									deleteLinksSourceNode(nodeId, nodesList, relations);
								}
							}
						}
						// Deleting relationsList for given nodeId
						if (relationsList == null || relationsList.isEmpty()) {
						} else {
							Iterator<Relations> relationitr = relationsList.iterator();
							while (relationitr.hasNext()) {
								Relations relation = relationitr.next();
								if (relation.getSourceNodeId().equals(nodeId)
										|| relation.getTargetNodeId().equals(nodeId)) {
									relationitr.remove();
								}
							}
						}
						Gson gson = new Gson();
						String jsonInString = gson.toJson(cdump);
						DSUtil.writeDataToFile(path, "acumos-cdump" + "-" + id, "json", jsonInString);
					}
				} catch (JsonParseException e) {
					logger.error(EELFLoggerDelegator.errorLogger, " JsonParseException in deleteNode() ", e);
					throw e;
				} catch (JsonMappingException e) {
					logger.error(EELFLoggerDelegator.errorLogger, " JsonMappingException in deleteNode() ", e);
					throw e;
				} catch (IOException e) {
					logger.error(EELFLoggerDelegator.errorLogger, " IOException in deleteNode() ", e);
					throw e;
				}
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, " Exception in deleteNode() ", e);
			throw new ServiceException("Failed to Delete the Node", props.getSolutionErrorCode(),
					"Failed to Delete the Node");
		}
		logger.debug(EELFLoggerDelegator.debugLogger, " deleteNode() in SolutionServiceImpl : Ends ");
		return deletedNode;
	}

	private void deleteLinksTargetNode(String nodeId, List<Nodes> nodesList, Relations relations) {
		String sourceNodeId = relations.getSourceNodeId();
		for (Nodes no : nodesList) {
			if (no.getNodeId().equals(sourceNodeId)) {
				String nodeType = no.getType().getName();
				// check if its of DataBroker or not
				if (props.getDatabrokerType().equals(nodeType)) {
					Property[] propArr = no.getProperties();
					ArrayList<Property> arrayList = new ArrayList<Property>(Arrays.asList(propArr));
					Iterator<Property> propertyItr = arrayList.iterator();
					while (propertyItr.hasNext()) {
						Property prop = propertyItr.next();
						DBMapOutput[] dbMapOutputArr = prop.getData_broker_map().getMap_outputs();
						for (int i = 0; i < dbMapOutputArr.length; i++) {
							dbMapOutputArr[i].setOutput_field(null);
						}
						DBMapInput[] dbMapInputArr = prop.getData_broker_map().getMap_inputs();
						for (DBMapInput dbMapIp : dbMapInputArr) {
							DBInputField dbInField = dbMapIp.getInput_field();
							dbInField.setChecked("NO");
							dbInField.setMapped_to_field("");
						}
					}
				}
				// check if its of Collator(Array-based/Param-based) or not if
				// yes need to delete the corresponding entries in the collator_map
				else if (props.getCollatorType().equals(nodeType)) {
					Property[] propArr = no.getProperties();
					ArrayList<Property> arrayList = new ArrayList<Property>(Arrays.asList(propArr));
					Iterator<Property> propertyItr = arrayList.iterator();
					while (propertyItr.hasNext()) {
						Property prop = propertyItr.next();
						if (null != prop.getCollator_map().getMap_inputs()) {
							CollatorMapInput[] cMapInArr = prop.getCollator_map().getMap_inputs();
							for (int i = 0; i < cMapInArr.length; i++) {
								if (cMapInArr[i].getInput_field().getSource_name().equals(nodeId)) {
									cMapInArr[i].setInput_field(new CollatorInputField());
								}

							}
						}
					}
				}
				// check if its of Splitter(Copy-based/Param-based) or not if
				// yes need to delete the corresponding entries in the splitter_map
				else if (props.getSplitterType().equals(nodeType)) {
					Property[] propArr = no.getProperties();
					ArrayList<Property> arrayList = new ArrayList<Property>(Arrays.asList(propArr));
					Iterator<Property> propertyItr = arrayList.iterator();
					while (propertyItr.hasNext()) {
						Property prop = propertyItr.next();
						if (null != prop.getSplitter_map().getMap_outputs()) {
							SplitterMapOutput[] sMapOutArr = prop.getSplitter_map().getMap_outputs();
							for (int i = 0; i < sMapOutArr.length; i++) {
								if (sMapOutArr[i].getOutput_field().getTarget_name().equals(nodeId)) {
									sMapOutArr[i].setOutput_field(new SplitterOutputField());
								}
							}
						}
					}
				}
			}
		}
	}

	private void deleteLinksSourceNode(String nodeId, List<Nodes> nodesList, Relations relations) {
		String targetNodeId = relations.getTargetNodeId();
		for (Nodes no : nodesList) {
			if (no.getNodeId().equals(targetNodeId)) {
				String nodeType = no.getType().getName();
				// check if its of Collator(Array-based/Param-based) or not if
				// yes need to delete the corresponding entries in the collator_map
				if (props.getCollatorType().equals(nodeType)) {
					Property[] propArr = no.getProperties();
					ArrayList<Property> arrayList = new ArrayList<Property>(Arrays.asList(propArr));
					Iterator<Property> propertyItr = arrayList.iterator();
					while (propertyItr.hasNext()) {
						Property prop = propertyItr.next();
						if (null != prop.getCollator_map().getMap_inputs()) {
							CollatorMapInput[] cMapInArr = prop.getCollator_map().getMap_inputs();
							for (int i = 0; i < cMapInArr.length; i++) {
								if (cMapInArr[i].getInput_field().getSource_name().equals(nodeId)) {
									cMapInArr[i].setInput_field(new CollatorInputField());
								}
							}
						}
					}
				}
				// check if its of Splitter(Copy-based/Param-based) or not if
				// yes need to delete the corresponding entries in the splitter_map
				else if (props.getSplitterType().equals(nodeType)) {
					Property[] propArr = no.getProperties();
					ArrayList<Property> arrayList = new ArrayList<Property>(Arrays.asList(propArr));
					Iterator<Property> propertyItr = arrayList.iterator();
					while (propertyItr.hasNext()) {
						Property prop = propertyItr.next();
						if (null != prop.getSplitter_map().getMap_outputs()) {
							SplitterMapOutput[] sMapOutArr = prop.getSplitter_map().getMap_outputs();
							for (int i = 0; i < sMapOutArr.length; i++) {
								if (sMapOutArr[i].getOutput_field().getTarget_name().equals(nodeId)) {
									sMapOutArr[i].setOutput_field(new SplitterOutputField());
								}
							}
						}
					}
				}
			}
		}
	}

	@Override
	public String getMatchingModels(String userId, String portType, JSONArray protobufJsonString) throws Exception {
		Gson gson = new Gson();
		List<MLPSolution> mlpSolutions = null;
		List<MLPArtifact> mlpArtifact  = null;
		List<MatchingModel> matchingModelList = new ArrayList<>();
		boolean validMatchingModel;
		LinkedHashMap<String, List<MLPSolutionRevision>> matchingModelRevisionList = new LinkedHashMap<String,List<MLPSolutionRevision>>();
		LinkedHashMap<String, List<MLPArtifact>> matchingModelArtifactTigifFileList = new LinkedHashMap<String, List<MLPArtifact>>();
		try {
			if(null != matchingModelServiceComponent.getMatchingModelsolutionList()){
				mlpSolutions = matchingModelServiceComponent.getMatchingModelsolutionList();	
				logger.debug(EELFLoggerDelegator.debugLogger,"Take solution list at the time of getSolutionS API is called as well from the local cache or session");
			}else{
				mlpSolutions = getSolutionsForMatchingModel(userId);
				logger.debug(EELFLoggerDelegator.debugLogger,"If solution list is not available from at time of getSolutionS API call as well " + "from the local cache or session then need to fetch from CCDS");
			}
			MatchingModel matchingModel = null;
			List<MLPSolutionRevision> mlpSolRevisions;
			String solutionId = null;
			if (null != mlpSolutions && !mlpSolutions.isEmpty()) {
				logger.debug(EELFLoggerDelegator.debugLogger," CommonDataService returned Solution list of size :  {} ", mlpSolutions.size());
				mlpSolRevisions = new ArrayList<>();
				for (MLPSolution mlpsol : mlpSolutions) {
					solutionId = mlpsol.getSolutionId();
					//mlpSolRevisions = cmnDataService.getSolutionRevisions(solutionId);
					if(null != matchingModelServiceComponent.getMatchingModelRevisionList() && 
							matchingModelServiceComponent.getMatchingModelRevisionList().containsKey(solutionId)){
						    mlpSolRevisions = matchingModelServiceComponent.getMatchingModelRevisionList().get(solutionId);
					}else{
						mlpSolRevisions = cmnDataService.getSolutionRevisions(solutionId);
						matchingModelRevisionList.put(solutionId, mlpSolRevisions);
					}
					if (mlpSolRevisions != null && mlpSolRevisions.size() != 0) {
						logger.debug(EELFLoggerDelegator.debugLogger," CommonDataService returned Solution Revision list of size : "+ mlpSolRevisions.size());
						validMatchingModel = false;
						for (MLPSolutionRevision mlpSolRevision : mlpSolRevisions) {
							
							matchingModel = new MatchingModel();
							if(null != matchingModelServiceComponent.getMatchingModelArtifactTigifFileList()){
								if(matchingModelServiceComponent.getMatchingModelArtifactTigifFileList().containsKey(mlpsol.getSolutionId()+mlpSolRevision.getRevisionId())){
									mlpArtifact = matchingModelServiceComponent.getMatchingModelArtifactTigifFileList().get(mlpsol.getSolutionId()+mlpSolRevision.getRevisionId());
								}	
						   }else{
								mlpArtifact = cmnDataService.getSolutionRevisionArtifacts(mlpsol.getSolutionId(), mlpSolRevision.getRevisionId());
								matchingModelArtifactTigifFileList.put(mlpsol.getSolutionId()+mlpSolRevision.getRevisionId(), mlpArtifact);
						   }
						    logger.debug(EELFLoggerDelegator.debugLogger," Solution Id {} ", mlpsol.getSolutionId());
							String artifactURI = "";
							if (null != mlpArtifact && !mlpArtifact.isEmpty()) {
								logger.debug(EELFLoggerDelegator.debugLogger," CommonDataService returned Solution Revision Artifact list of size : " + mlpArtifact.size());
								for (MLPArtifact mlpArti : mlpArtifact) {
									if ("TG".equalsIgnoreCase(mlpArti.getArtifactTypeCode())) {
										artifactURI = mlpArti.getUri();
										if (validateTheMatchingPort(artifactURI, portType, protobufJsonString)) {
											validMatchingModel = true;
										}
									} else {
									}
								}
							}
							if (validMatchingModel) {
								matchingModel.setMatchingModelName(mlpsol.getName());
								matchingModel.setTgifFileNexusURI(artifactURI);
								matchingModelList.add(matchingModel);
							}
						}
					} else {
						logger.debug(EELFLoggerDelegator.debugLogger," CommonDataService returned empty Solution Revision list ");
					}
				}
				if(null == matchingModelServiceComponent.getMatchingModelRevisionList()){
					matchingModelServiceComponent.setMatchingModelRevisionList(matchingModelRevisionList);
				}
				if(null ==matchingModelServiceComponent.getMatchingModelArtifactTigifFileList()){
					matchingModelServiceComponent.setMatchingModelArtifactTigifFileList(matchingModelArtifactTigifFileList);
				}
			} else {
				logger.debug(EELFLoggerDelegator.debugLogger," CommonDataService returned empty Solution list");
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, " Exception in getMatchingModels() ", e);
			throw new ServiceException("Exception in getMatchingModels() solution, revision and Artifact section");
		}
		if (matchingModelList != null && !matchingModelList.isEmpty()) {
			String jsonInString = gson.toJson(matchingModelList);
			return jsonInString;
		} else {
			return "false";
		}
	}
	 
	private boolean validateTheMatchingPort(String artifactURI, String portType, JSONArray protobufJsonString) throws Exception {
		logger.debug(EELFLoggerDelegator.debugLogger, " validateTheMatchingPort() Begin ");
		boolean matchingPortisFound = false;
		ByteArrayOutputStream byteArrayOutputStream = null;
		try {
			if (null != artifactURI && !"".equals(artifactURI)) {
				byteArrayOutputStream = getPayload(artifactURI);
				String result = byteArrayOutputStream.toString();
				if ("input".equals(portType)) {
					matchingPortisFound = validateProviderPort(result, protobufJsonString);
				} else if ("output".equals(portType)) {
					matchingPortisFound = validateConsumerPort(result, protobufJsonString);
				}
			} else { 
				logger.error(EELFLoggerDelegator.errorLogger,"validateTheMatchingPort() : artifactURI is null or empty");
			}

		} catch (JsonParseException e) {
			logger.error(EELFLoggerDelegator.errorLogger, "JsonParseException in validateTheMatchingPort() method : ",e);
			throw e;
		} catch (JsonMappingException e) {
			logger.error(EELFLoggerDelegator.errorLogger, "JsonMappingException in validateTheMatchingPort() method : ",e);
			throw e;
		} catch (IOException e) {
			logger.error(EELFLoggerDelegator.errorLogger, "IOException in validateTheMatchingPort() method : ", e);
			throw e;
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, "Exception in validateTheMatchingPort() method : ", e);
			throw e;
		} finally {
			try {
				if (byteArrayOutputStream != null) {
					byteArrayOutputStream.close();
				}
			} catch (IOException e) {
				logger.error(EELFLoggerDelegator.errorLogger,"Error : Exception in readArtifact() : Failed to close the byteArrayOutputStream", e);
				throw e;
			}
		}
		logger.debug(EELFLoggerDelegator.debugLogger, " validateTheMatchingPort() End ");
		return matchingPortisFound;

	}

	private boolean validateProviderPort(String protobufTgifOutput, JSONArray protobufJsonString)
			throws JsonParseException, JsonMappingException, IOException {
		logger.debug(EELFLoggerDelegator.debugLogger, " validateProviderPort() Begin ");
		Gson gson = new Gson();
		Tgif tgif = new Tgif();
		org.acumos.designstudio.ce.vo.tgif.Service service = new org.acumos.designstudio.ce.vo.tgif.Service();
		try {
			tgif = mapper.readValue(protobufTgifOutput, Tgif.class);
		} catch (Exception ex) {
			logger.error(EELFLoggerDelegator.errorLogger,
					" Exception in  validateProviderPort    {} ", ex);
		}
		service = tgif.getServices();
		boolean matchingPortIsFound = false;
		if (service != null) {
			if (service.getCalls() != null && service.getCalls().length != 0) {
				Call[] call = service.getCalls();
				MessageBody messageBody1;
				MessageargumentList messageargumentListObject;
				List<MessageargumentList> listOfArgument = new ArrayList<MessageargumentList>();
				for (int i = 0; i < call.length; i++) {
					MessageBody[] messageBody = mapper.readValue(call[i].getRequest().getFormat().toJSONString(),
							MessageBody[].class);
					if (messageBody != null && messageBody.length != 0) {
						for (int j = 0; j < messageBody.length; j++) {
							messageBody1 = new MessageBody();
							messageBody1.setMessageargumentList(messageBody[j].getMessageargumentList());
							if (messageBody[j].getMessageargumentList() != null
									&& !messageBody[j].getMessageargumentList().isEmpty()) {
								if (protobufJsonString.length() == messageBody1.getMessageargumentList().size()) {
									for (int k = 0; k < messageBody1.getMessageargumentList().size(); k++) {
										messageargumentListObject = new MessageargumentList();
										messageargumentListObject
												.setTag(messageBody1.getMessageargumentList().get(k).getTag());
										messageargumentListObject
												.setRole(messageBody1.getMessageargumentList().get(k).getRole());
										messageargumentListObject
												.setType(messageBody1.getMessageargumentList().get(k).getType());
										listOfArgument.add(messageargumentListObject);
										listOfArgument.toArray().toString();
									}
									String jsonInString = gson.toJson(listOfArgument);
									JsonNode tree1 = mapper.readTree(jsonInString);
									JsonNode tree2 = mapper.readTree(protobufJsonString.toString());
									boolean isItSame = tree1.equals(tree2);
									if (isItSame) {
										matchingPortIsFound = true;
										return matchingPortIsFound;
									}
								} else {
									matchingPortIsFound = false;
								}
							}
						}

					}

				}
			}
		}
		logger.debug(EELFLoggerDelegator.debugLogger, " validateProviderPort() End ");
		return matchingPortIsFound;

	}

	private boolean validateConsumerPort(String protobufTgifOutput, JSONArray protobufJsonString)
			throws JsonParseException, JsonMappingException, IOException {
		logger.debug(EELFLoggerDelegator.debugLogger, " validateConsumerPort() Start ");
		Gson gson = new Gson();
		Tgif tgif = new Tgif();
		org.acumos.designstudio.ce.vo.tgif.Service service = new org.acumos.designstudio.ce.vo.tgif.Service();
		try {
			tgif = mapper.readValue(protobufTgifOutput, Tgif.class);
		} catch (Exception ex) {
			logger.error(EELFLoggerDelegator.errorLogger,
					" Exception in  validateConsumerPort     {} ", ex);
		}
		service = tgif.getServices();
		boolean matchingPortIsFound = false;
		if (service != null) {
			if (service.getProvides() != null && service.getProvides().length != 0) {
				Provide[] provide = service.getProvides();
				MessageBody messageBody1;
				MessageargumentList messageargumentListObject;
				List<MessageargumentList> listOfArgument = new ArrayList<MessageargumentList>();
				for (int i = 0; i < provide.length; i++) {
					MessageBody[] messageBody = mapper.readValue(provide[i].getRequest().getFormat().toJSONString(),
							MessageBody[].class);
					if (messageBody != null && messageBody.length != 0) {
						for (int j = 0; j < messageBody.length; j++) {
							messageBody1 = new MessageBody();
							messageBody1.setMessageargumentList(messageBody[j].getMessageargumentList());
							if (messageBody[j].getMessageargumentList() != null
									&& !messageBody[j].getMessageargumentList().isEmpty()) {
								if (protobufJsonString.length() == messageBody1.getMessageargumentList().size()) {
									for (int k = 0; k < messageBody1.getMessageargumentList().size(); k++) {
										messageargumentListObject = new MessageargumentList();
										messageargumentListObject
												.setTag(messageBody1.getMessageargumentList().get(k).getTag());
										messageargumentListObject
												.setRole(messageBody1.getMessageargumentList().get(k).getRole());
										messageargumentListObject
												.setType(messageBody1.getMessageargumentList().get(k).getType());
										listOfArgument.add(messageargumentListObject);
										listOfArgument.toArray().toString();
									}
									String jsonInString = gson.toJson(listOfArgument);
									JsonNode tree1 = mapper.readTree(jsonInString);
									JsonNode tree2 = mapper.readTree(protobufJsonString.toString());
									boolean isItSame = tree1.equals(tree2);
									if (isItSame) {
										matchingPortIsFound = true;
										return matchingPortIsFound;
									}
								} else {
									matchingPortIsFound = false;
								}
							}

						}

					}

				}
			}
		}
		logger.debug(EELFLoggerDelegator.debugLogger, " validateConsumerPort() End ");
		return matchingPortIsFound;
	}

	@Override
	public boolean addLink(String userId, String solutionId, String version, String linkName, String linkId,
			String sourceNodeName, String sourceNodeId, String targetNodeName, String targetNodeId,
			String sourceNodeRequirement, String targetNodeCapabilityName, String cid, Property property) {
		logger.debug(EELFLoggerDelegator.debugLogger, " addLink() in SolutionServiceImpl : Begin ");
		String id = "";
		Gson gson = new Gson();
		String nodeToUpdate = "";
		boolean addedLink = false;
		List<Nodes> nodesList = new ArrayList<>();
		try {
			if (null != cid && null == solutionId) {
				id = cid;
			} else if (null == cid) {
				id = solutionId;
			}
			String path = DSUtil.readCdumpPath(userId, confprops.getToscaOutputFolder());
			String cdumpFileName = "acumos-cdump" + "-" + id + ".json";
			Cdump cdump = mapper.readValue(new File(path.concat(cdumpFileName)), Cdump.class);
			nodesList = cdump.getNodes();

			// update relations list, if link is created b/w 2 models
			if (null == property || (null != property && null == property.getData_map()
					&& null == property.getCollator_map() && null == property.getSplitter_map())) {
				updateLinkdetails(linkName, linkId, sourceNodeName, sourceNodeId, targetNodeName, targetNodeId,
						sourceNodeRequirement, targetNodeCapabilityName, cdump);
				addedLink = true;
			}
			if (null != property.getSplitter_map()) {
				nodeToUpdate = targetNodeId;
				if (nodesList != null && !nodesList.isEmpty()) {
					for (Nodes node : nodesList) {
						if (node.getNodeId().equals(nodeToUpdate)) {
							// update Splitter Input Message Signature
							updateSplitterMap(property.getSplitter_map(), node);
							updateLinkdetails(linkName, linkId, sourceNodeName, sourceNodeId, targetNodeName,
									targetNodeId, sourceNodeRequirement, targetNodeCapabilityName, cdump);
							addedLink = true;
						}
					}
				}
			}
			if (null != property.getCollator_map()) {
				nodeToUpdate = sourceNodeId;
				if (nodesList != null && !nodesList.isEmpty()) {
					for (Nodes node : nodesList) {
						if (node.getNodeId().equals(nodeToUpdate)) {
							// update Collator Output Message Signature
							updateCollatorMap(property.getCollator_map(), node);
							updateLinkdetails(linkName, linkId, sourceNodeName, sourceNodeId, targetNodeName,
									targetNodeId, sourceNodeRequirement, targetNodeCapabilityName, cdump);
							addedLink = true;
						}
					}
				}
			} else if (null != property.getData_map()) {
				// set properties field of DM + update relations list, if link is b/w Model & Data Mapper
				// Identify Data Mapper node to update
				if (null != property.getData_map() && property.getData_map().getMap_inputs().length == 0) {
					nodeToUpdate = sourceNodeId;
				} else {
					nodeToUpdate = targetNodeId;
				}
				// update the properties field of Data mapper node + update relations list with link details
				if (nodesList != null && !nodesList.isEmpty()) {
					for (Nodes node : nodesList) {
						if (node.getNodeId().equals(nodeToUpdate)) {
							Property[] propertyArr = node.getProperties();
							if (null == propertyArr || propertyArr.length == 0) {
								Property[] propertyArray = new Property[1];
								propertyArray[0] = property;
								node.setProperties(propertyArray);
								updateLinkdetails(linkName, linkId, sourceNodeName, sourceNodeId, targetNodeName,
										targetNodeId, sourceNodeRequirement, targetNodeCapabilityName, cdump);
								addedLink = true;
								break;
							} else {
								// set map_outputs of data_map under properties field of DM
								if (null != property.getData_map()
										&& property.getData_map().getMap_inputs().length == 0) {

									propertyArr[0].getData_map()
											.setMap_outputs(property.getData_map().getMap_outputs());

									updateLinkdetails(linkName, linkId, sourceNodeName, sourceNodeId, targetNodeName,
											targetNodeId, sourceNodeRequirement, targetNodeCapabilityName, cdump);
									addedLink = true;
									break;
								}
								// set map_inputs of data_map under properties field of DM
								if (null != property.getData_map()
										&& property.getData_map().getMap_outputs().length == 0) {
									propertyArr[0].getData_map().setMap_inputs(property.getData_map().getMap_inputs());
									updateLinkdetails(linkName, linkId, sourceNodeName, sourceNodeId, targetNodeName,
											targetNodeId, sourceNodeRequirement, targetNodeCapabilityName, cdump);
									addedLink = true;
									break;
								}
							}
						}
					}
				}
			}
			try {
				String jsonInString = gson.toJson(cdump);
				DSUtil.writeDataToFile(path, "acumos-cdump" + "-" + id, "json", jsonInString);
			} catch (JsonIOException e) {
				logger.error(EELFLoggerDelegator.errorLogger, "Exception in addLink() ", e);
				addedLink = false;
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, " Exception Occured in addLink() ", e);
			addedLink = false;
		}

		logger.debug(EELFLoggerDelegator.debugLogger, " addLink() in SolutionServiceImpl : End ");
		return addedLink;
	}

	private void updateLinkdetails(String linkName, String linkId, String sourceNodeName, String sourceNodeId,
			String targetNodeName, String targetNodeId, String sourceNodeRequirement, String targetNodeCapabilityName,
			Cdump cdump) {

		Relations relationObj = new Relations();
		relationObj.setLinkName(linkName);
		relationObj.setLinkId(linkId);
		relationObj.setSourceNodeName(sourceNodeName);
		relationObj.setSourceNodeId(sourceNodeId);
		relationObj.setTargetNodeName(targetNodeName);
		relationObj.setTargetNodeId(targetNodeId);
		relationObj.setSourceNodeRequirement(sourceNodeRequirement);
		relationObj.setTargetNodeCapability(targetNodeCapabilityName);
		List<Nodes> nodes = cdump.getNodes();
		String protoUri = "";
		for (Nodes no : nodes) {
			if (no.getType().getName().equals(props.getDatabrokerType())) {
				if (no.getNodeId().equals(sourceNodeId)) {
					for (Nodes n : nodes) {
						if (n.getNodeId().equals(targetNodeId)) {
							protoUri = n.getProtoUri();
							no.setProtoUri(protoUri);
						}
					}
				}
			}
		}
		if (cdump.getRelations() == null) {
			List<Relations> list = new ArrayList<>();
			list.add(relationObj);
			cdump.setRelations(list);
		} else {
			cdump.getRelations().add(relationObj);
		}
	}

	@Override
	public boolean deleteLink(String userId, String solutionId, String version, String cid, String linkId) {
		logger.debug(EELFLoggerDelegator.debugLogger, " deleteLink() in SolutionServiceImpl : Begin ");
		String id = "";
		String cdumpFileName;
		String sourceNodeId = null;
		String targetNodeId = null;
		boolean deletedLink = false;
		mapper.setSerializationInclusion(Include.NON_NULL);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		List<Nodes> nodesList = new ArrayList<>();
		String filePath = DSUtil.readCdumpPath(userId, confprops.getToscaOutputFolder());
		try {
			if (null != cid && null == solutionId) {
				id = cid;
			} else if (null == cid && null != solutionId) {
				id = solutionId;
			}
			cdumpFileName = "acumos-cdump" + "-" + id + ".json";
			File file = new File(filePath.concat(cdumpFileName));
			if (file.exists()) {
				Cdump cdump = mapper.readValue(new File(filePath.concat(cdumpFileName)), Cdump.class);
				List<Relations> relationsList = cdump.getRelations();
				if (null == relationsList || relationsList.isEmpty()) {
					deletedLink = false;
				} else {
					Iterator<Relations> relationsItr = relationsList.iterator();
					// Identify link to delete + Data mapper node to delete it's properties field
					while (relationsItr.hasNext()) {
						Relations relation = relationsItr.next();
						if (relation.getLinkId().equals(linkId)) {
							sourceNodeId = relation.getSourceNodeId();
							targetNodeId = relation.getTargetNodeId();
							nodesList = cdump.getNodes();
							// delete properties field from DM
							for (Nodes node : nodesList) {
								// For all NodeTypes input is SourceNodeId which is same as nodeId in Nodes
								logger.debug(EELFLoggerDelegator.debugLogger, "1. For all NodeTypes input is SourceNodeId which is same as nodeId in Nodes ");
								if (node.getNodeId().equals(sourceNodeId) && node.getProperties().length != 0) {
									if (props.getGdmType().equals(node.getType().getName())) {
										node.getProperties()[0].getData_map().setMap_outputs(new MapOutput[0]);
									} else if (props.getDatabrokerType().equals(node.getType().getName())) {
										node.getProperties()[0].getData_broker_map().setMap_outputs(new DBMapOutput[0]);
										DBMapInput[] dbMapInArr = node.getProperties()[0].getData_broker_map()
												.getMap_inputs();
										for (DBMapInput dbmInput : dbMapInArr) {
											DBInputField dbiField = dbmInput.getInput_field();
											dbiField.setChecked("NO");
											dbiField.setMapped_to_field("");
										}
										// Collator map Output which have only one output link
										logger.debug(EELFLoggerDelegator.debugLogger, " Collator map Output which have only one output link ");
									} else if (props.getCollatorType().equals(node.getType().getName())) {
										logger.debug(EELFLoggerDelegator.debugLogger, "Output Message Signature set as empty for Collator");
										node.getProperties()[0].getCollator_map().setOutput_message_signature("");
										node.getProperties()[0].getCollator_map()
												.setMap_outputs(new CollatorMapOutput[0]);

										// Splitter Map Output which may have single or multiple link(s)
										
									} else if (props.getSplitterType().equals(node.getType().getName())) {
										logger.debug(EELFLoggerDelegator.debugLogger, "splitterLink() : Begin  ");
										splitterLink(linkId, relationsList, node);
										logger.debug(EELFLoggerDelegator.debugLogger, "splitterLink() : End ");
									}
								}
								
								// For all NodeTypes input is targetNodeId which is same as nodeId in Nodes
								if (node.getNodeId().equals(targetNodeId) && node.getProperties().length != 0) {
									logger.debug(EELFLoggerDelegator.debugLogger, " For all NodeTypes input is targetNodeId which is same as nodeId in Nodes");
									if (props.getGdmType().equals(node.getType().getName())) {
										node.getProperties()[0].getData_map().setMap_inputs(new MapInputs[0]);
									} else if (props.getSplitterType().equals(node.getType().getName())) {
										logger.debug(EELFLoggerDelegator.debugLogger,"Input Message Signature set as empty for Splitter");
										node.getProperties()[0].getSplitter_map().setInput_message_signature("");
										node.getProperties()[0].getSplitter_map()
												.setMap_inputs(new SplitterMapInput[0]);
									} else {
										if (null != node.getProperties()[0].getCollator_map().getMap_inputs()) {
											List<String> targetNodeList = new ArrayList<String>();
											String source = null;
											for (Relations rel : relationsList) {
												if (rel.getLinkId().equals(linkId)) {
													source = rel.getSourceNodeId();
												}
												if (rel.getTargetNodeId().equals(node.getNodeId())
														&& node.getType().getName().equals(props.getCollatorType())) {
													targetNodeList.add(rel.getTargetNodeId());
												}
											}
											// If the targetNodeId List size is having only one means collator contains one input and need to delete
											// the entire mapInputs and Source table details
											if (targetNodeList.size() == 0) {
												logger.debug(EELFLoggerDelegator.debugLogger," If the targetNodeId List size is having only one means collator contains one input.");
												if (props.getCollatorType().equals(node.getType().getName())) {
													node.getProperties()[0].getCollator_map()
															.setMap_inputs(new CollatorMapInput[0]);
												}
												// If the targetNodeId List size is more than one means collator contains more than one inputs and need to
												// delete the only deleted link related mapping details mapInputs and Source table details
											} else {
												logger.debug(EELFLoggerDelegator.debugLogger," If the targetNodeId List size is more than one means collator contains more than one inputs.");
												CollatorMapInput[] cmInput = node.getProperties()[0].getCollator_map()
														.getMap_inputs();
												List<CollatorMapInput> cim = new LinkedList<>(Arrays.asList(cmInput));
												Iterator<CollatorMapInput> cmiItr = cim.iterator();
												CollatorMapInput collatorMapInput = null;
												while (cmiItr.hasNext()) {
													collatorMapInput = (CollatorMapInput) cmiItr.next();
													if (source.equals(
															collatorMapInput.getInput_field().getSource_name())) {
														cmiItr.remove();
														break;
													}
												}
												CollatorMapInput newCmInput[] = cim
														.toArray(new CollatorMapInput[cim.size()]);
												node.getProperties()[0].getCollator_map().setMap_inputs(newCmInput);
											}
										}
									}
								}
							}
							// delete link details form relations list
							deletedLink = true;
							relationsItr.remove();
							break;
						}
					}
					cdump.setNodes(nodesList);
					String jsonInString = mapper.writeValueAsString(cdump);
					DSUtil.writeDataToFile(filePath, "acumos-cdump" + "-" + id, "json", jsonInString);
				}
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger," Exception in deleteLink() in SolutionServiceImpl", e);
		}
		logger.debug(EELFLoggerDelegator.debugLogger, " deleteLink() in SolutionServiceImpl End ");
		return deletedLink;
	}

	private void splitterLink(String linkId, List<Relations> relationsList, Nodes node) {
		logger.debug(EELFLoggerDelegator.debugLogger, "splitterLink() : Begin  ");
		List<String> sourceNodeList = new ArrayList<String>();
		String target = null;
		if (null != node.getProperties()[0].getSplitter_map().getMap_outputs()) {
			for (Relations rel : relationsList) {
				if (rel.getLinkId().equals(linkId)) {
					target = rel.getTargetNodeId();
				}
				if (rel.getSourceNodeId().equals(node.getNodeId())
						&& node.getType().getName().equals(props.getSplitterType())) {
					sourceNodeList.add(rel.getSourceNodeId());
				}
			}
			// If the sourceNodeId List size is having only one means splitter contains one output
			// and need to delete the entire mapOutput and target table details
			if (sourceNodeList.size() == 0) {
				if (props.getSplitterType().equals(node.getType().getName())) {
					node.getProperties()[0].getSplitter_map().setMap_outputs(new SplitterMapOutput[0]);
				}
				// If the sourceNodeId List size is more than one means splitter  contains more than one output
				// and need to delete the only, deleted link related mapping details mapOutput and target table details
			} else {
				SplitterMapOutput[] spOutput = node.getProperties()[0].getSplitter_map().getMap_outputs();

				List<SplitterMapOutput> spMapOut = new LinkedList<>(Arrays.asList(spOutput));

				Iterator<SplitterMapOutput> spMapOutItr = spMapOut.iterator();
				SplitterMapOutput splitterMapOutput = null;
				while (spMapOutItr.hasNext()) {
					splitterMapOutput = (SplitterMapOutput) spMapOutItr.next();
					if (target.equals(splitterMapOutput.getOutput_field().getTarget_name())) {
						spMapOutItr.remove();
						break;
					}
				}
				SplitterMapOutput newSplOut[] = spMapOut.toArray(new SplitterMapOutput[spMapOut.size()]);
				node.getProperties()[0].getSplitter_map().setMap_outputs(newSplOut);
				logger.debug(EELFLoggerDelegator.debugLogger, "splitterLink() : Begin  ");
			}
		}
	}

	/**
	 * 
	 * @param commonDataServiceRestClient
	 *            Client
	 */
	public void getRestCCDSClient(CommonDataServiceRestClientImpl commonDataServiceRestClient) {
		cmnDataService = commonDataServiceRestClient;
	}

	/**
	 * 
	 * @param nexusArtifactClient1
	 *            NexusArtifactClient
	 * @param confprops1
	 *            ConfigurationProperties
	 * @param properties
	 *            Properties
	 */
	public void getNexusClient(NexusArtifactClient nexusArtifactClient1, ConfigurationProperties confprops1,
			org.acumos.designstudio.ce.util.Properties properties) {
		confprops = confprops1;
		props = properties;
		nexusArtifactClient = nexusArtifactClient1;
	}

	/**
	 * @param solutionId
	 * 			solutionId is required to get protoUrl
	 * @param version
	 * 			version is required to get protoUrl			
	 * @param artifactType
	 * 			artifactType is required to get protoUrl	
	 * @param fileExtention
	 * 			This method accepts fileExtention
	 * @throws AcumosException
	 *          Throws AcumosException while getting protoUrl, Use for getting the nexusURI for solution.
	 *  
	 */
	public String getProtoUrl(String solutionId, String version, String artifactType, String fileExtention) throws AcumosException {
		logger.debug(EELFLoggerDelegator.debugLogger, "getProtoUrl() : Begin");

		String nexusURI = "";
		List<MLPSolutionRevision> mlpSolutionRevisionList = null;
		String solutionRevisionId = null;
		List<MLPArtifact> mlpArtifactList;
		try {
			// 1. Get the list of SolutionRevision for the solutionId.
			try {
				mlpSolutionRevisionList = cmnDataService.getSolutionRevisions(solutionId);
			} catch (Exception e) {
				logger.error(EELFLoggerDelegator.errorLogger, " Exception in getSolutionRevisions() ",e);
				throw new ServiceException("Failed to get the SolutionRevisionList");
			}

			// 2. Match the version with the SolutionRevision and get the solutionRevisionId.
			if (null != mlpSolutionRevisionList && !mlpSolutionRevisionList.isEmpty()) {
				solutionRevisionId = mlpSolutionRevisionList.stream().filter(mlp -> mlp.getVersion().equals(version))
						.findFirst().get().getRevisionId();
				logger.debug(EELFLoggerDelegator.debugLogger, " SolutionRevisonId for Version :  {} ", solutionRevisionId );
			}
		} catch (NoSuchElementException | NullPointerException e) {
			logger.error(EELFLoggerDelegator.errorLogger,"Error : Exception in getProtoUrl() : Failed to fetch the Solution Revision Id",e);
			throw new NoSuchElementException("Failed to fetch the Solution Revision Id of the solutionId for the user");
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger,"Error : Exception in getProtoUrl() : Failed to fetch the Solution Revision Id",e);
			throw new ServiceException("Failed to fetch the Solution Revision Id for the solutionId " + solutionId);
		}

		if (null != solutionRevisionId) {
			// 3. Get the list of Artifiact for the SolutionId and SolutionRevisionId.
			try {
				mlpArtifactList = getListOfArtifacts(solutionId, solutionRevisionId);
			} catch (Exception e1) {
				throw new ServiceException(" Exception Occured decryptAndWriteTofile() ", "501","No artifact found for the solution Id " + solutionId+ " and revisionId " + solutionRevisionId );
			}

			if (null != mlpArtifactList && !mlpArtifactList.isEmpty()) {
				try {
					// 3. Get the nexus URI for the SolutionId
					/*nexusURI = mlpArtifactList.stream()
							.filter(mlpArt -> mlpArt.getArtifactTypeCode().equalsIgnoreCase(artifactType)).findFirst()
							.get().getUri();*/
					for(MLPArtifact mlpArt : mlpArtifactList){
						if( null != fileExtention ){
							if(mlpArt.getArtifactTypeCode().equalsIgnoreCase(artifactType) && mlpArt.getName().contains(fileExtention)){
								nexusURI = mlpArt.getUri();
								break;
							}
						} else if(mlpArt.getArtifactTypeCode().equalsIgnoreCase(artifactType)){
							nexusURI = mlpArt.getUri();
							break;
						}
						
					}
					logger.debug(EELFLoggerDelegator.debugLogger, " Nexus URI :  {} ", nexusURI );
				} catch (NoSuchElementException | NullPointerException e) {
					logger.error(EELFLoggerDelegator.errorLogger,"Error : Exception in getProtoUrl() : Failed to fetch the artifact URI for artifactType",e);
					throw new NoSuchElementException("Could not search the artifact URI for artifactType " + artifactType);
				} catch (Exception e) {
					logger.error(EELFLoggerDelegator.errorLogger,"Error : Exception in getProtoUrl() : Failed to fetch the artifact URI for artifactType",e);
					throw new ServiceException(" Exception Occured decryptAndWriteTofile() ", "501","Could not search the artifact URI for artifactType " + artifactType, e.getCause());
				}
			}
		}
		logger.debug(EELFLoggerDelegator.debugLogger, "getProtoUrl() : End");
		return nexusURI;
	}
	
	private List<MLPSolution> getSolutionsForMatchingModel(String userId){
		List<MLPSolution> matchingModelsolutionList  = new ArrayList<MLPSolution>();
		List<MLPSolution> mlpSolutionsList = null;
		Map<String, Object> queryParameters = new HashMap<>();
		queryParameters.put("active", Boolean.TRUE);
		
		// Code changes are to match the change in the CDS API Definition searchSolution in version 1.13.x
		
		RestPageResponse<MLPSolution> pageResponse = cmnDataService.searchSolutions(queryParameters, false,new RestPageRequest(0, props.getSolutionResultsetSize()));
		
		mlpSolutionsList = pageResponse.getContent();
		
		logger.debug(EELFLoggerDelegator.debugLogger, " The Date Format :  {} ", confprops.getDateFormat());
		
		if (null == mlpSolutionsList) {
			
			logger.debug(EELFLoggerDelegator.debugLogger," CommonDataService returned null Solution list");
			
		} else if (mlpSolutionsList.isEmpty()) {
			
			logger.debug(EELFLoggerDelegator.debugLogger," CommonDataService returned empty Solution list");
			
		} else {
			
			logger.debug(EELFLoggerDelegator.debugLogger," CommonDataService returned Solution list of size :  {} ", mlpSolutionsList.size());
			
			MLPUser mlpUser = cmnDataService.getUser(userId);
			
			logger.debug(EELFLoggerDelegator.debugLogger, "MLPUSer  {} ", mlpUser);
			
			// Get the TypeCodes from Properties file
			String compoSolnTlkitTypeCode = props.getCompositSolutiontoolKitTypeCode();
			String pbAccessTypeCode = props.getPublicAccessTypeCode();
			String prAccessTypeCode = props.getPrivateAccessTypeCode();
			String orAccessTypeCode = props.getOrganizationAccessTypeCode();
			// For each solution where toolkittypeCode is not null and not equal to "CP".
			
			for (MLPSolution mlpsolution : mlpSolutionsList) {
				List<MLPSolutionRevision> mlpSolRevisions = cmnDataService.getSolutionRevisions(mlpsolution.getSolutionId());
				for (MLPSolutionRevision mlpSolRevision : mlpSolRevisions) {
					if (mlpsolution.getToolkitTypeCode() != null
							&& (!mlpsolution.getToolkitTypeCode().equals(compoSolnTlkitTypeCode))) {
	                        String accessTypeCode = mlpSolRevision.getAccessTypeCode();
						if (accessTypeCode.equals(pbAccessTypeCode)) {
							matchingModelsolutionList.add(mlpsolution);
						}
	                    if (mlpSolRevision.getOwnerId().equals(userId) && accessTypeCode.equals(prAccessTypeCode)) {
							matchingModelsolutionList.add(mlpsolution);
						}
	                    if (accessTypeCode.equals(orAccessTypeCode)) {
							matchingModelsolutionList.add(mlpsolution);
						}
					}
				}

			}
			matchingModelServiceComponent.setMatchingModelsolutionList(matchingModelsolutionList);
		}
		return matchingModelsolutionList;
	}
}
