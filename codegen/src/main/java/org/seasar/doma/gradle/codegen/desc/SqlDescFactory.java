package org.seasar.doma.gradle.codegen.desc;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.seasar.doma.gradle.codegen.dialect.CodeGenDialect;
import org.seasar.doma.gradle.codegen.exception.CodeGenNullPointerException;

public class SqlDescFactory {

  private final String selectByIdFileName;

  private final String selectByIdAndVersionFileName;

  private File templatePrimaryDir;

  private CodeGenDialect dialect;

  public SqlDescFactory(File templatePrimaryDir, CodeGenDialect dialect) {
    this("selectById.sql", "selectByIdAndVersion.sql", templatePrimaryDir, dialect);
  }

  public SqlDescFactory(
      String selectByIdFileName,
      String selectByIdAndVersionFileName,
      File templatePrimaryDir,
      CodeGenDialect dialect) {
    if (selectByIdFileName == null) {
      throw new CodeGenNullPointerException("selectByIdFileName");
    }
    if (selectByIdAndVersionFileName == null) {
      throw new CodeGenNullPointerException("selectByIdAndVersionFileName");
    }
    if (dialect == null) {
      throw new CodeGenNullPointerException("dialect");
    }
    this.selectByIdFileName = selectByIdFileName;
    this.selectByIdAndVersionFileName = selectByIdAndVersionFileName;
    this.templatePrimaryDir = templatePrimaryDir;
    this.dialect = dialect;
  }

  public List<SqlDesc> createSqlDescs(EntityDesc entityDesc, DaoDesc daoDesc) {
    List<SqlDesc> results = new ArrayList<SqlDesc>();
    if (entityDesc.getIdEntityPropertyDescs().size() > 0) {
      results.add(
          createSqlDesc(
              entityDesc, daoDesc, selectByIdFileName, Constants.SELECT_BY_ID_SQL_TEMPLATE));
      if (entityDesc.getVersionEntityPropertyDesc() != null) {
        results.add(
            createSqlDesc(
                entityDesc,
                daoDesc,
                selectByIdAndVersionFileName,
                Constants.SELECT_BY_ID_AND_VERSION_SQL_TEMPLATE));
      }
    }
    for (String templateName : findTemplateNames()) {
      String fileName = removeTemplateExtension(templateName);
      results.add(createSqlDesc(entityDesc, daoDesc, fileName, templateName));
    }
    return results;
  }

  public SqlDesc createSqlDesc(
      EntityDesc entityDesc, DaoDesc daoDesc, String fileName, String templateName) {
    SqlDesc sqlFileDesc = new SqlDesc();
    sqlFileDesc.setFileName(fileName);
    sqlFileDesc.setTemplateName(templateName);
    sqlFileDesc.setEntityDesc(entityDesc);
    sqlFileDesc.setDaoDesc(daoDesc);
    sqlFileDesc.setDialect(dialect);
    return sqlFileDesc;
  }

  protected Set<String> findTemplateNames() {
    final Set<String> results = new HashSet<String>();
    if (templatePrimaryDir == null) {
      return results;
    }
    templatePrimaryDir.listFiles(
        new FileFilter() {

          @Override
          public boolean accept(File file) {
            if (file.isFile()) {
              String name = file.getName();
              if (!name.equals(Constants.SELECT_BY_ID_SQL_TEMPLATE)
                  && !name.equals(Constants.SELECT_BY_ID_AND_VERSION_SQL_TEMPLATE)) {
                if (name.endsWith(Constants.SQL_TEMPLATE_EXTENSION)) {
                  results.add(name);
                }
              }
            }
            return false;
          }
        });
    return results;
  }

  protected String removeTemplateExtension(String templateName) {
    return templateName.substring(0, templateName.length() - Constants.TEMPLATE_EXTENSION.length());
  }
}
