package my.app;

import java.io.StringWriter;

import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;

import com.fasterxml.jackson.databind.ObjectMapper;

import my.app.service.UserService;

////DONE by https://spring.io/blog/2011/06/21/spring-3-1-m2-testing-with-configuration-classes-and-profiles/
//The @Profile annotation tells Spring to apply this configuration only when the “test” profile is active. 
@Profile("test")
@Configuration
@PropertySource("classpath:application.properties")
public class UserControllerTestConfiguration {
	private static final Logger LOG = LoggerFactory.getLogger(my.app.UserControllerTestConfiguration.class.getName());
	@Autowired
	private Environment env;
	
	/*The method creates and returns a Mockito mock of the UserService class.*/
	@Bean
    //The @Primary annotation is there to make sure this instance is used instead of a real one for autowiring.
    @Primary
    public UserService userService() {
    	LOG.info("userService method was invoked");
        return Mockito.mock(UserService.class);
    }
	
	@Bean
    public ObjectMapper objectMapper() {
    	LOG.info("objectMapper method was invoked");
    	return new ObjectMapper();
    }
	
	@Bean
	@Scope("prototype")
    public StringWriter writer() {
    	LOG.info("stringWriter method was invoked");
    	return new StringWriter();
    }
}
