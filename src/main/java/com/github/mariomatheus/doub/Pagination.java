package com.github.mariomatheus.doub;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

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
		Holder<Integer> totalElementsHolder = new Holder<>(0);

		List<T> content = listsQueries.stream()
				.map(queryParam -> process(pageNumber, pageSize, pageFillHolder, totalElementsHolder, queryParam))
				.flatMap(Collection::stream)
				.collect(Collectors.toList());

		return new DPage<T>()
				.setContent(content)
				.setPageNumber(pageNumber)
				.setPageSize(pageSize)
				.setTotalElements(totalElementsHolder.value)
				.setTotalPages((int) Math.ceil((double) totalElementsHolder.value / pageSize));
	}

	private List<T> process(Integer pageNumber, Integer pageSize, Holder<Integer> pageFillHolder,
			Holder<Integer> totalElementsHolder, Pair<Integer, BiFunction<Integer, Integer, List<T>>> queryParam) {

		Integer listCount = queryParam.first();
		Integer pageOffset = pageNumber * pageSize;
		Integer pageRemaining = pageSize - pageFillHolder.value;
		Integer previousListCount = totalElementsHolder.value;

		totalElementsHolder.value += listCount;

		if (pageRemaining == 0 || pageOffset > listCount + previousListCount) {
			return Collections.<T>emptyList();
		}

		Integer offset = extractOffset(pageNumber, pageSize, previousListCount);
		List<T> listContent = queryParam.second().apply(offset, pageRemaining);

		pageFillHolder.value += listContent.size();

		return listContent;
	}

	static Integer extractOffset(Integer pageNumber, Integer pageSize, Integer previousListCount) {
		Integer diff = previousListCount <= pageSize ? pageSize - previousListCount : pageSize - (previousListCount % pageSize);
		diff = diff == pageSize ? 0 : diff;
		Integer finalPreviousPage = (int) Math.ceil((double) previousListCount / pageSize);
		Integer offset = finalPreviousPage >= pageNumber + 1 ? 0 : diff + (pageNumber - finalPreviousPage) * pageSize;
		return offset;
	}

	public Pagination<T> put(Integer listCount, BiFunction<Integer, Integer, List<T>> mapper) {
		listsQueries.add(Pair.of(listCount, mapper));
		return this;
	}

}
