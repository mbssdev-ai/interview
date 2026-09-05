package ir.rahgozin.wallet.infrastructure.api.advice;

import ir.rahgozin.wallet.application.common.exception.BusinessError;
import ir.rahgozin.wallet.application.common.exception.BusinessException;
import ir.rahgozin.wallet.application.common.exception.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Locale;

@ControllerAdvice
@RequiredArgsConstructor
@Log4j2
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handle(Exception ex) {
        log.error("exception occur: ", ex);
        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE)
                .body(
                        new ErrorResponse(
                                BusinessError.KEEF_DEFAULT_ERROR.getCode(),
                                getMessage(BusinessError.KEEF_DEFAULT_ERROR.getMessage())
                        )
                );
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<?> handle(BusinessException ex) {
        log.error("exception occur: ", ex);
        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE)
                .body(
                        new ErrorResponse(
                                ex.getCode(),
                                getMessage(ex.getMessage())
                        )
                );
    }

    private String getMessage(String key, String... args) {
        return messageSource.getMessage(key, args, Locale.of("fa"));
    }
}
