package org.seasar.doma.gradle.codegen.dialect;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.seasar.doma.gradle.codegen.desc.ClassConstants;
import org.seasar.doma.gradle.codegen.exception.CodeGenNullPointerException;
import org.seasar.doma.gradle.codegen.util.JdbcUtil;

public class Db2CodeGenDialect extends StandardCodeGenDialect {

  public Db2CodeGenDialect() {
    classNameMap.put("char () for bit data", ClassConstants.bytes.getQualifiedName());
    classNameMap.put("decimal", BigDecimal.class.getName());
    classNameMap.put("long varchar for bit data", ClassConstants.bytes.getQualifiedName());
    classNameMap.put("long varchar", String.class.getName());
    classNameMap.put("varchar () for bit data", ClassConstants.bytes.getQualifiedName());
  }

  @Override
  public String getName() {
    return "db2";
  }

  @Override
  public String getDialectClassName() {
    return ClassConstants.Db2Dialect.getQualifiedName();
  }

  @Override
  public String getDefaultSchemaName(String userName) {
    return userName != null ? userName.toUpperCase() : null;
  }

  @Override
  public boolean supportsIdentity() {
    return true;
  }

  @Override
  public boolean supportsSequence() {
    return true;
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
    String sql =
        "select generated from syscat.columns where tabschema = ? and tabname = ? and colname = ?";
    PreparedStatement ps = JdbcUtil.prepareStatement(connection, sql);
    ps.setString(1, schemaName);
    ps.setString(2, tableName);
    ps.setString(3, columnName);
    try {
      ResultSet rs = ps.executeQuery();
      try {
        if (rs.next()) {
          String generated = rs.getString(1);
          return "A".equals(generated) || "D".equals(generated);
        }
        return false;
      } finally {
        JdbcUtil.close(rs);
      }
    } finally {
      JdbcUtil.close(ps);
    }
  }
}
