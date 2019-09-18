package my.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import my.app.controller.UserController;

//this service sends JSON request from page controller to user controller and send it's response to page controller
@Component
public class PageService {
	//private UserRepository userRepository = new UserRepositoryImpl();
	private UserController userController;

	@Autowired
	public PageService(UserController userController) {
		this.userController = userController;
	}
	
	
	
	
}
