package my.app.service;

import java.util.List;
import java.util.Map;

import my.app.model.User;

public interface UserService {
    User save(User user);
    
    User update(User user);
    
    void delete(User user);
    
    Map<Integer, User> getAll();
	
    User getById(Integer id);
    
    List<User> getByLastName(String lastName);
    
    public List<User> getAllAsList();
    
    public void deleteById(Integer id);
}
