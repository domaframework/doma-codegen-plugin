package org.seasar.doma.gradle.codegen.extension;

import static org.seasar.doma.gradle.codegen.CodeGenPlugin.CONFIGURATION_NAME;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.sql.Driver;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import javax.sql.DataSource;

import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.file.Directory;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.FileTree;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.Provider;
import org.seasar.doma.gradle.codegen.GlobalFactory;
import org.seasar.doma.gradle.codegen.desc.LanguageClassResolver;
import org.seasar.doma.gradle.codegen.desc.LanguageType;
import org.seasar.doma.gradle.codegen.dialect.CodeGenDialect;
import org.seasar.doma.gradle.codegen.dialect.CodeGenDialectRegistry;
import org.seasar.doma.gradle.codegen.exception.CodeGenException;
import org.seasar.doma.gradle.codegen.generator.Generator;
import org.seasar.doma.gradle.codegen.jdbc.DriverWrapper;
import org.seasar.doma.gradle.codegen.message.Message;
import org.seasar.doma.gradle.codegen.util.ClassUtil;
import org.seasar.doma.gradle.codegen.util.JdbcUtil;

public class CodeGenConfig {

  private final String name;

  private final Configuration configuration;

  private final Property<GlobalFactory> globalFactory;

  private final Property<DataSource> dataSource;

  private final Property<CodeGenDialect> codeGenDialect;

  private final Property<String> user;

  private final Property<String> password;

  private final Property<String> url;

  private final Property<String> catalogName;

  private final Property<String> schemaName;

  private final Property<String> tableNamePattern;

  private final Property<String> ignoredTableNamePattern;

  private final ListProperty<String> tableTypes;

  private final Property<String> versionColumnNamePattern;

  private final Property<LanguageType> languageType;

  private final Property<LanguageClassResolver> languageClassResolver;

  private final Property<String> templateEncoding;

  private final DirectoryProperty templateDir;

  private final Property<String> encoding;

  private final DirectoryProperty sourceDir;

  private final DirectoryProperty testSourceDir;

  private final DirectoryProperty resourceDir;

  private final Property<Generator> generator;

  private final EntityConfig entityConfig;

  private final DaoConfig daoConfig;

  private final SqlConfig sqlConfig;

  private final SqlTestConfig sqlTestConfig;

  @Inject
  public CodeGenConfig(String name, Project project) {
    this.name = name;

    this.configuration = project.getConfigurations().getByName(CONFIGURATION_NAME);

    ObjectFactory objects = project.getObjects();

    globalFactory = objects.property(GlobalFactory.class);
    dataSource = objects.property(DataSource.class);
    codeGenDialect = objects.property(CodeGenDialect.class);
    url = objects.property(String.class);
    user = objects.property(String.class);
    password = objects.property(String.class);
    catalogName = objects.property(String.class);
    schemaName = objects.property(String.class);
    tableNamePattern = objects.property(String.class);
    ignoredTableNamePattern = objects.property(String.class);
    tableTypes = objects.listProperty(String.class);
    versionColumnNamePattern = objects.property(String.class);
    languageType = objects.property(LanguageType.class);
    languageClassResolver = objects.property(LanguageClassResolver.class);
    templateEncoding = objects.property(String.class);
    templateDir = objects.directoryProperty();
    encoding = objects.property(String.class);
    sourceDir = objects.directoryProperty();
    testSourceDir = objects.directoryProperty();
    resourceDir = objects.directoryProperty();
    generator = objects.property(Generator.class);

    entityConfig = objects.newInstance(EntityConfig.class);
    daoConfig = objects.newInstance(DaoConfig.class);
    sqlConfig = objects.newInstance(SqlConfig.class);
    sqlTestConfig = objects.newInstance(SqlTestConfig.class);

    applyDefaults(project);

    project.afterEvaluate(this::validateProperties);
  }

  private void applyDefaults(Project project) {
    globalFactory.set(new GlobalFactory());
    dataSource.set(dataSourceProvider());
    codeGenDialect.set(codeGenDialectProvider());
    tableNamePattern.set(".*");
    ignoredTableNamePattern.set(".*\\$.*");
    tableTypes.set(Collections.singletonList("TABLE"));
    versionColumnNamePattern.set("VERSION([_]?NO)?");
    languageType.set(LanguageType.JAVA);
    languageClassResolver.set(languageClassResolverProvider());
    templateEncoding.set(StandardCharsets.UTF_8.name());
    encoding.set(StandardCharsets.UTF_8.name());
    sourceDir.set(sourceDirProvider(project));
    testSourceDir.set(testSourceDirProvider(project));
    resourceDir.set(project.file("src/main/resources"));
    generator.set(generatorProvider());

    sqlTestConfig.getSqlFiles().set(sqlFilesProvider());
  }

