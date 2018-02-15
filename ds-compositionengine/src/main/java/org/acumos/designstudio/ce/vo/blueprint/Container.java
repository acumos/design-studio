/**
 * 
 */
package org.acumos.designstudio.ce.vo.blueprint;

import java.io.Serializable;

public class Container implements Serializable{

	private static final long serialVersionUID = 7033995176723370491L;

	private String container_name;
	private BaseOperationSignature operation_signature;
	/**
	 * @return the container_name
	 */
	public String getContainer_name() {
		return container_name;
	}
	/**
	 * @param container_name the container_name to set
	 */
	public void setContainer_name(String container_name) {
		this.container_name = container_name;
	}
	/**
	 * @return the operation_signature
	 */
	public BaseOperationSignature getOperation_signature() {
		return operation_signature;
	}
	/**
	 * @param operation_signature the operation_signature to set
	 */
	public void setOperation_signature(BaseOperationSignature operation_signature) {
		this.operation_signature = operation_signature;
	}
	
}
