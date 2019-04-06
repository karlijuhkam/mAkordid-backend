package ee.heikokarli.makordid.controller;

import ee.heikokarli.makordid.data.dto.response.error.ErrorResponse;
import ee.heikokarli.makordid.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.file.AccessDeniedException;

public abstract class AbstractApiController {

    @Autowired
    public AbstractApiController() {
    }
    @ExceptionHandler(ApiException.class)
    @ResponseBody
    public ErrorResponse handleApiError(HttpServletRequest request, HttpServletResponse response, ApiException e) {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(e.getStatusCode().value());
        // exceptionInfoLogger.printServerError(request, e, e.getErrorCode() + " - " + e.getDescription());
        return new ErrorResponse(e.getErrorCode(), e.getDescription());
    }

    @ExceptionHandler(HttpClientErrorException.class)
    @ResponseBody
    public ErrorResponse handleHttpClientError(HttpServletRequest request, HttpServletResponse response, HttpClientErrorException e) {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(e.getStatusCode().value());
        // exceptionInfoLogger.printServerError(request, e, e.getResponseBodyAsString());
        return new ErrorResponse(e.getStatusCode().toString(), e.getMessage());
    }
    @ExceptionHandler(NullPointerException.class)
    @ResponseBody
    public ErrorResponse handleNullPointerException(HttpServletRequest request, HttpServletResponse response, NullPointerException e) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(500);
        String trace = "";
        for(int i = 0; i < e.getStackTrace().length; i++) {
            trace += e.getStackTrace()[i].toString() + "\n";
        }
        // exceptionInfoLogger.printServerError(request, e, trace);

        return new ErrorResponse("500", trace);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public ErrorResponse handleAccessDeniedException(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        return new ErrorResponse("403", "Access denied");
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseBody
    public ErrorResponse handleNotFoundException(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        return new ErrorResponse(HttpStatus.NOT_FOUND.toString(), "Entity not found");
    }
}