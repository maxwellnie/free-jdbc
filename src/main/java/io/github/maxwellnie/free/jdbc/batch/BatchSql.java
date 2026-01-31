package io.github.maxwellnie.free.jdbc.batch;

import io.github.maxwellnie.free.jdbc.Sql;
import io.github.maxwellnie.free.jdbc.statement.ParametersHandler;

import java.sql.PreparedStatement;
import java.util.List;

/**
 * @author Maxwell Nie
 */
public interface BatchSql extends Sql {
    ParametersHandler<PreparedStatement, BatchSql> BATCH_SQL_PARAMETERS_HANDLER = (statement, sql) -> {
        for (int i = 0; i < sql.getParamsLists().size(); i++) {
            List<Object> parameters = sql.getParamsLists().get(i);
            for (int j = 0; j < parameters.size(); j++) {
                statement.setObject(j + 1, parameters.get(j));
            }
            statement.addBatch();
        }
    };

    String getSqlString();

    List<List<Object>> getParamsLists();
}
