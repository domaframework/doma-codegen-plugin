package org.seasar.doma.gradle.codegen.desc;

import org.seasar.doma.gradle.codegen.util.ClassUtil;
import org.seasar.doma.gradle.codegen.util.StringUtil;

public class EntityListenerDescFactory {

  protected final String packageName;

  protected final String superclassName;

  protected final ClassDescSupport classDescSupport = new ClassDescSupport();

  public EntityListenerDescFactory(String packageName, String superclassName) {
    this.packageName = packageName;
    this.superclassName = superclassName;
  }

  public EntityListenerDesc createEntityListenerDesc(EntityDesc entityDesc) {
    EntityListenerDesc entityListenerDesc = new EntityListenerDesc();
    entityListenerDesc.setEntityDesc(entityDesc);
    entityListenerDesc.setPackageName(entityDesc.getPackageName());
    String entityPrefix = StringUtil.defaultString(entityDesc.getEntityPrefix(), "");
    String entitySuffix = StringUtil.defaultString(entityDesc.getEntitySuffix(), "");
    String entityName = entityPrefix + entityDesc.getSimpleName() + entitySuffix;
    entityListenerDesc.setSimpleName(entityName + Constants.ENTITY_LISTENER_SUFFIX);
    entityListenerDesc.setEntityClassSimpleName(entityDesc.getSimpleName());
    if (superclassName != null) {
      entityListenerDesc.setSuperclassSimpleName(ClassUtil.getSimpleName(superclassName));
    }
    entityListenerDesc.setTemplateName(Constants.ENTITY_LISTENER_TEMPLATE);
    handleImportName(entityListenerDesc, entityDesc);
    return entityListenerDesc;
  }

  protected void handleImportName(EntityListenerDesc entityListenerDesc, EntityDesc entityDesc) {
    classDescSupport.addImportName(entityListenerDesc, entityDesc.getQualifiedName());
    if (superclassName == null) {
      classDescSupport.addImportName(entityListenerDesc, ClassConstants.EntityListener);
      classDescSupport.addImportName(entityListenerDesc, ClassConstants.PreInsertContext);
      classDescSupport.addImportName(entityListenerDesc, ClassConstants.PreUpdateContext);
      classDescSupport.addImportName(entityListenerDesc, ClassConstants.PreDeleteContext);
      classDescSupport.addImportName(entityListenerDesc, ClassConstants.PostInsertContext);
      classDescSupport.addImportName(entityListenerDesc, ClassConstants.PostUpdateContext);
      classDescSupport.addImportName(entityListenerDesc, ClassConstants.PostDeleteContext);
    } else {
      classDescSupport.addImportName(entityListenerDesc, superclassName);
    }
  }
}
