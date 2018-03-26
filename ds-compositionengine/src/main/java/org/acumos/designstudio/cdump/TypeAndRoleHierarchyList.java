/**
 * 
 */
package org.acumos.designstudio.cdump;

import java.io.Serializable;


public class TypeAndRoleHierarchyList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3267262096657636363L;
	
	private String name;
	private String role;
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the role
	 */
	public String getRole() {
		return role;
	}
	/**
	 * @param role the role to set
	 */
	public void setRole(String role) {
		this.role = role;
	}
	
	

}
