package org.seasar.doma.gradle.codegen.dialect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.seasar.doma.gradle.codegen.util.AssertionUtil;

public class CodeGenDialectRegistry {

  protected static final Map<String, CodeGenDialect> genDialectMap =
      Collections.synchronizedMap(new HashMap<String, CodeGenDialect>());

  static {
    register(new StandardCodeGenDialect());
    register(new HsqldbCodeGenDialect());
    register(new H2CodeGenDialect());
    register(new MysqlCodeGenDialect());
    register(new PostgresCodeGenDialect());
    register(new Oracle11CodeGenDialect());
    register(new OracleCodeGenDialect());
    register(new Db2CodeGenDialect());
    register(new Mssql2008CodeGenDialect());
    register(new MssqlCodeGenDialect());
  }

  public static void register(CodeGenDialect dialect) {
    AssertionUtil.assertNotNull(dialect);
    genDialectMap.put(dialect.getName(), dialect);
  }

  public static CodeGenDialect lookup(String dialectName) {
    return genDialectMap.get(dialectName);
  }

  public static List<String> getDialectNames() {
    return new ArrayList<String>(genDialectMap.keySet());
  }
}
