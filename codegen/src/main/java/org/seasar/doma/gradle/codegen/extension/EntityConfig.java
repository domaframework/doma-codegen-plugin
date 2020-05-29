package org.seasar.doma.gradle.codegen.extension;

import java.io.File;
import javax.inject.Inject;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Internal;
import org.seasar.doma.gradle.codegen.desc.GenerationType;
import org.seasar.doma.gradle.codegen.desc.NamingType;

public class EntityConfig {

  private final Property<Boolean> overwrite;

  private final Property<Boolean> overwriteListener;

  private final Property<String> superclassName;

  private final Property<String> listenerSuperclassName;

  private final Property<String> packageName;

  private final Property<GenerationType> generationType;

  private final Property<NamingType> namingType;

  private final Property<Long> initialValue;

  private final Property<Long> allocationSize;

  private final Property<Boolean> showCatalogName;

  private final Property<Boolean> showSchemaName;

  private final Property<Boolean> showTableName;

  private final Property<Boolean> showColumnName;

  private final Property<Boolean> showDbComment;

  private final Property<Boolean> useAccessor;

  protected Property<Boolean> useListener;

  protected Property<Boolean> useMetamodel;

  protected Property<String> originalStatesPropertyName;

  protected RegularFileProperty entityPropertyClassNamesFile;

  protected Property<String> prefix;

  protected Property<String> suffix;

  @Inject
  public EntityConfig(ObjectFactory objects) {
    overwrite = objects.property(Boolean.class);
    overwriteListener = objects.property(Boolean.class);
    superclassName = objects.property(String.class);
    listenerSuperclassName = objects.property(String.class);
    packageName = objects.property(String.class);
    generationType = objects.property(GenerationType.class);
    namingType = objects.property(NamingType.class);
    initialValue = objects.property(Long.class);
    allocationSize = objects.property(Long.class);
    showCatalogName = objects.property(Boolean.class);
    showSchemaName = objects.property(Boolean.class);
    showTableName = objects.property(Boolean.class);
    showColumnName = objects.property(Boolean.class);
    showDbComment = objects.property(Boolean.class);
    useAccessor = objects.property(Boolean.class);
    useListener = objects.property(Boolean.class);
    useMetamodel = objects.property(Boolean.class);
    originalStatesPropertyName = objects.property(String.class);
    entityPropertyClassNamesFile = objects.fileProperty();
    prefix = objects.property(String.class);
    suffix = objects.property(String.class);

    overwrite.set(true);
    overwriteListener.set(false);
    packageName.set("example.entity");
    showCatalogName.set(false);
    showSchemaName.set(false);
    showTableName.set(true);
    showColumnName.set(true);
    showDbComment.set(true);
    useAccessor.set(true);
    useListener.set(true);
    useMetamodel.set(true);
  }

  @Internal
  public Property<Boolean> getOverwrite() {
    return overwrite;
  }

  public void setOverwrite(boolean overwrite) {
    this.overwrite.set(overwrite);
  }

  @Internal
  public Property<Boolean> getOverwriteListener() {
    return overwriteListener;
  }

  public void setOverwriteListener(boolean overwriteListener) {
    this.overwriteListener.set(overwriteListener);
  }

  @Internal
  public Property<String> getSuperclassName() {
    return superclassName;
  }

  public void setSuperclassName(String superclassName) {
    this.superclassName.set(superclassName);
  }

  @Internal
  public Property<String> getListenerSuperclassName() {
    return listenerSuperclassName;
  }

  public void setListenerSuperclassName(String listenerSuperclassName) {
    this.listenerSuperclassName.set(listenerSuperclassName);
  }

  @Internal
  public Property<String> getPackageName() {
    return packageName;
  }

  public void setPackageName(String packageName) {
    this.packageName.set(packageName);
  }

  @Internal
  public Property<GenerationType> getGenerationType() {
    return generationType;
  }

  public void setGenerationType(GenerationType generationType) {
    this.generationType.set(generationType);
  }

  @Internal
  public Property<NamingType> getNamingType() {
    return namingType;
  }

  public void setNamingType(NamingType namingType) {
    this.namingType.set(namingType);
  }

  @Internal
  public Property<Long> getInitialValue() {
    return initialValue;
  }

  public void setInitialValue(Long initialValue) {
    this.initialValue.set(initialValue);
  }

  @Internal
  public Property<Long> getAllocationSize() {
    return allocationSize;
  }

  public void setAllocationSize(Long allocationSize) {
    this.allocationSize.set(allocationSize);
  }

  @Internal
  public Property<Boolean> getShowCatalogName() {
    return showCatalogName;
  }

  public void setShowCatalogName(boolean showCatalogName) {
    this.showCatalogName.set(showCatalogName);
  }

  @Internal
  public Property<Boolean> getShowSchemaName() {
    return showSchemaName;
  }

  public void setShowSchemaName(boolean showSchemaName) {
    this.showSchemaName.set(showSchemaName);
  }

  @Internal
  public Property<Boolean> getShowTableName() {
    return showTableName;
  }

  public void setShowTableName(boolean showTableName) {
    this.showTableName.set(showTableName);
  }

  @Internal
  public Property<Boolean> getShowColumnName() {
    return showColumnName;
  }

  public void setShowColumnName(boolean showColumnName) {
    this.showColumnName.set(showColumnName);
  }

  @Internal
  public Property<Boolean> getShowDbComment() {
    return showDbComment;
  }

  public void setShowDbComment(boolean showDbComment) {
    this.showDbComment.set(showDbComment);
  }

  @Internal
  public Property<Boolean> getUseAccessor() {
    return useAccessor;
  }

  public void setUseAccessor(boolean useAccessor) {
    this.useAccessor.set(useAccessor);
  }

  @Internal
  public Property<Boolean> getUseListener() {
    return useListener;
  }

  public void setUseListener(boolean useListener) {
    this.useListener.set(useListener);
  }

  @Internal
  public Property<Boolean> getUseMetamodel() {
    return useMetamodel;
  }

  public void setUseMetamodel(boolean useMetamodel) {
    this.useMetamodel.set(useMetamodel);
  }

  @Internal
  public Property<String> getOriginalStatesPropertyName() {
    return originalStatesPropertyName;
  }

  public void setOriginalStatesPropertyName(String originalStatesPropertyName) {
    this.originalStatesPropertyName.set(originalStatesPropertyName);
  }

  @Internal
  public RegularFileProperty getEntityPropertyClassNamesFile() {
    return entityPropertyClassNamesFile;
  }

  public void setEntityPropertyClassNamesFile(File entityPropertyClassNamesFile) {
    this.entityPropertyClassNamesFile.set(entityPropertyClassNamesFile);
  }

  @Internal
  public Property<String> getPrefix() {
    return prefix;
  }

  public void setPrefix(String prefix) {
    this.prefix.set(prefix);
  }

  @Internal
  public Property<String> getSuffix() {
    return suffix;
  }

  public void setSuffix(String suffix) {
    this.suffix.set(suffix);
  }
}