  private Provider<DataSource> dataSourceProvider() {
    return url.map(
            it -> {
              String driverClassName = JdbcUtil.inferDriverClassName(it);
              if (driverClassName == null) {
                throw new CodeGenException(Message.DOMAGEN0024);
              }
              ClassLoader classLoader = createClassLoader();
              Driver driver =
                      ClassUtil.newInstance(Driver.class, driverClassName, "driverClassName", classLoader);
              DriverWrapper driverWrapper = new DriverWrapper(driver);
              return globalFactory
                      .get()
                      .createDataSource(driverWrapper, user.getOrNull(), password.getOrNull(), url.get());
            });
  }

  protected ClassLoader createClassLoader() {
    if (configuration == null || configuration.isEmpty()) {
      return getClass().getClassLoader();
    }
    Set<File> fileSet = configuration.getFiles();
    URL[] urls = new URL[fileSet.size()];
    int i = 0;
    for (File file : fileSet) {
      try {
        urls[i++] = file.toURI().toURL();
      } catch (Exception e) {
        throw new RuntimeException("Failed to convert file to URL: " + file, e);
      }
    }
    return new URLClassLoader(urls, getClass().getClassLoader());
  }

  private Provider<CodeGenDialect> codeGenDialectProvider() {
    return url.map(
            it -> {
              String dialectName = JdbcUtil.inferDialectName(it);
              if (dialectName == null) {
                throw new CodeGenException(Message.DOMAGEN0025);
              }
              CodeGenDialect codeGenDialect = CodeGenDialectRegistry.lookup(dialectName);
              if (codeGenDialect == null) {
                throw new CodeGenException(Message.DOMAGEN0023, dialectName);
              }
              return codeGenDialect;
            });
  }

  private Provider<LanguageClassResolver> languageClassResolverProvider() {
    return languageType.map(LanguageType::getResolver);
  }

  private Provider<Directory> sourceDirProvider(Project project) {
    return languageType.map(
            it -> project.getLayout().getProjectDirectory().dir("src/main/" + it.name().toLowerCase()));
  }

  private Provider<Directory> testSourceDirProvider(Project project) {
    return languageType.map(
            it -> project.getLayout().getProjectDirectory().dir("src/test/" + it.name().toLowerCase()));
  }

  private Provider<Generator> generatorProvider() {
    return globalFactory.map(
            it ->
                    it.createGenerator(
                            languageType.get(), templateEncoding.get(), templateDir.getAsFile().getOrNull()));
  }

  private Provider<FileTree> sqlFilesProvider() {
    return resourceDir.map(
            it -> {
              String packageName = daoConfig.getPackageName().get();
              String path = packageName.replace('.', '/');
              return it.getAsFileTree()
                      .matching(
                              filterConfig -> filterConfig.include("META-INF/" + path + "/*/*.sql"));
            });
  }

  private void validateProperties(Project project) {
    if (!globalFactory.isPresent()) {
      throw new CodeGenException(Message.DOMAGEN0007, "globalFactory", "");
    }
    if (!dataSource.isPresent()) {
      throw new CodeGenException(
              Message.DOMAGEN0007, "dataSource", "Specify the \"url\" or the \"dataSource\" property.");
    }
    if (!codeGenDialect.isPresent()) {
      throw new CodeGenException(
              Message.DOMAGEN0007,
              "codeGenDialect",
              "Specify the \"url\" or the \"codeGenDialect\" property.");
    }
    if (!tableNamePattern.isPresent()) {
      throw new CodeGenException(Message.DOMAGEN0007, "tableNamePattern", "");
    }
    if (!ignoredTableNamePattern.isPresent()) {
      throw new CodeGenException(Message.DOMAGEN0007, "ignoredTableNamePattern", "");
    }
    if (!templateEncoding.isPresent()) {
      throw new CodeGenException(Message.DOMAGEN0007, "templateEncoding", "");
    }
    if (!encoding.isPresent()) {
      throw new CodeGenException(Message.DOMAGEN0007, "encoding", "");
    }
    if (!sourceDir.isPresent()) {
      throw new CodeGenException(Message.DOMAGEN0007, "sourceDir", "");
    }
    if (!resourceDir.isPresent()) {
      throw new CodeGenException(Message.DOMAGEN0007, "resourceDir", "");
    }
    if (!testSourceDir.isPresent()) {
      throw new CodeGenException(Message.DOMAGEN0007, "testSourceDir", "");
    }
    if (!generator.isPresent()) {
      throw new CodeGenException(Message.DOMAGEN0007, "generator", "");
    }
  }

