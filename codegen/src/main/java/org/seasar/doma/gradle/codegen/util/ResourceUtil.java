package org.seasar.doma.gradle.codegen.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import org.seasar.doma.gradle.codegen.exception.CodeGenException;
import org.seasar.doma.gradle.codegen.message.Message;

public final class ResourceUtil {

  public static URL getResource(String path) {
    AssertionUtil.assertNotNull(path);
    URL result = ResourceUtil.class.getResource("/" + path);
    if (result != null) {
      return result;
    }
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    if (loader == null) {
      return null;
    }
    return loader.getResource(path);
  }

  public static InputStream getResourceAsStream(String path) {
    AssertionUtil.assertNotNull(path);
    URL url = getResource(path);
    try {
      return url != null ? url.openStream() : null;
    } catch (IOException e) {
      return null;
    }
  }

  public static String getResourceAsString(String path) {
    AssertionUtil.assertNotNull(path);
    AssertionUtil.assertTrue(path.length() > 0);
    InputStream inputStream = getResourceAsStream(path);
    if (inputStream == null) {
      return null;
    }
    return IOUtil.readAsString(inputStream);
  }

  public static File getResourceAsFile(String path) {
    AssertionUtil.assertNotNull(path);
    URL url = getResource(path);
    if (url == null) {
      return null;
    }
    return getFile(url);
  }

  protected static File getFile(URL url) {
    AssertionUtil.assertNotNull(url);
    File file = new File(getFileName(url));
    if (file.exists()) {
      return file;
    }
    return null;
  }

  protected static String getFileName(URL url) {
    AssertionUtil.assertNotNull(url);
    String s = url.getFile();
    try {
      return URLDecoder.decode(s, "UTF-8");
    } catch (final UnsupportedEncodingException e) {
      throw new CodeGenException(Message.DOMAGEN9001, e, e);
    }
  }
}
