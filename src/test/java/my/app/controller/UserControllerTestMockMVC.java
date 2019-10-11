package my.app.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import my.app.UserControllerTestConfiguration;
import my.app.model.User;
import my.app.service.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Component
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=UserControllerTestConfiguration.class, loader=AnnotationConfigContextLoader.class)
public class UserControllerTestMockMVC {
	
	private static final Logger LOG = LoggerFactory.getLogger(my.app.controller.UserControllerTestMockMVC.class.getName());
	
	@Autowired
    private UserService userService;
	
	@InjectMocks
    private UserController userController;

	private List<User> data;
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	StringWriter writer;
	
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
		
		mockMvc = MockMvcBuilders
		        .standaloneSetup(userController)
		        .build();

    }
	
    @Test
	public void getAllUsers() throws Exception{
    	LOG.info("getAllUsers method was invoked");
       	when(userService.getAllAsList()).thenReturn(data);
       	
    	MvcResult result = mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE ))
                .andReturn();
    	
    	String content = result.getResponse().getContentAsString();
		writer = new StringWriter();
		mapper.writeValue(writer, data);
		assertEquals(writer.toString(), content);
	    verify(userService).getAllAsList();
    }
	
	@Test
	public void findById_ReturnsExistingUser() throws Exception {
    	LOG.info("findById_ReturnsExistingUser method was invoked");
    	when(userService.getById(1)).thenReturn(data.get(0));
    	when(userService.containsId(1)).thenReturn(true);
    	
    	MvcResult result = mockMvc.perform(get("/api/user/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE ))
                .andReturn();

    	String content = result.getResponse().getContentAsString();
    	mapper.writeValue(writer, new ArrayList<>(Arrays.asList(data.get(0))));
    	verify(userService).getById(1);
    	verify(userService).containsId(1);
    	assertEquals(writer.toString(), content);
    }
	
    @Test
	public void findUserById_ThrowsResponseStatusException() throws Exception {
    	LOG.info("findUserById_ReturnsExistingUser method was invoked");
    	when(userService.getById(5)).thenReturn(data.get(0));
    	when(userService.containsId(5)).thenReturn(false);
    	
    	mockMvc.perform(get("/api/user/{id}", 5))
                .andExpect(status().isNotFound());

    	verify(userService).containsId(5);
    	verifyNoMoreInteractions(userService);
    }
	
}
