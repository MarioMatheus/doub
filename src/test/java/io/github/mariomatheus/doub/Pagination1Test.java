package io.github.mariomatheus.doub;

import static io.github.mariomatheus.doub.util.Sub.queryList;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.github.mariomatheus.doub.util.DPage;
import junit.framework.TestCase;

public class Pagination1Test extends TestCase {
	
	List<String> uniqueList = Arrays.asList("Fulano", "Cicrano", "Beltrano");
	
	Pagination<String> pagination = new Pagination<String>()
			.put(uniqueList.size(), (offset, limit) -> queryList(offset, limit, uniqueList));
	
	
	private void defaultAssert(DPage<String> result) {
		assertEquals(result.getTotalElements().intValue(), uniqueList.size());
	}

	public void testPage1Size10() {
		DPage<String> result = pagination.paginate(0, 10);
		
		defaultAssert(result);
		assertEquals(result.getTotalPages().intValue(), 1);
		assertTrue(result.getContent().equals(uniqueList));
		assertEquals(result.getContent().size(), uniqueList.size());
	}
	
	public void testPage2Size10() {
		DPage<String> result = pagination.paginate(1, 10);
		
		defaultAssert(result);
		assertEquals(result.getTotalPages().intValue(), 1);
		assertTrue(result.getContent().equals(Collections.emptyList()));
		assertEquals(result.getContent().size(), 0);
	}
	
	public void testPage1Size2() {
		
		DPage<String> result = pagination.paginate(0, 2);
		
		defaultAssert(result);
		assertEquals(result.getTotalPages().intValue(), 2);
		assertTrue(result.getContent().equals(uniqueList.subList(0, 2)));
		assertEquals(result.getContent().size(), 2);
	}
	
	public void testPage2Size2() {
		DPage<String> result = pagination.paginate(1, 2);
		
		defaultAssert(result);
		assertEquals(result.getTotalPages().intValue(), 2);
		assertTrue(result.getContent().equals(uniqueList.subList(2, 3)));
		assertEquals(result.getContent().size(), 1);
	}
	
	public void testPage1Size1() {
		DPage<String> result = pagination.paginate(0, 1);
		
		defaultAssert(result);
		assertEquals(result.getTotalPages().intValue(), 3);
		assertTrue(result.getContent().equals(uniqueList.subList(0, 1)));
		assertEquals(result.getContent().size(), 1);
	}
	
	public void testPage2Size1() {
		DPage<String> result = pagination.paginate(1, 1);
		
		defaultAssert(result);
		assertEquals(result.getTotalPages().intValue(), 3);
		assertTrue(result.getContent().equals(uniqueList.subList(1, 2)));
		assertEquals(result.getContent().size(), 1);
	}
	
	public void testPage3Size2() {
		DPage<String> result = pagination.paginate(2, 1);
		
		defaultAssert(result);
		assertEquals(result.getTotalPages().intValue(), 3);
		assertTrue(result.getContent().equals(uniqueList.subList(2, 3)));
		assertEquals(result.getContent().size(), 1);
	}

}
