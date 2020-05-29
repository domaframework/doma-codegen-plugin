package org.seasar.doma.gradle.codegen.task;

import java.io.File;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.Nested;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;
import org.seasar.doma.gradle.codegen.GlobalFactory;
import org.seasar.doma.gradle.codegen.desc.EntityDesc;
import org.seasar.doma.gradle.codegen.desc.EntityListenerDesc;
import org.seasar.doma.gradle.codegen.desc.EntityListenerDescFactory;
import org.seasar.doma.gradle.codegen.extension.EntityConfig;
import org.seasar.doma.gradle.codegen.generator.GenerationContext;
import org.seasar.doma.gradle.codegen.generator.Generator;
import org.seasar.doma.gradle.codegen.util.FileUtil;

public class CodeGenEntityTask extends DefaultTask {

  private final ListProperty<EntityDesc> entityDescList =
      getProject().getObjects().listProperty(EntityDesc.class);

  private final Property<GlobalFactory> globalFactory =
      getProject().getObjects().property(GlobalFactory.class);

  private final Property<Generator> generator = getProject().getObjects().property(Generator.class);

  private final DirectoryProperty sourceDir = getProject().getObjects().directoryProperty();

  private final Property<String> encoding = getProject().getObjects().property(String.class);

  private EntityConfig entityConfig;

  @Internal
  public ListProperty<EntityDesc> getEntityDescList() {
    return entityDescList;
  }

  @Internal
  public Property<GlobalFactory> getGlobalFactory() {
    return globalFactory;
  }

  @Internal
  public Property<Generator> getGenerator() {
    return generator;
  }

  @OutputDirectory
  public DirectoryProperty getSourceDir() {
    return sourceDir;
  }

  @Internal
  public Property<String> getEncoding() {
    return encoding;
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
    EntityListenerDescFactory entityListenerDescFactory = createEntityListenerDescFactory();
    for (EntityDesc entityDesc : entityDescList.get()) {
      generateEntity(entityDesc);

      if (entityDesc.isUseListener()) {
        EntityListenerDesc entityListenerDesc =
            entityListenerDescFactory.createEntityListenerDesc(entityDesc);
        generateEntityListener(entityListenerDesc);
      }
    }
  }

  private EntityListenerDescFactory createEntityListenerDescFactory() {
    return globalFactory
        .get()
        .createEntityListenerDescFactory(
            entityConfig.getPackageName().get(),
            entityConfig.getListenerSuperclassName().getOrNull());
  }

  private void generateEntity(EntityDesc entityDesc) {
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

  private void generateEntityListener(EntityListenerDesc entityListenerDesc) {
    File javaFile =
        FileUtil.createJavaFile(sourceDir.get().getAsFile(), entityListenerDesc.getQualifiedName());
    GenerationContext context =
        new GenerationContext(
            entityListenerDesc,
            javaFile,
            entityListenerDesc.getTemplateName(),
            encoding.get(),
            entityConfig.getOverwriteListener().get());
    generator.get().generate(context);
  }
}
