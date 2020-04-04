package org.seasar.doma.gradle.codegen.generator;

import freemarker.cache.URLTemplateLoader;
import java.net.URL;
import org.seasar.doma.gradle.codegen.util.ResourceUtil;

public class ResourceTemplateLoader extends URLTemplateLoader {

  protected String basePath;

  public ResourceTemplateLoader(String basePath) {
    if (basePath == null) {
      throw new NullPointerException("basePath");
    }
    this.basePath = basePath;
  }

  @Override
  protected URL getURL(String name) {
    String path = basePath + "/" + name;
    return ResourceUtil.getResource(path);
  }
}
