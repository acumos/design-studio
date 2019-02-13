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

import java.lang.invoke.MethodHandles;

import org.acumos.designstudio.ce.docker.DockerConfiguration;
import org.acumos.designstudio.ce.exceptionhandler.ServiceException;
import org.acumos.designstudio.ce.util.ConfigurationProperties;
import org.acumos.designstudio.ce.util.Properties;
import org.acumos.designstudio.ce.vo.cdump.Cdump;
import org.acumos.designstudio.ce.vo.cdump.Nodes;
import org.acumos.designstudio.ce.vo.cdump.databroker.DataBrokerMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

@Service("DataBrokerServiceImpl")
public class DataBrokerServiceImpl implements IDataBrokerService {

private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	@Autowired
	private ConfigurationProperties confprops;
	
	@Autowired
	private Properties props;
	
	@Autowired
    private DockerConfiguration dockerConfiguration;
	
	private ResourceLoader resourceLoader;
	
	@Autowired
	public DataBrokerServiceImpl(ResourceLoader resourceLoader){
		this.resourceLoader = resourceLoader;
	}
	
	private static String PROTOBUF_TEMPLATE_NAME = "classpath:Protobuf_Template.txt";
	
	private static String DOCKERFILE_TEMPLATE_NAME = "classpath:Dockerfile_Template.txt";
	
	private String path = null;
	
	@Override
	@Deprecated
	public String createDeployDataBroker(Cdump cdump, String nodeId, String userId) throws ServiceException {
		String dockerImageURI = null;
		
		
		//TODO : Need to implement the jar creation, for now get the dockerimage URI of static databroker 
		dockerImageURI = confprops.getCsvdatabrokerURI();
		return dockerImageURI;
	}

	@Override
	public String getDataBrokerImageURI(Nodes node) throws ServiceException {
		String dockerImageURI = null;

		DataBrokerMap map = (null != node.getProperties()[0])? node.getProperties()[0].getData_broker_map():null;
		
		if(null != map){
			String databrokertype = map.getData_broker_type();
			switch (databrokertype) {
			case "csv":
				dockerImageURI = confprops.getCsvdatabrokerURI();
				break;
			case "image":
				dockerImageURI = confprops.getImagedatabrokerURI();
				break;
			case "json":
				dockerImageURI = confprops.getJsondatabrokerURI();
				break;
			case "sql":
				dockerImageURI = confprops.getSqldatabrokerURI();
				break;
			default:
				break;
			}
		}
		return dockerImageURI;
	}

}
