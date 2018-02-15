/**
 * 
 */
package org.acumos.designstudio.ce.vo.blueprint;

import java.io.Serializable;

/**
 * @author RP00490596
 *
 */
public class BaseOperationSignature implements Serializable {
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5164086725433716732L;
	private String operation_name;

	/**
	 * @return the operation_name
	 */
	public String getOperation_name() {
		return operation_name;
	}

	/**
	 * @param operation_name the operation_name to set
	 */
	public void setOperation_name(String operation_name) {
		this.operation_name = operation_name;
	} 
	
	
}
