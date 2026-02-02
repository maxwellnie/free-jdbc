package io.github.maxwellnie.free.jdbc.statement;

import io.github.maxwellnie.free.jdbc.callable.CallableSql;

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

    /**
     * 执行CallableSql
     * @param callableSql CallableSql实例
     * @param resultParser 结果解析器
     * @param <R> 返回类型
     * @return 执行结果
     * @throws SqlExecutionException SQL执行异常
     * @throws ResultParserException 结果解析异常
     */
    public <R> R execute(CallableSql callableSql, ResultParser<CallableStatement, Boolean, R> resultParser) throws SqlExecutionException, ResultParserException {
        if (callableSql == null)
            throw new SqlExecutionException("CallableSql cannot be null");
        if (statement == null)
            createStatement(callableSql.getSqlString());
        parameterize(CallableSql.CALLABLE_SQL_PARAMETERS_HANDLER, callableSql);
        return execute(Executor.PREPARED_STATEMENT_EXECUTE_EXECUTOR, resultParser);
    }

}