package my.app.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import my.app.model.User;
import my.app.service.UserService;

//REST API
@RestController
@RequestMapping("/api")
public class UserController {
    private static final Logger LOG = LoggerFactory.getLogger(my.app.controller.UserController.class.getName());
	private UserService userService;
	private static final String regex = "^[a-zA-Z\'-]+$";
	
	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	//return the list with all users
    @GetMapping(value ="/users", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<User>> getUsersList() {
		LOG.info("getUsersList method was invoked");
		List <User> resultSortedList = userService.getAllAsList(); 
		Collections.sort(resultSortedList, new Comparator<User>() {
		    @Override
		    public int compare(User first, User sec) {
		        return first.getId() > sec.getId() ? 1 : (first.getId() < sec.getId()) ? -1 : 0;
		    }
		});
        return ResponseEntity.status(HttpStatus.OK)
                .body(resultSortedList);
    }
    
	//finds user by id and produces data of user in JSON format
	@GetMapping(value ="/user/{id:\\d+}", produces = {MediaType.APPLICATION_JSON_VALUE})
	//@ResponseBody
    //We souldn't use the annotation @ResponseBody because we annotated class as a @RestController
    public List <User> findUserById(@PathVariable ("id") Integer id) {
		LOG.info("findUserById method was invoked with parameter id: {}", id);
		if (!userService.containsId(id)){
			LOG.warn("ResourceNotFoundException in findUserById method. There is no user with id: {} in DB", id);
			//throw new ResourceNotFoundException("User with id " + id + " not found");
			//an alternative way
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id " + id + " not found");
		}
		User user = userService.getById(id);

		if (user==null) {
			LOG.warn("ResourceNotFoundException in findUserById method. User gotten from userService is null");
			//throw new ResourceNotFoundException("User with id " + id + " not found");
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id " + id + " not found");
		}
		LOG.info("User gotten from userService: {}", user.toString());
		List<User> resultList = new ArrayList<>();
		resultList.add(user);
		return resultList;
    }
	
	//finds user by last name and produces data of user in JSON format
	@GetMapping(value ="/userln", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<User> findUserByLastName(@RequestParam(value="lastName", required=true) String lastName) {
		LOG.info("findUserByLastName method was invoked with parameter lastName: {}", lastName);
		List <User> users = userService.getByLastName(lastName);
		if ((users == null) || (users.isEmpty())) {
			LOG.warn("ResourceNotFoundException in findUserById method. There is no user with last name: {} in the DB", lastName);
			//throw new ResourceNotFoundException("There is no user with last name " + lastName + " in the data base");
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no user with last name " + lastName + " in the data base");
		}
		return users;
    }

	//getting data of user in JSON format and adding user to the users list
	@PostMapping(value = "/add", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<User> addNewUser(@Valid @RequestBody User user) {
		LOG.info("addNewUser method was invoked with request body user: {}", user.toString());
		//check for correct input of first name and last name (only letters)
		LOG.debug("addNewUser method: lastName matches regex {}", user.getLastName().matches(regex));
		LOG.debug("addNewUser method: firstName matches regex {}", user.getFirstName().matches(regex));
		
		if (!user.getLastName().matches(regex) || !user.getFirstName().matches(regex)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		
		if (userService.checkNumbers(user.getPhoneNumbers(), 0)) {
			User userSaved = userService.save(user);
			
			if (userSaved == null) {
				LOG.warn("ResponseStatusException in addNewUser method. User gotten from userService is null");
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Failed to add user to the data base");
				// throw new ResourceNotFoundException("Failed to add user to the data base");
			}

			LOG.info("user gotten from userService is: {}", userSaved.toString());
			return ResponseEntity.status(HttpStatus.CREATED).body(userSaved);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}
    
  //getting data of user and updating user in the users list
	@PutMapping(value = "/user/{id:\\d+}", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ResponseEntity<User> updateUser(@PathVariable("id") Integer id, @Valid @RequestBody User user) {
		LOG.info("updateUser method was invoked with path variable id: {} and request body user: {}", id,
				user.toString());
		
		//check for correct input of first name and last name (only letters)
		LOG.debug("updateUser method: lastName matches regex {}", user.getLastName().matches(regex));
		LOG.debug("updateUser method: firstName matches regex {}", user.getFirstName().matches(regex));
		
		if (!user.getLastName().matches(regex) || !user.getFirstName().matches(regex)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		
		if (userService.checkNumbers(user.getPhoneNumbers(), id)) {
			LOG.debug("updateUser method: user's numbers are unique");
			User userUpdated = userService.update(user);

			if (userUpdated == null) {
				LOG.warn("ResourceNotFoundException in updateUser method. User gotten from userService is null");
				// throw new ResourceNotFoundException("Failed to update user with id " + id);
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Failed to update user with id " + id);
			}
			
			LOG.info("user gotten from userService is: {}", userUpdated.toString());
			return ResponseEntity.status(HttpStatus.OK).body(userUpdated);
		}
		LOG.debug("updateUser method: numbers {} are not unique", user.getPhoneNumbers());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}

    
    //getting id and deleting user
    @DeleteMapping("/user/{id:\\d+}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("id") Integer id) {
		LOG.info("delete method was invoked with path variable id: {}", id);
    	//Checking if there is a user with this id in out list of users
    	//User user = userService.getById(id);

        if (userService.containsId(id)){
        	userService.deleteById(id);
        }
        else {
        	LOG.info("ResourceNotFoundException in delete method. There is no user with id: {}", id);
        	//throw new ResourceNotFoundException("There is no user with id " + id + " in the DB");
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no user with id " + id + " in the DB");
        }
    } 
}
