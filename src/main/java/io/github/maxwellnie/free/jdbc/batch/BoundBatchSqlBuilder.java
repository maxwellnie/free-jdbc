package io.github.maxwellnie.free.jdbc.batch;

import io.github.maxwellnie.free.jdbc.SqlBuildException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A specialized batch SQL builder that enforces a fixed parameter count for each batch entry.
 * This ensures that each batch operation has exactly the expected number of parameters,
 * preventing parameter count mismatches during batch execution.
 *
 * @author Maxwell Nie
 */
public class BoundBatchSqlBuilder extends BatchSqlBuilder{
    protected int batchParametersSize;

    /**
     * Constructs a BoundBatchSqlBuilder with the specified parameter count per batch entry.
     *
     * @param batchParametersSize The required number of parameters for each batch entry.
     *                           Must be greater than 0, otherwise a SqlBuildException will be thrown.
     * @throws SqlBuildException if batchParametersSize is less than or equal to 0
     */
    public BoundBatchSqlBuilder(int batchParametersSize) {
        if (batchParametersSize <= 0)
            throw new SqlBuildException("Batch parameters size must be greater than 0.");
        this.batchParametersSize = batchParametersSize;
        this.sqlParameters = new ArrayList<>();
    }

    /**
     * Constructs a BoundBatchSqlBuilder with the specified SQL string builder and parameter count per batch entry.
     *
     * @param sqlStringBuilder The StringBuilder containing the base SQL string for the batch operation.
     * @param batchParametersSize The required number of parameters for each batch entry.
     *                           Must be greater than 0, otherwise a SqlBuildException will be thrown.
     * @throws SqlBuildException if batchParametersSize is less than or equal to 0
     */
    public BoundBatchSqlBuilder(StringBuilder sqlStringBuilder, int batchParametersSize) {
        super(sqlStringBuilder);
        if (batchParametersSize <= 0)
            throw new SqlBuildException("Batch parameters size must be greater than 0.");
        this.batchParametersSize = batchParametersSize;
        this.sqlParameters = new ArrayList<>();
    }

    @Override
    public BoundBatchSqlBuilder appendSingleSql(String sql) {
        super.appendSingleSql(sql);
        return this;
    }

    /**
     * Appends a collection of parameters for a single batch entry.
     * The number of parameters in the collection must exactly match the expected batch parameter size.
     *
     * @param parameters The collection of parameters for one batch entry
     * @return This BoundBatchSqlBuilder instance for method chaining
     * @throws SqlBuildException if the parameter count doesn't match the expected batch parameter size
     */
    public BoundBatchSqlBuilder appendBatchSqlParameters(Collection<Object> parameters) {
        if (parameters == null)
            return this;
        if (parameters.size() != batchParametersSize)
            throw new SqlBuildException("Parameter count mismatch: expected " + batchParametersSize + " parameters, but got " + parameters.size() + " parameters.");
        List<Object> batchParameters = new ArrayList<>(parameters);
        sqlParameters.add(batchParameters);
        return this;
    }


    /**
     * Appends a variable argument list of parameters for a single batch entry.
     * The number of parameters in the array must exactly match the expected batch parameter size.
     *
     * @param parameters The array of parameters for one batch entry
     * @return This BoundBatchSqlBuilder instance for method chaining
     * @throws SqlBuildException if the parameter count doesn't match the expected batch parameter size
     */
    public BoundBatchSqlBuilder appendBatchSqlParameters(Object... parameters) {
        if (parameters == null)
            return this;
        if (parameters.length != batchParametersSize)
            throw new SqlBuildException("Parameter count mismatch: expected " + batchParametersSize + " parameters, but got " + parameters.length + " parameters.");
        List<Object> batchParameters = new ArrayList<>(parameters.length);
        for (Object parameter : parameters) batchParameters.add(parameter);
        sqlParameters.add(batchParameters);
        return this;
    }

    /**
     * Appends a SQL fragment along with its corresponding parameters for a single batch entry.
     * This method combines the SQL fragment with the provided parameters as one batch entry.
     *
     * @param sqlFragment The SQL fragment to append
     * @param parameters The parameters corresponding to the SQL fragment
     * @return This BoundBatchSqlBuilder instance for method chaining
     */
    public BatchSqlBuilder appendSingleSql(String sqlFragment, Collection<Object> parameters) {
        appendBatchSqlParameters(parameters);
        appendSingleSql(sqlFragment);
        return this;
    }

    /**
     * Appends a SQL fragment along with its corresponding parameters for a single batch entry.
     * This method combines the SQL fragment with the provided parameters as one batch entry.
     *
     * @param sqlFragment The SQL fragment to append
     * @param parameters The parameters corresponding to the SQL fragment
     * @return This BoundBatchSqlBuilder instance for method chaining
     */
    public BoundBatchSqlBuilder appendSingleSql(String sqlFragment, Object... parameters) {
        appendBatchSqlParameters(parameters);
        appendSingleSql(sqlFragment);
        return this;
    }

}
