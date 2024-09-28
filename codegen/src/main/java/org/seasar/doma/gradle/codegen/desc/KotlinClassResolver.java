package org.seasar.doma.gradle.codegen.desc;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.seasar.doma.gradle.codegen.meta.ColumnMeta;

public class KotlinClassResolver implements LanguageClassResolver {

  @Override
  public LanguageClass resolve(String javaClassName, ColumnMeta columnMeta) {
    Objects.requireNonNull(javaClassName);
    Map<String, LanguageClass> map = createMap();
    LanguageClass value = map.get(javaClassName);
    if (value != null) {
      return value;
    }
    return new LanguageClass(javaClassName, "null");
  }

  protected Map<String, LanguageClass> createMap() {
    Map<String, LanguageClass> map = new HashMap<>();
    map.put(Byte.class.getName(), new LanguageClass("Byte", "-1"));
    map.put(Short.class.getName(), new LanguageClass("Short", "-1"));
    map.put(Integer.class.getName(), new LanguageClass("Int", "-1"));
    map.put(Long.class.getName(), new LanguageClass("Long", "-1L"));
    map.put(Float.class.getName(), new LanguageClass("Float", "-1f"));
    map.put(Double.class.getName(), new LanguageClass("Double", "-1.0"));
    return map;
  }
}
