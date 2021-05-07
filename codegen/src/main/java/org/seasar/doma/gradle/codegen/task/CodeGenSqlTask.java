package org.seasar.doma.gradle.codegen.task;

import java.io.File;
import java.nio.charset.StandardCharsets;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.Nested;
import org.gradle.api.tasks.TaskAction;
import org.seasar.doma.gradle.codegen.GlobalFactory;
import org.seasar.doma.gradle.codegen.desc.DaoDesc;
import org.seasar.doma.gradle.codegen.desc.SqlDesc;
import org.seasar.doma.gradle.codegen.desc.SqlDescFactory;
import org.seasar.doma.gradle.codegen.dialect.CodeGenDialect;
import org.seasar.doma.gradle.codegen.extension.SqlConfig;
import org.seasar.doma.gradle.codegen.generator.GenerationContext;
import org.seasar.doma.gradle.codegen.generator.Generator;
import org.seasar.doma.gradle.codegen.util.FileUtil;

public class CodeGenSqlTask extends DefaultTask {

  private final ListProperty<DaoDesc> daoDescList =
      getProject().getObjects().listProperty(DaoDesc.class);

  private final Property<GlobalFactory> globalFactory =
      getProject().getObjects().property(GlobalFactory.class);

  private final Property<CodeGenDialect> dialect =
      getProject().getObjects().property(CodeGenDialect.class);

  private final DirectoryProperty templateDir = getProject().getObjects().directoryProperty();

  private final DirectoryProperty resourceDir = getProject().getObjects().directoryProperty();

  private final Property<Generator> generator = getProject().getObjects().property(Generator.class);

  private SqlConfig sqlConfig;

  @Internal
  public ListProperty<DaoDesc> getDaoDescList() {
    return daoDescList;
  }

  @Internal
  public Property<GlobalFactory> getGlobalFactory() {
    return globalFactory;
  }

  @Internal
  public Property<CodeGenDialect> getDialect() {
    return dialect;
  }

  @Internal
  public DirectoryProperty getTemplateDir() {
    return templateDir;
  }

  @Internal
  public DirectoryProperty getResourceDir() {
    return resourceDir;
  }

  @Internal
  public Property<Generator> getGenerator() {
    return generator;
  }

  @Nested
  public SqlConfig getSqlConfig() {
    return sqlConfig;
  }

  public void setSqlConfig(SqlConfig sqlConfig) {
    this.sqlConfig = sqlConfig;
  }

  @TaskAction
  public void generate() {
    SqlDescFactory sqlDescFactory = createSqlDescFactory();
    daoDescList.get().stream()
        .flatMap(
            daoDesc -> sqlDescFactory.createSqlDescs(daoDesc.getEntityDesc(), daoDesc).stream())
        .forEach(this::generateSql);
  }

  private SqlDescFactory createSqlDescFactory() {
    return globalFactory
        .get()
        .createSqlDescFactory(templateDir.getAsFile().getOrNull(), dialect.get());
  }

  private void generateSql(SqlDesc sqlDesc) {
    File sqlFile =
        FileUtil.createSqlDir(
            resourceDir.get().getAsFile(),
            sqlDesc.getDaoDesc().getQualifiedName(),
            sqlDesc.getFileName());
    GenerationContext context =
        new GenerationContext(
            sqlDesc,
            sqlFile,
            sqlDesc.getTemplateName(),
            StandardCharsets.UTF_8.name(),
            sqlConfig.getOverwrite().get());
    generator.get().generate(context);
  }
}
