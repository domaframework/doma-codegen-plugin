package org.seasar.doma.gradle.codegen.desc;

import org.seasar.doma.gradle.codegen.meta.ColumnMeta;

public interface LanguageClassResolver {

  LanguageClass resolve(String javaClassName, ColumnMeta columnMeta);
}
