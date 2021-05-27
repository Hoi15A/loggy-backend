package ch.zhaw.pm4.loganalyser.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * This class is used by the {@link ApiExceptionHandler} to create custom error responses.
 */
@RequiredArgsConstructor
@Getter
public class ErrorResponse {

    private final String message;
    private final List<String> details;

}
