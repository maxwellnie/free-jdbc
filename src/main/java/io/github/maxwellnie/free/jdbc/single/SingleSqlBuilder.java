package io.github.maxwellnie.free.jdbc.single;

import io.github.maxwellnie.free.jdbc.AbstractSqlBuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Maxwell Nie
 */
public class SingleSqlBuilder extends AbstractSqlBuilder<SingleSql, Object> {

    public SingleSqlBuilder(int parametersSize) {
        super(parametersSize);
    }

    public SingleSqlBuilder() {
        super();
    }

    @Override
    public SingleSqlBuilder appendSql(String sql) {
        super.appendSql(sql);
        return this;
    }

    public SingleSqlBuilder appendSqlParameter(Object parameter) {
        checkParameters();
        sqlParameters.add(parameter);
        return this;
    }

    public SingleSqlBuilder appendSqlParameters(Object... parameters) {
        if (parameters == null)
            return this;
        checkParameters();
        for (Object parameter : parameters) sqlParameters.add(parameter);
        return this;
    }

    public SingleSqlBuilder appendSqlParameters(Collection<Object> parameters) {
        if (parameters == null)
            return this;
        checkParameters();
        sqlParameters.addAll(parameters);
        return this;
    }

    public SingleSqlBuilder appendSqlFragment(String sqlFragment, Collection<Object> parameters) {
        appendSqlParameters(parameters);
        sqlStringBuilder.append(sqlFragment);
        return this;
    }

    public SingleSqlBuilder appendSqlFragment(String sqlFragment, Object... parameters) {
        appendSqlParameters(parameters);
        sqlStringBuilder.append(sqlFragment);
        return this;
    }

    public SingleSqlBuilder appendSqlFragment(String sqlFragment, Object parameters) {
        appendSqlParameter(parameters);
        sqlStringBuilder.append(sqlFragment);
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
