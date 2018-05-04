package org.acumos.csvdatabroker.service;

import org.acumos.csvdatabroker.util.Constants;
import org.acumos.csvdatabroker.util.EELFLoggerDelegator;
import org.acumos.csvdatabroker.vo.Configuration;
import org.springframework.stereotype.Component;


@Component("ConfigurationServiceImpl")
public class ConfigurationServiceImpl implements ConfigurationService {
	private final EELFLoggerDelegator logger = EELFLoggerDelegator.getLogger(ConfigurationServiceImpl.class);
	
	
	private Configuration conf;
	
	private int resultsetSize;
	
	private int start; 
	
	private boolean shellFileCreated;
	
	/**
	 * To increment the next sart by resultsetSize.
	 */
	public void incrementStart(){
		start = start + resultsetSize;
	}
	
	/**
	 * @return the conf
	 * @throws CloneNotSupportedException 
	 */
	public Configuration getConf() throws CloneNotSupportedException {
		return (null != conf)? (Configuration)conf.clone() : null;
	}

	/**
	 * @param conf 
	 */
	public void setConf(Configuration conf) {
		this.conf = conf;
		if(conf.getData_broker_map().getFirst_row().equals(Constants.FIRST_ROW_CONTAINS_FIELDNAMES)) {
			start = 1;
		} else {
			start = 0;
		}
		shellFileCreated = false;
	}

	/**
	 * @return the resultsetSize
	 */
	public int getResultsetSize() {
		return resultsetSize;
	}

	/**
	 * @param resultsetSize the resultsetSize to set
	 */
	public void setResultsetSize(int resultsetSize) {
		this.resultsetSize = resultsetSize;
	}

	/**
	 * @return the start
	 */
	public int getStart() {
		return start;
	}

	/**
	 * @param start the start to set
	 */
	public void setStart(int start) {
		this.start = start;
	}

	/**
	 * @return the shellFileCreated
	 */
	public boolean isShellFileCreated() {
		return shellFileCreated;
	}

	/**
	 * @param shellFileCreated the shellFileCreated to set
	 */
	public void setShellFileCreated(boolean shellFileCreated) {
		this.shellFileCreated = shellFileCreated;
	}
	
	
}
