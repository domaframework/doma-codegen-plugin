package org.seasar.doma.gradle.codegen.extension;

import javax.inject.Inject;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Internal;

public class SqlConfig {

  private final Property<Boolean> overwrite;

  @Inject
  public SqlConfig(ObjectFactory objects) {
    overwrite = objects.property(Boolean.class);
    overwrite.set(true);
  }

  @Internal
  public Property<Boolean> getOverwrite() {
    return overwrite;
  }

  public void setOverwrite(boolean overwrite) {
    this.overwrite.set(overwrite);
  }
}
