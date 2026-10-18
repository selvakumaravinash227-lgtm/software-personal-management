package spms.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import spms.dao.DataAccessException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestControllerAdvice
public class ApiExceptionHandler {
    private static final Logger LOG=Logger.getLogger(ApiExceptionHandler.class.getName());
    @ExceptionHandler(DataAccessException.class) @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public Map<String,String> database(DataAccessException e){LOG.log(Level.SEVERE,"SPMS request could not access the database",e);return Map.of("message",e.getMessage());}
    @ExceptionHandler(SecurityException.class) @ResponseStatus(HttpStatus.FORBIDDEN)
    public Map<String,String> forbidden(SecurityException e){return Map.of("message",e.getMessage());}
    @ExceptionHandler(IllegalArgumentException.class) @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String,String> invalid(IllegalArgumentException e){return Map.of("message",e.getMessage());}
}
