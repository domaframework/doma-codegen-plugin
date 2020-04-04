package org.seasar.doma.gradle.codegen.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class StringUtilTest {

  @Test
  public void testCapitalize() throws Exception {
    assertEquals("Aaa", StringUtil.capitalize("aaa"));
  }

  @Test
  public void testDecapitalize() throws Exception {
    assertEquals("aAA", StringUtil.decapitalize("AAA"));
  }

  @Test
  public void testFromSnakeCaseToCamelCase() throws Exception {
    assertEquals("aaaBbb", StringUtil.fromSnakeCaseToCamelCase("AAA_BBB"));
  }

  @Test
  public void testFromCamelCaseToSnakeCase() throws Exception {
    assertEquals("aaa_bbb", StringUtil.fromCamelCaseToSnakeCase("aaaBbb"));
  }

  @Test
  public void testIsNullOrEmpty() throws Exception {
    assertTrue(StringUtil.isNullOrEmpty(null));
    assertTrue(StringUtil.isNullOrEmpty(""));
    assertFalse(StringUtil.isNullOrEmpty(" "));
  }
}
