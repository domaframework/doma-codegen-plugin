package org.seasar.doma.gradle.codegen.task;

import java.io.File;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.Nested;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;
import org.seasar.doma.gradle.codegen.GlobalFactory;
import org.seasar.doma.gradle.codegen.desc.LanguageType;
import org.seasar.doma.gradle.codegen.desc.SqlTestDesc;
import org.seasar.doma.gradle.codegen.desc.SqlTestDescFactory;
import org.seasar.doma.gradle.codegen.desc.SqlTestSuiteDesc;
import org.seasar.doma.gradle.codegen.desc.SqlTestSuiteDescFactory;
import org.seasar.doma.gradle.codegen.dialect.CodeGenDialect;
import org.seasar.doma.gradle.codegen.extension.SqlTestConfig;
import org.seasar.doma.gradle.codegen.generator.GenerationContext;
import org.seasar.doma.gradle.codegen.generator.Generator;
import org.seasar.doma.gradle.codegen.util.FileUtil;

public class CodeGenSqlTestTask extends DefaultTask {

  private final Property<GlobalFactory> globalFactory =
      getProject().getObjects().property(GlobalFactory.class);

  private final Property<CodeGenDialect> dialect =
      getProject().getObjects().property(CodeGenDialect.class);

  private final Property<String> user = getProject().getObjects().property(String.class);

  private final Property<String> password = getProject().getObjects().property(String.class);

  private final Property<String> url = getProject().getObjects().property(String.class);

  private final Property<Generator> generator = getProject().getObjects().property(Generator.class);

  private final Property<LanguageType> languageType =
      getProject().getObjects().property(LanguageType.class);

  private final DirectoryProperty testSourceDir = getProject().getObjects().directoryProperty();

  private final Property<String> encoding = getProject().getObjects().property(String.class);

  private SqlTestConfig sqlTestConfig;

  @Internal
  public Property<GlobalFactory> getGlobalFactory() {
    return globalFactory;
  }

  @Internal
  public Property<CodeGenDialect> getDialect() {
    return dialect;
  }

  @Internal
  public Property<String> getUser() {
    return user;
  }

  @Internal
  public Property<String> getPassword() {
    return password;
  }

  @Internal
  public Property<String> getUrl() {
    return url;
  }

  @Internal
  public Property<Generator> getGenerator() {
    return generator;
  }

  @Internal
  public Property<LanguageType> getLanguageType() {
    return languageType;
  }

  @OutputDirectory
  public DirectoryProperty getTestSourceDir() {
    return testSourceDir;
  }

  @Internal
  public Property<String> getEncoding() {
    return encoding;
  }

  @Nested
  public SqlTestConfig getSqlTestConfig() {
    return sqlTestConfig;
  }

  public void setSqlTestConfig(SqlTestConfig sqlTestConfig) {
    this.sqlTestConfig = sqlTestConfig;
  }

  @TaskAction
  public void generate() {
    SqlTestSuiteDescFactory sqlTestSuiteDescFactory = createSqlTestSuiteDescFactory();
    SqlTestSuiteDesc sqlTestSuiteDesc =
        sqlTestSuiteDescFactory.createSqlTestSuiteDesc(sqlTestConfig.getSqlFiles().get());
    sqlTestSuiteDesc.getTestCaseDescs().forEach(this::generateSqlTestCase);
  }

  private SqlTestSuiteDescFactory createSqlTestSuiteDescFactory() {
    SqlTestDescFactory sqlTestDescFactory =
        globalFactory
            .get()
            .createSqlTestCaseDescFactory(
                dialect.get().getDialectClassName(), url.get(), user.get(), password.get());
    return globalFactory.get().createSqlTestSuiteDescFactory(sqlTestDescFactory);
  }

  private void generateSqlTestCase(SqlTestDesc sqlTestDesc) {
    File sourceFile =
        FileUtil.createFile(
            languageType.get(), testSourceDir.get().getAsFile(), sqlTestDesc.getQualifiedName());
    GenerationContext context =
        new GenerationContext(
            sqlTestDesc, sourceFile, sqlTestDesc.getTemplateName(), encoding.get(), true);
    generator.get().generate(context);
  }
}
