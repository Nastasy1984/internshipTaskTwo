package my.app.controller.exception;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class ErrorHandler {
    private static final Logger LOG = LoggerFactory.getLogger(my.app.controller.exception.ErrorHandler.class.getName());
	/*@ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
    public ResourceNotFoundException handleCustomException(ResourceNotFoundException e) {
        return e;
    }*/
	
	@ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleIOException(ResourceNotFoundException e) {
		LOG.info("handleIOException method was invoked");
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

	//This methods works if user sent not valid data to API
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationExceptions(
	  MethodArgumentNotValidException ex) {
		LOG.info("handleValidationExceptions method was invoked");
	    Map<String, String> errors = new HashMap<>();
	    ex.getBindingResult().getAllErrors().forEach((error) -> {
	        String fieldName = ((FieldError) error).getField();
	        String errorMessage = error.getDefaultMessage();
	        errors.put(fieldName, errorMessage);
	    });
	    LOG.debug("handleValidationExceptions created map of errors: {}", errors.toString());
	    return errors;
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
