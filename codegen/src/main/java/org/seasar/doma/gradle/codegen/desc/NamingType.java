package org.seasar.doma.gradle.codegen.desc;

import org.seasar.doma.gradle.codegen.exception.CodeGenNullPointerException;
import org.seasar.doma.gradle.codegen.util.StringUtil;

public enum NamingType {
  NONE(EnumConstants.NamingType_NONE) {

    @Override
    public String apply(String text) {
      return text;
    }
  },

  LOWER_CASE(EnumConstants.NamingType_LOWER_CASE) {

    @Override
    public String apply(String text) {
      if (text == null) {
        throw new CodeGenNullPointerException("text");
      }
      return text.toLowerCase();
    }
  },

  UPPER_CASE(EnumConstants.NamingType_UPPER_CASE) {

    @Override
    public String apply(String text) {
      if (text == null) {
        throw new CodeGenNullPointerException("text");
      }
      return text.toUpperCase();
    }
  },

  SNAKE_UPPER_CASE(EnumConstants.NamingType_SNAKE_UPPER_CASE) {

    @Override
    public String apply(String text) {
      if (text == null) {
        throw new CodeGenNullPointerException("text");
      }
      String s = StringUtil.fromCamelCaseToSnakeCase(text);
      return s.toUpperCase();
    }
  },

  SNAKE_LOWER_CASE(EnumConstants.NamingType_SNAKE_LOWER_CASE) {

    @Override
    public String apply(String text) {
      if (text == null) {
        throw new CodeGenNullPointerException("text");
      }
      String s = StringUtil.fromCamelCaseToSnakeCase(text);
      return s.toLowerCase();
    }
  };

  private final EnumConstants enumConstant;

  NamingType(EnumConstants enumConstant) {
    this.enumConstant = enumConstant;
  }

  public EnumConstants getEnumConstant() {
    return enumConstant;
  }

  public String getReferenceName() {
    return enumConstant.getReferenceName();
  }

  public abstract String apply(String text);
}
