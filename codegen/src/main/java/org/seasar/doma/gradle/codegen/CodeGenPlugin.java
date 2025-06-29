package org.seasar.doma.gradle.codegen;

import org.gradle.api.DefaultTask;
import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.TaskProvider;
import org.seasar.doma.gradle.codegen.extension.CodeGenConfig;
import org.seasar.doma.gradle.codegen.task.CodeGenDaoDescTask;
import org.seasar.doma.gradle.codegen.task.CodeGenDaoTask;
import org.seasar.doma.gradle.codegen.task.CodeGenDbMetaTask;
import org.seasar.doma.gradle.codegen.task.CodeGenDtoTask;
import org.seasar.doma.gradle.codegen.task.CodeGenEntityDescTask;
import org.seasar.doma.gradle.codegen.task.CodeGenEntityTask;
import org.seasar.doma.gradle.codegen.task.CodeGenSqlTask;
import org.seasar.doma.gradle.codegen.task.CodeGenSqlTestTask;
import org.seasar.doma.gradle.codegen.util.StringUtil;

public class CodeGenPlugin implements Plugin<Project> {

  public static final String EXTENSION_NAME = "domaCodeGen";
  public static final String CONFIGURATION_NAME = "domaCodeGen";
  public static final String TASK_GROUP_NAME = "Doma Code Generation";
  public static final String DB_META_TASK_NAME = "DbMeta";
  public static final String DTO_TASK_NAME = "Dto";
  public static final String ENTITY_DESC_TASK_NAME = "EntityDesc";
  public static final String ENTITY_TASK_NAME = "Entity";
  public static final String DAO_DESC_TASK_NAME = "DaoDesc";
  public static final String DAO_TASK_NAME = "Dao";
  public static final String SQL_TASK_NAME = "Sql";
  public static final String SQL_TEST_TASK_NAME = "SqlTest";
  public static final String ALL_TASK_NAME = "All";

  @Override
  public void apply(Project project) {
    project
        .getConfigurations()
        .create(
            CONFIGURATION_NAME,
            config -> {
              config.setDescription(
                  "The libraries used by the Doma CodeGen plugin for JDBC drivers and other dependencies");
              config.setVisible(false);
              config.setCanBeConsumed(false);
              config.setCanBeResolved(true);
            });

    NamedDomainObjectContainer<CodeGenConfig> container =
        project.container(
            CodeGenConfig.class,
            name -> project.getObjects().newInstance(CodeGenConfig.class, name, project));
    project.getExtensions().add(EXTENSION_NAME, container);
    container.all(codeGenConfig -> registerTasks(project, codeGenConfig));
  }

