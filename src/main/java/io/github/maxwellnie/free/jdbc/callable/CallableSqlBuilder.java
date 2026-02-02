package io.github.maxwellnie.free.jdbc.callable;

import io.github.maxwellnie.free.jdbc.AbstractSqlBuilder;
import io.github.maxwellnie.free.jdbc.SqlBuildException;
import io.github.maxwellnie.free.jdbc.SqlBuilder;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Maxwell Nie
 */
public class CallableSqlBuilder extends AbstractSqlBuilder<CallableSql, CallableSql.CallableParameter> implements SqlBuilder<CallableSql> {


    public CallableSqlBuilder() {
        super();
        sqlParameters = new ArrayList<>();
    }

    public CallableSqlBuilder(StringBuilder sqlStringBuilder) {
        super(sqlStringBuilder);
        sqlParameters = new ArrayList<>();
    }

    public CallableSqlBuilder(String sqlString) {
        super(new StringBuilder(sqlString));
        sqlParameters = new ArrayList<>();
    }

    public CallableSqlBuilder appendInParameter(Object parameter, int sqlTypes) {
        sqlParameters.add(new CallableSql.CallableParameter(parameter, sqlTypes, CallableSql.ParameterType.IN));
        return this;
    }

    public CallableSqlBuilder appendOutParameter(int sqlTypes) {
        sqlParameters.add(new CallableSql.CallableParameter(null, sqlTypes, CallableSql.ParameterType.OUT));
        return this;
    }

    public CallableSqlBuilder appendInOutParameter(Object parameter, int sqlTypes) {
        sqlParameters.add(new CallableSql.CallableParameter(parameter, sqlTypes, CallableSql.ParameterType.IN_OUT));
        return this;
    }

    @Override
    public CallableSql build() throws SqlBuildException {
        if (sqlStringBuilder == null || sqlStringBuilder.length() == 0) {
            throw new SqlBuildException("SQL string cannot be empty");
        }
        return new CallableSqlImpl(sqlStringBuilder.toString(), sqlParameters);
    }

    public static class CallableSqlImpl implements CallableSql {
        private final String sqlString;
        private final List<CallableParameter> callableParameters;

        public CallableSqlImpl(String sqlString, List<CallableParameter> callableParameters) {
            this.sqlString = sqlString;
            this.callableParameters = callableParameters;
        }

        private CallableSqlImpl(Builder builder) {
            this.sqlString = builder.sqlString;
            this.callableParameters = builder.callableParameters;
        }

        @Override
        public String getSqlString() {
            return sqlString;
        }

        @Override
        public List<CallableParameter> getCallableParameters() {
            return callableParameters;
        }

        public static class Builder {
            private final String sqlString;
            private final List<CallableParameter> callableParameters = new ArrayList<>();

            public Builder(String sqlString) {
                this.sqlString = sqlString;
            }

            public Builder addInParameter(Object parameter, int sqlTypes) {
                callableParameters.add(new CallableParameter(parameter, sqlTypes, ParameterType.IN));
                return this;
            }

            public Builder addOutParameter(int sqlTypes) {
                callableParameters.add(new CallableParameter(null, sqlTypes, ParameterType.OUT));
                return this;
            }

            public Builder addInOutParameter(Object parameter, int sqlTypes) {
                callableParameters.add(new CallableParameter(parameter, sqlTypes, ParameterType.IN_OUT));
                return this;
            }

            public CallableSqlImpl build() {
                return new CallableSqlImpl(this);
            }
        }
    }
}
