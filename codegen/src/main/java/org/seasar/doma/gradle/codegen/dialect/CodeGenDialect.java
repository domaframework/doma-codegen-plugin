package org.seasar.doma.gradle.codegen.dialect;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import org.seasar.doma.gradle.codegen.meta.ColumnMeta;

public interface CodeGenDialect {

  String getName();

  String getDialectClassName();

  String getDefaultSchemaName(String userName);

  String getMappedPropertyClassName(ColumnMeta columnMeta);

  void replacePropertyClassName(String oldClassName, String newClassName);

  boolean supportsIdentity();

  boolean supportsSequence();

  boolean isAutoIncrement(
      Connection connection,
      String catalogName,
      String schemaName,
      String tableName,
      String columnName)
      throws SQLException;

  boolean isJdbcCommentUnavailable();

  String getTableComment(
      Connection connection, String catalogName, String schemaName, String tableName)
      throws SQLException;

  Map<String, String> getColumnCommentMap(
      Connection connection, String catalogName, String schemaName, String tableName)
      throws SQLException;

  String convertToTimeLiteral(String value);

  String convertToDateLiteral(String value);

  String convertToTimestampLiteral(String value);

  /**
   * Enclose the name with quotation marks.
   *
   * @param name the name of a database object such as a table, a column, and so on
   * @return the name that is enclosed with quotation marks
   */
  String applyQuote(String name);
}
