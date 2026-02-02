package io.github.maxwellnie.free.jdbc.statement;


import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Maxwell Nie
 */
public abstract class BaseIntegratedStatement<T extends Statement, I extends IntegratedStatement<T, I>> implements IntegratedStatement<T, I> {
    protected Connection connection;
    protected Configuration configuration;
    protected T statement;
    protected String sql;
    protected I integratedStatement;

    public BaseIntegratedStatement(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Connection getConnection() {
        return this.connection;
    }

    @Override
    public I handleStatement(StatementHandler<T> handler) throws StatementException {
        validateStatement();
        if (handler != null) {
            try {
                handler.handle(statement);
            } catch (SQLException e) {
                throw new StatementException("Statement handle failed for SQL: " + sql + ", Error: " + e.getMessage() + ", Expected: valid statement handle operation", e);
            }
        }
        return integratedStatement;
    }

    @Override
    public I setConfiguration(Configuration configuration) {
        this.configuration = configuration;
        return integratedStatement;
    }

    protected void validateStatement() throws StatementException {
        if (statement == null)
            throw new StatementException("Statement is null, Expected: initialized statement object, Actual: null statement");
    }

    protected void validateSql() throws StatementException {
        if (sql == null)
            throw new StatementException("SQL is null, Expected: non-null SQL string, Actual: null SQL");
        if (sql.isEmpty())
            throw new StatementException("SQL is empty, Expected: non-empty SQL string, Actual: empty SQL string");
    }

    protected void validateConnection() throws StatementException {
        if (connection == null)
            throw new StatementException("Connection is null, Expected: active database connection, Actual: null connection");
    }

    @SuppressWarnings("unchecked")
    @Override
    public <P, R> R execute(Executor<? super T, P> executor, ResultParser<T, P, R> resultParser) throws SqlExecutionException, ResultParserException {
        validateStatement();
        if (executor == null) {
            if (resultParser == null)
                throw new SqlExecutionException("Executor and Result parser is null, Expected: at least one of executor or result parser to be non-null, Actual: both are null");
            if (defaultExecute())
                return resultParser.parse(statement, null);
            else
                throw new ResultParserException("Cannot parse result for update operation, Expected: query operation that returns result set, Actual: update operation with no result set");
        } else {
            try {
                if (resultParser == null)
                    return (R) executor.execute(statement, sql);
                return resultParser.parse(statement, executor.execute(statement, sql));
            } catch (SQLException e) {
                throw new SqlExecutionException("Statement execution failed for SQL: " + sql + ", Error: " + e.getMessage(), e);
            }
        }
    }

    /**
     * @return true if the first result is a ResultSet object; false if the first result is an update count or there is no result
     * @throws SqlExecutionException
     * @see Statement#execute(String)
     * @see java.sql.PreparedStatement#execute()
     */
    protected abstract boolean defaultExecute() throws SqlExecutionException;

    @Override
    public <P> I parameterize(ParametersHandler<? super T, P> parametersHandler, P parameters) throws StatementException {
        validateStatement();
        if (parametersHandler != null) {
            try {
                parametersHandler.parameterize(statement, parameters);
            } catch (SQLException e) {
                throw new StatementException("Statement parameterization failed for SQL: " + sql + ", Error: " + e.getMessage() + ", Expected: valid parameter binding", e);
            }
        }
        return integratedStatement;
    }

    protected void applyConfiguration() throws SQLException {
        if (configuration != null) {
            validateStatement();
            if (configuration.getQueryTimeout() > 0)
                statement.setQueryTimeout(configuration.getQueryTimeout());
            if (configuration.getFetchSize() > 0)
                statement.setFetchSize(configuration.getFetchSize());
        }
    }

    @Override
    public T getStatement() {
        return statement;
    }

    @Override
    public String getSql() {
        return sql;
    }

    @Override
    public boolean isClosed() throws StatementException {
        if (statement == null)
            return true;
        try {
            return statement.isClosed();
        } catch (SQLException e) {
            throw new StatementException("Error checking statement closure status: " + e.getMessage() + ", Expected: able to check closure status, Actual: exception occurred during check", e);
        }
    }

    @Override
    public void close() throws StatementException {
        if (statement != null) {
            try {
                statement.close();
                statement = null;
            } catch (SQLException e) {
                throw new StatementException("Error closing statement: " + e.getMessage() + ", Expected: successful closure, Actual: exception during closure", e);
            }
        }
    }
}