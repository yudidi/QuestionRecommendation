package com.sicnu.yudidi.utils.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Collections工具集.
 * ref: http://blog.csdn.net/u014001866/article/details/51568996
 */
public class CollectionsUtils {
	/**
	 * 返回a+b的新List.
	 */
	public static <T> List<T> union(final Collection<T> a, final Collection<T> b) {
		List<T> result = new ArrayList<T>(a);
		result.addAll(b);
		return result;
	}

	/**
	 * 返回a-b的新List.
	 */
	public static <T> List<T> subtract(final Collection<T> a, final Collection<T> b) {
		List<T> list = new ArrayList<T>(a);
		for (T element : b) {
			list.remove(element);
		}

		return list;
	}

	/**
	 * 返回a与b的交集的新List.
	 */
	public static <T> List<T> intersection(Collection<T> a, Collection<T> b) {
		List<T> list = new ArrayList<T>();

		for (T element : a) {
			if (b.contains(element)) {
				list.add(element);
			}
		}
		return list;
	}
	
	public static void main(String[] args){
		List<String> stringList1 = new ArrayList<String>();
		stringList1.add("111");
		stringList1.add("222");
		stringList1.add("333");
		
		List<String> stringList2 = new ArrayList<String>();
		stringList2.add("333");
		stringList2.add("444");
		stringList2.add("555");
		
		List<String> result = intersection(stringList1,stringList2);
		for(String a:result){
			System.out.println(a);
		}
	}
}

