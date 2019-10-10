package my.app.service;

import java.util.List;

import my.app.model.User;

public interface UserService {
    User save(User user);
    
    User update(User user);
      
    User getById(Integer id);
    
    boolean containsId(Integer id);
    
    List<User> getByLastName(String lastName);
    
    List<User> getAllAsList();
    
    void deleteById(Integer id);
    
    boolean checkNumbers(List<String>numbers, int id);
}
