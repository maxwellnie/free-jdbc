package io.github.maxwellnie.free.jdbc.single;

import io.github.maxwellnie.free.jdbc.Sql;
import io.github.maxwellnie.free.jdbc.statement.ParametersHandler;

import java.sql.PreparedStatement;
import java.util.List;

/**
 * @author Maxwell Nie
 */
public interface SingleSql extends Sql {
    ParametersHandler<PreparedStatement, SingleSql> SINGLE_SQL_PARAMETERS_HANDLER = (statement, sql) -> {
        for (int i = 0; i < sql.getSqlParameters().size(); i++) {
            statement.setObject(i + 1, sql.getSqlParameters().get(i));
        }
    };

    String getSqlString();

    List<Object> getSqlParameters();

    default String getSqlInfo() {
        return getSqlString();
    }
}
