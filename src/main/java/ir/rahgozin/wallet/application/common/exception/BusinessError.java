package ir.rahgozin.wallet.application.common.exception;

public enum BusinessError {

    KEEF_DEFAULT_ERROR(1, "KEEF_DEFAULT_ERROR"),
    CUSTOMER_NOT_FOUND(2, "CUSTOMER_NOT_FOUND"),
    CUSTOMER_IS_EXIST(3, "CUSTOMER_IS_EXIST"),
    ACCOUNT_NOT_FOUND(4, "ACCOUNT_NOT_FOUND"),
    INSUFFICIENT_BALANCE(5, "INSUFFICIENT_BALANCE"),
    ACCOUNT_DEBIT_IN_PROGRESS(6, "ACCOUNT_DEBIT_IN_PROGRESS"),
    INVALID_ACCOUNT_TYPE_FOR_WITHDRAW(7, "INVALID_ACCOUNT_TYPE_FOR_WITHDRAW"),
    INVALID_CUSTOMER_STATUS(8, "INVALID_CUSTOMER_STATUS"),
    TOO_MANY_REQUEST(9, "TOO_MANY_REQUEST"),

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
