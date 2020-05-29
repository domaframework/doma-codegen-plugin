package org.seasar.doma.gradle.codegen.task;

import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.gradle.api.DefaultTask;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.Nested;
import org.gradle.api.tasks.TaskAction;
import org.seasar.doma.gradle.codegen.GlobalFactory;
import org.seasar.doma.gradle.codegen.desc.EntityDesc;
import org.seasar.doma.gradle.codegen.desc.EntityDescFactory;
import org.seasar.doma.gradle.codegen.desc.EntityPropertyClassNameResolver;
import org.seasar.doma.gradle.codegen.desc.EntityPropertyDescFactory;
import org.seasar.doma.gradle.codegen.desc.NamingType;
import org.seasar.doma.gradle.codegen.dialect.CodeGenDialect;
import org.seasar.doma.gradle.codegen.extension.EntityConfig;
import org.seasar.doma.gradle.codegen.meta.TableMeta;
import org.seasar.doma.gradle.codegen.util.ClassUtil;

public class CodeGenEntityDescTask extends DefaultTask {

  private final List<EntityDesc> entityDescList = new ArrayList<>();

  private final ListProperty<TableMeta> tableMetaList =
      getProject().getObjects().listProperty(TableMeta.class);

  private final Property<GlobalFactory> globalFactory =
      getProject().getObjects().property(GlobalFactory.class);

  private final Property<DataSource> dataSource =
      getProject().getObjects().property(DataSource.class);

  private final Property<CodeGenDialect> dialect =
      getProject().getObjects().property(CodeGenDialect.class);

  private final Property<String> schemaName = getProject().getObjects().property(String.class);

  private final Property<String> tableNamePattern =
      getProject().getObjects().property(String.class);

  private final Property<String> ignoredTableNamePattern =
      getProject().getObjects().property(String.class);

  private final ListProperty<String> tableTypes =
      getProject().getObjects().listProperty(String.class);

  private final Property<String> versionColumnNamePattern =
      getProject().getObjects().property(String.class);

  private EntityConfig entityConfig;

  @Internal
  public Provider<List<EntityDesc>> getEntityDescList() {
    return getProject().provider(() -> entityDescList);
  }

  @Internal
  public ListProperty<TableMeta> getTableMetaList() {
    return tableMetaList;
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

  @Internal
  public Property<String> getVersionColumnNamePattern() {
    return versionColumnNamePattern;
  }

  @Nested
  public EntityConfig getEntityConfig() {
    return entityConfig;
  }

  public void setEntityConfig(EntityConfig entityConfig) {
    this.entityConfig = entityConfig;
  }

  @TaskAction
  public void create() {
    EntityPropertyClassNameResolver entityPropertyClassNameResolver =
        createEntityPropertyClassNameResolver();
    EntityPropertyDescFactory entityPropertyDescFactory =
        createEntityPropertyDescFactory(entityPropertyClassNameResolver);
    EntityDescFactory entityDescFactory = createEntityDescFactory(entityPropertyDescFactory);

    tableMetaList.get().stream()
        .map(
            tableMeta ->
                entityDescFactory.createEntityDesc(
                    tableMeta,
                    entityConfig.getPrefix().getOrNull(),
                    entityConfig.getSuffix().getOrNull()))
        .forEach(entityDescList::add);
  }

  private EntityPropertyClassNameResolver createEntityPropertyClassNameResolver() {
    return globalFactory
        .get()
        .createEntityPropertyClassNameResolver(
            entityConfig.getEntityPropertyClassNamesFile().getAsFile().getOrNull());
  }

  private EntityPropertyDescFactory createEntityPropertyDescFactory(
      EntityPropertyClassNameResolver entityPropertyClassNameResolver) {
    return globalFactory
        .get()
        .createEntityPropertyDescFactory(
            dialect.get(),
            entityPropertyClassNameResolver,
            versionColumnNamePattern.get(),
            entityConfig.getGenerationType().getOrNull(),
            entityConfig.getInitialValue().getOrNull(),
            entityConfig.getAllocationSize().getOrNull(),
            entityConfig.getShowColumnName().get());
  }

  private EntityDescFactory createEntityDescFactory(
      EntityPropertyDescFactory entityPropertyDescFactory) {
    Class<?> superclass = null;
    if (entityConfig.getSuperclassName().isPresent()) {
      superclass = ClassUtil.forName(entityConfig.getSuperclassName().get(), "superclassName");
    }
    return globalFactory
        .get()
        .createEntityDescFactory(
            entityConfig.getPackageName().get(),
            superclass,
            entityPropertyDescFactory,
            entityConfig.getNamingType().isPresent()
                ? entityConfig.getNamingType().get()
                : NamingType.NONE,
            entityConfig.getOriginalStatesPropertyName().getOrNull(),
            entityConfig.getShowCatalogName().get(),
            entityConfig.getShowSchemaName().get(),
            entityConfig.getShowTableName().get(),
            entityConfig.getShowDbComment().get(),
            entityConfig.getUseAccessor().get(),
            entityConfig.getUseListener().get(),
            entityConfig.getUseMetamodel().get());
  }
}
