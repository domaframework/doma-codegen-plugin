package org.seasar.doma.gradle.codegen.desc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;
import org.seasar.doma.gradle.codegen.util.ResourceUtil;

public class EntityPropertyClassNameResolverTest {

  @Test
  public void testResolve2() throws Exception {
    EntityDesc entityDesc = new EntityDesc();
    entityDesc.setPackageName("aaa");
    entityDesc.setSimpleName("Bbb");

    EntityPropertyClassNameResolver resolver = new EntityPropertyClassNameResolver(null);
    resolver.patternMap.put(Pattern.compile("aaa\\.Bbb@ccc"), "String");
    assertEquals("String", resolver.resolve(entityDesc, "ccc", "Integer"));
    assertEquals("Integer", resolver.resolve(entityDesc, "ddd", "Integer"));
  }

  @Test
  public void testResolve() throws Exception {
    String path = getClass().getName().replace('.', '/') + ".properties";
    File file = ResourceUtil.getResourceAsFile(path);
    EntityPropertyClassNameResolver resolver = new EntityPropertyClassNameResolver(file);

    EntityDesc entityDesc = new EntityDesc();
    entityDesc.setPackageName("aaa");
    entityDesc.setSimpleName("Bbb");
    assertEquals("java.lang.String", resolver.resolve(entityDesc, "ccc", "java.lang.Integer"));
    assertEquals("java.lang.Long", resolver.resolve(entityDesc, "ddd", "java.lang.Integer"));

    entityDesc = new EntityDesc();
    entityDesc.setPackageName("xxx");
    entityDesc.setSimpleName("Yyy");
    assertEquals("java.lang.Double", resolver.resolve(entityDesc, "ccc", "java.lang.Integer"));
  }
}
