package my.app.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import my.app.model.User;
import my.app.repository.UserRepository;

@Component
public class UserServiceImpl implements UserService {
    private static final Logger LOG = LoggerFactory.getLogger(my.app.service.UserServiceImpl.class.getName());
	private UserRepository userRepository;

	@Autowired
	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
		LOG.info("UserServiceImpl was created");
	}

	@Override
	public User save(User user) {
		LOG.info("save method was invoked");
		
		//deleting repeating numbers
		Set<String> cleanedNumbers = new HashSet<String>(user.getPhoneNumbers());
		List <String> listOfNumbers = new ArrayList<>(cleanedNumbers);
		user.setPhoneNumbers(listOfNumbers);
		
		User userSaved = userRepository.save(user);

		if (userSaved != null) {
			LOG.debug("User saved: {}", userSaved.toString());
			return userSaved;
		}
		
		LOG.debug("save method returns null");
			return null;
	}

	@Override
	public User update(User user) {
		LOG.info("update method was invoked");
		if (user != null  && user.getId() != null){
			//deleting repeating numbers
			Set<String> cleanedNumbers = new HashSet<String>(user.getPhoneNumbers());
			List <String> listOfNumbers = new ArrayList<>(cleanedNumbers);
			user.setPhoneNumbers(listOfNumbers);
			
			User userUpdated = userRepository.update(user);
			if (userUpdated != null) {
				LOG.debug("User updated: {}", userUpdated.toString());
				return userUpdated;
			}
		}
		LOG.debug("update method returns null");
		return null;
	}

	@Override
	public void deleteById(Integer id) {
		LOG.info("deleteById method was invoked");
		if(id!=null) {
			userRepository.delete(getById(id));
		}
	}
	
	@Override
	public boolean containsId(Integer id) {
		LOG.info("containsId method was invoked");
		if(id!=null) {
			return userRepository.containsId(id);
		}
		LOG.debug("containsId method returns false because id is null");
		return false;
	}
	
	@Override
	public List<User> getAllAsList() {
		LOG.info("getAllAsList method was invoked");
		return userRepository.getAll();
	}
	
	@Override
	public User getById(Integer id) {
		LOG.info("getById method was invoked");
		if(id!=null) {
			LOG.debug("Searching for user with id: {}", id);
			return userRepository.getById(id);
		}
		LOG.debug("getById method returns null because id is null");
		return null;
	}

	@Override
	public List<User> getByLastName(String lastName) {
		if(lastName!=null) {
			LOG.debug("Searching for user with lastName: {}", lastName);
			return userRepository.getByLastName(lastName);
		}
		LOG.debug("getByLastName method returns null because lastName is null");
		return null;
	}
	
	@Override
	public boolean checkNumbers(List<String> numbers, int id) {
		LOG.info("checkNumbers method was invoked");
		if (numbers != null && !numbers.isEmpty()) {
			LOG.debug("checkNumbers method got parameter numbers: {}", numbers.toString());
		
			//excluding repeating numbers from the initial list
			List<String> phoneNumbers = new ArrayList<>(new HashSet<String>(numbers));

			return userRepository.checkNumbers(phoneNumbers, id);
		}
		return false;
	};
	
}
