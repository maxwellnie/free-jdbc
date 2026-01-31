# Free-JDBC

Free-JDBC is a lightweight Java JDBC framework designed to simplify database operations with fluent APIs. It provides features like SQL building, batch processing, statement handling, and result parsing with minimal overhead.

## Features

- **Lightweight Design**: Clean architecture with no extra dependencies
- **Fluent API**: Supports method chaining for elegant code
- **SQL Builder**: Dynamic SQL construction with parameterized queries
- **Batch Processing**: Support for single and bulk SQL operations
- **Flexible Configuration**: Configurable execution parameters (timeout, fetch size, etc.)
- **Result Parsing**: Flexible result set parsing mechanism

## Installation

### Maven

Add the following dependency to your pom.xml:

```xml
<dependency>
    <groupId>io.github.maxwellnie</groupId>
    <artifactId>free-jdbc</artifactId>
    <version>1.1</version>
</dependency>
```

## Quick Start

See example code.

### Configuration Options

```java
import io.github.maxwellnie.free.jdbc.statement.ConfigurationImpl;

Configuration config = new ConfigurationImpl()
    .setQueryTimeout(30)           // Query timeout
    .setFetchSize(100)             // Fetch size
    .setResultSetType(ResultSet.TYPE_FORWARD_ONLY)  // Result set type
    .setResultSetConcurrency(ResultSet.CONCUR_READ_ONLY); // Concurrency type

// Apply configuration in statement
stmt.setConfiguration(config);
```

## Core Components

### 1. SQL Builders

- [SingleSqlBuilder](https://github.com/maxwellnie/free-jdbc/blob/master/src/main/java/io/github/maxwellnie/free/jdbc/single/SingleSqlBuilder.java): Used to build single parameterized SQL statements
- [BatchSqlBuilder](https://github.com/maxwellnie/free-jdbc/blob/master/src/main/java/io/github/maxwellnie/free/jdbc/batch/BatchSqlBuilder.java): Used to build batch SQL statements

### 2. Statement Handlers

- [PreparedIntegratedStatement](https://github.com/maxwellnie/free-jdbc/blob/master/src/main/java/io/github/maxwellnie/free/jdbc/statement/PreparedIntegratedStatement.java): Handles prepared statements
- [SimpleIntegratedStatement](https://github.com/maxwellnie/free-jdbc/blob/master/src/main/java/io/github/maxwellnie/free/jdbc/statement/SimpleIntegratedStatement.java): Handles simple statements
- [CallableIntegrateStatement](https://github.com/maxwellnie/free-jdbc/blob/master/src/main/java/io/github/maxwellnie/free/jdbc/statement/CallableIntegrateStatement.java): Handles stored procedure calls

### 3. Configuration Management

- [Configuration](https://github.com/maxwellnie/free-/blob/master/src/main/java/io/github/maxwellnie/free/jdbc/statement/Configuration.java): Defines statement execution configuration
- [ConfigurationImpl](https://github.com/maxwellnie/free-jdbc/blob/master/src/main/java/io/github/maxwellnie/free/jdbc/statement/ConfigurationImpl.java): Configuration implementation class

### 4. Result Handling

- [ResultParser](https://github.com/maxwellnie/free-jdbc/blob/master/src/main/java/io/github/maxwellnie/free/jdbc/statement/ResultParser.java): Result set parsing interface
- [Executor](https://github.com/maxwellnie/free-jdbc/blob/master/src/main/java/io/github/maxwellnie/free/jdbc/statement/Executor.java): Executor interface

## Example Code

More detailed usage examples can be found in the [example](https://github.com/maxwellnie/free-jdbc/blob/master/example) directory:

- [CrudOperationsExample.java](https://github.com/maxwellnie/free-jdbc/blob/master/example/CrudOperationsExample.java): Basic CRUD operations example
- [BatchOperationExample.java](https://github.com/maxwellnie/free-jdbc/blob/master/example/BatchOperationExample.java): Batch operations example
- [StoredProcedureExample.java](https://github.com/maxwellnie/free-jdbc/blob/master/example/StoredProcedureExample.java): Stored procedure call example

The examples demonstrate:
- **Basic CRUD Operations**: How to perform create, read, update, and delete operations
- **Batch Operations**: How to efficiently execute multiple SQL statements at once
- **Stored Procedure Calls**: How to call database stored procedures with parameters
- **Configuration Options**: How to configure various execution parameters
- **Result Handling**: How to process query results effectively

## Design Philosophy

Free-JDBC's design philosophy is to provide a simple yet powerful JDBC abstraction layer that allows developers to focus on business logic rather than JDBC boilerplate code. The framework has the following characteristics:

1. **Type Safety**: Ensured through generics
2. **Exception Handling**: Unified exception handling mechanism
3. **Resource Management**: Automatic resource cleanup
4. **Flexibility**: Extensible design allowing custom behaviors

## Exception Handling

The framework provides several specific exception types:

- [SqlBuildException](https://github.com/maxwellnie/free-jdbc/blob/master/src/main/java/io/github/maxwellnie/free/jdbc/SqlBuildException.java): Thrown when building SQL
- [StatementException](https://github.com/maxwellnie/free-jdbc/blob/master/src/main/java/io/github/maxwellnie/free/jdbc/statement/StatementException.java): Thrown during statement processing
- [SqlExecutionException](https://github.com/maxwellnie/free-jdbc/blob/master/src/main/java/io/github/maxwellnie/free/jdbc/statement/SqlExecutionException.java): Thrown during SQL execution
- [ResultParserException](https://github.com/maxwellnie/free-jdbc/blob/master/src/main/java/io/github/maxwellnie/free/jdbc/statement/ResultParserException.java): Thrown during result parsing

## Contributing

Feel free to submit Issues and Pull Requests to improve this project.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Documentation

- [English Version](README.md)
- [中文版](README_CN.md)