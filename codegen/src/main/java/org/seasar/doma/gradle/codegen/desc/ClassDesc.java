package org.seasar.doma.gradle.codegen.desc;

import java.util.SortedSet;
import java.util.TreeSet;

public abstract class ClassDesc {

  protected final SortedSet<String> importNames = new TreeSet<String>();

  protected String packageName;

  protected String simpleName;

  protected String comment;

  protected ClassDesc() {}

  public void addImportName(String name) {
    importNames.add(name);
  }

  public SortedSet<String> getImportNames() {
    return importNames;
  }

  public void setPackageName(String packageName) {
    this.packageName = packageName;
  }

  public String getPackageName() {
    return packageName;
  }

  public void setSimpleName(String simpleName) {
    this.simpleName = simpleName;
  }

  public String getSimpleName() {
    return simpleName;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public String getComment() {
    return comment;
  }

  public String getQualifiedName() {
    if (packageName == null || packageName.isEmpty()) {
      return simpleName;
    }
    return packageName + "." + simpleName;
  }
}
