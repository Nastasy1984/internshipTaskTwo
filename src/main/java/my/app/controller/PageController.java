package my.app.controller;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.String;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import my.app.model.User;
import my.app.service.PageService;

@Controller
public class PageController {
    private static final Logger LOG = LoggerFactory.getLogger(my.app.controller.PageController.class.getName());

    private PageService pageService;

	@Autowired
	public PageController(PageService pageService) {
		this.pageService = pageService;
		LOG.info("PageService was created");
	}

	// sending to the first page
	@GetMapping("/")
	public ModelAndView getWelcomePage() {
		LOG.info("getWelcomePage method was invoked");
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("index");
		return modelAndView;
	}

	// sending to the jsp page addNewUser
	@GetMapping("/add-new-user")
	public String addNewUserPage() {
		LOG.info("addNewUserPage method was invoked");
		return "addNewUser";
	}

	// sending to the jsp page findUser
	@GetMapping("/find-user")
	public String findUserPage() {
		LOG.info("findUserPage method was invoked");
		return "findUser";
	}

	//date formatter that we send to the user and searchResult pages to display dates correctly
	private void formateDates(ModelAndView modelAndView) {
		LOG.info("formateDates method was invoked");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		modelAndView.addObject("formatter", formatter);
	}

	// providing the list of all users
	@GetMapping("/show-all-users")
	public ModelAndView showUsersList(String... strings) {
		LOG.info("showUsersList method was invoked");
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("usersList", pageService.getUsersList());
		formateDates(modelAndView);
		if (strings != null && strings.length > 0) {
			modelAndView.addObject(strings[0]);
		}
		modelAndView.setViewName("user");
		return modelAndView;
	}

	// searching for user by id
	@GetMapping("/find-user/{id:\\d+}")
	public ModelAndView findUserById(@PathVariable("id") Integer id) {
		LOG.info("findUserById method was invoked with path variable id: {}", id);
		ModelAndView modelAndView = new ModelAndView();
		List<User> users = pageService.getUserById(id);
		
		if (users == null) {
			return failedSearch(id.toString());
		}
		
		LOG.debug("findUserById method got user: {}", users.toString());
		modelAndView.addObject("users", users);
		formateDates(modelAndView);
		modelAndView.setViewName("searchResult");
		return modelAndView;
	}

	// searching for user by last name
	@GetMapping("/find-user/{lastName:\\D+}")
	public ModelAndView findUserByLastName(@PathVariable("lastName") String lastName) {
		LOG.info("findUserByLastName method was invoked with path variable lastName: {}", lastName);
		ModelAndView modelAndView = new ModelAndView();
		List<User> users = pageService.getUserByLastName(lastName);

		if (users == null) {
			return failedSearch(lastName);
		}
		
		LOG.debug("findUserByLastName method got users: {}", users.toString());
		modelAndView.addObject("users", users);
		modelAndView.setViewName("searchResult");
		formateDates(modelAndView);
		return modelAndView;
	}

	private ModelAndView failedSearch(String userString) {
		LOG.info("failedSearch method was invoked for searching user {}", userString);
		ModelAndView modelAndView = new ModelAndView();
		String failString = "Failed to find user " + userString;
		modelAndView.setViewName("findUser");
		modelAndView.addObject("failString", failString);
		return modelAndView;
	}

	// getting parameters of user and adding user to the users list
	// sending to the page with users
	@PostMapping(value = "/add-new-user")
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public ModelAndView addNewUser(@RequestParam(value = "firstName", required = true) String firstName,
			@RequestParam(value = "lastName", required = true) String lastName,
			@RequestParam(value = "eMail", required = false) String eMail,
			@RequestParam (value = "number", required = true) List<String> numbers) {
		LOG.info("addNewUser method was invoked with parameters firstName: {}, lastName: {}, eMail: {}, numbers: {}", 
				firstName, lastName, eMail, numbers.toString());

		//checking obligatory fields last name and first name
		if (StringUtils.isBlank(firstName) || StringUtils.isBlank(lastName)) {
			String failString = "First name and last name are obligatory fields";
			return failedAdding(failString);
		}

		//checking eMail
		if ((!eMail.contains("@")) && (!StringUtils.isBlank(eMail))) {
			String failString = "Wrong E-mail. E-mail must contain @";
			return failedAdding(failString);
		}
		
		//checking numbers
		numbers.removeIf(""::equals);
		if (numbers.isEmpty()) {
			String failString = "User must have at least one phone number";
			return failedAdding(failString);
		}
		
		// saving the user by pageService
		ResponseEntity <User> responseEntity = pageService.addUser(firstName, lastName, eMail, numbers);
		int respCode = responseEntity.getStatusCodeValue();
		
		if (respCode == 400) {
			LOG.warn("The user's phone numbers are not unique");
			String failString = "The user's phone numbers are not unique";
			return failedAdding(failString);
		}
		
		if (respCode == 200) {
			User user = responseEntity.getBody();
			
			if (user != null) {
				String successString = "User " + user.getFirstName() + " " + user.getLastName()
						+ " was added successfully";
				LOG.info("User: {} was added successfully", user.toString());
				// redirecting to the list with all users		
				LOG.info("Redirecting to the user.jsp");
				return showUsersList(successString);
			}
		}
		return failedAdding("Failed adding. Please, try again");
	}
	
