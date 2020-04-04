package org.seasar.doma.gradle.codegen.extension;

import javax.inject.Inject;
import org.gradle.api.file.FileTree;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.InputFiles;

public class SqlTestConfig {

  private final Property<FileTree> sqlFiles;

  @Inject
  public SqlTestConfig(ObjectFactory objects) {
    this.sqlFiles = objects.property(FileTree.class);
  }

  @InputFiles
  public Property<FileTree> getSqlFiles() {
    return sqlFiles;
  }

  public void setSqlFiles(FileTree sqlFiles) {
    this.sqlFiles.set(sqlFiles);
  }
}
