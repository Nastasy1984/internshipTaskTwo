package my.app.controller;

import javax.persistence.Id;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
		modelAndView.addObject("users", pageService.getUserById(id));
		modelAndView.setViewName("searchResult");
        return modelAndView;
    }
	
	//DONE WORKS finding user by id
	@GetMapping("/find-user-by-last-name")
    public ModelAndView findUserByLastName(@RequestParam(value="lastName", required=true) String lastName) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("users", pageService.getUserByLastName(lastName));
		modelAndView.setViewName("searchResult");
        return modelAndView;
    }
		
	//DONE getting parameters of user and adding user to the users list 
	//sending to the page with user
	//TODO Exeptions
    @PostMapping(value ="/add-new-user")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ModelAndView addNewUser(@RequestParam(value="firstName", required=true) String firstName, 
    		@RequestParam(value="lastName", required=true) String lastName) {
    	ModelAndView modelAndView = new ModelAndView();

    	if ((firstName==null) || (lastName==null) || (lastName.equals("")) || (firstName.equals(""))) {
    		modelAndView.setViewName("addNewUser");
    		String failString = "All fields in form must be correctly filled";
    		modelAndView.addObject("failString", failString);
    		return modelAndView;
    	}
    	//saving the user by pageService
    	User user = pageService.addUser(firstName, lastName);
    	String successString = "User " + user.getLastName() + " " + user.getFirstName() + " was added successfully";
    	//redirect to the list with all users
		modelAndView.addObject("usersList", pageService.getUsersList());
		modelAndView.addObject("successString", successString);
		modelAndView.setViewName("user");
        return modelAndView;
    }
	
}
