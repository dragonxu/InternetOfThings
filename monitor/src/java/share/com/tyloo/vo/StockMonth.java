/* 
 * @(#)StockMonth.java    Created on 2008-1-11
 * Copyright (c) 2008 tunny, Inc. All rights reserved.
 * $Header: /usr/local/cvsroot/tyloo/src/java/share/com/renhenet/vo/StockMonth.java,v 1.1.1.1 2008/01/22 05:25:25 luoxn Exp $
 */
package com.tyloo.vo;

public class StockMonth {
	private int id;

	private String name;// ��Ʒ����

	private String units;// ������λ

	private int superMonth;// ���¿��

	private int todayMonthIn;// �������

	private int todayMonthOut;// ���³���

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSuperMonth() {
		return superMonth;
	}

	public void setSuperMonth(int superMonth) {
		this.superMonth = superMonth;
	}

	public int getTodayMonthIn() {
		return todayMonthIn;
	}

	public void setTodayMonthIn(int todayMonthIn) {
		this.todayMonthIn = todayMonthIn;
	}

	public int getTodayMonthOut() {
		return todayMonthOut;
	}

	public void setTodayMonthOut(int todayMonthOut) {
		this.todayMonthOut = todayMonthOut;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

}
