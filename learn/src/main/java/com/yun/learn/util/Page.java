package com.yun.learn.util;


import java.util.List;

public final class Page<T> {

	public static final int DEFAULT_PAGE_SIZE = 10;

	//总记录数
		private int count;
		//每页条数
		private int pageSize;
		//总页数
		private int pageCount;
		//当前页数
		private int currentPage;
		//上一页
		private int prevPage;
		//下一页
		private int nextPage;
		//起始记录数
		private int startRecord;
		//结束记录数
		private int endRecord;
		private List<T> list;

	public Page(String current, int count, String pageSize) {
		this.count = count;
		init(current, pageSize);
	}

	private void init(String current, String size) {
		//��ȡÿҳ��ʾ������
		initPageSize(size);
		//��ȡ��ҳ��
		initPageCount();
		//��ȡ��ǰҳ��
		initCurrentPage(current);
		//��ȡ��һҳ
		initPreviousPage();
		//��ȡ��һҳ
		initNextPage();
		//��ȡ��ʼ��¼��
		initStartRecord();
		//��ȡ������¼��
		initEndRecord();
	}

	private void initPageSize(String size) {
		if (size == null || size.trim().isEmpty()) {
			pageSize = DEFAULT_PAGE_SIZE;
			return;
		}
		try {
			pageSize = Integer.parseInt(size);
		} catch (Exception e) {
			pageSize = DEFAULT_PAGE_SIZE;
		}
	}

	private void initEndRecord() {
		endRecord = startRecord + pageSize - 1;
		if (endRecord > count) {
			endRecord = count;
		}
	}

	private void initStartRecord() {
		startRecord = (currentPage - 1) * pageSize;
		if (startRecord < 0) {
			startRecord = 0;
		}
		if (startRecord > count) {
			startRecord = count;
		}
	}

	private void initNextPage() {
		nextPage = currentPage + 1;
		if (nextPage > pageCount) {
			nextPage = pageCount;
		}
	}

	private void initPreviousPage() {
		prevPage = currentPage - 1;
		if (prevPage < 1) {
			prevPage = 1;
		}
	}

	private void initCurrentPage(String current) {
		if (current == null || current.trim().isEmpty()) {
			currentPage = 1;
			return;
		}
		try {
			currentPage = Integer.parseInt(current);
		} catch (Exception e) {
			currentPage = 1;
		}
		if (currentPage < 1) {
			currentPage = 1;
			return;
		}
		if (currentPage > pageCount) {
			currentPage = pageCount;
		}
	}

	private void initPageCount() {
		pageCount = count / pageSize;
		if (count % pageSize != 0) {
			pageCount++;
		}
	}

	public int getCount() {
		return count;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getPageCount() {
		return pageCount;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public int getNextPage() {
		return nextPage;
	}

	public int getStartRecord() {
		return startRecord;
	}

	public int getEndRecord() {
		return endRecord;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public int getPrevPage() {
		return prevPage;
	}
	
}

