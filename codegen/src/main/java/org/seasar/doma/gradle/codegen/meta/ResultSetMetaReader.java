package org.seasar.doma.gradle.codegen.meta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.seasar.doma.gradle.codegen.dialect.CodeGenDialect;
import org.seasar.doma.gradle.codegen.exception.CodeGenException;
import org.seasar.doma.gradle.codegen.exception.CodeGenNullPointerException;
import org.seasar.doma.gradle.codegen.message.Message;
import org.seasar.doma.gradle.codegen.util.JdbcUtil;

public class ResultSetMetaReader {

  protected final CodeGenDialect dialect;

  protected final DataSource dataSource;

  public ResultSetMetaReader(CodeGenDialect dialect, DataSource dataSource) {
    if (dialect == null) {
      throw new CodeGenNullPointerException("dialect");
    }
    if (dataSource == null) {
      throw new CodeGenNullPointerException("dataSource");
    }
    this.dialect = dialect;
    this.dataSource = dataSource;
  }

  public TableMeta read(String sql) {
    if (sql == null) {
      throw new CodeGenNullPointerException("sql");
    }
    Connection connection = JdbcUtil.getConnection(dataSource);
    try {
      PreparedStatement preparedStatement = JdbcUtil.prepareStatement(connection, sql);
      try {
        ResultSet resultSet = preparedStatement.executeQuery();
        try {
          return createTableMeta(resultSet.getMetaData());
        } finally {
          JdbcUtil.close(resultSet);
        }
      } finally {
        JdbcUtil.close(preparedStatement);
      }
    } catch (SQLException e) {
      throw new CodeGenException(Message.DOMAGEN9001, e, e);
    } finally {
      JdbcUtil.close(connection);
    }
  }

  protected TableMeta createTableMeta(ResultSetMetaData rsmd) throws SQLException {
    TableMeta tableMeta = new TableMeta();
    for (int i = 1, max = rsmd.getColumnCount() + 1; i < max; i++) {
      ColumnMeta columnMeta = new ColumnMeta();
      columnMeta.setName(rsmd.getColumnLabel(i));
      columnMeta.setSqlType(rsmd.getColumnType(i));
      columnMeta.setTypeName(rsmd.getTableName(i).toLowerCase());
      columnMeta.setLength(rsmd.getPrecision(i));
      columnMeta.setScale(rsmd.getScale(i));
      tableMeta.addColumnMeta(columnMeta);
    }
    return tableMeta;
  }
}
