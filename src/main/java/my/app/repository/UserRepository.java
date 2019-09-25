package my.app.repository;

import java.util.List;
import java.util.Map;

import my.app.model.User;

public interface UserRepository {
    boolean save(User user);
    
    void delete(User user);
    
    List <User> getAll();
	
    User getById(Integer id);
    
    boolean containsId(Integer id);
    
    List<User> getByLastName(String lastName);
    
    boolean update(User user);
}