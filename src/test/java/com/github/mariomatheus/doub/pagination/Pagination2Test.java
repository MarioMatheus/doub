package com.github.mariomatheus.doub.pagination;

import static com.github.mariomatheus.doub.pagination.util.Sub.queryList;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.github.mariomatheus.doub.Pagination;
import com.github.mariomatheus.doub.util.DPage;

import junit.framework.TestCase;

public class Pagination2Test extends TestCase {
	
	List<String> list1 = Arrays.asList("Fulano", "Cicrano", "Beltrano");
	List<String> list2 = Arrays.asList("Foo", "Bar", "Boya");
	
	List<String> mergedList = Stream
			.of(list1, list2)
			.flatMap(Collection::stream)
			.collect(Collectors.toList());
	
	Pagination<String> pagination = new Pagination<String>()
			.put(list1.size(), (offset, limit) -> queryList(offset, limit, list1))
			.put(list2.size(), (offset, limit) -> queryList(offset, limit, list2));
	
	
	private void defaultAssert(DPage<String> result) {
		assertEquals(result.getTotalElements().intValue(), mergedList.size());
	}
	
	public void testPage1Size10() {
		DPage<String> result = pagination.paginate(0, 10);
		
		defaultAssert(result);
		assertTrue(result.getContent().equals(mergedList));
		assertEquals(result.getContent().size(), mergedList.size());
	}
	
	public void testPage1Size4() {
		DPage<String> result = pagination.paginate(0, 4);
		
		defaultAssert(result);
		assertTrue(result.getContent().equals(mergedList.subList(0, 4)));
		assertEquals(result.getContent().size(), 4);
	}
	
	public void testPage2Size4() {
		DPage<String> result = pagination.paginate(1, 4);
		
		defaultAssert(result);
		assertTrue(result.getContent().equals(mergedList.subList(4, 6)));
		assertEquals(result.getContent().size(), 2);
	}
	
}
