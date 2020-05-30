package org.seasar.doma.gradle.codegen.desc;

public class MappedSuperclassDesc extends ClassDesc {

  protected String entityClassSimpleName;

  protected String superclassSimpleName;

  protected String mappedSuperclassSimpleName;

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

  public String getMappedSuperclassSimpleName() {
    return mappedSuperclassSimpleName;
  }

  public void setMappedSuperclassSimpleName(String mappedSuperclassSimpleName) {
    this.mappedSuperclassSimpleName = mappedSuperclassSimpleName;
  }

  public EntityDesc getEntityDesc() {
    return entityDesc;
  }

  public void setEntityDesc(EntityDesc entityDesc) {
    this.entityDesc = entityDesc;
  }
}
