package org.seasar.doma.gradle.codegen.desc;

import java.util.ArrayList;
import java.util.List;
import org.seasar.doma.gradle.codegen.util.StringUtil;

public class EntityDesc extends ClassDesc {

  protected String catalogName;

  protected String schemaName;

  protected String tableName;

  protected String qualifiedTableName;

  protected String entityPrefix;

  protected String entitySuffix;

  protected String superclassSimpleName;

  protected String listenerClassSimpleName;

  protected NamingType namingType;

  protected String originalStatesPropertyName;

  protected boolean compositeId;

  protected boolean showCatalogName;

  protected boolean showSchemaName;

  protected boolean showTableName;

  protected boolean useAccessor;

  protected boolean useListener;

  protected boolean useMetamodel;

  protected boolean useMappedSuperclass;

  protected boolean showDbComment;

  protected String templateName;

  protected final List<EntityPropertyDesc> entityPropertyDescs =
      new ArrayList<EntityPropertyDesc>();

  protected final List<EntityPropertyDesc> ownEntityPropertyDescs =
      new ArrayList<EntityPropertyDesc>();

  protected final List<EntityPropertyDesc> idEntityPropertyDescs =
      new ArrayList<EntityPropertyDesc>();

  protected EntityPropertyDesc versionEntityPropertyDesc;

  public void setTemplateName(String templateName) {
    this.templateName = templateName;
  }

  public String getTemplateName() {
    return templateName;
  }

  public void setCatalogName(String catalogName) {
    this.catalogName = catalogName;
  }

  public String getCatalogName() {
    return catalogName;
  }

  public void setSchemaName(String schemaName) {
    this.schemaName = schemaName;
  }

  public String getSchemaName() {
    return schemaName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  public String getTableName() {
    return tableName;
  }

  public String getEntityPrefix() {
    return entityPrefix;
  }

  public void setEntityPrefix(String entityPrefix) {
    this.entityPrefix = entityPrefix;
  }

  public String getEntitySuffix() {
    return entitySuffix;
  }

  public void setEntitySuffix(String entitySuffix) {
    this.entitySuffix = entitySuffix;
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

  public NamingType getNamingType() {
    return namingType;
  }

  public void setNamingType(NamingType namingType) {
    this.namingType = namingType;
  }

  public String getOriginalStatesPropertyName() {
    return originalStatesPropertyName;
  }

  public void setOriginalStatesPropertyName(String originalStatesPropertyName) {
    this.originalStatesPropertyName = originalStatesPropertyName;
  }

  public void setCompositeId(boolean compositeId) {
    this.compositeId = compositeId;
  }

  public boolean isCompositeId() {
    return compositeId;
  }

  public boolean isShowCatalogName() {
    return showCatalogName;
  }

  public void setShowCatalogName(boolean showCatalogName) {
    this.showCatalogName = showCatalogName;
  }

  public boolean isShowSchemaName() {
    return showSchemaName;
  }

  public void setShowSchemaName(boolean showSchemaName) {
    this.showSchemaName = showSchemaName;
  }

  public boolean isShowTableName() {
    return showTableName;
  }

  public void setShowTableName(boolean showTableName) {
    this.showTableName = showTableName;
  }

  public boolean isUseAccessor() {
    return useAccessor;
  }

  public void setUseAccessor(boolean useAccessor) {
    this.useAccessor = useAccessor;
  }

  public boolean isUseListener() {
    return useListener;
  }

  public void setUseListener(boolean useListener) {
    this.useListener = useListener;
  }

  public boolean isUseMetamodel() {
    return useMetamodel;
  }

  public void setUseMetamodel(boolean useMetamodel) {
    this.useMetamodel = useMetamodel;
  }

  public boolean isUseMappedSuperclass() {
    return useMappedSuperclass;
  }

  public void setUseMappedSuperclass(boolean useMappedSuperclass) {
    this.useMappedSuperclass = useMappedSuperclass;
  }

  public boolean isShowDbComment() {
    return showDbComment;
  }

  public void setShowDbComment(boolean showDbComment) {
    this.showDbComment = showDbComment;
  }

  public void addEntityPropertyDesc(EntityPropertyDesc entityPropertyDesc) {
    entityPropertyDescs.add(entityPropertyDesc);
    if (getQualifiedName().equals(entityPropertyDesc.getEntityClassName())) {
      ownEntityPropertyDescs.add(entityPropertyDesc);
    }
    if (entityPropertyDesc.isId()) {
      idEntityPropertyDescs.add(entityPropertyDesc);
    }
    if (entityPropertyDesc.isVersion()) {
      versionEntityPropertyDesc = entityPropertyDesc;
    }
  }

  public List<EntityPropertyDesc> getEntityPropertyDescs() {
    return entityPropertyDescs;
  }

  public List<EntityPropertyDesc> getOwnEntityPropertyDescs() {
    return ownEntityPropertyDescs;
  }

  public List<EntityPropertyDesc> getIdEntityPropertyDescs() {
    return idEntityPropertyDescs;
  }

  public EntityPropertyDesc getVersionEntityPropertyDesc() {
    return versionEntityPropertyDesc;
  }

  public String getQualifiedTableName() {
    return qualifiedTableName;
  }

  public void setQualifiedTableName(String qualifiedTableName) {
    this.qualifiedTableName = qualifiedTableName;
  }

  @Override
  public String getQualifiedName() {
    String prefix = StringUtil.defaultString(entityPrefix, "");
    String suffix = StringUtil.defaultString(entitySuffix, "");

    if (packageName == null || packageName.isEmpty()) {
      return prefix + simpleName + suffix;
    }

    return packageName + "." + prefix + simpleName + suffix;
  }
}
