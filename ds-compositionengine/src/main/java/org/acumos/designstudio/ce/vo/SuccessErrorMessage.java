package org.acumos.designstudio.ce.vo;

import java.io.Serializable;

public class SuccessErrorMessage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -552558223759520244L;
    private String success = "";
    private String errorMessage = "";
    
    public SuccessErrorMessage() {
	}
	public SuccessErrorMessage(String success, String errorMessage) {
		this.success = success;
		this.errorMessage = errorMessage;
	}
	/**
	 * @return the success
	 */
	public String getSuccess() {
		return success;
	}
	/**
	 * @param success the success to set
	 */
	public void setSuccess(String success) {
		this.success = success;
	}
	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}
	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
    
    
}
