package cn.chenzhen.wj.reflect;

public class ClassException extends RuntimeException{
    public ClassException(String message) {
        super(message);
    }

    public ClassException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClassException(Throwable cause) {
        super(cause);
    }

    public ClassException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
