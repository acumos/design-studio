package org.acumos.sqldatabroker.service;

import java.io.IOException;
import java.io.OutputStream;

import org.acumos.sqldatabroker.exceptionhandler.ServiceException;

public interface SQLDatabrokerService {
	/**
	 * This methods writes line by line data to the OutputStream passed as the input parameter
	 * @param out
	 *         OutputStream parameter to write output to.
	 * @throws ServiceException
	 * 			In case of any exception, this method throws the ServiceException
	 * @throws IOException
	 * 			Throws IOException while writing data.
	 */
	public void writeDataTo(OutputStream out) throws ServiceException, IOException;
	
	/**
	 * This method returns one line of output as array of byte
	 * @return byte[]
	 * 			Return the date in the from of byte[]
	 * @throws ServiceException
	 * 			In case of any exception, this method throws the ServiceException
	 */
	public byte[] getOneRecord() throws ServiceException;
}