  public String getName() {
    return name;
  }

  public Property<GlobalFactory> getGlobalFactory() {
    return globalFactory;
  }

  public void setGlobalFactory(GlobalFactory globalFactory) {
    this.globalFactory.set(globalFactory);
  }

  public Property<DataSource> getDataSource() {
    return dataSource;
  }

  public void setDataSource(DataSource dataSource) {
    this.dataSource.set(dataSource);
  }

  public Property<CodeGenDialect> getCodeGenDialect() {
    return codeGenDialect;
  }

  public void setCodeGenDialect(CodeGenDialect codeGenDialect) {
    this.codeGenDialect.set(codeGenDialect);
  }

  public Property<String> getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user.set(user);
  }

  public Property<String> getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password.set(password);
  }

  public Property<String> getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url.set(url);
  }

  public Property<String> getCatalogName() {
    return catalogName;
  }

  public void setCatalogName(String catalogName) {
    this.catalogName.set(catalogName);
  }

  public Property<String> getSchemaName() {
    return schemaName;
  }

  public void setSchemaName(String schemaName) {
    this.schemaName.set(schemaName);
  }

  public Property<String> getTableNamePattern() {
    return tableNamePattern;
  }

  public void setTableNamePattern(String tableNamePattern) {
    this.tableNamePattern.set(tableNamePattern);
  }

  public Property<String> getIgnoredTableNamePattern() {
    return ignoredTableNamePattern;
  }

  public void setIgnoredTableNamePattern(String ignoredTableNamePattern) {
    this.ignoredTableNamePattern.set(ignoredTableNamePattern);
  }

  public ListProperty<String> getTableTypes() {
    return tableTypes;
  }

  public void setTableTypes(List<String> tableTypes) {
    this.tableTypes.set(tableTypes);
  }

  public Property<String> getVersionColumnNamePattern() {
    return versionColumnNamePattern;
  }

  public void setVersionColumnNamePattern(String versionColumnNamePattern) {
    this.versionColumnNamePattern.set(versionColumnNamePattern);
  }

  public Property<LanguageType> getLanguageType() {
    return languageType;
  }

  public void setLanguageType(LanguageType languageType) {
    this.languageType.set(languageType);
  }

  public Property<LanguageClassResolver> getLanguageClassResolver() {
    return languageClassResolver;
  }

  public void setLanguageClassResolver(LanguageClassResolver languageClassResolver) {
    this.languageClassResolver.set(languageClassResolver);
  }

  public Property<String> getTemplateEncoding() {
    return templateEncoding;
  }

  public void setTemplateEncoding(String templateEncoding) {
    this.templateEncoding.set(templateEncoding);
  }

  public DirectoryProperty getTemplateDir() {
    return templateDir;
  }

  public void setTemplateDir(File templateDir) {
    this.templateDir.set(templateDir);
  }

  public Property<String> getEncoding() {
    return encoding;
  }

  public void setEncoding(String encoding) {
    this.encoding.set(encoding);
  }

  public DirectoryProperty getSourceDir() {
    return sourceDir;
  }

  public void setSourceDir(File sourceDir) {
    this.sourceDir.set(sourceDir);
  }

  public DirectoryProperty getTestSourceDir() {
    return testSourceDir;
  }

  public void setTestSourceDir(File testSourceDir) {
    this.testSourceDir.set(testSourceDir);
  }

  public DirectoryProperty getResourceDir() {
    return resourceDir;
  }

  public void setResourceDir(File resourceDir) {
    this.resourceDir.set(resourceDir);
  }

  public Property<Generator> getGenerator() {
    return generator;
  }

  public void setGenerator(Generator generator) {
    this.generator.set(generator);
  }

  public EntityConfig getEntityConfig() {
    return entityConfig;
  }

  public DaoConfig getDaoConfig() {
    return daoConfig;
  }

  public SqlConfig getSqlConfig() {
    return sqlConfig;
  }

  public SqlTestConfig getSqlTestConfig() {
    return sqlTestConfig;
  }

  public void entity(Action<EntityConfig> action) {
    action.execute(entityConfig);
  }

  public void dao(Action<DaoConfig> action) {
    action.execute(daoConfig);
  }

  public void sql(Action<SqlConfig> action) {
    action.execute(sqlConfig);
  }

  public void sqlTest(Action<SqlTestConfig> action) {
    action.execute(sqlTestConfig);
  }
}
