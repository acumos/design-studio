/**
 * 
 */
package org.acumos.designstudio.cdump;

import java.io.Serializable;


public class OutputField implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4254596034653861099L;
	
	private String tag;
	private String name;
	private TypeAndRoleHierarchyList[] type_and_role_hierarchy_list;
	
	/**
	 * @return the tag
	 */
	public String getTag() {
		return tag;
	}
	/**
	 * @param tag the tag to set
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}
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
	 * @return the type_and_role_hierarchy_list
	 */
	public TypeAndRoleHierarchyList[] getType_and_role_hierarchy_list() {
		return type_and_role_hierarchy_list;
	}
	/**
	 * @param type_and_role_hierarchy_list the type_and_role_hierarchy_list to set
	 */
	public void setType_and_role_hierarchy_list(TypeAndRoleHierarchyList[] type_and_role_hierarchy_list) {
		this.type_and_role_hierarchy_list = type_and_role_hierarchy_list;
	}
	
	
	

}
