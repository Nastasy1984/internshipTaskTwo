package my.app.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import my.app.model.User;


@Repository
public class UserRepositoryImpl implements UserRepository{

	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	private SimpleJdbcInsert simpleJdbcInsertToUsers;
	private SimpleJdbcInsert simpleJdbcInsertToPhoneNumbers;
	
	//TODO delete this
	private Map<Integer,User> users = new HashMap<>();
	
	//FIXME change to DB creating
	public UserRepositoryImpl() {
		User user1 = new User("A","A");
		user1.setId(1);
		user1.seteMail("a@tom.com");
		User user2 = new User("B","B");
		user2.setId(2);
		user2.seteMail("b@tom.com");
		User user3 = new User("V","V");
		user3.setId(3);
		user3.seteMail("v@tom.com");
		User user4 = new User("G","G");
		user4.setId(4);
		user4.seteMail("g@tom.com");
		this.users.put(user1.getId(), user1);
		this.users.put(user2.getId(), user2);
		this.users.put(user3.getId(), user3);
		this.users.put(user4.getId(), user4);
	}
	
	

	@Autowired
    public void setDataSource(final DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        final CustomSQLErrorCodeTranslator customSQLErrorCodeTranslator = new CustomSQLErrorCodeTranslator();
        jdbcTemplate.setExceptionTranslator(customSQLErrorCodeTranslator);
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

	
	//FIXME
	@Override
	public void update(User user) {
		if (user != null) {
			MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
			mapSqlParameterSource.addValue("id", user.getId());
			mapSqlParameterSource.addValue("lastName", user.getLastName());
			mapSqlParameterSource.addValue("firstName", user.getFirstName());
			mapSqlParameterSource.addValue("eMail", user.geteMail());
			final SqlParameterSource namedParameters = mapSqlParameterSource;
			final String UPDATE_BY_ID = "UPDATE USERS SET LAST_NAME = :lastName, FIRST_NAME = :firstName, EMAIL = :eMail WHERE USER_ID = :id";
			int a = namedParameterJdbcTemplate.update(UPDATE_BY_ID, namedParameters);
			System.out.println("HERE");
			System.out.println(a);
		}
	}

	//DONE WORKS WITH DB
	//Here I use SimpleJDBCInsert just to try it
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
			new SimpleJdbcInsert(this.jdbcTemplate).withTableName("users")
					.usingColumns("last_name", "first_name", "email").usingGeneratedKeyColumns("user_id", "created_on")
					.executeAndReturnKeyHolder(parameters).getKeys();

			/* System.out.println(keys.toString()); */
			return true;
		}
		return false;
	}
	
	//DONE WORKS WITH DB 
	@Override
	public void delete(User user) {
		final SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("id", user.getId());
		final String DELETE_BY_ID = "DELETE FROM USERS WHERE USER_ID = :id";
		namedParameterJdbcTemplate.update(DELETE_BY_ID, namedParameters);
	}
	
	//DONE WORKS WITH DB
	@Override
    public List<User> getAll() {
	     return jdbcTemplate.query("SELECT * FROM USERS", new UserRowMapper());
	}
	
	//DONE WORKS WITH DB
	@Override
	public User getById(Integer id) {
		if ((id != null) && (id >= 0)) {
			final SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("id", id);
			final String SELECT_BY_ID = "SELECT * FROM USERS WHERE USER_ID = :id";
			List<User> users = namedParameterJdbcTemplate.query(SELECT_BY_ID, namedParameters, new UserRowMapper());
			if ((users != null) && (!users.isEmpty())) {
				return users.get(0);
			}
		}
		return null;
	}
	
	
	//DONE WORKS WITH DB
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
	
	//DONE WORKS WITH DB
	@Override
	public List<User> getByLastName(String lastName) {
		if (lastName != null) {
			final SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("lastName", lastName);
			final String SELECT_BY_LAST_NAME = "SELECT * FROM USERS WHERE LAST_NAME = :lastName";
			List<User> users = namedParameterJdbcTemplate.query(SELECT_BY_LAST_NAME, namedParameters, new UserRowMapper());
			if ((users != null) && (!users.isEmpty())) {
				return users;
			}
		}
		return null;
	}
}