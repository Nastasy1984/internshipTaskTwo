package my.app.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import my.app.UserControllerTestConfiguration;
import my.app.model.User;
import my.app.service.PageService;

@Component
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=UserControllerTestConfiguration.class, loader=AnnotationConfigContextLoader.class)
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
    public void getWelcomePage() throws Exception {
    	LOG.info("getWelcomePage method was invoked");

    	mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("index"));
    }
    
    @SuppressWarnings("unchecked")
	@Test
    public void showUsersList_HappyPath() throws Exception {
    	LOG.info("showUsersList method was invoked");
    	when(pageService.getUsersList()).thenReturn(data);
    	
    	MvcResult result = mockMvc.perform(get("/show-all-users"))
      		.andExpect(status().isOk())
      		.andExpect(MockMvcResultMatchers.view().name("user"))
      		.andExpect(MockMvcResultMatchers.model().attributeExists("usersList"))
      		.andExpect(MockMvcResultMatchers.model().attributeExists("formatter"))
      		.andReturn();

    	List<User> userList = (List<User>) result.getModelAndView().getModelMap().get("usersList");
    	assertEquals(data, userList);
		verify(pageService).getUsersList();
      	verifyNoMoreInteractions(pageService);
    }
    
    @Test
    public void showUsersList_Returns404() throws Exception {
    	LOG.info("showUsersList method was invoked");
    	when(pageService.getUsersList()).thenReturn(null);
    	
      	mockMvc.perform(get("/show-all-users"))
      		.andExpect(status().isNotFound())
      		.andExpect(MockMvcResultMatchers.view().name("404"));
      	verify(pageService).getUsersList();
      	verifyNoMoreInteractions(pageService);
    }
    
    @Test
    public void findUserPage() throws Exception {
    	LOG.info("findUserPage method was invoked");
    	MockitoAnnotations.initMocks(this);
    	mockMvc.perform(get("/find-user"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("findUser"));
    }
    
    @SuppressWarnings("unchecked")
	@Test
    public void findUserById_HappyPath() throws Exception {
		LOG.info("findUserById_HappyPath method was invoked");
		when(pageService.getUserById(1)).thenReturn(new ArrayList<>(Arrays.asList(data.get(0))));
		
		MvcResult result = mockMvc.perform(get("/find-user/{id}", 1))
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("searchResult"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("users"))
			.andReturn();
		
		List<User> userList = (List<User>) result.getModelAndView().getModelMap().get("users");
		assertEquals(new ArrayList<User>(Arrays.asList(data.get(0))), userList);
		verify(pageService).getUserById(1);
      	verifyNoMoreInteractions(pageService);
    }
    
	@Test
    public void findUserById_failedSearch() throws Exception {
		LOG.info("findUserById_failedSearch method was invoked");
		when(pageService.getUserById(5)).thenReturn(null);
		
		mockMvc.perform(get("/find-user/{id}", 5))
  			.andExpect(MockMvcResultMatchers.redirectedUrl("/find-user"))
			.andExpect(status().isFound())
  			.andExpect(MockMvcResultMatchers.flash().attributeCount(1))
  			.andExpect(MockMvcResultMatchers.flash().attribute("failString", "Failed to find user 5"));

		verify(pageService).getUserById(5);
      	verifyNoMoreInteractions(pageService);
    }
	
    @SuppressWarnings("unchecked")
	@Test
    public void findUserByLastName_HappyPath() throws Exception {
		LOG.info("findUserByLastName_HappyPath method was invoked");
		when(pageService.getUserByLastName("Bb'Last-Name")).thenReturn(new ArrayList<>(Arrays.asList(data.get(1))));
		
		MvcResult result = mockMvc.perform(get("/find-user/{lastName}", "Bb'Last-Name"))
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("searchResult"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("users"))
			.andReturn();

		List<User> userList = (List<User>) result.getModelAndView().getModelMap().get("users");
		assertEquals(new ArrayList<User>(Arrays.asList(data.get(1))), userList);
		verify(pageService).getUserByLastName("Bb'Last-Name");
		verifyNoMoreInteractions(pageService);
    }
	
	@Test
    public void findUserByLastName_failedSearch() throws Exception {
		LOG.info("findUserById_failedSearch method was invoked");
		when(pageService.getUserByLastName("Cc'Last-Name")).thenReturn(null);
		
		mockMvc.perform(get("/find-user/{lastName}", "Cc'Last-Name"))
  			.andExpect(MockMvcResultMatchers.redirectedUrl("/find-user"))
			.andExpect(status().isFound())
  			.andExpect(MockMvcResultMatchers.flash().attributeCount(1))
  			.andExpect(MockMvcResultMatchers.flash().attribute("failString", "Failed to find user Cc'Last-Name"));

		verify(pageService).getUserByLastName("Cc'Last-Name");
      	verifyNoMoreInteractions(pageService);
    }

	@Test
    public void deleteUser_HappyPath() throws Exception {
		LOG.info("deleteUser_HappyPath method was invoked");
		
		when(pageService.deleteUser(2)).thenReturn(200);
		
    	mockMvc.perform(get("/delete/{id}", 2))
			.andExpect(MockMvcResultMatchers.redirectedUrl("/show-all-users"))
			.andExpect(status().isFound())
  			.andExpect(MockMvcResultMatchers.flash().attributeCount(1))
  			.andExpect(MockMvcResultMatchers.flash().attribute("successString", "User with id "+ 2 +" was deleted successfully"));;
	}
   
	@Test
    public void deleteUser_FailedDeleting() throws Exception {
		LOG.info("deleteUser_HappyPath method was invoked");
		
		when(pageService.deleteUser(5)).thenReturn(404);
		
    	mockMvc.perform(get("/delete/{id}", 5))
			.andExpect(MockMvcResultMatchers.redirectedUrl("/show-all-users"))
			.andExpect(status().isFound())
  			.andExpect(MockMvcResultMatchers.flash().attributeCount(1))
  			.andExpect(MockMvcResultMatchers.flash().attribute("successString", "Failed to delete user with id " + 5));;
	}
	
	@Test
    public void addNewUserPage_ReturnsAddNewUserPage() throws Exception {
    	LOG.info("addNewUserPage_ReturnsAddNewUserPage was invoked");

    	mockMvc.perform(get("/add-new-user"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("addNewUser"));
	}

	@Test
    public void addNewUser_HappyPath() throws Exception {
    	LOG.info("addNewUser_HappyPath method was invoked");
    	//creating user for adding
    	List<String> numbers = Arrays.asList("123");
    	User user = new User ("CcFn", "CcLn");
    	user.setPhoneNumbers(numbers);
    	user.seteMail("C@c");
    	
    	when(pageService.addUser(any(User.class))).thenReturn(ResponseEntity.status(201).body(user));

        //MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        //params.addAll("number", numbers);
        //params.put("eMail", Arrays.asList("C@c"));
        //params.put("firstName", Arrays.asList("CcFn"));
        //params.put("lastName", Arrays.asList("CcLn"));

    	mockMvc.perform(post("/add-new-user")
    			.param("firstName", user.getFirstName())
    			.param("lastName", user.getLastName())
    			.param("eMail", user.geteMail())
    			.param("number", "123")
    	        //.params(params)
    			)
                .andExpect(status().isFound())
    			.andExpect(MockMvcResultMatchers.redirectedUrl("/show-all-users"))
      			.andExpect(MockMvcResultMatchers.flash().attributeCount(1))
      			.andExpect(MockMvcResultMatchers.flash().attribute("successString", "User " + user.getFirstName() + " " + user.getLastName()
				+ " was added successfully"));
    	
    	verify(pageService).addUser(user);
		verifyNoMoreInteractions(pageService);
	}

	@Test
    public void addNewUser_InvalidEmail() throws Exception {
    	LOG.info("addNewUser_InvalidEmail method was invoked");
    	
    	//creating user for adding
    	List<String> numbers = Arrays.asList("123");
    	User user = new User ("CcFn", "CcLn");
    	user.setPhoneNumbers(numbers);
    	user.seteMail("Cc");

    	mockMvc.perform(post("/add-new-user")
    			.param("firstName", user.getFirstName())
    			.param("lastName", user.getLastName())
    			.param("eMail", user.geteMail())
    			.param("number", "123")
    			)
                .andExpect(status().isFound())
    			.andExpect(MockMvcResultMatchers.redirectedUrl("/add-new-user"))
      			.andExpect(MockMvcResultMatchers.flash().attributeCount(2))
      			.andExpect(MockMvcResultMatchers.flash().attribute("failString", "Invalid E-mail"))
    			.andExpect(MockMvcResultMatchers.flash().attribute("user", user));
    	
		verifyNoMoreInteractions(pageService);
	}

	@Test
    public void addNewUser_FailedBecauseNumbersAreEmpty() throws Exception {
    	LOG.info("addNewUser_FailedBecauseNumbersAreEmpty method was invoked");
    	
    	//creating user for adding
    	User user = new User ("CcFn", "CcLn");
    	
    	mockMvc.perform(post("/add-new-user")
    			.param("firstName", user.getFirstName())
    			.param("lastName", user.getLastName())
    			.param("number", "")
    			)
                .andExpect(status().isFound())
    			.andExpect(MockMvcResultMatchers.redirectedUrl("/add-new-user"))
      			.andExpect(MockMvcResultMatchers.flash().attributeCount(2))
      			.andExpect(MockMvcResultMatchers.flash().attribute("failString", "User must have at least one phone number"))
    			.andExpect(MockMvcResultMatchers.flash().attribute("user", user));
    	
		verifyNoMoreInteractions(pageService);
	}

	@Test
    public void addNewUser_FailedBecauseNumbersAreNotUnique_BadRequest() throws Exception {
    	LOG.info("addNewUser_FailedBecauseNumbersAreNotUnique_BadRequest");
    	
    	//creating user for adding
    	User user = new User ("CcFn", "CcLn");
    	List<String> numbers = Arrays.asList("123");
    	user.setPhoneNumbers(numbers);
    	
    	when(pageService.addUser(any(User.class))).thenReturn(ResponseEntity.status(400).body(null));
    	
    	mockMvc.perform(post("/add-new-user")
    			.param("firstName", user.getFirstName())
    			.param("lastName", user.getLastName())
    			.param("number", "123")
    			)
                .andExpect(status().isFound())
    			.andExpect(MockMvcResultMatchers.redirectedUrl("/add-new-user"))
      			.andExpect(MockMvcResultMatchers.flash().attributeCount(2))
      			.andExpect(MockMvcResultMatchers.flash().attribute("failString", "The user's phone numbers are not unique"))
    			.andExpect(MockMvcResultMatchers.flash().attribute("user", user));
    	
    	verify(pageService).addUser(user);
		verifyNoMoreInteractions(pageService);
	}
	
	@Test
    public void addNewUser_FailedAddingBecauseOfGetting404Error() throws Exception {
    	LOG.info("addNewUser_FailedAddingBecauseOfGetting404Error");
    	
    	//creating user for adding
    	List<String> numbers = Arrays.asList("123");
    	User user = new User ("CcFn", "CcLn");
    	user.setPhoneNumbers(numbers);
    	
    	when(pageService.addUser(any(User.class))).thenReturn(ResponseEntity.status(404).body(null));
    	
    	mockMvc.perform(post("/add-new-user")
    			.param("firstName", user.getFirstName())
    			.param("lastName", user.getLastName())
    			.param("number", "123")
    			)
                .andExpect(status().isFound())
    			.andExpect(MockMvcResultMatchers.redirectedUrl("/add-new-user"))
      			.andExpect(MockMvcResultMatchers.flash().attributeCount(2))
      			.andExpect(MockMvcResultMatchers.flash().attribute("failString", "Failed adding. Please, try again"))
    			.andExpect(MockMvcResultMatchers.flash().attribute("user", user));
    	
    	verify(pageService).addUser(user);
		verifyNoMoreInteractions(pageService);
	}

	@Test
    public void updateUser_HappyPath() throws Exception {
    	LOG.info("updateUser_HappyPath method was invoked");
    	//creating user for adding
    	List<String> numbers = Arrays.asList("123");
    	User user = new User ("CcFn", "CcLn");
    	user.setPhoneNumbers(numbers);
    	user.setId(3);
    	when(pageService.updateUser(user)).thenReturn(ResponseEntity.status(200).body(user));

    	mockMvc.perform(post("/update-user")
    			.param("firstName", user.getFirstName())
    			.param("lastName", user.getLastName())
    			.param("number", "123")
    			.param("id", user.getId().toString())
    			)
                .andExpect(status().isFound())
    			.andExpect(MockMvcResultMatchers.redirectedUrl("/show-all-users"))
      			.andExpect(MockMvcResultMatchers.flash().attributeCount(1))
      			.andExpect(MockMvcResultMatchers.flash().attribute("successString", "User " + user.getFirstName() + " " + user.getLastName()
				+ " was updated successfully"));
    	
    	verify(pageService).updateUser(user);
		verifyNoMoreInteractions(pageService);
	}
	
	
	@Test
    public void updateUser_InvalidEmail() throws Exception {
    	LOG.info("updateUser_InvalidEmail method was invoked");
    	//creating user for adding
    	User user = new User ("CcFn", "CcLn");
    	user.setId(3);

    	mockMvc.perform(post("/update-user")
    			.param("firstName", user.getFirstName())
    			.param("lastName", user.getLastName())
    			.param("eMail", "Cc")
    			.param("number", "123")
    			.param("id", user.getId().toString())
    			)
                .andExpect(status().isFound())
    			.andExpect(MockMvcResultMatchers.redirectedUrl("/update/" + user.getId()))
      			.andExpect(MockMvcResultMatchers.flash().attributeCount(1))
      			.andExpect(MockMvcResultMatchers.flash().attribute("failString", "Invalid E-mail"));
    	
		verifyNoMoreInteractions(pageService);
	}
	
	@Test
    public void updateUser_FailedBecauseNumbersAreEmpty() throws Exception {
    	LOG.info("updateUser_FailedBecauseNumbersAreEmpty method was invoked");
    	//creating user for adding
    	User user = new User ("CcFn", "CcLn");
    	user.setId(3);

    	mockMvc.perform(post("/update-user")
    			.param("firstName", user.getFirstName())
    			.param("lastName", user.getLastName())
    			.param("number", "")
    			.param("id", user.getId().toString())
    			)
                .andExpect(status().isFound())
    			.andExpect(MockMvcResultMatchers.redirectedUrl("/update/" + user.getId()))
      			.andExpect(MockMvcResultMatchers.flash().attributeCount(1))
      			.andExpect(MockMvcResultMatchers.flash().attribute("failString", "User must have at least one phone number"));
    	
		verifyNoMoreInteractions(pageService);
	}
	
	@Test
    public void updateUser_FailedBecauseNumbersAreNotUnique_BadRequest() throws Exception {
    	LOG.info("updateUser_failedBecauseNumbersAreNotUnique_BadRequest method was invoked");
    	//creating user for adding
    	User user = new User ("CcFn", "CcLn");
    	user.setId(3);
    	List<String> numbers = Arrays.asList("123");
    	user.setPhoneNumbers(numbers);
    	when(pageService.updateUser(any(User.class))).thenReturn(ResponseEntity.status(400).body(null));
    	mockMvc.perform(post("/update-user")
    			.param("firstName", user.getFirstName())
    			.param("lastName", user.getLastName())
    			.param("number", "123")
    			.param("id", user.getId().toString())
    			)
                .andExpect(status().isFound())
    			.andExpect(MockMvcResultMatchers.redirectedUrl("/update/" + user.getId()))
      			.andExpect(MockMvcResultMatchers.flash().attributeCount(1))
      			.andExpect(MockMvcResultMatchers.flash().attribute("failString", "The user's phone numbers are not unique"));
    	
    	verify(pageService).updateUser(user);
		verifyNoMoreInteractions(pageService);
	}
	
	@Test
    public void updateUser_FailedAddingBecauseOfGetting404Error() throws Exception {
    	LOG.info("updateUser_FailedAddingBecauseOfGetting404Error method was invoked");
    	//creating user for adding
    	User user = new User ("CcFn", "CcLn");
    	user.setId(3);
    	List<String> numbers = Arrays.asList("123");
    	user.setPhoneNumbers(numbers);
    	when(pageService.updateUser(any(User.class))).thenReturn(ResponseEntity.status(404).body(null));
    	mockMvc.perform(post("/update-user")
    			.param("firstName", user.getFirstName())
    			.param("lastName", user.getLastName())
    			.param("number", "123")
    			.param("id", user.getId().toString())
    			)
                .andExpect(status().isFound())
    			.andExpect(MockMvcResultMatchers.redirectedUrl("/update/" + user.getId()))
      			.andExpect(MockMvcResultMatchers.flash().attributeCount(1))
      			.andExpect(MockMvcResultMatchers.flash().attribute("failString", "Failed update. Please, try again with user " + user.getId()));
    	
    	verify(pageService).updateUser(user);
		verifyNoMoreInteractions(pageService);
	}

}
