/*
 * WARNING: DO NOT EDIT THIS FILE. This is a generated file that is synchronized
 * by MyEclipse Hibernate tool integration.
 *
 * Created Fri Aug 11 16:05:29 CST 2006 by MyEclipse Hibernate Tool.
 */
package com.tyloo.po;

import java.io.Serializable;
import java.util.Date;

import com.tyloo.fw.orm.IdPersistent;

public class RoleModule extends IdPersistent implements Serializable {

	private static final long serialVersionUID = 8751679588083303161L;

	private Integer id;

	private Integer moduleId;

	private Integer roleId;

	private String pri;

	private Date timeCreate;

	private Date timeModified;

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

	@Override
	public Date getTimeCreate() {
		return timeCreate;
	}

	@Override
	public void setTimeCreate(Date timeCreate) {
		this.timeCreate = timeCreate;
	}

	@Override
	public Date getTimeModified() {
		return timeModified;
	}

	@Override
	public void setTimeModified(Date timeModified) {
		this.timeModified = timeModified;
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

}
