package org.acumos.designstudio.ce.service;

import org.acumos.designstudio.cdump.Cdump;
import org.acumos.designstudio.ce.docker.DockerConfiguration;
import org.acumos.designstudio.ce.exceptionhandler.ServiceException;
import org.acumos.designstudio.ce.util.ConfigurationProperties;
import org.acumos.designstudio.ce.util.EELFLoggerDelegator;
import org.acumos.designstudio.ce.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

@Service("DataBrokerServiceImpl")
public class DataBrokerServiceImpl implements IDataBrokerService {

private static EELFLoggerDelegator logger = EELFLoggerDelegator.getLogger(DataBrokerServiceImpl.class);
	
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
	public String createDeployDataBroker(Cdump cdump, String nodeId, String userId) throws ServiceException {
		String dockerImageURI = null;
		
		
		//TODO : Need to implement the jar creation, for now get the dockerimage URI of static databroker 
		dockerImageURI = confprops.getDatabrokerImageURI();
		return dockerImageURI;
	}

}
