package cc.nuvu.pruebas.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @AllArgsConstructor
    @Getter
    private class ArgumentSummary {
        private final String field;
        private final String message;
    }

    @Override
    protected ResponseEntity handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        System.out.println("handleMethodArgumentNotValid");
        BindingResult result = ex.getBindingResult();
        final List<ArgumentSummary> details = result.getFieldErrors().stream()
            .map((fieldError -> new ArgumentSummary(fieldError.getField(), fieldError.getDefaultMessage())))
            .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(details);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public final ResponseEntity<?> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        System.out.println("handleConstraintViolation");
        final List<String> details = ex.getConstraintViolations()
            .parallelStream()
            .map(ConstraintViolation::getMessage)
            .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(details);
    }

}
