package my.app.controller.exception;

//is not used since I replace all ResourseNotFoundException with ResponseStatusException in User Controller
//@ResponseStatus(HttpStatus.NOT_FOUND)
@SuppressWarnings("serial")
public class ResourceNotFoundException extends RuntimeException{

	public ResourceNotFoundException() {
	}

	public ResourceNotFoundException(String message) {
		super(message);
	}

}
