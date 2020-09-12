package org.seasar.doma.gradle.codegen.util;

import java.io.File;
import java.io.IOException;
import org.seasar.doma.gradle.codegen.desc.LanguageType;
import org.seasar.doma.gradle.codegen.exception.CodeGenException;
import org.seasar.doma.gradle.codegen.message.Message;

public final class FileUtil {

  public static String getCanonicalPath(File file) {
    try {
      return file.getCanonicalPath();
    } catch (IOException e) {
      throw new CodeGenException(Message.DOMAGEN9001, e, e);
    }
  }

  public static File createFile(LanguageType languageType, File baseDir, String className) {
    AssertionUtil.assertNotNull(baseDir, className);
    String javaFilePath =
        className.replace('.', File.separatorChar) + "." + languageType.getFileExtension();
    return new File(baseDir, javaFilePath);
  }

  public static File createSqlDir(File baseDir, String className, String fileName) {
    AssertionUtil.assertNotNull(baseDir, className);
    File metaInfDir = new File(baseDir, "META-INF");
    File dir = new File(metaInfDir, className.replace('.', File.separatorChar));
    return new File(dir, fileName);
  }
}
