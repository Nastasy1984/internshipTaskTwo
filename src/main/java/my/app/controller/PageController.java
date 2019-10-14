package my.app.controller;

import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.internal.constraintvalidators.bv.money.MinValidatorForMonetaryAmount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.String;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

	/*SEARCHING*/
	// sending to the jsp page findUser
	@GetMapping("/find-user")
	public String findUserPage() {
		LOG.info("findUserPage method was invoked");
		return "findUser";
	}
	
	// searching for user by id
	@GetMapping("/find-user/{id:\\d+}")
	public ModelAndView findUserById(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
		LOG.info("findUserById method was invoked with path variable id: {}", id);
		ModelAndView modelAndView = new ModelAndView();
		List<User> users = pageService.getUserById(id);
		
		if (users == null) {
			return failedSearch(id.toString(), redirectAttributes);
		}
		
		LOG.debug("findUserById method got user: {}", users.toString());
		modelAndView.addObject("users", users);
		formateDates(modelAndView);
		modelAndView.setViewName("searchResult");
		return modelAndView;
	}

	// searching for user by last name
	@GetMapping("/find-user/{lastName:\\D+}")
	public ModelAndView findUserByLastName(@PathVariable("lastName") String lastName, RedirectAttributes redirectAttributes) {
		LOG.info("findUserByLastName method was invoked with path variable lastName: {}", lastName);
		ModelAndView modelAndView = new ModelAndView();
		List<User> users = pageService.getUserByLastName(lastName);

		if (users == null) {
			return failedSearch(lastName, redirectAttributes);
		}
		
		LOG.debug("findUserByLastName method got users: {}", users.toString());
		modelAndView.addObject("users", users);
		modelAndView.setViewName("searchResult");
		formateDates(modelAndView);
		return modelAndView;
	}

	private ModelAndView failedSearch(String userString, RedirectAttributes redirectAttributes) {
		LOG.info("failedSearch method was invoked for searching user {}", userString);
		String failString = "Failed to find user " + userString;
		redirectAttributes.addFlashAttribute("failString", failString);
		return new ModelAndView("redirect:/find-user");
	}
	

	/*DELETING*/
	// deleting user DONE WORKS with redirecting
	@GetMapping(value = "/delete/{id:\\d+}")
	public String deleteUser(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
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
		redirectAttributes.addFlashAttribute( "successString", resultString);
		return "redirect:/show-all-users";
	}
	
	/*ADDING*/
	// redirecting to the jsp page addNewUser
	@GetMapping("/add-new-user")
	public String addNewUserPage() {
		LOG.info("addNewUserPage method was invoked");
		return "addNewUser";
	}
	
	// getting parameters of user and adding user to the users list
	@PostMapping(value = "/add-new-user")
	public String addNewUser(@RequestParam(value = "firstName", required = true) String firstName,
			@RequestParam(value = "lastName", required = true) String lastName,
			@RequestParam(value = "eMail", required = false) String eMail,
			@RequestParam(value = "number", required = true) List<String> numbers,
			RedirectAttributes redirectAttributes) {
		LOG.info("addNewUser method was invoked with parameters firstName: {}, lastName: {}, eMail: {}, numbers: {}",
				firstName, lastName, eMail, numbers.toString());

		// All reasons of BAD REQUEST status except not unique phone numbers we are checking here
		// Therefore the only possible reason of BAD REQUEST status is not unique numbers

		User userGotten = new User (firstName, lastName);
		userGotten.seteMail(eMail);
		
		// checking numbers
		numbers.removeIf(""::equals);
		
		if (numbers.isEmpty()) {
			String failString = "User must have at least one phone number";
			return failedAdding(failString, redirectAttributes, userGotten);
		}
		
		userGotten.setPhoneNumbers(numbers);
		
		// checking eMail
		if (!StringUtils.isBlank(eMail)) {
			boolean isEmailValid = true;

			try {
				InternetAddress emailAddr = new InternetAddress(eMail);
				emailAddr.validate();
			} 
			catch (AddressException ex) {
				isEmailValid = false;
			}

			if (!isEmailValid) {
				String failString = "Invalid E-mail";
				return failedAdding(failString, redirectAttributes, userGotten);
			}
		}

		// saving the user by pageService
		ResponseEntity<User> responseEntity = pageService.addUser(userGotten);
		int respCode = responseEntity.getStatusCodeValue();

		if (respCode == 400) {
			LOG.warn("addNewUser method: The user's phone numbers are not unique");
			String failString = "The user's phone numbers are not unique";
			return failedAdding(failString, redirectAttributes, userGotten);
		}

		if (respCode == 201) {
			User user = responseEntity.getBody();

			if (user != null) {
				String successString = "User " + user.getFirstName() + " " + user.getLastName()
						+ " was added successfully";
				LOG.info("addNewUser method: User: {} was added successfully", user.toString());
				// redirecting to the list with all users
				LOG.info("addNewUser method: Redirecting to the user.jsp");
				redirectAttributes.addFlashAttribute("successString", successString);
				return "redirect:/show-all-users";
			}
		}
		return failedAdding("Failed adding. Please, try again", redirectAttributes, userGotten);
	}
	
	private String failedAdding(String failString, RedirectAttributes redirectAttributes, User user) {
		LOG.warn("failedAdding method was invoked because of: {}", failString);
		LOG.warn("failedAdding method: Redirecting to the addNewUser.jsp page");
		redirectAttributes.addFlashAttribute("failString", failString);
		redirectAttributes.addFlashAttribute("user", user);
		return "redirect:/add-new-user";
	}
		
	/*UPDATING*/
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
	@PostMapping(value = "/update-user")
	//@ResponseStatus(HttpStatus.CREATED)
	public String updateUser(@RequestParam(value = "id") Integer id,
			@RequestParam(value = "firstName", required = true) String firstName,
			@RequestParam(value = "lastName", required = true) String lastName,
			@RequestParam(value = "eMail", required = false) String eMail,
			@RequestParam (value = "number", required = true) List<String> numbers, 
			RedirectAttributes redirectAttributes) {
		LOG.info("updateUser method was invoked with parameters id: {}, firstName: {}, lastName: {}, eMail: {}, numbers: {}", 
				id, firstName, lastName, eMail, numbers.toString());

		//checking eMail
		if (!StringUtils.isBlank(eMail)) {
			boolean isEmailValid = true;
			
			try {
				InternetAddress emailAddr = new InternetAddress(eMail);
				emailAddr.validate();
			} 
			catch (AddressException ex) {
				isEmailValid = false;
			}
			
			if (!isEmailValid) {
				String failString = "Invalid E-mail";
				return failedUpdate(failString, id, redirectAttributes);
			}
		}
		
		//checking phone numbers
		numbers.removeIf(""::equals);
		
		if (numbers.isEmpty()) {
			String failString = "User must have at least one phone number";
			return failedUpdate(failString, id, redirectAttributes);
		}
		
		User userGotten = new User (firstName, lastName);
		userGotten.seteMail(eMail);
		userGotten.setId(id);
		userGotten.setPhoneNumbers(numbers);
				
		// updating by pageService
		//ResponseEntity <User> responseEntity = pageService.updateUser(id, firstName, lastName, eMail, numbers);
		ResponseEntity <User> responseEntity = pageService.updateUser(userGotten);
		int respCode = responseEntity.getStatusCodeValue();
		
		if (respCode == 400) {
			LOG.warn("The user's phone numbers are not unique");
			String failString = "The user's phone numbers are not unique";
			return failedUpdate(failString, id, redirectAttributes);
		}
		
		if (respCode == 200) {
			User user = responseEntity.getBody();
			String successString = "User " + user.getFirstName() + " " + user.getLastName()
					+ " was updated successfully";
			// redirecting to the list with all users		
			LOG.info("updateUser method: Redirecting to the user.jsp after updating User: {}", user.toString());
			redirectAttributes.addFlashAttribute( "successString", successString);
			return "redirect:/show-all-users";
		}
		
		return failedUpdate("Failed update. Please, try again with user " + id, id, redirectAttributes);
	}
	
	
	private String failedUpdate(String failString, Integer id, RedirectAttributes redirectAttributes) {
		LOG.warn("failedUpdate method was invoked for user with id: {}", id);
		LOG.warn("failedUpdate method: Redirecting to the updateUser.jsp page user id:{} with message {}", id, failString);
		redirectAttributes.addFlashAttribute("failString", failString);
		LOG.debug("failedUpdate method: Redirecting to url redirect:/update/{}", id);
		return "redirect:/update/" + id;
	}

	/*LOGIN*/
	@RequestMapping("/login")
	public String getLogin(@RequestParam(value="error", required = false) String error,
			@RequestParam(value="logout", required = false) String logout, 
			Model model) {
		LOG.info("getLogin method was invoked");
		model.addAttribute("error", error != null);
		model.addAttribute("logout", logout != null);
		
		return "login";
	}
	
	/*OTHER*/
	// sending to the first page
	@GetMapping("/")
	public ModelAndView getWelcomePage() {
		LOG.info("getWelcomePage method was invoked");
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("index");
		return modelAndView;
	}

	// date formatter that we send to the user and searchResult pages to display
	// dates correctly
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

		List<User> users = pageService.getUsersList();

		if (users != null) {
			modelAndView.addObject("usersList", users);
			formateDates(modelAndView);
			modelAndView.setViewName("user");
			return modelAndView;
		}

		modelAndView.setViewName("404");
		modelAndView.setStatus(HttpStatus.NOT_FOUND);
		return modelAndView;
	}
}