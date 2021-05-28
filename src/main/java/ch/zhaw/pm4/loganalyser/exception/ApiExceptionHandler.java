package ch.zhaw.pm4.loganalyser.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class catches and handles all exceptions that occur inside the controllers.
 */
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    public static final String INTERNAL_SERVER_ERROR_MESSAGE = "Server error";
    public static final String RECORD_NOT_FOUND_MESSAGE = "Record not found";
    public static final String RECORD_ALREADY_EXISTS_MESSAGE = "Record already exists";
    public static final String METHOD_ARGUMENT_NOT_VALID_MESSAGE = "Validation failed";
    public static final String PATH_NOT_FOUND_MESSAGE = "The path has not been found";
    public static final String INVALID_COLUMN_TYPE_MESSAGE = "The defined column type is invalid";
    public static final String INVALID_INPUT_MESSAGE = "The provided input is invalid";
    public static final String EXACT_CRITERIA_IS_NULL_MESSAGE = "The Exact searched String is Null";

    /**
     * Catches all {@link Exception} and returns it with the information what went wrong.
     * @param ex {@link Exception}
     * @param request {@link WebRequest}
     * @return ResponseEntity<Object>
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        return handleCustomException(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR_MESSAGE, ex);
    }

    /**
     * Catches a {@link RecordNotFoundException} and returns it with
     * additional details which record could not be found.
     * @param ex {@link RecordNotFoundException}
     * @param request {@link WebRequest}
     * @return ResponseEntity<Object>
     */
    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<Object> handleRecordNotFoundException(RecordNotFoundException ex, WebRequest request) {
        return handleCustomException(HttpStatus.NOT_FOUND, RECORD_NOT_FOUND_MESSAGE, ex);
    }

    /**
     * Catches a {@link RecordAlreadyExistsException} and returns it with
     * additional details which record already exists.
     * @param ex {@link RecordAlreadyExistsException}
     * @param request {@link WebRequest}
     * @return ResponseEntity<Object>
     */
    @ExceptionHandler(RecordAlreadyExistsException.class)
    public ResponseEntity<Object> handleRecordAlreadyExistsException(RecordAlreadyExistsException ex, WebRequest request) {
        return handleCustomException(HttpStatus.CONFLICT, RECORD_ALREADY_EXISTS_MESSAGE, ex);
    }

    /**
     * Catches a {@link PathNotFoundException} and returns it with
     * additional details which path wasn't found.
     * @param ex {@link PathNotFoundException}
     * @param request {@link WebRequest}
     * @return ResponseEntity<Object>
     */
    @ExceptionHandler(PathNotFoundException.class)
    public ResponseEntity<Object> handlePathNotFoundException(PathNotFoundException ex, WebRequest request) {
        return handleCustomException(HttpStatus.NOT_FOUND, PATH_NOT_FOUND_MESSAGE, ex);
    }

    /**
     * Catches a {@link ExactCriteriaIsNullException} and returns it
     * @param ex {@link ExactCriteriaIsNullException}
     * @param request {@link WebRequest}
     * @return ResponseEntity<Object>
     */
    @ExceptionHandler(ExactCriteriaIsNullException.class)
    public ResponseEntity<Object> handleExactCriteriaIsNullException(ExactCriteriaIsNullException ex, WebRequest request) {
        return handleCustomException(HttpStatus.BAD_REQUEST, EXACT_CRITERIA_IS_NULL_MESSAGE, ex);
    }

    /**
     * Catches a {@link InvalidColumnTypeException} and returns it with
     * additional details what was invalid.
     * @param ex {@link InvalidColumnTypeException}
     * @param request {@link WebRequest}
     * @return ResponseEntity<Object>
     */
    @ExceptionHandler(InvalidColumnTypeException.class)
    public ResponseEntity<Object> handleInvalidColumnTypeException(InvalidColumnTypeException ex, WebRequest request) {
        return handleCustomException(HttpStatus.BAD_REQUEST, INVALID_COLUMN_TYPE_MESSAGE, ex);
    }

    /**
     * Catches a {@link InvalidInputException} and returns it with
     * additional details what was invalid.
     * @param ex {@link InvalidInputException}
     * @param request {@link WebRequest}
     * @return {@link ResponseEntity<Object>}
     */
    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<Object> handleInvalidInputException(InvalidInputException ex, WebRequest request) {
        return handleCustomException(HttpStatus.BAD_REQUEST, INVALID_INPUT_MESSAGE, ex);
    }

    private ResponseEntity<Object> handleCustomException(HttpStatus httpStatus, String errorMessage, Exception ex) {
        String stacktrace = Arrays.stream(ex.getStackTrace())
                .map(StackTraceElement::toString)
                .collect(Collectors.joining("\n"));
        
        List<String> details = List.of(ex.getLocalizedMessage(), stacktrace);
        var error = new ErrorResponse(errorMessage, details);
        return new ResponseEntity<>(error, httpStatus);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        List<String> details = ex.getBindingResult().getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.toList());
        var error = new ErrorResponse(METHOD_ARGUMENT_NOT_VALID_MESSAGE, details);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        return handleAllExceptions(ex, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        return handleAllExceptions(ex, request);
    }

}
