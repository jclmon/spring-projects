package pl.jcom.common.dao;

import pl.jcom.common.exception.DataAccessException;
import pl.jcom.common.model.Sequence;

public interface SequenceDao  extends GenericDao<Sequence>{

	int getNextSequenceId(String key) throws DataAccessException;
}
