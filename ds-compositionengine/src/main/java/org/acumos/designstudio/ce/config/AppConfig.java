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

package org.acumos.designstudio.ce.config;

import java.lang.invoke.MethodHandles;

import org.acumos.cds.client.CommonDataServiceRestClientImpl;
import org.acumos.designstudio.ce.util.ConfigurationProperties;
import org.acumos.nexus.client.NexusArtifactClient;
import org.acumos.nexus.client.RepositoryLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 
 * 
 *
 */
@Configuration
public class AppConfig {
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Autowired
	private ConfigurationProperties confProps;

	@Bean
	@Lazy(value = true)
	public CommonDataServiceRestClientImpl commonDataServiceRestClientImpl() {
		CommonDataServiceRestClientImpl cdmsClient = (CommonDataServiceRestClientImpl) CommonDataServiceRestClientImpl.getInstance(confProps.getCmndatasvcendpoinurl(), confProps.getCmndatasvcuser(), confProps.getCmndatasvcpwd());
		return cdmsClient;
	}
	
	@Bean
	@Lazy(value = true)
	public NexusArtifactClient nexusArtifactClient() {
		RepositoryLocation repositoryLocation = new RepositoryLocation();
		repositoryLocation.setId("1");
		repositoryLocation.setUrl(confProps.getNexusendpointurl());
		repositoryLocation.setUsername(confProps.getNexususername());
		repositoryLocation.setPassword(confProps.getNexuspassword());
		NexusArtifactClient nexusArtifactClient = new NexusArtifactClient(repositoryLocation);
		return nexusArtifactClient;
	}
	

	 @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/dsce/**")
                .allowedOrigins("*")
               .allowedMethods("GET","POST");
            }
        };
    }
	 
}
