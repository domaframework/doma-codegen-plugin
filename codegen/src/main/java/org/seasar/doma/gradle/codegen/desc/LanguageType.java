package org.seasar.doma.gradle.codegen.desc;

public enum LanguageType {
  JAVA("java", "org/seasar/doma/gradle/codegen/template/java", new JavaClassResolver()),
  KOTLIN("kt", "org/seasar/doma/gradle/codegen/template/kotlin", new KotlinClassResolver());

  private final String fileExtension;
  private final String templateDir;
  private final LanguageClassResolver resolver;

  LanguageType(String fileExtension, String templateDir, LanguageClassResolver resolver) {
    this.fileExtension = fileExtension;
    this.templateDir = templateDir;
    this.resolver = resolver;
  }

  public String getFileExtension() {
    return fileExtension;
  }

  public String getTemplateDir() {
    return templateDir;
  }

  public LanguageClassResolver getResolver() {
    return resolver;
  }
}
