package my.app.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOG = LoggerFactory.getLogger(my.app.controller.UserController.class.getName());
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
		LOG.info("getUsersList method was invoked");
        return userService.getAllAsList();
    }
    
	//DONE WORKS finds user by id and produces data of user in JSON format
	@GetMapping(value ="/user/{id:\\d+}", produces = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
    public List <User> findUserById(@PathVariable ("id") Integer id) {
		LOG.info("findUserById method was invoked with parameter id: {}", id);
		if (!userService.containsId(id)){
			LOG.warn("ResourceNotFoundException in findUserById method. There is no user with id: {} in DB", id);
			throw new ResourceNotFoundException();
		}
		User user = userService.getById(id);

		if (user==null) {
			LOG.warn("ResourceNotFoundException in findUserById method. User gotten from userService is null");
			throw new ResourceNotFoundException();
		}
		LOG.info("User gotten from userService: {}", user.toString());
		List<User> resultList = new ArrayList<User>();
		resultList.add(userService.getById(id));
		return resultList;
    }
	
	//DONE WORKS finds user by last name and produces data of user in JSON format
	@GetMapping(value ="/userln", produces = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
    public List<User> findUserByLastName(@RequestParam(value="lastName", required=true) String lastName) {
		LOG.info("findUserByLastName method was invoked with parameter lastName: {}", lastName);
		List <User> users = userService.getByLastName(lastName);
		if ((users == null) || (users.isEmpty())) {
			LOG.warn("ResourceNotFoundException in findUserById method. There is no user with last name: {} in the DB", lastName);
			throw new ResourceNotFoundException("There is no user with last name " + lastName + " in the data base");
		}
		return userService.getByLastName(lastName);
    }

	//DONE WORKS getting data of user in JSON format and adding user to the users list
	@PostMapping(value = "/add", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public User addNewUser(@RequestBody User user) {
			LOG.info("addNewUser method was invoked with request body user: {}", user.toString());
			User userSaved = userService.save(user);

			if (userSaved == null) {
				LOG.warn("ResourceNotFoundException in addNewUser method. User gotten from userService is null");
				throw new ResourceNotFoundException();
			}

			LOG.info("user gotten from userService is: {}", userSaved.toString());
			return userSaved;
	}
    
  //DONE getting data of user and updating user in the users list
	@PutMapping(value = "/user/{id:\\d+}", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public User updateUser(@PathVariable("id") Integer id, @RequestBody User user) {

		LOG.info("updateUser method was invoked with path variable id: {} and request body user: {}", id, user.toString());
			User userUpdated = userService.update(user);

			if (userUpdated == null) {
				LOG.warn("ResourceNotFoundException in addNewUser method. User gotten from userService is null");
				throw new ResourceNotFoundException("Failed to update user with id " + id);
			}
			return userUpdated;
	}

    
    //DONE WORKS getting id and deleting user
    @DeleteMapping("/user/{id:\\d+}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void delete(@PathVariable("id") Integer id) {
		LOG.info("delete method was invoked with path variable id: {}", id);
    	//Checking if there is a user with this id in out list of users
    	User user = userService.getById(id);
    	
        if (user != null){
        	userService.deleteById(id);
        }
        else {
        	LOG.info("ResourceNotFoundException in delete method. There is no user with id: {}", id);
        	throw new ResourceNotFoundException("There is no user with id " + id + " in the DB");
        }
    }
}
