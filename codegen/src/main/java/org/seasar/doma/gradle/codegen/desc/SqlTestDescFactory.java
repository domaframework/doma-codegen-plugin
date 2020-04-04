package org.seasar.doma.gradle.codegen.desc;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.seasar.doma.gradle.codegen.exception.CodeGenNullPointerException;
import org.seasar.doma.gradle.codegen.util.ClassUtil;

public class SqlTestDescFactory {

  protected final String dialectClassName;

  protected final String url;

  protected final String user;

  protected final String password;

  protected final Set<File> sqlFiles = new TreeSet<File>();

  protected final ClassDescSupport classDescSupport = new ClassDescSupport();

  public SqlTestDescFactory(String dialectClassName, String url, String user, String password) {
    if (dialectClassName == null) {
      throw new CodeGenNullPointerException("dialectClassName");
    }
    if (url == null) {
      throw new CodeGenNullPointerException("url");
    }
    if (user == null) {
      throw new CodeGenNullPointerException("user");
    }
    if (password == null) {
      throw new CodeGenNullPointerException("password");
    }
    if (sqlFiles == null) {
      throw new CodeGenNullPointerException("sqlFiles");
    }
    this.dialectClassName = dialectClassName;
    this.url = url;
    this.user = user;
    this.password = password;
    this.sqlFiles.addAll(sqlFiles);
  }

  public SqlTestDesc createSqlFileTestDesc(String className, List<SqlTestMethodDesc> methodDescs) {
    SqlTestDesc sqlTestDesc = new SqlTestDesc();
    sqlTestDesc.setPackageName(ClassUtil.getPackageName(className));
    sqlTestDesc.setSimpleName(ClassUtil.getSimpleName(className));
    sqlTestDesc.setAbstrct(false);
    sqlTestDesc.setDialectClassName(dialectClassName);
    sqlTestDesc.setUrl(url);
    sqlTestDesc.setUser(user);
    sqlTestDesc.setPassword(password);
    sqlTestDesc.setTemplateName(Constants.SQL_TEST_CASE_TEMPLATE);
    sqlTestDesc.setMethodDescs(methodDescs);
    return sqlTestDesc;
  }
}
