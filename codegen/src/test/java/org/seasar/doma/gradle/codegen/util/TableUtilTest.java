package org.seasar.doma.gradle.codegen.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.function.Function;
import org.junit.jupiter.api.Test;

public class TableUtilTest {

  @Test
  public void testGetQualifiedTableName() {
    assertEquals("aaa.bbb.ccc", TableUtil.getQualifiedTableName("aaa", "bbb", "ccc"));
    assertEquals("bbb.ccc", TableUtil.getQualifiedTableName(null, "bbb", "ccc"));
    assertEquals("aaa.ccc", TableUtil.getQualifiedTableName("aaa", null, "ccc"));
    assertEquals("ccc", TableUtil.getQualifiedTableName(null, null, "ccc"));
  }

  @Test
  public void testGetEnquoteQualifiedTableName() {
    Function<String, String> enquote = name -> "[" + name + "]";
    assertEquals(
        "[aaa].[bbb].[ccc]", TableUtil.getEnquoteQualifiedTableName(enquote, "aaa", "bbb", "ccc"));
    assertEquals(
        "[bbb].[ccc]", TableUtil.getEnquoteQualifiedTableName(enquote, null, "bbb", "ccc"));
    assertEquals(
        "[aaa].[ccc]", TableUtil.getEnquoteQualifiedTableName(enquote, "aaa", null, "ccc"));
    assertEquals("[ccc]", TableUtil.getEnquoteQualifiedTableName(enquote, null, null, "ccc"));
  }
}
