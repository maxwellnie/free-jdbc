package io.github.maxwellnie.free.jdbc.single;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Maxwell Nie
 */
public class BoundSingleSqlBuilder extends SingleSqlBuilder{
    public BoundSingleSqlBuilder(int exceptedParametersSize) {
        this.sqlParameters = new ArrayList<>(exceptedParametersSize);
    }
    public BoundSingleSqlBuilder() {
        this.sqlParameters = new ArrayList<>();
    }
    public BoundSingleSqlBuilder(StringBuilder sqlStringBuilder, int exceptedParametersSize) {
        super(sqlStringBuilder);
        this.sqlParameters = new ArrayList<>(exceptedParametersSize);
    }
    public BoundSingleSqlBuilder(StringBuilder sqlStringBuilder) {
        super(sqlStringBuilder);
        this.sqlParameters = new ArrayList<>();
    }

    @Override
    public BoundSingleSqlBuilder appendSql(String sql) {
        super.appendSql(sql);
        return this;
    }

    public BoundSingleSqlBuilder appendSqlParameter(Object parameter) {
        sqlParameters.add(parameter);
        return this;
    }

    public BoundSingleSqlBuilder appendSqlParameters(Object... parameters) {
        if (parameters == null)
            return this;
        for (Object parameter : parameters) sqlParameters.add(parameter);
        return this;
    }

    public BoundSingleSqlBuilder appendSqlParameters(Collection<Object> parameters) {
        if (parameters == null)
            return this;
        sqlParameters.addAll(parameters);
        return this;
    }

    public BoundSingleSqlBuilder appendSqlFragment(String sqlFragment, Collection<Object> parameters) {
        appendSqlParameters(parameters);
        sqlStringBuilder.append(sqlFragment);
        return this;
    }

    public BoundSingleSqlBuilder appendSqlFragment(String sqlFragment, Object... parameters) {
        appendSqlParameters(parameters);
        sqlStringBuilder.append(sqlFragment);
        return this;
    }

    public BoundSingleSqlBuilder appendSqlFragment(String sqlFragment, Object parameters) {
        appendSqlParameter(parameters);
        sqlStringBuilder.append(sqlFragment);
        return this;
    }
}
