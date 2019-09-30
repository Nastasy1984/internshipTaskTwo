package my.app.service;

import java.util.List;

import my.app.model.User;

public interface UserService {
    User save(User user);
    
    User update(User user);
    
    void delete(User user);
    
    User getById(Integer id);
    
    boolean containsId(Integer id);
    
    List<User> getByLastName(String lastName);
    
    public List<User> getAllAsList();
    
    public void deleteById(Integer id);
}
