

import io.github.maxwellnie.free.jdbc.batch.BatchSqlBuilder;
import io.github.maxwellnie.free.jdbc.batch.BoundBatchSqlBuilder;
import io.github.maxwellnie.free.jdbc.statement.ConfigurationImpl;
import io.github.maxwellnie.free.jdbc.statement.Executor;
import io.github.maxwellnie.free.jdbc.statement.PreparedIntegratedStatement;
import io.github.maxwellnie.free.jdbc.batch.BatchSql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;


/**
 * Batch Operation Example
 */
public class BatchOperationExample {
    public static void main(String[] args) {
        // Example connection settings (modify according to actual database configuration)
        String url = "jdbc:h2:mem:testdb"; // Using in-memory database as example
        String username = "sa";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            CrudOperationsExample.createTable(connection);
            // Example 1: Batch insert data
            batchInsertExample(connection);
            CrudOperationsExample.selectData(connection);
            // Example 2: Batch update data
            batchUpdateExample(connection);
            CrudOperationsExample.selectData(connection);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Batch Insert Example
     */
    public static void batchInsertExample(Connection connection) throws SQLException {
        System.out.println("=== Batch Insert Example ===");
        
        // Create batch SQL - insert user data
        BoundBatchSqlBuilder batchSqlBuilder = new BoundBatchSqlBuilder(3); // Each row has 3 parameters (id, name, email)
        batchSqlBuilder.appendSingleSql("INSERT INTO users(id, name, email) VALUES(?, ?, ?)")
                .appendBatchSqlParameters(Arrays.asList(1, "Zhang San", "zhangsan@example.com"))
                .appendBatchSqlParameters(Arrays.asList(2, "Li Si", "lisi@example.com"))
                .appendBatchSqlParameters(Arrays.asList(3, "Wang Wu", "wangwu@example.com"));
        BatchSql batchSql = batchSqlBuilder.build();

        // Execute batch insert
        try (PreparedIntegratedStatement stmt = new PreparedIntegratedStatement(connection)) {
            // Set configuration (optional)
            stmt.setConfiguration(new ConfigurationImpl()
                    .setQueryTimeout(30)
                    .setFetchSize(100));

            // Execute batch operation
            int[] results = stmt.executeBatch(batchSql);
            /*int[] results = stmt.createStatement(batchSql.getSqlString())
                    .parameterize(batchSql.BATCH_SQL_PARAMETERS_HANDLER, batchSql)
                    .execute(Executor.PREPARED_STATEMENT_BATCH_EXECUTOR);*/

            System.out.println("Batch insert result: " + Arrays.toString(results));
        }
    }

    /**
     * Batch Update Example
     */
    public static void batchUpdateExample(Connection connection) throws SQLException {
        System.out.println("\n=== Batch Update Example ===");
        
        // Create batch SQL - update user data
        BoundBatchSqlBuilder batchSqlBuilder = new BoundBatchSqlBuilder(2); // Each row has 2 parameters (new_email, user_id)
        batchSqlBuilder.appendSingleSql("UPDATE users SET email = ? WHERE id = ?")
                .appendBatchSqlParameters(Arrays.asList("zhangsan_new@example.com", 1))
                .appendBatchSqlParameters(Arrays.asList("lisi_new@example.com", 2));
        BatchSql batchSql = batchSqlBuilder.build();

        // Execute batch update
        try (PreparedIntegratedStatement stmt = new PreparedIntegratedStatement(connection)) {
            int[] results = stmt.executeBatch(batchSql);
            /*int[] results = stmt.createStatement(batchSql.getSqlString())
                    .parameterize(batchSql.BATCH_SQL_PARAMETERS_HANDLER, batchSql)
                    .execute(Executor.PREPARED_STATEMENT_BATCH_EXECUTOR);*/

            System.out.println("Batch update result: " + Arrays.toString(results));
        }
    }
}