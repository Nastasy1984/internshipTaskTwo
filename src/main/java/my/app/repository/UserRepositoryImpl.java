package my.app.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOG = LoggerFactory.getLogger(my.app.repository.UserRepositoryImpl.class.getName());
	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	private UserListExtractor userListExtractor;


	@Autowired 
	public UserRepositoryImpl(UserListExtractor userListExtractor) {
		LOG.info("UserRepositoryImpl was created");
		this.userListExtractor = userListExtractor;
	}

	@Autowired
	public void setDataSource(final DataSource dataSource) {
		LOG.info("setDataSource method was invoked");
		jdbcTemplate = new JdbcTemplate(dataSource);
		final CustomSQLErrorCodeTranslator customSQLErrorCodeTranslator = new CustomSQLErrorCodeTranslator();
		jdbcTemplate.setExceptionTranslator(customSQLErrorCodeTranslator);
		namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	// DONE WORKS WITH DB
	@Override
	public List<User> getAll() {
		LOG.info("getAll method was invoked");
		//return jdbcTemplate.query("SELECT * FROM USERS", new UserRowMapper());
		return jdbcTemplate.query("SELECT\r\n" + 
				"	users.user_id, users.first_name, users.last_name, users.email, users.created_on,\r\n" + 
				"	phone_numbers.phone_number\r\n" + 
				"FROM users\r\n" + 
				"LEFT JOIN phone_numbers ON users.user_id=phone_numbers.user_id ORDER BY users.last_name", userListExtractor);
	}

	// DONE WORKS WITH DB
	@Override
	public User getById(Integer id) {
		LOG.info("getById method was invoked");
		if ((id != null) && (id >= 0)) {
			LOG.debug("searching for user with id: {}", id);
			final SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("id", id);
			//final String SELECT_BY_ID = "SELECT * FROM USERS WHERE USER_ID = :id";
			final String SELECT_BY_ID = "SELECT\r\n" + 
					"	users.user_id, users.first_name, users.last_name, users.email, users.created_on,\r\n" + 
					"	phone_numbers.phone_number\r\n" + 
					"FROM users\r\n" + 
					"LEFT JOIN phone_numbers ON users.user_id=phone_numbers.user_id\r\n" + 
					"WHERE users.user_id = :id";
			List<User> users = namedParameterJdbcTemplate.query(SELECT_BY_ID, namedParameters, userListExtractor);
			LOG.debug("getById method got users from DB : {}", users.toString());
			if ((users != null) && (!users.isEmpty())) {
				return users.get(0);
			}
		}
		LOG.info("getById method sends null");
		return null;
	}

	// DONE WORKS WITH DB
	@Override
	public boolean containsId(Integer id) {
		LOG.info("containsId method was invoked");
		if ((id != null) && (id >= 0)) {
			LOG.debug("searching for user with id: {}", id);
			final SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("id", id);
			final String COUNT_BY_ID = "SELECT COUNT(*) FROM public.users WHERE USER_ID = :id";
			Integer countUsersInteger = namedParameterJdbcTemplate.queryForObject(COUNT_BY_ID, namedParameters, Integer.class);
			if (countUsersInteger != null && countUsersInteger > 0) {
				LOG.info("User with id: {} was found in DB, containsId method sends true", id);
				return true;
			}
		}
		LOG.info("containsId method sends false");
		return false;
	}

	// DONE WORKS WITH DB
	@Override
	public List<User> getByLastName(String lastName) {
		LOG.info("getByLastName method was invoked");
		
		if (!StringUtils.isBlank(lastName)) {
			LOG.debug("searching for user with lastName: {}", lastName);
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
				LOG.debug("getByLastName method got users from DB: {}", users.toString());
				return users;
			}
			
		}
		return null;
	}

	@Override
	public boolean checkNumbers(List<String> numbers, int id) {
		LOG.info("checkNumbers method was invoked");
		
		if (numbers != null && !numbers.isEmpty()) {
			LOG.debug("checkNumbers method got parameter numbers: {} and id: {}", numbers.toString(), id);	
			
			for (String num: numbers) {
				MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
				LOG.debug("checking num: {} and id: {}", num, id);	
				mapSqlParameterSource.addValue("num", num);
				mapSqlParameterSource.addValue("id", id);
				SqlParameterSource namedParameters = mapSqlParameterSource;
				String count_numbers = "SELECT COUNT(*) FROM PHONE_NUMBERS WHERE PHONE_NUMBER = :num AND USER_ID != :id";
				Integer countNums = namedParameterJdbcTemplate.queryForObject(count_numbers, namedParameters, Integer.class);
				LOG.debug("countNums: {} for num: {}", countNums, num);
				if (countNums != null && countNums > 0) {
					LOG.debug("Phone number num: {} was found in DB, checkNumbers returns false", num);
					return false;
				}
				
			}
			LOG.debug("Phone numbers numbers: {} were not found in DB, checkNumbers returns true", numbers);
			return true;
		}
		return false;
	}
	
	
	
	
	@Override
	public User update(User user) {
		LOG.info("update method was invoked");
		if (user != null && user.getId() != null) {
			LOG.debug("Updating user: {}", user.toString());
			MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
			mapSqlParameterSource.addValue("id", user.getId());
			mapSqlParameterSource.addValue("lastName", user.getLastName());
			mapSqlParameterSource.addValue("firstName", user.getFirstName());
			mapSqlParameterSource.addValue("eMail", user.geteMail());
			SqlParameterSource namedParameters = mapSqlParameterSource;
			final String UPDATE_BY_ID = "UPDATE USERS SET LAST_NAME = :lastName, FIRST_NAME = :firstName, EMAIL = :eMail WHERE USER_ID = :id";
			namedParameterJdbcTemplate.update(UPDATE_BY_ID, namedParameters);

			// deleting all existing numbers for this user
			final String DELETE_NUM_BY_ID = "DELETE FROM PHONE_NUMBERS WHERE USER_ID = :id";
			LOG.debug("Deleting all numbers for user with id: {}", user.getId());
			namedParameterJdbcTemplate.update(DELETE_NUM_BY_ID, namedParameters);

			// adding updated numbers
			for (String num : user.getPhoneNumbers()) {
				if (!num.equals("") && num != null) {
					LOG.debug("Inserting number: {} for user with id: {}", num, user.getId());
					mapSqlParameterSource.addValue("number", num);
					String insert_num_by_id = "INSERT INTO PHONE_NUMBERS (PHONE_NUMBER, USER_ID) VALUES (:number, :id)";
					namedParameters = mapSqlParameterSource;
					namedParameterJdbcTemplate.update(insert_num_by_id, namedParameters);
				}
			}
			return getById(user.getId());
		}
		return null;
	}

	// DONE WORKS WITH DB
	// Here I use SimpleJDBCInsert just to try it
	public User save(User user) {
		LOG.info("save method was invoked");
		
		if (user != null && !StringUtils.isBlank(user.getFirstName())  && !StringUtils.isBlank(user.getLastName())) {
			LOG.debug("Saving user: {}", user.toString());
			final Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("FIRST_NAME", user.getFirstName());
			parameters.put("LAST_NAME", user.getLastName());
			parameters.put("EMAIL", user.geteMail());
			Map<String, Object> keys = new SimpleJdbcInsert(this.jdbcTemplate).withTableName("users")
					.usingColumns("last_name", "first_name", "email").usingGeneratedKeyColumns("user_id", "created_on")
					.executeAndReturnKeyHolder(parameters).getKeys();

			Integer userId = (Integer)keys.get("user_id"); 
			LOG.info("save method created user with generated id: {}", userId);		
			//adding numbers
			if (user.getPhoneNumbers() != null && !user.getPhoneNumbers().isEmpty()) {
				
				for (String num : user.getPhoneNumbers()) {
					
					if (!StringUtils.isBlank(num)) {
						parameters.put("USER_ID", userId);
						parameters.put("PHONE_NUMBER", num);
						new SimpleJdbcInsert(this.jdbcTemplate).withTableName("phone_numbers")
								.usingColumns("user_id", "phone_number").usingGeneratedKeyColumns("number_id")
								.executeAndReturnKeyHolder(parameters).getKeys();
					}
				}
			}
			return getById(userId);
		}
		return null;
	}

	// DONE WORKS WITH DB
	@Override
	public void delete(User user) {
		LOG.info("delete method was invoked");
		if (user != null) {
			LOG.debug("Deleting user: {}", user.toString());
			final SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("id", user.getId());
			final String DELETE_BY_ID = "DELETE FROM USERS WHERE USER_ID = :id";
			namedParameterJdbcTemplate.update(DELETE_BY_ID, namedParameters);
			LOG.debug("User with id: {} was deleted from DB", user.getId());
		}
	}
}