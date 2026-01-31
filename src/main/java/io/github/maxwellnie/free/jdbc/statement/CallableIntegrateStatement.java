package io.github.maxwellnie.free.jdbc.statement;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Maxwell Nie
 */
public class CallableIntegrateStatement extends BaseIntegratedStatement<CallableStatement, CallableIntegrateStatement> {

    public CallableIntegrateStatement(Connection connection) {
        super(connection);
        super.integratedStatement = this;
    }

    @Override
    public CallableIntegrateStatement createStatement(String sql) throws StatementException {
        this.sql = sql;
        validateConnection();
        validateSql();
        try {
            if (configuration != null)
                statement = StatementUtils.callableStatement(connection, sql, configuration.getResultSetType(),
                        configuration.getResultSetConcurrency(), configuration.getHoldability());
            else
                statement = connection.prepareCall(sql);
            applyConfiguration();
        } catch (SQLException e) {
            throw new StatementException("Callable statement creation failed for SQL: " + sql + ", Error: " + e.getMessage() +
                    ", Expected: successful callable statement creation, Actual: SQLException during creation", e);
        }
        return integratedStatement;
    }

    @Override
    protected boolean defaultExecute() throws SqlExecutionException {
        try {
            return statement.execute();
        } catch (SQLException e) {
            throw new SqlExecutionException("Callable statement execution failed for SQL: " + sql + ", Error: " + e.getMessage() +
                    ", Expected: successful execution, Actual: SQLException during execution", e);
        }
    }
}