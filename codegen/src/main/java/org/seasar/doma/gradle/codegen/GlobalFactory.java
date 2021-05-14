package org.seasar.doma.gradle.codegen;

import java.io.File;
import java.sql.Driver;
import java.util.List;
import javax.sql.DataSource;
import org.seasar.doma.gradle.codegen.desc.DaoDescFactory;
import org.seasar.doma.gradle.codegen.desc.EntityDescFactory;
import org.seasar.doma.gradle.codegen.desc.EntityListenerDescFactory;
import org.seasar.doma.gradle.codegen.desc.EntityPropertyClassNameResolver;
import org.seasar.doma.gradle.codegen.desc.EntityPropertyDescFactory;
import org.seasar.doma.gradle.codegen.desc.GenerationType;
import org.seasar.doma.gradle.codegen.desc.LanguageClassResolver;
import org.seasar.doma.gradle.codegen.desc.LanguageType;
import org.seasar.doma.gradle.codegen.desc.MappedSuperclassDescFactory;
import org.seasar.doma.gradle.codegen.desc.NamingType;
import org.seasar.doma.gradle.codegen.desc.SqlDescFactory;
import org.seasar.doma.gradle.codegen.desc.SqlTestDescFactory;
import org.seasar.doma.gradle.codegen.desc.SqlTestSuiteDescFactory;
import org.seasar.doma.gradle.codegen.dialect.CodeGenDialect;
import org.seasar.doma.gradle.codegen.generator.Generator;
import org.seasar.doma.gradle.codegen.jdbc.SimpleDataSource;
import org.seasar.doma.gradle.codegen.meta.ResultSetMetaReader;
import org.seasar.doma.gradle.codegen.meta.TableMetaReader;

public class GlobalFactory {

  public DataSource createDataSource(Driver driver, String user, String password, String url) {
    SimpleDataSource dataSource = new SimpleDataSource();
    dataSource.setDriver(driver);
    dataSource.setUser(user);
    dataSource.setPassword(password);
    dataSource.setUrl(url);
    return dataSource;
  }

  public TableMetaReader createTableMetaReader(
      CodeGenDialect dialect,
      DataSource dataSource,
      String catalogName,
      String schemaName,
      String tableNamePattern,
      String ignoredTableNamePattern,
      List<String> tableTypes) {
    return new TableMetaReader(
        dialect,
        dataSource,
        catalogName,
        schemaName,
        tableNamePattern,
        ignoredTableNamePattern,
        tableTypes);
  }

  public ResultSetMetaReader createResultSetMetaReader(
      CodeGenDialect dialect, DataSource dataSource) {
    return new ResultSetMetaReader(dialect, dataSource);
  }

  public EntityPropertyDescFactory createEntityPropertyDescFactory(
      CodeGenDialect dialect,
      EntityPropertyClassNameResolver propertyClassNameResolver,
      LanguageClassResolver languageClassResolver,
      String versionColumnNamePattern,
      GenerationType generationType,
      Long initialValue,
      Long allocationSize,
      boolean showColumnName) {
    return new EntityPropertyDescFactory(
        dialect,
        propertyClassNameResolver,
        languageClassResolver,
        versionColumnNamePattern,
        generationType,
        initialValue,
        allocationSize,
        showColumnName);
  }

  public EntityDescFactory createEntityDescFactory(
      String packageName,
      Class<?> superclass,
      EntityPropertyDescFactory entityPropertyDescFactory,
      NamingType namingType,
      String originalStatesPropertyName,
      boolean showCatalogName,
      boolean showSchemaName,
      boolean showTableName,
      boolean showDbComment,
      boolean useAccessor,
      boolean useListener,
      boolean useMetamodel,
      boolean useMappedSuperclass) {
    return new EntityDescFactory(
        packageName,
        superclass,
        entityPropertyDescFactory,
        namingType,
        originalStatesPropertyName,
        showCatalogName,
        showSchemaName,
        showTableName,
        showDbComment,
        useAccessor,
        useListener,
        useMetamodel,
        useMappedSuperclass);
  }

  public EntityListenerDescFactory createEntityListenerDescFactory(
      String packageName, String superclassName) {
    return new EntityListenerDescFactory(packageName, superclassName);
  }

  public MappedSuperclassDescFactory createMappedSuperclassDescFactory(
      String packageName, String entitySuperclassName) {
    return new MappedSuperclassDescFactory(packageName, entitySuperclassName);
  }

  public DaoDescFactory createDaoDescFactory(
      String packageName, String suffix, String configClassName) {
    return new DaoDescFactory(packageName, suffix, configClassName);
  }

  public EntityPropertyClassNameResolver createEntityPropertyClassNameResolver(File propertyFile) {
    return new EntityPropertyClassNameResolver(propertyFile);
  }

  public SqlTestDescFactory createSqlTestCaseDescFactory(
      String dialectClassName, String url, String user, String password) {
    return new SqlTestDescFactory(dialectClassName, url, user, password);
  }

  public SqlTestSuiteDescFactory createSqlTestSuiteDescFactory(
      SqlTestDescFactory sqlTestDescFactory) {
    return new SqlTestSuiteDescFactory(sqlTestDescFactory);
  }

  public SqlDescFactory createSqlDescFactory(File templatePrimaryDir, CodeGenDialect dialect) {
    return new SqlDescFactory(templatePrimaryDir, dialect);
  }

  public Generator createGenerator(
      LanguageType languageType, String templateEncoding, File templatePrimaryDir) {
    return new Generator(languageType, templateEncoding, templatePrimaryDir);
  }
}
