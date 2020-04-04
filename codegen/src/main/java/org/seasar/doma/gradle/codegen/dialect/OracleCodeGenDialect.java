package org.seasar.doma.gradle.codegen.dialect;

import org.seasar.doma.gradle.codegen.desc.ClassConstants;

public class OracleCodeGenDialect extends Oracle11CodeGenDialect {

  @Override
  public String getName() {
    return "oracle";
  }

  @Override
  public String getDialectClassName() {
    return ClassConstants.OracleDialect.getQualifiedName();
  }

  @Override
  public boolean supportsIdentity() {
    return true;
  }
}
