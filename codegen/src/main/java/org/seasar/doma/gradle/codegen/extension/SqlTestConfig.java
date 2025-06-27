package org.seasar.doma.gradle.codegen.extension;

import javax.inject.Inject;
import org.gradle.api.file.FileTree;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.Internal;

public class SqlTestConfig {

  private final Property<FileTree> sqlFiles;
  private final Property<String> url;
  private final Property<String> user;
  private final Property<String> password;

  @Inject
  public SqlTestConfig(ObjectFactory objects) {
    this.sqlFiles = objects.property(FileTree.class);
    this.url = objects.property(String.class);
    this.user = objects.property(String.class);
    this.password = objects.property(String.class);
  }

  @InputFiles
  public Property<FileTree> getSqlFiles() {
    return sqlFiles;
  }

  public void setSqlFiles(FileTree sqlFiles) {
    this.sqlFiles.set(sqlFiles);
  }

  @Internal
  public Property<String> getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url.set(url);
  }

  @Internal
  public Property<String> getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user.set(user);
  }

  @Internal
  public Property<String> getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password.set(password);
  }
}
