package io.github.maxwellnie.free.jdbc.statement;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * Statement handler functional interface
 * Used for custom operations on Statement objects
 *
 * @param <T> Statement type
 */
@FunctionalInterface
public
interface StatementHandler<T extends Statement> {

    /**
     * Handles the Statement object
     *
     * @param statement Statement object to handle
     * @throws SQLException If a SQL exception occurs during processing
     */
    void handle(T statement) throws SQLException;
}
