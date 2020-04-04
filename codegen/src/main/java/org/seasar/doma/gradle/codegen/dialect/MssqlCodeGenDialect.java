package org.seasar.doma.gradle.codegen.dialect;

import org.seasar.doma.gradle.codegen.desc.ClassConstants;

public class MssqlCodeGenDialect extends Mssql2008CodeGenDialect {

  public MssqlCodeGenDialect() {}

  @Override
  public String getName() {
    return "mssql";
  }

  @Override
  public String getDialectClassName() {
    return ClassConstants.Mssql.getQualifiedName();
  }

  @Override
  public boolean supportsSequence() {
    return true;
  }
}
