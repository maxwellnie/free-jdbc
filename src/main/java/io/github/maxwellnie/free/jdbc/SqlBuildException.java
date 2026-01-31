package io.github.maxwellnie.free.jdbc;


/**
 * @author Maxwell Nie
 */
public class SqlBuildException extends RuntimeException {
    public SqlBuildException() {
    }

    public SqlBuildException(String message) {
        super(message);
    }

    public SqlBuildException(String message, Throwable cause) {
        super(message, cause);
    }

    public SqlBuildException(Throwable cause) {
        super(cause);
    }

    public SqlBuildException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
