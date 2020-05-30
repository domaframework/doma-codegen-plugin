package org.seasar.doma.gradle.codegen.desc;

import java.util.LinkedHashMap;
import java.util.Map;
import org.seasar.doma.gradle.codegen.exception.CodeGenNullPointerException;
import org.seasar.doma.gradle.codegen.meta.ColumnMeta;
import org.seasar.doma.gradle.codegen.meta.TableMeta;
import org.seasar.doma.gradle.codegen.util.ClassUtil;
import org.seasar.doma.gradle.codegen.util.StringUtil;

public class EntityDescFactory {

  protected final String packageName;

  protected final EntityPropertyDescFactory entityPropertyDescFactory;

  protected final NamingType namingType;

  protected String originalStatesPropertyName;

  protected final boolean showCatalogName;

  protected final boolean showSchemaName;

  protected final boolean showTableName;

  protected final boolean showDbComment;

  protected final boolean useAccessor;

  protected final boolean useListener;

  protected final boolean useMetamodel;

  protected final boolean useMappedSuperclass;

  protected final Class<?> superclass;

  protected final EntityPropertyDescMerger entityPropertyDescMerger;

  protected final ClassDescSupport classDescSupport = new ClassDescSupport();

  public EntityDescFactory(
      String packageName,
      Class<?> superclass,
      EntityPropertyDescFactory entityPropertyDescFactory,
      NamingType namingType,
      String originalStatesPropertyName,
      boolean showCatalogName,
      boolean showSchemaName,
      boolean showTableName,
      boolean showDbComment,
      boolean useAccessor,
      boolean useListener,
      boolean useMetamodel,
      boolean useMappedSuperclass) {
    if (entityPropertyDescFactory == null) {
      throw new CodeGenNullPointerException("entityPropertyDescFactory");
    }
    if (namingType == null) {
      throw new CodeGenNullPointerException("namingType");
    }
    this.packageName = packageName;
    this.superclass = superclass;
    this.entityPropertyDescFactory = entityPropertyDescFactory;
    this.namingType = namingType;
    this.originalStatesPropertyName = originalStatesPropertyName;
    this.showCatalogName = showCatalogName;
    this.showSchemaName = showSchemaName;
    this.showTableName = showTableName;
    this.showDbComment = showDbComment;
    this.useAccessor = useAccessor;
    this.useListener = useListener;
    this.useMetamodel = useMetamodel;
    this.useMappedSuperclass = useMappedSuperclass;
    this.entityPropertyDescMerger = new EntityPropertyDescMerger(superclass);
  }

  public EntityDesc createEntityDesc(TableMeta tableMeta) {
    String name = StringUtil.fromSnakeCaseToCamelCase(tableMeta.getName());
    return createEntityDesc(tableMeta, null, null, StringUtil.capitalize(name));
  }

  public EntityDesc createEntityDesc(
      TableMeta tableMeta, String entityPrefix, String entitySuffix) {
    String name = StringUtil.fromSnakeCaseToCamelCase(tableMeta.getName());
    return createEntityDesc(tableMeta, entityPrefix, entitySuffix, StringUtil.capitalize(name));
  }

  public EntityDesc createEntityDesc(
      TableMeta tableMeta, String entityPrefix, String entitySuffix, String simpleName) {
    EntityDesc entityDesc = new EntityDesc();
    entityDesc.setNamingType(namingType);
    entityDesc.setOriginalStatesPropertyName(originalStatesPropertyName);
    entityDesc.setCatalogName(tableMeta.getCatalogName());
    entityDesc.setSchemaName(tableMeta.getSchemaName());
    entityDesc.setTableName(tableMeta.getName());
    entityDesc.setQualifiedTableName(tableMeta.getQualifiedTableName());
    entityDesc.setPackageName(packageName);
    String prefix = StringUtil.defaultString(entityPrefix, "");
    entityDesc.setEntityPrefix(prefix);
    String suffix = StringUtil.defaultString(entitySuffix, "");
    entityDesc.setEntitySuffix(suffix);
    entityDesc.setSimpleName(simpleName);
    if (useMappedSuperclass) {
      entityDesc.setSuperclassSimpleName(
          Constants.MAPPED_SUPER_CLASS_PREFIX + prefix + simpleName + suffix);
    } else if (superclass != null) {
      entityDesc.setSuperclassSimpleName(superclass.getSimpleName());
    }
    entityDesc.setListenerClassSimpleName(
        entityDesc.getEntityPrefix()
            + ClassUtil.getSimpleName(
                entityDesc.getSimpleName()
                    + entityDesc.getEntitySuffix()
                    + Constants.ENTITY_LISTENER_SUFFIX));
    entityDesc.setCompositeId(tableMeta.hasCompositePrimaryKey());
    entityDesc.setComment(tableMeta.getComment());
    entityDesc.setShowCatalogName(showCatalogName);
    entityDesc.setShowSchemaName(showSchemaName);
    entityDesc.setShowDbComment(true);
    entityDesc.setUseAccessor(useAccessor);
    entityDesc.setUseListener(useListener);
    entityDesc.setUseMetamodel(useMetamodel);
    entityDesc.setUseMappedSuperclass(useMappedSuperclass);
    entityDesc.setTemplateName(Constants.ENTITY_TEMPLATE);
    handleShowTableName(entityDesc, tableMeta);
    handleEntityPropertyDesc(entityDesc, tableMeta);
    handleImportName(entityDesc, tableMeta);
    return entityDesc;
  }

