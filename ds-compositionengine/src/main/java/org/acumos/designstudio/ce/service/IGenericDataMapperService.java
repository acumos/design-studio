package org.acumos.designstudio.ce.service;

import org.acumos.designstudio.cdump.Cdump;
import org.acumos.designstudio.ce.exceptionhandler.ServiceException;

public interface IGenericDataMapperService {

	public String createDeployGDM(Cdump cdump, String nodeId, String userId) throws ServiceException ;
	
}
