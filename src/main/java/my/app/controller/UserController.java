package my.app.controller;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import my.app.controller.exception.ResourceNotFoundException;
import my.app.model.User;
import my.app.service.UserService;

@RestController
public class UserController {
	
	private UserService userService;
	
	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	//DONE WORKS return the list with all users
    @GetMapping(value ="/users", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<User> getUsersList() {
        return userService.getAllAsList();
    }
    
      

	//DONE WORKS finds user by id and produces data of user in JSON format
	@GetMapping(value ="/user/{id:\\d+}", produces = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
    public List <User> findUserById(@PathVariable ("id") Integer id) {
		if ((id==null) || (!userService.getAll().containsKey(id))){
			throw new ResourceNotFoundException();
		}
		User user = userService.getById(id);
		if (user==null) {
			throw new ResourceNotFoundException();
		}
		List<User> resultList = new ArrayList<User>();
		resultList.add(userService.getById(id));
		return resultList;
    }
	
	//DONE WORKS finds user by last name and produces data of user in JSON format
	@GetMapping(value ="/userln", produces = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
    public List<User> findUserByLastName(@RequestParam(value="lastName", required=true) String lastName) {
		List <User> users = userService.getByLastName(lastName);
		if (users.size()==0) {
			throw new ResourceNotFoundException();
		}
		return userService.getByLastName(lastName);
    }
	
	//DONE WORKS getting data of user in JSON format and adding user to the users list
    @PostMapping(value ="/add", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public User addNewUser(@RequestParam(value="firstName", required=true) String firstName, 
    		@RequestParam(value="lastName", required=true) String lastName) {
        if ((firstName!=null) &&  (lastName!=null) && (!lastName.equals("")) && (!firstName.equals(""))){
        	User user = new User (firstName,lastName);
        	User userSaved = userService.save(user);
        	return userSaved;
        }
        return null;
    }
    
  //DONE getting data of user and updating user in the users list
    @PutMapping(value = "/user/{id:\\d+}", consumes = {MediaType.TEXT_PLAIN_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public User updateUser(@PathVariable ("id") Integer id, @RequestBody String userString) {
    	
    	if ((userString!=null)){
    		StringReader reader = new StringReader(userString);
	        ObjectMapper mapperFromJson = new ObjectMapper();
	        User user;
			try {
				user = mapperFromJson.readValue(reader, User.class);
				User userUpdated = userService.update(user);
	        	return userUpdated;
			} catch (IOException e) {
				e.printStackTrace();
			}  
        }
        return null;
    }
    
    /*
  //DOes NOT WORK WHY??? getting data of user and updating user in the users list
    @PutMapping(value = "/user/{id:\\d+}", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public User updateUser(@PathVariable ("id") Integer id, @RequestBody User user) {

    	
    	if ((user!=null)){
	        	User userUpdated = userService.update(user);

	        	return userUpdated;
	        
        }
        return null;
    }
    */

    
    //DONE WORKS getting id and deleting user
    @DeleteMapping("/user/{id:\\d+}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void delete(@PathVariable("id") Integer id) {
    	User user = userService.getById(id);
        if ((id > 0) && (user != null)){
        	userService.deleteById(id);
        }
        else {
        	throw new ResourceNotFoundException();
        }
    }
}
