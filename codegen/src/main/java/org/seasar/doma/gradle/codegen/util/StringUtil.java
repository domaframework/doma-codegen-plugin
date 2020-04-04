package org.seasar.doma.gradle.codegen.util;

import java.nio.CharBuffer;

public final class StringUtil {

  public static String capitalize(String text) {
    if (isNullOrEmpty(text)) {
      return text;
    }
    char chars[] = text.toCharArray();
    chars[0] = Character.toUpperCase(chars[0]);
    return new String(chars);
  }

  public static String decapitalize(String text) {
    if (isNullOrEmpty(text)) {
      return text;
    }
    char chars[] = text.toCharArray();
    chars[0] = Character.toLowerCase(chars[0]);
    return new String(chars);
  }

  public static String fromSnakeCaseToCamelCase(String text) {
    if (isNullOrEmpty(text)) {
      return text;
    }
    String[] array = text.split("_");
    if (array.length == 0) {
      return "";
    }
    StringBuilder result = new StringBuilder();
    result.append(array[0].toLowerCase());
    for (int i = 1; i < array.length; i++) {
      String s = capitalize(array[i].toLowerCase());
      result.append(s);
    }
    return result.toString();
  }

  public static String fromCamelCaseToSnakeCase(String text) {
    if (isNullOrEmpty(text)) {
      return text;
    }
    StringBuilder result = new StringBuilder();
    CharBuffer buf = CharBuffer.wrap(text);
    while (buf.hasRemaining()) {
      char c = buf.get();
      result.append(Character.toLowerCase(c));
      buf.mark();
      if (buf.hasRemaining()) {
        char c2 = buf.get();
        if (Character.isLowerCase(c) && Character.isUpperCase(c2)) {
          result.append("_");
        }
        buf.reset();
      }
    }
    return result.toString();
  }

  public static boolean isNullOrEmpty(String text) {
    return text == null || text.isEmpty();
  }

  public static String defaultString(final String str, final String defaultStr) {
    return str == null ? defaultStr : str;
  }
}
