package ir.rahgozin.wallet.application.common.exception;

public class BusinessException extends RuntimeException {

    private final Integer code;

    public BusinessException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public BusinessException(BusinessError businessError) {
        super(businessError.getMessage());
        this.code = businessError.getCode();
    }

    public Integer getCode() {
        return code;
    }
}
