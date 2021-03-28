package io.github.mariomatheus.doub;

import junit.framework.TestCase;

public class PaginationOffsetTest extends TestCase {
	
	public void testExtractOffsetWithPreviousCountListLessThanPageSize() {
		assertEquals(Pagination.extractOffset(0, 0, 0).intValue(), 0);
		assertEquals(Pagination.extractOffset(0, 5, 2).intValue(), 0);
		assertEquals(Pagination.extractOffset(0, 5, 3).intValue(), 0);
		assertEquals(Pagination.extractOffset(0, 5, 4).intValue(), 0);
		
		assertEquals(Pagination.extractOffset(0, 4, 2).intValue(), 0);
		assertEquals(Pagination.extractOffset(1, 4, 2).intValue(), 2);
		assertEquals(Pagination.extractOffset(2, 4, 2).intValue(), 2 + 4);
		assertEquals(Pagination.extractOffset(3, 4, 2).intValue(), 2 + 4 + 4);
	}
	
	public void testExtractOffsetWithPreviousCountEqualPageSize() {
		assertEquals(Pagination.extractOffset(0, 0, 0).intValue(), 0);
		assertEquals(Pagination.extractOffset(1, 1, 1).intValue(), 0);
		assertEquals(Pagination.extractOffset(1, 2, 2).intValue(), 0);
		assertEquals(Pagination.extractOffset(1, 3, 3).intValue(), 0);
		
		assertEquals(Pagination.extractOffset(0, 4, 4).intValue(), 0);
		assertEquals(Pagination.extractOffset(1, 4, 4).intValue(), 0);
		assertEquals(Pagination.extractOffset(2, 4, 4).intValue(), 4);
		assertEquals(Pagination.extractOffset(3, 4, 4).intValue(), 4 + 4);
	}
	
	public void testExtractOffsetWithPreviousCountListGreaterThanPageSize() {
		assertEquals(Pagination.extractOffset(0, 5, 6).intValue(), 0);
		assertEquals(Pagination.extractOffset(0, 5, 10).intValue(), 0);
		assertEquals(Pagination.extractOffset(0, 5, 20).intValue(), 0);
		
		assertEquals(Pagination.extractOffset(0, 4, 5).intValue(), 0);
		assertEquals(Pagination.extractOffset(1, 4, 5).intValue(), 0);
		assertEquals(Pagination.extractOffset(2, 4, 5).intValue(), 3);
		assertEquals(Pagination.extractOffset(3, 4, 5).intValue(), 3 + 4);
	}
	
}
