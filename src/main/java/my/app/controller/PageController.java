package my.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
	
	//private UserService userService = new UserServiceImpl();
	private PageService pageService;
	
	@Autowired
	public PageController(PageService pageService) {
		this.pageService = pageService;
	}

	//sending to the first page
	@GetMapping("/")
	public ModelAndView getWelcomePage() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("index");
		return modelAndView;
	}
	
	//sending to the jsp page addNewUser
	@GetMapping("/add-new-user")
    public String addNewUserPage() {
        return "addNewUser";
    }

	
	//sending to the jsp page findUser
	@GetMapping("/find-user")
    public String findUserPage() {
        return "findUser";
    }
	
	//DONE WORKS providing the list of all users
	@GetMapping("/show-all-users")
    public ModelAndView showUsersList() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("usersList", pageService.getUsersList());
		modelAndView.setViewName("user");
        return modelAndView;
    }
	
	//DONE WORKS finding user by id
	@GetMapping("/find-user/{id:\\d+}")
    public ModelAndView findUserById(@PathVariable ("id") Integer id) {
		ModelAndView modelAndView = new ModelAndView();
		List <User> users = pageService.getUserById(id);
		
		if (users==null) {
			return failedSearch(id.toString());
		}
	
		modelAndView.addObject("users", users);
		modelAndView.setViewName("searchResult");
        return modelAndView;
    }
	
	
	@GetMapping("/find-user/{lastName:\\D+}")
    public ModelAndView findUserByLastName(@PathVariable ("lastName") String lastName) {
		ModelAndView modelAndView = new ModelAndView();
		List <User> usersList = pageService.getUserByLastName(lastName);
		
		if (usersList==null) {
			return failedSearch(lastName);
		}
		
		modelAndView.addObject("users", usersList);
		modelAndView.setViewName("searchResult");
        return modelAndView;
    }
	
	
	public ModelAndView failedSearch(String userString) {
		ModelAndView modelAndView = new ModelAndView();
		String failString = "Failed to find user " + userString;
		modelAndView.setViewName("findUser");
		modelAndView.addObject("failString", failString);
		return modelAndView;
    }

	//DONE WORKS getting parameters of user and adding user to the users list 
	//sending to the page with users
    @PostMapping(value ="/add-new-user")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ModelAndView addNewUser(@RequestParam(value="firstName", required=true) String firstName, 
    		@RequestParam(value="lastName", required=true) String lastName, @RequestParam(value="eMail", required=false) String eMail) {
    	ModelAndView modelAndView = new ModelAndView();

    	if ((firstName==null) || (lastName==null) || (lastName.equals("")) || (firstName.equals(""))) {
    		modelAndView.setViewName("addNewUser");
    		String failString = "First name and last name are obligatory fields";
    		modelAndView.addObject("failString", failString);
    		return modelAndView;
    	}
    	if (eMail==null) {
    		eMail = "";
    	}
    	//saving the user by pageService
    	User user = pageService.addUser(firstName, lastName, eMail);
    	String successString = "User " + user.getFirstName() + " " + user.getLastName() + " was added successfully";
    	//redirect to the list with all users
		modelAndView.addObject("usersList", pageService.getUsersList());
		modelAndView.addObject("successString", successString);
		modelAndView.setViewName("user");
        return modelAndView;
    }
   
    //DONE deleting user 
    @GetMapping(value ="/delete/{id:\\d+}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ModelAndView deleteUser(@PathVariable ("id") Integer id) {
    	ModelAndView modelAndView = new ModelAndView();
    	
    	int respCode = pageService.deleteUser(id);
		if (respCode == 200) {
			String successString = "User with id " + id + " was deleted successfully";
			modelAndView.addObject("successString", successString);
		}
		else {
			String failString = "Failed to delete user with id " + id;
			modelAndView.addObject("successString", failString);
		}
    	modelAndView.addObject("usersList", pageService.getUsersList());
		modelAndView.setViewName("user");
        return modelAndView;
    }
    
	//DONE WORKS sending to the user\s updating page
	@GetMapping(value ="/update/{id:\\d+}")
    public ModelAndView updateUserPage(@PathVariable ("id") Integer id) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("updateUser");
		modelAndView.addObject("id", id);
		List <User> users = pageService.getUserById(id);
		modelAndView.addObject("user", users.get(0));
        return modelAndView;
    }
	
	
	//DONE getting parameters of user and updating user in the users list 
	//sending to the page with users
    @PostMapping(value ="/update-user")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ModelAndView updateUser(@RequestParam(value="id") Integer id, @RequestParam(value="firstName", required=false) String firstName, 
    		@RequestParam(value="lastName", required=false) String lastName, @RequestParam(value="eMail", required=false) String eMail) {
    	ModelAndView modelAndView = new ModelAndView();
    	if ((lastName.equals("")) && (firstName.equals("")) && (eMail.equals(""))) {
    		modelAndView.setViewName("updateUser");
    		modelAndView.addObject("id", id);
    		String failString = "At least one field in the form must be filled";
    		modelAndView.addObject("failString", failString);
    		return modelAndView;
    	}

    	User user = pageService.updateUser(id, firstName, lastName, eMail);	
    	
    	String successString = "User " + user.getFirstName() + " " + user.getLastName() + " was updated successfully";
    	//redirect to the list with all users
		modelAndView.addObject("usersList", pageService.getUsersList());
		modelAndView.addObject("successString", successString);
		modelAndView.setViewName("user");
        return modelAndView;
    }
	
	/*
	
	//DONE WORKS finding user by id
	@GetMapping("/find-user/{id:\\D+}")
	public ModelAndView findUserByIdWrongInput(@PathVariable("id") String wrongId) {
		ModelAndView modelAndView = new ModelAndView();
		String failString = "Please enter number to the id field ";
		modelAndView.setViewName("findUser");
		modelAndView.addObject("failString", failString);
		return modelAndView;
	}
	
	
	//DONE WORKS finding user by id
	@GetMapping("/find-user-by-last-name")
    public ModelAndView findUserByLastName(@RequestParam(value="lastName", required=true) String lastName) {
		ModelAndView modelAndView = new ModelAndView();
		List <User> usersList = pageService.getUserByLastName(lastName);
		if (usersList==null) {
			String failString = "Failed to find user with last name " + lastName;
			modelAndView.setViewName("findUser");
			modelAndView.addObject("failString", failString);
			return modelAndView;
		}
		modelAndView.addObject("users", usersList);
		modelAndView.setViewName("searchResult");
        return modelAndView;
    }*/
		
}
