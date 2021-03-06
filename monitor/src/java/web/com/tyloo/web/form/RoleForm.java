/* 
 * @(#)LoginForm.java    Created on 2008-1-2
 * Copyright (c) 2008 tunny, Inc. All rights reserved.
 * $Header: /usr/local/cvsroot/jiande/src/java/web/com/renhenet/web/form/RoleForm.java,v 1.1.1.1 2008/03/26 04:08:28 luoxn Exp $
 */
package com.tyloo.web.form;

import com.tyloo.fw.waf.BasePOForm;

public class RoleForm extends BasePOForm {
	private static final long serialVersionUID = -332539494050222597L;

	private String name;

	private int groupPri;

	private String salseType;

	private String memo;

	public int getGroupPri() {
		return groupPri;
	}

	public void setGroupPri(int groupPri) {
		this.groupPri = groupPri;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSalseType() {
		return salseType;
	}

	public void setSalseType(String salseType) {
		this.salseType = salseType;
	}

}
