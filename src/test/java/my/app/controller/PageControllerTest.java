package my.app.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.io.StringWriter;
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
import org.springframework.beans.factory.annotation.Autowired;

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


import com.fasterxml.jackson.databind.ObjectMapper;

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
	
	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	StringWriter writer;
	
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
    	List<String> numbers = Arrays.asList("123", "456", "789");
    	User user = new User ("CcFn", "CcLn");
    	user.setPhoneNumbers(numbers);
    	mapper.writeValue(writer, numbers);
    	
    	when(pageService.addUser(user)).thenReturn(ResponseEntity.status(201).body(user));

        //MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        //params.addAll("number", numbers);
        //params.put("eMail", Arrays.asList("C@c"));
        //params.put("firstName", Arrays.asList("CcFn"));
        //params.put("lastName", Arrays.asList("CcLn"));

    	mockMvc.perform(post("/add-new-user")
    			.param("firstName", user.getFirstName())
    			.param("lastName", user.getLastName())
    			.param("eMail", "C@c")
    			.param("number", writer.toString())
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
    	List<String> numbers = Arrays.asList("123", "456", "789");
    	User user = new User ("CcFn", "CcLn");
    	user.setPhoneNumbers(numbers);
    	user.seteMail("Cc");
    	mapper.writeValue(writer, numbers);

    	mockMvc.perform(post("/add-new-user")
    			.param("firstName", user.getFirstName())
    			.param("lastName", user.getLastName())
    			.param("eMail", user.geteMail())
    			.param("number", writer.toString())
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
    public void addNewUser_FailedAddingBecauseNumbersAreNotUnique_BadRequest() throws Exception {
    	LOG.info("addNewUser_FailedAddingBecauseNumbersAreNotUnique_BadRequest");
    	
    	//creating user for adding
    	List<String> numbers = Arrays.asList("123", "456", "789");
    	User user = new User ("CcFn", "CcLn");
    	user.setPhoneNumbers(numbers);
    	mapper.writeValue(writer, numbers);
    	
    	when(pageService.addUser(user)).thenReturn(ResponseEntity.status(400).body(null));
    	
    	mockMvc.perform(post("/add-new-user")
    			.param("firstName", user.getFirstName())
    			.param("lastName", user.getLastName())
    			.param("number", writer.toString())
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
    	List<String> numbers = Arrays.asList("123", "456", "789");
    	User user = new User ("CcFn", "CcLn");
    	user.setPhoneNumbers(numbers);
    	mapper.writeValue(writer, numbers);
    	
    	when(pageService.addUser(user)).thenReturn(ResponseEntity.status(404).body(null));
    	
    	mockMvc.perform(post("/add-new-user")
    			.param("firstName", user.getFirstName())
    			.param("lastName", user.getLastName())
    			.param("number", writer.toString())
    			)
                .andExpect(status().isFound())
    			.andExpect(MockMvcResultMatchers.redirectedUrl("/add-new-user"))
      			.andExpect(MockMvcResultMatchers.flash().attributeCount(2))
      			.andExpect(MockMvcResultMatchers.flash().attribute("failString", "Failed adding. Please, try again"))
    			.andExpect(MockMvcResultMatchers.flash().attribute("user", user));
    	
    	verify(pageService).addUser(user);
		verifyNoMoreInteractions(pageService);
	}
	
    /*
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
		userGotten.seteMail(eMail);

		// checking numbers
		numbers.removeIf(""::equals);
		
		if (numbers.isEmpty()) {
			String failString = "User must have at least one phone number";
			return failedAdding(failString, redirectAttributes, userGotten);
		}

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
	*/
}
