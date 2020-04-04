package org.seasar.doma.gradle.codegen.desc;

import org.seasar.doma.gradle.codegen.dialect.CodeGenDialect;

public class SqlDesc {

  protected String fileName;

  protected String templateName;

  protected EntityDesc entityDesc;

  protected DaoDesc daoDesc;

  protected CodeGenDialect dialect;

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getTemplateName() {
    return templateName;
  }

  public void setTemplateName(String templateName) {
    this.templateName = templateName;
  }

  public EntityDesc getEntityDesc() {
    return entityDesc;
  }

  public void setEntityDesc(EntityDesc entityDesc) {
    this.entityDesc = entityDesc;
  }

  public DaoDesc getDaoDesc() {
    return daoDesc;
  }

  public void setDaoDesc(DaoDesc daoDesc) {
    this.daoDesc = daoDesc;
  }

  public void setDialect(CodeGenDialect dialect) {
    this.dialect = dialect;
  }

  public String toTime(String value) {
    return dialect.convertToTimeLiteral(value);
  }

  public String toDate(String value) {
    return dialect.convertToDateLiteral(value);
  }

  public String toTimestamp(String value) {
    return dialect.convertToTimestampLiteral(value);
  }
}
