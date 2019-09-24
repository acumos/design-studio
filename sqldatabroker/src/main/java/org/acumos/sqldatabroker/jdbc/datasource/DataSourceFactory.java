/*-
 * ===============LICENSE_START=======================================================
 * Acumos
 * ===================================================================================
 * Copyright (C) 2017 - 2018 AT&T Intellectual Property & Tech Mahindra. All rights reserved.
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

package org.acumos.sqldatabroker.jdbc.datasource;

import org.acumos.sqldatabroker.exceptionhandler.ServiceException;
import org.acumos.sqldatabroker.util.DatabrokerConstants;
import org.acumos.sqldatabroker.util.EELFLoggerDelegator;
import org.acumos.sqldatabroker.vo.Configuration;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.stereotype.Component;

@Component
public class DataSourceFactory {

	private final EELFLoggerDelegator logger = EELFLoggerDelegator.getLogger(DataSourceFactory.class);	
	
	/**
	 * Return the BasicDataSource instance as per the data source class name configured in the configuration instance passed in. 
	 * Currently supporting MYSQL and Oracle only.
	 * @param conf
	 * 			Configuration details
	 * @return
	 * 			BasicDataSource 
	 * @throws ServiceException
	 * 			Throws ServiceException in case any failure
	 */
	public BasicDataSource getDataSource(Configuration conf) throws ServiceException {

		BasicDataSource ds = null;
		try {
			// Support is not provided for all data base so we have condition else condition is not required.
			if (DatabrokerConstants.MYSQLCLASSNAME.equals(conf.getDataSourceClassName())) {
				ds = new BasicDataSource();
				ds.setDriverClassName(DatabrokerConstants.MYSQLCLASSNAME);
			} else if (DatabrokerConstants.ORACLECLASSNAME.equals(conf.getDataSourceClassName())) {
				ds = new BasicDataSource();
				ds.setDriverClassName(DatabrokerConstants.ORACLECLASSNAME);
			}
			if (null != ds) {
				ds.setUrl(conf.getTarget_system_url());
				ds.setUsername(conf.getUserId());
				ds.setPassword(conf.getPassword());
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegator.errorLogger, "Exception while creating DataSource", e);
			throw new ServiceException("Exception while creating DataSource.", "401", "Exception in getDataSource()",e);
		}

		return ds;
	}
	
}
