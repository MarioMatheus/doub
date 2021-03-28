package io.github.mariomatheus.doub;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import javax.xml.ws.Holder;

import io.github.mariomatheus.doub.util.DPage;
import io.github.mariomatheus.doub.util.Pair;

/**
 * A multiplexed pagination builder.
 * 
 * <p>
 * This class allows you to build an object pager with different origins. The
 * main functionality is to provide a common pagination between the objects
 * managed by different processes, distributing the requisition of resources in
 * an intelligent way.
 * 
 * <p>
 * To build the pagination, it is necessary to provide two resources to the
 * {@code Pagination} object through the {@code put} method, the total number of
 * elements managed by the process and a function to request these elements
 * through the {@code limit} and {@code offset} parameters that will be
 * calculated internally and used as arguments of this function for the data
 * recovery.
 * 
 * <p>
 * After the construction of the {@code Pagination} object, the paginate
 * function is used to request the objects in a unified and transparent way.
 * 
 * <pre>
 * <code>
 * new Pagination()
 *   .put(fooTotalElements, (offset, limit) -&#62; repository.findFooByOffsetAndLimit(offset, limit))
 *   .put(barTotalElements, (offset, limit) -&#62; service.requestBarByOffsetAndLimit(offset, limit))
 *   .paginate(0, 10)
 *   .getContent();
 * </code>
 * </pre>
 * 
 * @author mariomatheus
 *
 * @param <T> Pagination managed type
 */
public final class Pagination<T> {

	private List<Pair<Integer, BiFunction<Integer, Integer, List<T>>>> listsQueries;

	/**
	 * Create a new {@code Pagination} object
	 */
	public Pagination() {
		this.listsQueries = new ArrayList<>();
	}

	/**
	 * Add a resource to be managed by {@code Pagination} object
	 * 
	 * <p>
	 * For pagination to be managed transparently and correctly, it is necessary to
	 * have two essential parameters, the total number of elements that will be
	 * paginated and a function to recover these elements. The function provides two
	 * arguments for the retrieval of the elements, the {@code offset} which
	 * specifies the starting position in the specific list of elements and the
	 * {@code limit}, which specifies the maximum number of elements to be
	 * retrieved.
	 * 
	 * <pre>
	 * <code>
	 * new Pagination()
	 *   .put(fooTotalElements, (offset, limit) -&#62; repository.findFooByOffsetAndLimit(offset, limit))
	 *   .put(barTotalElements, (offset, limit) -&#62; service.requestBarByOffsetAndLimit(offset, limit));
	 * </code>
	 * </pre>
	 * 
	 * @param listCount Total number of elements that will be paginated
	 * @param mapper    Function to recover the elements. The function provides two
	 *                  arguments for the retrieval of the elements, the
	 *                  {@code offset} which specifies the starting position in the
	 *                  specific list of elements and the {@code limit}, which
	 *                  specifies the maximum number of elements to be retrieved.
	 * 
	 * @return The managed {@code Pagination} object
	 */
	public Pagination<T> put(Integer listCount, BiFunction<Integer, Integer, List<T>> mapper) {
		listsQueries.add(Pair.of(listCount, mapper));
		return this;
	}

	/**
	 * Retrieve page from managed {@code Pagination} object
	 * 
	 * @param pageNumber Number of page
	 * @param pageSize   Size of page
	 * @return {@link io.github.mariomatheus.doub.util.DPage} object with page data
	 */
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
		Integer diff = previousListCount <= pageSize
				? pageSize - previousListCount
				: pageSize - (previousListCount % pageSize);

		diff = diff == pageSize ? 0 : diff;

		Integer finalPreviousPage = (int) Math.ceil((double) previousListCount / pageSize);
		Integer offset = finalPreviousPage >= pageNumber + 1 ? 0 : diff + (pageNumber - finalPreviousPage) * pageSize;

		return offset;
	}

}
