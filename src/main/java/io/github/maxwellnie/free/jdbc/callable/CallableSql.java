package io.github.maxwellnie.free.jdbc.callable;

import io.github.maxwellnie.free.jdbc.Sql;
import io.github.maxwellnie.free.jdbc.statement.ParametersHandler;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Maxwell Nie
 */
public interface CallableSql extends Sql {
    String getSqlString();

    List<CallableParameter> getCallableParameters();

    class CallableParameter {
        public CallableParameter(Object parameter, int sqlTypes, ParameterType parameterType) {
            this.parameter = parameter;
            this.sqlTypes = sqlTypes;
            this.parameterType = parameterType;
        }

        Object parameter;
        int sqlTypes;
        ParameterType parameterType;
    }

    enum ParameterType {
        IN,
        OUT,
        IN_OUT
    }

    @Override
    default String getSqlInfo() {
        return getSqlString();
    }

    ParametersHandler<CallableStatement, CallableSql> CALLABLE_SQL_PARAMETERS_HANDLER = (statement, sql) -> {
        if (sql == null || sql.getCallableParameters() == null) {
            return;
        }

        for (int i = 0; i < sql.getCallableParameters().size(); i++) {
            CallableParameter parameter = sql.getCallableParameters().get(i);
            int paramIndex = i + 1;

            switch (parameter.parameterType) {
                case IN:
                    statement.setObject(paramIndex, parameter.parameter, parameter.sqlTypes);
                    break;
                case OUT:
                    statement.registerOutParameter(paramIndex, parameter.sqlTypes);
                    break;
                case IN_OUT:
                    statement.setObject(paramIndex, parameter.parameter, parameter.sqlTypes);
                    statement.registerOutParameter(paramIndex, parameter.sqlTypes);
                    break;
            }
        }
    };
}

