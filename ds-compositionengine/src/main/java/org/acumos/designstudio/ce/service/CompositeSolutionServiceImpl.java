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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.acumos.cds.AccessTypeCode;
import org.acumos.cds.ModelTypeCode;
import org.acumos.cds.ValidationStatusCode;
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
import org.acumos.designstudio.ce.vo.Artifact;
import org.acumos.designstudio.ce.vo.DSCompositeSolution;
import org.acumos.designstudio.ce.vo.DSSolution;
import org.acumos.designstudio.ce.vo.DSResult;
import org.acumos.designstudio.ce.vo.SuccessErrorMessage;
import org.acumos.designstudio.ce.vo.blueprint.BPCollatorMap;
import org.acumos.designstudio.ce.vo.blueprint.BPDataBrokerMap;
import org.acumos.designstudio.ce.vo.blueprint.BPSplitterMap;
import org.acumos.designstudio.ce.vo.blueprint.BaseOperationSignature;
import org.acumos.designstudio.ce.vo.blueprint.BluePrint;
import org.acumos.designstudio.ce.vo.blueprint.Container;
import org.acumos.designstudio.ce.vo.blueprint.Node;
import org.acumos.designstudio.ce.vo.blueprint.NodeOperationSignature;
import org.acumos.designstudio.ce.vo.blueprint.OperationSignatureList;
import org.acumos.designstudio.ce.vo.blueprint.ProbeIndicator;
import org.acumos.designstudio.ce.vo.cdump.Capabilities;
import org.acumos.designstudio.ce.vo.cdump.Cdump;
import org.acumos.designstudio.ce.vo.cdump.Nodes;
import org.acumos.designstudio.ce.vo.cdump.Property;
import org.acumos.designstudio.ce.vo.cdump.Relations;
import org.acumos.designstudio.ce.vo.cdump.ReqCapability;
import org.acumos.designstudio.ce.vo.cdump.Requirements;
import org.acumos.designstudio.ce.vo.cdump.collator.CollatorInputField;
import org.acumos.designstudio.ce.vo.cdump.collator.CollatorMapInput;
import org.acumos.designstudio.ce.vo.cdump.collator.CollatorMapOutput;
import org.acumos.designstudio.ce.vo.cdump.collator.CollatorOutputField;
import org.acumos.designstudio.ce.vo.cdump.databroker.DBInputField;
import org.acumos.designstudio.ce.vo.cdump.databroker.DBMapInput;
import org.acumos.designstudio.ce.vo.cdump.databroker.DBMapOutput;
import org.acumos.designstudio.ce.vo.cdump.databroker.DBOTypeAndRoleHierarchy;
import org.acumos.designstudio.ce.vo.cdump.databroker.DBOutputField;
import org.acumos.designstudio.ce.vo.cdump.splitter.SplitterInputField;
import org.acumos.designstudio.ce.vo.cdump.splitter.SplitterMapInput;
import org.acumos.designstudio.ce.vo.cdump.splitter.SplitterMapOutput;
import org.acumos.designstudio.ce.vo.cdump.splitter.SplitterOutputField;
import org.acumos.nexus.client.NexusArtifactClient;
import org.acumos.nexus.client.data.UploadArtifactInfo;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;

@Service("compositeServiceImpl")
public class CompositeSolutionServiceImpl implements ICompositeSolutionService {

	private static EELFLoggerDelegator logger = EELFLoggerDelegator.getLogger(CompositeSolutionServiceImpl.class);

    private SuccessErrorMessage successErrorMessage = null;
    
    private static final String SPLITTER_TYPE = "Splitter";
    
    private static final String COLLATOR_TYPE = "Collator";
    
    private static final String MLMODEL_TYPE = "MLModel";
    
    private static final String DATAMAPPER_TYPE = "DataMapper";
    
    private static final String DATABROKER_TYPE = "DataBroker";
    
    private static final String OPERATION_EXTRACTOR = "%PLUS%";
    
    private static final String FIRST_NODE_POSITION = "first";
    
    private static final String LAST_NODE_POSITION = "last";
    
	@Autowired
	private Properties props;

	@Autowired
	private ConfigurationProperties confprops;

	@Autowired
	private CommonDataServiceRestClientImpl cdmsClient;

	@Autowired
	private NexusArtifactClient nexusArtifactClient;

	@Autowired
	private GenericDataMapperServiceImpl gdmService;

	@Autowired
	private DataBrokerServiceImpl dbService;
	
	@Override
	public String saveCompositeSolution(DSCompositeSolution dscs) throws AcumosException {

		String result = "";
		String error = "{\"errorCode\" : \"%s\", \"errorDescription\" : \"%s\"}";
		logger.debug(EELFLoggerDelegator.debugLogger, " saveCompositeSolution() Begin ");
		// Case 1. New Composite Solution : CID exist and SolutionID is missing.
		if (null != dscs.getcId() && null == dscs.getSolutionId()) {
			logger.debug(EELFLoggerDelegator.debugLogger,"Started implementation of Case 1 : cid is present and SolutionId is null");
			String cdumpFileName = "acumos-cdump" + "-" + dscs.getcId() + ".json";
			String filePath = DSUtil.readCdumpPath(dscs.getAuthor(), confprops.getToscaOutputFolder());

			File cdump = new File(filePath.concat(cdumpFileName));
			// 3. If cid is present then validate the cid : By checking hether the corresponding json file existing or not.
			if (cdump.exists()) {

				// check if the solution name and its version already exists using CDS
				Boolean solutionsExists = false;
				Map<String, Object> queryParameters = new HashMap<String, Object>();
				queryParameters.put("name", dscs.getSolutionName());
				RestPageRequest restPageRequets = new RestPageRequest(0,props.getSolutionResultsetSize());
				//Code changes are to match the change in the CDS API Definition searchSolution in version 1.13.x
				RestPageResponse<MLPSolution> pageResponse = cdmsClient.searchSolutions(queryParameters, false, restPageRequets);
				List<MLPSolution> mlpSolutions = pageResponse.getContent();
				List<MLPSolutionRevision> mlpSolnRevision;
				if (null != mlpSolutions && !mlpSolutions.isEmpty()) {
					for (MLPSolution mlpSol : mlpSolutions) {
						mlpSolnRevision = cdmsClient.getSolutionRevisions(mlpSol.getSolutionId());
						if (null != mlpSolnRevision && !mlpSolnRevision.isEmpty()) {
							for (MLPSolutionRevision mlpSolRev : mlpSolnRevision) {
								if (mlpSolRev.getVersion().equalsIgnoreCase(dscs.getVersion())) {
									solutionsExists = true;
								}
							}
						}
					}
				}
				if (solutionsExists) {
					result = "{\"duplicateErrorCode\" : \"219\", \"duplicate\" : \"Solution already exists.Please change either solution name, version or both.\"}";
				} else {
					try {
						result = insertCompositeSolution(dscs);
					} catch (IOException e) {
						logger.error(EELFLoggerDelegator.errorLogger, " IOException in insertCompositeSolution ",e);
						throw new ServiceException("  Exception in insertCompositeSolution ", "222","IOException in insertCompositeSolution");
					}
				}
			} else {
				result = String.format(error, "201", "Unknown Composition Id " + dscs.getcId());
			}
			logger.debug(EELFLoggerDelegator.debugLogger,"Ended implementation of Case 1 : cid is present and SolutionId is null");
		} else if (null == dscs.getcId() && null != dscs.getSolutionId()) {
			logger.debug(EELFLoggerDelegator.debugLogger,"Started implementation of Case 2 : cid is null and SolutionId is present");
			try {
				result = updateCompositeSolution(dscs);
			} catch (IOException e) {
				logger.error(EELFLoggerDelegator.errorLogger, " Exception in updateCompositeSolution ", e);
				throw new ServiceException("  Exception in insertCompositeSolution ", "222","Exception in updateCompositeSolution");
			}
			logger.debug(EELFLoggerDelegator.debugLogger,"Ended implementation of Case 2 : cid is null and SolutionId is present");
		} else {
			result = String.format(error, "201", "Error while saving the solution");
		}
		// 4. (Future) The Composition Engine must call the Modeling Engine to ensure the TOSCA validation of cdump file.
		logger.debug(EELFLoggerDelegator.debugLogger, " saveCompositeSolution() End ");
		return result;
	}

	/**
	 * 
	 * @param dscs
	 * @return
	 * @throws AcumosException
	 * @throws IOException
	 */
	private String insertCompositeSolution(DSCompositeSolution dscs) throws AcumosException, IOException {

		logger.debug(EELFLoggerDelegator.debugLogger, " insertCompositeSolution() Begin ");
		MLPSolution mlpSolution = new MLPSolution();
		try {
 
			mlpSolution = new MLPSolution();
			mlpSolution.setName(dscs.getSolutionName());
			mlpSolution.setDescription(dscs.getDescription());
			mlpSolution.setUserId(dscs.getAuthor());
			mlpSolution.setModelTypeCode(ModelTypeCode.PR.toString());
			mlpSolution.setToolkitTypeCode("CP");
			mlpSolution.setActive(true);
			mlpSolution = cdmsClient.createSolution(mlpSolution);
			logger.debug(EELFLoggerDelegator.debugLogger, "1.  Successfully Created the Solution {0} & generated Solution ID : {1}", mlpSolution.getName(), mlpSolution.getSolutionId());
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger,"Error :  Exception in insertCompositeSolution() Failed to create the Solution ",e);
			throw new ServiceException("  Exception in insertCompositeSolution ", "222","Failed to create the Solution");
		}

