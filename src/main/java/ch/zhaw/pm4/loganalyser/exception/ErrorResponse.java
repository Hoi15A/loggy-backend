package ch.zhaw.pm4.loganalyser.exception;

import lombok.Data;
import lombok.NonNull;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * This class is used by the {@link ApiExceptionHandler} to create
 * custom error responses.
 */
@Data
@XmlRootElement(name = "error")
public class ErrorResponse {
    @NonNull private String message;
    @NonNull private List<String> details;
}
