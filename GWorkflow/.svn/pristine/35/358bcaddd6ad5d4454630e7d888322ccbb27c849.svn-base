package com.karimovceyhun.workflow.interfaces;

import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;

import com.karimovceyhun.workflow.data.Bean;

/**
 * IService is the general service interface
 * 
 * @author erdem
 * 
 */
public interface IService {
	/**
	 * Saves bean
	 * 
	 * @param <T>
	 * @param bean
	 */
	public <T extends Bean> void save(T bean);

	/**
	 * deletes bean
	 * 
	 * @param <T>
	 * @param bean
	 */
	public <T extends Bean> void delete(T bean);

	/**
	 * finds object
	 * 
	 * @param <T>
	 * @param object
	 * @param id
	 * @return
	 */
	public <T extends Bean> T find(Class<T> clazz, Long id);

	/**
	 * List
	 * 
	 * @param firstRow
	 * @param rowCount
	 * @param sortField
	 * @param sortAscending
	 * @return
	 */
	public <T extends Bean> List<T> list(Class<T> clazz, int firstRow,
			int rowCount, String sortField, boolean sortAscending);

	public <T extends Bean> int countList(Class<T> clazz);

	public <T extends Bean> List<T> list(Class<T> clazz, int firstRow,
			int rowCount, String sortField, boolean sortAscending,
			List<Criterion> whereClause);

	public <T extends Bean> int countList(Class<T> clazz,
			List<Criterion> whereClause);

	public <T extends Bean> List<T> list(Class<T> clazz, int firstRow,
			int rowCount, String sortField, boolean sortAscending,
			DetachedCriteria detachedCriteria);

	public <T extends Bean> int countList(Class<T> clazz,
			DetachedCriteria detachedCriteria);

	public <T extends Bean> List<T> list(Class<T> clazz);

	public <T extends Bean> List<T> list(Class<T> clazz, DetachedCriteria detachedCriteria);

	public <T extends Bean> List<T> list(Class<T> clazz, List<Criterion> whereClause);

}
