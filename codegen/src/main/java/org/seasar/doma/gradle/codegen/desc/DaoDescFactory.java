package org.seasar.doma.gradle.codegen.desc;

import org.seasar.doma.gradle.codegen.exception.CodeGenNullPointerException;
import org.seasar.doma.gradle.codegen.util.ClassUtil;
import org.seasar.doma.gradle.codegen.util.StringUtil;

public class DaoDescFactory {

  protected final String packageName;

  protected final String suffix;

  protected final String configClassName;

  protected final ClassDescSupport classDescSupport = new ClassDescSupport();

  public DaoDescFactory(String packageName, String suffix, String configClassName) {
    if (suffix == null) {
      throw new CodeGenNullPointerException("suffix");
    }
    this.packageName = packageName;
    this.suffix = suffix;
    this.configClassName = configClassName;
  }

  public DaoDesc createDaoDesc(EntityDesc entityDesc) {
    DaoDesc daoDesc = new DaoDesc();
    daoDesc.setPackageName(packageName);
    String entityPrefix = StringUtil.defaultString(entityDesc.getEntityPrefix(), "");
    String entitySuffix = StringUtil.defaultString(entityDesc.getEntitySuffix(), "");
    String simpleName = entityPrefix + entityDesc.getSimpleName() + entitySuffix + suffix;
    daoDesc.setSimpleName(simpleName);
    if (configClassName != null) {
      daoDesc.setConfigClassSimpleName(ClassUtil.getSimpleName(configClassName));
    }
    daoDesc.setEntityDesc(entityDesc);
    daoDesc.setTemplateName(Constants.DAO_TEMPLATE);
    handleImportName(daoDesc, entityDesc);
    return daoDesc;
  }

  protected void handleImportName(DaoDesc daoDesc, EntityDesc entityDesc) {
    classDescSupport.addImportName(daoDesc, ClassConstants.Dao);
    classDescSupport.addImportName(daoDesc, ClassConstants.Insert);
    classDescSupport.addImportName(daoDesc, ClassConstants.Update);
    classDescSupport.addImportName(daoDesc, ClassConstants.Delete);
    if (configClassName != null) {
      classDescSupport.addImportName(daoDesc, configClassName);
    }
    classDescSupport.addImportName(daoDesc, entityDesc.getQualifiedName());
    for (EntityPropertyDesc propertyDesc : entityDesc.getIdEntityPropertyDescs()) {
      classDescSupport.addImportName(daoDesc, propertyDesc.getPropertyClassName());
      classDescSupport.addImportName(daoDesc, ClassConstants.Select);
    }
    if (entityDesc.getIdEntityPropertyDescs().size() > 0) {
      classDescSupport.addImportName(daoDesc, ClassConstants.Select);
      for (EntityPropertyDesc propertyDesc : entityDesc.getIdEntityPropertyDescs()) {
        classDescSupport.addImportName(daoDesc, propertyDesc.getPropertyClassName());
      }
      if (entityDesc.getVersionEntityPropertyDesc() != null) {
        classDescSupport.addImportName(
            daoDesc, entityDesc.getVersionEntityPropertyDesc().getPropertyClassName());
      }
    }
  }
}
