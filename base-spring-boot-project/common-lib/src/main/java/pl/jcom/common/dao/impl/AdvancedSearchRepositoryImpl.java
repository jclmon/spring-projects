package pl.jcom.common.dao.impl;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import pl.jcom.common.dao.AdvancedSearchRepository;

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
 *
 */
public class AdvancedSearchRepositoryImpl implements AdvancedSearchRepository {

	private static final String INCLUDE_FIELD = ":1";

	private final MongoOperations mongoOperations;
	private final ConversionService conversionService;

	public AdvancedSearchRepositoryImpl(MongoOperations mongoOperations, ConversionService conversionService) {
		super();
		this.mongoOperations = mongoOperations;
		this.conversionService = conversionService;
	}

	/**
	 * Method to find entities by params
	 */
	public <T> Page<T> findAllByParams(final Map<String, Object> parameters, Class<T> entityClass) {

		Query queryCount = this.createBasicQueryFromParameters(parameters, true, entityClass);
		Long number = this.mongoOperations.count(queryCount, entityClass);

		if (number > 0) {
			Query query = this.createBasicQueryFromParameters(parameters, false, entityClass);

			final List<T> list = this.mongoOperations.find(query, entityClass);

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

	private Query createBasicQueryFromParameters(final Map<String, Object> parameters, boolean count,
			Class<?> entityClass) {
		String fieldsToQuery = "";
		if (parameters.containsKey(AdvancedSearchRepository.FIELDS_PARAM)) {
			final String[] fields = parameters.get(AdvancedSearchRepository.FIELDS_PARAM).toString()
					.split(AdvancedSearchRepository.PARAM_SEPARATOR);
			StringBuffer fieldsTest = null;
			for (final String field : fields) {
				if (fieldsTest == null) {
					fieldsTest = new StringBuffer(field).append(INCLUDE_FIELD);
				} else {
					fieldsTest.append(AdvancedSearchRepository.PARAM_SEPARATOR).append(field)
							.append(INCLUDE_FIELD);
				}
			}
			fieldsToQuery = fieldsTest.toString();
		}
		final BasicQuery query = new BasicQuery("{}", "{" + fieldsToQuery + "}");

		if (!count) {
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
				final PageRequest pageRequest = new PageRequest(page, size);
				query.with(pageRequest);
			}
		}

		if (parameters.containsKey(AdvancedSearchRepository.SORT_PARAM)) {
			final String[] sortParameters = parameters.get(AdvancedSearchRepository.SORT_PARAM).toString()
					.split(AdvancedSearchRepository.PARAM_SEPARATOR);
			final List<Sort.Order> orders = new ArrayList<Sort.Order>();
			for (final String shortParam : sortParameters) {
				if (shortParam.startsWith("-")) {
					orders.add(new Order(Direction.DESC, shortParam.substring(1)));
				} else {
					orders.add(new Order(Direction.ASC, shortParam));
				}
			}
			final Sort sortQuery = new Sort(orders);
			query.with(sortQuery);
		}

		if (parameters.containsKey(AdvancedSearchRepository.FILTER_PARAM)) {

			final String[] conditions = parameters.get(AdvancedSearchRepository.FILTER_PARAM).toString()
					.split(AdvancedSearchRepository.PARAM_SEPARATOR);
			for (String condition : conditions) {
				String[] params = condition.split(AdvancedSearchRepository.EQUALS_PARAM + "|"
						+ AdvancedSearchRepository.NEGATION_PARAM + "|"
						+ AdvancedSearchRepository.GREATER_THAN_PARAM + "|"
						+ AdvancedSearchRepository.LESS_THAN_PARAM + "|"
						+ AdvancedSearchRepository.LIKE_PARAM);
				String name = params[0];

				if (condition.contains(AdvancedSearchRepository.EQUALS_PARAM)) {
					if (params.length == 2) {
						Object value = getValue(entityClass, name, params[1]);
						query.addCriteria(Criteria.where(name).is(value));

					} else {
						Object[] entities = Arrays.copyOfRange(params, 1, params.length);
						List<Object> parameterType = new ArrayList<Object>();
						for (Object value : entities) {
							parameterType.add(getValue(entityClass, name, value));
						}
						query.addCriteria(Criteria.where(name).in(parameterType));

					}
				} else if (condition.contains(AdvancedSearchRepository.NEGATION_PARAM)) {
					Object value = getValue(entityClass, name, params[1]);
					query.addCriteria(Criteria.where(name).is(value).not());
				} else if (condition.contains(AdvancedSearchRepository.GREATER_THAN_PARAM)) {
					Object value = getValue(entityClass, name, params[1]);
					query.addCriteria(Criteria.where(name).gt(value));
				} else if (condition.contains(AdvancedSearchRepository.LESS_THAN_PARAM)) {
					Object value = getValue(entityClass, name, params[1]);
					query.addCriteria(Criteria.where(name).lt(value));

				} else if (condition.contains(AdvancedSearchRepository.LIKE_PARAM)) {
					query.addCriteria(Criteria.where(name).regex(params[1]));

				}
			}
		}

		return query;
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

//	private String generatePattern(String strToSearch) {
//		String result = strToSearch.toLowerCase();
//		for (String[] replacement : AdvancedSearchRepositoryHelper.replacements) {
//			result = result.replaceAll(replacement[0], replacement[1]);
//		}
//		return result;
//	}

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
