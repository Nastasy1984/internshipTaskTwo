package my.app.repository;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;

public class CustomSQLErrorCodeTranslator extends SQLErrorCodeSQLExceptionTranslator {
    private static final Logger LOG = LoggerFactory.getLogger(my.app.repository.CustomSQLErrorCodeTranslator.class.getName());
    
    @Override
    protected DataAccessException customTranslate(final String task, final String sql, final SQLException sqlException) {
		LOG.warn("customTranslate method was invoked with task: {} sql: {}", task, sql);
		
        if (sqlException.getErrorCode() == 23505) {
    		LOG.warn("ErrorCode == 23505 throwing new DuplicateKeyException");
            return new DuplicateKeyException("Custome Exception translator - Integrity contraint voilation.", sqlException);
        }
        
        return null;
    }

}