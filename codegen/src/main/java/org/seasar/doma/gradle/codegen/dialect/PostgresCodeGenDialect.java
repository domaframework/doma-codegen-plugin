package org.seasar.doma.gradle.codegen.dialect;

import java.sql.Blob;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.seasar.doma.gradle.codegen.desc.ClassConstants;

public class PostgresCodeGenDialect extends StandardCodeGenDialect {

  public PostgresCodeGenDialect() {
    classNameMap.put("bigserial", Long.class.getName());
    classNameMap.put("bit", ClassConstants.bytes.getQualifiedName());
    classNameMap.put("bool", Boolean.class.getName());
    classNameMap.put("bpchar", String.class.getName());
    classNameMap.put("bytea", ClassConstants.bytes.getQualifiedName());
    classNameMap.put("float4", Float.class.getName());
    classNameMap.put("float8", Double.class.getName());
    classNameMap.put("int2", Short.class.getName());
    classNameMap.put("int4", Integer.class.getName());
    classNameMap.put("int8", Long.class.getName());
    classNameMap.put("money", Float.class.getName());
    classNameMap.put("oid", Blob.class.getName());
    classNameMap.put("serial", Integer.class.getName());
    classNameMap.put("text", String.class.getName());
    classNameMap.put("timestamptz", LocalDateTime.class.getName());
    classNameMap.put("timetz", LocalTime.class.getName());
    classNameMap.put("varbit", ClassConstants.bytes.getQualifiedName());
    classNameMap.put("varchar", String.class.getName());
  }

  @Override
  public String getName() {
    return "postgres";
  }

  @Override
  public String getDialectClassName() {
    return ClassConstants.PostgresDialect.getQualifiedName();
  }

  @Override
  public String getDefaultSchemaName(String userName) {
    return null;
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
