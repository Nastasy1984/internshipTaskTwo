package my.app.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import my.app.model.User;
//this class helps us to map query results to Java objects
@Component
public class UserRowMapper implements RowMapper <User> {
	@Override
    public User mapRow(final ResultSet rs, final int rowNum) throws SQLException {     
		final User user = new User();
        user.setId(rs.getInt("user_id"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.seteMail(rs.getString("email"));
        
        //I wrote it in several lines intentionally to make it more understandable
        
        Timestamp timestamp = (rs.getTimestamp("created_on"));    
        LocalDateTime createdOn = timestamp.toLocalDateTime();
        user.setCreatedOn(createdOn);
        return user;
    }

}
