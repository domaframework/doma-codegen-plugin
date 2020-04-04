package org.seasar.doma.gradle.codegen.task;

import java.io.File;
import javax.sql.DataSource;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.Nested;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.options.Option;
import org.seasar.doma.gradle.codegen.GlobalFactory;
import org.seasar.doma.gradle.codegen.desc.EntityDesc;
import org.seasar.doma.gradle.codegen.desc.EntityDescFactory;
import org.seasar.doma.gradle.codegen.desc.EntityPropertyClassNameResolver;
import org.seasar.doma.gradle.codegen.desc.EntityPropertyDescFactory;
import org.seasar.doma.gradle.codegen.desc.NamingType;
import org.seasar.doma.gradle.codegen.dialect.CodeGenDialect;
import org.seasar.doma.gradle.codegen.extension.EntityConfig;
import org.seasar.doma.gradle.codegen.generator.GenerationContext;
import org.seasar.doma.gradle.codegen.generator.Generator;
import org.seasar.doma.gradle.codegen.meta.ResultSetMetaReader;
import org.seasar.doma.gradle.codegen.meta.TableMeta;
import org.seasar.doma.gradle.codegen.util.ClassUtil;
import org.seasar.doma.gradle.codegen.util.FileUtil;

public class CodeGenDtoTask extends DefaultTask {

  private final Property<GlobalFactory> globalFactory =
      getProject().getObjects().property(GlobalFactory.class);

  private final Property<Generator> generator = getProject().getObjects().property(Generator.class);

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

  private final Property<String> encoding = getProject().getObjects().property(String.class);

  private final DirectoryProperty sourceDir = getProject().getObjects().directoryProperty();

  private final Property<String> sql = getProject().getObjects().property(String.class);

  private final Property<String> dtoName = getProject().getObjects().property(String.class);

  private EntityConfig entityConfig;

  @Internal
  public Property<GlobalFactory> getGlobalFactory() {
    return globalFactory;
  }

  @Internal
  public Property<Generator> getGenerator() {
    return generator;
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

  @OutputDirectory
  public DirectoryProperty getSourceDir() {
    return sourceDir;
  }

  @Internal
  public Property<String> getEncoding() {
    return encoding;
  }

  @Input
  @Option(option = "sql", description = "The SQL for a DTO.")
  public Property<String> getSql() {
    return sql;
  }

  @Input
  @Option(option = "dtoName", description = "The simple name of a generated DTO.")
  public Property<String> getDtoName() {
    return dtoName;
  }

  @Nested
  public EntityConfig getEntityConfig() {
    return entityConfig;
  }

  public void setEntityConfig(EntityConfig entityConfig) {
    this.entityConfig = entityConfig;
  }

  @TaskAction
  public void generate() {
    ResultSetMetaReader resultSetMetaReader = createResultSetMetaReader();
    EntityPropertyClassNameResolver entityPropertyClassNameResolver =
        createEntityPropertyClassNameResolver();
    EntityPropertyDescFactory entityPropertyDescFactory =
        createEntityPropertyDescFactory(entityPropertyClassNameResolver);
    EntityDescFactory entityDescFactory = createEntityDescFactory(entityPropertyDescFactory);

    TableMeta tableMeta = resultSetMetaReader.read(sql.get());
    EntityDesc entityDesc =
        entityDescFactory.createEntityDesc(
            tableMeta,
            entityConfig.getPrefix().getOrNull(),
            entityConfig.getSuffix().getOrNull(),
            dtoName.get());
    generateDto(entityDesc);
  }

  private ResultSetMetaReader createResultSetMetaReader() {
    return globalFactory.get().createResultSetMetaReader(dialect.get(), dataSource.get());
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
      superclass =
          ClassUtil.forName(entityConfig.getSuperclassName().getOrNull(), "superclassName");
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
            false);
  }

  protected void generateDto(EntityDesc entityDesc) {
    File javaFile =
        FileUtil.createJavaFile(sourceDir.get().getAsFile(), entityDesc.getQualifiedName());
    GenerationContext context =
        new GenerationContext(
            entityDesc,
            javaFile,
            entityDesc.getTemplateName(),
            encoding.get(),
            entityConfig.getOverwrite().get());
    generator.get().generate(context);
  }
}
