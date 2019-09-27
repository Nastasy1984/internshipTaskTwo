package my.app.config;

import javax.sql.DataSource;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.fasterxml.jackson.databind.ObjectMapper;


//config class
@Configuration
//import Spring MVC WebMvcConfigurationSupport
//in the case of REST, it detects the existence of Jackson and JAXB 2 on the classpath 
//and automatically creates and registers default JSON and XML converters. 
@EnableWebMvc
//where we can find our components
@ComponentScan(basePackages = "my.app")
public class WebConfig {
 //show, where we can find our jsp pages
    @Bean
    ViewResolver viewResolver(){
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/");
        resolver.setSuffix(".jsp");
        return resolver;
    }
    
    @Bean
    public ObjectMapper objectMapper() {
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
        return HttpClients.createDefault();
    }
    
    @Bean
    public DataSource postgreSQLDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/users");
        dataSource.setUsername("postgres");
        dataSource.setPassword("Novgorod14");
        return dataSource;
    }
    
    
    
    
}