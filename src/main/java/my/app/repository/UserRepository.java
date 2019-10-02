package my.app.repository;

import java.util.List;

import my.app.model.User;

public interface UserRepository {
    User save(User user);
    
    void delete(User user);
    
    List <User> getAll();
	
    User getById(Integer id);
    
    boolean containsId(Integer id);
    
    List<User> getByLastName(String lastName);
    
    User update(User user);
    
    boolean checkNumbers(List<String> numbers, int id);
   
}