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
//@Controller
public class UserController {
	
	//private UserService userService = new UserServiceImpl();
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
    
      

	//DONE finds user by id and produces data of user in JSON format
	@GetMapping(value ="/user/{id:\\d+}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.OK)
	@ResponseBody
    public List <User> findUserById(@PathVariable ("id") Integer id) {
		//TODO change to normal message about mistake
		if ((id==null) || (!userService.getAll().containsKey(id))){
			throw new ResourceNotFoundException();
		}
		List<User> resultList = new ArrayList<User>();
		resultList.add(userService.getById(id));
		return resultList;
    }
	
	//WORKS finds user by last name and produces data of user in JSON format
	@GetMapping(value ="/user/{ln:\\D+}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.OK)
	@ResponseBody
    public List<User> findUserByLastName(@RequestParam(value="lastName", required=true) String lastName) {
		//TODO change to normal message about mistake
		if ((lastName==null) || (lastName.equals(""))){
			throw new ResourceNotFoundException();
		}
		//TODO change to normal message about mistake
		if (userService.getByLastName(lastName).size()==0) {
			throw new ResourceNotFoundException();
		}

		return userService.getByLastName(lastName);
    }
	
	//getting data of user in JSON format and adding user to the users list
    @PostMapping(value ="/add", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public User addNewUser(@RequestParam(value="firstName", required=true) String firstName, 
    		@RequestParam(value="lastName", required=true) String lastName) {
        if ((firstName!=null) &&  (lastName!=null) && (!lastName.equals("")) && (!firstName.equals(""))){
        	User user = new User (firstName,lastName);
        	User user2 = userService.save(user);
        	return user2;
        }
        return null;
    }
	
    //getting data of user in JSON format and updating user in the users list
    @PutMapping(value = "/user/{id:\\d+}", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public User update(@PathVariable( "id" ) Integer id, @RequestBody User user) {
        if (id > 0) {
        	userService.save(user);
        }
        return user;
    }
	
    //getting id and deleting user
    @DeleteMapping("/user/{id:\\d+}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String delete(@PathVariable("id") Integer id) {
        if ((id > 0) && (userService.getById(id) != null)){
        	userService.deleteById(id);
        	return "Deleted";
        }
        return "Failed to delete";
    }
}
