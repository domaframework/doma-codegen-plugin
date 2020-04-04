package org.seasar.doma.gradle.codegen.desc;

import org.seasar.doma.gradle.codegen.util.ClassUtil;

public class ClassDescSupport {

  public void addImportName(ClassDesc classDesc, Class<?> importedClass) {
    String canonicalName = importedClass.getCanonicalName();
    String packageName = ClassUtil.getPackageName(canonicalName);
    if (isImportTargetPackage(classDesc, packageName)) {
      classDesc.addImportName(canonicalName);
    }
  }

  public void addImportName(ClassDesc classDesc, String importedClassName) {
    String packageName = ClassUtil.getPackageName(importedClassName);
    if (isImportTargetPackage(classDesc, packageName)) {
      classDesc.addImportName(importedClassName);
    }
  }

  public void addImportName(ClassDesc classDesc, ClassConstants importedClass) {
    String packageName = importedClass.getPackageName();
    if (isImportTargetPackage(classDesc, packageName)) {
      classDesc.addImportName(importedClass.getQualifiedName());
    }
  }

  public void addImportName(ClassDesc classDesc, EnumConstants importedClass) {
    ClassConstants classConstant = importedClass.getClassConstant();
    String packageName = classConstant.getPackageName();
    if (isImportTargetPackage(classDesc, packageName)) {
      classDesc.addImportName(importedClass.getImportName());
    }
  }

  protected boolean isImportTargetPackage(ClassDesc classDesc, String importPackageName) {
    if (importPackageName == null) {
      return false;
    }
    if (importPackageName.isEmpty()) {
      return false;
    }
    if (importPackageName.equals(classDesc.getPackageName())) {
      return false;
    }
    if (importPackageName.equals("java.lang")) {
      return false;
    }
    return true;
  }
}
