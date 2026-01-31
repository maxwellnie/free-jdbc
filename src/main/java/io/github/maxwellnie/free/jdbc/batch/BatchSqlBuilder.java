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
    public BatchSqlBuilder() {
    }

    public BatchSqlBuilder(StringBuilder sqlStringBuilder) {
        super(sqlStringBuilder);
    }

    /**
     * Appends a single SQL statement to the batch.
     * NOTE: Ensure that multiple SQL statements are separated by semicolons (;) when appending multiple statements.
     *
     * @param sql The SQL statement to append
     * @return This BatchSqlBuilder instance for method chaining
     */
    public BatchSqlBuilder appendSingleSql(String sql) {
        sqlStringBuilder.append(sql);
        return this;
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
