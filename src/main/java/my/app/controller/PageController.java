package my.app.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
}
