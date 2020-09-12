package org.seasar.doma.gradle.codegen.desc;

import java.util.Objects;

public class LanguageClass {

  private final String className;
  private final String defaultValue;

  public LanguageClass(String className, String initialValue) {
    this.className = Objects.requireNonNull(className);
    this.defaultValue = Objects.requireNonNull(initialValue);
  }

  public String getClassName() {
    return className;
  }

  public String getDefaultValue() {
    return defaultValue;
  }
}
