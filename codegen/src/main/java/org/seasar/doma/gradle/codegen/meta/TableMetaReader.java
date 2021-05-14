package org.seasar.doma.gradle.codegen.meta;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import javax.sql.DataSource;
import org.seasar.doma.gradle.codegen.dialect.CodeGenDialect;
import org.seasar.doma.gradle.codegen.exception.CodeGenException;
import org.seasar.doma.gradle.codegen.exception.CodeGenNullPointerException;
import org.seasar.doma.gradle.codegen.message.Message;
import org.seasar.doma.gradle.codegen.util.JdbcUtil;

public class TableMetaReader {

  protected final CodeGenDialect dialect;

  protected final DataSource dataSource;

  protected final String catalogName;

  protected final String schemaName;

  protected final Pattern tableNamePattern;

  protected final Pattern ignoredTableNamePattern;

  protected final List<String> tableTypes;

  public TableMetaReader(
      CodeGenDialect dialect,
      DataSource dataSource,
      String catalogName,
      String schemaName,
      String tableNamePattern,
      String ignoredTableNamePattern,
      List<String> tableTypes) {
    if (dialect == null) {
      throw new CodeGenNullPointerException("dialect");
    }
    if (dataSource == null) {
      throw new CodeGenNullPointerException("dataSource");
    }
    if (tableNamePattern == null) {
      throw new CodeGenNullPointerException("tableNamePattern");
    }
    if (ignoredTableNamePattern == null) {
      throw new CodeGenNullPointerException("ignoreTableNamePattern");
    }
    if (tableTypes == null) {
      throw new CodeGenNullPointerException("tableTypes");
    }
    this.dialect = dialect;
    this.dataSource = dataSource;
    this.catalogName = catalogName;
    this.schemaName = schemaName;
    this.tableNamePattern = Pattern.compile(tableNamePattern, Pattern.CASE_INSENSITIVE);
    this.ignoredTableNamePattern =
        Pattern.compile(ignoredTableNamePattern, Pattern.CASE_INSENSITIVE);
    this.tableTypes = tableTypes;
  }

  public List<TableMeta> read() {
    Connection con = JdbcUtil.getConnection(dataSource);
    try {
      DatabaseMetaData metaData = con.getMetaData();
      List<TableMeta> tableMetas =
          getTableMetas(
              metaData,
              catalogName,
              schemaName != null ? schemaName : getDefaultSchemaName(metaData));
      for (TableMeta tableMeta : tableMetas) {
        Set<String> primaryKeySet = getPrimaryKeys(metaData, tableMeta);
        handleColumnMeta(metaData, tableMeta, primaryKeySet);
      }
      if (dialect.isJdbcCommentUnavailable()) {
        readCommentFromDictinary(con, tableMetas);
      }
      return tableMetas;
    } catch (SQLException e) {
      throw new CodeGenException(Message.DOMAGEN9001, e, e);
    } finally {
      JdbcUtil.close(con);
    }
  }

  protected void handleColumnMeta(
      DatabaseMetaData metaData, TableMeta tableMeta, Set<String> primaryKeySet)
      throws SQLException {
    for (ColumnMeta columnMeta : getDbColumnMetas(metaData, tableMeta)) {
      if (primaryKeySet.contains(columnMeta.getName())) {
        columnMeta.setPrimaryKey(true);
        if (primaryKeySet.size() == 1) {
          columnMeta.setAutoIncrement(isAutoIncrement(metaData, tableMeta, columnMeta.getName()));
        }
      }
      tableMeta.addColumnMeta(columnMeta);
    }
  }

  protected String getDefaultSchemaName(DatabaseMetaData metaData) throws SQLException {
    String userName = metaData.getUserName();
    return dialect.getDefaultSchemaName(userName);
  }

