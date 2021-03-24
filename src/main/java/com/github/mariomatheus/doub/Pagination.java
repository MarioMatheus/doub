package com.github.mariomatheus.doub;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.xml.ws.Holder;

import com.github.mariomatheus.doub.util.DPage;
import com.github.mariomatheus.doub.util.Pair;

public final class Pagination<T> {

	private List<Pair<Integer, BiFunction<Integer, Integer, List<T>>>> listsQueries;

	public Pagination() {
		this.listsQueries = new ArrayList<>();
	}

	public DPage<T> paginate(Integer pageNumber, Integer pageSize) {
		Holder<Integer> pageFillHolder = new Holder<>(0);
		List<Integer> totalElements = new ArrayList<>();

		List<T> content = IntStream
				.range(0, listsQueries.size())
				.mapToObj(index -> process(pageNumber, pageSize, pageFillHolder, totalElements, index))
				.flatMap(Collection::stream).collect(Collectors.toList());

		return new DPage<T>()
				.setContent(content)
				.setPageNumber(pageNumber)
				.setPageSize(pageSize)
				.setTotalElements(totalElements.stream().reduce(Integer::sum).orElse(0));
				// TODO: totalPages - (int) Math.ceil((double) totalElements / size)
	}

	private List<T> process(
			Integer pageNumber, Integer pageSize,
			Holder<Integer> pageFillHolder, List<Integer> totalElements, Integer index) {
		
		Pair<Integer, BiFunction<Integer, Integer, List<T>>> queryParam = listsQueries.get(index);

		Integer listCount = queryParam.first();
		Integer pageOffset = pageNumber * pageSize;
		Integer pageRemaining = pageSize - pageFillHolder.value;

		totalElements.add(listCount);
		
		Integer previousListCount = IntStream.range(0, index)
				.map(i -> totalElements.get(i))
				.reduce(Integer::sum)
				.orElse(0);

		if (pageRemaining == 0 || pageOffset > listCount + previousListCount) {
			return Collections.<T>emptyList();
		}
		
		Integer diff = previousListCount <= pageSize ? pageSize - previousListCount : pageSize - (previousListCount % pageSize);
		diff = diff == pageSize ? 0 : diff;
		Integer finalPreviousPage = (int) Math.ceil((double) previousListCount / pageSize);
		Integer offset = finalPreviousPage >= pageNumber + 1 ? 0 : diff + (pageNumber - finalPreviousPage) * pageSize;

		List<T> listContent = queryParam.second().apply(offset, pageRemaining);

		pageFillHolder.value += listContent.size();

		return listContent;
	}

	public Pagination<T> put(Integer listCount, BiFunction<Integer, Integer, List<T>> mapper) {
		listsQueries.add(Pair.of(listCount, mapper));
		return this;
	}

}
