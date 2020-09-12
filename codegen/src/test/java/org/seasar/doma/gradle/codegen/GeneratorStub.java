package org.seasar.doma.gradle.codegen;

import java.io.File;
import java.io.StringWriter;
import java.io.Writer;
import org.seasar.doma.gradle.codegen.desc.LanguageType;
import org.seasar.doma.gradle.codegen.generator.GenerationContext;
import org.seasar.doma.gradle.codegen.generator.Generator;

public class GeneratorStub extends Generator {

  private StringWriter writer = new StringWriter(300);

  public GeneratorStub() {
    super(LanguageType.JAVA);
  }

  public GeneratorStub(LanguageType languageType) {
    super(languageType);
  }

  @Override
  protected boolean exists(File file) {
    return false;
  }

  @Override
  protected void mkdirs(File dir) {}

  @Override
  protected Writer openWriter(GenerationContext context) {
    return writer;
  }

  protected String getResult() {
    return writer.toString();
  }

  protected void clear() {
    writer = new StringWriter(300);
  }
}
