package org.acumos.designstudio.ce.service;

import org.acumos.designstudio.cdump.Cdump;
import org.acumos.designstudio.ce.exceptionhandler.ServiceException;

public interface IGenericDataMapperService {

	public String createDeployGDM(Cdump cdump, String userId) throws ServiceException ;
	//public void createProtobuf(Cdump cdump, String userId);
	//public void createJavaClasses();
	//public void createFieldMappingFile();
	//public void createGDMJar();
	//public void createGDMDockerImage();
}
