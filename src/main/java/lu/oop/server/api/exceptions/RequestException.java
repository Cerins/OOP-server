package lu.oop.server.api.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lu.oop.server.api.responses.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

/**
 * Exception class which standardizes the bad responses in the oop-server project
 */
public class RequestException extends Exception {

    Logger logger = LoggerFactory.getLogger(RequestException.class);
    private static final long serialVersionUID = -7271224527076331282L;

    private final HttpStatus code;

    private final String error;

    private final String logMessage;

    /**
     * Creates the exception
     * @param code HTTP response code
     * @param error error subclass
     * @param message the message for the api consumer
     * @param logMessage the message which will be logged
     */
    public RequestException(HttpStatus code, String error, String message, String logMessage) {
        super(message);
        this.code = code;
        this.logMessage = logMessage;
        this.error = error;
    }

    public RequestException(HttpStatus code, String message, String logMessage) {
        this(code, null, message, logMessage);
    }

    public HttpStatus getCode() {
        return code;
    }

    public String getLogMessage() {
        return logMessage;
    }

    public String getError() {
        return error;
    }

    /**
     * Logs the exception
     */
    public void log() {
        logger.error("{}:{}:{}", getCode().toString(), getMessage(), getLogMessage());
    }

    /**
     * From the exception generates response dto instance
     * @param request the incoming http request
     * @return standard error response dto
     */
    public ErrorResponse toResponse(HttpServletRequest request) {
        String error = getError();
        if(error == null) {
            error = getCode().getReasonPhrase();
        }
        return  new ErrorResponse(
                getCode().value(),
                error,
                getMessage(),
                request.getRequestURI()
        );
    }


}
