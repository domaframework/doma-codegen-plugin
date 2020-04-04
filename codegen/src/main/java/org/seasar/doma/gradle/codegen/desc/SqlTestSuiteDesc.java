package org.seasar.doma.gradle.codegen.desc;

import java.util.ArrayList;
import java.util.List;

public class SqlTestSuiteDesc {

  protected List<SqlTestDesc> testCaseDescs = new ArrayList<>();

  public List<SqlTestDesc> getTestCaseDescs() {
    return testCaseDescs;
  }

  public void addTestCaseDesc(SqlTestDesc testCaseDesc) {
    testCaseDescs.add(testCaseDesc);
  }
}
