package org.acumos.designstudio.ce.service;

import org.acumos.designstudio.ce.exceptionhandler.ServiceException;
import org.acumos.designstudio.ce.vo.cdump.Cdump;

public interface IGenericDataMapperService {

	public String createDeployGDM(Cdump cdump, String nodeId, String userId) throws ServiceException ;
	
}
