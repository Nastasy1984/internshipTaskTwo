package my.app.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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
	private SimpleJdbcInsert simpleJdbcInsert;
	
	//TODO delete this
	private Map<Integer,User> users = new HashMap<>();
	
	//FIXME change to DB creating
	public UserRepositoryImpl() {
		//creating 3 users for probe
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
        simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("users");
    }

	
	//FIXME
	@Override
	public boolean save(User user) {
		// imitation of generating new id (maximum id + 1)
		Set<Integer> setKeySet = users.keySet();
		Integer max = 0;
		// looking for maximum id in users
		for (Integer key : setKeySet) {
			if (key.intValue() > max.intValue()) {
				max = key;
			}
		}
		Integer newId = max + 1;
		user.setId(newId);
		users.put(user.getId(), user);
		return true;
	}
	
	//FIXME
	@Override
	public boolean update(User user) {
		if (user == null) {
			return false;
		}
		users.put(user.getId(), user);
		return true;
	}
	/*
	//FIXME
	@Override
	public boolean containsId(Integer id) {
		if (users.containsKey(id)){
			return true;
		}
		return false;
	}
	*/
	//FIXME
	@Override
	public void delete(User user) {
		users.remove(user.getId());
	}
	
	
	
	//DONE WORKS WITH DB
	@Override
    public List<User> getAll() {
	     return jdbcTemplate.query("SELECT * FROM USERS", new UserRowMapper());
	}
	
	//DONE WORKS WITH DB
	@Override
	public User getById(Integer id) {
        final SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("id", id);
        final String SELECT_BY_ID = "SELECT * FROM USERS WHERE USER_ID = :id";
        return (User) namedParameterJdbcTemplate.queryForObject(SELECT_BY_ID, namedParameters, new UserRowMapper());
	}
	
	
	//DONE WORKS WITH DB
	@Override
	public boolean containsId(Integer id) {
		final SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("id", id);
		final String COUNT_BY_ID = "SELECT COUNT(*) FROM public.users WHERE USER_ID = :id";
		Integer countUsersInteger = 0;
		countUsersInteger = namedParameterJdbcTemplate.queryForObject(COUNT_BY_ID, namedParameters, Integer.class);
		if (countUsersInteger > 0) {
			return true;
		}
		return false;
	}
	
	
	//FIXME
	@Override
	public List<User> getByLastName(String lastName) {
		if (lastName != null) {
			List<User> listValuesList = new ArrayList<User>(users.values());
			List<User> resultList = new ArrayList<User>();
			for (User user: listValuesList) {
				if (user.getLastName().equals(lastName)){
					resultList.add(user);
				}
			}
			return resultList;
		}
		return null;
	}
}