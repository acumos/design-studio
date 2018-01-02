/**
 * 
 */
package org.acumos.designstudio.toscagenerator.vo.protobuf;

import java.io.Serializable;
import java.util.List;

/**
 * @author AB00343130
 *
 */
public class ComplexType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8550690156793239836L;
	
	private String messageName ="";
	private List<MessageargumentList> messageargumentList;
	/**
	 * @return the messageName
	 */
	public String getMessageName() {
		return messageName;
	}
	/**
	 * @param messageName the messageName to set
	 */
	public void setMessageName(String messageName) {
		this.messageName = messageName;
	}
	/**
	 * @return the messageargumentList
	 */
	public List<MessageargumentList> getMessageargumentList() {
		return messageargumentList;
	}
	/**
	 * @param messageargumentList the messageargumentList to set
	 */
	public void setMessageargumentList(List<MessageargumentList> messageargumentList) {
		this.messageargumentList = messageargumentList;
	}
	

}
