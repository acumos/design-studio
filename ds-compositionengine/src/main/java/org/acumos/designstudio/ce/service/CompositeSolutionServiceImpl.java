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
import org.acumos.designstudio.cdump.Capabilities;
import org.acumos.designstudio.cdump.Cdump;
import org.acumos.designstudio.cdump.DataMap;
import org.acumos.designstudio.cdump.Nodes;
import org.acumos.designstudio.cdump.Property;
import org.acumos.designstudio.cdump.Relations;
import org.acumos.designstudio.cdump.ReqCapability;
import org.acumos.designstudio.cdump.Requirements;
import org.acumos.designstudio.ce.exceptionhandler.AcumosException;
import org.acumos.designstudio.ce.exceptionhandler.ServiceException;
import org.acumos.designstudio.ce.util.ConfigurationProperties;
import org.acumos.designstudio.ce.util.DSUtil;
import org.acumos.designstudio.ce.util.EELFLoggerDelegator;
import org.acumos.designstudio.ce.util.Properties;
import org.acumos.designstudio.ce.vo.Artifact;
import org.acumos.designstudio.ce.vo.DSCompositeSolution;
import org.acumos.designstudio.ce.vo.DSSolution;
import org.acumos.designstudio.ce.vo.blueprint.BaseOperationSignature;
import org.acumos.designstudio.ce.vo.blueprint.BluePrint;
import org.acumos.designstudio.ce.vo.blueprint.Container;
import org.acumos.designstudio.ce.vo.blueprint.Node;
import org.acumos.designstudio.ce.vo.blueprint.NodeOperationSignature;
import org.acumos.designstudio.ce.vo.blueprint.OperationSignatureList;
import org.acumos.designstudio.ce.vo.blueprint.ProbeIndicator;
import org.acumos.nexus.client.NexusArtifactClient;
import org.acumos.nexus.client.data.UploadArtifactInfo;
import org.apache.commons.collections.CollectionUtils;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;

/**
 * 
 *
 *
 */
@Service("compositeServiceImpl")
public class CompositeSolutionServiceImpl implements ICompositeSolutionService {

	private static EELFLoggerDelegator logger = EELFLoggerDelegator.getLogger(CompositeSolutionServiceImpl.class);

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

