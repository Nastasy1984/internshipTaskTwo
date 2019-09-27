package my.app.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import my.app.model.User;
import my.app.repository.utils.UserListExtractor;

@Repository
public class UserRepositoryImpl implements UserRepository {

	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	private UserListExtractor userListExtractor;


	@Autowired 
	public UserRepositoryImpl(UserListExtractor userListExtractor) {
		this.userListExtractor = userListExtractor;
	}

	@Autowired
	public void setDataSource(final DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		final CustomSQLErrorCodeTranslator customSQLErrorCodeTranslator = new CustomSQLErrorCodeTranslator();
		jdbcTemplate.setExceptionTranslator(customSQLErrorCodeTranslator);
		namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	// DONE WORKS WITH DB
	@Override
	public List<User> getAll() {
		
		//return jdbcTemplate.query("SELECT * FROM USERS", new UserRowMapper());
		return jdbcTemplate.query("SELECT\r\n" + 
				"	users.user_id, users.first_name, users.last_name, users.email, users.created_on,\r\n" + 
				"	phone_numbers.phone_number\r\n" + 
				"FROM users\r\n" + 
				"LEFT JOIN phone_numbers ON users.user_id=phone_numbers.user_id", userListExtractor);
	}

	// DONE WORKS WITH DB
	@Override
	public User getById(Integer id) {
		if ((id != null) && (id >= 0)) {
			final SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("id", id);
			//final String SELECT_BY_ID = "SELECT * FROM USERS WHERE USER_ID = :id";
			final String SELECT_BY_ID = "SELECT\r\n" + 
					"	users.user_id, users.first_name, users.last_name, users.email, users.created_on,\r\n" + 
					"	phone_numbers.phone_number\r\n" + 
					"FROM users\r\n" + 
					"LEFT JOIN phone_numbers ON users.user_id=phone_numbers.user_id\r\n" + 
					"WHERE users.user_id = :id";
			List<User> users = namedParameterJdbcTemplate.query(SELECT_BY_ID, namedParameters, userListExtractor);
			if ((users != null) && (!users.isEmpty())) {
				return users.get(0);
			}
		}
		return null;
	}

	// DONE WORKS WITH DB
	@Override
	public boolean containsId(Integer id) {
		if ((id != null) && (id >= 0)) {
			final SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("id", id);
			final String COUNT_BY_ID = "SELECT COUNT(*) FROM public.users WHERE USER_ID = :id";
			Integer countUsersInteger = 0;
			countUsersInteger = namedParameterJdbcTemplate.queryForObject(COUNT_BY_ID, namedParameters, Integer.class);
			if (countUsersInteger > 0) {
				return true;
			}
		}
		return false;
	}

	// DONE WORKS WITH DB
	@Override
	public List<User> getByLastName(String lastName) {
		if (lastName != null) {
			final SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("lastName", lastName);
			//final String SELECT_BY_LAST_NAME = "SELECT * FROM USERS WHERE LAST_NAME = :lastName";
			final String SELECT_BY_LAST_NAME = "SELECT\r\n" + 
					"	users.user_id, users.first_name, users.last_name, users.email, users.created_on,\r\n" + 
					"	phone_numbers.phone_number\r\n" + 
					"FROM users\r\n" + 
					"LEFT JOIN phone_numbers ON users.user_id=phone_numbers.user_id\r\n" + 
					"WHERE users.last_name = :lastName";
			
			List<User> users = namedParameterJdbcTemplate.query(SELECT_BY_LAST_NAME, namedParameters,
					userListExtractor);
			if ((users != null) && (!users.isEmpty())) {
				return users;
			}
		}
		return null;
	}

	@Override
	public void update(User user) {
		if (user != null) {
			MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
			mapSqlParameterSource.addValue("id", user.getId());
			mapSqlParameterSource.addValue("lastName", user.getLastName());
			mapSqlParameterSource.addValue("firstName", user.getFirstName());
			mapSqlParameterSource.addValue("eMail", user.geteMail());
			SqlParameterSource namedParameters = mapSqlParameterSource;
			final String UPDATE_BY_ID = "UPDATE USERS SET LAST_NAME = :lastName, FIRST_NAME = :firstName, EMAIL = :eMail WHERE USER_ID = :id";
			namedParameterJdbcTemplate.update(UPDATE_BY_ID, namedParameters);
			
			//deleting all existing numbers
			final String DELETE_NUM_BY_ID = "DELETE FROM PHONE_NUMBERS WHERE USER_ID = :id";
			namedParameterJdbcTemplate.update(DELETE_NUM_BY_ID, namedParameters);
			
			//adding updated numbers		
			for (String num : user.getPhoneNumbers()) {
				if (!num.equals("") && num != null) {
					mapSqlParameterSource.addValue("number", num);
					String insert_num_by_id = "INSERT INTO PHONE_NUMBERS (PHONE_NUMBER, USER_ID) VALUES (:number, :id)";
					namedParameters = mapSqlParameterSource;
					namedParameterJdbcTemplate.update(insert_num_by_id, namedParameters);
				}
			}
		}
	}

	// DONE WORKS WITH DB
	// Here I use SimpleJDBCInsert just to try it
	public boolean save(User user) {
		
		if ((user != null) && (user.getFirstName() != null) && (user.getLastName() != null)) {
			final Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("FIRST_NAME", user.getFirstName());
			parameters.put("LAST_NAME", user.getLastName());
			parameters.put("EMAIL", user.geteMail());
			/*
			 * Map<String, Object> keys = new SimpleJdbcInsert(this.jdbcTemplate)
			 * .withTableName("users") .usingColumns("last_name", "first_name", "email")
			 * .usingGeneratedKeyColumns("user_id", "created_on")
			 * .executeAndReturnKeyHolder(parameters) .getKeys();
			 */
			Map<String, Object> keys = new SimpleJdbcInsert(this.jdbcTemplate).withTableName("users")
					.usingColumns("last_name", "first_name", "email").usingGeneratedKeyColumns("user_id", "created_on")
					.executeAndReturnKeyHolder(parameters).getKeys();

			Integer userId = (Integer)keys.get("user_id"); 
					
			//adding updated numbers		
			for (String num : user.getPhoneNumbers()) {
				if (!num.equals("") && num != null) {
					parameters.put("USER_ID", userId);
					parameters.put("PHONE_NUMBER", num);
					new SimpleJdbcInsert(this.jdbcTemplate).withTableName("phone_numbers")
					.usingColumns("user_id", "phone_number").usingGeneratedKeyColumns("number_id")
					.executeAndReturnKeyHolder(parameters).getKeys();
				}
			}
			
			/* System.out.println(keys.toString()); */
			return true;
		}
		return false;
	}

	// DONE WORKS WITH DB
	@Override
	public void delete(User user) {
		final SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("id", user.getId());
		final String DELETE_BY_ID = "DELETE FROM USERS WHERE USER_ID = :id";
		namedParameterJdbcTemplate.update(DELETE_BY_ID, namedParameters);
	}

}