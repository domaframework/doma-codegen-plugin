package org.seasar.doma.gradle.codegen.desc;

import org.seasar.doma.gradle.codegen.meta.ColumnMeta;

public class JavaClassResolver implements LanguageClassResolver {
  @Override
  public LanguageClass resolve(String javaClassName, ColumnMeta columnMeta) {
    return new LanguageClass(javaClassName, "null");
  }
}
