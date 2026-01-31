package io.github.maxwellnie.free.jdbc.batch;

import io.github.maxwellnie.free.jdbc.AbstractSqlBuilder;
import io.github.maxwellnie.free.jdbc.SqlBuildException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Maxwell Nie
 */
public class BatchSqlBuilder extends AbstractSqlBuilder<BatchSql, List<Object>> {
    protected final int batchParametersSize;

    /**
     * @param batchSize batch size
     */
    public BatchSqlBuilder(int batchSize) {
        super(batchSize);
        this.batchParametersSize = 0;
    }

    public BatchSqlBuilder() {
        this(0);
    }

    public BatchSqlBuilder(int batchSize, int batchParametersSize) {
        super(batchSize);
        this.batchParametersSize = batchParametersSize;
    }

    @Override
    public BatchSqlBuilder appendSql(String sql) {
        super.appendSql(sql);
        return this;
    }

    public BatchSqlBuilder appendBatchSqlParameters(Collection<Object> parameters) {
        checkParameters();
        if (parameters == null)
            return this;
        if (parameters.size() != batchParametersSize)
            throw new SqlBuildException("Parameter count mismatch: expected " + batchParametersSize + " parameters, but got " + parameters.size() + " parameters.");
        List<Object> batchParameters = newBatchParameters();
        batchParameters.addAll(parameters);
        sqlParameters.add(batchParameters);
        return this;
    }

    public BatchSqlBuilder appendBatchSqlParameters(Object... parameters) {
        if (parameters == null)
            return this;
        if (parameters.length != batchParametersSize)
            throw new SqlBuildException("Parameter count mismatch: expected " + batchParametersSize + " parameters, but got " + parameters.length + " parameters.");
        List<Object> batchParameters = newBatchParameters();
        for (Object parameter : parameters) batchParameters.add(parameter);
        sqlParameters.add(batchParameters);
        return this;
    }

    public BatchSqlBuilder appendBatchSqlFragment(String sqlFragment, Collection<Object> parameters) {
        appendBatchSqlParameters(parameters);
        sqlStringBuilder.append(sqlFragment);
        return this;
    }

    public BatchSqlBuilder appendBatchSqlFragment(String sqlFragment, Object... parameters) {
        appendBatchSqlParameters(parameters);
        sqlStringBuilder.append(sqlFragment);
        return this;
    }

    protected List<Object> newBatchParameters() {
        if (batchParametersSize > 0)
            return new ArrayList<>(batchParametersSize);
        else
            throw new SqlBuildException("You are trying to create parameters list to a non-prepared statement.");
    }

    @Override
    public BatchSql build() throws SqlBuildException {
        return new BatchSqlImpl(sqlStringBuilder.toString(), sqlParameters == null ? Collections.emptyList() : sqlParameters);
    }

    public static class BatchSqlImpl implements BatchSql {
        final String sqlString;
        final List<List<Object>> paramsLists;

        public BatchSqlImpl(String sqlString, List<List<Object>> paramsLists) {
            this.sqlString = sqlString;
            this.paramsLists = paramsLists;
        }

        public String getSqlString() {
            return sqlString;
        }

        @Override
        public List<List<Object>> getParamsLists() {
            return paramsLists;
        }

        @Override
        public String getSqlInfo() {
            if (paramsLists == null) {
                return "sql:" + sqlString;
            } else {
                return "sql:" + sqlString + System.lineSeparator() + "with parameters: " + paramsLists;
            }
        }
    }
}
