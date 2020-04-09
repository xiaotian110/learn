package com.yun.learn.util;

import java.util.List;

public class DatatablePage {
	private int draw;
	private long recordsTotal;
	private long recordsFiltered;
	private List data;
	private String error;
	
	public DatatablePage(int draw,long recordsTotal,long recordsFiltered,List data) {
		this.draw=draw;
		this.recordsTotal=recordsTotal;
		this.recordsFiltered=recordsFiltered;
		this.data=data;
	}
	public DatatablePage(int draw,String error) {
		this.error=error;
		this.draw=draw;
	}
	public int getDraw() {
		return draw;
	}
	public void setDraw(int draw) {
		this.draw = draw;
	}
	public long getRecordsTotal() {
		return recordsTotal;
	}
	public void setRecordsTotal(long recordsTotal) {
		this.recordsTotal = recordsTotal;
	}
	public long getRecordsFiltered() {
		return recordsFiltered;
	}
	public void setRecordsFiltered(long recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}
	public List getData() {
		return data;
	}
	public void setData(List data) {
		this.data = data;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	

}
