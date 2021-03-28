package com.github.mariomatheus.doub.util;

import java.util.List;

public final class Sub {
	public static <T> List<T> queryList(Integer offset, Integer limit, List<T> list) {
		return list.subList(offset, offset + limit > list.size() ? list.size() : offset + limit);
	}
}
