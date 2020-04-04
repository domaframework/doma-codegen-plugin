package org.seasar.doma.gradle.codegen.dialect;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.seasar.doma.gradle.codegen.desc.ClassConstants;
import org.seasar.doma.gradle.codegen.exception.CodeGenNullPointerException;
import org.seasar.doma.gradle.codegen.meta.ColumnMeta;
import org.seasar.doma.gradle.codegen.util.JdbcUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Oracle11CodeGenDialect extends StandardCodeGenDialect {

  Logger logger = LoggerFactory.getLogger(Oracle11CodeGenDialect.class);

  public Oracle11CodeGenDialect() {
    classNameMap.put("binary_double", Double.class.getName());
    classNameMap.put("binary_float", Float.class.getName());
    classNameMap.put("long", String.class.getName());
    classNameMap.put("long raw", ClassConstants.bytes.getQualifiedName());
    classNameMap.put("nvarchar2", String.class.getName());
    classNameMap.put("raw", ClassConstants.bytes.getQualifiedName());
    classNameMap.put("varchar2", String.class.getName());
  }

  @Override
  public String getName() {
    return "oracle11";
  }

  @Override
  public String getDialectClassName() {
    return ClassConstants.Oracle11Dialect.getQualifiedName();
  }

  @Override
  public boolean isJdbcCommentUnavailable() {
    return true;
  }

  @Override
  public String getTableComment(
      Connection connection, String catalogName, String schemaName, String tableName)
      throws SQLException {
    if (connection == null) {
      throw new CodeGenNullPointerException("connection");
    }
    if (schemaName == null) {
      throw new CodeGenNullPointerException("schemaName");
    }
    if (tableName == null) {
      throw new CodeGenNullPointerException("tableName");
    }
    String sql =
        "select comments from all_tab_comments where owner = ? and table_name = ? and table_type in ('TABLE', 'VIEW')";
    logger.info(String.format(sql.replace("?", "'%s'"), schemaName, tableName));
    PreparedStatement preparedStatement = connection.prepareStatement(sql);
    try {
      preparedStatement.setString(1, schemaName);
      preparedStatement.setString(2, tableName);
      ResultSet resultSet = preparedStatement.executeQuery();
      try {
        if (resultSet.next()) {
          return resultSet.getString(1);
        }
        return null;
      } finally {
        JdbcUtil.close(resultSet);
      }
    } finally {
      JdbcUtil.close(preparedStatement);
    }
  }

  @Override
  public Map<String, String> getColumnCommentMap(
      Connection connection, String catalogName, String schemaName, String tableName)
      throws SQLException {
    if (connection == null) {
      throw new CodeGenNullPointerException("connection");
    }
    if (schemaName == null) {
      throw new CodeGenNullPointerException("schemaName");
    }
    if (tableName == null) {
      throw new CodeGenNullPointerException("tableName");
    }

    String sql =
        "select column_name, comments from all_col_comments where owner = ? and table_name = ?";
    logger.info(String.format(sql.replace("?", "'%s'"), schemaName, tableName));
    PreparedStatement preparedStatement = connection.prepareStatement(sql);
    try {
      preparedStatement.setString(1, schemaName);
      preparedStatement.setString(2, tableName);
      ResultSet resultSet = preparedStatement.executeQuery();
      try {
        Map<String, String> commentMap = new HashMap<String, String>();
        while (resultSet.next()) {
          commentMap.put(resultSet.getString(1), resultSet.getString(2));
        }
        return commentMap;
      } finally {
        JdbcUtil.close(resultSet);
      }
    } finally {
      JdbcUtil.close(preparedStatement);
    }
  }

  @Override
  public String getMappedPropertyClassName(ColumnMeta columnMeta) {
    if ("number".equals(columnMeta.getTypeName())) {
      if (columnMeta.getScale() != 0) {
        return BigDecimal.class.getName();
      }
      if (columnMeta.getLength() < 5) {
        return Short.class.getName();
      }
      if (columnMeta.getLength() < 10) {
        return Integer.class.getName();
      }
      if (columnMeta.getLength() < 19) {
        return Long.class.getName();
      }
      return BigInteger.class.getName();
    }
    return super.getMappedPropertyClassName(columnMeta);
  }

  @Override
  public boolean supportsSequence() {
    return true;
  }

  @Override
  public String convertToTimeLiteral(String value) {
    return "time" + super.convertToTimeLiteral(value);
  }

  @Override
  public String convertToDateLiteral(String value) {
    return "date" + super.convertToDateLiteral(value);
  }

  @Override
  public String convertToTimestampLiteral(String value) {
    return "timestamp" + super.convertToTimestampLiteral(value);
  }
}
