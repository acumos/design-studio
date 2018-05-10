package org.acumos.designstudio.ce.service;

import org.acumos.designstudio.ce.exceptionhandler.ServiceException;
import org.acumos.designstudio.ce.vo.cdump.Cdump;
import org.acumos.designstudio.ce.vo.cdump.Nodes;

public interface IDataBrokerService {
	/**
	 * This method create and deploy the Databroker as per the legacy implementation
	 * @param cdump
	 * 		Accepts the parameter Cump Vo 
	 * @param nodeId
	 * 		Accepts the parameter Node Id
	 * @param userId
	 * 		Accepts the parameter User Id
	 * @return
	 * 		Returns the Docker Image URI of the newly uploaded docker impage.
	 * @throws ServiceException
	 * 		This method throws ServiceException
	 */
	@Deprecated
	public String createDeployDataBroker(Cdump cdump, String nodeId, String userId) throws ServiceException;
	
	/**
	 * This Method returns the databroker image URI as per the databroker type. 
	 * @param node
	 * 		Accepts the Cdump Node 
	 * @return
	 * 		Returns the Docker Image URI w.r.t to databroker type. 
	 * 
	 * @throws ServiceException
	 * 		This method throws ServiceException
	 */
	public String getDataBrokerImageURI(Nodes node) throws ServiceException;
}
