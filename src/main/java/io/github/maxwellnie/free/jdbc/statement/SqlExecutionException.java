package io.github.maxwellnie.free.jdbc.statement;

/**
 * @author Maxwell Nie
 */
public class SqlExecutionException extends StatementException {
    public SqlExecutionException() {
    }

    public SqlExecutionException(String message) {
        super(message);
    }

    public SqlExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public SqlExecutionException(Throwable cause) {
        super(cause);
    }

    public SqlExecutionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
