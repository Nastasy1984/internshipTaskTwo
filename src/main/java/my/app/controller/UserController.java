package my.app.controller;

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
		if ((id==null) || (!userService.containsId(id))){
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
		if ((users == null) || (users.isEmpty())) {
			throw new ResourceNotFoundException("There are no user with last name " + lastName + " in the user's list");
		}
		return userService.getByLastName(lastName);
    }

	//DONE WORKS getting data of user in JSON format and adding user to the users list
    @PostMapping(value ="/add", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public User addNewUser(@RequestBody User user) {
    	//TODO delete
    	System.out.println("UC");
    	System.out.println("User gotten");
    	System.out.println(user.toString());
        /*if ((firstName!=null) &&  (lastName!=null) && (!lastName.equals("")) && (!firstName.equals(""))){
        	User user = new User (firstName, lastName);
        	
        	if (eMail != null) {
        		user.seteMail(eMail);
        	}*/
        	
    	
    	User userSaved = userService.save(user);
        	return userSaved;
        //}
        //return null;
    }
    
  //DONE getting data of user and updating user in the users list
    @PutMapping(value = "/user/{id:\\d+}", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public User updateUser(@PathVariable ("id") Integer id, @RequestBody User user) {
    	if (id==null){
            throw new ResourceNotFoundException("Id is null!");
    	}
    	if ((user!=null)){
    		
			User userUpdated = userService.update(user);
			if (userUpdated==null) {
				throw new ResourceNotFoundException("There are no user with id " + id + " in the user's list");
			}
			return userUpdated;  
        }
        return null;
    }
    
    //DONE WORKS getting id and deleting user
    @DeleteMapping("/user/{id:\\d+}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void delete(@PathVariable("id") Integer id) {
    	if (id==null){
            throw new ResourceNotFoundException("Id is null!");
    	}
    	//Checking if there is a user with this id in out list of users
    	User user = userService.getById(id);
        if (user != null){
        	userService.deleteById(id);
        }
        else {
        	throw new ResourceNotFoundException("There are no user with id " + id + " in the user's list");
        }
    }
}