		// 2. create the solutionRevision
		MLPSolutionRevision mlpSolutionRevision = new MLPSolutionRevision();
		try {
			mlpSolutionRevision.setSolutionId(mlpSolution.getSolutionId());
			mlpSolutionRevision.setDescription(dscs.getDescription());
			mlpSolutionRevision.setUserId(dscs.getAuthor());
			mlpSolutionRevision.setVersion(dscs.getVersion());
			mlpSolutionRevision.setValidationStatusCode(ValidationStatusCode.IP.toString());
			mlpSolutionRevision.setAccessTypeCode(AccessTypeCode.PR.toString());
			mlpSolutionRevision.setPublisher(dscs.getProvider());
			
			mlpSolutionRevision = cdmsClient.createSolutionRevision(mlpSolutionRevision);

			logger.debug(EELFLoggerDelegator.debugLogger,"2. Successfully Created the SolutionRevision :  {} ", mlpSolutionRevision.getRevisionId());
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger,"Error : Exception in insertCompositeSolution() : Failed to create the Solution Revision",e);
			throw new ServiceException("  Exception in insertCompositeSolution() ", "222","Failed to create the Solution");
		}

		String path = DSUtil.readCdumpPath(dscs.getAuthor(), confprops.getToscaOutputFolder());
		if(null == dscs.getcId()){
			dscs.setcId(mlpSolution.getSolutionId());
		}
		String cdumpFileName = "acumos-cdump" + "-" + dscs.getcId();
		String payload = "";

		try {
			ObjectMapper mapper = new ObjectMapper();
			Cdump cdump = mapper.readValue(new File(path.concat(cdumpFileName).concat(".json")), Cdump.class);
			if (null == cdump) {
				logger.debug(EELFLoggerDelegator.debugLogger,"Error : Cdump file not found for Solution ID :   {} ", mlpSolution.getSolutionId());
			} else {
				cdump.setCname(dscs.getSolutionName());
				cdump.setVersion(dscs.getVersion());
				cdump.setValidSolution(false);
				cdump.setSolutionId(mlpSolution.getSolutionId());
				SimpleDateFormat sdf = new SimpleDateFormat(confprops.getDateFormat());
				cdump.setMtime(sdf.format(new Date()));
				logger.debug(EELFLoggerDelegator.debugLogger,"3. Successfully read the Cdump file for solution ID :  {} ", mlpSolution.getSolutionId());
				Gson gson = new Gson();
				payload = gson.toJson(cdump);
				cdumpFileName = "acumos-cdump" + "-" + mlpSolution.getSolutionId();
				DSUtil.writeDataToFile(path, cdumpFileName, "json", payload);
				// store members (parent-child relationships) of composite solutions into CDS.
				boolean flag = storeCompositeSolutionMembers(cdump);
				logger.debug(EELFLoggerDelegator.debugLogger,"Store members (parent-child relationships) of composite solutions into CDS : Status : {}", flag);
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger,"Error : Exception in insertCompositeSolution() : Failed to Find the Cdump File ",e);
			throw new ServiceException("  Exception in insertCompositeSolution() ", "222","Failed to create the Solution");
		}

		Artifact cdumpArtifact = new Artifact(cdumpFileName, "json", mlpSolution.getSolutionId(), dscs.getVersion(),
				path, payload.length());

		logger.debug(EELFLoggerDelegator.debugLogger,"4. Successfully updated the Cdump file for solution ID :  {} ", mlpSolution.getSolutionId());

		try {
			uploadFilesToRepository(mlpSolution.getSolutionId(), dscs.getVersion(), cdumpArtifact);
			dscs.setCdump(cdumpArtifact);
			logger.debug(EELFLoggerDelegator.debugLogger,"5. Successfully uploaded the Cdump file for solution ID :  {} ", mlpSolution.getSolutionId());
			DSUtil.deleteFile(path.concat("acumos-cdump" + "-" + dscs.getcId()).concat(".json"));
			logger.debug(EELFLoggerDelegator.debugLogger,"5.1 Successfully deleted the local Cdump file for solution ID : " + mlpSolution.getSolutionId());

		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger,"Error : Exception in insertCompositeSolution() : Failed to upload the Cdump File to Nexus ",e);
			DSUtil.deleteFile(path.concat("acumos-cdump" + "-" + dscs.getcId()).concat(".json"));
			if (null != mlpSolution.getSolutionId())
				DSUtil.deleteFile(path.concat(cdumpFileName).concat(".json"));
			logger.debug(EELFLoggerDelegator.debugLogger,"5.1 Successfully deleted the local Cdump file for solution ID : " + mlpSolution.getSolutionId());
			throw new ServiceException("  Exception in insertCompositeSolution() ", "222","Failed to create the Solution");
		}

		// 4. create the artifact
		MLPArtifact mlpArtifact = null;
		try {
			mlpArtifact = new MLPArtifact();
			mlpArtifact.setArtifactTypeCode(props.getArtifactTypeCode());
			mlpArtifact.setDescription("Cdump File for : " + cdumpArtifact.getName() + " for SolutionID : "
					+ cdumpArtifact.getSolutionID() + " with version : " + cdumpArtifact.getVersion());
			mlpArtifact.setUri(cdumpArtifact.getNexusURI());
			mlpArtifact.setName(cdumpArtifact.getName());
			mlpArtifact.setUserId(dscs.getAuthor());
			mlpArtifact.setVersion(cdumpArtifact.getVersion());
			mlpArtifact.setSize(cdumpArtifact.getContentLength());

			mlpArtifact = cdmsClient.createArtifact(mlpArtifact);

			// 5. associate articat to the solutionRevisionArtifact.
			logger.debug(EELFLoggerDelegator.debugLogger,"6. Successfully created the artifact for the cdumpfile for the solution : {0} artifact ID : {1}" ,mlpSolution.getSolutionId(), mlpArtifact.getArtifactId());

			cdmsClient.addSolutionRevisionArtifact(mlpSolution.getSolutionId(), mlpSolutionRevision.getRevisionId(),
					mlpArtifact.getArtifactId());

			logger.debug(EELFLoggerDelegator.debugLogger,"7. Successfully associated the Solution Revision Artifact for solution ID  : " + mlpSolution.getSolutionId());
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger,"Error : Exception in insertCompositeSolution() : Failed to create the Solution Artifact ",e);
			throw new ServiceException("  Exception in insertCompositeSolution() ", "222","Failed to create the Solution");
		}

		// 5. Detete the cdump file

		logger.debug(EELFLoggerDelegator.debugLogger, " insertCompositeSolution() End ");

		return "{\"solutionId\": \"" + mlpSolution.getSolutionId() + "\", \"version\" : \"" + dscs.getVersion() + "\" }";

	}

	/**
	 * 
	 * @param dscs
	 *            DSCompositeSolution
	 * @return Success or error message
	 * @throws AcumosException
	 *             On failure
	 * @throws IOException
	 *             On failure
	 */
	
	public String updateCompositeSolution(DSCompositeSolution dscs) throws AcumosException, IOException {
		logger.debug(EELFLoggerDelegator.debugLogger, " updateCompositeSolution() Begin ");

		String result = null;
		String error = "{\"errorCode\" : \"%s\", \"errorDescription\" : \"%s\"}";
		// 1. get the solution details using CDS : MLPSolution
		MLPSolution mlpSolution = cdmsClient.getSolution(dscs.getSolutionId());
		
		String path = DSUtil.readCdumpPath(dscs.getAuthor(), confprops.getToscaOutputFolder());

		String cdumpFileName = "acumos-cdump" + "-" + mlpSolution.getSolutionId();
		ObjectMapper mapper = new ObjectMapper();
		Cdump cdump = mapper.readValue(new File(path.concat(cdumpFileName).concat(".json")), Cdump.class);
		
		if (null != mlpSolution) {
			
			// 2. check the solution name with input solution name, if solution name is different then its altogether new solution.
			if (mlpSolution.getName().equalsIgnoreCase(dscs.getSolutionName())) {

				// 3. get the list of solutionRevision details using CDS : MLPSolutionRevison sorted in descending order.
				List<MLPSolutionRevision> mlpSolutionList = cdmsClient.getSolutionRevisions(dscs.getSolutionId());

				Collections.sort(mlpSolutionList, new Comparator<MLPSolutionRevision>() {
					@Override
					public int compare(MLPSolutionRevision s1, MLPSolutionRevision s2) {
						return s2.getCreated().compareTo(s1.getCreated());
					}
				});
				
				MLPSolutionRevision mlpSolutionRevision = null;
				for(MLPSolutionRevision mlpsr : mlpSolutionList){
					if(mlpsr.getVersion().equals(dscs.getVersion())){
						mlpSolutionRevision = mlpsr;
					}
				}
				
				if(null == mlpSolutionRevision){
					logger.debug(EELFLoggerDelegator.debugLogger, "Upadating Solution with the new version");
					result = updateSolnWithNewVersion(mlpSolution, dscs);
				} else{
					if (cdump.getVersion().equals(dscs.getVersion())) {
						result = updateExistingSolution(mlpSolutionRevision, mlpSolution, dscs, cdump, cdumpFileName, path);
					} else {
						if (!dscs.getIgnoreLesserVersionConflictFlag()) {
							result = "{\"alert\": \"" + props.getAskToUpdateExistingCompSolnMsg() + "\" }";
						} else if (dscs.getIgnoreLesserVersionConflictFlag()) {
							logger.debug(EELFLoggerDelegator.debugLogger,"Upadating Existing Solution with the previous version");
							result = updateExistingSolution(mlpSolutionRevision, mlpSolution, dscs, cdump, cdumpFileName, path);
						}
					}
				}
				// update members (parent-child relationships) of composite solutions into CDS.
				boolean flag = storeCompositeSolutionMembers(cdump);
				logger.debug(EELFLoggerDelegator.debugLogger,"Store members (parent-child relationships) of composite solutions into CDS : Status : {}", flag);
			
			} else {
				// New Case: When user tries to update the existting solution with a different name Update the dscs with the new values
				dscs.setcId(dscs.getSolutionId());
				dscs.setSolutionId(null);
				// call the save method to get the normal flow saving a solution
				result = saveCompositeSolution(dscs);

			}
		} else
			result = String.format(error, "207", "Solution does not exist in the Database");
		logger.debug(EELFLoggerDelegator.debugLogger, " updateCompositeSolution() End ");
		return result;
	}

	/**
	 * 
	 * @param mlpSR
	 *            MLPSolutionRevision
	 * @param mlpSolution
	 *            MLPSolution
	 * @param dscs
	 *            DSCompositeSolution
	 * @return Success or error
	 * @throws IOException
	 *             On error
	 * @throws AcumosException
	 *             On error
	 */
	public String updateExistingSolution(MLPSolutionRevision mlpSR, MLPSolution mlpSolution, DSCompositeSolution dscs, Cdump cdump, String cdumpFileName, String path)
			throws IOException, AcumosException {
		logger.debug(EELFLoggerDelegator.debugLogger, " updateExistingSolution() Start ");
		String result = "";
		Date currentDate = new Date();
		
		if("PB".equals(mlpSR.getAccessTypeCode())){
			result = "{\"duplicateErrorCode\" : \"219\", \"duplicate\" : \"Solution In Public/Company. Please change either solution name, version or both.\"}";
		} else {
			if (null == cdump) {
				logger.debug(EELFLoggerDelegator.debugLogger,"Error : Cdump file not found for Solution ID :   {} ", mlpSolution.getSolutionId());
			} else {
				// 5.2 Update the cdump file with mtime
				cdump.setMtime(new SimpleDateFormat(confprops.getDateFormat()).format(currentDate));
				cdump.setValidSolution(false);
				cdump.setVersion(mlpSR.getVersion());
				Gson gson = new Gson();
				String payload = gson.toJson(cdump);
				DSUtil.writeDataToFile(path, cdumpFileName, "json", payload);
				Artifact cdumpArtifact = new Artifact(cdumpFileName, "json", mlpSolution.getSolutionId(), dscs.getVersion(),
						path, payload.length());
				logger.debug(EELFLoggerDelegator.debugLogger,"4. Successfully updated the Cdump file for solution ID :  {} ", mlpSolution.getSolutionId());
				// 5.3 upload the cdump file in Nexus Repositry. (file name should be the same as previous one).
				uploadFilesToRepository(mlpSolution.getSolutionId(), dscs.getVersion(), cdumpArtifact);
	
				// Fetch the existing artifact
				List<MLPArtifact> artfactsList = cdmsClient.getSolutionRevisionArtifacts(mlpSR.getSolutionId(), mlpSR.getRevisionId());
	
				for (MLPArtifact mlpArtifact : artfactsList) {
	
					logger.debug(EELFLoggerDelegator.debugLogger,"mlpArtifact.getArtifactTypeCode()  :  {} ", mlpArtifact.getArtifactTypeCode());
					if (mlpArtifact.getArtifactTypeCode().equals(props.getArtifactTypeCode())) {
						logger.debug(EELFLoggerDelegator.debugLogger, " {0} = {1} ", mlpArtifact.getArtifactTypeCode(), props.getArtifactTypeCode());
						dscs.setCdump(cdumpArtifact);
						mlpArtifact.setUri(cdumpArtifact.getNexusURI());
						mlpArtifact.setModified(currentDate);
						mlpArtifact.setSize(cdumpArtifact.getContentLength());
						cdmsClient.updateArtifact(mlpArtifact);
						logger.debug(EELFLoggerDelegator.debugLogger," Successfully updated the artifact for the cdumpfile for the solution : {0} artifact ID : {1}" , mlpSolution.getSolutionId(),  mlpArtifact.getArtifactId());
						result = "{\"solutionId\": \"" + mlpSolution.getSolutionId() + "\", \"version\" : \"" + dscs.getVersion() + "\" }";
						break;
					}
				}
				// 5.4 update the solutionRevisoin (i.e., to update the modified date of the solutionrevision)
				mlpSR.setModified(currentDate);
				mlpSR.setDescription(dscs.getDescription());
				cdmsClient.updateSolutionRevision(mlpSR);
	
				// 5.5 Update the solution (i.e., to update the modified date of the solution).
				mlpSolution.setModified(currentDate);
				cdmsClient.updateSolution(mlpSolution);
			}
		}
		logger.debug(EELFLoggerDelegator.debugLogger, " updateExistingSolution() End ");
		return result;
	}

	/**
	 * @param mlpSolution
	 *            MLPSolution
	 * @param dscs
	 *            DSCompositeSolution
	 * @return Success or error
	 * @throws IOException
	 *             On error
	 * @throws AcumosException
	 *             On error
	 */
	public String updateSolnWithNewVersion(MLPSolution mlpSolution, DSCompositeSolution dscs) throws IOException, AcumosException {

		// Set solution active to true
		mlpSolution.setActive(true);
		cdmsClient.updateSolution(mlpSolution);

		// 6. Case 3 - update the solution with new version
		logger.debug(EELFLoggerDelegator.debugLogger, " updateSolnWithNewVersion() Start ");
		String result = "";
		Date currentDate = new Date();
		// 5.1 read the cdump file from the outputfolder
		String path = DSUtil.readCdumpPath(dscs.getAuthor(), confprops.getToscaOutputFolder());
		// Changed in current implementation Please check while merging
		String cdumpFileName = "acumos-cdump" + "-" + mlpSolution.getSolutionId();
		ObjectMapper mapper = new ObjectMapper();
		Cdump cdump = mapper.readValue(new File(path.concat(cdumpFileName).concat(".json")), Cdump.class);

		// 5.2 Update the cdump file with mtime
		cdump.setMtime(new SimpleDateFormat(confprops.getDateFormat()).format(currentDate));

		// 6.1 update the cdump file with cname = solution name and version = version
		cdump.setCname(dscs.getSolutionName());
		cdump.setVersion(dscs.getVersion());
		cdump.setValidSolution(false);

		// 6.2 Create new SolutionRevision using CDS : inputSolutionId, version : this will return the solutionrevisionid
		MLPSolutionRevision mlpSolutionRevision = new MLPSolutionRevision();
		try {
			mlpSolutionRevision.setSolutionId(mlpSolution.getSolutionId());
			mlpSolutionRevision.setDescription(dscs.getDescription());
			mlpSolutionRevision.setUserId(dscs.getAuthor());
			mlpSolutionRevision.setVersion(dscs.getVersion());
			mlpSolutionRevision.setValidationStatusCode(ValidationStatusCode.IP.toString());
			mlpSolutionRevision.setAccessTypeCode(AccessTypeCode.PR.toString());
			mlpSolutionRevision.setPublisher(dscs.getProvider());
			
			// Get the latest date in to variable and then use it.
			mlpSolutionRevision.setModified(currentDate);
			mlpSolutionRevision = cdmsClient.createSolutionRevision(mlpSolutionRevision);

			logger.debug(EELFLoggerDelegator.debugLogger," Successfully Created the SolutionRevision :  {} ", mlpSolutionRevision.getRevisionId());
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger,"Error : Exception in updateSolnWithNewVersion() : Failed to create the Solution Revision", e);
			throw new ServiceException("  Exception in updateSolnWithNewVersion() ", "222","Failed to create the Solution");
		}

		Gson gson = new Gson();
		String payload = gson.toJson(cdump);

		DSUtil.writeDataToFile(path, cdumpFileName, "json", payload);
		Artifact cdumpArtifact = new Artifact(cdumpFileName, "json", mlpSolution.getSolutionId(), dscs.getVersion(),
				path, payload.length());
		logger.debug(EELFLoggerDelegator.debugLogger," Successfully updated the Cdump file for solution ID :  {} ", mlpSolution.getSolutionId());
		// 5.3 upload the cdump file in Nexus Repositry. : this will return the nexus URI

		try {
			uploadFilesToRepository(mlpSolution.getSolutionId(), dscs.getVersion(), cdumpArtifact);
			dscs.setCdump(cdumpArtifact);
			logger.debug(EELFLoggerDelegator.debugLogger,"5. Successfully uploaded the Cdump file for solution ID :  {} ", mlpSolution.getSolutionId());
			
			logger.debug(EELFLoggerDelegator.debugLogger,"5.1 Successfully deleted the local Cdump file for solution ID : " + mlpSolution.getSolutionId());

		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger,"Error : Exception in updateSolnWithNewVersion() : Failed to upload the Cdump File to Nexus ",e);
			logger.debug(EELFLoggerDelegator.debugLogger,"5.1 Successfully deleted the local Cdump file for solution ID : " + mlpSolution.getSolutionId());
			throw new ServiceException("  Exception in updateSolnWithNewVersion() ", "222","Failed to create the Solution");
		}

		// 6.3 Create new Artifact : set the artifact code, the URI
		MLPArtifact mlpArtifact = null;

		try {
			mlpArtifact = new MLPArtifact();
			mlpArtifact.setArtifactTypeCode(props.getArtifactTypeCode());
			mlpArtifact.setDescription("Cdump File for : " + cdumpArtifact.getName() + " for SolutionID : "
					+ cdumpArtifact.getSolutionID() + " with version : " + cdumpArtifact.getVersion());
			mlpArtifact.setUri(cdumpArtifact.getNexusURI());
			mlpArtifact.setName(cdumpArtifact.getName());
			mlpArtifact.setUserId(dscs.getAuthor());
			mlpArtifact.setVersion(cdumpArtifact.getVersion());
			mlpArtifact.setSize(cdumpArtifact.getContentLength());
			// 6.4 Save the Artifact using CDS : this will return the artifactId
			mlpArtifact = cdmsClient.createArtifact(mlpArtifact);

			logger.debug(EELFLoggerDelegator.debugLogger," Successfully created the artifact for the cdumpfile for the solution : {0} artifact ID : {1}" ,mlpSolution.getSolutionId() , mlpArtifact.getArtifactId());
			// 6.5 assocaite the artifact with solutionrevision using CDS.
			cdmsClient.addSolutionRevisionArtifact(mlpSolution.getSolutionId(), mlpSolutionRevision.getRevisionId(),
					mlpArtifact.getArtifactId());

			logger.debug(EELFLoggerDelegator.debugLogger," Successfully associated the Solution Revision Artifact for solution ID  : " + mlpSolution.getSolutionId());
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger,"Error : Exception in updateSolnWithNewVersion() : Failed to create the Solution Artifact ", e);
			throw new ServiceException("  Exception in updateSolnWithNewVersion() ", "222","Failed to create the Solution");
		}
		// 5.5 Update the solution (i.e., to update the modified date of the solution).
		mlpSolution.setModified(currentDate);
		cdmsClient.updateSolution(mlpSolution);
		result = "{\"solutionId\": \"" + mlpSolution.getSolutionId() + "\", \"version\" : \"" + dscs.getVersion() + "\" }";
		logger.debug(EELFLoggerDelegator.debugLogger, " updateSolnWithNewVersion() End ");
		return result;
	}

	/**
	 * 
	 * @param solutionID
	 * @param version
	 * @param a
	 * @throws AcumosException
	 */
	private void uploadFilesToRepository(String solutionID, String version, Artifact a) throws AcumosException {
		logger.debug(EELFLoggerDelegator.debugLogger, "  uploadFilesToRepository() started ");
		FileInputStream fileInputStream = null;
		UploadArtifactInfo artifactInfo = null;
		try {

			// 1. group id ,2. artifact name, 3. version, 4. extension i.e., packaging, 5. size of content, 6. actual file input stream.
			fileInputStream = new FileInputStream(a.getPayloadURI());
			artifactInfo = nexusArtifactClient.uploadArtifact(confprops.getNexusgroupid(),
					a.getSolutionID() + "_" + a.getType(), a.getVersion(), a.getExtension(), a.getContentLength(),
					fileInputStream);
			a.setNexusURI(artifactInfo.getArtifactMvnPath());

			logger.debug(EELFLoggerDelegator.debugLogger, " uploadFilesToRepository() ended ");
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, " Exception Occured  uploadFilesToRepository() ", e);
			throw new ServiceException("  Exception in updateSolnWithNewVersion() ", "222",
					"Exception Occured while uploadFiles To Repository");
		}

	}

	@Override
	public boolean deleteCompositeSolution(String userId, String solutionId, String version)
			throws AcumosException, JSONException {
		logger.debug(EELFLoggerDelegator.debugLogger, " deleteCompositeSolution() Begin ");
		boolean result = true;

		try {
			// 1. get the solution and solutionRevision for the solution ID.
			MLPSolution mlpSolution = cdmsClient.getSolution(solutionId);
			boolean solutionFound = false;
			if (null != mlpSolution) {
				List<MLPSolutionRevision> mlpSolutionRevisions = cdmsClient.getSolutionRevisions(solutionId);

				// Check the size of mlpSolutionRevisions
				if (mlpSolutionRevisions.size() == 1) {
					mlpSolution.setActive(false);
					cdmsClient.updateSolution(mlpSolution);
				}

				for (MLPSolutionRevision mlpSolRevision : mlpSolutionRevisions) {
					// 2. match the Author with the input userId.
					if (mlpSolRevision.getVersion().equals(version) && mlpSolRevision.getUserId().equals(userId)) {
						// get the list of artifact for the Revision
						String revisionId = mlpSolRevision.getRevisionId();
						List<MLPArtifact> mlpArtifacts = cdmsClient.getSolutionRevisionArtifacts(solutionId,
								revisionId);

						for (MLPArtifact mlpArtifact : mlpArtifacts) {
							// if (mlpArtifact.getArtifactTypeCode().equals("CD")) {
							// write the above statement as below then Sonar will not give any warning
							if ("CD".equals(mlpArtifact.getArtifactTypeCode())) {
								String artifactId = mlpArtifact.getArtifactId();
								// Delete SolutionRevisionArtifact
								cdmsClient.dropSolutionRevisionArtifact(solutionId, revisionId, artifactId);
								logger.debug(EELFLoggerDelegator.debugLogger,
										" Successfully Deleted the SolutionRevisionArtifact ");
								// Delete Artifact
								// 3. Delete the artifact "CD"
								cdmsClient.deleteArtifact(artifactId);
								logger.debug(EELFLoggerDelegator.debugLogger,
										" Successfully Deleted the CDump Artifact ");
								// 4. Delete the cdump file from the Nexus
								nexusArtifactClient.deleteArtifact(mlpArtifact.getUri());
							}
						}

						// 5. Delete the SolutionRevision from the DB
						cdmsClient.deleteSolutionRevision(solutionId, mlpSolRevision.getRevisionId());
						logger.debug(EELFLoggerDelegator.debugLogger,
								" Successfully Deleted the Solution Revision ");
						solutionFound = true;
						result = true;
						deleteMember(solutionId);
					}
				}

				if (!solutionFound) {
					result = false;

				}
			} else {
				result = false;
				logger.debug(EELFLoggerDelegator.debugLogger, "Solution Not found :  {} ", solutionId);
			}
		} catch (Exception e) {
			result = false;
			logger.error(EELFLoggerDelegator.errorLogger, " Exception in deleteCompositeSolution() ",
					e);
			throw new ServiceException("  Exception in deleteCompositeSolution() ", "201",
					"Not able to delete the Solution Version");
		}
		logger.debug(EELFLoggerDelegator.debugLogger, " deleteCompositeSolution() End ");
		return result;

	}

	@Override
	public String closeCompositeSolution(String userId, String solutionId, String solutionVersion, String cid) {
		logger.debug(EELFLoggerDelegator.debugLogger, " closeCompositeSolution() : Begin ");
		String result = "";
		String resultTemplate = "{\"success\":\"%s\", \"errorMessage\":\"%s\"}";
		String id = "";
		if (null != cid && null == solutionId) {
			id = cid;
		} else if (null == cid && null != solutionId) {
			id = solutionId;
		}
		try {
			String cdumpFileName = "acumos-cdump" + "-" + id + ".json";
			if (userId == null) {
				result = String.format(resultTemplate, false, "Cannot perform requested operation – User Id missing");
			} else {
				String path = DSUtil.readCdumpPath(userId, confprops.getToscaOutputFolder());
				logger.debug(EELFLoggerDelegator.debugLogger, "Delete file :  {} ", path.concat(cdumpFileName));
				DSUtil.deleteFile(path.concat(cdumpFileName));
				logger.debug(EELFLoggerDelegator.debugLogger, "Delete User Dir :  {} ", path);
				DSUtil.rmUserdir(path);
				result = String.format(resultTemplate, true, "");
			}
		} catch (Exception e) {
			result = String.format(resultTemplate, false, "Cannot Close the Composite Solution");
			logger.error(EELFLoggerDelegator.errorLogger, " Exception in closeCompositeSolution() ", e);
		}
		logger.debug(EELFLoggerDelegator.debugLogger, " closeCompositeSolution() : End ");
		return result;
	}

	@Override
	public String clearCompositeSolution(String userId, String solutionId, String solutionVersion, String cid) {
		logger.debug(EELFLoggerDelegator.debugLogger, " clearCompositeSolution() : Begin ");
		String result = "";
		String resultTemplate = "{\"success\":\"%s\", \"errorMessage\":\"%s\"}";
		String id = "";
		if (null != cid && null == solutionId) {
			id = cid;
		} else if (null == cid && null != solutionId) {
			id = solutionId;
		}
		ObjectMapper mapper = new ObjectMapper();
		try {
			String cdumpFileName = "acumos-cdump" + "-" + id + ".json";
			String path = DSUtil.readCdumpPath(userId, confprops.getToscaOutputFolder());
			Cdump cdump = mapper.readValue(new File(path.concat(cdumpFileName)), Cdump.class);
			if (cdump.getNodes() == null && cdump.getRelations() == null) {
				result = String.format(resultTemplate, false, "No Nodes and Edges are there to Clear");
			}
			if (cdump.getNodes() != null) {
				cdump.getNodes().clear();
				result = String.format(resultTemplate, true, "");
			}
			if (cdump.getRelations() != null) {
				cdump.getRelations().clear();
				result = String.format(resultTemplate, true, "");
			}
			SimpleDateFormat sdf = new SimpleDateFormat(confprops.getDateFormat());
			cdump.setMtime(sdf.format(new Date()));
			Gson gson = new Gson();
			String jsonInString = gson.toJson(cdump);
			DSUtil.writeDataToFile(path, "acumos-cdump" + "-" + id, "json", jsonInString);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, " Exception in clearCompositeSolution() ", e);
			result = String.format(resultTemplate, false, "");
		}
		logger.debug(EELFLoggerDelegator.debugLogger, " clearCompositeSolution() : End ");
		return result;
	}

	@Override
	public String getCompositeSolutions(String userID, String visibilityLevel) throws AcumosException {
		logger.debug(EELFLoggerDelegator.debugLogger, " getCompositeSolutions() Begin ");
		String result = "[";

		List<MLPSolution> mlpSolutions = null;
		try {
			Map<String, Object> queryParameters = new HashMap<String, Object>();
			queryParameters.put("active", Boolean.TRUE);
			queryParameters.put("toolkitTypeCode", props.getToolKit());
			RestPageRequest restPageRequets = new RestPageRequest(0,props.getSolutionResultsetSize());
			//Code changes are to match the change in the CDS API Definition searchSolution in version 1.13.x
			RestPageResponse<MLPSolution> pageResponse = cdmsClient.searchSolutions(queryParameters, false, restPageRequets);

			mlpSolutions  = pageResponse.getContent();
			String solutionId = "";
			DSSolution dssolution = null;
			List<DSSolution> dsSolutions = new ArrayList<>();
			List<String> solutionIds = new ArrayList<>();
			List<MLPSolutionRevision> mlpSolRevisions = null;
			StringBuilder strBuilder = new StringBuilder();
			logger.debug(EELFLoggerDelegator.debugLogger, " The Date Format :  {} ", confprops.getDateFormat());
			SimpleDateFormat sdf = new SimpleDateFormat(confprops.getDateFormat());

			if (mlpSolutions == null) {
				logger.debug(EELFLoggerDelegator.debugLogger,
						" CommonDataService returned null Solution list");
			} else if (mlpSolutions.isEmpty()) {
				logger.debug(EELFLoggerDelegator.debugLogger,
						" CommonDataService returned empty Solution list");
			} else {
				logger.debug(EELFLoggerDelegator.debugLogger,
						" CommonDataService returned Solution list of size :  {} ", mlpSolutions.size());
				mlpSolRevisions = new ArrayList<>();

					for (MLPSolution mlpsol : mlpSolutions) {
						
						mlpSolRevisions = cdmsClient.getSolutionRevisions(mlpsol.getSolutionId());
						for (MLPSolutionRevision mlpSolRevision : mlpSolRevisions) {
								
							if (visibilityLevel.contains(mlpSolRevision.getAccessTypeCode())) {
								String userId = mlpSolRevision.getUserId();
								MLPUser user = cdmsClient.getUser(userId);
								if (null != mlpSolRevision.getAccessTypeCode()
										&& (("PR".equals(mlpSolRevision.getAccessTypeCode()) && userId.equals(userID))
												|| ("PB".equals(mlpSolRevision.getAccessTypeCode()))
												|| ("OR".equals(mlpSolRevision.getAccessTypeCode())))) {
									solutionId = mlpsol.getSolutionId();
									solutionIds.add(solutionId);
									
									String userName = user.getFirstName() + " " + user.getLastName();
									if (mlpSolRevisions == null) {
										logger.debug(EELFLoggerDelegator.debugLogger,
												" CommonDataService returned null SolutionRevision list");
									} else if (mlpSolRevisions.isEmpty()) {
										logger.debug(EELFLoggerDelegator.debugLogger,
												" CommonDataService returned empty SolutionRevision list");
									} else {
										logger.debug(EELFLoggerDelegator.debugLogger,
												" CommonDataService returned SolutionRevision list of size : "
														+ mlpSolRevisions.size());
										
											dssolution = new DSSolution();
											dssolution.setSolutionId(mlpsol.getSolutionId());
											dssolution.setSolutionRevisionId(mlpSolRevision.getRevisionId());
											dssolution.setCreatedDate(sdf.format(mlpSolRevision.getCreated().getTime()));
											dssolution.setIcon(null);
											// 1. Solution Name
											dssolution.setSolutionName(mlpsol.getName());
											// 5. Solution Provider
											dssolution.setProvider(mlpSolRevision.getPublisher());
											// 6. Solution Tool Kit
											dssolution.setToolKit(mlpsol.getToolkitTypeCode());
											// 7. Solution Category
											dssolution.setCategory(mlpsol.getModelTypeCode());
											// 8. Solution Description
											dssolution.setDescription(mlpsol.getDescription());
											// 9. Solution Visibility
											dssolution.setVisibilityLevel(mlpSolRevision.getAccessTypeCode());
											// 2. Solution Version
											dssolution.setVersion(mlpSolRevision.getVersion());
											// 3. Solution On boarder
											dssolution.setOnBoarder(userName);
											// 4. Solution Author
											dssolution.setAuthor(userName);
											dsSolutions.add(dssolution);
											strBuilder.append(dssolution.toJsonString());
											strBuilder.append(",");
										}
									}
								}
							}
						}

					}			if (strBuilder.length() > 1) {
				result = result + strBuilder.substring(0, strBuilder.length() - 1);
			}
			result = result + "]";

		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, " Exception in getSolutions() ", e);
			throw new ServiceException("  Exception in getSolutions() ", props.getSolutionErrorCode(),
					props.getSolutionErrorDesc());
		}
		logger.debug(EELFLoggerDelegator.debugLogger, " getSolutions() End ");
		return result;

	}

	@Override
	public String validateCompositeSolution(String userId, String solutionName, String solutionId, String version)
			throws AcumosException {
		String result = "";
		DSResult resultVo = new DSResult();
		logger.debug(EELFLoggerDelegator.debugLogger, "validateCompositeSolution() : Begin ");
		String path = DSUtil.readCdumpPath(userId, confprops.getToscaOutputFolder());
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		Date currentDate = new Date();
		try {
			Cdump cdump = null;
			// 1. Read the cdump file
			logger.debug(EELFLoggerDelegator.debugLogger, "1. Read the cdump file");
			String cdumpFileName = "acumos-cdump" + "-" + solutionId;
			cdump = mapper.readValue(new File(path.concat(cdumpFileName).concat(".json")), Cdump.class);
			// 2. get the Nodes from the cdump file and collect the nodeId's
			logger.debug(EELFLoggerDelegator.debugLogger,"2. get the Nodes from the cdump file and collect the nodeId's");
			List<Nodes> nodes = cdump.getNodes();
			List<Relations> relationsList = cdump.getRelations();
			// Check for the Nodes and Relations in the CDUMP is empty or not
			if (null != nodes && null != relationsList && !relationsList.isEmpty()) {
				resultVo = validateComposition(cdump);
				if(resultVo.getSuccess().equals("true")){
					// On successful validation generate the BluePrint file
					// Update the Cdump file with validaSolution as true after successful validation conditions and Modified Time also
					logger.debug(EELFLoggerDelegator.debugLogger,"On successful validation generate the BluePrint file.");
					cdump.setValidSolution(true);
					cdump.setMtime(new SimpleDateFormat(confprops.getDateFormat()).format(currentDate));
					Gson gson = new Gson();
					String emptyCdumpJson = gson.toJson(cdump);
					path = DSUtil.createCdumpPath(userId, confprops.getToscaOutputFolder());
					// Write Data to File
					DSUtil.writeDataToFile(path, "acumos-cdump" + "-" + solutionId, "json", emptyCdumpJson);
					Artifact cdumpArtifact = new Artifact(cdumpFileName, "json",solutionId, version, path, emptyCdumpJson.length());
					// upload the file to repository
					uploadFilesToRepository(solutionId, version,cdumpArtifact);
					result = createAndUploadBluePrint(userId, solutionId, solutionName, version,cdump);
				}
			}else if(null != nodes && nodes.size() == 1 && (null == relationsList || relationsList.isEmpty())){
				for(Nodes no : nodes){
					if(no.getType().getName().equals(MLMODEL_TYPE)){
						cdump.setValidSolution(true);
						cdump.setMtime(new SimpleDateFormat(confprops.getDateFormat()).format(currentDate));
						Gson gson = new Gson();
						String emptyCdumpJson = gson.toJson(cdump);
						path = DSUtil.createCdumpPath(userId, confprops.getToscaOutputFolder());
						// Write Data to File
						DSUtil.writeDataToFile(path, "acumos-cdump" + "-" + solutionId, "json", emptyCdumpJson);
						Artifact cdumpArtifact = new Artifact(cdumpFileName, "json",solutionId, version, path, emptyCdumpJson.length());
						// upload the file to repository
						uploadFilesToRepository(solutionId, version,cdumpArtifact);
						result = createAndUploadBluePrint(userId, solutionId, solutionName, version,cdump);
					}else {
						resultVo.setSuccess("false");
		                resultVo.setErrorDescription("Invalid Composite Solution : \"" + no.getName() + "\" should be ML Model only");
					}
				}
			} else {
				 resultVo.setSuccess("false");
                 resultVo.setErrorDescription("Invalid Composite Solution : Composite Solution should contain at least two models to connect");
			}
			 result = mapper.writeValueAsString(resultVo);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, " Exception in validateCompositeSolution() in Service ", e);
			throw new ServiceException("  Exception in validateCompositeSolution() ", "333","Failed to create the Solution Artifact");
		}
		logger.debug(EELFLoggerDelegator.debugLogger, " validateCompositeSolution() in Service  : End ");
		return result;
	}

	private DSResult validateEachNode(Cdump cdump) {
		DSResult resultVo = new DSResult();
		List<Nodes> nodes = cdump.getNodes();
		List<Relations> relationsList = cdump.getRelations();
		//Get the First and Last Model 
		String firstNodeId = getNodeIdForPosition(cdump, FIRST_NODE_POSITION);
		String lastNodeId = getNodeIdForPosition(cdump,LAST_NODE_POSITION);
           //For each Node : 
		for(Nodes node : nodes){
			//get the node type 
		    String nodeType = node.getType().getName();
		    String nodeId = node.getNodeId();
		    switch (nodeType) {
		        case MLMODEL_TYPE : 
		        	 //check whether its first node
		            if(nodeId.equals(firstNodeId)){ // Node--
		            	//Then it should be source of only one link.
		            	int connectedCnt = getSourceCountForNodeId(relationsList, nodeId);
		            	if(connectedCnt == 1) {  
		            		resultVo.setSuccess("true");
		            	} else { //Indicates its connected to multiple nodes, and validation fails.
		            		resultVo.setSuccess("false");
		                    resultVo.setErrorDescription("Invalid Composite Solution : MLModel \"" + node.getName() + "\" is connected to multiple Nodes");
		            	}
		            } else if(node.getNodeId().equals(lastNodeId)){ // --Node
		                //Then it should be target of only one link.
		            	int connectedCnt = getTargetCountForNodeId(relationsList, nodeId);
		            	if(connectedCnt == 1 ) { 
		                	//Check for correct port connected
		                	boolean isCorrectPortsConnected = correctPortsConnected(relationsList, nodeId);
		                	if(isCorrectPortsConnected){
		                		resultVo.setSuccess("true");
		                	}else {
		                		resultVo.setSuccess("false");
		                        resultVo.setErrorDescription("Invalid Composite Solution : Incorrect ports are connected to MLModel \"" + node.getName() + "\"");
							}
		            	} else {
		            		//Indicates multiple nodes are connected to the Node and vlaidation fails. 
		            		resultVo.setSuccess("false");
		                    resultVo.setErrorDescription("Invalid Composite Solution : Multiple Nodes are connected to MLModel \""+ node.getName() +"\"");
		            	}
		            	
		            } else { // --Node--
		            	 //It should be source of one link and target of another one link. 
		            	int connectedCnt = getTargetCountForNodeId(relationsList, nodeId);
		            	if(connectedCnt == 1 ) {
		            		connectedCnt = getSourceCountForNodeId(relationsList, nodeId);
		                	if(connectedCnt == 1) {  
		                		//Check for correct port connected
		                    	boolean isCorrectPortsConnected = correctPortsConnected(relationsList, nodeId);
		                    	if(isCorrectPortsConnected){
		                    		resultVo.setSuccess("true");
		                    		resultVo.setErrorDescription("");
		                    	}else {
		                    		resultVo.setSuccess("false");
		                            resultVo.setErrorDescription("Invalid Composite Solution : Incorrect ports are connected to MLModel \"" + node.getName() + "\"");
								}
		                	} else { //Indicates its connected to multiple nodes, and validation fails.
		                		resultVo.setSuccess("false");
		                        resultVo.setErrorDescription("Invalid Composite Solution : MLModel \"" + node.getName() + "\" is connected to multiple Nodes");
		                	}
		            	} else {
		            		//Indicates multiple nodes are connected to the Node and vlaidation fails. 
		            		resultVo.setSuccess("false");
		                    resultVo.setErrorDescription("Invalid Composite Solution : Multiple Nodes are connected to MLModel \""+ node.getName() +"\"");
		            	}
					}
		            break;
		        case DATABROKER_TYPE : 
		        	//DataBroker should be the first Node 
		        	if(nodeId.equals(firstNodeId)){ // Node--
		                // Then it should be source of only one link.
		            	int connectedCnt = getSourceCountForNodeId(relationsList, nodeId);
		            	if(connectedCnt == 1) {  
		            		// It should not be connected to Splitter 
		                    boolean connectedToSplitter = isConnectedToSplitter(nodes, relationsList, nodeId);
						if (!connectedToSplitter) {
							// It should not be connected to Collator
							boolean connectedToCollator = isConnectedToCollator(nodes, relationsList, nodeId);
							if (!connectedToCollator) {
								// Validate mapping
								Property[] p = node.getProperties();
								if (p.length == 1) {
									resultVo.setSuccess("true");
									resultVo.setErrorDescription("");
								} else {
									resultVo.setSuccess("false");
		                            resultVo.setErrorDescription("Invalid Composite Solution : DataBroker \"" + node.getName() + "\" Mapping Details are Incorrect");
								}
							} else {
		                    	resultVo.setSuccess("false");
		                        resultVo.setErrorDescription("Invalid Composite Solution : DataBroker \"" + node.getName() + "\" cannot be connected to Collator");
							}
						} else {
		                    	resultVo.setSuccess("false");
		                        resultVo.setErrorDescription("Invalid Composite Solution : DataBroker \"" + node.getName() + "\" cannot be connected to Splitter");
		                    }
		                    
		            	} else { //Indicates its connected to multiple nodes, and validation fails.
		            		resultVo.setSuccess("false");
		                    resultVo.setErrorDescription("Invalid Composite Solution : DataBroker \"" + node.getName() + "\" is connected to multiple Nodes");
		            	}
		                
		           } else {
		        	   resultVo.setSuccess("false");
		               resultVo.setErrorDescription("Invalid Composite Solution : DataBroker \"" + node.getName() + "\" should be the first Node");
		           }
		        	break;
				case DATAMAPPER_TYPE : 
					// Datamapper should not be the first node
					if (node.getNodeId().equals(firstNodeId)) {
						resultVo.setSuccess("false");
						resultVo.setErrorDescription("Invalid Composite Solution : DataMapper \""
								+ node.getName() + "\" should not be the first Node");
					} else if (node.getNodeId().equals(lastNodeId)) {
						resultVo.setSuccess("false");
						resultVo.setErrorDescription("Invalid Composite Solution : DataMapper \""
								+ node.getName() + "\" should not be the Last Node");
					} else {
						// Then it should be Target of only one link.
						int connectedCnt = getTargetCountForNodeId(relationsList, nodeId);
		            	if(connectedCnt == 1 ) {
		            		// Then it should be source of only one link.
		            		connectedCnt = getSourceCountForNodeId(relationsList, nodeId);
							if(connectedCnt == 1){
								// It should not be connected to Splitter
								boolean connectedToSplitter = isConnectedToSplitter(nodes, relationsList, nodeId);
								if (!connectedToSplitter) {
									// It should not be connected to Collator
									boolean connectedToCollator = isConnectedToCollator(nodes, relationsList, nodeId);
									if (!connectedToCollator) {
										// Validate mapping
										Property[] p = node.getProperties();
										if (p.length == 1) {
											resultVo.setSuccess("true");
											resultVo.setErrorDescription("");
										} else {
											resultVo.setSuccess("false");
											resultVo.setErrorDescription("Invalid Composite Solution : DataMapper \"" + node.getName()
															+ "\" Mapping Details are Incorrect");
										}
									} else {
										resultVo.setSuccess("false");
										resultVo.setErrorDescription("Invalid Composite Solution : DataMapper \""
												+ node.getName() + "\" cannot be connected to Collator");
									}
								} else {
									resultVo.setSuccess("false");
									resultVo.setErrorDescription("Invalid Composite Solution : DataMapper \""
											+ node.getName() + "\" cannot be connected to Splitter");
								}
							} else {
								resultVo.setSuccess("false");
								resultVo.setErrorDescription("Invalid Composite Solution : Multiple Nodes are connected to output port of DataMapper \""
										+ node.getName() + "\"");
							}
						} else { // Indicates its connected to multiple nodes, and validation fails.
							resultVo.setSuccess("false");
							resultVo.setErrorDescription("Invalid Composite Solution : DataMapper \""
									+ node.getName() + "\" is connected to multiple Nodes");
						}
					}
					break;
				case SPLITTER_TYPE:
					// Should not be the first node
					if (node.getNodeId().equals(firstNodeId)) {
						resultVo.setSuccess("false");
						resultVo.setErrorDescription("Invalid Composite Solution : Splitter \""
								+ node.getName() + "\" should not be the first Node");
					} else if (node.getNodeId().equals(lastNodeId)) { // It should not be last node i.e, should be
						// connected either one or more nodes.
						resultVo.setSuccess("false");
						resultVo.setErrorDescription("Invalid Composite Solution : Splitter \""
								+ node.getName() + "\" should not be the Last Node");
					}else if(null != node.getProperties()[0].getSplitter_map().getMap_outputs() && node.getProperties()[0].getSplitter_map().getMap_outputs().length != 0){
						SplitterMapOutput mapOutput[] = node.getProperties()[0].getSplitter_map().getMap_outputs();
						for(SplitterMapOutput smo : mapOutput){
							String errorIndicator = smo.getOutput_field().getError_indicator();
							if(errorIndicator.equals("false")){
								resultVo.setSuccess("false");
								resultVo.setErrorDescription("Invalid Composite Solution : Splitter \""
										+ node.getName() + "\" Mapping Contains Error Indicator as False");
								break;
							}
						}
					}else {
						// It should be target of only one link.
						int connectedCnt = getTargetCountForNodeId(relationsList, nodeId);
						Nodes sourceNode = null; 
						Nodes targetNode = null;
		            	if(connectedCnt == 1 ) {
		            		// Source should not be Splitter/collator/DataBroker/DataMapper i.e, ML Model
		            		boolean connectedToTool = false;
		            		for(Relations rel: relationsList){
		            			if(rel.getTargetNodeId().equals(nodeId)){
		            				sourceNode = getNodeForId(nodes, rel.getSourceNodeId());
		            				if(!sourceNode.getType().getName().equals(MLMODEL_TYPE)){
		            					connectedToTool = true;
		            					break;
		            				}
		            			}
		            		}
		            		if(!connectedToTool){
		            			// Target Should not be Splitter/collator/DataBroker/DataMapper i.e, ML Model
		            			connectedToTool = false;
		                		for(Relations rel: relationsList){
		                			if(rel.getSourceNodeId().equals(nodeId)){
		                				targetNode = getNodeForId(nodes, rel.getTargetNodeId());
		                				if(!targetNode.getType().getName().equals(MLMODEL_TYPE)){
		                					connectedToTool = true;
		                					break;
		                				}
		                			}
		                		}
		                		if(!connectedToTool){
		                			resultVo.setSuccess("true");
									resultVo.setErrorDescription("");
		                		} else {
		                			resultVo.setSuccess("false");
									resultVo.setErrorDescription("Invalid Composite Solution : Splitter \""
											+ node.getName() + "\" should not be connected to DS tool : \"" + targetNode.getName() + "\"");
		                		}
		            		} else {
		                		resultVo.setSuccess("false");
								resultVo.setErrorDescription("Invalid Composite Solution : DS Tool \""
										+ sourceNode.getName() + "\" should not be connected to Splitter : \"" + node.getName() + "\"");
		            		}
		            		
		            	} else {
		            		resultVo.setSuccess("false");
							resultVo.setErrorDescription("Invalid Composite Solution : Multiple Nodes are connected to Input Port of Splitter \""
									+ node.getName() + "\"");
		            	}
						
					}
					break;
		        case COLLATOR_TYPE : 
		        	// Should not be the first node
					if (node.getNodeId().equals(firstNodeId)) {
						resultVo.setSuccess("false");
						resultVo.setErrorDescription("Invalid Composite Solution : Collator \""
								+ node.getName() + "\" should not be the first Node");
					} else if (node.getNodeId().equals(lastNodeId)) { // It should not be last node i.e, should be
						// connected either one or more nodes.
						resultVo.setSuccess("false");
						resultVo.setErrorDescription("Invalid Composite Solution : Collator \""
								+ node.getName() + "\" should not be the Last Node");
					}else if(null != node.getProperties()[0].getCollator_map().getMap_inputs() && node.getProperties()[0].getCollator_map().getMap_inputs().length != 0){
						CollatorMapInput mapInput[] = node.getProperties()[0].getCollator_map().getMap_inputs();
						for(CollatorMapInput cmi : mapInput){
							String errorIndicator = cmi.getInput_field().getError_indicator();
							if(errorIndicator.equals("false")){
								resultVo.setSuccess("false");
								resultVo.setErrorDescription("Invalid Composite Solution : Collator \""
										+ node.getName() + "\" Mapping Contains Error Indicator as False");
								break;
							}
						}
					}else {
						// It should be source of only one link.
		        		int connectedCnt = getSourceCountForNodeId(relationsList, nodeId);
		        		Nodes sourceNode = null; 
						Nodes targetNode = null;
						if(connectedCnt == 1){
							// Source should not be Splitter/collator/DataBroker/DataMapper i.e, ML Model
		            		boolean connectedToTool = false;
		            		for(Relations rel: relationsList){
		            			if(rel.getTargetNodeId().equals(nodeId)){
		            				sourceNode = getNodeForId(nodes, rel.getSourceNodeId());
		            				if(!sourceNode.getType().getName().equals(MLMODEL_TYPE)){
		            					connectedToTool = true;
		            					break;
		            				}
		            			}
		            		}
		            		if(!connectedToTool){
		            			// Target Should not be Splitter/collator/DataBroker/DataMapper i.e, ML Model
		            			connectedToTool = false;
		                		for(Relations rel: relationsList){
		                			if(rel.getSourceNodeId().equals(nodeId)){
		                				targetNode = getNodeForId(nodes, rel.getTargetNodeId());
		                				if(!targetNode.getType().getName().equals(MLMODEL_TYPE)){
		                					connectedToTool = true;
		                					break;
		                				}
		                			}
		                		}
		                		if(!connectedToTool){
		                			resultVo.setSuccess("true");
									resultVo.setErrorDescription("");
		                		} else {
		                			resultVo.setSuccess("false");
									resultVo.setErrorDescription("Invalid Composite Solution : Collator \""
											+ node.getName() + "\" should not be connected to DS tool : \"" + sourceNode.getName() + "\"");
		                		}
		            		} else {
		                		resultVo.setSuccess("false");
								resultVo.setErrorDescription("Invalid Composite Solution : DS Tool \""
										+ sourceNode.getName() + "\" should not be connected to Collator : \"" + node.getName() + "\"");
		            		}
		            		
						} else {
							resultVo.setSuccess("false");
							resultVo.setErrorDescription("Invalid Composite Solution : Multiple Nodes are connected to output port of Collator \""
									+ node.getName() + "\"");
						}
					}
					break;
		        default:
		    }
		    if (resultVo.getSuccess().equals("false")) {
				break;
			}
		}
		
		return resultVo;
	}

	private boolean isConnectedToCollator(List<Nodes> nodes, List<Relations> relationsList, String nodeId) {
		boolean isConnected = false;
		List<String> collatorNodeIds = new ArrayList<String>();
		//1. Check if composite solution have splitter
		for(Nodes n : nodes){
			if(n.getType().getName().equals(COLLATOR_TYPE)){
				collatorNodeIds.add(n.getNodeId());
			}
		}
		
		if(!collatorNodeIds.isEmpty()){ //Collator exists in the composite solution
			//From relationsList get the links where source Node id is databroker
			for(Relations link : relationsList){
				if(link.getSourceNodeId().equals(nodeId)){
					//check if target node id is splitter id 
					for(String targetId : collatorNodeIds){
						if(link.getTargetNodeId().equals(targetId)){
							isConnected = true;
						}
					}
				}
			}
		}
		return isConnected;
	}
	private boolean isConnectedToSplitter(List<Nodes> nodes, List<Relations> relationsList, String nodeId) {
		boolean isConnected = false;
		List<String> splitterNodeIds = new ArrayList<String>();
		//1. Check if composite solution have splitter
		for(Nodes n : nodes){
			if(n.getType().getName().equals(SPLITTER_TYPE)){
				splitterNodeIds.add(n.getNodeId());
			}
		}
		
		if(!splitterNodeIds.isEmpty()){ //Splitter exists in the composite solution
			//From relationsList get the links where source Node id is databroker
			for(Relations link : relationsList){
				if(link.getSourceNodeId().equals(nodeId)){
					//check if target node id is splitter id 
					for(String targetId : splitterNodeIds){
						if(link.getTargetNodeId().equals(targetId)){
							isConnected = true;
						}
					}
				}
			}
		}
		return isConnected;
	}

	private int getTargetCountForNodeId(List<Relations> relationsList, String nodeId) {
		int connectedCnt = 0;
		for(Relations rel : relationsList){
			String sourceNodeId = rel.getTargetNodeId();
			if(nodeId.equals(sourceNodeId)){
				connectedCnt++;
			}
		}
		return connectedCnt;
	}

	private int getSourceCountForNodeId(List<Relations> relationsList, String nodeId) {
		int connectedCnt = 0;
		for(Relations rel : relationsList){
			String sourceNodeId = rel.getSourceNodeId();
			if(nodeId.equals(sourceNodeId)){
				connectedCnt++;
			}
		}
		return connectedCnt;
	}

	/**
	 * @param cdump
	 * @return
	 */
	private DSResult validateComposition(Cdump cdump) {
		DSResult result = new DSResult();
		List<Nodes> nodes = cdump.getNodes();
		List<Relations> relationsList = cdump.getRelations();
		//check if any isolated node 
		List<String> isolatedNodesName = getIsolatedNodesName(nodes, relationsList);
		if(isolatedNodesName.isEmpty()){
			//Composite solution should have only one first Node
			List<String> firstNodeNames = getNodesForPosition(cdump, FIRST_NODE_POSITION);
			if(firstNodeNames.size() == 1){
				//Composite solution should have only one last Node
				List<String> lastNodeNames = getNodesForPosition(cdump, LAST_NODE_POSITION);
				if(lastNodeNames.size() == 1){
						result = validateEachNode(cdump);
				} else {
					result.setSuccess("false");
					result.setErrorDescription("Invalid Composite Solution : Nodes " + lastNodeNames + " are not connected");
				}
			} else if (firstNodeNames.size() == 0) { 
				result.setSuccess("false");
				result.setErrorDescription("Invalid Composite Solution : Cyclic Graph is not permitted");
			} else {
				result.setSuccess("false");
				result.setErrorDescription("Invalid Composite Solution : Nodes " + firstNodeNames + " are not connected");
			}
		} else {
			result.setSuccess("false");
			result.setErrorDescription("Invalid Composite Solution : " + isolatedNodesName + " are isolated nodes");
		}
		return result;
	}

	private List<String> getIsolatedNodesName(List<Nodes> nodes, List<Relations> relationsList) {
		//1. get the unique nodeids list from the relation
		HashSet<String> relNodeIdSet = new HashSet<String>();
		for (Relations rhs : relationsList) {
			relNodeIdSet.add(rhs.getSourceNodeId());
			relNodeIdSet.add(rhs.getTargetNodeId());
		}
		//2. Check Isolated nodes
		List<String> isolatedNodesName = new ArrayList<String>();
		for(Nodes node : nodes){
			if (!relNodeIdSet.contains(node.getNodeId())) {
				isolatedNodesName.add(node.getName());
			}
		}
		return isolatedNodesName;
	}

	private boolean correctPortsConnected(List<Relations> relationsList, String nodeId) {
		boolean isCorrectPortsConnected =  true;//ValidateCorrectPortsConnected(cdump);
		String srcOperation = null;
		String trgOperation = null;
		for (Relations rel : relationsList) {
			if(nodeId.equals(rel.getSourceNodeId())){
				srcOperation = rel.getSourceNodeRequirement().replace("+", OPERATION_EXTRACTOR);
				srcOperation = srcOperation.split(OPERATION_EXTRACTOR)[0];
				//Now check if same input port is connected or not and its not the first node. 
					boolean isSameportConnected = false;
					for (Relations rel2 : relationsList) {
						if(nodeId.equals(rel2.getTargetNodeId())){ //Node is target of some other link
							trgOperation = rel2.getTargetNodeCapability().replace("+", OPERATION_EXTRACTOR);
							trgOperation = trgOperation.split(OPERATION_EXTRACTOR)[0];
							if(trgOperation.equals(srcOperation)){
								isSameportConnected = true;
								break;
							}
						}
					}
					if(!isSameportConnected){
						isCorrectPortsConnected = false;
						break;
					}
			}
		}
		return isCorrectPortsConnected;
	}
	


	/**
	 * @return
	 * @throws ServiceException 
	 */
	private String createAndUploadBluePrint(String userId, String solutionId, String solutionName, String version, Cdump cdump) throws ServiceException {
		String result = "";
		String bluePrintFileName = "";
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.setSerializationInclusion(Include.NON_NULL);
		String path = DSUtil.readCdumpPath(userId, confprops.getToscaOutputFolder());
		logger.debug(EELFLoggerDelegator.debugLogger,"6. On successful validation generate the BluePrint file");
		BluePrint bluePrint = new BluePrint();
		// 7. Set the Solution name and version
		logger.debug(EELFLoggerDelegator.debugLogger,"7. On successful validation generate the BluePrint file");
		bluePrint.setName(solutionName);
		bluePrint.setVersion(version);
		
		String probeIndicator = null;
		probeIndicator = cdump.getProbeIndicator();
		ProbeIndicator pIndicator = new ProbeIndicator();
		pIndicator.setValue(probeIndicator);
		List<ProbeIndicator> probeLst = new ArrayList<ProbeIndicator>();
		probeLst.add(pIndicator);
		bluePrint.setProbeIndicator(probeLst); // In cdump probeIndicator is a string, in blueprint it should not be an array just a string
		
		List<Container> containerList = new ArrayList<Container>();
		Container container = new Container();
		BaseOperationSignature bos = new BaseOperationSignature();
		String opearion = "";
		List<Nodes> nodes = cdump.getNodes();
		List<Relations> relationsList = cdump.getRelations();
		Set<String> sourceNodeId = new HashSet<>();
		Set<String> targetNodeId = new HashSet<>();
		// if relations not equal to null i.e its contains more than one model in canvas
		if(null != relationsList && !relationsList.isEmpty()){
			for (Relations rlns : relationsList) {
				sourceNodeId.add(rlns.getSourceNodeId());
				targetNodeId.add(rlns.getTargetNodeId());
			}
			sourceNodeId.removeAll(targetNodeId);
			for (Relations rltn : relationsList) {
				if (sourceNodeId.contains(rltn.getSourceNodeId())) {
					opearion = rltn.getSourceNodeRequirement().replace("+", OPERATION_EXTRACTOR);
					opearion = opearion.split(OPERATION_EXTRACTOR)[0];
					bos.setOperation_name(opearion);
					container.setOperation_signature(bos);
					String containerName = rltn.getSourceNodeName();
					container.setContainer_name(containerName);
					containerList.add(container);
				}
			}
		}else{
			// canvas contains only one model which is ML Model only
			for(Nodes no : nodes){
				String reqOperationName = no.getRequirements()[0].getCapability().getId();
				bos.setOperation_name(reqOperationName);
				container.setOperation_signature(bos);
				container.setContainer_name(no.getName());
				containerList.add(container);
			}
		}
		bluePrint.setInput_ports(containerList);
		// 8. Get the nodes from Cdump file & set the required details in the blueprint nodes
		logger.debug(EELFLoggerDelegator.debugLogger,"8. Get the nodes from Cdump file & set the required details in the blueprint nodes");
		List<Nodes> cdumpNodes = cdump.getNodes();
		List<Node> bpnodes = new ArrayList<>();
		List<MLPSolutionRevision> mlpSolRevisions = null;
		MLPSolutionRevision mlpSolRevision = null;
		String nodeName = "";
		String nodeId = "";
		String nodeSolutionId = "";
		String nodeVersion = "";
		String dockerImageURL = null;
		Node bpnode = null;

		// 9. Extract NodeId, NodeName,NodeSolutionId,NodeVersion
		logger.debug(EELFLoggerDelegator.debugLogger,"9. Extract NodeId, NodeName,NodeSolutionId,NodeVersion");
		for (Nodes n : cdumpNodes) {
			nodeName = n.getName(); // TO set in the blue print
			nodeId = n.getNodeId(); 
			nodeSolutionId = n.getNodeSolutionId(); // To get the DockerImageUrl
			nodeVersion = n.getNodeVersion(); // To get the nodeVersion
			// 11. Get the MlpSolutionRevisions from CDMSClient for the NodeSolutionId
			logger.debug(EELFLoggerDelegator.debugLogger,"11. Get the MlpSolutionRevisions from CDMSClient for the NodeSolutionId");
			mlpSolRevision = getSolutionRevisions(nodeSolutionId, nodeVersion, mlpSolRevision);
						
			if (n.getType().getName().equalsIgnoreCase(props.getGdmType())) {
				logger.debug(EELFLoggerDelegator.debugLogger,"GDM Found :  {} ", n.getNodeId());
				// For Generic Data Mapper, get the dockerImageUrl by deploying the GDM Construct the image for the Generic Data mapper
				logger.debug(EELFLoggerDelegator.debugLogger,"For Generic Data Mapper, get the dockerImageUrl by deploying the GDM Construct the image for the Generic Data mapper");
				dockerImageURL = gdmService.createDeployGDM(cdump, n.getNodeId(), userId);
				if (null == dockerImageURL) {
					logger.debug(EELFLoggerDelegator.debugLogger,"Error : Issue in createDeployGDM() : Failed to create the Solution Artifact ");
					throw new ServiceException("  Issue in createDeployGDM() ", "333",
							"Issue while crearting and deploying GDM image");
				}
			} else if(n.getType().getName().equalsIgnoreCase(props.getDatabrokerType())) {
				logger.debug(EELFLoggerDelegator.debugLogger,"DataBroker Found :  {} ", n.getType().getName());
				dockerImageURL = dbService.getDataBrokerImageURI(n);
				if (null == dockerImageURL) {
					logger.debug(EELFLoggerDelegator.debugLogger,"Error : Issue in createDeployDataBroker() : Failed to create the Solution Artifact ");
					throw new ServiceException("  Issue in createDeployGDM() ", "333",
							"Issue while crearting and deploying DataBroker image");
				}
			} else {
				// Else for basic models, upload the image and get the uri
				// 12. Get the list of artifact from CDMSClient which will return the DockerImageUrl
				logger.debug(EELFLoggerDelegator.debugLogger,"12. Get the list of artifact from CDMSClient which will return the DockerImageUrl");
				dockerImageURL = getDockerImageURL(nodeSolutionId, mlpSolRevision);
			}
			// 13. Set the values in the bluePrint Node
			logger.debug(EELFLoggerDelegator.debugLogger,"13. Set the values in the bluePrint Node");
			bpnode = new Node();
			bpnode.setContainer_name(nodeName);
			bpnode.setImage(dockerImageURL);
			String node_type = (null != n.getType().getName()? n.getType().getName().trim() : "");
			bpnode.setNode_type(node_type); 
			
			// Check for the Node type is DataBroker or not
			if (node_type.equals(props.getDatabrokerType())) {
				// Need to set all the values of DataBrokerMap Get the Property[] from Nodes
				BPDataBrokerMap bpdbMap = getDataBrokerDetails(n);
				bpnode.setData_broker_map(bpdbMap);
			}
			
			// check for the Node type is Splitter or not
			if(node_type.equals(props.getSplitterType())){
				// Need to set all the values of SplitterMap Get the Property[] from Nodes
				BPSplitterMap bpsMap = getSplitterDetails(n);
				bpnode.setSplitter_map(bpsMap);
				bpnode.setImage("");
			}
			// check for the Node type is Collator or not
			if(node_type.equals(props.getCollatorType())){
				// Need to set all the values of CollatorMap Get the Property[] from Nodes
				BPCollatorMap bpcMap = getCollatorDetails(n);
				bpnode.setCollator_map(bpcMap);
				bpnode.setImage("");
			}
			
			String protoUri = n.getProtoUri();
			if(n.getType().getName().equals(props.getCollatorType())){
				bpnode.setProto_uri(""); 
			}else if (n.getType().getName().equals(props.getSplitterType())) {
				bpnode.setProto_uri(""); 
			}else{
				bpnode.setProto_uri(protoUri); 
			}
			//Set operation_signature_list
			List<Capabilities> capabilities = Arrays.asList(n.getCapabilities());
			String nodeOperationName = null;
			List<Container> containerLst = new ArrayList<Container>();
			
			List<OperationSignatureList> oslList = new ArrayList<>();
			OperationSignatureList osll = null;
			NodeOperationSignature nos = null;
			List<Container> connectedToList;
			connectedToList = new ArrayList<>();
			// If the relations are null or empty i.e single ML model is there in Canvas 
			if(null != relationsList && !relationsList.isEmpty()){
				//Get the connected port 
				String connectedPort = getConnectedPort(cdump.getRelations(), n.getNodeId());
				for(Capabilities c : capabilities ){
					nodeOperationName = c.getTarget().getId();
					if(nodeOperationName.equals(connectedPort)){
						osll = new OperationSignatureList();
						nos = new NodeOperationSignature();
						nos.setOperation_name(nodeOperationName);
						nos.setInput_message_name(c.getTarget().getName()[0].getMessageName());  
						//NodeOperationSignature input_message_name should have been array, as operation can have multiple input messages.  
						//Its seems to be some gap
						nos.setOutput_message_name(getOutputMessage(n.getRequirements(), nodeOperationName));
						osll.setOperation_signature(nos);
						containerLst = getRelations(cdump, nodeId);
						osll.setConnected_to(containerLst);
						oslList.add(osll);
					}
				}
			}else {
				// canvas contains only one Model which is ML Model only
				for(Capabilities c : capabilities ){
					nodeOperationName = c.getTarget().getId();
					osll = new OperationSignatureList();
					nos = new NodeOperationSignature();
					nos.setOperation_name(nodeOperationName);
					nos.setInput_message_name(c.getTarget().getName()[0].getMessageName());  
					nos.setOutput_message_name(getOutputMessage(n.getRequirements(), nodeOperationName));
					osll.setOperation_signature(nos);
					osll.setConnected_to(connectedToList);
					oslList.add(osll);
				}
			}
			bpnode.setOperation_signature_list(oslList);
			// 14. Add the nodedetails to bluepring nodes list
			logger.debug(EELFLoggerDelegator.debugLogger,"14. Add the nodedetails to blueprint nodes list");
			bpnodes.add(bpnode);
		}
		bluePrint.setNodes(bpnodes);

		// 20. Create the MLPArtifact
		logger.debug(EELFLoggerDelegator.debugLogger,"20. Create the MLPArtifact");
		MLPArtifact mlpArtifact = null;
		try {
			
			// 15. Write Data to bluePrint file and construct the name of the file
			logger.debug(EELFLoggerDelegator.debugLogger,"15. Write Data to bluePrint file and construct the name of the file");
			bluePrintFileName = "BluePrint" + "-" + solutionId;
			// 16. Convert bluePrint to json
			logger.debug(EELFLoggerDelegator.debugLogger,"16. Convert bluePrint to json");
			String bluePrintJson = mapper.writeValueAsString(bluePrint);
			// 17. Create and write details to file
			logger.debug(EELFLoggerDelegator.debugLogger,"17. Create and write details to file");
			DSUtil.writeDataToFile(path, bluePrintFileName, "json", bluePrintJson);
			// 18. Get the Artifact Data
			logger.debug(EELFLoggerDelegator.debugLogger,"18. Get the Artifact Data");
			Artifact bluePrintArtifact = new Artifact(bluePrintFileName, "json", solutionId, version,
					path, bluePrintJson.length());
			// 19. Upload the file to Nexus
			logger.debug(EELFLoggerDelegator.debugLogger,"19. Upload the file to Nexus");
			uploadFilesToRepository(solutionId, version, bluePrintArtifact);
			
			mlpArtifact = new MLPArtifact();
			mlpArtifact.setArtifactTypeCode("BP");
			mlpArtifact.setDescription("BluePrint File for : " + solutionName + " for SolutionID : "
					+ solutionId + " with version : " + version);
			mlpArtifact.setUri(bluePrintArtifact.getNexusURI());
			mlpArtifact.setName(bluePrintArtifact.getName());
			mlpArtifact.setUserId(userId);
			mlpArtifact.setVersion(version);
			mlpArtifact.setSize(bluePrintJson.length());

			// 21. Get the SolutionRevisions from CDMSClient.
			logger.debug(EELFLoggerDelegator.debugLogger,"21. Get the SolutionRevisions from CDMSClient.");
			mlpSolRevisions = cdmsClient.getSolutionRevisions(solutionId);
			MLPSolutionRevision compositeSolutionRevision = null;
			// 22. Iterate over MLPSolutionRevisions and get the CompositeSolutionRevision.
			logger.debug(EELFLoggerDelegator.debugLogger,"22. Iterate over MLPSolutionRevisions and get the CompositeSolutionRevision.");
			for (MLPSolutionRevision solRev : mlpSolRevisions) {
				if (solRev.getVersion().equals(version)) {
					compositeSolutionRevision = solRev;
					break;
				}
			}
			List<MLPArtifact> mlpArtiLst = cdmsClient.getSolutionRevisionArtifacts(solutionId,compositeSolutionRevision.getRevisionId());

			// 23. Creating the Artifact from CDMSClient.
			logger.debug(EELFLoggerDelegator.debugLogger,"23. Creating the Artifact from CDMSClient.");

			boolean bluePrintExists = false;
			for (MLPArtifact mlpArt : mlpArtiLst) {
				if (props.getBlueprintArtifactType().equals(mlpArt.getArtifactTypeCode())) {
					// update the artifact details with artifactId
					mlpArtifact.setArtifactId(mlpArt.getArtifactId());
					bluePrintExists = true;
					break;
				}
			}
			logger.debug(EELFLoggerDelegator.debugLogger, " ArtifactFlag for BP : " + bluePrintExists);
			if (bluePrintExists) {

				// 24. Update the Artifact which is already exists as BP
				cdmsClient.updateArtifact(mlpArtifact);
				logger.debug(EELFLoggerDelegator.debugLogger,"24. Updated the ArtifactTypeCode BP which is already exists");
			} else {
				mlpArtifact = cdmsClient.createArtifact(mlpArtifact);

				logger.debug(EELFLoggerDelegator.debugLogger,"Successfully created the artifact for the BluePrint for the solution : "
								+ solutionId + " artifact ID : " + mlpArtifact.getArtifactId());

				// 25. Associate theSolutionRevisionArtifact for solution ID.

				logger.debug(EELFLoggerDelegator.debugLogger,"25. Associate the SolutionRevisionArtifact for solution ID.");

				cdmsClient.addSolutionRevisionArtifact(solutionId,compositeSolutionRevision.getRevisionId(), mlpArtifact.getArtifactId());

				logger.debug(EELFLoggerDelegator.debugLogger," Successfully associated the Solution Revision Artifact for solution ID  : "
								+ solutionId);
			}
			result = "{\"success\" : \"true\", \"errorDescription\" : \"\"}";
		} catch (Exception e) {
			result = "{\"success\" : \"false\", \"errorDescription\" : \"Exception while creating Blutprint.\"}";
			logger.error(EELFLoggerDelegator.errorLogger,"Error : Exception in validateCompositeSolution() : Failed to create the Solution Artifact ",e);
			throw new ServiceException("  Exception in validateCompositeSolution() ", "333","Failed to create the Solution Artifact");
			
		}
		return result;
	}


	private BPCollatorMap getCollatorDetails(Nodes n) {
		Property[] prop = n.getProperties();
		ArrayList<Property> propsList = new ArrayList<Property>(Arrays.asList(prop));
		String collator_type = null;
		String output_message_signature = null;
		
		BPCollatorMap bpcMap = new BPCollatorMap();
		
		List<CollatorMapInput> collatorMapInputLst = new ArrayList<CollatorMapInput>();
		List<CollatorMapOutput> collatorMapOutputLst = new ArrayList<CollatorMapOutput>();
		
		// Iterate over the PropertyList of the Node from Cdump
		if (null != propsList) {
			for (Property coprops : propsList) {
				if (null != coprops.getCollator_map()) {
					// Set all the values from Property List of cdump to Blueprint CollatorMap object
					collator_type = coprops.getCollator_map().getCollator_type();
					output_message_signature = coprops.getCollator_map().getOutput_message_signature();

					bpcMap.setCollator_type(collator_type);
					bpcMap.setOutput_message_signature(output_message_signature);

					// Get the CollatorMapInput[] from CollatorMap from the Cdump file
					// Check if the collator_type is Param based then mapInputs
					// and mapOutputs should be populated, else not
					if (collator_type.equals(props.getDefaultCollatorType())) {
						bpcMap.setMap_inputs(null);
						bpcMap.setMap_outputs(null);
					} else {
						if (null != coprops.getCollator_map().getMap_inputs() && coprops.getCollator_map().getMap_inputs().length != 0) {
							CollatorMapInput[] coMapIn = coprops.getCollator_map().getMap_inputs();
							// Convert CollatorMapInput[] to List
							ArrayList<CollatorMapInput> cmiLst = new ArrayList<CollatorMapInput>(
									Arrays.asList(coMapIn));
							// Iterate over the CollatorMapInput List of the Cdump file
							for (CollatorMapInput cmi : cmiLst) {

								CollatorInputField coInField = new CollatorInputField();
								CollatorMapInput coMapInput = new CollatorMapInput();

								// set the all values into CollatorInputField of BluePrint File
								coInField.setParameter_name(cmi.getInput_field().getParameter_name());
								coInField.setParameter_tag(cmi.getInput_field().getParameter_tag());
								coInField.setParameter_type(cmi.getInput_field().getParameter_type());
								coInField.setParameter_role(cmi.getInput_field().getParameter_role());
								coInField.setSource_name(cmi.getInput_field().getSource_name());
								coInField.setMessage_signature(cmi.getInput_field().getMessage_signature());
								coInField.setMapped_to_field(cmi.getInput_field().getMapped_to_field());
								coInField.setError_indicator(cmi.getInput_field().getError_indicator());

								coMapInput.setInput_field(coInField);
								collatorMapInputLst.add(coMapInput);
							}
							// Convert the CollatorMapInput List to CollatorMapInput[]
							CollatorMapInput[] coMapInArr = new CollatorMapInput[collatorMapInputLst.size()];
							coMapInArr = collatorMapInputLst.toArray(coMapInArr);
							bpcMap.setMap_inputs(coMapInArr);
						}

						// Get the CollatorMapOutput[] from CollatorMap from the Cdump file
						if (null != coprops.getCollator_map().getMap_outputs() && coprops.getCollator_map().getMap_outputs().length != 0) {
							CollatorMapOutput[] coMapOut = coprops.getCollator_map().getMap_outputs();
							ArrayList<CollatorMapOutput> cmoLst = new ArrayList<CollatorMapOutput>(
									Arrays.asList(coMapOut));

							// Iterate over CollatorMapOutput List of Cdump File
							for (CollatorMapOutput cmo : cmoLst) {

								CollatorOutputField coOutField = new CollatorOutputField();
								CollatorMapOutput coMapOutput = new CollatorMapOutput();

								// set the all values into CollatorOutputField of BluePrint File
								coOutField.setParameter_name(cmo.getOutput_field().getParameter_name());
								coOutField.setParameter_tag(cmo.getOutput_field().getParameter_tag());
								coOutField.setParameter_type(cmo.getOutput_field().getParameter_type());
								coOutField.setParameter_role(cmo.getOutput_field().getParameter_role());

								coMapOutput.setOutput_field(coOutField);
								collatorMapOutputLst.add(coMapOutput);
							}
							// Convert the CollatorMapOutput List to CollatorMapOutput[]
							CollatorMapOutput[] coMapOutArr = new CollatorMapOutput[collatorMapOutputLst.size()];
							coMapOutArr = collatorMapOutputLst.toArray(coMapOutArr);
							bpcMap.setMap_outputs(coMapOutArr);
						}
					}
				}
			}
		}
		return bpcMap;
	}

	private BPSplitterMap getSplitterDetails(Nodes n) {
		Property[] prop = n.getProperties();
		ArrayList<Property> propsList = new ArrayList<Property>(Arrays.asList(prop));
		String splitter_type = null;
		String input_message_signature = null;
		
		BPSplitterMap bpsMap = new BPSplitterMap();
		
		List<SplitterMapInput> splitterMapInputLst = new ArrayList<SplitterMapInput>();
		List<SplitterMapOutput> splitterMapOutputLst = new ArrayList<SplitterMapOutput>();
		
		// Iterate over the PropertyList of the Node from Cdump
		if (null != propsList) {
			for (Property splprops : propsList) {
				if (null != splprops.getSplitter_map()) {
					// Set all the values from Property List of cdump to Blueprint SplitterMap object
					splitter_type = splprops.getSplitter_map().getSplitter_type();
					input_message_signature = splprops.getSplitter_map().getInput_message_signature();

					bpsMap.setSplitter_type(splitter_type);
					bpsMap.setInput_message_signature(input_message_signature);

					// Get the SplitterMapInput[] from SplitterMap from the Cdump file
					// Check if the splitter_type is Param based then mapInputs
					// and mapOutputs should be populated, else not
					if (splitter_type.equals(props.getDefaultSplitterType())) {
						bpsMap.setMap_inputs(null);
						bpsMap.setMap_outputs(null);
					} else {
						if (null != splprops.getSplitter_map().getMap_inputs() && splprops.getSplitter_map().getMap_inputs().length !=0) {
							SplitterMapInput[] spMapIn = splprops.getSplitter_map().getMap_inputs();
							// Convert SplitterMapInput[] to List
							ArrayList<SplitterMapInput> smiLst = new ArrayList<SplitterMapInput>(
									Arrays.asList(spMapIn));
							// Iterate over the SplitterMapInput List of the Cdump file
							for (SplitterMapInput smi : smiLst) {
								SplitterInputField spInField = new SplitterInputField();
								SplitterMapInput spMapInput = new SplitterMapInput();
								// set the all values into SplitterInputField of BluePrint File
								spInField.setParameter_name(smi.getInput_field().getParameter_name());
								spInField.setParameter_tag(smi.getInput_field().getParameter_tag());
								spInField.setParameter_type(smi.getInput_field().getParameter_type());
								spInField.setParameter_role(smi.getInput_field().getParameter_role());
								spMapInput.setInput_field(spInField);
								splitterMapInputLst.add(spMapInput);
							}
							// Convert the SplitterMapInput List to SplitterMapInput[]
							SplitterMapInput[] spMapInArr = new SplitterMapInput[splitterMapInputLst.size()];
							spMapInArr = splitterMapInputLst.toArray(spMapInArr);
							bpsMap.setMap_inputs(spMapInArr);
						}

						// Get the SplitterMapOutput[] from SplitterMap from the Cdump file
						if (null != splprops.getSplitter_map().getMap_outputs() && splprops.getSplitter_map().getMap_outputs().length != 0) {
							SplitterMapOutput[] spMapOut = splprops.getSplitter_map().getMap_outputs();
							ArrayList<SplitterMapOutput> smoLst = new ArrayList<SplitterMapOutput>(
									Arrays.asList(spMapOut));

							// Iterate over SplitterMapOutput of Cdump File
							for (SplitterMapOutput smo : smoLst) {

								SplitterOutputField spOutField = new SplitterOutputField();
								SplitterMapOutput spMapOutput = new SplitterMapOutput();

								spOutField.setParameter_name(smo.getOutput_field().getParameter_name());
								spOutField.setParameter_tag(smo.getOutput_field().getParameter_tag());
								spOutField.setParameter_type(smo.getOutput_field().getParameter_type());
								spOutField.setParameter_role(smo.getOutput_field().getParameter_role());
								spOutField.setTarget_name(smo.getOutput_field().getTarget_name());
								spOutField.setMessage_signature(smo.getOutput_field().getMessage_signature());
								spOutField.setError_indicator(smo.getOutput_field().getError_indicator());
								spOutField.setMapped_to_field(smo.getOutput_field().getMapped_to_field());

								spMapOutput.setOutput_field(spOutField);
								splitterMapOutputLst.add(spMapOutput);
							}
							// Convert the SplitterMapOutput List to SplitterMapOutput[]
							SplitterMapOutput[] spMapOutArr = new SplitterMapOutput[splitterMapOutputLst.size()];
							spMapOutArr = splitterMapOutputLst.toArray(spMapOutArr);
							bpsMap.setMap_outputs(spMapOutArr);
						}
					}
				}
			}
		}
		return bpsMap;
	}

	private BPDataBrokerMap getDataBrokerDetails(Nodes n) {
		Property[] prop = n.getProperties();
		// Convert the Property[] into List
		ArrayList<Property> propslst = new ArrayList<Property>(Arrays.asList(prop));
		String script = null;
		String data_broker_Type = null;
		String target_system_Url = null;
		String local_system_data_file_Path = null;
		String first_Row = null;
		String csv_file_field_Separator = null;

		// BluePrint DataBrokerMap object
		BPDataBrokerMap bpdbMap = new BPDataBrokerMap();
		List<DBMapInput> dataBrokerMapInputLst = new ArrayList<DBMapInput>();
		List<DBMapOutput> dataBrokerMapOutputLst = new ArrayList<DBMapOutput>();

		// Iterate over the PropertyList of the Node from Cdump
		if (null != propslst) {
			for (Property dbprops : propslst) {
				if (null != dbprops.getData_broker_map()) {
					// Set all the values from Property List of cdump to Blueprint DataBrokerMap object
					script = dbprops.getData_broker_map().getScript();
					data_broker_Type = dbprops.getData_broker_map().getData_broker_type();
					target_system_Url = dbprops.getData_broker_map().getTarget_system_url();
					local_system_data_file_Path = dbprops.getData_broker_map().getLocal_system_data_file_path();
					first_Row = dbprops.getData_broker_map().getFirst_row();
					csv_file_field_Separator = dbprops.getData_broker_map().getCsv_file_field_separator();

					bpdbMap.setData_broker_type(data_broker_Type);
					bpdbMap.setScript(script);
					bpdbMap.setTarget_system_url(target_system_Url);
					bpdbMap.setLocal_system_data_file_path(local_system_data_file_Path);
					bpdbMap.setFirst_row(first_Row);
					bpdbMap.setCsv_file_field_separator(csv_file_field_Separator);

					// Get the DBMapInput[] from DataBrokerMap from the Cdump file
					if (null != dbprops.getData_broker_map().getMap_inputs()) {
						DBMapInput[] dmapIn = dbprops.getData_broker_map().getMap_inputs();
						// Convert MapInputs[] to List
						ArrayList<DBMapInput> dbmiLst = new ArrayList<DBMapInput>(Arrays.asList(dmapIn));
						// Iterate over the DBMapInput List of the Cdump file
						for (DBMapInput db : dbmiLst) {
							DBInputField dbInField = new DBInputField();
							DBMapInput dbMapInput = new DBMapInput();
							// set the all values into DataBroker Input Field of BluePrint File
							dbInField.setName(db.getInput_field().getName());
							dbInField.setType(db.getInput_field().getType());
							dbInField.setChecked(db.getInput_field().getChecked());
							dbInField.setMapped_to_field(db.getInput_field().getMapped_to_field());
							dbMapInput.setInput_field(dbInField);
							dataBrokerMapInputLst.add(dbMapInput);
						}
						// Convert the DataBrokerMapInput Lsit to DBMapInput[]
						DBMapInput[] dbMapInArr = new DBMapInput[dataBrokerMapInputLst.size()];
						dbMapInArr = dataBrokerMapInputLst.toArray(dbMapInArr);
						bpdbMap.setMap_inputs(dbMapInArr);
					}

					// Get the DBMapOutput[] from DataBrokerMap from the Cdump file
					if (null != dbprops.getData_broker_map().getMap_outputs()) {
						DBMapOutput[] dbMapOutArr = dbprops.getData_broker_map().getMap_outputs();
						ArrayList<DBMapOutput> dbmoLst = new ArrayList<DBMapOutput>(Arrays.asList(dbMapOutArr));

						DBOTypeAndRoleHierarchy[] dboTypeAndRoleHierarchyArr = null;

						// Iterate over DBMapOutput List of Cdump File
						for (DBMapOutput dbOut : dbmoLst) {
							// Set DBMapOutput values of Cdump into DBOutputField values of BluePrint File
							DBMapOutput dbMapOutput = new DBMapOutput();
							DBOutputField dbOutField = new DBOutputField();
							dbOutField.setName(dbOut.getOutput_field().getName());
							dbOutField.setTag(dbOut.getOutput_field().getTag());

							List<DBOTypeAndRoleHierarchy> dboList = new ArrayList<DBOTypeAndRoleHierarchy>();
							// Iterate over DBOTypeAndRoleHierarchy List of Cdump File
							for (DBOTypeAndRoleHierarchy dboTypeAndRoleHierarchy : dbOut.getOutput_field()
									.getType_and_role_hierarchy_list()) {

								DBOTypeAndRoleHierarchy dboTypeAndRole = new DBOTypeAndRoleHierarchy();
								dboTypeAndRole.setName(dboTypeAndRoleHierarchy.getName());
								dboTypeAndRole.setRole(dboTypeAndRoleHierarchy.getRole());
								dboList.add(dboTypeAndRole);
							}
							dboTypeAndRoleHierarchyArr = new DBOTypeAndRoleHierarchy[dboList.size()];
							dboTypeAndRoleHierarchyArr = dboList.toArray(dboTypeAndRoleHierarchyArr);
							dbOutField.setType_and_role_hierarchy_list(dboTypeAndRoleHierarchyArr);

							dbMapOutput.setOutput_field(dbOutField);
							dataBrokerMapOutputLst.add(dbMapOutput);
						}

						// Convert DBMapOutPutList to DBMapOutput[]
						DBMapOutput[] dbMapOutputArr = new DBMapOutput[dataBrokerMapOutputLst.size()];
						dbMapOutputArr = dataBrokerMapOutputLst.toArray(dbMapOutputArr);
						bpdbMap.setMap_outputs(dbMapOutputArr);
					}
				}
			}
		}
		return bpdbMap;
	}

	/**
	 * @param requirements
	 * @param nodeOperationName
	 * @return
	 */
	private String getOutputMessage(Requirements[] requirements, String nodeOperationName) {
		String result = null;
		List<Requirements> requirementLst = Arrays.asList(requirements);
		if(null != nodeOperationName && nodeOperationName.trim() != ""){
			ReqCapability capability = null; 
			for(Requirements r : requirementLst) {
				capability = r.getCapability();
				if(capability.getId().equals(nodeOperationName)){
					result = capability.getName()[0].getMessageName(); 
					//NodeOperationSignature output_message_name should have been array, as operation can have multiple output messages.  
					//Its seems to be some gap
					
				}
			}
		}
		
		return result;
	}

	/**
	 * 
	 * @param requirements
	 * @param nodeName
	 * @return
	 */
	private String getConnectedPort(List<Relations> requirements, String nodeId){
		String result = null;
		if(null != nodeId && nodeId.trim() != ""){
			for(Relations r : requirements) {
				if(nodeId.equals(r.getSourceNodeId())){
					result = r.getSourceNodeRequirement().replace("+", OPERATION_EXTRACTOR);
					result = result.split(OPERATION_EXTRACTOR)[0];
					break;
				} else if(nodeId.equals(r.getTargetNodeId())){
					result = r.getTargetNodeCapability().replace("+", OPERATION_EXTRACTOR);
					result = result.split(OPERATION_EXTRACTOR)[0];
					break;
				}
			}
		}
		return result;
	}
	/**
	 * @param nodeSolutionId
	 * @param dockerImageURL
	 * @param mlpSolRevision
	 * @return
	 */
	private String getDockerImageURL(String nodeSolutionId, MLPSolutionRevision mlpSolRevision) {
		List<MLPArtifact> mlpArtifacts;
		mlpArtifacts = cdmsClient.getSolutionRevisionArtifacts(nodeSolutionId, mlpSolRevision.getRevisionId());
		// Iterate over the MLPArtifacts and get the DockerImage ArticactCode as
		// DI and get the DockerImage URL
		String dockerImageURL = "";
		for (MLPArtifact a : mlpArtifacts) {
			if ("DI".equals(a.getArtifactTypeCode())) {
				dockerImageURL = a.getUri();
				break;
			}
		}
		return dockerImageURL;
	}

	/**
	 * @param nodeSolutionId
	 * @param nodeVersion
	 * @param mlpSolRevision
	 * @return
	 */
	private MLPSolutionRevision getSolutionRevisions(String nodeSolutionId, String nodeVersion,
			MLPSolutionRevision mlpSolRevision) {
		List<MLPSolutionRevision> mlpSolRevisions;
		mlpSolRevisions = cdmsClient.getSolutionRevisions(nodeSolutionId);
		// Iterate over the MlpSolutionRevisions and get the SolutionRevision
		for (MLPSolutionRevision solRev : mlpSolRevisions) {
			if (solRev.getVersion().equals(nodeVersion)) {
				mlpSolRevision = solRev;
				break;
			}
		}
		return mlpSolRevision;
	}

	/**
	 * @param cdump
	 * @param nodeId
	 * @return
	 */
	private List<Container> getRelations(Cdump cdump, String nodeId) {
		List<Container> connectedToList;
		Container connectedTo;
		connectedToList = new ArrayList<>();
		// Get the Relations from the Cdump File
		List<Relations> cRelations = cdump.getRelations();
		String operation = null;
		BaseOperationSignature bos;

		// Get the Relations with sourceNodeId as node id
		for (Relations cr : cRelations) {
			if (cr.getSourceNodeId().equals(nodeId)) {
				// Get the targeNodeName and set it to depends_on object
				connectedTo = new Container();
				connectedTo.setContainer_name(cr.getTargetNodeName());
				bos = new BaseOperationSignature();
				operation = cr.getTargetNodeCapability().replace("+", OPERATION_EXTRACTOR);
				operation = operation.split(OPERATION_EXTRACTOR)[0];
				bos.setOperation_name(operation);
				connectedTo.setOperation_signature(bos);
				connectedToList.add(connectedTo);
			}
		}
		return connectedToList;
	}
	@Override
	public SuccessErrorMessage setProbeIndicator(String userId, String solutionId, String version, String cid,
			String probeIndicator) {
		logger.debug(EELFLoggerDelegator.debugLogger, " setProbeIndicator() : Begin ");
        String id = "";
		Gson gson = new Gson();
		ObjectMapper mapper = new ObjectMapper();
		try {
            if (null != cid && null == solutionId) {
				id = cid;
			} else if (null == cid) {
				id = solutionId;
			}
			String path = DSUtil.readCdumpPath(userId, confprops.getToscaOutputFolder());
			String cdumpFileName = "acumos-cdump" + "-" + id + ".json";
			Cdump cdump = mapper.readValue(new File(path.concat(cdumpFileName)), Cdump.class);
			cdump.setProbeIndicator(probeIndicator);
            try {
				String jsonInString = gson.toJson(cdump);
				DSUtil.writeDataToFile(path, "acumos-cdump" + "-" + id, "json", jsonInString);
				successErrorMessage = getResponseMessageStatus(probeIndicator,"");
			}catch (JsonIOException e) {
				successErrorMessage = getResponseMessageStatus("false","There is some issue to set prob indicator value,please check the log1 file");
				logger.error(EELFLoggerDelegator.errorLogger, "Exception in setProbeIndicator() ", e);
			}
		} catch (Exception e) {
			successErrorMessage = getResponseMessageStatus("false","There is some issue to set prob indicator value,please check the log file");
			logger.error(EELFLoggerDelegator.errorLogger, " Exception Occured in setProbeIndicator() ", e);
		}
        logger.debug(EELFLoggerDelegator.debugLogger, " setProbeIndicator() : End ");
		return successErrorMessage;
	}
		
	private SuccessErrorMessage getResponseMessageStatus(String messagestatus, String messagedescription) {
		return new SuccessErrorMessage(messagestatus, messagedescription);
	}
	 
	private Nodes getNodeForId(List<Nodes> nodes, String nodeId){
		Nodes node = null;
		for(Nodes n : nodes){
			if(n.getNodeId().equals(nodeId)){
				node = n;
				break;
			}
		}
		return node;
	}

	private List<String> getNodesForPosition(Cdump cdump, String position) {
		List<String> nodeNames = new ArrayList<String>();
		Set<String> sourceNodeId = new HashSet<>();
		Set<String> targetNodeId = new HashSet<>();
		List<Relations> relationsList = cdump.getRelations();
		List<Nodes> nodes = cdump.getNodes();
		Nodes node = null;
		for (Relations rlns : relationsList) {
			sourceNodeId.add(rlns.getSourceNodeId());
			targetNodeId.add(rlns.getTargetNodeId());
		}

		if (position.equals(FIRST_NODE_POSITION)) {
			sourceNodeId.removeAll(targetNodeId);
			for (String nodeId : sourceNodeId) {
				node = getNodeForId(nodes, nodeId);
				nodeNames.add(node.getName());
			}
		} else if(position.equals(LAST_NODE_POSITION)){
			targetNodeId.removeAll(sourceNodeId);
			for (String nodeId : targetNodeId) {
				node = getNodeForId(nodes, nodeId);
				nodeNames.add(node.getName());
			}
		}
		return nodeNames;
	}

	private String getNodeIdForPosition(Cdump cdump, String position) {
		String nodeId = null;
		Set<String> sourceNodeId = new HashSet<>();
		Set<String> targetNodeId = new HashSet<>();
		List<Relations> relationsList = cdump.getRelations();
		for (Relations rlns : relationsList) {
			sourceNodeId.add(rlns.getSourceNodeId());
			targetNodeId.add(rlns.getTargetNodeId());
		}
		if (position.equals(FIRST_NODE_POSITION)) {
			sourceNodeId.removeAll(targetNodeId);
			nodeId = (sourceNodeId.iterator().hasNext() ? sourceNodeId.iterator().next() : nodeId);
		} else if(position.equals(LAST_NODE_POSITION)){
			targetNodeId.removeAll(sourceNodeId);
			nodeId = (targetNodeId.iterator().hasNext() ? targetNodeId.iterator().next() : nodeId);
		}
		return nodeId;
	}

	public void getRestCCDSClient(CommonDataServiceRestClientImpl commonDataServiceRestClient) {
		cdmsClient = commonDataServiceRestClient;
	}

	public void getNexusClient(NexusArtifactClient nexusArtifactClient1, ConfigurationProperties confprops1,
			org.acumos.designstudio.ce.util.Properties properties) {
		confprops = confprops1;
		props = properties;
		nexusArtifactClient = nexusArtifactClient1;
	}
	
	public void setGenericDataMapperServiceImpl(GenericDataMapperServiceImpl gdmService){
		this.gdmService = gdmService;
	}
	
	private boolean storeCompositeSolutionMembers(Cdump cdump) throws ServiceException {

		boolean flag = false;
		try {
			List<Nodes> nodes = cdump.getNodes();
			List<String> parentChildList = cdmsClient.getCompositeSolutionMembers(cdump.getSolutionId());
			if (!parentChildList.isEmpty() & parentChildList != null) {
				for (String childNode : parentChildList) {
					cdmsClient.dropCompositeSolutionMember(cdump.getSolutionId(), childNode);
				}
			}

			if (!nodes.isEmpty() & nodes != null) {
				for (Nodes node : nodes) {
					cdmsClient.addCompositeSolutionMember(cdump.getSolutionId(), node.getNodeSolutionId());
				}
				flag = true;
			}
		} catch (Exception e) {
			flag = false;
			logger.error(EELFLoggerDelegator.errorLogger, " Exception Occured in storeCompositeSolutionMembers() ", e);
			throw new ServiceException(" Exception in updateCompositeSolution() ", "333","Failed to add CompositeSolution Member");
		}
		return flag;
	}
	
	private void deleteMember(String solutionId) throws ServiceException {
		
		try {
		List<String> parentChildList = cdmsClient.getCompositeSolutionMembers(solutionId);
			if(!parentChildList.isEmpty() & parentChildList != null){
				for(String childNode: parentChildList){
					cdmsClient.dropCompositeSolutionMember(solutionId, childNode);
				}
			}
			
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, " Exception Occured in deleteMember() ", e);
			throw new ServiceException(" Exception in updateCompositeSolution() ", "333","Failed to drop CompositeSolution Member");
		}		
		
	}
	
}
