package io.github.maxwellnie.free.jdbc.single;

import io.github.maxwellnie.free.jdbc.SqlBuildException;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Maxwell Nie
 */
public class BoundSingleSqlBuilder extends SingleSqlBuilder{
    protected int expectedParametersSize;
    public BoundSingleSqlBuilder(StringBuilder stringBuilder, int expectedParametersSize){
        super(stringBuilder);
        if (stringBuilder == null)
            throw new SqlBuildException("stringBuilder cannot be null");
        if (expectedParametersSize < 0)
            throw new SqlBuildException("expectedParametersSize must be greater than 0");
        this.expectedParametersSize = expectedParametersSize;
        sqlParameters = new ArrayList<>(expectedParametersSize);
    }
    public BoundSingleSqlBuilder(int expectedParametersSize){
        this(new StringBuilder(), expectedParametersSize);
        this.expectedParametersSize = expectedParametersSize;
    }
    public BoundSingleSqlBuilder(){
        this(new StringBuilder(), 0);
        this.expectedParametersSize = -1;
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
    protected void validateParametersSize(){
        if (expectedParametersSize != -1 && sqlParameters.size() != expectedParametersSize)
            throw new SqlBuildException("expectedParametersSize is " + expectedParametersSize + " but actual is " + sqlParameters.size());
    }

    @Override
    public SingleSql build() {
        validateParametersSize();
        return super.build();
    }
}
