package my.app.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import my.app.model.User;
import my.app.service.PageService;


public class PageControllerTest {
    private static final Logger LOG = LoggerFactory.getLogger(my.app.controller.PageControllerTest.class.getName());
    
	@Mock
    private PageService pageService;
	@InjectMocks
    private PageController pageController;
	private MockMvc mockMvc;
	private List<User> data;
	
	@Before
    public void setUp(){		
		LOG.info("setUp method was invoked");
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders
		        .standaloneSetup(pageController)
		        .build();
		data = new ArrayList<>();
		User aUser = new User ("AaFirstName", "AaLastName");
		aUser.setId(1);
		aUser.seteMail("a@a");
		aUser.setCreatedOn(LocalDateTime.of(2010, 10, 10, 10, 10));
		List<String> aNum = new ArrayList<>(Arrays.asList("11", "12", "13", "14"));
		aUser.setPhoneNumbers(aNum);
		data.add(aUser);
		
		User bUser = new User ("Bb'First-Name", "Bb'Last-Name");
		bUser.setId(2);
		bUser.setCreatedOn(LocalDateTime.of(2005, 5, 5, 5, 5));
		List<String> bNum = new ArrayList<>(Arrays.asList("21"));
		bUser.setPhoneNumbers(bNum);
		data.add(bUser);
		Mockito.clearInvocations(pageService);
    }
	
    @Test
    public void findUserPage() throws Exception {
    	LOG.info("findUserPage method was invoked");
    	MockitoAnnotations.initMocks(this);
    	mockMvc.perform(get("/find-user"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("findUser"));
    	
    }
    
    @Test
    public void getWelcomePage() throws Exception {
    	LOG.info("getWelcomePage method was invoked");

    	mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("index"));
    }
    
    @Test
    public void showUsersList_HappyPath() throws Exception {
    	LOG.info("showUsersList method was invoked");
    	when(pageService.getUsersList()).thenReturn(data);
    	
      	mockMvc.perform(get("/show-all-users"))
      		.andExpect(status().isOk())
      		.andExpect(MockMvcResultMatchers.view().name("user"))
      		.andExpect(MockMvcResultMatchers.model().attributeExists("usersList"));
      	 verify(pageService).getUsersList();
    }
    
    /*
    @GetMapping("/show-all-users")
	public ModelAndView showUsersList() {
		LOG.info("showUsersList method was invoked");
		ModelAndView modelAndView = new ModelAndView();

		List<User> users = pageService.getUsersList();

		if (users != null) {
			modelAndView.addObject("usersList", pageService.getUsersList());
			formateDates(modelAndView);
			modelAndView.setViewName("user");
			return modelAndView;
		}

		modelAndView.setViewName("404");
		return modelAndView;
	}*/
}
