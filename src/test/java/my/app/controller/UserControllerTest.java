package my.app.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
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
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.Assert.*;

import my.app.UserControllerTestConfiguration;
import my.app.model.User;
import my.app.service.UserService;


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
	
	//@Autowired
	@InjectMocks
    private UserController userController;
	
	@Autowired
	private ObjectMapper mapper;
	/*@Autowired
	private CloseableHttpClient client;
	@Autowired
	private String hostName;
	private String urlBeginsWith;*/
	private List<User> data;
	
	/*@PostConstruct
    public void postConstruct() {
		LOG.info("postConstruct method was invoked");
    	LOG.debug("Hostname in postConstruct: {}", hostName);
        urlBeginsWith = "http://" + hostName + ":8080/SpringRest/api/";
        LOG.debug("urlBeginsWith set in postConstruct: {}", urlBeginsWith);
    }
	
    @PreDestroy
    public void cleanUp() throws Exception {
		LOG.info("cleanUp method was invoked");
    	client.close();
    }*/
	
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
		/*
		Mockito.when(userServiceMock.getAllAsList()).thenReturn(data);
		Mockito.when(userServiceMock.getById(1)).thenReturn(aUser);
		Mockito.when(userServiceMock.getById(3)).thenReturn(null);
		Mockito.when(userServiceMock.getByLastName(Mockito.eq(bUser.getLastName()))).thenReturn(new ArrayList<>(Arrays.asList(bUser)));
		Mockito.when(userServiceMock.getByLastName("WrongLastName")).thenReturn(null);
		Mockito.when(userServiceMock.containsId(3)).thenReturn(false);
		Mockito.when(userServiceMock.containsId(1)).thenReturn(true);
		Mockito.doNothing().when(userServiceMock).deleteById(Mockito.any());
		*/
    }
	
	//DONE
    @Test
    public void getAllUsers() throws IOException {
    	LOG.info("getAllUsers method was invoked");
       	Mockito.when(userService.getAllAsList()).thenReturn(data);
       	ResponseEntity<List<User>> responseEntity = userController.getUsersList();
    	assertEquals(responseEntity.getBody(), data);
    	assertEquals(responseEntity.getStatusCodeValue(), 200);
    }
    
    @Test
	public void findUserById_ReturnsExistingUser(){
    	LOG.info("findUserById_ReturnsExistingUser method was invoked");
    	Mockito.when(userService.getById(1)).thenReturn(data.get(0));
    	Mockito.when(userService.containsId(1)).thenReturn(true);
    	List<User> actualList = userController.findUserById(1);
    	List<User> expected = new ArrayList<>(Arrays.asList(data.get(0)));
    	assertEquals(actualList, expected);
    }
	
    @Test (expected = ResponseStatusException.class)
	public void findUserById_ThrowsResponseStatusException(){
    	LOG.info("findUserById_ThrowsResponseStatusException method was invoked");
    	Mockito.when(userService.getById(3)).thenReturn(null);
    	Mockito.when(userService.containsId(3)).thenReturn(false);
    	List<User> actualList = userController.findUserById(3);
    	assertEquals(actualList, null);
    }
	
    @Test
	public void findUserByLastName_ReturnsExistingUser(){
    	LOG.info("findUserByLastName_ReturnsExistingUser method was invoked");
    	Mockito.when(userService.getByLastName("Bb'First-Name")).thenReturn(new ArrayList<>(Arrays.asList(data.get(1))));
    	List<User> actualList = userController.findUserByLastName("Bb'First-Name");
    	List<User> expected = new ArrayList<>(Arrays.asList(data.get(1)));
    	assertEquals(actualList, expected);
    }
	
    @Test (expected = ResponseStatusException.class)
	public void findUserByLastName_ThrowsResponseStatusException(){
    	LOG.info("findUserByLastName_ReturnsExistingUser method was invoked");
    	Mockito.when(userService.getByLastName("Ccc")).thenReturn(null);
    	List<User> actualList = userController.findUserByLastName("Ccc");
    	assertEquals(actualList, null);
    }
	
	
	
	
	
	
	
/*
	private ObjectMapper mapper;
	private CloseableHttpClient client;
	
	@Before
    public void setUp(){
		client = HttpClients.createDefault();
		mapper = new ObjectMapper();
    }
	
	@After
    public void tearDown(){
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	
	@Test
	public void GetUsersListTest() throws IOException{
		HttpGet httpGet = new HttpGet("http://localhost:8080/SpringRest/users");
		CloseableHttpResponse response = client.execute(httpGet);
		String string = EntityUtils.toString(response.getEntity());
		StringReader reader = new StringReader(string);
		User[] usersArr = mapper.readValue(reader, User[].class);
		int respCode = response.getStatusLine().getStatusCode();
		assertEquals(new Integer(1), usersArr[0].getId());
		assertEquals(200, respCode);
	}
	
	
	@Test
	public void GetUserByIdTest() throws IOException{
		HttpGet httpGet = new HttpGet("http://localhost:8080/SpringRest/user/1");
		CloseableHttpResponse response = client.execute(httpGet);
		String string = EntityUtils.toString(response.getEntity());
		StringReader reader = new StringReader(string);
		User[] usersArr = mapper.readValue(reader, User[].class);
		int respCode = response.getStatusLine().getStatusCode();
		assertEquals(new Integer(1), usersArr[0].getId());
		assertEquals(200, respCode);
	}
	
	@Test
	public void updateUserTest() throws IOException{
		String url = "http://localhost:8080/SpringRest/user/1";
		
		User user = new User ("Tom", "Tomson");
		user.seteMail("tomson@tom.com");
		user.setId(1);
		StringWriter writer = new StringWriter();
		mapper.writeValue(writer, user);
		HttpPut httpPut = new HttpPut(url);
		httpPut.setHeader("Accept", "application/json");
		httpPut.setHeader("Content-type", "application/json");
		StringEntity stringEntity = new StringEntity(writer.toString());
		httpPut.setEntity(stringEntity);
		CloseableHttpResponse response = client.execute(httpPut);
		User userUpdated = mapper.readValue(response.getEntity().getContent(), User.class);
		int respCode = response.getStatusLine().getStatusCode();
		user.setId(1);
		assertEquals(user, userUpdated);
		assertEquals(200, respCode);
		
	}
	
	*/
	
}
