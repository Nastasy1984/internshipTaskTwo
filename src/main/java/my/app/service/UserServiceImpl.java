package my.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import my.app.model.User;
import my.app.repository.UserRepository;

@Component
public class UserServiceImpl implements UserService {
	
	private UserRepository userRepository;

	@Autowired
	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public User save(User user) {
		boolean isSaved = userRepository.save(user);
		if (isSaved) {
			return user;
		}
			return null;
	}

	@Override
	public User update(User user) {
		boolean isUpdated = userRepository.update(user);
		if (isUpdated) {
			return user;
		}
			return null;
	}
	
	
	@Override
	public void delete(User user) {
		if(user!=null) {
			userRepository.delete(user);
		}
	}

	@Override
	public void deleteById(Integer id) {
		if(id!=null) {
			userRepository.delete(getById(id));
		}
	}
	
	@Override
	public boolean containsId(Integer id) {
		if(id!=null) {
			return userRepository.containsId(id);
		}
		return false;
	}
	
	@Override
	public List<User> getAllAsList() {
		return userRepository.getAll();
	}
	
	@Override
	public User getById(Integer id) {
		return userRepository.getById(id);
	}

	@Override
	public List<User> getByLastName(String lastName) {
		if(lastName!=null) {
			return userRepository.getByLastName(lastName);
		}
		return null;
	}
}
