package org.seasar.doma.gradle.codegen.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class JdbcUtilTest {

  @Test
  public void testInferDialectName() throws Exception {
    String dialectName = JdbcUtil.inferDialectName("jdbc:postgresql://localhost/hoge");
    assertEquals("postgres", dialectName);
  }

  @Test
  public void testInferDialectName_mysql() throws Exception {
    String dialectName = JdbcUtil.inferDialectName("jdbc:mysql://localhost:3306/hoge");
    assertEquals("mysql", dialectName);
  }

  @Test
  public void testInferDialectName_unknown() throws Exception {
    String dialectName = JdbcUtil.inferDialectName("jdbc:unknown://localhost/hoge");
    assertNull(dialectName);
  }

  @Test
  public void testInferDialectName_invalidUrl() throws Exception {
    String dialectName = JdbcUtil.inferDialectName("localhost/hoge");
    assertNull(dialectName);
  }

  @Test
  public void testInferDriverClassName() throws Exception {
    String driverClassName = JdbcUtil.inferDriverClassName("jdbc:postgresql://localhost/hoge");
    assertEquals("org.postgresql.Driver", driverClassName);
  }

  @Test
  public void testInferDriverClassName_mysql() throws Exception {
    String driverClassName = JdbcUtil.inferDriverClassName("jdbc:mysql://localhost:3306/hoge");
    assertEquals("com.mysql.cj.jdbc.Driver", driverClassName);
  }

  @Test
  public void testInferDriverClassName_mariadb() throws Exception {
    String driverClassName = JdbcUtil.inferDriverClassName("jdbc:mariadb://localhost:3306/hoge");
    assertEquals("org.mariadb.jdbc.Driver", driverClassName);
  }

  @Test
  public void testInferDriverClassName_unknown() throws Exception {
    String driverClassName = JdbcUtil.inferDriverClassName("jdbc:unknown://localhost/hoge");
    assertNull(driverClassName);
  }

  @Test
  public void testInferDriverClassName_invalidUrl() throws Exception {
    String driverClassName = JdbcUtil.inferDriverClassName("localhost/hoge");
    assertNull(driverClassName);
  }
}
