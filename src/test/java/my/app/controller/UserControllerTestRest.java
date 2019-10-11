package my.app.controller;

import static org.mockito.Mockito.*;

import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import static org.springframework.http.HttpStatus.OK;

import com.fasterxml.jackson.databind.ObjectMapper;

import my.app.UserControllerTestConfiguration;
import my.app.model.User;
import my.app.service.UserService;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;


/*Testing UserController as a rest service*/

@Component
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
//ApplicationContext will be loaded from the static inner ContextConfiguration class
@ContextConfiguration(classes=UserControllerTestConfiguration.class, loader=AnnotationConfigContextLoader.class)
public class UserControllerTestRest {
	private static final Logger LOG = LoggerFactory.getLogger(my.app.controller.UserControllerTestRest.class.getName());
	
	@Autowired
    private UserService userService;
	
	@InjectMocks
    private UserController userController;

	private List<User> data;
	
	@Autowired
	private ObjectMapper mapper;

	
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
		RestAssuredMockMvc.standaloneSetup(userController);
    }
	
	@Test
	public void test_get_all_success() throws Exception {
		when(userService.getAllAsList()).thenReturn(data);
		StringWriter writer = new StringWriter();
		mapper.writeValue(writer, data);
		given()
	      .when()
	        .get("/api/users")
	      .then()
	        .log().ifValidationFails()
	        .statusCode(OK.value())
	        .contentType(JSON)
	        .body(is(equalTo(writer.toString())));
	    verify(userService).getAllAsList();
	    verifyNoMoreInteractions(userService);
	}
}
