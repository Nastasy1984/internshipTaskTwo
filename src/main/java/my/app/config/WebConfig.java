package my.app.config;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import com.fasterxml.jackson.databind.ObjectMapper;


//config class
@Configuration
//import Spring MVC WebMvcConfigurationSupport
//in the case of REST, it detects the existence of Jackson and JAXB 2 on the classpath 
//and automatically creates and registers default JSON and XML converters. 
@EnableWebMvc
//where we can find our components
@ComponentScan(basePackages = "my.app")
public class WebConfig implements WebMvcConfigurer{
	private static final Logger LOG = LoggerFactory.getLogger(my.app.config.WebConfig.class.getName());
 //show, where we can find our jsp pages
    @Bean
    ViewResolver viewResolver(){
    	LOG.info("viewResolver method was invoked");
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/");
        resolver.setSuffix(".jsp");
        return resolver;
    }
    
    
   /* @Override
    public void addViewControllers(ViewControllerRegistry registry) {
       //registry.addViewController("/").setViewName("index");
       registry.addViewController("/login").setViewName("login.jsp");;
    }*/
    
    
    @Bean
    public ObjectMapper objectMapper() {
    	LOG.info("objectMapper method was invoked");
    	/*ObjectMapper objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new Serializer());
        javaTimeModule.addDeserializer(LocalDateTime.class, new Deserializer());
        objectMapper.registerModule(javaTimeModule);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);*/
    	return new ObjectMapper();
    }
    
    @Bean
    public CloseableHttpClient closeableHttpClient() {
    	LOG.info("closeableHttpClient method was invoked");
        return HttpClients.createDefault();
    }
    
    //This method worked correctly, but I replaced it with the data source that provides pooling
    /*
    @Bean
    public DataSource postgreSQLDataSource() {
    	LOG.info("postgreSQLDataSource method was invoked");
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/users");
        dataSource.setUsername("postgres");
        dataSource.setPassword("Novgorod14");
        return dataSource;
    }*/
    
    
    @Bean
    public DataSource postgreSQLDataSource() {
    	LOG.info("postgreSQLDataSource method was invoked");
    	BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setUrl("jdbc:postgresql://localhost:5432/users");
        ds.setUsername("postgres");
        ds.setPassword("Novgorod14");
        ds.setMinIdle(5);
        ds.setMaxIdle(10);
        return ds;
    }
    
    
    //for RequestParam validation
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
    	LOG.info("methodValidationPostProcessor method was invoked");
        return new MethodValidationPostProcessor();
    }
    
    /*
    @Bean
    public SimpleMappingExceptionResolver simpleMappingExceptionResolver(){
    SimpleMappingExceptionResolver resolver = new SimpleMappingExceptionResolver();
    //page and statusCode pare 
    //you also can use method setStatusCodes(properties)
    resolver.addStatusCode("404", 404); 

    //set views for exception 
    Properties mapping = new Properties();
    mapping.put("my.app.controller.exception.ResourceNotFoundException" , "page");    
    resolver.setExceptionsMapping(mapping); 

    return resolver;
    }*/
    
    
}