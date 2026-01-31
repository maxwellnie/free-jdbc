

import io.github.maxwellnie.free.jdbc.single.BoundSingleSqlBuilder;
import io.github.maxwellnie.free.jdbc.single.SingleSqlBuilder;
import io.github.maxwellnie.free.jdbc.statement.*;
import io.github.maxwellnie.free.jdbc.statement.SimpleIntegratedStatement;
import io.github.maxwellnie.free.jdbc.single.SingleSql;

import java.sql.*;

/**
 * Basic CRUD Operations Example
 */
public class CrudOperationsExample {
    public static void main(String[] args) {
        // Example connection settings (modify according to actual database configuration)
        String url = "jdbc:h2:mem:testdb"; // Using in-memory database as example
        String username = "sa";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            
            // Example 1: Create table
            createTable(connection);
            
            // Example 2: Insert data
            insertData(connection);
            
            // Example 3: Query data
            selectData(connection);
            
            // Example 4: Update data
            updateData(connection);
            
            // Example 5: Delete data
            deleteData(connection);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create Table Example
     */
    public static void createTable(Connection connection) throws SQLException {
        System.out.println("=== Create Table Example ===");
        
        String createTableSql = "CREATE TABLE IF NOT EXISTS users (" +
                "id INT PRIMARY KEY," +
                "name VARCHAR(100)," +
                "email VARCHAR(100)" +
                ")";
        
        try (SimpleIntegratedStatement stmt = new SimpleIntegratedStatement(connection)) {
            boolean result = stmt.createStatement(createTableSql)
                    .execute(Executor.STATEMENT_EXECUTOR);
            
            System.out.println("Table created successfully: " + !result); // execute returns false for DDL
        }
    }

    /**
     * Insert Data Example
     */
    public static void insertData(Connection connection) throws SQLException {
        System.out.println("\n=== Insert Data Example ===");
        
        SingleSql singleSql = new BoundSingleSqlBuilder(3)
                .appendSql("INSERT INTO users(id, name, email) VALUES(")
                .appendSqlParameter(1)
                .appendSql("?, ")
                .appendSqlParameter("Zhang San")
                .appendSql("?, ")
                .appendSqlParameter("zhangsan@example.com")
                .appendSql("?)")
                .build();
        
        try (PreparedIntegratedStatement stmt = new PreparedIntegratedStatement(connection)) {
            int result = stmt.createStatement(singleSql.getSqlString())
                    .parameterize(SingleSql.SINGLE_SQL_PARAMETERS_HANDLER, singleSql)
                    .execute(Executor.PREPARED_STATEMENT_UPDATE_EXECUTOR);
            
            System.out.println("Data inserted successfully: " + (result >0));
        }
    }

    /**
     * Query Data Example
     */
    public static void selectData(Connection connection) throws SQLException {
        System.out.println("\n=== Query Data Example ===");
        
        SingleSql singleSql = new BoundSingleSqlBuilder(1)
                .appendSql("SELECT id, name, email FROM users WHERE id = ?")
                .appendSqlParameter(1)
                .build();
        
        try (PreparedIntegratedStatement stmt = new PreparedIntegratedStatement(connection)) {
            stmt.createStatement(singleSql.getSqlString())
                    .parameterize(SingleSql.SINGLE_SQL_PARAMETERS_HANDLER, singleSql)
                    .execute((ResultParser<PreparedStatement, SingleSql, Object>) (preparedStmt, sql) -> {
                        try (ResultSet rs = preparedStmt.executeQuery()) {
                            while (rs.next()) {
                                int id = rs.getInt("id");
                                String name = rs.getString("name");
                                String email = rs.getString("email");
                                System.out.printf("ID: %d, Name: %s, Email: %s%n", id, name, email);
                            }
                        }catch (SQLException e){
                            throw new ResultParserException();
                        }
                        return null;
                    });
        } catch (ResultParserException e) {
            throw new SQLException(e);
        }
    }

    /**
     * Update Data Example
     */
    public static void updateData(Connection connection) throws SQLException {
        System.out.println("\n=== Update Data Example ===");
        
        SingleSql singleSql = new BoundSingleSqlBuilder(2)
                .appendSql("UPDATE users SET email = ?")
                .appendSqlParameter("zhangsan_updated@example.com")
                .appendSql(" WHERE id = ?")
                .appendSqlParameter(1)
                .build();
        
        try (PreparedIntegratedStatement stmt = new PreparedIntegratedStatement(connection)) {
            int affectedRows = stmt.createStatement(singleSql.getSqlString())
                    .parameterize(SingleSql.SINGLE_SQL_PARAMETERS_HANDLER, singleSql)
                    .execute(Executor.PREPARED_STATEMENT_UPDATE_EXECUTOR);
            
            System.out.println("Updated rows count: " + affectedRows);
        }
    }

    /**
     * Delete Data Example
     */
    public static void deleteData(Connection connection) throws SQLException {
        System.out.println("\n=== Delete Data Example ===");
        
        SingleSql singleSql = new BoundSingleSqlBuilder(1)
                .appendSql("DELETE FROM users WHERE id = ?")
                .appendSqlParameter(1)
                .build();
        
        try (PreparedIntegratedStatement stmt = new PreparedIntegratedStatement(connection)) {
            int affectedRows = stmt.createStatement(singleSql.getSqlString())
                    .parameterize(SingleSql.SINGLE_SQL_PARAMETERS_HANDLER, singleSql)
                    .execute(Executor.PREPARED_STATEMENT_UPDATE_EXECUTOR);
            
            System.out.println("Deleted rows count: " + affectedRows);
        }
    }
}