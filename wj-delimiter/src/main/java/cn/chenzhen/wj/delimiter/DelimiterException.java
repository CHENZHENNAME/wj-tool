package cn.chenzhen.wj.delimiter;

public class DelimiterException extends RuntimeException{
    public DelimiterException(String message) {
        super(message);
    }

    public DelimiterException(String message, Throwable cause) {
        super(message, cause);
    }

    public DelimiterException(Throwable cause) {
        super(cause);
    }

    public DelimiterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
