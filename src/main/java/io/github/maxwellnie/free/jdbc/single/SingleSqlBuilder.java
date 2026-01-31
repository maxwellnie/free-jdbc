package io.github.maxwellnie.free.jdbc.single;

import io.github.maxwellnie.free.jdbc.AbstractSqlBuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Maxwell Nie
 */
public class SingleSqlBuilder extends AbstractSqlBuilder<SingleSql, Object> {
    public SingleSqlBuilder() {
    }

    public SingleSqlBuilder(StringBuilder sqlStringBuilder) {
        super(sqlStringBuilder);
    }

    public SingleSqlBuilder appendSql(String sql) {
        this.sqlStringBuilder.append(sql);
        return this;
    }
    @Override
    public SingleSql build() {
        return new SingleSqlImpl(sqlStringBuilder.toString(), sqlParameters == null ? Collections.emptyList() : sqlParameters);
    }

    public static class SingleSqlImpl implements SingleSql {
        final String sqlString;
        final List<Object> sqlParameters;

        public SingleSqlImpl(String sqlString, List<Object> sqlParameters) {
            this.sqlString = sqlString;
            this.sqlParameters = sqlParameters;
        }

        @Override
        public String getSqlString() {
            return sqlString;
        }

        @Override
        public List<Object> getSqlParameters() {
            return sqlParameters;
        }

        @Override
        public String getSqlInfo() {
            if (sqlParameters == null) {
                return "sql:" + sqlString;
            } else {
                return "sql:" + sqlString + System.lineSeparator() + "with parameters: " + sqlParameters;
            }
        }
    }
}
