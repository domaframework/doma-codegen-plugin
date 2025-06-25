package org.seasar.doma.gradle.codegen.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.seasar.doma.gradle.codegen.exception.CodeGenException;

@SuppressWarnings("unchecked")
public class ClassUtilTest {

  @Test
  public void testGetPackageName() {
    assertEquals("java.lang", ClassUtil.getPackageName("java.lang.String"));
    assertEquals("", ClassUtil.getPackageName("String"));
    assertEquals("", ClassUtil.getPackageName(""));
  }

  @Test
  public void testGetSimpleName() {
    assertEquals("String", ClassUtil.getSimpleName("java.lang.String"));
    assertEquals("String", ClassUtil.getSimpleName("String"));
    assertEquals("", ClassUtil.getSimpleName(""));
  }

  @Test
  public void testToBoxedPrimitiveTypeIfPossible() {
    assertSame(Void.class, ClassUtil.toBoxedPrimitiveTypeIfPossible(void.class));
    assertSame(Character.class, ClassUtil.toBoxedPrimitiveTypeIfPossible(char.class));
    assertSame(Boolean.class, ClassUtil.toBoxedPrimitiveTypeIfPossible(boolean.class));
    assertSame(Byte.class, ClassUtil.toBoxedPrimitiveTypeIfPossible(byte.class));
    assertSame(Short.class, ClassUtil.toBoxedPrimitiveTypeIfPossible(short.class));
    assertSame(Integer.class, ClassUtil.toBoxedPrimitiveTypeIfPossible(int.class));
    assertSame(Long.class, ClassUtil.toBoxedPrimitiveTypeIfPossible(long.class));
    assertSame(Float.class, ClassUtil.toBoxedPrimitiveTypeIfPossible(float.class));
    assertSame(Double.class, ClassUtil.toBoxedPrimitiveTypeIfPossible(double.class));
    assertSame(String.class, ClassUtil.toBoxedPrimitiveTypeIfPossible(String.class));
  }

  @Test
  public void testNewInstance() {
    List<String> list = ClassUtil.newInstance(List.class, "java.util.ArrayList", "testProperty");
    assertInstanceOf(ArrayList.class, list);
  }

  @Test
  public void testNewInstance_WithClassLoader() throws Exception {
    URLClassLoader classLoader = new URLClassLoader(new URL[0], getClass().getClassLoader());
    List<String> list =
        ClassUtil.newInstance(List.class, "java.util.ArrayList", "testProperty", classLoader);
    assertInstanceOf(ArrayList.class, list);
  }

  @Test
  public void testNewInstance_ClassNotFound() {
    assertThrows(
        CodeGenException.class,
        () -> {
          ClassUtil.newInstance(List.class, "non.existent.Class", "testProperty");
        });
  }

  @Test
  public void testNewInstance_NotAssignable() {
    assertThrows(
        CodeGenException.class,
        () -> {
          ClassUtil.newInstance(List.class, "java.lang.String", "testProperty");
        });
  }

  @Test
  public void testNewInstance_WithClassLoader_ClassNotFound() throws Exception {
    URLClassLoader classLoader = new URLClassLoader(new URL[0], getClass().getClassLoader());
    assertThrows(
        CodeGenException.class,
        () -> {
          ClassUtil.newInstance(List.class, "non.existent.Class", "testProperty", classLoader);
        });
  }

  @Test
  public void testNewInstance_WithClassLoader_NotAssignable() throws Exception {
    try (URLClassLoader classLoader = new URLClassLoader(new URL[0], getClass().getClassLoader())) {
      assertThrows(
          CodeGenException.class,
          () -> {
            ClassUtil.newInstance(List.class, "java.lang.String", "testProperty", classLoader);
          });
    }
  }

  @Test
  public void testForName() {
    Class<?> clazz = ClassUtil.forName("java.lang.String", "testProperty");
    assertSame(String.class, clazz);
  }

  @Test
  public void testForName_WithClassLoader() throws Exception {
    URLClassLoader classLoader = new URLClassLoader(new URL[0], getClass().getClassLoader());
    Class<?> clazz = ClassUtil.forName("java.lang.String", "testProperty", classLoader);
    assertSame(String.class, clazz);
  }

  @Test
  public void testForName_ClassNotFound() {
    assertThrows(
        CodeGenException.class,
        () -> {
          ClassUtil.forName("non.existent.Class", "testProperty");
        });
  }

  @Test
  public void testForName_WithClassLoader_ClassNotFound() throws Exception {
    URLClassLoader classLoader = new URLClassLoader(new URL[0], getClass().getClassLoader());
    assertThrows(
        CodeGenException.class,
        () -> {
          ClassUtil.forName("non.existent.Class", "testProperty", classLoader);
        });
  }
}
