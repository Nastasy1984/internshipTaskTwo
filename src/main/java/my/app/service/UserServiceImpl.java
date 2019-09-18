package my.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import my.app.model.User;
import my.app.repository.UserRepository;
import my.app.repository.UserRepositoryImpl;

@Component
public class UserServiceImpl implements UserService {
	
	//private UserRepository userRepository = new UserRepositoryImpl();
	private UserRepository userRepository;

	@Autowired
	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}


	@Override
	public void save(User user) {
		if(user!=null) {
			userRepository.save(user);
		}
	}

	@Override
	public void delete(User user) {
		if(user!=null) {
			userRepository.delete(user);
		}
	}

	@Override
	public void deleteById(Integer id) {
		if(id > 0) {
			userRepository.delete(getById(id));
		}
	}
	
	@Override
	public Map<Integer, User> getAll() {
		return userRepository.getAll();
	}
	
	@Override
	public List<User> getAllAsList() {
		return new ArrayList<User>(userRepository.getAll().values());
	}

	@Override
	public User getById(Integer id) {
		if(id > 0) {
			return userRepository.getById(id);
		}
		return null;
	}

	@Override
	public List<User> getByLastName(String lastName) {
		if(lastName!=null) {
			return userRepository.getByLastName(lastName);
		}
		return null;
	}
	/*
	//simple checking
	public static void main(String[] args) {
		UserServiceImpl aImpl = new UserServiceImpl(new UserRepositoryImpl()); 		
		List<User> myList = aImpl.getByLastName("Borisov");
		for (User user : myList) {
			System.out.println(user);
		}
	}
	*/

}
