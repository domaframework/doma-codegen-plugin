package org.seasar.doma.gradle.codegen.desc;

import org.seasar.doma.gradle.codegen.util.ClassUtil;

public class EntityPropertyDesc {

  protected String name;

  protected String propertyClassName;

  protected String languageClassName;

  protected boolean id;

  protected GenerationType generationType;

  protected Long initialValue;

  protected Long allocationSize;

  protected boolean version;

  protected String columnName;

  protected String comment;

  protected boolean showColumnName;

  protected boolean number;

  protected boolean time;

  protected boolean date;

  protected boolean timestamp;

  protected String entityClassName;

  protected boolean nullable;

  protected String defaultValue;

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public String getPropertyClassName() {
    return propertyClassName;
  }

  public void setPropertyClassName(String propertyClassName) {
    this.propertyClassName = propertyClassName;
  }

  public String getPropertyClassSimpleName() {
    return ClassUtil.getSimpleName(propertyClassName);
  }

  public String getLanguageClassSimpleName() {
    return ClassUtil.getSimpleName(languageClassName);
  }

  public void setLanguageClassName(String languageClassName) {
    this.languageClassName = languageClassName;
  }

  public void setId(boolean id) {
    this.id = id;
  }

  public boolean isId() {
    return id;
  }

  public void setGenerationType(GenerationType generationType) {
    this.generationType = generationType;
  }

  public GenerationType getGenerationType() {
    return generationType;
  }

  public void setInitialValue(Long initialValue) {
    this.initialValue = initialValue;
  }

  public Long getInitialValue() {
    return initialValue;
  }

  public void setAllocationSize(Long allocationSize) {
    this.allocationSize = allocationSize;
  }

  public Long getAllocationSize() {
    return allocationSize;
  }

  public void setVersion(boolean version) {
    this.version = version;
  }

  public boolean isVersion() {
    return version;
  }

  public void setColumnName(String columnName) {
    this.columnName = columnName;
  }

  public String getColumnName() {
    return columnName;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public String getComment() {
    return comment;
  }

  public boolean isShowColumnName() {
    return showColumnName;
  }

  public void setShowColumnName(boolean showColumnName) {
    this.showColumnName = showColumnName;
  }

  public boolean isNumber() {
    return number;
  }

  public void setNumber(boolean number) {
    this.number = number;
  }

  public boolean isTime() {
    return time;
  }

  public void setTime(boolean time) {
    this.time = time;
  }

  public boolean isDate() {
    return date;
  }

  public void setDate(boolean date) {
    this.date = date;
  }

  public boolean isTimestamp() {
    return timestamp;
  }

  public void setTimestamp(boolean timestamp) {
    this.timestamp = timestamp;
  }

  public String getEntityClassName() {
    return entityClassName;
  }

  public void setEntityClassName(String entityClassName) {
    this.entityClassName = entityClassName;
  }

  public boolean isNullable() {
    return nullable;
  }

  public void setNullable(boolean nullable) {
    this.nullable = nullable;
  }

  public String getDefaultValue() {
    return defaultValue;
  }

  public void setDefaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
  }
}
