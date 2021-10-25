package com.ms.core.common.dao.impl;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import com.ms.core.common.dao.AdvancedSearchRepository;

/**
 * The Class AdvancedSearchRepositoryImpl.
 * 
 * P.E. Se puede acceder a esta interfaz en http://localhost:8083/api/v1/product/search?…   (donde los puntos suspensivos se sustituyen por los parámetros que se indican después).
 * 
 * Para ello, se pueden concatenar una serie de parámetros variables con & de la siguiente manera:
 * 
 * page=X&size=Y para indicar mediante X e Y el número de página y el tamaño de la misma
 * 
 * sort=-name,id ordena mediante este comando primero por ‘name’ de forma descendente y después por ‘id’ de forma ascendente
 * 
 * fields=id,name indica que queremos recuperar solamente ‘id’ y ‘name’, los demás parámetros vendrán a null o no vendrán si se configura con JsonIgnore(Ignore.NULL)
 * 
 * filter=id:1:4,userId>5,name~product,userId!7 para en este caso poder incluir múltiples filtros mediante el uso de ‘coma’
 */
public class AdvancedSearchRepositoryImpl implements AdvancedSearchRepository {

	private static final String INCLUDE_FIELD = ":1";

	private static final Logger logger = LoggerFactory.getLogger(AdvancedSearchRepository.class);
	
	private final ConversionService conversionService;
	
	@PersistenceContext
	protected EntityManager entityManager;

	public AdvancedSearchRepositoryImpl(ConversionService conversionService) {
		super();
		this.conversionService = conversionService;
	}

	/**
	 * Method to find entities by params
	 */
	public <T> Page<T> findAllByParams(final Map<String, Object> parameters, Class<T> entityClass) {

		TypedQuery<Long> queryCount = this.createQueryCountFromParameters(parameters, entityClass);
		Long number = queryCount.getSingleResult();

		if (number > 0) {
			TypedQuery<Object[]> query = this.createBasicQueryFromParameters(parameters, entityClass);

			final List<T> list = (List<T>) query.getResultList();

			Integer page = getPage(parameters);
			Integer size = getSize(parameters);

			List<Order> orders = processOrder(parameters);
			Sort sort = null;
			if(orders!= null && !orders.isEmpty()){
				sort = new Sort(orders);
			}

			PageRequest pageRequest = new PageRequest(page, size, sort);

			return new PageImpl<>(list, pageRequest, number);
		}

		return new PageImpl<>(new ArrayList<>());
	}

	private TypedQuery<Long> createQueryCountFromParameters(Map<String, Object> parameters, Class<?> entityClass) {
		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<?> root = criteria.from( entityClass );
		Expression<Long> countExpression = builder.count(root.get("id"));
		criteria.select(countExpression);		
	    try {
			getFilter(entityClass, parameters, builder, criteria, root);
		} catch (NoSuchFieldException | SecurityException e) {
			logger.error("Error al obtener el filtro de query", e);
		}		
		return this.entityManager.createQuery(criteria);
	}

	private TypedQuery<Object[]> createBasicQueryFromParameters(final Map<String, Object> parameters, Class<?> entityClass) {
		
				
		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();		
		CriteriaQuery<Object[]> criteria = builder.createQuery( Object[].class );		
		Root<?> root = criteria.from( entityClass );
				
		if (parameters.containsKey(AdvancedSearchRepository.FIELDS_PARAM)) {
			final String[] fields = parameters.get(AdvancedSearchRepository.FIELDS_PARAM).toString()
					.split(AdvancedSearchRepository.PARAM_SEPARATOR);

			List<Selection<?>> selection = new ArrayList<>();
			for (final String field : fields) {
				Path<Object> f = root.get(field);
				if(f != null)  {
					selection.add(f);
				}
			}
			criteria.select( builder.array( (Selection<?>[]) selection.toArray() ) );
		}
		
	    try {
			getFilter(entityClass, parameters, builder, criteria, root);
		} catch (NoSuchFieldException | SecurityException e) {
			logger.error("Error al obtener el filtro de query", e);
		}

		if (parameters.containsKey(AdvancedSearchRepository.SORT_PARAM)) {
			final String[] sortParameters = parameters.get(AdvancedSearchRepository.SORT_PARAM).toString()
					.split(AdvancedSearchRepository.PARAM_SEPARATOR);

			List<javax.persistence.criteria.Order> order = new ArrayList<>();
			for (final String shortParam : sortParameters) {
				if (shortParam.startsWith("-")) {
					order.add( builder.desc(root.get(shortParam.substring(1))) );
				} else {
					order.add( builder.asc(root.get(shortParam)) );
				}
			}
			criteria.orderBy( (javax.persistence.criteria.Order[]) order.toArray() );
		}
	    
	    TypedQuery<Object[]> query = this.entityManager.createQuery(criteria);	  
	    
		if (parameters.containsKey(AdvancedSearchRepository.PAGE_PARAM)
				|| parameters.containsKey(AdvancedSearchRepository.SIZE_PARAM)) {
			Integer page = AdvancedSearchRepository.PAGE_DEFAULT;
			Integer size = AdvancedSearchRepository.SIZE_DEFAULT;
			if (parameters.get(AdvancedSearchRepository.PAGE_PARAM) != null) {
				page = Integer.valueOf(parameters.get(AdvancedSearchRepository.PAGE_PARAM).toString());
			}
			if (parameters.get(AdvancedSearchRepository.SIZE_PARAM) != null) {
				size = Integer.valueOf(parameters.get(AdvancedSearchRepository.SIZE_PARAM).toString());
			}

			query.setFirstResult((page) * size);
			query.setMaxResults(size);
		}

		return query;
	}

