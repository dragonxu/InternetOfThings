package com.tyloo.web;
/**
 * @ClassName: SelectObject
 * @Description: TODO(����ҳ������ѡ������)
 * @author gavin.shao
 * @date 2011-3-31 ����09:56:57
 * 
 */
public class SelectObject {
 private String id;
 private String name;
 private String manager;

public SelectObject() {
	 
 }

 public SelectObject(String id,String name) {
	 this.id = id;
	 this.name = name;
 }

public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}

public String getManager() {
	return manager;
}
public void setManager(String manager) {
	this.manager = manager;
}
 
}
