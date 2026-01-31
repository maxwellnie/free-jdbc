package io.github.maxwellnie.free.jdbc.statement;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Statement executor functional interface
 * Defines how to defaultExecute Statement and return results
 *
 * @param <T> Statement type
 * @param <R> Execution result type
 */
@FunctionalInterface
public interface Executor<T extends Statement, R> {
    /**
     * Statement batch execution executor
     * Used for batch processing of multiple SQL statements, returning int array result
     */
    Executor<Statement, Boolean> STATEMENT_EXECUTOR = (statement, sql) -> statement.execute(sql);

    Executor<Statement, int[]> STATEMENT_BATCH_EXECUTOR = (statement, sqls) -> {
        IntegratedStatement.addBatchSql(statement, sqls);
        return statement.executeBatch();
    };

    /**
     * Statement large capacity batch execution executor
     * Used for large capacity batch processing, returning long array result
     */
    Executor<Statement, long[]> STATEMENT_BATCH_LARGE_BATCH_EXECUTOR = (statement, sqls) -> {
        IntegratedStatement.addBatchSql(statement, sqls);
        return statement.executeLargeBatch();
    };

    /**
     * Prepared statement batch execution executor
     * Used for batch processing of prepared statements, returning int array result
     */
    Executor<PreparedStatement, int[]> PREPARED_STATEMENT_BATCH_EXECUTOR = (statement, sql) -> statement.executeBatch();
    /**
     * Prepared statement large capacity batch execution executor
     * Used for large capacity batch processing of prepared statements, returning long array result
     */
    Executor<PreparedStatement, long[]> PREPARED_STATEMENT_BATCH_LARGE_EXECUTOR = (statement, sql) -> statement.executeLargeBatch();

    Executor<PreparedStatement, Boolean> PREPARED_STATEMENT_EXECUTE_EXECUTOR = (statement, sql) -> statement.execute();

    Executor<PreparedStatement, Integer> PREPARED_STATEMENT_UPDATE_EXECUTOR = (statement, sql) -> statement.executeUpdate();

    /**
     * Executes Statement and returns result
     *
     * @param statement Statement object to defaultExecute
     * @param sql       SQL statement to defaultExecute
     * @return Execution result
     * @throws SQLException If a SQL exception occurs during execution
     */
    R execute(T statement, String sql) throws SQLException;
}
