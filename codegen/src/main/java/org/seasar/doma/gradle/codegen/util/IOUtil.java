package org.seasar.doma.gradle.codegen.util;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import org.seasar.doma.gradle.codegen.exception.CodeGenException;
import org.seasar.doma.gradle.codegen.message.Message;

public final class IOUtil {

  protected static final int BUF_SIZE = 8192;

  public static String readAsString(InputStream inputStream) {
    AssertionUtil.assertNotNull(inputStream);
    BufferedReader reader =
        new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
    StringBuilder buf = new StringBuilder(200);
    try {
      CharBuffer c = CharBuffer.allocate(BUF_SIZE);
      while (reader.read(c) > -1) {
        c.flip();
        buf.append(c);
        c.clear();
      }
    } catch (IOException e) {
      throw new CodeGenException(Message.DOMAGEN9001, e, e);
    } finally {
      IOUtil.close(reader);
    }
    return buf.toString();
  }

  public static String readAsString(File file) {
    AssertionUtil.assertNotNull(file);
    InputStream inputStream = null;
    try {
      inputStream = new FileInputStream(file);
      return readAsString(inputStream);
    } catch (FileNotFoundException e) {
      throw new CodeGenException(Message.DOMAGEN9001, e, e);
    } finally {
      IOUtil.close(inputStream);
    }
  }

  public static void close(Closeable closeable) {
    if (closeable != null) {
      try {
        closeable.close();
      } catch (IOException ignored) {
      }
    }
  }
}
