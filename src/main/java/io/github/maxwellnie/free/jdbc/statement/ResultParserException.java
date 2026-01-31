package io.github.maxwellnie.free.jdbc.statement;

/**
 * @author Maxwell Nie
 */
public class ResultParserException extends StatementException {
    public ResultParserException() {
    }

    public ResultParserException(String message) {
        super(message);
    }

    public ResultParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResultParserException(Throwable cause) {
        super(cause);
    }

    public ResultParserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
