package ch.zhaw.pm4.loganalyser.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * This class is used by the {@link ApiExceptionHandler} to create custom error responses.
 */
@NoArgsConstructor
@RequiredArgsConstructor
@Getter @Setter
public class ErrorResponse {

    @NonNull private String message;
    @NonNull private List<String> details;

}
