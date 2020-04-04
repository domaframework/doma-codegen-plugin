package org.seasar.doma.gradle.codegen.dialect;

import org.seasar.doma.gradle.codegen.desc.ClassConstants;

public class HsqldbCodeGenDialect extends StandardCodeGenDialect {

  public HsqldbCodeGenDialect() {
    classNameMap.put("int", Integer.class.getName());
    classNameMap.put("varchar_ignorecase", String.class.getName());
  }

  @Override
  public String getName() {
    return "hsqldb";
  }

  @Override
  public String getDialectClassName() {
    return ClassConstants.HsqldbDialect.getQualifiedName();
  }

  @Override
  public String getDefaultSchemaName(String userName) {
    return null;
  }

  @Override
  public boolean isJdbcCommentUnavailable() {
    return true;
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
