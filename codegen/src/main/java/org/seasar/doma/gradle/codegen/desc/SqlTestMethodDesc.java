package org.seasar.doma.gradle.codegen.desc;

public class SqlTestMethodDesc {

  protected String methodName;

  protected String path;

  public SqlTestMethodDesc(String methodName, String path) {
    this.methodName = methodName;
    this.path = path;
  }

  public String getMethodName() {
    return methodName;
  }

  public void setMethodName(String methodName) {
    this.methodName = methodName;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }
}
