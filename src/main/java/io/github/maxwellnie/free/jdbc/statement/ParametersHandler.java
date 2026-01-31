package io.github.maxwellnie.free.jdbc.statement;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * Functional interface for handling statement parameters, used to set parameters for SQL statements
 * or configure input/output parameters for stored procedures.
 *
 * @param <S> Specific type of Statement, must be a subclass of Statement
 * @param <P> Type of parameters to be set
 * @author Maxwell Nie
 */
@FunctionalInterface
public
interface ParametersHandler<S extends Statement, P> {
    void parameterize(S statement, P parameters) throws SQLException;
}