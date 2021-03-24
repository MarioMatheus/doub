package com.github.mariomatheus.doub.pagination;

import static com.github.mariomatheus.doub.pagination.util.Sub.queryList;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.github.mariomatheus.doub.Pagination;
import com.github.mariomatheus.doub.util.DPage;

import junit.framework.TestCase;

public class Pagination3Page extends TestCase {

	List<String> list1 = Arrays.asList("Fulano", "Cicrano");
	List<String> list2 = Arrays.asList("Beltrano");
	List<String> list3 = Arrays.asList("Foo", "Bar", "Boya");
	
	List<String> mergedList = Stream
			.of(list1, list2, list3)
			.flatMap(Collection::stream)
			.collect(Collectors.toList());
	
	Pagination<String> pagination = new Pagination<String>()
			.put(list1.size(), (offset, limit) -> queryList(offset, limit, list1))
			.put(list2.size(), (offset, limit) -> queryList(offset, limit, list2))
			.put(list3.size(), (offset, limit) -> queryList(offset, limit, list3));


	private void defaultAssert(DPage<String> result) {
		assertEquals(result.getTotalElements().intValue(), mergedList.size());
	}
	
	public void testPage1Size10() {
		DPage<String> result = pagination.paginate(0, 10);
		
		defaultAssert(result);
		assertTrue(result.getContent().equals(mergedList));
		assertEquals(result.getContent().size(), mergedList.size());
	}
	
	public void testPage2Size10() {
		DPage<String> result = pagination.paginate(1, 10);
		
		defaultAssert(result);
		assertTrue(result.getContent().equals(Collections.emptyList()));
		assertEquals(result.getContent().size(), 0);
	}
	
	public void testPage1Size5() {
		DPage<String> result = pagination.paginate(0, 5);
		
		defaultAssert(result);
		assertTrue(result.getContent().equals(mergedList.subList(0, 5)));
		assertEquals(result.getContent().size(), 5);
	}
	
	public void testPage2Size5() {
		DPage<String> result = pagination.paginate(1, 5);
		
		defaultAssert(result);
		assertTrue(result.getContent().equals(mergedList.subList(5, 6)));
		assertEquals(result.getContent().size(), 1);
	}

}
