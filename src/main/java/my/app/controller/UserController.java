package my.app.controller;

import java.util.List;
import java.util.Optional;


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
import org.springframework.ui.Model;

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

	//WORKS return the list with all users
    @GetMapping(value ="/users", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public List<User> getUsersList() {
        return userService.getAllAsList();
    }

	//WORKS finds user by id and produces data of user in JSON format
	@GetMapping(value ="/find-user/{id:\\d+}", produces = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
    public User findUserById(@PathVariable ("id") Integer id) {
		//TODO change to normal message about mistake
		if ((id==null) || (!userService.getAll().containsKey(id))){
			throw new ResourceNotFoundException();
		}
		return userService.getById(id);
    }
	
	//WORKS finds user by last name and produces data of user in JSON format
	@GetMapping(value ="/find-user-by-last-name", produces = {MediaType.APPLICATION_JSON_VALUE})
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
    @PostMapping(value ="/add-new-user", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public User addNewUser(@RequestBody User user) {
        if (user!=null) {
        	userService.save(user);
        }
        return user;
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
