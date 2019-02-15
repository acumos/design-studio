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
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.NoSuchElementException;

import org.acumos.cds.client.CommonDataServiceRestClientImpl;
import org.acumos.cds.domain.MLPArtifact;
import org.acumos.cds.domain.MLPSolutionRevision;
import org.acumos.designstudio.ce.exceptionhandler.AcumosException;
import org.acumos.designstudio.ce.exceptionhandler.ServiceException;
import org.acumos.designstudio.ce.util.DSLogConstants;
import org.acumos.designstudio.ce.util.Properties;
import org.acumos.nexus.client.NexusArtifactClient;
import org.acumos.nexus.client.RepositoryLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 *
 *
 */
@Service
public class AcumosCatalogServiceImpl implements IAcumosCatalog {

	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Autowired
	private CommonDataServiceRestClientImpl cmnDataService;

	@Autowired
	private Properties props;

	@Autowired
	private NexusArtifactClient nexusArtifactClient;

	@Override
	public String fetchJsonTOSCA(String solutionId, String version) {
		logger.debug("fetchJsonTOSCA() : Begin");

		String result = "";
		String error = "{\"errorCode\" : \"%s\", \"errorDescription\" : \"%s\"}";

		List<MLPSolutionRevision> mlpSolutionRevisionList;
		String solutionRevisionId = null;
		ByteArrayOutputStream byteArrayOutputStream = null;
		List<MLPArtifact> mlpArtifactList;
		cmnDataService.setRequestId(MDC.get(DSLogConstants.MDCs.REQUEST_ID));
		try {
			// 1. Get the list of SolutionRevision for the solutionId.
			mlpSolutionRevisionList = getSolutionRevisionsList(solutionId);

			// 2. Match the version with the SolutionRevision and get the
			// solutionRevisionId.
			if (null != mlpSolutionRevisionList && !mlpSolutionRevisionList.isEmpty()) {
				solutionRevisionId = mlpSolutionRevisionList.stream().filter(mlp -> mlp.getVersion().equals(version))
						.findFirst().get().getRevisionId();
				logger.debug("SolutionRevisonId for Version :  {} ", solutionRevisionId);
			} else {
				result = String.format(error, "501", "Failed to fetch the Solution Revision List");
			}
		} catch (Exception e) {
			logger.error("Error : Exception in fetchJsonTOSCA() : Failed to fetch the Solution Revision List",e);
			result = String.format(error, "501","Failed to fetch the Solution Revision List for the version {} ", version);
		}

		if (null != solutionRevisionId) {
			// 3. Get the list of Artifiact for the SolutionId and SolutionRevisionId.
			mlpArtifactList = getListOfArtifacts(solutionId, solutionRevisionId);

			String artifactType = props.getArtifactType();
			String nexusURI = "";
			if (null != mlpArtifactList && !mlpArtifactList.isEmpty()) {
				try {
					nexusURI = mlpArtifactList.stream()
							.filter(mlpArt -> mlpArt.getArtifactTypeCode().equalsIgnoreCase(artifactType)).findFirst()
							.get().getUri();
					logger.debug("Nexus URI :  {} ", nexusURI);

					if (null != nexusURI) {
						byteArrayOutputStream = getPayload(nexusURI);
						logger.debug("Response in String Format : {}" , byteArrayOutputStream.toString() );
						result = byteArrayOutputStream.toString();
					} else {
						result = String.format(error, "504","Could not search the artifact URI for artifactType " + artifactType);
					}
				} catch (Exception e) {
					logger.error("Error : Exception in fetchJsonTOSCA() : Failed to fetch the Solution Revision List",e);
					result = String.format(error, "504","Could not search the artifact URI for artifactType TG" + artifactType);

				} finally {
					try {
						if (byteArrayOutputStream != null) {
							byteArrayOutputStream.close();
						}
					} catch (IOException e) {
						logger.error("Error : Exception in readArtifact() : Failed to close the byteArrayOutputStream", e);
					}
				}

			} else {
				result = String.format(error, "503", "Failed to fetch the Artifact Details for " + solutionId + "with solutionRevisionId" + solutionRevisionId);
			}
		} else {
			result = String.format(error, "502","Failed to fetch the solutionRevisionId for " + solutionId + " with version " + version);
		}
		logger.debug("fetchJsonTOSCA() : End");
		return result;
	}

	/**
	 * 
	 * @param solutionId
	 * @return
	 */
	private List<MLPSolutionRevision> getSolutionRevisionsList(String solutionId) {
		logger.debug("getSolutionRevisions() : Begin ");
		List<MLPSolutionRevision> solRevisionsList = null;
		try {
			solRevisionsList = cmnDataService.getSolutionRevisions(solutionId);
		} catch (Exception e) {
			logger.error("Exception in getSolutionRevisions() ", e);
		}
		logger.debug("getSolutionRevisions() : End ");
		return solRevisionsList;
	}

