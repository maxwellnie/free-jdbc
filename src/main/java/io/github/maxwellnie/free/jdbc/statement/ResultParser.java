package io.github.maxwellnie.free.jdbc.statement;

import java.io.Serializable;

/**
 * @author Maxwell Nie
 */
@FunctionalInterface
public interface ResultParser<O, P, R> extends Serializable {
    long serialVersionUID = 135238493948L;

    R parse(O originalObject, P parameters) throws ResultParserException;
}
