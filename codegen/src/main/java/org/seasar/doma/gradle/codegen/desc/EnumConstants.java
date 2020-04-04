package org.seasar.doma.gradle.codegen.desc;

public enum EnumConstants {
  NamingType_NONE(ClassConstants.NamingType, "NONE"),

  NamingType_UPPER_CASE(ClassConstants.NamingType, "UPPER_CASE"),

  NamingType_LOWER_CASE(ClassConstants.NamingType, "LOWER_CASE"),

  NamingType_SNAKE_UPPER_CASE(ClassConstants.NamingType, "SNAKE_UPPER_CASE"),

  NamingType_SNAKE_LOWER_CASE(ClassConstants.NamingType, "SNAKE_LOWER_CASE"),

  GenerationType_SEQUENCE(ClassConstants.GenerationType, "SEQUENCE"),

  GenerationType_TABLE(ClassConstants.GenerationType, "TABLE"),

  GenerationType_IDENTITY(ClassConstants.GenerationType, "IDENTITY"),
  ;

  private final ClassConstants classConstant;

  private final String name;

  private EnumConstants(ClassConstants classConstant, String name) {
    this.classConstant = classConstant;
    this.name = name;
  }

  public ClassConstants getClassConstant() {
    return classConstant;
  }

  public String getImportName() {
    return classConstant.getQualifiedName();
  }

  public String getReferenceName() {
    return classConstant.getSimpleName() + "." + name;
  }

  public String getName() {
    return name;
  }
}
