package my.app.repository.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import my.app.model.User;

@Component
public class UserListExtractor implements ResultSetExtractor<List<User>>{
    private static final Logger LOG = LoggerFactory.getLogger(my.app.repository.utils.UserListExtractor.class.getName());

	@Override
	public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
		LOG.info("extractData method was invoked");
		//we will save users in the map for our purposes
		Map<Integer, User> map = new HashMap<>();
		while (rs.next()) {
			// getting id
			Integer id = rs.getInt("user_id");
			// checking if there is a user with this id in the map
			User user = map.get(id);
			// if no
			if (user == null) {
				// creating new user
				user = new User();
				// getting all fields except phone numbers
				user.setId(id);
				user.setFirstName(rs.getString("first_name"));
				user.setLastName(rs.getString("last_name"));
				/*String eMail = rs.getString("email");
				if (eMail!=null) {
					user.seteMail(eMail);
				}*/
				user.seteMail(rs.getString("email"));
		        Timestamp timestamp = (rs.getTimestamp("created_on"));    
		        LocalDateTime createdOn = timestamp.toLocalDateTime();
		        user.setCreatedOn(createdOn);
				// putting him to the map with id as a key
				map.put(id, user);
			}
			// if there is such user in the map we ignore his previous fields
			//now anyway we have a user (from the map or from the DB)
			// checking if there are phone numbers in this user 
			List <String> phoneNumbers = user.getPhoneNumbers();
			
			// if no
			if (phoneNumbers == null) {
				//creating set of numbers
				phoneNumbers = new ArrayList<String>();
				user.setPhoneNumbers(phoneNumbers);
			}
			//now user has a set of numbers
			//getting number from the DB
				String phoneNumber = rs.getString("phone_number");
				//adding number to the user's phone numbers set
				user.getPhoneNumbers().add(phoneNumber);	
		}
		List<User> users = new ArrayList<User>(map.values());
		LOG.debug("extractData method extracted users: {}",users.toString());
		return users;
	}

}
