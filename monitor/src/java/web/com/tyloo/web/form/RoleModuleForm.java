/* 
 * @(#)LoginForm.java    Created on 2008-1-2
 * Copyright (c) 2008 tunny, Inc. All rights reserved.
 * $Header: /usr/local/cvsroot/jiande/src/java/web/com/renhenet/web/form/RoleModuleForm.java,v 1.1.1.1 2008/03/26 04:08:28 luoxn Exp $
 */
package com.tyloo.web.form;

import com.tyloo.fw.waf.BasePOForm;

public class RoleModuleForm extends BasePOForm {
	private static final long serialVersionUID = -332539494050222597L;

	private Integer moduleId;

	private Integer roleId;

	private String pri;

	public Integer getModuleId() {
		return moduleId;
	}

	public void setModuleId(Integer moduleId) {
		this.moduleId = moduleId;
	}

	public String getPri() {
		return pri;
	}

	public void setPri(String pri) {
		this.pri = pri;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
}
