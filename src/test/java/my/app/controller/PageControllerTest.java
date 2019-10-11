package my.app.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import my.app.model.User;
import my.app.service.PageService;


public class PageControllerTest {
    private static final Logger LOG = LoggerFactory.getLogger(my.app.controller.PageControllerTest.class.getName());
    
	@Mock
    private PageService pageService;
	@InjectMocks
    private PageController pageController;
	private MockMvc mockMvc;
	
	@Before
    public void setUp(){		
		LOG.info("setUp method was invoked");
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders
		        .standaloneSetup(pageController)
		        .build();
    }
	
    @Test
    public void findUserPage() throws Exception {
    	LOG.info("findUserPage method was invoked");

    	mockMvc.perform(get("/find-user"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("findUser"));
    	
    }

}
