/**
 * 
 */
package org.acumos.designstudio.ce.vo.blueprint;

import java.io.Serializable;
import java.util.List;

public class OperationSignatureList implements Serializable {

	private static final long serialVersionUID = 7663725728196793021L;

	private NodeOperationSignature operation_signature;
	private List<Container> connected_to;
	/**
	 * @return the operation_signature
	 */
	public NodeOperationSignature getOperation_signature() {
		return operation_signature;
	}
	/**
	 * @param operation_signature the operation_signature to set
	 */
	public void setOperation_signature(NodeOperationSignature operation_signature) {
		this.operation_signature = operation_signature;
	}
	/**
	 * @return the connected_to
	 */
	public List<Container> getConnected_to() {
		return connected_to;
	}
	/**
	 * @param connected_to the connected_to to set
	 */
	public void setConnected_to(List<Container> connected_to) {
		this.connected_to = connected_to;
	} 
	
	
	
}
