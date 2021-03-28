package com.github.mariomatheus.doub.util;

import java.util.List;

/**
 * Page object managed by {@link com.github.mariomatheus.doub.Pagination}
 * with general pagination info and element content
 * 
 * @author mariomatheus
 *
 * @param <T> Pagination managed type
 */
public class DPage <T> {
	
	private List<T> content;
	private Integer totalElements;
	private Integer totalPages;
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

	public Integer getTotalPages() {
		return totalPages;
	}

	public DPage<T> setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
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
