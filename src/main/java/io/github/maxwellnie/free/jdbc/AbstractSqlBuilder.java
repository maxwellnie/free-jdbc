package io.github.maxwellnie.free.jdbc;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Maxwell Nie
 */
public abstract class AbstractSqlBuilder<S extends Sql, P> implements SqlBuilder<S> {
    protected StringBuilder sqlStringBuilder;
    protected List<P> sqlParameters;

    public AbstractSqlBuilder(int parametersSize) {
        this.sqlStringBuilder = new StringBuilder();
        if (parametersSize > 0)
            this.sqlParameters = new ArrayList<>(parametersSize);
    }

    public AbstractSqlBuilder() {
        this(0);
    }

    public AbstractSqlBuilder<S, P> appendSql(String sql) {
        sqlStringBuilder.append(sql);
        return this;
    }

    protected void checkParameters() throws SqlBuildException {
        if (sqlParameters == null)
            throw new SqlBuildException("You are trying to add parameters to a non-prepared statement.");
    }
}
