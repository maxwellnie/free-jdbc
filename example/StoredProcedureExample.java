
import io.github.maxwellnie.free.jdbc.callable.CallableSql;
import io.github.maxwellnie.free.jdbc.callable.CallableSqlBuilder;
import io.github.maxwellnie.free.jdbc.statement.*;
import io.github.maxwellnie.free.jdbc.statement.CallableIntegrateStatement;

import java.sql.*;

/**
 * Stored Procedure Call Example
 */
public class StoredProcedureExample {
    public static void main(String[] args) {
        // Example connection settings (modify according to actual database configuration)
        String url = "jdbc:h2:mem:testdb"; // Using in-memory database as example
        String username = "sa";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {

            createStoredProcedures(connection);

            // Example 1: Call stored procedure with input/output parameters
            callStoredProcedureWithInOutParams(connection);

            // Example 2: Call stored procedure with return parameter
            callStoredProcedureWithReturnParam(connection);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createStoredProcedures(Connection connection) throws SQLException {
        System.out.println("=== Creating Stored Procedures ===");

        try (Statement stmt = connection.createStatement()) {
            try {
                stmt.execute("DROP ALIAS IF EXISTS GET_USER_COUNT");
            } catch (SQLException e) {
            }

            stmt.execute("CREATE ALIAS GET_USER_COUNT AS $$ int GET_USER_COUNT() { return 42; } $$");
            System.out.println("Created GET_USER_COUNT function");
        }

        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP ALIAS IF EXISTS UPDATE_STRING");
            stmt.execute("CREATE ALIAS UPDATE_STRING AS $$ String UPDATE_STRING(String input) { return input + \"_updated\"; } $$");
            System.out.println("Created UPDATE_STRING function");
        } catch (SQLException e) {
            System.out.println("Could not create UPDATE_STRING function: " + e.getMessage());
        }
    }

    /**
     * Call stored procedure with return parameter using CallableSql
     */
    public static void callStoredProcedureWithReturnParam(Connection connection) throws SQLException, SqlExecutionException, ResultParserException {
        System.out.println("\n=== Call Stored Procedure With Return Parameter Using CallableSql ===");

        // Create CallableSql using builder pattern
        CallableSql callableSql = new CallableSqlBuilder("{ ? = CALL GET_USER_COUNT() }")
                .appendOutParameter(Types.INTEGER) // Return parameter is OUT parameter
                .build();

        try (CallableIntegrateStatement stmt = new CallableIntegrateStatement(connection)) {
            Integer userCount = stmt.execute(callableSql, (callableStatement, result) -> {
                try {
                    // Get return value
                    return callableStatement.getInt(1);
                } catch (SQLException e) {
                    throw new ResultParserException(e);
                }
            });

            System.out.println("User count: " + userCount);
        }
    }

    /**
     * Call stored procedure with input/output parameters using CallableSql
     */
    public static void callStoredProcedureWithInOutParams(Connection connection) throws SQLException, SqlExecutionException, ResultParserException {
        System.out.println("\n=== Call Stored Procedure With Input/Output Parameters Using CallableSql ===");

        // Create CallableSql using builder pattern
        CallableSql callableSql = new CallableSqlBuilder("{ ? = CALL UPDATE_STRING(?) }")
                .appendOutParameter(Types.VARCHAR) // Return parameter is OUT parameter
                .appendInParameter("initial_value", Types.VARCHAR) // Input parameter
                .build();

        try (CallableIntegrateStatement stmt = new CallableIntegrateStatement(connection)) {
            String result = stmt.execute(callableSql, (callableStatement, executeResult) -> {
                try {
                    // Get return value
                    return callableStatement.getString(1);
                } catch (SQLException e) {
                    throw new ResultParserException(e);
                }
            });

            System.out.println("Result: " + result);
        }
    }
}
