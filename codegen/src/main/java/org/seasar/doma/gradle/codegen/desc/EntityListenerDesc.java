package org.seasar.doma.gradle.codegen.desc;

public class EntityListenerDesc extends ClassDesc {

  protected String entityClassSimpleName;

  protected String superclassSimpleName;

  protected String listenerClassSimpleName;

  protected EntityDesc entityDesc;

  protected String templateName;

  public void setTemplateName(String templateName) {
    this.templateName = templateName;
  }

  public String getTemplateName() {
    return templateName;
  }

  public String getEntityClassSimpleName() {
    return entityClassSimpleName;
  }

  public void setEntityClassSimpleName(String entityClassSimpleName) {
    this.entityClassSimpleName = entityClassSimpleName;
  }

  public String getSuperclassSimpleName() {
    return superclassSimpleName;
  }

  public void setSuperclassSimpleName(String superclassSimpleName) {
    this.superclassSimpleName = superclassSimpleName;
  }

  public String getListenerClassSimpleName() {
    return listenerClassSimpleName;
  }

  public void setListenerClassSimpleName(String listenerClassSimpleName) {
    this.listenerClassSimpleName = listenerClassSimpleName;
  }

  public EntityDesc getEntityDesc() {
    return entityDesc;
  }

  public void setEntityDesc(EntityDesc entityDesc) {
    this.entityDesc = entityDesc;
  }
}
