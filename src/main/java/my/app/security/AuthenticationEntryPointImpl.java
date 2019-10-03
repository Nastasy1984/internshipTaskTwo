package my.app.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;


@Component
public class AuthenticationEntryPointImpl extends BasicAuthenticationEntryPoint{
    private static final Logger LOG = LoggerFactory.getLogger(my.app.controller.UserController.class.getName());
	
	// RealmName appears in the login window 
    @Override
    //public void afterPropertiesSet() throws Exception{
    public void afterPropertiesSet(){
		LOG.info("afterPropertiesSet method was invoked");
        setRealmName("SpringRest");
        super.afterPropertiesSet();
    }
    
    
	//Commences an authentication scheme
	@Override
    public void commence (HttpServletRequest request, HttpServletResponse response, AuthenticationException authEx)  {
		LOG.info("commence method was invoked");
        response.addHeader("WWW-Authenticate", "Basic realm=" + getRealmName());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        PrintWriter writer;
		try {
			writer = response.getWriter();
			writer.println("HTTP Status 401 - " + authEx.getMessage());
		} 
		catch (IOException e) {
			LOG.warn("Commence method caught: {}", e.getClass().getName());
		} 
    }
 
}
