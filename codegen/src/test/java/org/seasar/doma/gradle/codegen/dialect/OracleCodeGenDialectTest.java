package org.seasar.doma.gradle.codegen.dialect;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class OracleCodeGenDialectTest {

  @Test
  public void testConvertToTimeLiteral() throws Exception {
    OracleCodeGenDialect dialect = new OracleCodeGenDialect();
    assertEquals("time'a'", dialect.convertToTimeLiteral("a"));
  }

  @Test
  public void testConvertToDateLiteral() throws Exception {
    OracleCodeGenDialect dialect = new OracleCodeGenDialect();
    assertEquals("date'a'", dialect.convertToDateLiteral("a"));
  }

  @Test
  public void testConvertToTimestampLiteral() throws Exception {
    OracleCodeGenDialect dialect = new OracleCodeGenDialect();
    assertEquals("timestamp'a'", dialect.convertToTimestampLiteral("a"));
  }
}
