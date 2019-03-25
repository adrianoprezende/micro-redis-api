package com.bankslips.adapter;

import java.util.List;

/**
 * The Interface IAdapter.
 * T is the output and K the input
 *
 * @param <T> the generic type
 * @param <K> the key type
 */
public interface IAdapter<T, K> {

	/**
	 * To entity.
	 *
	 * @param dto the dto
	 * @return the t
	 */
	public T toEntity(K dto);

	/**
	 * To entity list.
	 *
	 * @param dtoList the dto list
	 * @return the list
	 */
	public List<T> toEntityList(List<K> dtoList);

}
