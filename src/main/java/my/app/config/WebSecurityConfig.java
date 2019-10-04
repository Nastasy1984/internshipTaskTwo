package my.app.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import my.app.security.CustomAccessDeniedHandler;
import my.app.security.CustomAuthenticationFailureHandler;
import my.app.security.CustomLogoutSuccessHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	
	private static final Logger LOG = LoggerFactory.getLogger(my.app.config.WebSecurityConfig.class.getName());
	
	public WebSecurityConfig() {
		super();
	}

	//creating new user (here - in memory!) with his login and password
	//Authentication Manager. The Authentication Provider is backed by a simple, 
	//in-memory implementation – InMemoryUserDetailsManager specifically. 
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		LOG.info("configureGlobal method was invoked");
        auth
                .inMemoryAuthentication()
                .withUser("admin").password(passwordEncoder().encode("admin")).roles("ADMIN");
    }
	
	 @Override
	    protected void configure(final HttpSecurity http) throws Exception {
	        http
	          .csrf().disable()     
	          .authorizeRequests()
	          .antMatchers("/", "/**", "/api/**")
	          .hasRole("ADMIN")
	          .antMatchers("/login*").permitAll()
	          .anyRequest().authenticated()
	          .and()
	          .formLogin()

	          //.loginPage("/login")
	          //.loginProcessingUrl("/perform_login")
	          .defaultSuccessUrl("/", true)
	          //.failureUrl("/login.html?error=true")
	          //.failureHandler(authenticationFailureHandler())
	          .and()
	          .logout()
	          .logoutUrl("/perform_logout")
	          .deleteCookies("JSESSIONID")
	          .logoutSuccessHandler(logoutSuccessHandler());
	    }
	     
	
	
	
	
	    @Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }
	    
	    
	    @Bean
	    public LogoutSuccessHandler logoutSuccessHandler() {
	        return new CustomLogoutSuccessHandler();
	    }

	    @Bean
	    public AccessDeniedHandler accessDeniedHandler() {
	        return new CustomAccessDeniedHandler();
	    }

	    @Bean
	    public AuthenticationFailureHandler authenticationFailureHandler() {
	        return new CustomAuthenticationFailureHandler();
	    }
	    
}
