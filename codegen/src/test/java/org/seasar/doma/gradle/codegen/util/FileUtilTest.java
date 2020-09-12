package org.seasar.doma.gradle.codegen.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import org.junit.jupiter.api.Test;
import org.seasar.doma.gradle.codegen.desc.LanguageType;

public class FileUtilTest {

  @Test
  public void testCreateJavaFile() throws Exception {
    File file = FileUtil.createFile(LanguageType.JAVA, new File("."), "aaa.bbb.Ccc");
    assertEquals("Ccc.java", file.getName());
  }
}
