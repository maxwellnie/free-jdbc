
import io.github.maxwellnie.free.jdbc.single.SingleSqlBuilder;
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

            // Example 3: Call simple stored procedure
            callSimpleStoredProcedure(connection);

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
     * Call stored procedure with input/output parameters example
     */
    public static void callStoredProcedureWithInOutParams(Connection connection) throws SQLException {
        System.out.println("\n=== Call Stored Procedure With Input/Output Parameters Example ===");

        String storedProcCall = "{ ? = CALL UPDATE_STRING(?) }";

        try (CallableIntegrateStatement stmt = new CallableIntegrateStatement(connection)) {
            String result = stmt.createStatement(storedProcCall)
                .parameterize((callableStatement, params) -> {
                    // Register return parameter
                    callableStatement.registerOutParameter(1, Types.VARCHAR);
                    // Set input parameter
                    callableStatement.setString(2, "initial_value");
                }, null)
                .execute((ResultParser<CallableStatement, Object, String>) (callableStatement, params) -> {
                    try {
                        // Execute stored procedure
                        callableStatement.execute();

                        // Get output parameter value
                        String outputValue = callableStatement.getString(1);
                        System.out.println("Output parameter value: " + outputValue);

                        return outputValue;
                    } catch (SQLException e) {
                        throw new ResultParserException(e);
                    }
                });
        }
    }

    /**
     * Call stored procedure with return parameter example
     */
    public static void callStoredProcedureWithReturnParam(Connection connection) throws SQLException {
        System.out.println("\n=== Call Stored Procedure With Return Parameter Example ===");

        String storedProcCall = "{ ? = CALL GET_USER_COUNT() }";

        try (CallableIntegrateStatement stmt = new CallableIntegrateStatement(connection)) {
            Integer userCount = stmt.createStatement(storedProcCall)
                .parameterize((callableStatement, params) -> {
                    // Register return parameter
                    callableStatement.registerOutParameter(1, Types.INTEGER);
                }, null)
                .execute((ResultParser<CallableStatement, Object, Integer>) (callableStatement, params) -> {
                    try {
                        // Execute stored procedure
                        callableStatement.execute();

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
     * Call simple stored procedure example (no return value)
     */
    public static void callSimpleStoredProcedure(Connection connection) throws SQLException {
        System.out.println("\n=== Call Simple Stored Procedure Example ===");

        String storedProcCall = "CALL NOW()";

        try (CallableIntegrateStatement stmt = new CallableIntegrateStatement(connection)) {
            Boolean hasResultSet = stmt.createStatement(storedProcCall)
                .execute((Executor<CallableStatement, Boolean>) (callableStatement, params) -> {
                    // Execute stored procedure and return if there is result set
                    try {
                        return callableStatement.execute();
                    } catch (SQLException e) {
                        throw new SqlExecutionException(e);
                    }
                });

            System.out.println("Execution completed, has result set: " + hasResultSet);
        }
    }
}