  protected List<TableMeta> getTableMetas(
      DatabaseMetaData metaData, String catalogName, String schemaName) throws SQLException {
    List<TableMeta> results = new ArrayList<TableMeta>();
    ResultSet rs =
        metaData.getTables(
            catalogName,
            schemaName,
            null,
            this.tableTypes.toArray(new String[this.tableTypes.size()]));
    try {
      while (rs.next()) {
        TableMeta tableMeta = new TableMeta();
        tableMeta.setCatalogName(rs.getString("TABLE_CAT"));
        tableMeta.setSchemaName(rs.getString("TABLE_SCHEM"));
        tableMeta.setName(rs.getString("TABLE_NAME"));
        tableMeta.setComment(rs.getString("REMARKS"));
        if (isTargetTable(tableMeta)) {
          results.add(tableMeta);
        }
      }
      return results;
    } finally {
      JdbcUtil.close(rs);
    }
  }

  protected boolean isTargetTable(TableMeta dbTableMeta) {
    String name = dbTableMeta.getName();
    if (!tableNamePattern.matcher(name).matches()) {
      return false;
    }
    if (ignoredTableNamePattern.matcher(name).matches()) {
      return false;
    }
    return true;
  }

  protected List<ColumnMeta> getDbColumnMetas(DatabaseMetaData metaData, TableMeta tableMeta)
      throws SQLException {
    List<ColumnMeta> results = new ArrayList<ColumnMeta>();
    ResultSet rs =
        metaData.getColumns(
            tableMeta.getCatalogName(), tableMeta.getSchemaName(), tableMeta.getName(), null);
    try {
      while (rs.next()) {
        ColumnMeta columnMeta = new ColumnMeta();
        columnMeta.setName(rs.getString("COLUMN_NAME"));
        columnMeta.setSqlType(rs.getInt("DATA_TYPE"));
        columnMeta.setTypeName(rs.getString("TYPE_NAME").toLowerCase());
        columnMeta.setLength(rs.getInt("COLUMN_SIZE"));
        columnMeta.setScale(rs.getInt("DECIMAL_DIGITS"));
        columnMeta.setNullable(rs.getBoolean("NULLABLE"));
        columnMeta.setDefaultValue(rs.getString("COLUMN_DEF"));
        columnMeta.setComment(rs.getString("REMARKS"));
        results.add(columnMeta);
      }
      return results;
    } finally {
      JdbcUtil.close(rs);
    }
  }

  protected Set<String> getPrimaryKeys(DatabaseMetaData metaData, TableMeta tableMeta)
      throws SQLException {
    Set<String> results = new HashSet<String>();
    ResultSet rs =
        metaData.getPrimaryKeys(
            tableMeta.getCatalogName(), tableMeta.getSchemaName(), tableMeta.getName());
    try {
      while (rs.next()) {
        results.add(rs.getString("COLUMN_NAME"));
      }
    } finally {
      JdbcUtil.close(rs);
    }
    return results;
  }

  protected boolean isAutoIncrement(
      DatabaseMetaData metaData, TableMeta tableMeta, String columnName) throws SQLException {
    return dialect.isAutoIncrement(
        metaData.getConnection(),
        tableMeta.getCatalogName(),
        tableMeta.getSchemaName(),
        tableMeta.getName(),
        columnName);
  }

  protected void readCommentFromDictinary(Connection connection, List<TableMeta> dbTableMetaList)
      throws SQLException {
    for (TableMeta tableMeta : dbTableMetaList) {
      String tableComment =
          dialect.getTableComment(
              connection,
              tableMeta.getCatalogName(),
              tableMeta.getSchemaName(),
              tableMeta.getName());
      tableMeta.setComment(tableComment);
      Map<String, String> columnCommentMap =
          dialect.getColumnCommentMap(
              connection,
              tableMeta.getCatalogName(),
              tableMeta.getSchemaName(),
              tableMeta.getName());
      for (ColumnMeta columnMeta : tableMeta.getColumnMetas()) {
        String columnName = columnMeta.getName();
        if (columnCommentMap.containsKey(columnName)) {
          String columnComment = columnCommentMap.get(columnName);
          columnMeta.setComment(columnComment);
        }
      }
    }
  }
}
