package org.seasar.doma.gradle.codegen.meta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.seasar.doma.gradle.codegen.util.TableUtil;

public class TableMeta {

  protected String catalogName;

  protected String schemaName;

  protected String name;

  protected String comment;

  protected final List<ColumnMeta> columnMetas = new ArrayList<ColumnMeta>();

  protected final List<ColumnMeta> primaryKeyColumnMetas = new ArrayList<ColumnMeta>();

  public String getCatalogName() {
    return catalogName;
  }

  public void setCatalogName(String catalogName) {
    this.catalogName = catalogName;
  }

  public String getSchemaName() {
    return schemaName;
  }

  public void setSchemaName(String schemaName) {
    this.schemaName = schemaName;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public List<ColumnMeta> getColumnMetas() {
    return Collections.unmodifiableList(columnMetas);
  }

  public void addColumnMeta(ColumnMeta columnMeta) {
    columnMetas.add(columnMeta);
    columnMeta.setTableMeta(this);
    if (columnMeta.isPrimaryKey()) {
      primaryKeyColumnMetas.add(columnMeta);
    }
  }

  public List<ColumnMeta> getPrimaryKeyColumnMetas() {
    return Collections.unmodifiableList(primaryKeyColumnMetas);
  }

  public String getQualifiedTableName() {
    return TableUtil.getQualifiedTableName(catalogName, schemaName, name);
  }

  public boolean hasCompositePrimaryKey() {
    return primaryKeyColumnMetas.size() > 1;
  }
}
