package cn.chenzhen.wj.fix;

public class FixException extends RuntimeException{
    public FixException(String message) {
        super(message);
    }

    public FixException(String message, Throwable cause) {
        super(message, cause);
    }

    public FixException(Throwable cause) {
        super(cause);
    }

    public FixException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
