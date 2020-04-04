package org.seasar.doma.gradle.codegen.extension;

import javax.inject.Inject;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Internal;

public class DaoConfig {

  private final Property<Boolean> overwrite;

  private final Property<String> packageName;

  private final Property<String> suffix;

  private final Property<String> configClassName;

  @Inject
  public DaoConfig(ObjectFactory objects) {
    overwrite = objects.property(Boolean.class);
    packageName = objects.property(String.class);
    suffix = objects.property(String.class);
    configClassName = objects.property(String.class);

    overwrite.set(false);
    packageName.set("example.dao");
    suffix.set("Dao");
  }

  @Internal
  public Property<Boolean> getOverwrite() {
    return overwrite;
  }

  public void setOverwrite(boolean overwrite) {
    this.overwrite.set(overwrite);
  }

  @Internal
  public Property<String> getPackageName() {
    return packageName;
  }

  public void setPackageName(String packageName) {
    this.packageName.set(packageName);
  }

  @Internal
  public Property<String> getSuffix() {
    return suffix;
  }

  public void setSuffix(String suffix) {
    this.suffix.set(suffix);
  }

  @Internal
  public Property<String> getConfigClassName() {
    return configClassName;
  }

  public void setConfigClassName(String configClassName) {
    this.configClassName.set(configClassName);
  }
}
