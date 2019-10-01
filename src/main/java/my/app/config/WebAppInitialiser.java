package my.app.config;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
//this class allow us to register our configuration in Spring context
public class WebAppInitialiser extends AbstractAnnotationConfigDispatcherServletInitializer {
	 // Here we should add config where we initialize beans
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{RootConfig.class};
    }
 // Here we should add config where we initialize ViewResolver
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{WebConfig.class};
    }
 
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
    
    //for error handling with errorHandler class
    @Override
    protected DispatcherServlet createDispatcherServlet(WebApplicationContext servletAppContext) {
        final DispatcherServlet dispatcherServlet = (DispatcherServlet) super.createDispatcherServlet(servletAppContext);
        dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
        return dispatcherServlet;
    }
    
}