	/**
	 * 
	 * @param solutionId
	 * @param solutionRevisionId
	 * @return
	 */
	private List<MLPArtifact> getListOfArtifacts(String solutionId, String solutionRevisionId) {
		List<MLPArtifact> mlpArtifactsList = null;
		try {
			mlpArtifactsList = cmnDataService.getSolutionRevisionArtifacts(solutionId, solutionRevisionId);
		} catch (Exception e) {
			logger.error("Exception in getListOfArtifacts() ", e);
		}
		return mlpArtifactsList;

	}

	/**
	 * 
	 * @param uri
	 * @return
	 */
	private ByteArrayOutputStream getPayload(String uri) {

		ByteArrayOutputStream outputStream = null;
		try {
			outputStream = nexusArtifactClient.getArtifact(uri);
		} catch (Exception ex) {
			logger.error("Exception in getListOfArtifacts() ", ex);
		}
		return outputStream;
	}

	@Override
	public String readArtifact(String userId, String solutionId, String version, String artifactType)
			throws AcumosException {
		logger.debug("readArtifact() : Begin");

		String result = "";

		List<MLPSolutionRevision> mlpSolutionRevisionList;
		String solutionRevisionId = null;
		ByteArrayOutputStream byteArrayOutputStream = null;
		List<MLPArtifact> mlpArtifactList;
		cmnDataService.setRequestId(MDC.get(DSLogConstants.MDCs.REQUEST_ID));
		try {
			// 1. Get the list of SolutionRevision for the solutionId.
			mlpSolutionRevisionList = getSolutionRevisionsList(solutionId);

			// 2. Match the version with the SolutionRevision and get the
			// solutionRevisionId.
			if (null != mlpSolutionRevisionList && !mlpSolutionRevisionList.isEmpty()) {
				solutionRevisionId = mlpSolutionRevisionList.stream().filter(mlp -> mlp.getVersion().equals(version))
						// && mlp.getOwnerId().equalsIgnoreCase(userId))
						.findFirst().get().getRevisionId();
				logger.debug("SolutionRevisonId for Version :  {} ", solutionRevisionId );
			}
		} catch (NoSuchElementException | NullPointerException e) {
			logger.error("Error : Exception in readArtifact() : Failed to fetch the Solution Revision Id",e);
			throw new NoSuchElementException("Failed to fetch the Solution Revision Id of the solutionId for the user");
		} catch (Exception e) {
			logger.error("Error : Exception in readArtifact() : Failed to fetch the Solution Revision Id",e);
			throw new ServiceException("Failed to fetch the Solution Revision Id for the solutionId " + solutionId);
		}

		if (null != solutionRevisionId) {
			// 3. Get the list of Artifiact for the SolutionId and SolutionRevisionId.
			mlpArtifactList = getListOfArtifacts(solutionId, solutionRevisionId);
			String nexusURI = "";
			if (null != mlpArtifactList && !mlpArtifactList.isEmpty()) {
				try {
					nexusURI = mlpArtifactList.stream()
							.filter(mlpArt -> mlpArt.getArtifactTypeCode().equalsIgnoreCase(artifactType)).findFirst()
							.get().getUri();
					logger.debug("Nexus URI :  {} ", nexusURI );

					if (null != nexusURI) {
						byteArrayOutputStream = getPayload(nexusURI);
						logger.debug("Response in String Format :  {} ", byteArrayOutputStream.toString() );
						result = byteArrayOutputStream.toString();
					}
				} catch (NoSuchElementException | NullPointerException e) {
					logger.error("Error : Exception in readArtifact() : Failed to fetch the artifact URI for artifactType",e);
					throw new NoSuchElementException("Could not search the artifact URI for artifactType " + artifactType);
				} catch (Exception e) {
					logger.error("Error : Exception in readArtifact() : Failed to fetch the artifact URI for artifactType",e);
					throw new ServiceException("  Exception Occured decryptAndWriteTofile() ", "501","Could not search the artifact URI for artifactType " + artifactType, e.getCause());
				} finally {
					try {
						if (byteArrayOutputStream != null) {
							byteArrayOutputStream.close();
						}
					} catch (IOException e) {
						logger.error("Error : Exception in readArtifact() : Failed to close the byteArrayOutputStream", e);
					}
				}
			}
		} else {
			throw new ServiceException("  Solution version details not found ", "501","Could not search the artifact URI for artifactType " + artifactType);
		}
		logger.debug("readArtifact() : End");
		return result;
	}

	/**
	 * 
	 * @param url
	 *            Common dataservice endpoint
	 * @param user
	 *            User name
	 * @param pass
	 *            Passwor
	 */
	public void getRestClient(String url, String user, String pass) {
		cmnDataService = (CommonDataServiceRestClientImpl) CommonDataServiceRestClientImpl.getInstance(url, user, pass);
		cmnDataService.setRequestId(MDC.get(DSLogConstants.MDCs.REQUEST_ID));
	}

	/**
	 * 
	 * @param repositoryLocation
	 *            RepositoryLocation
	 */
	public void getNexusClient(RepositoryLocation repositoryLocation) {
		nexusArtifactClient = new NexusArtifactClient(repositoryLocation);
	}

}
