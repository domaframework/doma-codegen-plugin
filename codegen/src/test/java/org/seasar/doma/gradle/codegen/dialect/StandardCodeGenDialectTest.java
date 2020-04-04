package org.seasar.doma.gradle.codegen.dialect;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Types;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

public class StandardCodeGenDialectTest {

  @Test
  public void testReplacePropertyClassName() throws Exception {
    StandardCodeGenDialect dialect = new StandardCodeGenDialect();
    String localDateClassName = LocalDate.class.getName();
    String utilDateClassName = java.util.Date.class.getName();
    dialect.replacePropertyClassName(localDateClassName, utilDateClassName);
    assertEquals(utilDateClassName, dialect.classNameMap.get("date"));
    assertEquals(utilDateClassName, dialect.fallbackClassNameMap.get(Types.DATE));
  }

  @Test
  public void testConvertToTimeLiteral() throws Exception {
    StandardCodeGenDialect dialect = new StandardCodeGenDialect();
    assertEquals("'a'", dialect.convertToTimeLiteral("a"));
  }

  @Test
  public void testConvertToDateLiteral() throws Exception {
    StandardCodeGenDialect dialect = new StandardCodeGenDialect();
    assertEquals("'a'", dialect.convertToDateLiteral("a"));
  }

  @Test
  public void testConvertToTimestampLiteral() throws Exception {
    StandardCodeGenDialect dialect = new StandardCodeGenDialect();
    assertEquals("'a'", dialect.convertToTimestampLiteral("a"));
  }
}
