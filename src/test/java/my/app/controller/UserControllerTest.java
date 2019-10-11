package my.app.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.web.server.ResponseStatusException;

import my.app.UserControllerTestConfiguration;
import my.app.model.User;
import my.app.service.UserService;


/*Here I test UserController just for write return value. To test UC for right type and response code look at UserControllerTestRest class*/

//DONE by https://spring.io/blog/2011/06/21/spring-3-1-m2-testing-with-configuration-classes-and-profiles/
@Component
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
//ApplicationContext will be loaded from the static inner ContextConfiguration class
@ContextConfiguration(classes=UserControllerTestConfiguration.class, loader=AnnotationConfigContextLoader.class)
public class UserControllerTest {

	private static final Logger LOG = LoggerFactory.getLogger(my.app.controller.UserControllerTest.class.getName());
	@Autowired
	//@Mock
    private UserService userService;
	@InjectMocks
    private UserController userController;
	private List<User> data;
	
	@Before
    public void setUp(){		
		LOG.info("setUp method was invoked");
		MockitoAnnotations.initMocks(this);
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
    }
	
    @Test
    public void getAllUsers() throws IOException {
    	LOG.info("getAllUsers method was invoked");
       	when(userService.getAllAsList()).thenReturn(data);
       	ResponseEntity<List<User>> responseEntity = userController.getUsersList();
       	verify(userService).getAllAsList();
    	assertEquals(responseEntity.getBody(), data);
    	assertEquals(responseEntity.getStatusCodeValue(), 200);
    }

    @Test
	public void findUserById_ReturnsExistingUser(){
    	LOG.info("findUserById_ReturnsExistingUser method was invoked");
    	when(userService.getById(1)).thenReturn(data.get(0));
    	when(userService.containsId(1)).thenReturn(true);
    	List<User> actualList = userController.findUserById(1);
    	List<User> expected = new ArrayList<>(Arrays.asList(data.get(0)));
    	assertEquals(actualList, expected);
    }

    @Test (expected = ResponseStatusException.class)
	public void findUserById_ThrowsResponseStatusException(){
    	LOG.info("findUserById_ThrowsResponseStatusException method was invoked");
    	when(userService.getById(3)).thenReturn(null);
    	when(userService.containsId(3)).thenReturn(false);
    	List<User> actualList = userController.findUserById(3);
    	verify(userService).getById(3);
    	verify(userService).containsId(3);
    	assertEquals(actualList, null);
    }
	   
    @Test
	public void findUserByLastName_ReturnsExistingUser(){
    	LOG.info("findUserByLastName_ReturnsExistingUser method was invoked");
    	when(userService.getByLastName("Bb'First-Name")).thenReturn(new ArrayList<>(Arrays.asList(data.get(1))));
    	List<User> actualList = userController.findUserByLastName("Bb'First-Name");
    	List<User> expected = new ArrayList<>(Arrays.asList(data.get(1)));
    	verify(userService).getByLastName("Bb'First-Name");
    	assertEquals(actualList, expected);
    }
	
    @Test (expected = ResponseStatusException.class)
	public void findUserByLastName_ThrowsResponseStatusException(){
    	LOG.info("findUserByLastName_ThrowsResponseStatusException method was invoked");
    	when(userService.getByLastName("Ccc")).thenReturn(null);
    	List<User> actualList = userController.findUserByLastName("Ccc");
    	verify(userService).getByLastName("Ccc");
    	assertEquals(actualList, null);
    }
		
    @Test 
	public void delete_HappyPath(){
    	LOG.info("delete_HappyPath method was invoked");
    	when(userService.containsId(1)).thenReturn(true);
    	doNothing().when(userService).deleteById(any());
    	userController.delete(1);
    	verify(userService).containsId(1);
    	verify(userService).deleteById(1);
    	assertEquals(0, 0);
    }
  
    @Test (expected = ResponseStatusException.class)
	public void delete_ThrowsResponseStatusException(){
    	LOG.info("delete_ThrowsResponseStatusException method was invoked");
    	when(userService.containsId(3)).thenReturn(false);
    	doNothing().when(userService).deleteById(any());
    	userController.delete(3);
    } 
    
    @Test 
	public void addNewUser_HappyPath(){
    	LOG.info("addNewUser_HappyPath method was invoked");
    	User user = new User("CcFN","CcLN");
    	user.setPhoneNumbers(new ArrayList<>(Arrays.asList("31")));
    	when(userService.checkNumbers(user.getPhoneNumbers(), 0)).thenReturn(true);
    	when(userService.save(user)).thenReturn(user);
    	ResponseEntity<User> responseEntity = userController.addNewUser(user);
    	assertEquals(responseEntity.getStatusCodeValue(), 200);
    	assertEquals(responseEntity.getBody(), user);	
    }
    