	private void getFilter(Class<?> entityClass, Map<String, Object> parameters, CriteriaBuilder builder, CriteriaQuery<?> criteria, Root<?> root) throws NoSuchFieldException, SecurityException {
		if (parameters.containsKey(AdvancedSearchRepository.FILTER_PARAM)) {

			final String[] conditions = parameters.get(AdvancedSearchRepository.FILTER_PARAM).toString()
					.split(AdvancedSearchRepository.PARAM_SEPARATOR);
			
			Predicate predicate = null;
			for (String condition : conditions) {
				String[] params = condition.split(AdvancedSearchRepository.EQUALS_PARAM + "|"
						+ AdvancedSearchRepository.NEGATION_PARAM + "|"
						+ AdvancedSearchRepository.GREATER_THAN_PARAM + "|"
						+ AdvancedSearchRepository.LESS_THAN_PARAM + "|"
						+ AdvancedSearchRepository.LIKE_PARAM);

				Predicate p = null;
				String name = params[0];
				
				if (condition.contains(AdvancedSearchRepository.EQUALS_PARAM)) {
					if (params.length == 2) {
						Object value = getValue(entityClass, name, params[1]);
						p = builder.equal(root.get(name), value);

					} else {
						Object[] entities = Arrays.copyOfRange(params, 1, params.length);
						In<Object> inClause = builder.in(root.get(name));
						for (Object value : entities) {
							inClause.value(getValue(entityClass, name, value));
						}
						p = inClause;
					}
					
				} else if (condition.contains(AdvancedSearchRepository.NEGATION_PARAM)) {
					Object value = getValue(entityClass, name, params[1]);
					p = builder.notEqual(root.get(name), value);
					
				} else if (condition.contains(AdvancedSearchRepository.GREATER_THAN_PARAM)) {
					final Field field = entityClass.getDeclaredField(name);
					Class<?> type = field.getType();
				    Expression<?> parameter = builder.parameter(type, name);					  
					p = builder.greaterThan(root.get(name), (Expression<Comparable>)parameter);
					
				} else if (condition.contains(AdvancedSearchRepository.LESS_THAN_PARAM)) {
					final Field field = entityClass.getDeclaredField(name);
					Class<?> type = field.getType();
				    Expression<?> parameter = builder.parameter(type, name);					  
					p = builder.lessThan(root.get(name), (Expression<Comparable>)parameter);					

				} else if (condition.contains(AdvancedSearchRepository.LIKE_PARAM)) {
					p = builder.like(root.get(name), params[1]);

				}
				
				if(p != null) {
					predicate = builder.and(p);
				}
			}
			
			if(predicate != null) {
				criteria.where(predicate);	
			}			
			
		}		
	}

	private Object getValue(final Class<?> clazz, final String fieldName, final Object object) {
		try {
			final Field field = clazz.getDeclaredField(fieldName);
			Class<?> type = field.getType();
			if (type.equals(List.class)) {
				ParameterizedType pType = (ParameterizedType) field.getGenericType();
				type = (Class<?>) pType.getActualTypeArguments()[0];
			}
			if (type.equals(object.getClass())) {
				if (type.equals(String.class)) {
					return object.toString();
				}
				return object;
			}
			if (this.conversionService.canConvert(object.getClass(), type)) {
				return this.conversionService.convert(object, type);
			} else {
				throw new RuntimeException("Not convert type of field: " + fieldName + " on " + clazz.getSimpleName());
			}

		} catch (final Exception e) {
			throw new RuntimeException("Not found field: " + fieldName + " on " + clazz.getSimpleName());
		}

	}

	private Integer getSize(Map<String, Object> parameters) {
		Integer size = SIZE_DEFAULT;
		if (parameters.get(SIZE_PARAM) != null) {
			size = Integer.valueOf(parameters.get(SIZE_PARAM).toString());
		}
		return size;
	}

	private Integer getPage(Map<String, Object> parameters) {
		Integer page = PAGE_DEFAULT;
		if (parameters.get(PAGE_PARAM) != null) {
			page = Integer.valueOf(parameters.get(PAGE_PARAM).toString());
		}
		return page;
	}

	private List<Order> processOrder(Map<String, Object> parameters) {
		List<Order> orders = new ArrayList<>();
		if (parameters.containsKey(SORT_PARAM)) {
			final String[] sortParameters = parameters.get(SORT_PARAM).toString().split(PARAM_SEPARATOR);
			for (final String shortParam : sortParameters) {
				if (shortParam.startsWith("-")) {
					orders.add(new Order(Direction.DESC, shortParam.substring(1)));
				} else {
					orders.add(new Order(Direction.ASC, shortParam));
				}
			}
		}
		return orders;
	}

}
