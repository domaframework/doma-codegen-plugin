package org.seasar.doma.gradle.codegen.desc;

import java.util.List;

public class SqlTestDesc extends ClassDesc {

  protected String templateName;

  protected String dialectClassName = null;

  protected String driverClassName = null;

  protected String user = null;

  protected String password = null;

  protected String url = null;

  protected boolean abstrct;

  protected List<SqlTestMethodDesc> methodDescs;

  public String getTemplateName() {
    return templateName;
  }

  public void setTemplateName(String templateName) {
    this.templateName = templateName;
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public boolean isAbstrct() {
    return abstrct;
  }

  public void setAbstrct(boolean abstrct) {
    this.abstrct = abstrct;
  }

  public String getDialectClassName() {
    return dialectClassName;
  }

  public void setDialectClassName(String dialectClassName) {
    this.dialectClassName = dialectClassName;
  }

  public List<SqlTestMethodDesc> getMethodDescs() {
    return methodDescs;
  }

  public void setMethodDescs(List<SqlTestMethodDesc> methodDescs) {
    this.methodDescs = methodDescs;
  }
}
