# Free-JDBC

Free-JDBC 是一个轻量级的 Java JDBC 框架，旨在通过流畅的 API 简化数据库操作。它提供了 SQL 构建、批处理、语句处理和结果解析等功能，具有最小的开销。

## 特性

- **轻量级设计**：简洁的架构，无额外依赖
- **流式 API**：支持链式调用，代码更优雅
- **SQL 构建器**：动态构建 SQL 语句，支持参数化查询
- **批量处理**：支持单个和批量 SQL 操作
- **灵活配置**：可配置的执行参数（超时时间、获取大小等）
- **结果解析**：灵活的结果集解析机制

## 安装

### Maven

在您的 pom.xml 文件中添加以下依赖：

```xml
<dependency>
    <groupId>io.github.maxwellnie</groupId>
    <artifactId>free-jdbc</artifactId>
    <version>1.2</version>
</dependency>
```

## 快速开始

### 配置选项

```java
import io.github.maxwellnie.free.jdbc.statement.ConfigurationImpl;

Configuration config = new ConfigurationImpl()
    .setQueryTimeout(30)           // 查询超时时间
    .setFetchSize(100)             // 获取大小
    .setResultSetType(ResultSet.TYPE_FORWARD_ONLY)  // 结果集类型
    .setResultSetConcurrency(ResultSet.CONCUR_READ_ONLY); // 并发类型

// 在语句中应用配置
stmt.setConfiguration(config);
```

## 核心组件

### 1. SQL 构建器

- [SingleSqlBuilder](https://github.com/maxwellnie/free-jdbc/blob/master/src/main/java/io/github/maxwellnie/free/jdbc/single/SingleSqlBuilder.java)：用于构建单条带参数的 SQL 语句
- [BatchSqlBuilder](https://github.com/maxwellnie/free-jdbc/blob/master/src/main/java/io/github/maxwellnie/free/jdbc/batch/BatchSqlBuilder.java)：用于构建批量 SQL 语句

### 2. 语句处理器

- [PreparedIntegratedStatement](https://github.com/maxwellnie/free-jdbc/blob/master/src/main/java/io/github/maxwellnie/free/jdbc/statement/PreparedIntegratedStatement.java)：预编译语句处理
- [SimpleIntegratedStatement](https://github.com/maxwellnie/free-jdbc/blob/master/src/main/java/io/github/maxwellnie/free/jdbc/statement/SimpleIntegratedStatement.java)：简单语句处理
- [CallableIntegrateStatement](https://github.com/maxwellnie/free-jdbc/blob/master/src/main/java/io/github/maxwellnie/free/jdbc/statement/CallableIntegrateStatement.java)：存储过程调用

### 3. 配置管理

- [Configuration](https://github.com/maxwellnie/free-jdbc/blob/master/src/main/java/io/github/maxwellnie/free/jdbc/statement/Configuration.java)：定义语句执行配置
- [ConfigurationImpl](https://github.com/maxwellnie/free-jdbc/blob/master/src/main/java/io/github/maxwellnie/free/jdbc/statement/ConfigurationImpl.java)：配置实现类

### 4. 结果处理

- [ResultParser](https://github.com/maxwellnie/free-jdbc/blob/master/src/main/java/io/github/maxwellnie/free/jdbc/statement/ResultParser.java)：结果集解析接口
- [Executor](https://github.com/maxwellnie/free-jdbc/blob/master/src/main/java/io/github/maxwellnie/free/jdbc/statement/Executor.java)：执行器接口

## 示例代码

更多详细的使用示例可以在 [example](https://github.com/maxwellnie/free-jdbc/blob/master/example) 目录中找到：

- [CrudOperationsExample.java](https://github.com/maxwellnie/free-jdbc/blob/master/example/CrudOperationsExample.java)：基本CRUD操作示例
- [BatchOperationExample.java](https://github.com/maxwellnie/free-jdbc/blob/master/example/BatchOperationExample.java)：批量操作示例
- [StoredProcedureExample.java](https://github.com/maxwellnie/free-jdbc/blob/master/example/StoredProcedureExample.java)：存储过程调用示例

示例展示了：
- **基本 CRUD 操作**：如何执行创建、读取、更新和删除操作
- **批量操作**：如何一次高效地执行多个 SQL 语句
- **存储过程调用**：如何使用参数调用数据库存储过程
- **配置选项**：如何配置各种执行参数
- **结果处理**：如何有效地处理查询结果

## 设计理念

Free-JDBC 的设计理念是提供一个简单而强大的 JDBC 抽象层，让开发者能够专注于业务逻辑而不是 JDBC 的样板代码。框架具有以下特点：

1. **类型安全**：通过泛型确保类型安全
2. **异常处理**：统一的异常处理机制
3. **资源管理**：自动资源清理
4. **灵活性**：可扩展的设计允许自定义行为

## 异常处理

框架提供了一系列特定的异常类型：

- [SqlBuildException](https://github.com/maxwellnie/free-jdbc/blob/master/src/main/java/io/github/maxwellnie/free/jdbc/SqlBuildException.java)：SQL 构建时发生错误
- [StatementException](https://github.com/maxwellnie/free-jdbc/blob/master/src/main/java/io/github/maxwellnie/free/jdbc/statement/StatementException.java)：语句处理时发生错误
- [SqlExecutionException](https://github.com/maxwellnie/free-jdbc/blob/master/src/main/java/io/github/maxwellnie/free/jdbc/statement/SqlExecutionException.java)：SQL 执行时发生错误
- [ResultParserException](https://github.com/maxwellnie/free-jdbc/blob/master/src/main/java/io/github/maxwellnie/free/jdbc/statement/ResultParserException.java)：结果解析时发生错误

## 贡献

欢迎提交 Issue 和 Pull Request 来改进此项目。

## 许可证

此项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 文档

- [English Version](README.md)
- [中文版](README_CN.md)