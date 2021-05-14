package org.seasar.doma.gradle.codegen.task;

import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.gradle.api.DefaultTask;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.TaskAction;
import org.seasar.doma.gradle.codegen.GlobalFactory;
import org.seasar.doma.gradle.codegen.dialect.CodeGenDialect;
import org.seasar.doma.gradle.codegen.exception.CodeGenException;
import org.seasar.doma.gradle.codegen.message.Message;
import org.seasar.doma.gradle.codegen.meta.TableMeta;
import org.seasar.doma.gradle.codegen.meta.TableMetaReader;

public class CodeGenDbMetaTask extends DefaultTask {

  private final List<TableMeta> tableMetaList = new ArrayList<>();

  private final Property<GlobalFactory> globalFactory =
      getProject().getObjects().property(GlobalFactory.class);

  private final Property<DataSource> dataSource =
      getProject().getObjects().property(DataSource.class);

  private final Property<CodeGenDialect> dialect =
      getProject().getObjects().property(CodeGenDialect.class);

  private final Property<String> catalogName = getProject().getObjects().property(String.class);

  private final Property<String> schemaName = getProject().getObjects().property(String.class);

  private final Property<String> tableNamePattern =
      getProject().getObjects().property(String.class);

  private final Property<String> ignoredTableNamePattern =
      getProject().getObjects().property(String.class);

  private final ListProperty<String> tableTypes =
      getProject().getObjects().listProperty(String.class);

  @Internal
  public Provider<List<TableMeta>> getTableMetaList() {
    return getProject().provider(() -> tableMetaList);
  }

  @Internal
  public Property<GlobalFactory> getGlobalFactory() {
    return globalFactory;
  }

  @Internal
  public Property<DataSource> getDataSource() {
    return dataSource;
  }

  @Internal
  public Property<CodeGenDialect> getDialect() {
    return dialect;
  }

  @Internal
  public Property<String> getCatalogName() {
    return catalogName;
  }

  @Internal
  public Property<String> getSchemaName() {
    return schemaName;
  }

  @Internal
  public Property<String> getTableNamePattern() {
    return tableNamePattern;
  }

  @Internal
  public Property<String> getIgnoredTableNamePattern() {
    return ignoredTableNamePattern;
  }

  @Internal
  public ListProperty<String> getTableTypes() {
    return tableTypes;
  }

  @TaskAction
  public void read() {
    TableMetaReader tableMetaReader = createTableMetaReader();
    tableMetaList.addAll(tableMetaReader.read());
    if (tableMetaList.isEmpty()) {
      throw new CodeGenException(Message.DOMAGEN0005);
    }
  }

  private TableMetaReader createTableMetaReader() {
    return globalFactory
        .get()
        .createTableMetaReader(
            dialect.get(),
            dataSource.get(),
            catalogName.getOrNull(),
            schemaName.getOrNull(),
            tableNamePattern.get(),
            ignoredTableNamePattern.get(),
            tableTypes.get());
  }
}
