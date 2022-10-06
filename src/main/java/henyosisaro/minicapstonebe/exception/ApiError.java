package henyosisaro.minicapstonebe.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
public class ApiError {

    private String message;
    private HttpStatus status;
    private ZonedDateTime timestamp;
}

