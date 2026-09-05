package ir.rahgozin.wallet.application.common.exception;

public enum BusinessError {

    KEEF_DEFAULT_ERROR(1, "KEEF_DEFAULT_ERROR"),
    CUSTOMER_NOT_FOUND(2, "CUSTOMER_NOT_FOUND"),
    CUSTOMER_IS_EXIST(3, "CUSTOMER_IS_EXIST"),

    ;

    private final Integer code;
    private final String message;

    BusinessError(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
