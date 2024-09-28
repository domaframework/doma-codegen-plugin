package org.seasar.doma.gradle.codegen.util;

import org.seasar.doma.gradle.codegen.exception.CodeGenException;
import org.seasar.doma.gradle.codegen.message.Message;

public final class ClassUtil {

  public static String getPackageName(String qualifiedName) {
    AssertionUtil.assertNotNull(qualifiedName);
    int pos = qualifiedName.lastIndexOf('.');
    if (pos < 0) {
      return "";
    }
    return qualifiedName.substring(0, pos);
  }

  public static String getSimpleName(String qualifiedName) {
    AssertionUtil.assertNotNull(qualifiedName);
    int pos = qualifiedName.lastIndexOf('.');
    if (pos < 0) {
      return qualifiedName;
    }
    return qualifiedName.substring(pos + 1);
  }

  public static Class<?> toBoxedPrimitiveTypeIfPossible(Class<?> clazz) {
    AssertionUtil.assertNotNull(clazz);
    if (clazz == void.class) {
      return Void.class;
    }
    if (clazz == char.class) {
      return Character.class;
    }
    if (clazz == boolean.class) {
      return Boolean.class;
    }
    if (clazz == byte.class) {
      return Byte.class;
    }
    if (clazz == short.class) {
      return Short.class;
    }
    if (clazz == int.class) {
      return Integer.class;
    }
    if (clazz == long.class) {
      return Long.class;
    }
    if (clazz == float.class) {
      return Float.class;
    }
    if (clazz == double.class) {
      return Double.class;
    }
    return clazz;
  }

  public static <T> T newInstance(Class<T> supertype, String className, String propertyName) {
    AssertionUtil.assertNotNull(supertype, className, propertyName);
    Class<?> clazz = forName(className, propertyName);
    if (!supertype.isAssignableFrom(clazz)) {
      throw new CodeGenException(Message.DOMAGEN0014, propertyName, className, supertype.getName());
    }
    try {
      return supertype.cast(clazz.getDeclaredConstructor().newInstance());
    } catch (ReflectiveOperationException e) {
      throw new CodeGenException(Message.DOMAGEN0015, propertyName, className, e);
    }
  }

  public static Class<?> forName(String className, String propertyName) {
    AssertionUtil.assertNotNull(className, propertyName);
    try {
      return Class.forName(className);
    } catch (ClassNotFoundException e) {
      throw new CodeGenException(Message.DOMAGEN0013, propertyName, className, e);
    }
  }
}