  private void registerTasks(Project project, CodeGenConfig codeGenConfig) {
    TaskContainer tasks = project.getTasks();
    String prefix = EXTENSION_NAME + StringUtil.capitalize(codeGenConfig.getName());

    TaskProvider<CodeGenDbMetaTask> dbMetaTask =
        tasks.register(prefix + DB_META_TASK_NAME, CodeGenDbMetaTask.class);
    TaskProvider<CodeGenDtoTask> dtoTask =
        tasks.register(prefix + DTO_TASK_NAME, CodeGenDtoTask.class);
    TaskProvider<CodeGenEntityDescTask> entityDescTask =
        tasks.register(prefix + ENTITY_DESC_TASK_NAME, CodeGenEntityDescTask.class);
    TaskProvider<CodeGenEntityTask> entityTask =
        tasks.register(prefix + ENTITY_TASK_NAME, CodeGenEntityTask.class);
    TaskProvider<CodeGenDaoDescTask> daoDescTask =
        tasks.register(prefix + DAO_DESC_TASK_NAME, CodeGenDaoDescTask.class);
    TaskProvider<CodeGenDaoTask> daoTask =
        tasks.register(prefix + DAO_TASK_NAME, CodeGenDaoTask.class);
    TaskProvider<CodeGenSqlTask> sqlTask =
        tasks.register(prefix + SQL_TASK_NAME, CodeGenSqlTask.class);
    TaskProvider<CodeGenSqlTestTask> sqlTestTask =
        tasks.register(prefix + SQL_TEST_TASK_NAME, CodeGenSqlTestTask.class);
    TaskProvider<DefaultTask> allTask = tasks.register(prefix + ALL_TASK_NAME, DefaultTask.class);

    dbMetaTask.configure(
        task -> {
          task.setDescription("Reads database metadata.");
          connectProperties(task, codeGenConfig);
          task.getOutputs().upToDateWhen(__ -> false);
        });
    entityDescTask.configure(
        task -> {
          task.setDescription("Creates entity descriptions.");
          task.dependsOn(dbMetaTask);
          task.getOutputs().upToDateWhen(__ -> false);
          task.getTableMetaList().set(dbMetaTask.get().getTableMetaList());
          connectProperties(task, codeGenConfig);
        });
    daoDescTask.configure(
        task -> {
          task.setDescription("Creates DAO descriptions.");
          task.dependsOn(entityDescTask);
          task.getOutputs().upToDateWhen(__ -> false);
          task.getEntityDescList().set(entityDescTask.get().getEntityDescList());
          connectProperties(task, codeGenConfig);
        });
    dtoTask.configure(
        task -> {
          task.setDescription("Reads resultSet metadata and generate a DTO source file.");
          task.setGroup(TASK_GROUP_NAME);
          task.getOutputs().upToDateWhen(__ -> false);
          connectProperties(task, codeGenConfig);
        });
    entityTask.configure(
        task -> {
          task.setDescription("Generates entity source files.");
          task.setGroup(TASK_GROUP_NAME);
          task.dependsOn(entityDescTask);
          task.getOutputs().upToDateWhen(__ -> false);
          task.getEntityDescList().set(entityDescTask.get().getEntityDescList());
          connectProperties(task, codeGenConfig);
        });
    daoTask.configure(
        task -> {
          task.setDescription("Generates DAO source files.");
          task.setGroup(TASK_GROUP_NAME);
          task.dependsOn(daoDescTask);
          task.getOutputs().upToDateWhen(__ -> false);
          task.getDaoDescList().set(daoDescTask.get().getDaoDescList());
          connectProperties(task, codeGenConfig);
        });
    sqlTask.configure(
        task -> {
          task.setDescription("Generates SQL files.");
          task.setGroup(TASK_GROUP_NAME);
          task.dependsOn(daoDescTask);
          task.getOutputs().upToDateWhen(__ -> false);
          task.getDaoDescList().set(daoDescTask.get().getDaoDescList());
          connectProperties(task, codeGenConfig);
        });
    sqlTestTask.configure(
        task -> {
          task.setDescription("Generates SQL test source files.");
          task.setGroup(TASK_GROUP_NAME);
          task.dependsOn(sqlTask);
          task.getOutputs().upToDateWhen(__ -> false);
          connectProperties(task, codeGenConfig);
        });
    allTask.configure(
        task -> {
          task.setDescription("Generates all.");
          task.setGroup(TASK_GROUP_NAME);
          task.dependsOn(entityTask, daoTask, sqlTask, sqlTestTask);
          task.getOutputs().upToDateWhen(__ -> false);
        });
  }

  private void connectProperties(CodeGenDbMetaTask task, CodeGenConfig extension) {
    task.getGlobalFactory().set(extension.getGlobalFactory());
    task.getDataSource().set(extension.getDataSource());
    task.getDialect().set(extension.getCodeGenDialect());
    task.getCatalogName().set(extension.getCatalogName());
    task.getSchemaName().set(extension.getSchemaName());
    task.getTableTypes().set(extension.getTableTypes());
    task.getTableNamePattern().set(extension.getTableNamePattern());
    task.getIgnoredTableNamePattern().set(extension.getIgnoredTableNamePattern());
  }

