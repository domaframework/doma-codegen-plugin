package org.seasar.doma.gradle.codegen.dialect;

import org.seasar.doma.gradle.codegen.desc.ClassConstants;

public class H2CodeGenDialect extends StandardCodeGenDialect {

  public H2CodeGenDialect() {}

  @Override
  public String getName() {
    return "h2";
  }

  @Override
  public String getDialectClassName() {
    return ClassConstants.H2Dialect.getQualifiedName();
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
