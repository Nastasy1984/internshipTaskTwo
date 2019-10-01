package my.app.controller;

import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
	public ModelAndView showUsersList() {
		LOG.info("showUsersList method was invoked");
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("usersList", pageService.getUsersList());
		formateDates(modelAndView);
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
			@RequestParam (value = "number", required = false) List<String> numbers) {
		LOG.info("addNewUser method was invoked with parameters firstName: {}, lastName: {}, eMail: {}, numbers: {}", 
				firstName, lastName, eMail, numbers.toString());
		ModelAndView modelAndView = new ModelAndView();

		if ((firstName == null) || (lastName == null) || (lastName.equals("")) || (firstName.equals(""))) {
			LOG.warn("Failed adding because of wrong input of names. firstName: {}, lastName: {}", firstName, lastName);
			LOG.warn("Sending to the addNewUser.jsp page");
			modelAndView.setViewName("addNewUser");
			String failString = "First name and last name are obligatory fields";
			modelAndView.addObject("failString", failString);
			return modelAndView;
		}

		if ((!eMail.contains("@")) && (!eMail.equals(""))) {
			LOG.warn("Failed adding because of wrong input of eMail: {}", eMail);
			LOG.warn("Sending to the addNewUser.jsp page");
			modelAndView.setViewName("addNewUser");
			String failString = "Wrong E-mail. E-mail must contain @";
			modelAndView.addObject("failString", failString);
			return modelAndView;
		}

		// saving the user by pageService
		User user = pageService.addUser(firstName, lastName, eMail, numbers);
		String successString = "User " + user.getFirstName() + " " + user.getLastName() + " was added successfully";
		LOG.info("User with id: {}, firstName: {}, lastName: {}, eMail: {}, numbers: {} was added successfully", user.getId(), user.getFirstName(), 
				user.getLastName(), user.geteMail(), user.getPhoneNumbers().toString());
		LOG.info("Redirecting to the user.jsp");
		// redirect to the list with all users
		modelAndView.addObject("usersList", pageService.getUsersList());
		modelAndView.addObject("successString", successString);
		formateDates(modelAndView);
		modelAndView.setViewName("user");
		return modelAndView;
	}

	// deleting user
	@GetMapping(value = "/delete/{id:\\d+}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ModelAndView deleteUser(@PathVariable("id") Integer id) {
		LOG.info("deleteUser method was invoked for user with path variable id: {}", id);
		ModelAndView modelAndView = new ModelAndView();

		int respCode = pageService.deleteUser(id);
		if (respCode == 200) {
			LOG.info("Got {} response status. User with id: {} was deleted", respCode, id);
			String successString = "User with id " + id + " was deleted successfully";
			modelAndView.addObject("successString", successString);
		} else {
			String failString = "Failed to delete user with id " + id;
			modelAndView.addObject("successString", failString);
		}
		modelAndView.addObject("usersList", pageService.getUsersList());
		formateDates(modelAndView);
		modelAndView.setViewName("user");
		return modelAndView;
	}

	// sending to the user\s updating page
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
		ModelAndView modelAndView = new ModelAndView();
		
		if ((lastName.equals("")) || (firstName.equals(""))) {
			String failString = "Last name and first name must be filled";
			LOG.warn("Redirecting to failedUpdate method");
			return failedUpdate(failString, id);
		}

		if ((!eMail.contains("@")) && (!eMail.equals(""))) {
			String failString = "Wrong E-mail. E-mail must contain @";
			LOG.warn("Redirecting to failedUpdate method");
			return failedUpdate(failString, id);
		}
		
		User user = pageService.updateUser(id, firstName, lastName, eMail, numbers);		     
		String successString = "User " + user.getFirstName() + " " + user.getLastName() + " was updated successfully";
		LOG.info("User with id: {}, firstName: {}, lastName: {}, eMail: {}, numbers: {} was updated successfully", user.getId(), user.getFirstName(), 
				user.getLastName(), user.geteMail(), user.getPhoneNumbers().toString());
		LOG.info("Redirecting to the user.jsp");
		// redirect to the list with all users
		modelAndView.addObject("usersList", pageService.getUsersList());
		modelAndView.addObject("successString", successString);
		formateDates(modelAndView);
		modelAndView.setViewName("user");
		return modelAndView;
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
	
    @RequestMapping(value="/error")
    @ResponseBody
    public ModelAndView handle() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("404");
		return modelAndView;
    }
	
}
