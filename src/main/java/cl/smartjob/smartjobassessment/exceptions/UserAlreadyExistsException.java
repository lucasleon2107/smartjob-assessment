package cl.smartjob.smartjobassessment.exceptions;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends BaseApiException {
    public UserAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
