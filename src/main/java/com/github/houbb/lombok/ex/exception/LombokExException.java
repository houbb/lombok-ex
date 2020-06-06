package com.github.houbb.lombok.ex.exception;

/**
 * <p> project: lombok-ex-LombokExException </p>
 * <p> create on 2020/6/6 20:08 </p>
 *
 * @author binbin.hou
 * @since 0.0.8
 */
public class LombokExException extends RuntimeException {

    public LombokExException() {
    }

    public LombokExException(String message) {
        super(message);
    }

    public LombokExException(String message, Throwable cause) {
        super(message, cause);
    }

    public LombokExException(Throwable cause) {
        super(cause);
    }

    public LombokExException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
