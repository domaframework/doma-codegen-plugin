package org.seasar.doma.gradle.codegen.desc;

import org.seasar.doma.gradle.codegen.util.ClassUtil;
import org.seasar.doma.gradle.codegen.util.StringUtil;

public class MappedSuperclassDescFactory {

  protected final String packageName;

  protected final String entitySuperclassName;

  protected final ClassDescSupport classDescSupport = new ClassDescSupport();

  public MappedSuperclassDescFactory(String packageName, String entitySuperclassName) {
    this.packageName = packageName;
    this.entitySuperclassName = entitySuperclassName;
  }

  public MappedSuperclassDesc createMappedSuperclassDesc(EntityDesc entityDesc) {
    MappedSuperclassDesc mappedSuperclassDesc = new MappedSuperclassDesc();
    mappedSuperclassDesc.setEntityDesc(entityDesc);
    mappedSuperclassDesc.setPackageName(entityDesc.getPackageName());
    String entityPrefix = StringUtil.defaultString(entityDesc.getEntityPrefix(), "");
    String entitySuffix = StringUtil.defaultString(entityDesc.getEntitySuffix(), "");
    String entityName = entityPrefix + entityDesc.getSimpleName() + entitySuffix;
    mappedSuperclassDesc.setSimpleName(Constants.MAPPED_SUPER_CLASS_PREFIX + entityName);
    mappedSuperclassDesc.setEntityClassSimpleName(entityDesc.getSimpleName());
    if (entitySuperclassName != null) {
      String simpleName = ClassUtil.getSimpleName(entitySuperclassName);
      mappedSuperclassDesc.setSuperclassSimpleName(simpleName);
    }
    mappedSuperclassDesc.setTemplateName(Constants.MAPPED_SUPERCLASS_TEMPLATE);
    handleImportName(mappedSuperclassDesc, entityDesc);
    return mappedSuperclassDesc;
  }

  protected void handleImportName(
      MappedSuperclassDesc mappedSuperclassDesc, EntityDesc entityDesc) {
    classDescSupport.addImportName(mappedSuperclassDesc, entityDesc.getQualifiedName());
    if (entitySuperclassName != null) {
      classDescSupport.addImportName(mappedSuperclassDesc, entitySuperclassName);
    }
  }
}
