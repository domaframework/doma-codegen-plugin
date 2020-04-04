package org.seasar.doma.gradle.codegen.dialect;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.seasar.doma.gradle.codegen.desc.ClassConstants;
import org.seasar.doma.gradle.codegen.exception.CodeGenNullPointerException;
import org.seasar.doma.gradle.codegen.meta.ColumnMeta;
import org.seasar.doma.gradle.codegen.util.JdbcUtil;
import org.seasar.doma.gradle.codegen.util.TableUtil;

public class StandardCodeGenDialect implements CodeGenDialect {

  protected final Map<String, String> classNameMap = new HashMap<String, String>();

  protected final Map<Integer, String> fallbackClassNameMap = new HashMap<Integer, String>();

  public StandardCodeGenDialect() {
    classNameMap.put("bigint", Long.class.getName());
    classNameMap.put("binary", ClassConstants.bytes.getQualifiedName());
    classNameMap.put("bit", Boolean.class.getName());
    classNameMap.put("blob", Blob.class.getName());
    classNameMap.put("boolean", Boolean.class.getName());
    classNameMap.put("char", String.class.getName());
    classNameMap.put("clob", Clob.class.getName());
    classNameMap.put("date", LocalDate.class.getName());
    classNameMap.put("decimal", BigDecimal.class.getName());
    classNameMap.put("double", Double.class.getName());
    classNameMap.put("float", Float.class.getName());
    classNameMap.put("integer", Integer.class.getName());
    classNameMap.put("longnvarchar", String.class.getName());
    classNameMap.put("longvarbinary", ClassConstants.bytes.getQualifiedName());
    classNameMap.put("longvarchar", String.class.getName());
    classNameMap.put("nclob", NClob.class.getName());
    classNameMap.put("nchar", String.class.getName());
    classNameMap.put("numeric", BigDecimal.class.getName());
    classNameMap.put("nvarchar", String.class.getName());
    classNameMap.put("real", Float.class.getName());
    classNameMap.put("smallint", Short.class.getName());
    classNameMap.put("time", LocalTime.class.getName());
    classNameMap.put("timestamp", LocalDateTime.class.getName());
    classNameMap.put("tinyint", Short.class.getName());
    classNameMap.put("varbinary", ClassConstants.bytes.getQualifiedName());
    classNameMap.put("varchar", String.class.getName());
    classNameMap.put("xml", SQLXML.class.getName());

    fallbackClassNameMap.put(Types.BIGINT, Long.class.getName());
    fallbackClassNameMap.put(Types.BINARY, ClassConstants.bytes.getQualifiedName());
    fallbackClassNameMap.put(Types.BIT, Boolean.class.getName());
    fallbackClassNameMap.put(Types.BLOB, Blob.class.getName());
    fallbackClassNameMap.put(Types.BOOLEAN, Boolean.class.getName());
    fallbackClassNameMap.put(Types.CHAR, String.class.getName());
    fallbackClassNameMap.put(Types.CLOB, Clob.class.getName());
    fallbackClassNameMap.put(Types.DATE, LocalDate.class.getName());
    fallbackClassNameMap.put(Types.DECIMAL, BigDecimal.class.getName());
    fallbackClassNameMap.put(Types.DOUBLE, Double.class.getName());
    fallbackClassNameMap.put(Types.FLOAT, Float.class.getName());
    fallbackClassNameMap.put(Types.INTEGER, Integer.class.getName());
    fallbackClassNameMap.put(Types.LONGNVARCHAR, String.class.getName());
    fallbackClassNameMap.put(Types.LONGVARBINARY, ClassConstants.bytes.getQualifiedName());
    fallbackClassNameMap.put(Types.LONGVARCHAR, String.class.getName());
    fallbackClassNameMap.put(Types.NCHAR, String.class.getName());
    fallbackClassNameMap.put(Types.NCLOB, NClob.class.getName());
    fallbackClassNameMap.put(Types.NUMERIC, BigDecimal.class.getName());
    fallbackClassNameMap.put(Types.REAL, Float.class.getName());
    fallbackClassNameMap.put(Types.SMALLINT, Short.class.getName());
    fallbackClassNameMap.put(Types.SQLXML, SQLXML.class.getName());
    fallbackClassNameMap.put(Types.TIME, LocalTime.class.getName());
    fallbackClassNameMap.put(Types.TIMESTAMP, LocalDateTime.class.getName());
    fallbackClassNameMap.put(Types.TINYINT, Short.class.getName());
    fallbackClassNameMap.put(Types.VARBINARY, ClassConstants.bytes.getQualifiedName());
    fallbackClassNameMap.put(Types.VARCHAR, String.class.getName());
    fallbackClassNameMap.put(Types.NVARCHAR, String.class.getName());
  }

