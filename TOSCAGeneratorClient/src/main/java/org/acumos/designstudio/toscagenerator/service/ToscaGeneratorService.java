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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.acumos.cds.client.CommonDataServiceRestClientImpl;
import org.acumos.cds.domain.MLPArtifact;
import org.acumos.cds.domain.MLPSolutionRevision;
import org.acumos.designstudio.toscagenerator.exceptionhandler.AcumosException;
import org.acumos.designstudio.toscagenerator.exceptionhandler.ServiceException;
import org.acumos.designstudio.toscagenerator.util.Properties;
import org.acumos.designstudio.toscagenerator.util.ToscaUtil;
import org.acumos.designstudio.toscagenerator.vo.Artifact;
import org.acumos.nexus.client.NexusArtifactClient;
import org.acumos.nexus.client.RepositoryLocation;
import org.acumos.nexus.client.data.UploadArtifactInfo;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ToscaGeneratorService {
	private static final Logger logger = LoggerFactory.getLogger(ToscaGeneratorService.class);
	
	/**
	 * 
	 * @param solutionID
	 *            solution ID
	 * @param version
	 *            version
	 * @param response
	 *            JSON to parse
	 * @return List of Artifact
	 * @throws AcumosException
	 *             On failure
	 */
	public List<Artifact> decryptAndWriteTofile(String solutionID, String version, String response)
			throws AcumosException {
		List<Artifact> toscaFiles = new ArrayList<Artifact>();
		try {
			String path = Properties.getTempFolderPath(solutionID, version);
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(response);
			JSONObject jsonObject = (JSONObject) obj;

			Base64.Decoder mimeDecoder = Base64.getMimeDecoder();

			// template
			byte[] templateBytes = mimeDecoder.decode(jsonObject.get("template").toString());
			ToscaUtil.writeDataToFile(path, "template", "yaml", new String(templateBytes));
			Artifact template = new Artifact("template", "yaml", solutionID, version, path, templateBytes.length);
			toscaFiles.add(template);

			// schema

			byte[] schemaBytes = mimeDecoder.decode(jsonObject.get("schema").toString());
			ToscaUtil.writeDataToFile(path, "schema", "yaml", new String(schemaBytes));
			Artifact schema = new Artifact("schema", "yaml", solutionID, version, path, schemaBytes.length);
			toscaFiles.add(schema);

			// translate

			byte[] translateBytes = mimeDecoder.decode(jsonObject.get("translate").toString());
			ToscaUtil.writeDataToFile(path, "translate", "yaml", new String(translateBytes));
			Artifact translate = new Artifact("translate", "yaml", solutionID, version, path, translateBytes.length);
			toscaFiles.add(translate);
		} catch (Exception e) {
			logger.error(" Exception Occured  decryptAndWriteTofile() ", e);
			throw new ServiceException("Exception Occured decryptAndWriteTofile()",
					Properties.getDecryptionErrorCode(), Properties.getDecryptionErrorDesc(), e.getCause());
		}
		return toscaFiles;
	}

	/**
	 * 
	 * @param solutionID
	 *            solution ID
	 * @param version
	 *            version
	 * @param toscaFiles
	 *            List of TOSCA file artifacts
	 * @return List of artifacts
	 * @throws AcumosException
	 *             on failure
	 */
	public List<Artifact> uploadFilesToRepository(String solutionID, String version, List<Artifact> toscaFiles)
			throws AcumosException {
		@SuppressWarnings("unused")
		String path = Properties.getTempFolderPath(solutionID, version);
		RepositoryLocation repositoryLocation = new RepositoryLocation();
		repositoryLocation.setId("1");
		repositoryLocation.setUrl(Properties.getNexusEndPointURL());
		repositoryLocation.setUsername(Properties.getNexusUserName());
		repositoryLocation.setPassword(Properties.getNexusPassword());
		repositoryLocation.setProxy(Properties.getNexusProxy());
		// if you need a proxy to access the Nexus
		NexusArtifactClient artifactClient = new NexusArtifactClient(repositoryLocation);
		FileInputStream fileInputStream = null;
		UploadArtifactInfo artifactInfo = null;
		String revisionId = null; 
		CommonDataServiceRestClientImpl cdmsClient = (CommonDataServiceRestClientImpl) CommonDataServiceRestClientImpl.getInstance(
				Properties.getCmnDataSvcEndPoinURL(), Properties.getCmnDataSvcUser(), Properties.getCmnDataSvcPwd());
		try {
			if (toscaFiles != null && !toscaFiles.isEmpty()) {
				for (Artifact a : toscaFiles) {

					// 1. group id ,2. artifact name, 3. version, 4. extension i.e., packaging, 5. size of content, 6. actual file input stream.
					List<MLPSolutionRevision> mlpSolnRevision = cdmsClient.getSolutionRevisions(solutionID);
					if (null != mlpSolnRevision && !mlpSolnRevision.isEmpty()) {
						for (MLPSolutionRevision mlpSolRev : mlpSolnRevision) {
							if (mlpSolRev.getVersion().equalsIgnoreCase(version)) {
								revisionId = mlpSolRev.getRevisionId();
							}
						}
					} 
					fileInputStream = new FileInputStream(a.getPayloadURI());
					if(null != revisionId) {
						artifactInfo = artifactClient.uploadArtifact(Properties.getNexusGropuId()+"."+a.getSolutionID()+"."+revisionId,
								 a.getType(), a.getVersion(), a.getExtension(),	a.getContentLength(), fileInputStream);
						a.setNexusURI(artifactInfo.getArtifactMvnPath());
					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception Occured  uploadFilesToRepository()", e);
			throw new ServiceException("Exception Occured  uploadFilesToRepository()",
					Properties.getUploadFileErrorCode(), Properties.getUploadFileErrorDesc(), e.getCause());
		}
		finally {
			if(null != fileInputStream){
				try {
					fileInputStream.close();
				} catch (IOException e) {
					logger.error("Exception Occured  while closing fileInputStream()", e);
				}
			}
		}
		return toscaFiles;

	}

	/**
	 * 
	 * @param modelMetaData
	 *            Model metadata
	 * @return TOSCA models
	 * @throws MalformedURLException
	 *             On bad URL
	 * @throws IOException
	 *             On failure to read
	 * @throws ProtocolException
	 *             on HTTP failure
	 * @throws AcumosException
	 *             On other failure
	 */
	public String getToscaModels(String modelMetaData)
			throws MalformedURLException, IOException, ProtocolException, AcumosException {
		String json_spec = "{ \"spec\" : " + modelMetaData + "}";
		StringBuilder sb = null;
		BufferedReader br = null;
		// 1. Integrate TOSCA Model Generator Python Web Service
		try {
			URL url = new URL(Properties.getToscaGeneratorEndPointURL());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");

			OutputStream os = conn.getOutputStream();
			os.write(json_spec.getBytes());
			os.flush();

			br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String line;
			sb = new StringBuilder();
			line = br.readLine();
			while (line != null) {
				sb.append(line);
				line = br.readLine();
			}
			conn.disconnect();

		} catch (Exception ex) {
			logger.error("Exception Occured  getToscaModels()", ex);
			throw new ServiceException("Exception Occured  getToscaModels()", Properties.getConnectionErrorCode(),
					Properties.getConnectionErrorDesc(), ex.getCause());
		} finally {
			if (null != br) {
				br.close();
			}
		}
		return sb.toString();
	}

	/**
	 * 
	 * @param solutionId
	 *            Solution ID
	 * @param solutionRevisionId
	 *            Revision ID
	 * @param ownerID
	 *            User ID
	 * @param toscaFiles
	 *            List of TOSCA file artifacts
	 * @throws AcumosException
	 *            On failure
	 * @throws URISyntaxException 
	 * 			 On failure
	 */
	public void postArtifact(String solutionId, String solutionRevisionId, String ownerID, List<Artifact> toscaFiles)
			throws AcumosException, URISyntaxException {

		CommonDataServiceRestClientImpl cdmsClient = (CommonDataServiceRestClientImpl) CommonDataServiceRestClientImpl.getInstance(
				Properties.getCmnDataSvcEndPoinURL(), Properties.getCmnDataSvcUser(), Properties.getCmnDataSvcPwd());
		MLPArtifact cArtifact = null;
		MLPArtifact result = null;
		if (toscaFiles != null && !toscaFiles.isEmpty()) {
			for (Artifact a : toscaFiles) {
				cArtifact = new MLPArtifact();
				logger.debug("Type : " + a.getType());
				logger.debug("ArtifactTypeCode : " + Properties.getTOSCATypeCode(a.getType()));
				logger.debug("Descripton : " + "Tosca file : " + a.getName() + " for SolutionID : " + a.getSolutionID()
						+ " with version : " + a.getVersion());
				cArtifact.setArtifactTypeCode(Properties.getTOSCATypeCode(a.getType()));
				cArtifact.setDescription("Tosca file : " + a.getName() + " for SolutionID : " + a.getSolutionID()
						+ " with version : " + a.getVersion());
				cArtifact.setUri(a.getNexusURI());
				cArtifact.setName(a.getName());
				cArtifact.setUserId(ownerID);
				cArtifact.setVersion(a.getVersion());
				cArtifact.setSize(a.getContentLength());
				try {
					result = cdmsClient.createArtifact(cArtifact);
					// Associate the TOSCA Artifact to the SolutionRevisionArtifact;
					cdmsClient.addSolutionRevisionArtifact(solutionId, solutionRevisionId, result.getArtifactId());

				} catch (Exception ex) {
					logger.error("Exception Occured  postArtifact() ", ex);
					throw new ServiceException(
							"Exception Occured  postArtifact()",
							Properties.getConnectionErrorCode(), Properties.getConnectionErrorDesc(), ex.getCause());
				}
			}
		}
	}

}