	private ModelAndView failedAdding(String failString) {
		LOG.info("failedAdding method was invoked");
		LOG.warn("Failed adding because of: {}", failString);
		LOG.warn("Sending to the addNewUser.jsp page");
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("addNewUser");
		modelAndView.addObject("failString", failString);
		return modelAndView;
	}

	// deleting user
	@GetMapping(value = "/delete/{id:\\d+}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ModelAndView deleteUser(@PathVariable("id") Integer id) {
		LOG.info("deleteUser method was invoked for user with path variable id: {}", id);
		int respCode = pageService.deleteUser(id);
		String resultString;
		if (respCode == 200) {
			LOG.info("Got {} response status. User with id: {} was deleted", respCode, id);
			resultString = "User with id " + id + " was deleted successfully";
		} 
		else {
			resultString = "Failed to delete user with id " + id;
		}

		return showUsersList(resultString);
	}

	// redirecting to the user's updating page
	@GetMapping(value = "/update/{id:\\d+}")
	public ModelAndView updateUserPage(@PathVariable("id") Integer id) {
		LOG.info("updateUserPage method was invoked with path variable id: {}", id);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("updateUser");
		modelAndView.addObject("id", id);
		List<User> users = pageService.getUserById(id);
		modelAndView.addObject("user", users.get(0));
		return modelAndView;
	}

	// getting parameters of user and updating user in the users list
	// sending to the page with users
	@PostMapping(value = "/update-user")
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public ModelAndView updateUser(@RequestParam(value = "id") Integer id,
			@RequestParam(value = "firstName", required = false) String firstName,
			@RequestParam(value = "lastName", required = false) String lastName,
			@RequestParam(value = "eMail", required = false) String eMail,
			@RequestParam (value = "number", required = false) List<String> numbers) {
		LOG.info("updateUser method was invoked with parameters id: {}, firstName: {}, lastName: {}, eMail: {}, numbers: {}", 
				id, firstName, lastName, eMail, numbers.toString());
		
		//checking obligatory fields last name and first name
		if (StringUtils.isBlank(firstName) || StringUtils.isBlank(lastName)) {
			String failString = "Last name and first name must be filled";
			LOG.warn("Redirecting to failedUpdate method");
			return failedUpdate(failString, id);
		}

		//checking eMail
		if ((!eMail.contains("@")) && (!StringUtils.isBlank(eMail))) {
			String failString = "Wrong E-mail. E-mail must contain @";
			LOG.warn("Redirecting to failedUpdate method");
			return failedUpdate(failString, id);
		}
		
		//checking phone numbers
		numbers.removeIf(""::equals);
		
		if (numbers.isEmpty()) {
			String failString = "User must have at least one phone number";
			return failedUpdate(failString, id);
		}
		
		// updating by pageService
		ResponseEntity <User> responseEntity = pageService.updateUser(id, firstName, lastName, eMail, numbers);
		int respCode = responseEntity.getStatusCodeValue();
		
		if (respCode == 400) {
			LOG.warn("The user's phone numbers are not unique");
			String failString = "The user's phone numbers are not unique";
			return failedUpdate(failString, id);
		}
		
		if (respCode == 200) {
			User user = responseEntity.getBody();
			String successString = "User " + user.getFirstName() + " " + user.getLastName()
					+ " was updated successfully";
			LOG.info("User: {} was updated successfully", user.toString());
			// redirecting to the list with all users		
			LOG.info("Redirecting to the user.jsp");
			return showUsersList(successString);
		}
		return failedUpdate("Failed update. Please, try again with user", id);
	}

	private ModelAndView failedUpdate(String failString, Integer id) {
		LOG.info("failedUpdate method was invoked");
		LOG.warn("Failed updating of user with id: {}", id);
		LOG.warn("Sending to the updateUser.jsp page with message {}", failString);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("updateUser");
		modelAndView.addObject("id", id);
		modelAndView.addObject("failString", failString);
		List<User> users = pageService.getUserById(id);
		modelAndView.addObject("user", users.get(0));
		return modelAndView;
	}
	
}
