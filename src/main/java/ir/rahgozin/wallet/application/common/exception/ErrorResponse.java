package ir.rahgozin.wallet.application.common.exception;

public record ErrorResponse(
        Integer code,
        String message
) {
}