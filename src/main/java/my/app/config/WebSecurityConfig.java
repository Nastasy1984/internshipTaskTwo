package my.app.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;

@Configuration
//@EnableWebSecurity supports web security and provide integration with Spring MVC
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
    private static final Logger LOG = LoggerFactory.getLogger(my.app.controller.UserController.class.getName());
    /*@Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;*/
    
    //Service interface for encoding passwords. The preferred implementation is BCryptPasswordEncoder - 
    //- implementation that uses the BCrypt strong hashing function
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    //creating new user (here - in memory!) with his login and password
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) {
    	LOG.info("configureGlobal method was invoked");
        try {
			auth.inMemoryAuthentication()
			  .withUser("admin").password(passwordEncoder().encode("admin"))
			  .roles("ADMIN");
		} 
        catch (Exception e) {
			LOG.warn("configureGlobal method caught: {}", e.getClass().getName());
		}
    }
    
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	LOG.info("configure method was invoked");
        http
        	//.sessionManagement()
        	//.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        	//.and()
        	.authorizeRequests()
        	.antMatchers("/user/**", "/add", "/users")
        	.hasRole("ADMIN")
        	
        	.antMatchers("/", "/**").permitAll()
        	.anyRequest() 
        	.authenticated() 
        	.and() 
        	.formLogin()
        	.and() 
        	.httpBasic()
        	.and()
        	.csrf() 
        	.disable (); 
        	
        ;
    }
       
    /*
    //using the httpBasic() element to define Basic Authentication
    //determine which URL should be protected and which shouldn't
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
       // Allows configuring the HttpSecurity to only be invoked when matching the provided ant pattern.
       // the visitors can see all site at root path (/*) and to the list with all users without authentication
       // These URLs will not be protected
          .antMatchers("/", "/users").permitAll()
       // All requests send to the Web Server request must be authenticated
          .anyRequest().authenticated()
          .and()
          .httpBasic()
       // Use AuthenticationEntryPoint to authenticate user/password
          .authenticationEntryPoint(authenticationEntryPoint);
 
        //TODO delete comment I don't know yet what is it and is it necessary
       // http.addFilterAfter(new MyAppFilter(), BasicAuthenticationFilter.class);
    }
 
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
          .withUser("admin").password(passwordEncoder().encode("admin"))
          .authorities("ADMIN");
    }
*/
}
