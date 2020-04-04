package org.seasar.doma.gradle.codegen.desc;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

import java.io.File;
import java.util.Objects;
import java.util.stream.StreamSupport;
import org.seasar.doma.gradle.codegen.exception.CodeGenNullPointerException;
import org.seasar.doma.gradle.codegen.util.FileUtil;
import org.seasar.doma.gradle.codegen.util.Pair;
import org.seasar.doma.gradle.codegen.util.StringUtil;

public class SqlTestSuiteDescFactory {

  protected SqlTestDescFactory sqlTestDescFactory;

  public SqlTestSuiteDescFactory(SqlTestDescFactory sqlTestDescFactory) {
    if (sqlTestDescFactory == null) {
      throw new CodeGenNullPointerException("sqlTestCaseDescFactory");
    }
    this.sqlTestDescFactory = sqlTestDescFactory;
  }

  public SqlTestSuiteDesc createSqlTestSuiteDesc(Iterable<File> sqlFiles) {
    if (sqlFiles == null) {
      throw new CodeGenNullPointerException("sqlFiles");
    }
    SqlTestSuiteDesc suiteDesc = new SqlTestSuiteDesc();
    StreamSupport.stream(sqlFiles.spliterator(), false)
        .filter(Objects::nonNull)
        .filter(f -> f.isFile())
        .filter(f -> f.getName().endsWith(".sql"))
        .filter(f -> !f.getName().contains("-"))
        .map(FileUtil::getCanonicalPath)
        .map(path -> path.replace(File.separator, "/"))
        .map(this::fromPathToPair)
        .filter(Objects::nonNull)
        .collect(groupingBy(Pair::getFirst, mapping(Pair::getSecond, toList())))
        .forEach(
            (className, methodDescs) -> {
              SqlTestDesc testCaseDesc =
                  sqlTestDescFactory.createSqlFileTestDesc(className, methodDescs);
              suiteDesc.addTestCaseDesc(testCaseDesc);
            });
    return suiteDesc;
  }

  protected Pair<String, SqlTestMethodDesc> fromPathToPair(String path) {
    int pos = path.indexOf("/META-INF/");
    int pos2 = path.lastIndexOf('/');
    if (pos == -1 || pos2 == -1 || pos + "/META-INF/".length() == pos2) {
      return null;
    }
    String sqlPath = path.substring(pos + 1);
    String dirName = path.substring(pos + "/META-INF/".length(), pos2);
    String baseName = path.substring(pos2 + 1);
    String className = dirName.replace('/', '.') + "Test";
    String methodName =
        "test" + StringUtil.capitalize(baseName.substring(0, baseName.length() - ".sql".length()));
    return new Pair<>(className, new SqlTestMethodDesc(methodName, sqlPath));
  }
}