	@Override
	public String saveCompositeSolution(DSCompositeSolution dscs) throws AcumosException {

		String result = "";
		String error = "{\"errorCode\" : \"%s\", \"errorDescription\" : \"%s\"}";
		logger.debug(EELFLoggerDelegator.debugLogger, "------- saveCompositeSolution() Begin --------");
		// Case 1. New Composite Solution : CID exist and SolutionID is missing.
		if (null != dscs.getcId() && null == dscs.getSolutionId()) {
			logger.debug(EELFLoggerDelegator.debugLogger,
					"Started implementation of Case 1 : cid is present and SolutionId is null");
			String cdumpFileName = "acumos-cdump" + "-" + dscs.getcId() + ".json";
			String filePath = DSUtil.readCdumpPath(dscs.getAuthor(), confprops.getToscaOutputFolder());

			File cdump = new File(filePath.concat(cdumpFileName));
			// 3. If cid is present then validate the cid : By checking
			// whether the corresponding json file existing or not.
			if (cdump.exists()) {

				// check if the solution name and its version already exists using CDS
				Boolean solutionsExists = false;
				Map<String, Object> queryParameters = new HashMap<>();
				queryParameters.put("name", dscs.getSolutionName());
				//Code changes are to match the change in the CDS API Definition searchSolution in version 1.13.x
				RestPageResponse<MLPSolution> pageResponse = cdmsClient.searchSolutions(queryParameters, false, new RestPageRequest(0,props.getSolutionResultsetSize()));
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
						logger.error(EELFLoggerDelegator.errorLogger, "--- IOException in insertCompositeSolution ---",
								e);
					}
				}
			} else {
				result = String.format(error, "201", "Unknown Composition Id " + dscs.getcId());
			}
			logger.debug(EELFLoggerDelegator.debugLogger,
					"Ended implementation of Case 1 : cid is present and SolutionId is null");
		} else if (null == dscs.getcId() && null != dscs.getSolutionId()) {
			logger.debug(EELFLoggerDelegator.debugLogger,
					"Started implementation of Case 2 : cid is null and SolutionId is present");
			try {
				result = updateCompositeSolution(dscs);
			} catch (IOException e) {
				logger.error(EELFLoggerDelegator.errorLogger, "--- Exception in updateCompositeSolution ----", e);
			}
			logger.debug(EELFLoggerDelegator.debugLogger,
					"Ended implementation of Case 2 : cid is null and SolutionId is present");
		} else {
			result = String.format(error, "201", "Error while saving the solution");
		}

		// 4. (Future) The Composition Engine must call the Modeling Engine
		// to ensure the TOSCA validation of cdump file.
		logger.debug(EELFLoggerDelegator.debugLogger, "------- saveCompositeSolution() End --------");
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

		logger.debug(EELFLoggerDelegator.debugLogger, "------- insertCompositeSolution() Begin --------");
		MLPSolution mlpSolution = new MLPSolution();
		try {

			mlpSolution = new MLPSolution();
			mlpSolution.setName(dscs.getSolutionName());
			mlpSolution.setDescription(dscs.getDescription());
			mlpSolution.setOwnerId(dscs.getAuthor());
			mlpSolution.setValidationStatusCode(ValidationStatusCode.IP.toString());
			mlpSolution.setProvider(dscs.getProvider());
			mlpSolution.setAccessTypeCode(AccessTypeCode.PR.toString());
			mlpSolution.setModelTypeCode(ModelTypeCode.PR.toString());
			mlpSolution.setToolkitTypeCode("CP");
			// mlpSolution.
			mlpSolution.setActive(true);
			mlpSolution = cdmsClient.createSolution(mlpSolution);
			logger.debug(EELFLoggerDelegator.debugLogger, "-------1.  Successfully Created the Solution "
					+ mlpSolution.getName() + " & generated Solution ID : " + mlpSolution.getSolutionId());
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger,
					"---------Error :  Exception in insertCompositeSolution() Failed to create the Solution ----------",
					e);
			throw new ServiceException("---  Exception in insertCompositeSolution ----", "222",
					"Failed to create the Solution");
		}

		// 2. create the solutionRevision
		MLPSolutionRevision mlpSolutionRevision = new MLPSolutionRevision();
		try {
			mlpSolutionRevision.setSolutionId(mlpSolution.getSolutionId());
			mlpSolutionRevision.setDescription(dscs.getDescription());
			mlpSolutionRevision.setOwnerId(dscs.getAuthor());
			mlpSolutionRevision.setVersion(dscs.getVersion());

			mlpSolutionRevision = cdmsClient.createSolutionRevision(mlpSolutionRevision);

			logger.debug(EELFLoggerDelegator.debugLogger,
					"-------2. Successfully Created the SolutionRevision : " + mlpSolutionRevision.getRevisionId());
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger,
					"---------Error : Exception in insertCompositeSolution() : Failed to create the Solution Revision----------",
					e);
			throw new ServiceException("---  Exception in insertCompositeSolution() ----", "222",
					"Failed to create the Solution");
		}

		String path = DSUtil.readCdumpPath(dscs.getAuthor(), confprops.getToscaOutputFolder());
		String cdumpFileName = "acumos-cdump" + "-" + dscs.getcId();
		String payload = "";

		try {
			ObjectMapper mapper = new ObjectMapper();
			Cdump cdump = mapper.readValue(new File(path.concat(cdumpFileName).concat(".json")), Cdump.class);
			if (null == cdump) {
				logger.debug(EELFLoggerDelegator.debugLogger,
						"-------Error : Cdump file not found for Solution ID :  " + mlpSolution.getSolutionId());
			} else {
				cdump.setCname(dscs.getSolutionName());
				cdump.setVersion(dscs.getVersion());
				cdump.setSolutionId(mlpSolution.getSolutionId());
				SimpleDateFormat sdf = new SimpleDateFormat(confprops.getDateFormat());
				cdump.setMtime(sdf.format(new Date()));
				logger.debug(EELFLoggerDelegator.debugLogger,
						"-------3. Successfully read the Cdump file for solution ID : " + mlpSolution.getSolutionId());
				Gson gson = new Gson();
				payload = gson.toJson(cdump);
				cdumpFileName = "acumos-cdump" + "-" + mlpSolution.getSolutionId();
				DSUtil.writeDataToFile(path, cdumpFileName, "json", payload);
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger,
					"---------Error : Exception in insertCompositeSolution() : Failed to Find the Cdump File ----------",
					e);
			throw new ServiceException("---  Exception in insertCompositeSolution() ----", "222",
					"Failed to create the Solution");
		}

		Artifact cdumpArtifact = new Artifact(cdumpFileName, "json", mlpSolution.getSolutionId(), dscs.getVersion(),
				path, payload.length());

		logger.debug(EELFLoggerDelegator.debugLogger,
				"-------4. Successfully updated the Cdump file for solution ID : " + mlpSolution.getSolutionId());

		try {
			uploadFilesToRepository(mlpSolution.getSolutionId(), dscs.getVersion(), cdumpArtifact);
			dscs.setCdump(cdumpArtifact);
			logger.debug(EELFLoggerDelegator.debugLogger,
					"-------5. Successfully uploaded the Cdump file for solution ID : " + mlpSolution.getSolutionId());
			DSUtil.deleteFile(path.concat("acumos-cdump" + "-" + dscs.getcId()).concat(".json"));
			logger.debug(EELFLoggerDelegator.debugLogger,
					"-------5.1 Successfully deleted the local Cdump file for solution ID : "
							+ mlpSolution.getSolutionId());

		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger,
					"---------Error : Exception in insertCompositeSolution() : Failed to upload the Cdump File to Nexus ----------",
					e);
			DSUtil.deleteFile(path.concat("acumos-cdump" + "-" + dscs.getcId()).concat(".json"));
			if (null != mlpSolution.getSolutionId())
				DSUtil.deleteFile(path.concat(cdumpFileName).concat(".json"));
			logger.debug(EELFLoggerDelegator.debugLogger,
					"-------5.1 Successfully deleted the local Cdump file for solution ID : "
							+ mlpSolution.getSolutionId());
			throw new ServiceException("---  Exception in insertCompositeSolution() ----", "222",
					"Failed to create the Solution");
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
			mlpArtifact.setOwnerId(dscs.getAuthor());
			mlpArtifact.setVersion(cdumpArtifact.getVersion());
			mlpArtifact.setSize(cdumpArtifact.getContentLength());

			mlpArtifact = cdmsClient.createArtifact(mlpArtifact);

			// 5. associate articat to the solutionRevisionArtifact.
			logger.debug(EELFLoggerDelegator.debugLogger,
					"-------6. Successfully created the artifact for the cdumpfile for the solution : "
							+ mlpSolution.getSolutionId() + " artifact ID : " + mlpArtifact.getArtifactId());

			cdmsClient.addSolutionRevisionArtifact(mlpSolution.getSolutionId(), mlpSolutionRevision.getRevisionId(),
					mlpArtifact.getArtifactId());

			logger.debug(EELFLoggerDelegator.debugLogger,
					"-------7. Successfully associated the Solution Revision Artifact for solution ID  : "
							+ mlpSolution.getSolutionId());
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger,
					"---------Error : Exception in insertCompositeSolution() : Failed to create the Solution Artifact ----------",
					e);
			throw new ServiceException("---  Exception in insertCompositeSolution() ----", "222",
					"Failed to create the Solution");
		}

		// 5. Detete the cdump file

		logger.debug(EELFLoggerDelegator.debugLogger, "------- insertCompositeSolution() End --------");

		return "{\"solutionId\": \"" + mlpSolution.getSolutionId() + "\", \"version\" : \"" + dscs.getVersion()
				+ "\" }";

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
		logger.debug(EELFLoggerDelegator.debugLogger, "------- updateCompositeSolution() Begin --------");

		String result = null;
		String error = "{\"errorCode\" : \"%s\", \"errorDescription\" : \"%s\"}";
		// 1. get the solution details using CDS : MLPSolution
		MLPSolution mlpSolution = cdmsClient.getSolution(dscs.getSolutionId());

		if (null != mlpSolution) {

			// 2. check the solution name with input solution name
			if (mlpSolution.getName().equalsIgnoreCase(dscs.getSolutionName())) {

				// 3. get the list of solutionRevision details using CDS :
				// MLPSolutionRevison sorted in descending order.
				List<MLPSolutionRevision> mlpSolutionList = cdmsClient.getSolutionRevisions(dscs.getSolutionId());

				Collections.sort(mlpSolutionList, new Comparator<MLPSolutionRevision>() {
					@Override
					public int compare(MLPSolutionRevision s1, MLPSolutionRevision s2) {
						return s2.getCreated().compareTo(s1.getCreated());
					}
				});

				if (null != mlpSolutionList && !mlpSolutionList.isEmpty()) {
					// 4. get the first solutionRevision and check the solution version with input
					// solution version
					if (mlpSolutionList.get(0).getVersion().equals(dscs.getVersion())) {
						logger.debug(EELFLoggerDelegator.debugLogger, "Upadating Existing Solution");
						result = updateExistingSolution(mlpSolutionList.get(0), mlpSolution, dscs);

					} else {
						boolean previousFlag = false;
						int cnt = 0;// To skip the first solutionRevision
						for (MLPSolutionRevision mlpSR : mlpSolutionList) {
							if (cnt > 0) {
								if (mlpSR.getVersion().equals(dscs.getVersion())) {
									previousFlag = true;

									if (!dscs.getIgnoreLesserVersionConflictFlag()) {

										result = "{\"alert\": \"" + props.getAskToUpdateExistingCompSolnMsg() + "\" }";
									} else if (dscs.getIgnoreLesserVersionConflictFlag()) {
										logger.debug(EELFLoggerDelegator.debugLogger,
												"Upadating Existing Solution with the previous version");
										result = updateExistingSolution(mlpSR, mlpSolution, dscs);
									}
								}
							} else {
								cnt++;
							}
						}
						if (!previousFlag) {
							logger.debug(EELFLoggerDelegator.debugLogger, "Upadating Solution with the new version");
							result = updateSolnWithNewVersion(mlpSolution, dscs);
						}
					}
				} else { // This means no version available for the solution as result for delete
							// solution. So add new version to the existing solution.

					logger.debug(EELFLoggerDelegator.debugLogger,
							"No version found, so adding new version to the Solution");
					result = updateSolnWithNewVersion(mlpSolution, dscs);
				}
			} else {
				// New Case: When user tries to update the existting solution with a different
				// name
				// Update the dscs with the new values
				dscs.setcId(dscs.getSolutionId());
				dscs.setSolutionId(null);
				// call the save method to get the normal flow saving a solution
				result = saveCompositeSolution(dscs);

			}
		} else {

			result = String.format(error, "207", "Solution does not exist in the Database");
		}
		logger.debug(EELFLoggerDelegator.debugLogger, "------- updateCompositeSolution() End --------");
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
	public String updateExistingSolution(MLPSolutionRevision mlpSR, MLPSolution mlpSolution, DSCompositeSolution dscs)
			throws IOException, AcumosException {
		logger.debug(EELFLoggerDelegator.debugLogger, "------- updateExistingSolution() Start --------");
		String result = "";
		Date currentDate = new Date();

		// 5.1 read the cdump file from the outputfolder
		String path = DSUtil.readCdumpPath(dscs.getAuthor(), confprops.getToscaOutputFolder());

		// Changed in current implementation Please check while merging
		String cdumpFileName = "acumos-cdump" + "-" + mlpSolution.getSolutionId();
		ObjectMapper mapper = new ObjectMapper();
		Cdump cdump = mapper.readValue(new File(path.concat(cdumpFileName).concat(".json")), Cdump.class);

		if (null == cdump) {
			logger.debug(EELFLoggerDelegator.debugLogger,
					"-------Error : Cdump file not found for Solution ID :  " + mlpSolution.getSolutionId());
		} else {
			// 5.2 Update the cdump file with mtime
			cdump.setMtime(new SimpleDateFormat(confprops.getDateFormat()).format(currentDate));
			Gson gson = new Gson();
			String payload = gson.toJson(cdump);
			DSUtil.writeDataToFile(path, cdumpFileName, "json", payload);
			Artifact cdumpArtifact = new Artifact(cdumpFileName, "json", mlpSolution.getSolutionId(), dscs.getVersion(),
					path, payload.length());
			logger.debug(EELFLoggerDelegator.debugLogger,
					"-------4. Successfully updated the Cdump file for solution ID : " + mlpSolution.getSolutionId());
			// 5.3 upload the cdump file in Nexus Repositry. (file name should be the same
			// as previous one).
			uploadFilesToRepository(mlpSolution.getSolutionId(), dscs.getVersion(), cdumpArtifact);

			// Fetch the existing artifact
			List<MLPArtifact> artfactsList = cdmsClient.getSolutionRevisionArtifacts(mlpSR.getSolutionId(),
					mlpSR.getRevisionId());

			for (MLPArtifact mlpArtifact : artfactsList) {

				logger.debug(EELFLoggerDelegator.debugLogger,
						"mlpArtifact.getArtifactTypeCode()  : " + mlpArtifact.getArtifactTypeCode());
				if (mlpArtifact.getArtifactTypeCode().equals(props.getArtifactTypeCode())) {
					logger.debug(EELFLoggerDelegator.debugLogger,
							mlpArtifact.getArtifactTypeCode() + "=" + props.getArtifactTypeCode());
					dscs.setCdump(cdumpArtifact);
					mlpArtifact.setUri(cdumpArtifact.getNexusURI());
					mlpArtifact.setModified(currentDate);
					mlpArtifact.setSize(cdumpArtifact.getContentLength());
					cdmsClient.updateArtifact(mlpArtifact);
					logger.debug(EELFLoggerDelegator.debugLogger,
							"------- Successfully updated the artifact for the cdumpfile for the solution : "
									+ mlpSolution.getSolutionId() + " artifact ID : " + mlpArtifact.getArtifactId());
					result = "{\"solutionId\": \"" + mlpSolution.getSolutionId() + "\", \"version\" : \""
							+ dscs.getVersion() + "\" }";
					break;
				}
			}
			// 5.4 update the solutionRevisoin (i.e., to update the modified date of the
			// solutionrevision)
			mlpSR.setModified(currentDate);
			cdmsClient.updateSolutionRevision(mlpSR);

			// 5.5 Update the solution (i.e., to update the modified date of the solution).
			mlpSolution.setModified(currentDate);
			cdmsClient.updateSolution(mlpSolution);
		}
		logger.debug(EELFLoggerDelegator.debugLogger, "------- updateExistingSolution() End --------");
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
	public String updateSolnWithNewVersion(MLPSolution mlpSolution, DSCompositeSolution dscs)
			throws IOException, AcumosException {

		// Set solution active to true
		mlpSolution.setActive(true);
		cdmsClient.updateSolution(mlpSolution);

		// 6. Case 3 - update the solution with new version
		logger.debug(EELFLoggerDelegator.debugLogger, "------- updateSolnWithNewVersion() Start --------");
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

		// 6.2 Create new SolutionRevision using CDS : inputSolutionId, version : this
		// will return the solutionrevisionid
		MLPSolutionRevision mlpSolutionRevision = new MLPSolutionRevision();
		try {
			mlpSolutionRevision.setSolutionId(mlpSolution.getSolutionId());
			mlpSolutionRevision.setDescription(dscs.getDescription());
			mlpSolutionRevision.setOwnerId(dscs.getAuthor());
			mlpSolutionRevision.setVersion(dscs.getVersion());
			// Get the latest date in to variable and then use it.
			mlpSolutionRevision.setModified(currentDate);
			mlpSolutionRevision = cdmsClient.createSolutionRevision(mlpSolutionRevision);

			logger.debug(EELFLoggerDelegator.debugLogger,
					"------- Successfully Created the SolutionRevision : " + mlpSolutionRevision.getRevisionId());
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger,
					"---------Error : Exception in updateSolnWithNewVersion() : Failed to create the Solution Revision----------",
					e);
			throw new ServiceException("---  Exception in updateSolnWithNewVersion() ----", "222",
					"Failed to create the Solution");
		}

		Gson gson = new Gson();
		String payload = gson.toJson(cdump);

		DSUtil.writeDataToFile(path, cdumpFileName, "json", payload);
		Artifact cdumpArtifact = new Artifact(cdumpFileName, "json", mlpSolution.getSolutionId(), dscs.getVersion(),
				path, payload.length());
		logger.debug(EELFLoggerDelegator.debugLogger,
				"------- Successfully updated the Cdump file for solution ID : " + mlpSolution.getSolutionId());
		// 5.3 upload the cdump file in Nexus Repositry. : this will return the nexus
		// URI

		try {
			uploadFilesToRepository(mlpSolution.getSolutionId(), dscs.getVersion(), cdumpArtifact);
			dscs.setCdump(cdumpArtifact);
			logger.debug(EELFLoggerDelegator.debugLogger,
					"-------5. Successfully uploaded the Cdump file for solution ID : " + mlpSolution.getSolutionId());
			logger.debug(EELFLoggerDelegator.debugLogger,
					"-------5.1 Successfully deleted the local Cdump file for solution ID : "
							+ mlpSolution.getSolutionId());

		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger,
					"---------Error : Exception in updateSolnWithNewVersion() : Failed to upload the Cdump File to Nexus ----------",
					e);
			logger.debug(EELFLoggerDelegator.debugLogger,
					"-------5.1 Successfully deleted the local Cdump file for solution ID : "
							+ mlpSolution.getSolutionId());
			throw new ServiceException("---  Exception in updateSolnWithNewVersion() ----", "222",
					"Failed to create the Solution");
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
			mlpArtifact.setOwnerId(dscs.getAuthor());
			mlpArtifact.setVersion(cdumpArtifact.getVersion());
			mlpArtifact.setSize(cdumpArtifact.getContentLength());
			// 6.4 Save the Artifact using CDS : this will return the artifactId
			mlpArtifact = cdmsClient.createArtifact(mlpArtifact);

			logger.debug(EELFLoggerDelegator.debugLogger,
					"------- Successfully created the artifact for the cdumpfile for the solution : "
							+ mlpSolution.getSolutionId() + " artifact ID : " + mlpArtifact.getArtifactId());
			// 6.5 assocaite the artifact with solutionrevision using CDS.
			cdmsClient.addSolutionRevisionArtifact(mlpSolution.getSolutionId(), mlpSolutionRevision.getRevisionId(),
					mlpArtifact.getArtifactId());

			logger.debug(EELFLoggerDelegator.debugLogger,
					"------- Successfully associated the Solution Revision Artifact for solution ID  : "
							+ mlpSolution.getSolutionId());
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger,
					"---------Error : Exception in updateSolnWithNewVersion() : Failed to create the Solution Artifact ----------",
					e);
			throw new ServiceException("---  Exception in updateSolnWithNewVersion() ----", "222",
					"Failed to create the Solution");
		}

		// 5.5 Update the solution (i.e., to update the modified date of the solution).
		mlpSolution.setModified(currentDate);
		cdmsClient.updateSolution(mlpSolution);
		result = "{\"solutionId\": \"" + mlpSolution.getSolutionId() + "\", \"version\" : \"" + dscs.getVersion()
				+ "\" }";
		logger.debug(EELFLoggerDelegator.debugLogger, "------- updateSolnWithNewVersion() End --------");
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
		logger.debug("-----------  uploadFilesToRepository() started -----------");
		FileInputStream fileInputStream = null;
		UploadArtifactInfo artifactInfo = null;
		try {

			// 1. group id ,2. artifact name, 3. version, 4. extension i.e., packaging, 5.
			// size of content, 6. actual file input stream.
			fileInputStream = new FileInputStream(a.getPayloadURI());
			artifactInfo = nexusArtifactClient.uploadArtifact(confprops.getNexusgroupid(),
					a.getSolutionID() + "_" + a.getType(), a.getVersion(), a.getExtension(), a.getContentLength(),
					fileInputStream);
			a.setNexusURI(artifactInfo.getArtifactMvnPath());

			logger.debug("--------------- uploadFilesToRepository() ended --------------");
		} catch (Exception e) {
			logger.error(" --------------- Exception Occured  uploadFilesToRepository() --------------", e);
		}

	}

	@Override
	public boolean deleteCompositeSolution(String userId, String solutionId, String version)
			throws AcumosException, JSONException {
		logger.debug(EELFLoggerDelegator.debugLogger, "------- deleteCompositeSolution() Begin --------");
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
					if (mlpSolRevision.getVersion().equals(version) && mlpSolRevision.getOwnerId().equals(userId)) {
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
										"--- Successfully Deleted the SolutionRevisionArtifact ---");
								// Delete Artifact
								// 3. Delete the artifact "CD"
								cdmsClient.deleteArtifact(artifactId);
								logger.debug(EELFLoggerDelegator.debugLogger,
										"--- Successfully Deleted the CDump Artifact ---");
								// 4. Delete the cdump file from the Nexus
								nexusArtifactClient.deleteArtifact(mlpArtifact.getUri());
							}
						}

						// 5. Delete the SolutionRevision from the DB
						cdmsClient.deleteSolutionRevision(solutionId, mlpSolRevision.getRevisionId());
						logger.debug(EELFLoggerDelegator.debugLogger,
								"--- Successfully Deleted the Solution Revision ---");
						solutionFound = true;
						result = true;
					}
				}

				if (!solutionFound) {
					result = false;

				}
			} else {
				result = false;
				logger.error(EELFLoggerDelegator.errorLogger, "Solution Not found : " + solutionId);
			}
		} catch (Exception e) {
			result = false;
			logger.error(EELFLoggerDelegator.errorLogger, "--------- Exception in deleteCompositeSolution() ----------",
					e);
			throw new ServiceException("---  Exception in deleteCompositeSolution() ----", "201",
					"Not able to delete the Solution Version");
		}
		logger.debug(EELFLoggerDelegator.debugLogger, "------- deleteCompositeSolution() End --------");
		return result;

	}

	@Override
	public String closeCompositeSolution(String userId, String solutionId, String solutionVersion, String cid) {
		logger.debug(EELFLoggerDelegator.debugLogger, "------ closeCompositeSolution() : Begin -------");
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
				logger.debug(EELFLoggerDelegator.debugLogger, "Delete file : " + path.concat(cdumpFileName));
				DSUtil.deleteFile(path.concat(cdumpFileName));
				logger.debug(EELFLoggerDelegator.debugLogger, "Delete User Dir : " + path);
				DSUtil.rmUserdir(path);
				result = String.format(resultTemplate, true, "");
			}
		} catch (Exception e) {
			result = String.format(resultTemplate, false, "Cannot Close the Composite Solution");
			logger.error(EELFLoggerDelegator.errorLogger, "---- Exception in closeCompositeSolution() -----", e);
		}
		logger.debug(EELFLoggerDelegator.debugLogger, "------ closeCompositeSolution() : End -------");
		return result;
	}

	@Override
	public String clearCompositeSolution(String userId, String solutionId, String solutionVersion, String cid) {
		logger.debug(EELFLoggerDelegator.debugLogger, "------ clearCompositeSolution() : Begin -------");
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
			logger.error(EELFLoggerDelegator.errorLogger, "------ Exception in clearCompositeSolution() ------", e);
			result = String.format(resultTemplate, false, "");
		}
		logger.debug(EELFLoggerDelegator.debugLogger, "------ clearCompositeSolution() : End -------");
		return result;
	}

	@Override
	public String getCompositeSolutions(String userID, String visibilityLevel) throws AcumosException {
		logger.debug(EELFLoggerDelegator.debugLogger, "------- getCompositeSolutions() Begin --------");
		String result = "[";

		List<MLPSolution> mlpSolutions = null;
		try {
			Map<String, Object> queryParameters = new HashMap<>();
			queryParameters.put("active", Boolean.TRUE);
			queryParameters.put("toolkitTypeCode", props.getToolKit());
			//Code changes are to match the change in the CDS API Definition searchSolution in version 1.13.x
			RestPageResponse<MLPSolution> pageResponse = cdmsClient.searchSolutions(queryParameters, false, new RestPageRequest(0,props.getSolutionResultsetSize()));
			mlpSolutions  = pageResponse.getContent();
			String solutionId = "";
			DSSolution dssolution = null;
			List<DSSolution> dsSolutions = new ArrayList<>();
			List<String> solutionIds = new ArrayList<>();
			List<MLPSolutionRevision> mlpSolRevisions = null;
			StringBuilder strBuilder = new StringBuilder();
			logger.debug(EELFLoggerDelegator.debugLogger, "------ The Date Format : " + confprops.getDateFormat());
			SimpleDateFormat sdf = new SimpleDateFormat(confprops.getDateFormat());

			if (mlpSolutions == null) {
				logger.debug(EELFLoggerDelegator.debugLogger,
						"------- CommonDataService returned null Solution list--------");
			} else if (mlpSolutions.isEmpty()) {
				logger.debug(EELFLoggerDelegator.debugLogger,
						"------- CommonDataService returned empty Solution list--------");
			} else {
				logger.debug(EELFLoggerDelegator.debugLogger,
						"------- CommonDataService returned Solution list of size : " + mlpSolutions.size()
								+ " --------");
				mlpSolRevisions = new ArrayList<>();

				for (MLPSolution mlpsol : mlpSolutions) {
					if (visibilityLevel.contains(mlpsol.getAccessTypeCode())) {
						String userId = mlpsol.getOwnerId();
						MLPUser user = cdmsClient.getUser(userId);
						if (null != mlpsol.getAccessTypeCode()
								&& (("PR".equals(mlpsol.getAccessTypeCode()) && userId.equals(userID))
										|| ("PB".equals(mlpsol.getAccessTypeCode()))
										|| ("OR".equals(mlpsol.getAccessTypeCode())))) {
							solutionId = mlpsol.getSolutionId();
							solutionIds.add(solutionId);
							mlpSolRevisions = cdmsClient.getSolutionRevisions(solutionId);
							String userName = user.getFirstName() + " " + user.getLastName();
							if (mlpSolRevisions == null) {
								logger.debug(EELFLoggerDelegator.debugLogger,
										"------- CommonDataService returned null SolutionRevision list--------");
							} else if (mlpSolRevisions.isEmpty()) {
								logger.debug(EELFLoggerDelegator.debugLogger,
										"------- CommonDataService returned empty SolutionRevision list--------");
							} else {
								logger.debug(EELFLoggerDelegator.debugLogger,
										"------- CommonDataService returned SolutionRevision list of size : "
												+ mlpSolRevisions.size() + " --------");
								for (MLPSolutionRevision mlpSolRevision : mlpSolRevisions) {
									dssolution = new DSSolution();
									dssolution.setSolutionId(mlpsol.getSolutionId());
									dssolution.setSolutionRevisionId(mlpSolRevision.getRevisionId());
									dssolution.setCreatedDate(sdf.format(mlpSolRevision.getCreated().getTime()));
									dssolution.setIcon(null);
									// 1. Solution Name
									dssolution.setSolutionName(mlpsol.getName());
									// 5. Solution Provider
									dssolution.setProvider(mlpsol.getProvider());
									// 6. Solution Tool Kit
									dssolution.setToolKit(mlpsol.getToolkitTypeCode());
									// 7. Solution Category
									dssolution.setCategory(mlpsol.getModelTypeCode());
									// 8. Solution Description
									dssolution.setDescription(mlpsol.getDescription());
									// 9. Solution Visibility
									dssolution.setVisibilityLevel(mlpsol.getAccessTypeCode());
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
			}
			if (strBuilder.length() > 1) {
				result = result + strBuilder.substring(0, strBuilder.length() - 1);
			}
			result = result + "]";

		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, "--------- Exception in getSolutions() ----------", e);
			throw new ServiceException("---  Exception in getSolutions() ----", props.getSolutionErrorCode(),
					props.getSolutionErrorDesc());
		}
		logger.debug(EELFLoggerDelegator.debugLogger, "------- getSolutions() End --------");
		return result;

	}

	@Override
	public String validateCompositeSolution(String userId, String solutionName, String solutionId, String version) {
		String result = "";
		logger.debug(EELFLoggerDelegator.debugLogger, "validateCompositeSolution() : Begin ");
		logger.debug("---- validateCompositeSolution() ------ : Begin ");
		String path = DSUtil.readCdumpPath(userId, confprops.getToscaOutputFolder());
		String bluePrintFileName = "";
		ObjectMapper mapper = new ObjectMapper();
		try {

			Cdump cdump = null;
			// 1. Read the cdump file
			logger.debug("1. Read the cdump file");
			String cdumpFileName = "acumos-cdump" + "-" + solutionId;
			cdump = mapper.readValue(new File(path.concat(cdumpFileName).concat(".json")), Cdump.class);
			// 2. get the Nodes from the cdump file and collect the nodeId's
			logger.debug("2. get the Nodes from the cdump file and collect the nodeId's");
			List<Nodes> nodes = cdump.getNodes();
			ArrayList<String> idList = new ArrayList<>();
			if (nodes != null) {
				for (Nodes n : nodes) {
					idList.add(n.getNodeId());
				}
				// 3. get the Relations(Links) from cdump file and collect the
				// SourceNodeId and TargetNodeId and add those to set
				logger.debug("3. get the Relations(Links) from cdump file and collect the SourceNodeId and TargetNodeId and add those to set");
				List<Relations> relationsList = cdump.getRelations();
				HashSet<String> set = new HashSet<>();
				if (relationsList != null) {
					HashSet<Relations> relationsSet = new HashSet<>(relationsList);
					if (relationsSet != null) {
						for (Relations rhs : relationsSet) {
							set.add(rhs.getSourceNodeId());
							set.add(rhs.getTargetNodeId());
						}
					}
					// 4. Verify the all the nodeId's and Relations(SourceNodeId and TargetNodeId) are there or not.
					logger.debug("4. Verify the all the nodeId's and Relations(SourceNodeId and TargetNodeId) are there or not.");
					if (CollectionUtils.isEqualCollection(idList, set)) {
						// 5. Checking the Composite Solution Nodes and Relations are connected or not.
						logger.debug("5. Checking the Composite Solution Nodes and Relations are connected or not.");
						if (relationsList.size() >= idList.size() - 1) {
							// 6. On successful validation generate the BluePrint file
							logger.debug("6. On successful validation generate the BluePrint file");
							BluePrint bluePrint = new BluePrint();
							// 7. Set the Solution name and version
							logger.debug("7. On successful validation generate the BluePrint file");
							bluePrint.setName(solutionName);
							bluePrint.setVersion(version);
							
							String probeIndicator = null;
							probeIndicator = cdump.getProbeIndicator();
							ProbeIndicator pIndicator = new ProbeIndicator();
							pIndicator.setValue(probeIndicator);
							List<ProbeIndicator> probeLst = new ArrayList<ProbeIndicator>();
							probeLst.add(pIndicator);
							bluePrint.setProbeIndocator(probeLst); // In cdump probeIndicator is a string, in blueprint it should not be an array just a string 
							Set<String> sourceNodeId = new HashSet<>();
							Set<String> targetNodeId = new HashSet<>();
							for (Relations rlns : relationsList) {
								sourceNodeId.add(rlns.getSourceNodeId());
								targetNodeId.add(rlns.getTargetNodeId());
							}
							sourceNodeId.removeAll(targetNodeId);
							
							List<Container> containerList = new ArrayList<Container>();
							Container container = new Container();
							BaseOperationSignature bos = new BaseOperationSignature();
							String opearion = "";
							for (Relations rltn : relationsList) {
								if (sourceNodeId.contains(rltn.getSourceNodeId())) {
									opearion = rltn.getSourceNodeRequirement().replace("+", "%PLUS%");
									opearion = opearion.split("%PLUS%")[0];
									logger.debug(EELFLoggerDelegator.debugLogger, "Opearion : " + opearion);
									bos.setOperation_name(opearion);
									container.setOperation_signature(bos);
									String containerName = rltn.getTargetNodeName();
									container.setContainer_name(containerName);
									containerList.add(container);
								}
							}
							bluePrint.setInput_ports(containerList);
							// 8. Get the nodes from Cdump file & set the required details in the blueprint nodes
							logger.debug("8. Get the nodes from Cdump file & set the required details in the blueprint nodes");
							List<Nodes> cdumpNodes = cdump.getNodes();
							String nodeName = "";
							String nodeId = "";
							String nodeSolutionId = "";
							String nodeVersion = "";
							List<OperationSignatureList> oslList = new ArrayList<>();
							OperationSignatureList osll = null;
							NodeOperationSignature nos = null;
							String dockerImageURL = null;
							Node bpnode = null;
							List<Node> bpnodes = new ArrayList<>();
							List<MLPSolutionRevision> mlpSolRevisions = null;
							MLPSolutionRevision mlpSolRevision = null;
							int propLength = 0;
							DataMap dataMap = null;
							Property[] properties = null;
							String gdm = "GDM";

							// 9. Extract NodeId, NodeName,NodeSolutionId,NodeVersion
							logger.debug("9. Extract NodeId, NodeName,NodeSolutionId,NodeVersion");
							for (Nodes n : cdumpNodes) {
								nodeName = n.getName(); // TO set in the blue print
								nodeId = n.getNodeId(); 
								nodeSolutionId = n.getNodeSolutionId(); // To get the DockerImageUrl
								nodeVersion = n.getNodeVersion(); // To get the nodeVersion
								// 11. Get the MlpSolutionRevisions from CDMSClient for the NodeSolutionId
								logger.debug("11. Get the MlpSolutionRevisions from CDMSClient for the NodeSolutionId");
								mlpSolRevision = getSolutionRevisions(nodeSolutionId, nodeVersion, mlpSolRevision);
								boolean isGDM = false;
								// get the properties from Nodes
								properties = n.getProperties();
								// check whether properties are exits or not
								logger.debug("check whether properties are exits or not");
								if (null != properties && properties.length > 0) {
									propLength = properties.length;
									for (int i = 0; i < propLength; i++) {
										dataMap = properties[i].getData_map();
										if (null != dataMap) {
											if (null != gdm) {
												logger.debug("GDM present");
												isGDM = true;
											}
											break;
										}
									}
								}
								logger.debug("GDM Found : " + isGDM);
								if (isGDM) {
									// For Generic Data Mapper, get the dockerImageUrl by deploying the GDM
									// Construct the image for the Generic Data mapper
									logger.debug("For Generic Data Mapper, get the dockerImageUrl by deploying the GDM Construct the image for the Generic Data mapper");
									dockerImageURL = gdmService.createDeployGDM(cdump, userId);
									if (null == dockerImageURL) {
										logger.error(EELFLoggerDelegator.errorLogger,"---------Error : Issue in createDeployGDM() : Failed to create the Solution Artifact ----------");
										logger.debug("---------Error : Issue in createDeployGDM() : Failed to create the Solution Artifact ----------");
										throw new ServiceException("---  Issue in createDeployGDM() ----", "333",
												"Issue while crearting and deploying GDM image");
									}
								} else {
									// Else for basic models, upload the image and get the uri
									// 12. Get the list of artifact from CDMSClient which will return the
									// DockerImageUrl
									logger.debug("12. Get the list of artifact from CDMSClient which will return the DockerImageUrl");
									dockerImageURL = getDockerImageURL(nodeSolutionId, mlpSolRevision);
								}
								// 13. Set the values in the bluePrint Node
								logger.debug("13. Set the values in the bluePrint Node");
								bpnode = new Node();
								bpnode.setContainer_name(nodeName);
								bpnode.setImage(dockerImageURL);
								String node_type = (null != n.getType().getName()? n.getType().getName().trim() : "");
								bpnode.setNode_type(node_type); 
								String protoUri = n.getProtoUri();
								bpnode.setProto_uri(protoUri); 
								
								List<Capabilities> capabilities = Arrays.asList(n.getCapabilities());
								String nodeOperationName = null;
								List<Container> containerLst = new ArrayList<Container>();
								
								//Get the connected port 
								String connectedPort = getConnectedPort(cdump.getRelations(), n.getNodeId());
								
								for(Capabilities c : capabilities ){
									nodeOperationName = c.getTarget().getId();
									if(nodeOperationName.equals(connectedPort)){
										osll = new OperationSignatureList();
										nos = new NodeOperationSignature();
										
										nos.setOperation_name(nodeOperationName);
										//set input_message_name
										nos.setInput_message_name(c.getTarget().getName()[0].getMessageName());  
										//NodeOperationSignature input_message_name should have been array, as operation can have multiple input messages.  
										//Its seems to be some gap
										
										//set output_message_name
										nos.setOutput_message_name(getOutputMessage(n.getRequirements(), nodeOperationName));
										//Set NodeOperationSignature as operation_signature
										osll.setOperation_signature(nos);
										//set List<Container> connected_to
										containerLst = getRelations(cdump, nodeId);
										osll.setConnected_to(containerLst);
										oslList.add(osll);
									}
								}
								bpnode.setOperation_signature_list(oslList);

								// 14. Add the nodedetails to bluepring nodes list
								logger.debug("14. Add the nodedetails to blueprint nodes list");
								bpnodes.add(bpnode);
							}

							bluePrint.setNodes(bpnodes);
							
							// 15. Write Data to bluePrint file and construct the name of the file
							logger.debug("15. Write Data to bluePrint file and construct the name of the file");
							bluePrintFileName = "BluePrint" + "-" + solutionId;
							// 16. Convert bluePrint to json
							logger.debug("16. Convert bluePrint to json");
							String bluePrintJson = mapper.writeValueAsString(bluePrint);
							// 17. Create and write details to file
							logger.debug("17. Create and write details to file");
							DSUtil.writeDataToFile(path, bluePrintFileName, "json", bluePrintJson);
							// 18. Get the Artifact Data
							logger.debug("18. Get the Artifact Data");
							Artifact bluePrintArtifact = new Artifact(bluePrintFileName, "json", solutionId, version,
									path, bluePrintJson.length());
							// 19. Upload the file to Nexus
							logger.debug("19. Upload the file to Nexus");
							uploadFilesToRepository(solutionId, version, bluePrintArtifact);

							// 20. Create the MLPArtifact
							logger.debug("20. Create the MLPArtifact");
							MLPArtifact mlpArtifact = null;
							try {
								mlpArtifact = new MLPArtifact();
								mlpArtifact.setArtifactTypeCode("BP");
								mlpArtifact.setDescription("BluePrint File for : " + solutionName + " for SolutionID : "
										+ solutionId + " with version : " + version);
								mlpArtifact.setUri(bluePrintArtifact.getNexusURI());
								mlpArtifact.setName(bluePrintArtifact.getName());
								mlpArtifact.setOwnerId(userId);
								mlpArtifact.setVersion(version);
								mlpArtifact.setSize(bluePrintJson.length());

								// 21. Creating the Artifact from CDMSClient.
								logger.debug("21. Creating the Artifact from CDMSClient.");
								mlpArtifact = cdmsClient.createArtifact(mlpArtifact);

								logger.debug("-------Successfully created the artifact for the BluePrint for the solution : "
												+ solutionId + " artifact ID : " + mlpArtifact.getArtifactId());

								// 22. Get the SolutionRevisions from CDMSClient.
								logger.debug("22. Get the SolutionRevisions from CDMSClient.");
								mlpSolRevisions = cdmsClient.getSolutionRevisions(solutionId);
								MLPSolutionRevision compositeSolutionRevision = null;
								// 23. Iterate over MLPSolutionRevisions and get the CompositeSolutionRevision.
								logger.debug(
										"23. Iterate over MLPSolutionRevisions and get the CompositeSolutionRevision.");
								for (MLPSolutionRevision solRev : mlpSolRevisions) {
									if (solRev.getVersion().equals(version)) {
										compositeSolutionRevision = solRev;
										break;
									}
								}
								// 24. Associate the SolutionRevisionArtifact for solution ID.
								logger.debug("24. Associate the SolutionRevisionArtifact for solution ID.");
								cdmsClient.addSolutionRevisionArtifact(solutionId,compositeSolutionRevision.getRevisionId(), mlpArtifact.getArtifactId());

								logger.debug(EELFLoggerDelegator.debugLogger,"------- Successfully associated the Solution Revision Artifact for solution ID  : " + solutionId);

							} catch (Exception e) {
								logger.error(EELFLoggerDelegator.errorLogger,"---------Error : Exception in validateCompositeSolution() : Failed to create the Solution Artifact ----------",e);
								throw new ServiceException("---  Exception in validateCompositeSolution() ----", "333",
										"Failed to create the Solution Artifact");
							}
							result = "{\"success\" : \"true\", \"errorDescription\" : \"\"}";

						} else {
							result = "{\"success\" : \"false\", \"errorDescription\" : \"Invalid Composite Solution\"}";
						}
					} else {
						result = "{\"success\" : \"false\", \"errorDescription\" : \"Invalid Composite Solution\"}";
					}
				} else {
					result = "{\"success\" : \"false\", \"errorDescription\" : \"Invalid Composite Solution\"}";
				}
			} else {
				result = "{\"success\" : \"false\", \"errorDescription\" : \"Invalid Composite Solution\"}";
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger,"------- Exception in validateCompositeSolution() in Service -------", e);
			logger.error("------------Exception Message : " + e.getMessage());
		}
		logger.debug("---- validateCompositeSolution() in Service ------ : End ");
		logger.debug(EELFLoggerDelegator.debugLogger, "---- validateCompositeSolution() in Service ------ : End ");
		return result;
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
					result = r.getSourceNodeRequirement().replace("+", "%PLUS%");
					result = result.split("%PLUS%")[0];
					break;
				} else if(nodeId.equals(r.getTargetNodeId())){
					result = r.getTargetNodeCapability().replace("+", "%PLUS%");
					result = result.split("%PLUS%")[0];
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
				operation = cr.getTargetNodeCapability().replace("+", "%PLUS%");
				operation = operation.split("%PLUS%")[0];
				bos.setOperation_name(operation);
				connectedTo.setOperation_signature(bos);
				connectedToList.add(connectedTo);
			}
		}
		return connectedToList;
	}
	@Override
	public String setProbeIndicator(String userId, String solutionId, String version, String cid,
			String probeIndicator) {
		logger.debug(EELFLoggerDelegator.debugLogger, "------ setProbeIndicator() in CompositeSolutionServiceImpl : Begin---------- ");
        String id = "";
		Gson gson = new Gson();
		String result = "";
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
				result = "{\"success\" : \"true\", \"errorDescription\" : \"\"}";
			}catch (JsonIOException e) {
				result = "{\"success\" : \"false\", \"errorDescription\" : \"There is some issue to set prob indicator value,please check the log file\"}";
				logger.error(EELFLoggerDelegator.errorLogger, "------Exception in setProbeIndicator() ----------", e);
			}
		} catch (Exception e) {
			result = "{\"success\" : \"false\", \"errorDescription\" : \"There is some issue to set prob indicator value,please check the log file\"}";
			logger.error(EELFLoggerDelegator.errorLogger, "----- Exception Occured in setProbeIndicator() ------", e);
		}
        logger.debug(EELFLoggerDelegator.debugLogger, "------ setProbeIndicator() in CompositeSolutionServiceImpl : End---------- ");
		return result;
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

}
