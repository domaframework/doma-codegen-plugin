package org.seasar.doma.gradle.codegen.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import org.junit.jupiter.api.Test;

public class FileUtilTest {

  @Test
  public void testCreateJavaFile() throws Exception {
    File file = FileUtil.createJavaFile(new File("."), "aaa.bbb.Ccc");
    assertEquals("Ccc.java", file.getName());
  }
}
