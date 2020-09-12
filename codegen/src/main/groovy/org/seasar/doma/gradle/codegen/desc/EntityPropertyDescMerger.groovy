package org.seasar.doma.gradle.codegen.desc

import java.lang.annotation.Annotation
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import org.seasar.doma.gradle.codegen.exception.CodeGenException;
import org.seasar.doma.gradle.codegen.message.Message;
import org.seasar.doma.gradle.codegen.util.ClassUtil;

class EntityPropertyDescMerger {

  protected final Class<?> superclass;

  EntityPropertyDescMerger(Class<?> superclass) {
    this.superclass = superclass;
  }

  void merge(EntityDesc entityDesc, Map<String, EntityPropertyDesc> propertyDescMap) {
    for (EntityPropertyInfo propertyInfo : getSuperclassEntityPropertyInfo()) {
      EntityPropertyDesc propertyDesc = propertyDescMap.get(propertyInfo.columnName.toLowerCase());
      if (propertyDesc == null) {
        throw new CodeGenException(
            Message.DOMAGEN0021,
            superclass.getName(),
            propertyInfo.propertyField.getName(),
            propertyInfo.columnName,
            entityDesc.getQualifiedTableName());
      }
      mergeEntityProperty(propertyDesc, propertyInfo);
    }
  }

  private Set<EntityPropertyInfo> getSuperclassEntityPropertyInfo() {
    Set<EntityPropertyInfo> results = new HashSet<EntityPropertyInfo>();
    if (superclass == null) {
      return results;
    }
    for (Class<?> clazz = superclass; clazz != Object.class; clazz = clazz.getSuperclass()) {
      def entity = getAnnotation(clazz, ClassConstants.Entity);
      if (entity == null) {
        continue;
      }
      def namingType = entity.naming();
      for (Field field : clazz.getDeclaredFields()) {
        int m = field.getModifiers();
        if (Modifier.isStatic(m)) {
          continue;
        }
        if (isAnnotationPresent(field, ClassConstants.Transient)) {
          continue;
        }
        EntityPropertyInfo propertyInfo = new EntityPropertyInfo();
        propertyInfo.entityClass = clazz;
        propertyInfo.propertyField = field;
        def column = getAnnotation(field, ClassConstants.Column);
        propertyInfo.column = column;
        if (column == null || column.name().isEmpty()) {
          propertyInfo.columnName = namingType.apply(field.getName()).toLowerCase();
        } else {
          propertyInfo.columnName = column.name();
        }
        propertyInfo.id = getAnnotation(field, ClassConstants.Id);
        propertyInfo.generatedValue = getAnnotation(field, ClassConstants.GeneratedValue);
        propertyInfo.sequenceGenerator = getAnnotation(field, ClassConstants.SequenceGenerator);
        propertyInfo.tableGenerator = getAnnotation(field, ClassConstants.TableGenerator);
        propertyInfo.version = getAnnotation(field, ClassConstants.Version);
        results.add(propertyInfo);
      }
    }
    return results;
  }

  private void mergeEntityProperty(EntityPropertyDesc dest, EntityPropertyInfo src) {
    dest.setEntityClassName(src.entityClass.getName());
    dest.setPropertyClassName(src.propertyField.getType().getName());
    Class<?> maybeNumberClass =
        ClassUtil.toBoxedPrimitiveTypeIfPossible(src.propertyField.getType());
    if (Number.class.isAssignableFrom(maybeNumberClass)) {
      dest.setNumber(true);
    }
    if (src.version != null) {
      dest.setVersion(true);
    }
    if (src.id != null) {
      dest.setId(true);
      if (src.generatedValue != null) {
        switch (src.generatedValue.strategy()) {
          case IDENTITY:
            dest.setGenerationType(GenerationType.IDENTITY);
            break;
          case SEQUENCE:
            dest.setGenerationType(GenerationType.SEQUENCE);
            if (src.sequenceGenerator != null) {
              dest.setInitialValue(src.sequenceGenerator.getDefaultValue());
              dest.setAllocationSize(src.sequenceGenerator.allocationSize());
              dest.setAllocationSize(src.sequenceGenerator.allocationSize());
            }
            break;
          case TABLE:
            dest.setGenerationType(GenerationType.TABLE);
            if (src.tableGenerator != null) {
              dest.setInitialValue(src.tableGenerator.getDefaultValue());
              dest.setAllocationSize(src.tableGenerator.allocationSize());
              dest.setAllocationSize(src.tableGenerator.allocationSize());
            }
            break;
          default:
            break;
        }
      }
    }
  }

  private boolean isAnnotationPresent(AnnotatedElement element, ClassConstants constants) {
    return getAnnotation(element, constants) != null;
  }

  private Annotation getAnnotation(AnnotatedElement element, ClassConstants constants) {
    for(Annotation annotation in element.getAnnotations()) {
      if (annotation.annotationType().getName().equals(constants.getQualifiedName())) {
        return annotation;
      }
    }
    return null;
  }

  private static class EntityPropertyInfo {
    String columnName;
    Class<?> entityClass;
    Field propertyField;
    def column;
    def id;
    def generatedValue;
    def sequenceGenerator;
    def tableGenerator;
    def version;
  }
}
