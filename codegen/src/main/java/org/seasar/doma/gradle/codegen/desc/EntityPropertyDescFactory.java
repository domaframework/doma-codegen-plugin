package org.seasar.doma.gradle.codegen.desc;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.regex.Pattern;
import org.seasar.doma.gradle.codegen.dialect.CodeGenDialect;
import org.seasar.doma.gradle.codegen.exception.CodeGenException;
import org.seasar.doma.gradle.codegen.exception.CodeGenNullPointerException;
import org.seasar.doma.gradle.codegen.message.Message;
import org.seasar.doma.gradle.codegen.meta.ColumnMeta;
import org.seasar.doma.gradle.codegen.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntityPropertyDescFactory {

  private final Logger logger = LoggerFactory.getLogger(EntityPropertyDescFactory.class);

  protected final CodeGenDialect dialect;

  protected final EntityPropertyClassNameResolver propertyClassNameResolver;

  protected final Pattern versionColumnNamePattern;

  protected final GenerationType generationType;

  protected final Long initialValue;

  protected final Long allocationSize;

  protected final boolean showColumnName;

  public EntityPropertyDescFactory(
      CodeGenDialect dialect,
      EntityPropertyClassNameResolver propertyClassNameResolver,
      String versionColumnNamePattern,
      GenerationType generationType,
      Long initialValue,
      Long allocationSize,
      boolean showColumnName) {
    if (dialect == null) {
      throw new CodeGenNullPointerException("dialect");
    }
    if (propertyClassNameResolver == null) {
      throw new CodeGenNullPointerException("propertyClassNameResolver");
    }
    if (versionColumnNamePattern == null) {
      throw new CodeGenNullPointerException("versionColumnNamePattern");
    }
    this.dialect = dialect;
    this.propertyClassNameResolver = propertyClassNameResolver;
    this.versionColumnNamePattern =
        Pattern.compile(versionColumnNamePattern, Pattern.CASE_INSENSITIVE);
    this.generationType = generationType;
    this.initialValue = initialValue;
    this.allocationSize = allocationSize;
    this.showColumnName = showColumnName;

    validateGenerationType(generationType);
  }

  protected void validateGenerationType(GenerationType generationType) {
    if (generationType == null) {
      return;
    }
    if (generationType == GenerationType.IDENTITY) {
      if (!dialect.supportsIdentity()) {
        throw new CodeGenException(Message.DOMAGEN0003, dialect.getName());
      }
    } else if (generationType == GenerationType.SEQUENCE) {
      if (!dialect.supportsSequence()) {
        throw new CodeGenException(Message.DOMAGEN0004, dialect.getName());
      }
    }
  }

  public EntityPropertyDesc createEntityPropertyDesc(EntityDesc entityDesc, ColumnMeta columnMeta) {
    EntityPropertyDesc propertyDesc = new EntityPropertyDesc();
    handleName(entityDesc, propertyDesc, columnMeta);
    propertyDesc.setColumnName(columnMeta.getName());
    if (columnMeta.isPrimaryKey()) {
      propertyDesc.setId(true);
      if (!entityDesc.isCompositeId()) {
        if (columnMeta.isAutoIncrement()) {
          propertyDesc.setGenerationType(GenerationType.IDENTITY);
        } else {
          propertyDesc.setGenerationType(generationType);
          propertyDesc.setInitialValue(initialValue);
          propertyDesc.setAllocationSize(allocationSize);
        }
      }
    }
    propertyDesc.setComment(columnMeta.getComment());
    handlePropertyClassName(entityDesc, propertyDesc, columnMeta);
    descriminateType(entityDesc, propertyDesc, columnMeta);
    handleShowColumnName(entityDesc, propertyDesc, columnMeta);
    handleVersion(entityDesc, propertyDesc, columnMeta);
    propertyDesc.setEntityClassName(entityDesc.getQualifiedName());
    return propertyDesc;
  }

  protected void handleName(
      EntityDesc entityDesc, EntityPropertyDesc propertyDesc, ColumnMeta columnMeta) {
    String name = StringUtil.fromSnakeCaseToCamelCase(columnMeta.getName());
    propertyDesc.setName(name);
  }

  protected void handlePropertyClassName(
      EntityDesc entityDesc, EntityPropertyDesc propertyDesc, ColumnMeta columnMeta) {
    String defaultClassName = dialect.getMappedPropertyClassName(columnMeta);
    String className =
        propertyClassNameResolver.resolve(entityDesc, propertyDesc.getName(), defaultClassName);
    if (className == null) {
      logger.info(
          Message.DOMAGEN0018.getMessage(
              columnMeta.getTableMeta().getName(),
              columnMeta.getName(),
              columnMeta.getTypeName(),
              columnMeta.getSqlType()));
      className = String.class.getName();
    }
    propertyDesc.setPropertyClassName(className);
  }

  protected void descriminateType(
      EntityDesc entityDesc, EntityPropertyDesc propertyDesc, ColumnMeta columnMeta) {
    String defaultClassName = dialect.getMappedPropertyClassName(columnMeta);
    if (Byte.class.getName().equals(defaultClassName)
        || Short.class.getName().equals(defaultClassName)
        || Integer.class.getName().equals(defaultClassName)
        || Long.class.getName().equals(defaultClassName)
        || Float.class.getName().equals(defaultClassName)
        || Double.class.getName().equals(defaultClassName)
        || BigInteger.class.getName().equals(defaultClassName)
        || BigDecimal.class.getName().equals(defaultClassName)
        || byte.class.getName().equals(defaultClassName)
        || short.class.getName().equals(defaultClassName)
        || int.class.getName().equals(defaultClassName)
        || long.class.getName().equals(defaultClassName)
        || float.class.getName().equals(defaultClassName)
        || double.class.getName().equals(defaultClassName)) {
      propertyDesc.setNumber(true);
    } else if (Time.class.getName().equals(defaultClassName)
        || LocalTime.class.getName().equals(defaultClassName)) {
      propertyDesc.setTime(true);
    } else if (Date.class.getName().equals(defaultClassName)
        || LocalDate.class.getName().equals(defaultClassName)) {
      propertyDesc.setDate(true);
    } else if (Timestamp.class.getName().equals(defaultClassName)
        || java.util.Date.class.getName().equals(defaultClassName)
        || LocalDateTime.class.getName().equals(defaultClassName)) {
      propertyDesc.setTimestamp(true);
    }
  }

  protected void handleShowColumnName(
      EntityDesc entityDesc, EntityPropertyDesc propertyDesc, ColumnMeta columnMeta) {
    if (showColumnName || isNameDifferentBetweenPropertyAndColumn(entityDesc, propertyDesc)) {
      propertyDesc.setShowColumnName(true);
    }
  }

  protected boolean isNameDifferentBetweenPropertyAndColumn(
      EntityDesc entityDesc, EntityPropertyDesc propertyDesc) {
    String propertyName = propertyDesc.getName();
    String columnName = propertyDesc.getColumnName();
    NamingType namingType = entityDesc.getNamingType();
    return !columnName.equalsIgnoreCase(namingType.apply(propertyName));
  }

  protected void handleVersion(
      EntityDesc entityDesc, EntityPropertyDesc propertyDesc, ColumnMeta columnMeta) {
    if (isVersionAnnotatable(propertyDesc.getPropertyClassName())) {
      if (versionColumnNamePattern.matcher(columnMeta.getName()).matches()) {
        propertyDesc.setVersion(true);
      }
    }
  }

  protected boolean isVersionAnnotatable(String className) {
    return Byte.class.getName().equals(className)
        || Short.class.getName().equals(className)
        || Integer.class.getName().equals(className)
        || Long.class.getName().equals(className)
        || Float.class.getName().equals(className)
        || Double.class.getName().equals(className)
        || BigInteger.class.getName().equals(className)
        || BigDecimal.class.getName().equals(className);
  }
}
