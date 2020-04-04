package org.seasar.doma.gradle.codegen.desc;

public class DaoDesc extends ClassDesc {

  protected String configClassSimpleName;

  protected EntityDesc entityDesc;

  protected String templateName;

  public String getConfigClassSimpleName() {
    return configClassSimpleName;
  }

  public void setConfigClassSimpleName(String configClassSimpleName) {
    this.configClassSimpleName = configClassSimpleName;
  }

  public EntityDesc getEntityDesc() {
    return entityDesc;
  }

  public void setEntityDesc(EntityDesc entityDesc) {
    this.entityDesc = entityDesc;
  }

  public void setTemplateName(String templateName) {
    this.templateName = templateName;
  }

  public String getTemplateName() {
    return templateName;
  }
}