  @Override
  public String getName() {
    return "standard";
  }

  @Override
  public String getDialectClassName() {
    return ClassConstants.StandardDialect.getQualifiedName();
  }

  @Override
  public boolean isJdbcCommentUnavailable() {
    return false;
  }

  @Override
  public String getDefaultSchemaName(String userName) {
    return userName;
  }

  @Override
  public boolean isAutoIncrement(
      Connection connection,
      String catalogName,
      String schemaName,
      String tableName,
      String columnName)
      throws SQLException {
    if (connection == null) {
      throw new CodeGenNullPointerException("connection");
    }
    if (tableName == null) {
      throw new CodeGenNullPointerException("tableName");
    }
    if (columnName == null) {
      throw new CodeGenNullPointerException("columnName");
    }
    String fullTableName = TableUtil.getQualifiedTableName(catalogName, schemaName, tableName);
    String sql = "select " + columnName + " from " + fullTableName + " where 1 = 0";
    PreparedStatement preparedStatement = connection.prepareStatement(sql);
    try {
      ResultSet resultSet = preparedStatement.executeQuery();
      try {
        ResultSetMetaData rsMetaData = resultSet.getMetaData();
        return rsMetaData.isAutoIncrement(1);
      } finally {
        JdbcUtil.close(resultSet);
      }
    } finally {
      JdbcUtil.close(preparedStatement);
    }
  }

  @Override
  public String getTableComment(
      Connection connection, String catalogName, String schemaName, String tableName)
      throws SQLException {
    return "";
  }

  @Override
  public Map<String, String> getColumnCommentMap(
      Connection connection, String catalogName, String schemaName, String tableName)
      throws SQLException {
    return Collections.emptyMap();
  }

  @Override
  public boolean supportsIdentity() {
    return false;
  }

  @Override
  public boolean supportsSequence() {
    return false;
  }

  @Override
  public String getMappedPropertyClassName(ColumnMeta columnMeta) {
    String mappedClassName = classNameMap.get(columnMeta.getTypeName());
    if (mappedClassName != null) {
      return mappedClassName;
    }
    mappedClassName = fallbackClassNameMap.get(columnMeta.getSqlType());
    if (mappedClassName != null) {
      return mappedClassName;
    }
    return null;
  }

  @Override
  public void replacePropertyClassName(String oldClassName, String newClassName) {
    if (oldClassName == null) {
      throw new CodeGenNullPointerException("oldClassName");
    }
    if (newClassName == null) {
      throw new CodeGenNullPointerException("newClassName");
    }
    for (Map.Entry<String, String> entry : classNameMap.entrySet()) {
      if (oldClassName.equals(entry.getValue())) {
        entry.setValue(newClassName);
      }
    }
    for (Map.Entry<Integer, String> entry : fallbackClassNameMap.entrySet()) {
      if (oldClassName.equals(entry.getValue())) {
        entry.setValue(newClassName);
      }
    }
  }

  @Override
  public String convertToTimeLiteral(String value) {
    if (value == null) {
      throw new CodeGenNullPointerException("value");
    }
    return "'" + value + "'";
  }

  @Override
  public String convertToDateLiteral(String value) {
    if (value == null) {
      throw new CodeGenNullPointerException("value");
    }
    return "'" + value + "'";
  }

  @Override
  public String convertToTimestampLiteral(String value) {
    if (value == null) {
      throw new CodeGenNullPointerException("value");
    }
    return "'" + value + "'";
  }
}
