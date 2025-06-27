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
  public void testInferDialectName_mysql_aws() throws Exception {
    String dialectName =
        JdbcUtil.inferDialectName(
            "jdbc:mysql:aws://db-identifier.cluster-XYZ.us-east-2.rds.amazonaws.com:3306");
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
  public void testInferDriverClassName_mysql_aws() throws Exception {
    String driverClassName =
        JdbcUtil.inferDriverClassName(
            "jdbc:mysql:aws://db-identifier.cluster-XYZ.us-east-2.rds.amazonaws.com:3306");
    assertEquals("software.aws.rds.jdbc.mysql.Driver", driverClassName);
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

  @Test
  public void testInferDialectName_testcontainers_postgresql() throws Exception {
    String dialectName = JdbcUtil.inferDialectName("jdbc:tc:postgresql:13:///test");
    assertEquals("postgres", dialectName);
  }

  @Test
  public void testInferDialectName_testcontainers_mysql() throws Exception {
    String dialectName = JdbcUtil.inferDialectName("jdbc:tc:mysql:8:///test");
    assertEquals("mysql", dialectName);
  }

  @Test
  public void testInferDialectName_testcontainers_mariadb() throws Exception {
    String dialectName = JdbcUtil.inferDialectName("jdbc:tc:mariadb:10.5:///test");
    assertEquals("mysql", dialectName);
  }

  @Test
  public void testInferDialectName_testcontainers_oracle() throws Exception {
    String dialectName = JdbcUtil.inferDialectName("jdbc:tc:oracle:21c:///test");
    assertEquals("oracle", dialectName);
  }

  @Test
  public void testInferDialectName_testcontainers_sqlserver() throws Exception {
    String dialectName = JdbcUtil.inferDialectName("jdbc:tc:sqlserver:2019:///test");
    assertEquals("mssql", dialectName);
  }

  @Test
  public void testInferDialectName_testcontainers_db2() throws Exception {
    String dialectName = JdbcUtil.inferDialectName("jdbc:tc:db2:11.5:///test");
    assertEquals("db2", dialectName);
  }

  @Test
  public void testInferDriverClassName_testcontainers_postgresql() throws Exception {
    String driverClassName = JdbcUtil.inferDriverClassName("jdbc:tc:postgresql:13:///test");
    assertEquals("org.testcontainers.jdbc.ContainerDatabaseDriver", driverClassName);
  }

  @Test
  public void testInferDriverClassName_testcontainers_mysql() throws Exception {
    String driverClassName = JdbcUtil.inferDriverClassName("jdbc:tc:mysql:8:///test");
    assertEquals("org.testcontainers.jdbc.ContainerDatabaseDriver", driverClassName);
  }

  @Test
  public void testInferDriverClassName_testcontainers_mariadb() throws Exception {
    String driverClassName = JdbcUtil.inferDriverClassName("jdbc:tc:mariadb:10.5:///test");
    assertEquals("org.testcontainers.jdbc.ContainerDatabaseDriver", driverClassName);
  }

  @Test
  public void testInferDriverClassName_testcontainers_oracle() throws Exception {
    String driverClassName = JdbcUtil.inferDriverClassName("jdbc:tc:oracle:21c:///test");
    assertEquals("org.testcontainers.jdbc.ContainerDatabaseDriver", driverClassName);
  }

  @Test
  public void testInferDriverClassName_testcontainers_sqlserver() throws Exception {
    String driverClassName = JdbcUtil.inferDriverClassName("jdbc:tc:sqlserver:2019:///test");
    assertEquals("org.testcontainers.jdbc.ContainerDatabaseDriver", driverClassName);
  }

  @Test
  public void testInferDriverClassName_testcontainers_db2() throws Exception {
    String driverClassName = JdbcUtil.inferDriverClassName("jdbc:tc:db2:11.5:///test");
    assertEquals("org.testcontainers.jdbc.ContainerDatabaseDriver", driverClassName);
  }
}
