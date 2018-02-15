/**
 * 
 */
package org.acumos.designstudio.ce.vo.blueprint;

/**
 * @author RP00490596
 *
 */
public class NodeOperationSignature extends BaseOperationSignature {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7867606266539774888L;

	private String input_message_name;
	private String output_message_name;
	
	/**
	 * @return the input_message_name
	 */
	public String getInput_message_name() {
		return input_message_name;
	}
	/**
	 * @param input_message_name the input_message_name to set
	 */
	public void setInput_message_name(String input_message_name) {
		this.input_message_name = input_message_name;
	}
	/**
	 * @return the output_message_name
	 */
	public String getOutput_message_name() {
		return output_message_name;
	}
	/**
	 * @param output_message_name the output_message_name to set
	 */
	public void setOutput_message_name(String output_message_name) {
		this.output_message_name = output_message_name;
	}
	
	
	
}