    @Test 
	public void addNewUser_InvalidLastName(){
    	LOG.info("addNewUser_InvalidLastName method was invoked");
    	User user = new User("CcFN","123");
    	user.setPhoneNumbers(new ArrayList<>(Arrays.asList("31")));
    	ResponseEntity<User> responseEntity = userController.addNewUser(user);
    	assertEquals(responseEntity.getStatusCodeValue(), 400);
    	assertEquals(responseEntity.getBody(), null);	
    }
    
    @Test 
	public void addNewUser_NotUniqueNumbers(){
    	LOG.info("addNewUser_NotUniqueNumbers method was invoked");
    	User user = new User("CcFN","CcLn");
    	user.setPhoneNumbers(new ArrayList<>(Arrays.asList("11")));
    	when(userService.checkNumbers(user.getPhoneNumbers(), 0)).thenReturn(false);
    	ResponseEntity<User> responseEntity = userController.addNewUser(user);
 	    verify(userService, times(0)).save(any());
    	assertEquals(responseEntity.getStatusCodeValue(), 400);
    	assertEquals(responseEntity.getBody(), null);	
    }
    
    @Test (expected = ResponseStatusException.class)
	public void addNewUser_ThrowsResponseStatusException(){
    	LOG.info("addNewUser_ThrowsResponseStatusException method was invoked");
    	User user = new User("CcFN","CcLn");
    	user.setPhoneNumbers(new ArrayList<>(Arrays.asList("11")));
    	when(userService.checkNumbers(user.getPhoneNumbers(), 0)).thenReturn(true);
    	when(userService.save(user)).thenReturn(null);
    	userController.addNewUser(user);
    	verify(userService, times(1)).checkNumbers(user.getPhoneNumbers(), 0);
    	verify(userService, times(1)).save(any());
    }
    
    @Test
    public void updateUser_HappyPath() {
    	LOG.info("updateUser_HappyPath method was invoked");
    	User user = new User("CcFN","CcLn");
    	user.setPhoneNumbers(new ArrayList<>(Arrays.asList("31")));
    	user.setId(1);
    	user.seteMail("eMail@Test.com");
    	user.setCreatedOn(LocalDateTime.of(2010, 10, 10, 10, 10));
    	when(userService.checkNumbers(user.getPhoneNumbers(), user.getId())).thenReturn(true);
    	when(userService.update(user)).thenReturn(user);
    	ResponseEntity<User> responseEntity = userController.updateUser(user.getId(), user);
    	assertEquals(responseEntity.getStatusCodeValue(), 200);
    	assertEquals(responseEntity.getBody(), user);
    }

    @Test 
	public void updateUser_InvalidLastName(){
    	LOG.info("updateUser_InvalidLastName method was invoked");
    	User user = new User("CcFN","/8");
    	user.setPhoneNumbers(new ArrayList<>(Arrays.asList("31")));
    	user.setId(1);
    	user.seteMail("eMail@Test.com");
    	user.setCreatedOn(LocalDateTime.of(2010, 10, 10, 10, 10));
    	ResponseEntity<User> responseEntity = userController.updateUser(user.getId(), user);
    	assertEquals(responseEntity.getStatusCodeValue(), 400);
    	assertEquals(responseEntity.getBody(), null);	
    }
   
    @Test 
	public void updateUser__NotUniqueNumbers(){
    	LOG.info("updateUser_NotUniqueNumbers method was invoked");
    	User user = new User("CcFN","CcLn");
    	user.setPhoneNumbers(new ArrayList<>(Arrays.asList("11")));
    	user.setId(1);
    	user.seteMail("eMail@Test.com");
    	user.setCreatedOn(LocalDateTime.of(2010, 10, 10, 10, 10));   	
    	when(userService.checkNumbers(user.getPhoneNumbers(), user.getId())).thenReturn(false);
    	ResponseEntity<User> responseEntity = userController.updateUser(user.getId(), user);
    	assertEquals(responseEntity.getStatusCodeValue(), 400);
    	assertEquals(responseEntity.getBody(), null);    	
    }
    
    @Test (expected = ResponseStatusException.class)
	public void updateUser_ThrowsResponseStatusException(){
    	LOG.info("updateUser_ThrowsResponseStatusException method was invoked");
    	User user = new User("CcFN","CcLn");
    	user.setPhoneNumbers(new ArrayList<>(Arrays.asList("11")));
    	user.setId(1);
    	user.seteMail("eMail@Test.com");
    	user.setCreatedOn(LocalDateTime.of(2010, 10, 10, 10, 10));   	
    	when(userService.checkNumbers(user.getPhoneNumbers(), user.getId())).thenReturn(true);
    	when(userService.update(user)).thenReturn(null);
    	userController.updateUser(user.getId(), user);
    	verify(userService, times(1)).checkNumbers(user.getPhoneNumbers(), user.getId());
    	verify(userService, times(1)).update(any());
    }
}
