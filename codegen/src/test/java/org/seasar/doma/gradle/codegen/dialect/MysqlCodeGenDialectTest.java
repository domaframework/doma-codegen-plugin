package org.seasar.doma.gradle.codegen.dialect;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.seasar.doma.gradle.codegen.meta.ColumnMeta;

public class MysqlCodeGenDialectTest {

  @ParameterizedTest
  @MethodSource
  public void theory(Fixture param) {
    MysqlCodeGenDialect sut = new MysqlCodeGenDialect();
    ColumnMeta columnMeta = new ColumnMeta();
    columnMeta.setTypeName(param.value);
    columnMeta.setLength(param.length);
    String actual = sut.getMappedPropertyClassName(columnMeta);

    assertEquals(param.expected, actual);
  }

  static Stream<Fixture> theory() {
    return Stream.of(
        // NUMBER
        new Fixture("bit", 1, "java.lang.Boolean"),
        new Fixture("bit", 2, "java.lang.Byte"),
        new Fixture("tinyint", 1, "java.lang.Boolean"),
        new Fixture("tinyint", 2, "java.lang.Byte"),
        new Fixture("bool", "java.lang.Boolean"),
        new Fixture("boolean", "java.lang.Boolean"),
        new Fixture("smallint", "java.lang.Short"),
        new Fixture("mediumint", "java.lang.Integer"),
        new Fixture("int", "java.lang.Integer"),
        new Fixture("integer", "java.lang.Integer"),
        new Fixture("bigint", "java.lang.Long"),
        new Fixture("serial", "java.math.BigInteger"),
        new Fixture("decimal", "java.math.BigDecimal"),
        new Fixture("dec", "java.math.BigDecimal"),
        new Fixture("float", "java.lang.Float"),
        new Fixture("double", "java.lang.Double"),
        new Fixture("double precision", "java.lang.Double"),
        new Fixture("tinyint unsigned", 1, "java.lang.Boolean"),
        new Fixture("tinyint unsigned", 2, "java.lang.Short"),
        new Fixture("smallint unsigned", "java.lang.Integer"),
        new Fixture("mediumint unsigned", "java.lang.Integer"),
        new Fixture("int unsigned", "java.lang.Long"),
        new Fixture("integer unsigned", "java.lang.Long"),
        new Fixture("bigint unsigned", "java.math.BigInteger"),
        new Fixture("decimal unsigned", "java.math.BigDecimal"),
        new Fixture("dec unsigned", "java.math.BigDecimal"),
        new Fixture("float unsigned", "java.lang.Float"),
        new Fixture("double unsigned", "java.lang.Double"),
        new Fixture("double precision unsigned", "java.lang.Double"),

        // DATE
        new Fixture("date", "java.time.LocalDate"),
        new Fixture("datetime", "java.time.LocalDateTime"),
        new Fixture("timestamp", "java.time.LocalDateTime"),
        new Fixture("time", "java.time.LocalTime"),
        new Fixture("year", "java.lang.Short"),

        // LOB
        new Fixture("tinyblob", "java.sql.Blob"),
        new Fixture("blob", "java.sql.Blob"),
        new Fixture("mediumblob", "java.sql.Blob"),
        new Fixture("longblob", "java.sql.Blob"),

        // BINARY and VARBINARY
        new Fixture("binary", ".byte[]"),
        new Fixture("varbinary", ".byte[]"));
  }

  static class Fixture {
    String value;
    int length;
    String expected;

    Fixture(String value, String expected) {
      this.value = value;
      this.expected = expected;
    }

    Fixture(String value, int length, String expected) {
      this.value = value;
      this.length = length;
      this.expected = expected;
    }
  }
}