  private void connectProperties(CodeGenDtoTask task, CodeGenConfig extension) {
    task.getGlobalFactory().set(extension.getGlobalFactory());
    task.getGenerator().set(extension.getGenerator());
    task.getDataSource().set(extension.getDataSource());
    task.getDialect().set(extension.getCodeGenDialect());
    task.getSchemaName().set(extension.getSchemaName());
    task.getTableTypes().set(extension.getTableTypes());
    task.getTableNamePattern().set(extension.getTableNamePattern());
    task.getIgnoredTableNamePattern().set(extension.getIgnoredTableNamePattern());
    task.getVersionColumnNamePattern().set(extension.getVersionColumnNamePattern());
    task.getLanguageType().set(extension.getLanguageType());
    task.getLanguageClassResolver().set(extension.getLanguageClassResolver());
    task.getSourceDir().set(extension.getSourceDir());
    task.getEncoding().set(extension.getEncoding());
    task.setEntityConfig(extension.getEntityConfig());
  }

  private void connectProperties(CodeGenEntityDescTask task, CodeGenConfig extension) {
    task.getGlobalFactory().set(extension.getGlobalFactory());
    task.getDataSource().set(extension.getDataSource());
    task.getDialect().set(extension.getCodeGenDialect());
    task.getSchemaName().set(extension.getSchemaName());
    task.getTableTypes().set(extension.getTableTypes());
    task.getTableNamePattern().set(extension.getTableNamePattern());
    task.getIgnoredTableNamePattern().set(extension.getIgnoredTableNamePattern());
    task.getVersionColumnNamePattern().set(extension.getVersionColumnNamePattern());
    task.getLanguageClassResolver().set(extension.getLanguageClassResolver());
    task.setEntityConfig(extension.getEntityConfig());
  }

  private void connectProperties(CodeGenEntityTask task, CodeGenConfig extension) {
    task.getGlobalFactory().set(extension.getGlobalFactory());
    task.getGenerator().set(extension.getGenerator());
    task.getLanguageType().set(extension.getLanguageType());
    task.getSourceDir().set(extension.getSourceDir());
    task.getEncoding().set(extension.getEncoding());
    task.setEntityConfig(extension.getEntityConfig());
  }

  private void connectProperties(CodeGenDaoDescTask task, CodeGenConfig extension) {
    task.getGlobalFactory().set(extension.getGlobalFactory());
    task.setDaoConfig(extension.getDaoConfig());
  }

  private void connectProperties(CodeGenDaoTask task, CodeGenConfig extension) {
    task.getGenerator().set(extension.getGenerator());
    task.getLanguageType().set(extension.getLanguageType());
    task.getSourceDir().set(extension.getSourceDir());
    task.getEncoding().set(extension.getEncoding());
    task.setDaoConfig(extension.getDaoConfig());
  }

  private void connectProperties(CodeGenSqlTask task, CodeGenConfig extension) {
    task.getGlobalFactory().set(extension.getGlobalFactory());
    task.getDialect().set(extension.getCodeGenDialect());
    task.getTemplateDir().set(extension.getTemplateDir());
    task.getGenerator().set(extension.getGenerator());
    task.getResourceDir().set(extension.getResourceDir());
    task.setSqlConfig(extension.getSqlConfig());
  }

  private void connectProperties(CodeGenSqlTestTask task, CodeGenConfig extension) {
    task.getGlobalFactory().set(extension.getGlobalFactory());
    task.getDialect().set(extension.getCodeGenDialect());
    task.getUser().set(extension.getUser());
    task.getPassword().set(extension.getPassword());
    task.getUrl().set(extension.getUrl());
    task.getGenerator().set(extension.getGenerator());
    task.getLanguageType().set(extension.getLanguageType());
    task.getTestSourceDir().set(extension.getTestSourceDir());
    task.getEncoding().set(extension.getEncoding());
    task.setSqlTestConfig(extension.getSqlTestConfig());
  }
}
