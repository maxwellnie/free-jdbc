package io.github.maxwellnie.free.jdbc;

/**
 * @author Maxwell Nie
 */
public interface SqlBuilder<S extends Sql> {
    S build() throws SqlBuildException;
}
