package ch.zhaw.pm4.loganalyser.exception;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * This class is used by the {@link ApiExceptionHandler} to create
 * custom error responses.
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class ErrorResponse {
    @NonNull private String message;
    @NonNull private List<String> details;
}
