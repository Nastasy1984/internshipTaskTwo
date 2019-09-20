package my.app.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


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
	
	//DONE finding user by id
	@GetMapping("/find-user-by-last-name")
    public ModelAndView findUserByLastName(@RequestParam(value="lastName", required=true) String lastName) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("users", pageService.getUserByLastName(lastName));
		modelAndView.setViewName("searchResult");
        return modelAndView;
    }
	
}
