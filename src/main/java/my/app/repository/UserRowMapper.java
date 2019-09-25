package my.app.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import my.app.model.User;
//this class helps us to map query results to Java objects
public class UserRowMapper implements RowMapper <User> {
	@Override
    public User mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        final User user = new User();
        user.setId(rs.getInt("user_id"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.seteMail(rs.getString("email"));
        return user;
    }

}