  protected void handleShowTableName(EntityDesc entityDesc, TableMeta tableMeta) {
    if (showTableName || isNameDifferentBetweenEntityAndTable(entityDesc)) {
      entityDesc.setShowTableName(true);
    }
  }

  protected boolean isNameDifferentBetweenEntityAndTable(EntityDesc entityDesc) {
    String entityPrefix = StringUtil.defaultString(entityDesc.getEntityPrefix(), "");
    String entitySuffix = StringUtil.defaultString(entityDesc.getEntitySuffix(), "");
    String entityName = entityPrefix + entityDesc.getSimpleName() + entitySuffix;
    String tableName = entityDesc.getTableName();
    return !tableName.equalsIgnoreCase(namingType.apply(entityName));
  }

  protected void handleEntityPropertyDesc(EntityDesc entityDesc, TableMeta tableMeta) {
    Map<String, EntityPropertyDesc> propertyDescMap =
        new LinkedHashMap<String, EntityPropertyDesc>();
    for (ColumnMeta columnMeta : tableMeta.getColumnMetas()) {
      EntityPropertyDesc propertyDesc =
          entityPropertyDescFactory.createEntityPropertyDesc(entityDesc, columnMeta);
      propertyDescMap.put(propertyDesc.getColumnName().toLowerCase(), propertyDesc);
    }
    entityPropertyDescMerger.merge(entityDesc, propertyDescMap);
    for (EntityPropertyDesc propertyDesc : propertyDescMap.values()) {
      entityDesc.addEntityPropertyDesc(propertyDesc);
    }
  }

  protected void handleImportName(EntityDesc entityDesc, TableMeta tableMeta) {
    classDescSupport.addImportName(entityDesc, ClassConstants.Entity);
    if (entityDesc.isUseMetamodel()) {
      classDescSupport.addImportName(entityDesc, ClassConstants.Metamodel);
    }
    if (entityDesc.getCatalogName() != null
        || entityDesc.getSchemaName() != null
        || entityDesc.getTableName() != null) {
      classDescSupport.addImportName(entityDesc, ClassConstants.Table);
    }
    if (!useMappedSuperclass && superclass != null) {
      classDescSupport.addImportName(entityDesc, superclass.getName());
    }
    if (namingType != NamingType.NONE) {
      classDescSupport.addImportName(entityDesc, namingType.getEnumConstant());
    }
    if (originalStatesPropertyName != null) {
      classDescSupport.addImportName(entityDesc, ClassConstants.OriginalStates);
    }
    for (EntityPropertyDesc propertyDesc : entityDesc.getOwnEntityPropertyDescs()) {
      if (propertyDesc.isId()) {
        classDescSupport.addImportName(entityDesc, ClassConstants.Id);
        if (propertyDesc.getGenerationType() != null) {
          classDescSupport.addImportName(entityDesc, ClassConstants.GeneratedValue);
          classDescSupport.addImportName(entityDesc, ClassConstants.GenerationType);
          GenerationType generationType = propertyDesc.getGenerationType();
          if (generationType != null) {
            classDescSupport.addImportName(entityDesc, generationType.getEnumConstant());
            if (generationType == GenerationType.SEQUENCE) {
              classDescSupport.addImportName(entityDesc, ClassConstants.SequenceGenerator);
            } else if (generationType == GenerationType.TABLE) {
              classDescSupport.addImportName(entityDesc, ClassConstants.TableGenerator);
            }
          }
        }
      }
      classDescSupport.addImportName(entityDesc, ClassConstants.Column);
      if (propertyDesc.isVersion()) {
        classDescSupport.addImportName(entityDesc, ClassConstants.Version);
      }
      classDescSupport.addImportName(entityDesc, propertyDesc.getPropertyClassName());
    }
  }
}
