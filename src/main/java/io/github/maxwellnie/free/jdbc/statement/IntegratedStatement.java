package io.github.maxwellnie.free.jdbc.statement;


import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Integrated SQL statement execution interface, unifying the handling of various types of Statement operations
 * Provides functions for creating, configuring, parameterizing, and executing SQL statements
 * Supports batch execution and custom execution strategies
 *
 * @param <T> Specific type of Statement, must be a subclass of Statement
 * @param <I> Specific type of IntegratedStatement, must be a subclass of IntegratedStatement
 * @author Maxwell Nie
 */
public interface IntegratedStatement<T extends Statement, I extends IntegratedStatement<T, I>> extends AutoCloseable {

    /**
     * Adds multiple SQL statements to the batch queue
     * Splits the input SQL string by semicolons and adds each non-empty SQL statement to the batch
     *
     * @param statement   Statement object to add batch to
     * @param multipleSql String containing one or more SQL statements separated by semicolons
     * @throws SQLException If SQL statement is empty or database operation fails
     */
    static void addBatchSql(Statement statement, String multipleSql) throws SQLException {
        for (String singleSql : multipleSql.split(";")) {
            String trimmedSql = singleSql.trim();
            if (trimmedSql.isEmpty())
                continue;
            statement.addBatch(trimmedSql);
        }
    }

    /**
     * Gets the database connection associated with this Statement
     *
     * @return Database connection object
     */
    Connection getConnection();

    /**
     * Creates a specific Statement object based on the SQL string
     *
     * @param sql SQL statement to execute
     * @return Current integrated Statement instance, supporting method chaining
     * @throws StatementException If Statement creation fails, typically due to invalid SQL, null connection, or database connectivity issues
     */
    I createStatement(String sql) throws StatementException;

    /**
     * Sets configuration information for the Statement
     *
     * @param configuration Configuration object containing settings like query timeout, fetch size, etc.
     * @return Current integrated Statement instance, supporting method chaining
     */
    I setConfiguration(Configuration configuration);

    /**
     * Parameterizes the Statement object with the specified parameters
     *
     * @param parametersHandler Parameter handler for custom parameterization logic
     * @param parameters        Parameters to be set in the Statement
     * @return Current integrated Statement instance, supporting method chaining
     * @throws StatementException If an error occurs during parameterization, typically due to invalid parameter types or indices
     */
    <P> I parameterize(ParametersHandler<? super T, P> parametersHandler, P parameters) throws StatementException;

    default <R> R execute(Executor<? super T, R> executor) throws SqlExecutionException {
        return execute(executor, null);
    }

    /**
     * Executes SQL statement using the specified executor and result parser
     * <pre><code>
     *   if(executor != null && resultParser != null){
     *         // Return parsed result (It is parsing execution result.)
     *     }else if(executor == null && resultParser != null){
     *         // Return parsed result (It is not parsing execution result.)
     *     }else if(executor != null && resultParser == null){
     *         // Return execution result
     *     }else{
     *         // Default implementation throws an SqlExecutionException.
     *     }
     * </code></pre>
     *
     * @param executor     Executor defining the specific execution logic.
     * @param resultParser Result parser for parsing execution results.
     * @param <P>          Type of execution result
     * @param <R>          Type of parsed result
     * @return Execution result
     * @throws ResultParserException If an error occurs during result parsing, typically due to unexpected result types or structure
     * @throws SqlExecutionException If an error occurs during execution, typically due to SQL syntax errors or database constraints
     */
    <P, R> R execute(Executor<? super T, P> executor, ResultParser<T, P, R> resultParser) throws SqlExecutionException, ResultParserException;

    default <P, R> R execute(ResultParser<T, P, R> resultParser) throws SqlExecutionException, ResultParserException {
        return execute(null, resultParser);
    }

    /**
     * Processes the Statement object using the specified handler
     *
     * @param handler Statement handler for custom operations on the Statement
     * @return Current integrated Statement instance, supporting method chaining
     * @throws StatementException If an error occurs during processing, typically due to invalid statement operations
     */
    I handleStatement(StatementHandler<T> handler) throws StatementException;

    /**
     * Gets the internal Statement object
     *
     * @return Internal Statement instance
     */
    T getStatement();

    /**
     * Gets the currently executed SQL statement
     *
     * @return Current SQL statement string
     */
    String getSql();

    /**
     * Checks whether the Statement object is closed
     *
     * @return true if the Statement object is closed or null, false otherwise
     * @throws StatementException If an error occurs during checking, typically due to connection issues
     */
    boolean isClosed() throws StatementException;

    /**
     * Closes the Statement object
     *
     * @throws StatementException If an error occurs during closing, typically due to resource cleanup issues
     */
    void close() throws StatementException;
}