package my.app.repository;

import java.util.List;
import java.util.Map;

import my.app.model.User;

public interface UserRepository {
    void save(User user);
    
    void delete(User user);
    
    Map<Integer, User> getAll();
	
    User getById(Integer id);
    
    List<User> getByLastName(String lastName);
}