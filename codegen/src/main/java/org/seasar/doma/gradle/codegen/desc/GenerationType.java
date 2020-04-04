package org.seasar.doma.gradle.codegen.desc;

public enum GenerationType {
  IDENTITY(EnumConstants.GenerationType_IDENTITY),

  SEQUENCE(EnumConstants.GenerationType_SEQUENCE),

  TABLE(EnumConstants.GenerationType_TABLE);

  private final EnumConstants enumConstant;

  GenerationType(EnumConstants enumConstant) {
    this.enumConstant = enumConstant;
  }

  public EnumConstants getEnumConstant() {
    return enumConstant;
  }

  public String getReferenceName() {
    return enumConstant.getReferenceName();
  }
}
