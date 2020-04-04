package org.seasar.doma.gradle.codegen.generator;

import java.io.File;
import org.seasar.doma.gradle.codegen.exception.CodeGenNullPointerException;

public class GenerationContext {

  protected final Object model;

  protected final File file;

  protected final String templateName;

  protected final String encoding;

  protected final boolean overwrite;

  public GenerationContext(
      Object model, File file, String templateName, String encoding, boolean overwrite) {
    if (model == null) {
      throw new CodeGenNullPointerException("model");
    }
    if (file == null) {
      throw new CodeGenNullPointerException("file");
    }
    if (templateName == null) {
      throw new CodeGenNullPointerException("templateName");
    }
    if (encoding == null) {
      throw new CodeGenNullPointerException("fileEncoding");
    }
    this.model = model;
    this.file = file;
    this.templateName = templateName;
    this.encoding = encoding;
    this.overwrite = overwrite;
  }

  public Object getModel() {
    return model;
  }

  public File getFile() {
    return file;
  }

  public String getTemplateName() {
    return templateName;
  }

  public String getEncoding() {
    return encoding;
  }

  public boolean isOverwrite() {
    return overwrite;
  }
}
