package com.github.mariomatheus.doub.util;

import java.util.List;

public class DPage <T> {
	
	private List<T> content;
	private Integer totalElements;
	private Integer pageNumber;
	private Integer pageSize;
	
	public List<T> getContent() {
		return content;
	}
	
	public DPage<T> setContent(List<T> content) {
		this.content = content;
		return this;
	}
	
	public Integer getTotalElements() {
		return totalElements;
	}
	
	public DPage<T> setTotalElements(Integer totalElements) {
		this.totalElements = totalElements;
		return this;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public DPage<T> setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
		return this;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public DPage<T> setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
		return this;
	}

}
