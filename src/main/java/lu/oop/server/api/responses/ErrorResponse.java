package lu.oop.server.api.responses;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * DTO to return an error response for the oop server api
 */
public class ErrorResponse {
    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

    private int status;

    private String error;
    private String message;
    private String path;

    public ErrorResponse(int status, String error, String message, String path) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    /**
     * From the instance serialize to json, used to write response
     * @return the json of the response
     * @throws JsonProcessingException could not create json
     */
    public String toJson() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }

    /**
     * Given the response instance write the error to HTTP body as json
     * @param response interface to which write http response
     * @throws IOException could not write
     */
    public void writeToResponse(HttpServletResponse response) throws IOException {
        response.setStatus(getStatus());
        response.setHeader("Content-Type", "application/json");
        response.getWriter().write(toJson());
    }

}
