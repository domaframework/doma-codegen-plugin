package org.seasar.doma.gradle.codegen.dialect;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.seasar.doma.gradle.codegen.desc.ClassConstants;
import org.seasar.doma.gradle.codegen.meta.ColumnMeta;

public class MysqlCodeGenDialect extends StandardCodeGenDialect {

  public MysqlCodeGenDialect() {
    classNameMap.put("bool", Boolean.class.getName());
    classNameMap.put("boolean", Boolean.class.getName());
    classNameMap.put("smallint", Short.class.getName());
    classNameMap.put("smallint unsigned", Integer.class.getName());
    classNameMap.put("mediumint", Integer.class.getName());
    classNameMap.put("mediumint unsigned", Integer.class.getName());
    classNameMap.put("int", Integer.class.getName());
    classNameMap.put("int unsigned", Long.class.getName());
    classNameMap.put("integer", Integer.class.getName());
    classNameMap.put("integer unsigned", Long.class.getName());
    classNameMap.put("bigint", Long.class.getName());
    classNameMap.put("bigint unsigned", BigInteger.class.getName());
    classNameMap.put("serial", BigInteger.class.getName());
    classNameMap.put("decimal", BigDecimal.class.getName());
    classNameMap.put("decimal unsigned", BigDecimal.class.getName());
    classNameMap.put("dec", BigDecimal.class.getName());
    classNameMap.put("dec unsigned", BigDecimal.class.getName());
    classNameMap.put("float", Float.class.getName());
    classNameMap.put("float unsigned", Float.class.getName());
    classNameMap.put("double", Double.class.getName());
    classNameMap.put("double unsigned", Double.class.getName());
    classNameMap.put("double precision", Double.class.getName());
    classNameMap.put("double precision unsigned", Double.class.getName());

    classNameMap.put("date", LocalDate.class.getName());
    classNameMap.put("datetime", LocalDateTime.class.getName());
    classNameMap.put("timestamp", LocalDateTime.class.getName());
    classNameMap.put("time", LocalTime.class.getName());
    classNameMap.put("year", Short.class.getName());

    classNameMap.put("tinyblob", Blob.class.getName());
    classNameMap.put("blob", Blob.class.getName());
    classNameMap.put("mediumblob", Blob.class.getName());
    classNameMap.put("longblob", Blob.class.getName());

    classNameMap.put("binary", ClassConstants.bytes.getQualifiedName());
    classNameMap.put("varbinary", ClassConstants.bytes.getQualifiedName());
  }

  @Override
  public String getName() {
    return "mysql";
  }

  @Override
  public String getDialectClassName() {
    return ClassConstants.MysqlDialect.getQualifiedName();
  }

  @Override
  public String getMappedPropertyClassName(ColumnMeta columnMeta) {
    if ("bit".equals(columnMeta.getTypeName()) || "tinyint".equals(columnMeta.getTypeName())) {
      return columnMeta.getLength() <= 1 ? Boolean.class.getName() : Byte.class.getName();
    }
    if ("tinyint unsigned".equals(columnMeta.getTypeName())) {
      return columnMeta.getLength() <= 1 ? Boolean.class.getName() : Short.class.getName();
    }
    return super.getMappedPropertyClassName(columnMeta);
  }

  @Override
  public boolean supportsIdentity() {
    return true;
  }

  @Override
  public boolean supportsSequence() {
    return true;
  }
}
