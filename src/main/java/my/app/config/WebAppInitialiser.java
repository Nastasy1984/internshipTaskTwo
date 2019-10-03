package my.app.config;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
//this class allow us to register our configuration in Spring context
public class WebAppInitialiser extends AbstractAnnotationConfigDispatcherServletInitializer {
	 // Here we should add config where we initialize beans
	// root-config classes will be registered in the ContextLoaderListener context
    @Override
    protected Class<?>[] getRootConfigClasses() {
    // AppConfig defines beans that would be in root-context.xml
    // for service and infrastructure beans
        return new Class[]{RootConfig.class, WebSecurityConfig.class};
    }
 // Here we should add config where we initialize ViewResolver
 // WebConfig defines beans that would be in servlet.xml
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{WebConfig.class};
    }
 
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
    
    //for error handling with errorHandler class
    //in one app can be several dispatcher servlets
    @Override
    protected DispatcherServlet createDispatcherServlet(WebApplicationContext servletAppContext) {
        final DispatcherServlet dispatcherServlet = (DispatcherServlet) super.createDispatcherServlet(servletAppContext);
        dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
        return dispatcherServlet;
    }
    
}
