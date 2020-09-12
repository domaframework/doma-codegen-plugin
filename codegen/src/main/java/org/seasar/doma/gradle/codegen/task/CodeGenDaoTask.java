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
import org.seasar.doma.gradle.codegen.desc.DaoDesc;
import org.seasar.doma.gradle.codegen.desc.LanguageType;
import org.seasar.doma.gradle.codegen.extension.DaoConfig;
import org.seasar.doma.gradle.codegen.generator.GenerationContext;
import org.seasar.doma.gradle.codegen.generator.Generator;
import org.seasar.doma.gradle.codegen.util.FileUtil;

public class CodeGenDaoTask extends DefaultTask {

  private final ListProperty<DaoDesc> daoDescList =
      getProject().getObjects().listProperty(DaoDesc.class);

  private final Property<Generator> generator = getProject().getObjects().property(Generator.class);

  private final Property<LanguageType> languageType =
      getProject().getObjects().property(LanguageType.class);

  private final DirectoryProperty sourceDir = getProject().getObjects().directoryProperty();

  private final Property<String> encoding = getProject().getObjects().property(String.class);

  private DaoConfig daoConfig;

  @Internal
  public ListProperty<DaoDesc> getDaoDescList() {
    return daoDescList;
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
  public DirectoryProperty getSourceDir() {
    return sourceDir;
  }

  @Internal
  public Property<String> getEncoding() {
    return encoding;
  }

  @Nested
  public DaoConfig getDaoConfig() {
    return daoConfig;
  }

  public void setDaoConfig(DaoConfig daoConfig) {
    this.daoConfig = daoConfig;
  }

  @TaskAction
  public void generate() {
    daoDescList.get().forEach(this::generateDao);
  }

  private void generateDao(DaoDesc daoDesc) {
    File sourceFile =
        FileUtil.createFile(
            languageType.get(), sourceDir.getAsFile().get(), daoDesc.getQualifiedName());
    GenerationContext context =
        new GenerationContext(
            daoDesc,
            sourceFile,
            daoDesc.getTemplateName(),
            encoding.get(),
            daoConfig.getOverwrite().get());
    generator.get().generate(context);
  }
}
