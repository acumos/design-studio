/*-
 * ===============LICENSE_START=======================================================
 * Acumos
 * ===================================================================================
 * Copyright (C) 2017 AT&T Intellectual
 * 						Property & Tech
 * 						Mahindra. All rights reserved.
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

package org.acumos.designstudio.ce.docker;

import org.acumos.designstudio.ce.exceptionhandler.ServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 
 * 
 *  
 */
@Component
public class DockerConfiguration {
	// TODO : Need to get the details from SPRING_APPLICATION_JSON
	@Value("${docker.config}")
	private String config;

	@Value("${docker.api.version}")
	private String apiVersion;

	@Value("${docker.host}")
	private String host;

	@Value("${docker.port}")
	private Integer port;

	@Value("${docker.registry.url}")
	private String registryUrl;

	@Value("${docker.registry.username}")
	private String registryUsername;

	@Value("${docker.registry.password}")
	private String registryPassword;

	@Value("${docker.registry.email}")
	private String registryEmail;

	@Value("${docker.imagetag.prefix}")
	private String imagetagPrefix;
	
	@Value("${docker.tls.verify}")
	private boolean tlsVerify;
	
	@Value("${docker.max_total_connections}")
	private Integer maxTotalConnections;

	@Value("${docker.max_per_route_connections}")
	private Integer maxPerRouteConnections;

	private Integer requestTimeout;
	
	private String certPath;

	private boolean socket = false;

	private String cmdExecFactory = "com.github.dockerjava.netty.NettyDockerCmdExecFactory";

	

	public String getConfig() {
		return config;
	}

	/**
	 * 
	 * @return URL
	 * @throws ServiceException On failure
	 */
	public String toUrl() throws ServiceException {
		if (this.host == null)
			throw new ServiceException("host is required");
		if (this.port == null)
			throw new ServiceException("port is required");
		return ((this.socket) ? "unix" : "tcp") + "://" + host + ":" + port;
	}
		 

	public String getApiVersion() {
		return apiVersion;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getHost() {
		return host;
	}
	
	public void setPort(Integer port) {
		this.port = port;
	}

	public Integer getPort() {
		return port;
	}

	public String getRegistryUsername() {
		return registryUsername;
	}

	public String getRegistryPassword() {
		return registryPassword;
	}

	/**
	 * @return the imagetagPrefix
	 */
	public String getImagetagPrefix() {
		return imagetagPrefix;
	}

	public String getRegistryUrl() {
		return registryUrl;
	}

	public String getRegistryEmail() {
		return registryEmail;
	}

	
	public Integer getRequestTimeout() {
		return requestTimeout;
	}

	public void setRequestTimeout(Integer requestTimeout) {
		this.requestTimeout = requestTimeout;
	}
	 
	public boolean isTlsVerify() {
		return tlsVerify;
	}

	public String getCertPath() {
		return certPath;
	}
	
	public void setSocket(boolean socket) {
		this.socket = socket;
	}

	public boolean isSocket() {
		return socket;
	}

	public String getCmdExecFactory() {
		return cmdExecFactory;
	}

	public Integer getMaxTotalConnections() {
		return maxTotalConnections;
	}

	public Integer getMaxPerRouteConnections() {
		return maxPerRouteConnections;
	}

}
