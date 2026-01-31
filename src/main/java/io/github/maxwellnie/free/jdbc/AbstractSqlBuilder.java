package io.github.maxwellnie.free.jdbc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Maxwell Nie
 */
public abstract class AbstractSqlBuilder<S extends Sql, P> implements SqlBuilder<S> {
    protected StringBuilder sqlStringBuilder;
    protected List<P> sqlParameters;
    public AbstractSqlBuilder() {
        sqlStringBuilder = new StringBuilder();
    }
    public AbstractSqlBuilder(StringBuilder sqlStringBuilder){
        this.sqlStringBuilder = sqlStringBuilder;
    }
}
