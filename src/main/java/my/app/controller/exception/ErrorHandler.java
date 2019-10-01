package my.app.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class ErrorHandler {
	/*@ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
    public ResourceNotFoundException handleCustomException(ResourceNotFoundException e) {
        return e;
    }*/
	
	@ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleIOException(ResourceNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

	/*
	@ExceptionHandler(NoHandlerFoundException.class)
	@ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handle(Exception e) {
    	return "Page not found";
   }*/
	
	@ExceptionHandler(NoHandlerFoundException.class)
	@ResponseBody
    public ModelAndView handle() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("404");
		return modelAndView;
	}
		
    /*
    @ExceptionHandler(NoHandlerFoundException.class)
    public String handle(Exception e) {
    	return "404";
   }*/
}
